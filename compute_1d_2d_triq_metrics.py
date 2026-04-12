"""
Compute TRIQ, GRQ, PEQ, SPQ for 1D and 2D clustering results.
Adapted from 3D triclustering metrics (TrLab/TriGen base paper).
"""

import numpy as np
import pandas as pd
from pathlib import Path
from scipy.stats import pearsonr, spearmanr
from sklearn.cluster import KMeans, AgglomerativeClustering
from sklearn.cluster import SpectralBiclustering, SpectralCoclustering
from sklearn.preprocessing import StandardScaler

BASE = Path(__file__).parent
QUALITYC_TSV = BASE / "data" / "benchmark" / "Benchmark_Datasets" / "QualityC" / "dataset_quality_constant_data.tsv"
QUALITYC_SLICE_DIR = BASE / "data" / "benchmark" / "Benchmark_Datasets" / "QualityC" / "TriGen"
YEAST_DIR = BASE / "algorithms" / "TrLab3.5" / "resources" / "0001"
OUT_DIR = BASE / "output" / "1d_2d"

# TriGen weights (without BIOQ)
W_GRQ, W_PEQ, W_SPQ = 0.4, 0.05, 0.05


def load_qualityc():
    df = pd.read_csv(QUALITYC_TSV, sep="\t", index_col=0)
    n_genes, n_samples = len(df), 10
    n_times = df.shape[1] // n_samples
    arr = df.values.astype(float).reshape(n_genes, n_samples, n_times)
    return arr.reshape(n_genes, -1)


def load_qualityc_from_slices():
    if not QUALITYC_SLICE_DIR.exists():
        return None
    slices = [pd.read_csv(QUALITYC_SLICE_DIR / f"dataset_quality_constant_data_z{z}.csv", header=None).values.astype(float)
              for z in range(5) if (QUALITYC_SLICE_DIR / f"dataset_quality_constant_data_z{z}.csv").exists()]
    if not slices:
        return None
    return np.stack(slices, axis=-1).reshape(slices[0].shape[0], -1)


def load_yeast():
    if not YEAST_DIR.exists():
        return None
    slices = []
    for t in range(14):
        p = YEAST_DIR / f"yeast_t{t}.csv"
        if not p.exists():
            break
        slices.append(pd.read_csv(p, sep=";", header=None).values.astype(float))
    if not slices:
        return None
    return np.stack(slices, axis=-1).reshape(slices[0].shape[0], -1)


def grq_submatrix(X: np.ndarray) -> float:
    """GRQ adapted for 2D: 1 - normalized MSR. Higher = more coherent."""
    if X.size < 4:
        return 0.0
    row_means = X.mean(axis=1, keepdims=True)
    col_means = X.mean(axis=0, keepdims=True)
    grand = X.mean()
    residue = X - row_means - col_means + grand
    msr = np.mean(residue ** 2)
    var_x = np.var(X) + 1e-10
    grq = max(0.0, 1.0 - msr / var_x)
    return float(grq)


def peq_submatrix(X: np.ndarray) -> float:
    """PEQ: mean of upper-diagonal |Pearson correlations| between rows (genes)."""
    if X.shape[0] < 2:
        return 0.0
    cor = np.corrcoef(X)
    if cor.size < 4:
        return 0.0
    triu = np.triu_indices(cor.shape[0], k=1)
    vals = np.abs(cor[triu])
    vals = np.nan_to_num(vals, nan=0.0)
    return float(np.mean(vals))


def spq_submatrix(X: np.ndarray) -> float:
    """SPQ: mean of upper-diagonal |Spearman correlations| between rows."""
    if X.shape[0] < 2:
        return 0.0
    try:
        res = spearmanr(X, axis=1)
        cor = res.statistic if hasattr(res, "statistic") else res[0]
        cor = np.asarray(cor)
        if cor.ndim == 0:
            return float(abs(cor))
        triu = np.triu_indices(cor.shape[0], k=1)
        vals = np.abs(cor[triu])
        vals = np.nan_to_num(vals, nan=0.0)
        return float(np.mean(vals)) if vals.size > 0 else 0.0
    except Exception:
        return 0.0


def triq_from_components(grq: float, peq: float, spq: float) -> float:
    return (W_GRQ * grq + W_PEQ * peq + W_SPQ * spq) / (W_GRQ + W_PEQ + W_SPQ)


def metrics_for_submatrix(X: np.ndarray) -> dict:
    grq = grq_submatrix(X)
    peq = peq_submatrix(X)
    spq = spq_submatrix(X)
    triq = triq_from_components(grq, peq, spq)
    return {"TRIQ": triq, "GRQ": grq, "PEQ": peq, "SPQ": spq}


def _make_nonnegative(X):
    xmin = X.min()
    return X - xmin + 1e-6 if xmin < 0 else X


