"""
1D and 2D analysis on Benchmark_Datasets (synthetic QualityC) and Yeast (Spellman)
using scikit-learn. Aligned with Final Year Report (1D: K-Means, Hierarchical; 2D: Biclustering).
"""

import numpy as np
import pandas as pd
from pathlib import Path
from sklearn.cluster import KMeans, AgglomerativeClustering
from sklearn.cluster import SpectralBiclustering, SpectralCoclustering
from sklearn.preprocessing import StandardScaler

BASE = Path(__file__).parent

# --- Dataset paths ---
QUALITYC_TSV = BASE / "data" / "benchmark" / "Benchmark_Datasets" / "QualityC" / "dataset_quality_constant_data.tsv"
QUALITYC_SLICE_DIR = BASE / "data" / "benchmark" / "Benchmark_Datasets" / "QualityC" / "TriGen"
YEAST_DIR = BASE / "algorithms" / "TrLab3.5" / "resources" / "0001"
OUT_DIR = BASE / "output" / "1d_2d"


def load_qualityc() -> np.ndarray:
    """Load QualityC TSV -> tensor (genes, samples, times)."""
    df = pd.read_csv(QUALITYC_TSV, sep="\t", index_col=0)
    # Columns: y0z0, y1z0, ... y9z4 -> 10 samples, 5 times (z0..z4)
    n_genes = len(df)
    n_samples = 10
    n_times = df.shape[1] // n_samples
    arr = df.values.astype(float)
    tensor = arr.reshape(n_genes, n_samples, n_times)
    return tensor


def load_qualityc_from_slices() -> np.ndarray:
    """Load QualityC from TriGen z-slice CSVs (comma-separated)."""
    if not QUALITYC_SLICE_DIR.exists():
        return None
    slices = []
    for z in range(5):
        p = QUALITYC_SLICE_DIR / f"dataset_quality_constant_data_z{z}.csv"
        if not p.exists():
            break
        s = pd.read_csv(p, header=None).values.astype(float)
        slices.append(s)
    if not slices:
        return None
    return np.stack(slices, axis=-1)  # genes, samples, times


def load_yeast() -> np.ndarray:
    """Load yeast from TrLab z-slice CSVs (semicolon-separated)."""
    if not YEAST_DIR.exists():
        return None
    slices = []
    for t in range(14):
        p = YEAST_DIR / f"yeast_t{t}.csv"
        if not p.exists():
            break
        df = pd.read_csv(p, sep=";", header=None)
        slices.append(df.values.astype(float))
    if not slices:
        return None
    # each slice: genes x samples -> stack on axis 2
    return np.stack(slices, axis=-1)


def tensor_to_1d(tensor: np.ndarray) -> np.ndarray:
    """Flatten (genes, samples, times) -> (genes, samples*times)."""
    n_genes, n_samples, n_times = tensor.shape
    return tensor.reshape(n_genes, -1)


def tensor_to_2d(tensor: np.ndarray) -> np.ndarray:
    """Same as 1D for biclustering: (genes, samples*times)."""
    return tensor_to_1d(tensor)


def _make_nonnegative(X: np.ndarray) -> np.ndarray:
    """Shift to non-negative for spectral biclustering."""
    xmin = X.min()
    if xmin < 0:
        X = X - xmin + 1e-6
    return X


def run_1d(matrix: np.ndarray, name: str, n_clusters: int = 5, random_state: int = 42):
    """1D: K-Means and Hierarchical (Agglomerative) clustering on genes."""
    scaler = StandardScaler()
    X = scaler.fit_transform(matrix)

    print(f"\n--- 1D Clustering ({name}) ---")
    print(f"  Matrix shape: {matrix.shape} (genes x features)")

    # K-Means
    km = KMeans(n_clusters=n_clusters, random_state=random_state, n_init=10)
    km_labels = km.fit_predict(X)
    print(f"  K-Means: {n_clusters} clusters, inertia={km.inertia_:.2f}")

    # Hierarchical (Agglomerative)
    agg = AgglomerativeClustering(n_clusters=n_clusters)
    agg_labels = agg.fit_predict(X)
    print(f"  AgglomerativeClustering: {n_clusters} clusters")

    return {"kmeans_labels": km_labels, "agg_labels": agg_labels}


