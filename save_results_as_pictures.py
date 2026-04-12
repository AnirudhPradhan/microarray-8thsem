"""
Save 1D, 2D, and 3D clustering/triclustering results as pictures.
Reference: cursor_synthetic_dataset_implementationversion3.md
"""

import numpy as np
import pandas as pd
from pathlib import Path
import matplotlib.pyplot as plt
import matplotlib
matplotlib.use("Agg")  # Non-interactive backend for saving

BASE = Path(__file__).parent
OUT_1D_2D = BASE / "output" / "1d_2d"
IMG_DIR = BASE / "output" / "images"
QUALITYC_TSV = BASE / "data" / "benchmark" / "Benchmark_Datasets" / "QualityC" / "dataset_quality_constant_data.tsv"
QUALITYC_SLICE_DIR = BASE / "data" / "benchmark" / "Benchmark_Datasets" / "QualityC" / "TriGen"
YEAST_DIR = BASE / "algorithms" / "TrLab3.5" / "resources" / "0001"
TRILAB_QC_TRIQ = BASE / "algorithms" / "TrLab3.5" / "resources" / "synthetic_qualityc_run" / "synthetic_qualityc_run" / "synthetic_qualityc_run_triq.csv"
TRILAB_YEAST_TRIQ = BASE / "algorithms" / "TrLab3.5" / "resources" / "yeast_trigenpaper_run" / "yeast_trigenpaper_run" / "yeast_trigenpaper_run_triq.csv"


def load_qualityc_tensor():
    """Load QualityC as tensor."""
    if QUALITYC_TSV.exists():
        df = pd.read_csv(QUALITYC_TSV, sep="\t", index_col=0)
        n_genes, n_samples = len(df), 10
        n_times = df.shape[1] // n_samples
        return df.values.astype(float).reshape(n_genes, n_samples, n_times)
    if QUALITYC_SLICE_DIR.exists():
        slices = []
        for z in range(5):
            p = QUALITYC_SLICE_DIR / f"dataset_quality_constant_data_z{z}.csv"
            if not p.exists():
                break
            slices.append(pd.read_csv(p, header=None).values.astype(float))
        if slices:
            return np.stack(slices, axis=-1)
    return None


def load_yeast_tensor():
    """Load Yeast as tensor."""
    if not YEAST_DIR.exists():
        return None
    slices = []
    for t in range(14):
        p = YEAST_DIR / f"yeast_t{t}.csv"
        if not p.exists():
            break
        slices.append(pd.read_csv(p, sep=";", header=None).values.astype(float))
    return np.stack(slices, axis=-1) if slices else None


def plot_1d_results():
    """Save 1D clustering result pictures."""
    IMG_DIR.mkdir(exist_ok=True)

    for safe in ["qualityc", "yeast"]:
        sizes_csv = OUT_1D_2D / f"{safe}_1d_cluster_sizes.csv"
        if not sizes_csv.exists():
            continue

        df = pd.read_csv(sizes_csv)
        fig, ax = plt.subplots(figsize=(8, 5))
        x = np.arange(len(df))
        w = 0.35
        ax.bar(x - w / 2, df["KMeans_size"], w, label="K-Means", color="steelblue")
        ax.bar(x + w / 2, df["Agglomerative_size"], w, label="Agglomerative", color="coral")
        ax.set_xlabel("Cluster")
        ax.set_ylabel("Size (genes)")
        ax.set_title(f"1D Clustering – {safe.capitalize()}\nCluster sizes (K-Means vs Agglomerative)")
        ax.set_xticks(x)
        ax.set_xticklabels(df["Cluster"])
        ax.legend()
        ax.grid(axis="y", alpha=0.3)
        fig.tight_layout()
        fig.savefig(IMG_DIR / f"1d_cluster_sizes_{safe}.png", dpi=150, bbox_inches="tight")
        plt.close()

    # 1D TRIQ metrics (from 1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv)
    triq_csv = OUT_1D_2D / "1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv"
    if triq_csv.exists():
        df = pd.read_csv(triq_csv)
        for dname in ["QualityC", "Yeast"]:
            sub = df[(df["Dataset"] == dname) & (df["Algorithm"].str.startswith("1D"))]
            if sub.empty:
                continue
            safe = dname.replace(" ", "_").lower()
            fig, axes = plt.subplots(2, 2, figsize=(10, 8))
            metrics = ["TRIQ", "GRQ", "PEQ", "SPQ"]
            for ax, m in zip(axes.flat, metrics):
                for algo in sub["Algorithm"].unique():
                    s = sub[sub["Algorithm"] == algo]
                    ax.bar(range(len(s)), s[m], label=algo.replace("1D ", ""), alpha=0.8)
                ax.set_title(m)
                ax.set_ylabel(m)
                ax.legend()
            fig.suptitle(f"1D Metrics – {dname}")
            fig.tight_layout()
            fig.savefig(IMG_DIR / f"1d_metrics_{safe}.png", dpi=150, bbox_inches="tight")
            plt.close()

    print("  1D pictures saved.")