def run_and_compute_all():
    OUT_DIR.mkdir(exist_ok=True)
    datasets = []
    if QUALITYC_TSV.exists():
        datasets.append(("QualityC", load_qualityc(), 5))
    elif load_qualityc_from_slices() is not None:
        datasets.append(("QualityC", load_qualityc_from_slices(), 5))
    if load_yeast() is not None:
        datasets.append(("Yeast", load_yeast(), 5))

    all_results = []

    for dname, matrix, n_clusters in datasets:
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(matrix)
        X_bic = _make_nonnegative(matrix.copy())

        # 1D: K-Means
        km = KMeans(n_clusters=n_clusters, random_state=42, n_init=10)
        km_labels = km.fit_predict(X_scaled)
        km_metrics = []
        for k in range(n_clusters):
            mask = km_labels == k
            block = matrix[mask]
            m = metrics_for_submatrix(block)
            km_metrics.append({**m, "Solution": f"KMeans {{k+1}}", "Algorithm": "1D K-Means", "Dataset": dname})
            km_metrics[-1]["Solution"] = f"KMeans {{{k+1}}}"
        all_results.extend(km_metrics)

        # 1D: Agglomerative
        agg = AgglomerativeClustering(n_clusters=n_clusters)
        agg_labels = agg.fit_predict(X_scaled)
        for k in range(n_clusters):
            block = matrix[agg_labels == k]
            m = metrics_for_submatrix(block)
            all_results.append({**m, "Solution": f"Agglomerative {{{k+1}}}", "Algorithm": "1D Agglomerative", "Dataset": dname})

        # 2D: SpectralCoclustering – each (row_cluster, col_cluster) block
        coc = SpectralCoclustering(n_clusters=n_clusters, random_state=42)
        coc.fit(X_bic)
        rl, cl = coc.row_labels_, coc.column_labels_
        for kr in range(n_clusters):
            for kc in range(n_clusters):
                mask_r = rl == kr
                mask_c = cl == kc
                if mask_r.sum() > 0 and mask_c.sum() > 0:
                    block = matrix[np.ix_(mask_r, mask_c)]
                    m = metrics_for_submatrix(block)
                    bid = kr * n_clusters + kc + 1
                    all_results.append({**m, "Solution": f"Cocluster {{{bid}}}", "Algorithm": "2D SpectralCoclustering", "Dataset": dname})

        # 2D: SpectralBiclustering – top 5 biclusters (one per row/col block pair)
        bic = SpectralBiclustering(n_clusters=(n_clusters, n_clusters), random_state=42, n_init=3)
        bic.fit(X_bic)
        for i in range(min(5, bic.biclusters_[0].shape[0])):
            row_mask = bic.biclusters_[0][i]
            col_mask = bic.biclusters_[1][i]
            if row_mask.sum() > 0 and col_mask.sum() > 0:
                block = matrix[np.ix_(row_mask, col_mask)]
                m = metrics_for_submatrix(block)
                all_results.append({**m, "Solution": f"Bicluster {{{i+1}}}", "Algorithm": "2D SpectralBiclustering", "Dataset": dname})

    return all_results, datasets


def format_table_like_image(rows: list, algo: str, dname: str) -> str:
    """Format as Solution | TRIQ | GRQ | PEQ | SPQ table."""
    lines = [f"\n## {algo} – {dname}", ""]
    lines.append("| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |")
    lines.append("| :------- | :---- | :---- | :---- | :---- |")
    for r in rows:
        s = r["Solution"]
        triq = r["TRIQ"]
        grq = r["GRQ"]
        peq = r["PEQ"]
        spq = r["SPQ"]
        lines.append(f"| {s}  | {triq:.3f} | {grq:.3f} | {peq:.3f} | {spq:.3f} |")
    return "\n".join(lines)


def main():
    print("Computing TRIQ, GRQ, PEQ, SPQ for 1D and 2D...")
    results, datasets = run_and_compute_all()

    df = pd.DataFrame(results)
    df.to_csv(OUT_DIR / "1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv", index=False)

    report = [
        "# TRIQ, GRQ, PEQ, SPQ for 1D and 2D Clustering",
        "",
        "Adapted from 3D triclustering metrics. Each cluster/bicluster block is evaluated.",
        "",
    ]

    for dname in df["Dataset"].unique():
        for algo in ["1D K-Means", "1D Agglomerative", "2D SpectralCoclustering", "2D SpectralBiclustering"]:
            sub = df[(df["Dataset"] == dname) & (df["Algorithm"] == algo)]
            if len(sub) > 0:
                report.append(format_table_like_image(sub.to_dict("records"), algo, dname))

    (OUT_DIR / "1d_2d_TRIQ_metrics_report.md").write_text("\n".join(report))
    print(f"Saved: {OUT_DIR / '1d_2d_TRIQ_metrics_report.md'}")
    print(f"Saved: {OUT_DIR / '1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv'}")


if __name__ == "__main__":
    main()