def run_2d(matrix: np.ndarray, name: str, n_clusters: int = 5, random_state: int = 42):
    """2D: Spectral Biclustering and Spectral Coclustering."""
    X = matrix.copy()
    X = _make_nonnegative(X)

    print(f"\n--- 2D Biclustering ({name}) ---")
    print(f"  Matrix shape: {matrix.shape}")

    results = {}

    # SpectralCoclustering - partitions rows and columns
    try:
        coc = SpectralCoclustering(n_clusters=n_clusters, random_state=random_state)
        coc.fit(X)
        row_labels = coc.row_labels_
        col_labels = coc.column_labels_
        results["cocluster_row_labels"] = row_labels
        results["cocluster_col_labels"] = col_labels
        print(f"  SpectralCoclustering: {n_clusters} row clusters, {n_clusters} col clusters")
    except Exception as e:
        print(f"  SpectralCoclustering failed: {e}")
        results["cocluster_row_labels"] = None
        results["cocluster_col_labels"] = None

    # SpectralBiclustering - allows overlapping biclusters
    try:
        bic = SpectralBiclustering(
            n_clusters=(n_clusters, n_clusters),
            random_state=random_state,
            n_init=3,
        )
        bic.fit(X)
        results["bicluster"] = bic
        print(f"  SpectralBiclustering: fitted, biclusters_ shape {bic.biclusters_[0].shape}")
    except Exception as e:
        print(f"  SpectralBiclustering failed: {e}")
        results["bicluster"] = None

    return results