def plot_2d_results():
    """Save 2D biclustering result pictures."""
    IMG_DIR.mkdir(exist_ok=True)

    triq_csv = OUT_1D_2D / "1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv"
    if triq_csv.exists():
        df = pd.read_csv(triq_csv)
        for dname in ["QualityC", "Yeast"]:
            sub = df[(df["Dataset"] == dname) & (df["Algorithm"].str.startswith("2D"))]
            if sub.empty:
                continue
            safe = dname.replace(" ", "_").lower()
            fig, ax = plt.subplots(figsize=(10, 6))
            x = np.arange(len(sub))
            ax.bar(x - 0.2, sub["TRIQ"], 0.2, label="TRIQ", color="steelblue")
            ax.bar(x, sub["GRQ"], 0.2, label="GRQ", color="coral")
            ax.bar(x + 0.2, sub["PEQ"], 0.2, label="PEQ", color="seagreen")
            ax.bar(x + 0.4, sub["SPQ"], 0.2, label="SPQ", color="mediumpurple")
            ax.set_xlabel("Cocluster / Bicluster")
            ax.set_ylabel("Score")
            ax.set_title(f"2D Biclustering Metrics – {dname}")
            ax.set_xticks(x)
            ax.set_xticklabels(sub["Solution"], rotation=45, ha="right")
            ax.legend(ncol=4)
            ax.grid(axis="y", alpha=0.3)
            fig.tight_layout()
            fig.savefig(IMG_DIR / f"2d_metrics_{safe}.png", dpi=150, bbox_inches="tight")
            plt.close()

    # 2D heatmaps: load data and cocluster labels, reorder and plot
    for name, tensor_loader in [("qualityc", load_qualityc_tensor), ("yeast", load_yeast_tensor)]:
        tensor = tensor_loader()
        if tensor is None:
            continue
        matrix = tensor.reshape(tensor.shape[0], -1)
        npz = OUT_1D_2D / f"{name}_1d_2d_results.npz"
        if not npz.exists():
            continue
        data = np.load(npz, allow_pickle=True)
        rl, cl = data["cocluster_row_labels"], data["cocluster_col_labels"]
        if rl is None:
            continue
        row_order = np.argsort(rl)
        col_order = np.argsort(cl)
        M = matrix[row_order][:, col_order]
        # Subsample for visualization if too large
        max_rows, max_cols = 300, 80
        if M.shape[0] > max_rows:
            step = M.shape[0] // max_rows
            M = M[::step]
        if M.shape[1] > max_cols:
            step = M.shape[1] // max_cols
            M = M[:, ::step]
        fig, ax = plt.subplots(figsize=(8, 6))
        im = ax.imshow(M, aspect="auto", cmap="RdYlBu_r")
        ax.set_title(f"2D Cocluster Arrangement – {name.capitalize()}\n(rows/cols reordered by cluster)")
        ax.set_xlabel("Features (ordered by column cluster)")
        ax.set_ylabel("Genes (ordered by row cluster)")
        plt.colorbar(im, ax=ax)
        fig.tight_layout()
        fig.savefig(IMG_DIR / f"2d_heatmap_{name}.png", dpi=150, bbox_inches="tight")
        plt.close()

    print("  2D pictures saved.")


def plot_3d_results():
    """Save 3D triclustering (TriGen) result pictures."""
    IMG_DIR.mkdir(exist_ok=True)

    def parse_trilab_triq(path):
        """Parse TrLab TRIQ CSV (semicolon-separated).
        QualityC: SOLUTION;TRIQ;GRQ;PEQ;SPQ
        Yeast: SOLUTION;TRIQ;BIOQ;TRIQN;BIOQN;GRQ;PEQ;SPQ
        """
        if not path.exists():
            return None
        lines = path.read_text().strip().split("\n")
        data = []
        in_section = False
        has_bioq = False  # Yeast format has BIOQ columns
        for line in lines:
            if line.startswith("SOLUTION;TRIQ"):
                has_bioq = "BIOQ" in line
                in_section = True
                continue
            if not in_section or not line.strip():
                continue
            parts = line.replace(",", ".").split(";")
            if len(parts) >= 2:
                try:
                    row = {"solution": parts[0], "TRIQ": float(parts[1])}
                    if has_bioq and len(parts) >= 8:
                        row["GRQ"] = float(parts[5])
                        row["PEQ"] = float(parts[6])
                        row["SPQ"] = float(parts[7])
                    elif len(parts) >= 5:
                        row["GRQ"] = float(parts[2])
                        row["PEQ"] = float(parts[3])
                        row["SPQ"] = float(parts[4])
                    else:
                        row["GRQ"] = row["PEQ"] = row["SPQ"] = 0
                    data.append(row)
                except (ValueError, IndexError):
                    pass
        return pd.DataFrame(data) if data else None

    for path, label in [
        (TRILAB_QC_TRIQ, "QualityC (Base paper)"),
        (TRILAB_YEAST_TRIQ, "Yeast (TriGen paper)"),
    ]:
        df = parse_trilab_triq(path)
        if df is None or df.empty:
            continue
        safe = "qualityc" if "qualityc" in str(path).lower() else "yeast"
        fig, axes = plt.subplots(2, 2, figsize=(10, 8))
        metrics = ["TRIQ", "GRQ", "PEQ", "SPQ"]
        for ax, m in zip(axes.flat, metrics):
            ax.bar(df["solution"], df[m], color="steelblue", edgecolor="navy")
            ax.set_ylabel(m)
            ax.set_title(f"3D Tricluster {m}")
            ax.tick_params(axis="x", rotation=45)
        fig.suptitle(f"3D TriGen – {label}")
        fig.tight_layout()
        fig.savefig(IMG_DIR / f"3d_metrics_{safe}.png", dpi=150, bbox_inches="tight")
        plt.close()

        # Single combined bar chart
        fig, ax = plt.subplots(figsize=(8, 5))
        x = np.arange(len(df))
        w = 0.2
        ax.bar(x - 1.5 * w, df["TRIQ"], w, label="TRIQ")
        ax.bar(x - 0.5 * w, df["GRQ"], w, label="GRQ")
        ax.bar(x + 0.5 * w, df["PEQ"], w, label="PEQ")
        ax.bar(x + 1.5 * w, df["SPQ"], w, label="SPQ")
        ax.set_xticks(x)
        ax.set_xticklabels(df["solution"])
        ax.set_ylabel("Score")
        ax.set_title(f"3D TriGen – {label}")
        ax.legend()
        ax.grid(axis="y", alpha=0.3)
        fig.tight_layout()
        fig.savefig(IMG_DIR / f"3d_triq_bar_{safe}.png", dpi=150, bbox_inches="tight")
        plt.close()

    print("  3D pictures saved.")


def plot_comparison_summary():
    """Save a summary comparison figure (1D vs 2D vs 3D)."""
    IMG_DIR.mkdir(exist_ok=True)

    fig, axes = plt.subplots(1, 3, figsize=(14, 5))
    triq_csv = OUT_1D_2D / "1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv"
    df = pd.read_csv(triq_csv) if triq_csv.exists() else None

    # 1D: average TRIQ by algorithm (from CSV)
    if df is not None:
        sub1d = df[df["Algorithm"].str.startswith("1D")]
        if not sub1d.empty:
            means = sub1d.groupby("Algorithm")["TRIQ"].mean()
            axes[0].bar(means.index.str.replace("1D ", ""), means.values, color=["steelblue", "coral"])
            axes[0].set_title("1D (Average TRIQ)")
            axes[0].tick_params(axis="x", rotation=30)

    # 2D: average TRIQ by algorithm
    if df is not None:
        sub2d = df[df["Algorithm"].str.startswith("2D")]
        if not sub2d.empty:
            means = sub2d.groupby("Algorithm")["TRIQ"].mean()
            axes[1].bar(means.index.str.replace("2D ", ""), means.values, color=["seagreen", "mediumpurple"])
            axes[1].set_title("2D (Average TRIQ)")
            axes[1].tick_params(axis="x", rotation=30)

    # 3D: TRIQ from TrLab
    def get_3d_triq(path):
        if not path.exists():
            return []
        lines = path.read_text().strip().split("\n")
        vals = []
        for line in lines:
            if line.startswith("TRI_{") and ";" in line:
                try:
                    vals.append(float(line.split(";")[1]))
                except (ValueError, IndexError):
                    pass
        return vals

    qc_vals = get_3d_triq(TRILAB_QC_TRIQ)
    yeast_vals = get_3d_triq(TRILAB_YEAST_TRIQ)
    if qc_vals or yeast_vals:
        labels, vals = [], []
        if qc_vals:
            labels.append("QualityC\n(3D TriGen)")
            vals.append(np.mean(qc_vals))
        if yeast_vals:
            labels.append("Yeast\n(3D TriGen)")
            vals.append(np.mean(yeast_vals))
        axes[2].bar(labels, vals, color=["steelblue", "coral"])
        axes[2].set_title("3D (Mean TRIQ)")

    fig.suptitle("1D / 2D / 3D Results Comparison")
    fig.tight_layout()
    fig.savefig(IMG_DIR / "comparison_1d_2d_3d.png", dpi=150, bbox_inches="tight")
    plt.close()
    print("  Comparison summary saved.")


def main():
    IMG_DIR.mkdir(exist_ok=True)
    print("Saving 1D, 2D, 3D results as pictures...")
    print(f"Output: {IMG_DIR}")
    plot_1d_results()
    plot_2d_results()
    plot_3d_results()
    plot_comparison_summary()
    print(f"\nDone. Images saved to {IMG_DIR}")


if __name__ == "__main__":
    main()