def main():
    OUT_DIR.mkdir(exist_ok=True)
    print("=== 1D and 2D Analysis (sklearn) on Base Paper + TriGen Datasets ===\n")

    datasets = []

    # 1. QualityC (Base paper synthetic)
    tensor_qc = load_qualityc()
    if tensor_qc is not None:
        matrix_qc = tensor_to_1d(tensor_qc)
        datasets.append(("QualityC (Base paper)", matrix_qc, min(5, matrix_qc.shape[0] // 50)))
    else:
        tensor_qc = load_qualityc_from_slices()
        if tensor_qc is not None:
            matrix_qc = tensor_to_1d(tensor_qc)
            datasets.append(("QualityC (Base paper)", matrix_qc, 5))

    # 2. Yeast (TriGen paper)
    tensor_yeast = load_yeast()
    if tensor_yeast is not None:
        matrix_yeast = tensor_to_1d(tensor_yeast)
        datasets.append(("Yeast (TriGen paper)", matrix_yeast, 5))

    if not datasets:
        print("No datasets found. Check paths:")
        print(f"  QualityC TSV: {QUALITYC_TSV}")
        print(f"  QualityC slices: {QUALITYC_SLICE_DIR}")
        print(f"  Yeast: {YEAST_DIR}")
        return

    for name, matrix, n_clusters in datasets:
        print(f"\n{'='*60}")
        print(f"Dataset: {name} | shape={matrix.shape} | n_clusters={n_clusters}")
        print("=" * 60)

        r1 = run_1d(matrix, name, n_clusters=n_clusters)
        r2 = run_2d(matrix, name, n_clusters=n_clusters)

        # Save labels and write output tables
        safe_name = name.split("(")[0].strip().lower().replace(" ", "_")

        # 1D outputs: cluster labels + size summary
        df_1d = pd.DataFrame({
            "gene_idx": np.arange(len(r1["kmeans_labels"])),
            "KMeans_cluster": r1["kmeans_labels"],
            "Agglomerative_cluster": r1["agg_labels"],
        })
        df_1d.to_csv(OUT_DIR / f"{safe_name}_1d_cluster_assignments.csv", index=False)

        km_sizes = np.bincount(r1["kmeans_labels"])
        agg_sizes = np.bincount(r1["agg_labels"])
        pd.DataFrame({
            "Cluster": np.arange(n_clusters),
            "KMeans_size": km_sizes,
            "Agglomerative_size": agg_sizes,
        }).to_csv(OUT_DIR / f"{safe_name}_1d_cluster_sizes.csv", index=False)

        # 2D outputs: row/col labels + bicluster info
        if r2.get("cocluster_row_labels") is not None:
            df_2d_coc = pd.DataFrame({
                "row_idx": np.arange(len(r2["cocluster_row_labels"])),
                "row_cluster": r2["cocluster_row_labels"],
            })
            df_2d_coc.to_csv(OUT_DIR / f"{safe_name}_2d_cocluster_row_labels.csv", index=False)
            coc_row_sizes = np.bincount(r2["cocluster_row_labels"])
            coc_col_sizes = np.bincount(r2["cocluster_col_labels"])
            pd.DataFrame({
                "Cluster": np.arange(n_clusters),
                "Cocluster_rows": coc_row_sizes,
                "Cocluster_cols": coc_col_sizes,
            }).to_csv(OUT_DIR / f"{safe_name}_2d_cocluster_sizes.csv", index=False)

        if r2.get("bicluster") is not None:
            bic = r2["bicluster"]
            bic_data = []
            for i in range(bic.biclusters_[0].shape[0]):
                row_mask = bic.biclusters_[0][i]
                col_mask = bic.biclusters_[1][i]
                bic_data.append({
                    "bicluster": i + 1,
                    "n_rows": row_mask.sum(),
                    "n_cols": col_mask.sum(),
                    "volume": row_mask.sum() * col_mask.sum(),
                })
            pd.DataFrame(bic_data).to_csv(OUT_DIR / f"{safe_name}_2d_spectral_bicluster_sizes.csv", index=False)

        np.savez(
            OUT_DIR / f"{safe_name}_1d_2d_results.npz",
            kmeans_labels=r1["kmeans_labels"],
            agg_labels=r1["agg_labels"],
            cocluster_row_labels=r2.get("cocluster_row_labels"),
            cocluster_col_labels=r2.get("cocluster_col_labels"),
        )

    # Write summary report
    _write_summary_report(OUT_DIR, datasets)
    print(f"\n--- Done. Outputs saved to {OUT_DIR} ---")


def _write_summary_report(out_dir: Path, datasets: list):
    """Write markdown summary of all 1D and 2D results."""
    lines = [
        "# 1D and 2D Analysis Output – Both Datasets",
        "",
        "## Datasets",
        "1. **QualityC** (Base paper synthetic) – 500 genes × 50 features",
        "2. **Yeast** (TriGen paper) – 6178 genes × 56 features",
        "",
        "---",
        "",
    ]
    for name, matrix, n_clusters in datasets:
        safe = name.split("(")[0].strip().lower().replace(" ", "_")
        lines.append(f"## {name}")
        lines.append("")

        # 1D
        km_sizes = np.load(out_dir / f"{safe}_1d_2d_results.npz", allow_pickle=True)
        km_l = km_sizes["kmeans_labels"]
        agg_l = km_sizes["agg_labels"]
        km_s = np.bincount(km_l)
        agg_s = np.bincount(agg_l)

        lines.append("### 1D Clustering")
        lines.append("| Algorithm | Cluster sizes |")
        lines.append("|-----------|---------------|")
        lines.append(f"| **K-Means** | {list(km_s)} |")
        lines.append(f"| **AgglomerativeClustering** | {list(agg_s)} |")
        lines.append("")

        # 2D
        try:
            d = np.load(out_dir / f"{safe}_1d_2d_results.npz", allow_pickle=True)
            rl, cl = d["cocluster_row_labels"], d["cocluster_col_labels"]
            if rl is not None:
                r_s = np.bincount(rl)
                c_s = np.bincount(cl)
                lines.append("### 2D Biclustering")
                lines.append("| Algorithm | Row cluster sizes | Col cluster sizes |")
                lines.append("|-----------|-------------------|-------------------|")
                lines.append(f"| **SpectralCoclustering** | {list(r_s)} | {list(c_s)} |")
        except Exception:
            pass

        bic_csv = out_dir / f"{safe}_2d_spectral_bicluster_sizes.csv"
        if bic_csv.exists():
            df = pd.read_csv(bic_csv)
            lines.append("")
            lines.append("| **SpectralBiclustering** | Bicluster | Rows | Cols | Volume |")
            lines.append("|--------------------------|-----------|------|------|--------|")
            for _, row in df.iterrows():
                lines.append(f"| | {int(row['bicluster'])} | {int(row['n_rows'])} | {int(row['n_cols'])} | {int(row['volume'])} |")

        lines.append("")
        lines.append("---")
        lines.append("")

    (out_dir / "SUMMARY_1d_2d_output.md").write_text("\n".join(lines))


if __name__ == "__main__":
    main()
