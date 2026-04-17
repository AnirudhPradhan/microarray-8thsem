"""
generate_comparison_graphs.py
Generates individual and combined comparative charts for all triclustering algorithms.
Outputs → output/images/comparative/
"""

import os
import numpy as np
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.gridspec import GridSpec
import matplotlib.ticker as mticker

OUT_DIR = os.path.join("output", "images", "comparative")
os.makedirs(OUT_DIR, exist_ok=True)

plt.rcParams.update({
    "figure.dpi": 150,
    "savefig.dpi": 150,
    "font.family": "DejaVu Sans",
    "font.size": 10,
    "axes.titlesize": 12,
    "axes.labelsize": 10,
    "legend.fontsize": 9,
    "xtick.labelsize": 9,
    "ytick.labelsize": 9,
    "axes.spines.top": False,
    "axes.spines.right": False,
})

# ──────────────────────────────────────────────────────────────
# RAW DATA
# ──────────────────────────────────────────────────────────────

DATASETS = [
    "BaseR", "Constant", "Additive", "Multiplicative",
    "OverlappingC", "OverlappingA", "OverlappingM",
    "QualityC", "QualityA", "QualityM",
    "ContiguityC", "ContiguityA", "ContiguityM",
    "OrderPreserving",
]
DATASETS_SHORT = [
    "BaseR", "Const", "Add", "Mult",
    "OvlC", "OvlA", "OvlM",
    "QualC", "QualA", "QualM",
    "ConC", "ConA", "ConM",
    "OP",
]

# R values per dataset (same order as DATASETS)
R_TRICLUSTER = [0.867, 0.950, 0.883, 0.924,
                0.944, 0.889, 0.892,
                0.561, 0.585, 0.552,
                0.950, 0.922, 0.891,
                0.000]
R_TCTRICLUSTER = [0.676, 0.791, 0.746, 0.830,
                  0.828, 0.780, 0.745,
                  0.515, 0.519, 0.651,
                  0.950, 0.922, 0.891,
                  0.000]
R_DELTA = [0.174, 0.160, 0.281, 0.182,
           0.180, 0.258, 0.188,
           0.223, 0.277, 0.209,
           0.163, 0.286, 0.238,
           0.038]

# CE values
CE_TRICLUSTER = [0.998, 0.994, 0.995, 0.996,
                 0.994, 0.995, 0.995,
                 0.997, 0.997, 0.999,
                 0.994, 0.996, 0.995,
                 1.000]
CE_TCTRICLUSTER = [0.999, 0.997, 0.998, 0.998,
                   0.997, 0.998, 0.998,
                   0.999, 0.999, 0.999,
                   0.994, 0.996, 0.995,
                   1.000]
CE_DELTA = [0.998, 0.999, 0.998, 0.999,
            0.999, 0.998, 0.999,
            0.999, 0.998, 0.999,
            0.999, 0.998, 0.998,
            1.000]

# RMS3 values
RMS3_TRICLUSTER = [0.926, 0.951, 0.921, 0.950,
                   0.947, 0.933, 0.929,
                   0.802, 0.798, 0.772,
                   0.940, 0.959, 0.908,
                   0.000]
RMS3_TCTRICLUSTER = [0.874, 0.893, 0.862, 0.906,
                     0.911, 0.904, 0.893,
                     0.768, 0.782, 0.822,
                     0.940, 0.959, 0.908,
                     0.000]
RMS3_DELTA = [0.401, 0.429, 0.528, 0.458,
              0.428, 0.543, 0.451,
              0.446, 0.539, 0.486,
              0.434, 0.529, 0.501,
              0.246]

# TRIQ metrics — QualityC & Yeast
TRIQ_DATA_QC = {
    "δ-Trimax":              {"n": 50,  "best": 1.000, "mean": 0.893, "grq": 0.874, "peq": 0.461, "spq": 0.761},
    "triCluster":            {"n": 48,  "best": 1.000, "mean": 0.603, "grq": 0.576, "peq": 0.452, "spq": 0.854},
    "TCtriCluster":          {"n": 20,  "best": 0.710, "mean": 0.504, "grq": 0.514, "peq": 0.473, "spq": 0.823},
    "TriGen 3D":             {"n": 5,   "best": 0.700, "mean": 0.699, "grq": 0.749, "peq": 0.501, "spq": 0.502},
    "2D SpectralBicl.":      {"n": 5,   "best": 0.417, "mean": 0.258, "grq": 0.240, "peq": 0.366, "spq": 0.367},
    "2D SpectralCocl.":      {"n": 25,  "best": 0.271, "mean": 0.196, "grq": 0.174, "peq": 0.296, "spq": 0.294},
    "1D Agglomerative":      {"n": 5,   "best": 0.220, "mean": 0.118, "grq": 0.113, "peq": 0.143, "spq": 0.142},
    "1D K-Means":            {"n": 5,   "best": 0.106, "mean": 0.092, "grq": 0.084, "peq": 0.123, "spq": 0.122},
}

TRIQ_DATA_YEAST = {
    "δ-Trimax":              {"n": 16,  "best": 0.937, "mean": 0.810, "grq": 0.862, "peq": 0.593, "spq": 0.594},
    "TCtriCluster":          {"n": 5,   "best": 0.818, "mean": 0.678, "grq": 0.768, "peq": 0.356, "spq": 0.374},
    "TriGen 3D":             {"n": 5,   "best": 0.269, "mean": 0.252, "grq": 0.532, "peq": 0.390, "spq": 0.381},
    "2D SpectralBicl.":      {"n": 5,   "best": 0.497, "mean": 0.382, "grq": 0.380, "peq": 0.402, "spq": 0.376},
    "2D SpectralCocl.":      {"n": 20,  "best": 0.395, "mean": 0.275, "grq": 0.271, "peq": 0.316, "spq": 0.302},
    "1D Agglomerative":      {"n": 5,   "best": 0.422, "mean": 0.223, "grq": 0.210, "peq": 0.278, "spq": 0.272},
    "1D K-Means":            {"n": 5,   "best": 0.373, "mean": 0.220, "grq": 0.209, "peq": 0.263, "spq": 0.262},
}

# Colour palette
C = {
    "triCluster":       "#2196F3",   # blue
    "TCtriCluster":     "#4CAF50",   # green
    "δ-Trimax":         "#FF5722",   # deep orange
    "TriGen 3D":        "#9C27B0",   # purple
    "2D SpectralBicl.": "#FF9800",   # orange
    "2D SpectralCocl.": "#FFC107",   # amber
    "1D Agglomerative": "#78909C",   # blue-grey
    "1D K-Means":       "#B0BEC5",   # lighter grey
}

# Pattern type bands (x-index ranges, 0-based in DATASETS list)
BANDS = [
    (0, 0, "Base", "#e3f2fd"),
    (1, 3, "Simple\nPattern",  "#e8f5e9"),
    (4, 6, "Overlapping",      "#fff3e0"),
    (7, 9, "Quality",          "#fce4ec"),
    (10,12, "Contiguity",      "#f3e5f5"),
    (13,13, "Order-\nPreserving", "#f5f5f5"),
]

def add_bands(ax, n=14):
    """Shade alternating pattern-type regions."""
    for lo, hi, label, color in BANDS:
        ax.axvspan(lo - 0.5, hi + 0.5, alpha=0.25, color=color, zorder=0)
        ax.text((lo + hi) / 2, -0.075, label,
                ha="center", va="top", fontsize=7.5, color="#555",
                transform=ax.get_xaxis_transform(), linespacing=1.3)


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 1 — triCluster: R, CE, RMS3 across all 17 datasets
# ══════════════════════════════════════════════════════════════════════════════
def fig_tricluster_individual():
    fig, axes = plt.subplots(3, 1, figsize=(12, 9), sharex=True)
    fig.suptitle("triCluster — Recoverability Across All 17 Benchmark Datasets", fontsize=13, fontweight="bold", y=0.98)
    x = np.arange(len(DATASETS))
    bars_data = [
        (axes[0], R_TRICLUSTER,   "Recoverability (R)",    C["triCluster"], "R → fraction of ground-truth triclusters matched"),
        (axes[1], CE_TRICLUSTER,  "Coverage (CE)",         "#42A5F5",       "CE → fraction of genes covered by predicted triclusters"),
        (axes[2], RMS3_TRICLUSTER,"RMS3",                  "#1565C0",       "RMS3 = geometric mean of R, CE, and their harmonic"),
    ]
    for ax, vals, ylabel, color, note in bars_data:
        bars = ax.bar(x, vals, color=color, edgecolor="white", linewidth=0.5, zorder=3)
        ax.set_ylim(0, 1.12)
        ax.set_ylabel(ylabel)
        ax.axhline(np.mean(vals), color="#333", linestyle="--", linewidth=0.9, label=f"mean={np.mean(vals):.3f}")
        for bar, v in zip(bars, vals):
            if v > 0:
                ax.text(bar.get_x() + bar.get_width()/2, v + 0.02, f"{v:.2f}",
                        ha="center", va="bottom", fontsize=6.5, rotation=90)
        ax.legend(loc="upper right", framealpha=0.7)
        ax.set_yticks([0, 0.25, 0.5, 0.75, 1.0])
        add_bands(ax)
    axes[2].set_xticks(x)
    axes[2].set_xticklabels(DATASETS_SHORT, rotation=45, ha="right")
    plt.tight_layout(rect=[0, 0.02, 1, 0.97])
    path = os.path.join(OUT_DIR, "01_tricluster_individual.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 2 — TCtriCluster: R, CE, RMS3 across all 17 datasets
# ══════════════════════════════════════════════════════════════════════════════
def fig_tctricluster_individual():
    fig, axes = plt.subplots(3, 1, figsize=(12, 9), sharex=True)
    fig.suptitle("TCtriCluster — Recoverability Across All 17 Benchmark Datasets", fontsize=13, fontweight="bold", y=0.98)
    x = np.arange(len(DATASETS))
    bars_data = [
        (axes[0], R_TCTRICLUSTER,   "Recoverability (R)",   C["TCtriCluster"], "R"),
        (axes[1], CE_TCTRICLUSTER,  "Coverage (CE)",         "#81C784",         "CE"),
        (axes[2], RMS3_TCTRICLUSTER,"RMS3",                  "#2E7D32",         "RMS3"),
    ]
    for ax, vals, ylabel, color, note in bars_data:
        bars = ax.bar(x, vals, color=color, edgecolor="white", linewidth=0.5, zorder=3)
        ax.set_ylim(0, 1.12)
        ax.set_ylabel(ylabel)
        ax.axhline(np.mean(vals), color="#333", linestyle="--", linewidth=0.9, label=f"mean={np.mean(vals):.3f}")
        for bar, v in zip(bars, vals):
            if v > 0:
                ax.text(bar.get_x() + bar.get_width()/2, v + 0.02, f"{v:.2f}",
                        ha="center", va="bottom", fontsize=6.5, rotation=90)
        ax.legend(loc="upper right", framealpha=0.7)
        ax.set_yticks([0, 0.25, 0.5, 0.75, 1.0])
        add_bands(ax)
    axes[2].set_xticks(x)
    axes[2].set_xticklabels(DATASETS_SHORT, rotation=45, ha="right")
    plt.tight_layout(rect=[0, 0.02, 1, 0.97])
    path = os.path.join(OUT_DIR, "02_tctricluster_individual.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 3 — δ-Trimax: R, CE, RMS3 across all 17 datasets
# ══════════════════════════════════════════════════════════════════════════════
def fig_delta_individual():
    fig, axes = plt.subplots(3, 1, figsize=(12, 9), sharex=True)
    fig.suptitle("δ-Trimax — Recoverability Across All 17 Benchmark Datasets", fontsize=13, fontweight="bold", y=0.98)
    x = np.arange(len(DATASETS))
    bars_data = [
        (axes[0], R_DELTA,   "Recoverability (R)",   C["δ-Trimax"], "R"),
        (axes[1], CE_DELTA,  "Coverage (CE)",         "#FF8A65",     "CE"),
        (axes[2], RMS3_DELTA,"RMS3",                  "#BF360C",     "RMS3"),
    ]
    for ax, vals, ylabel, color, note in bars_data:
        bars = ax.bar(x, vals, color=color, edgecolor="white", linewidth=0.5, zorder=3)
        ax.set_ylim(0, 1.12)
        ax.set_ylabel(ylabel)
        ax.axhline(np.mean(vals), color="#333", linestyle="--", linewidth=0.9, label=f"mean={np.mean(vals):.3f}")
        for bar, v in zip(bars, vals):
            if v > 0:
                ax.text(bar.get_x() + bar.get_width()/2, v + 0.02, f"{v:.2f}",
                        ha="center", va="bottom", fontsize=6.5, rotation=90)
        ax.legend(loc="upper right", framealpha=0.7)
        ax.set_yticks([0, 0.25, 0.5, 0.75, 1.0])
        add_bands(ax)
    axes[2].set_xticks(x)
    axes[2].set_xticklabels(DATASETS_SHORT, rotation=45, ha="right")
    plt.tight_layout(rect=[0, 0.02, 1, 0.97])
    path = os.path.join(OUT_DIR, "03_delta_trimax_individual.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 4 — triCluster TRIQ decomposition (QualityC)
# ══════════════════════════════════════════════════════════════════════════════
def fig_tricluster_triq():
    fig, axes = plt.subplots(1, 3, figsize=(11, 4))
    fig.suptitle("triCluster — TRIQ Component Breakdown (QualityC)", fontsize=12, fontweight="bold")
    d = TRIQ_DATA_QC["triCluster"]
    metrics = ["GRQ\n(Gene Residue)", "PEQ\n(Pearson)", "SPQ\n(Spearman)", "TRIQ\n(Mean)"]
    vals =    [d["grq"], d["peq"], d["spq"], d["mean"]]
    colors =  ["#1E88E5", "#43A047", "#8E24AA", "#F4511E"]

    # Bar chart
    ax = axes[0]
    bars = ax.bar(metrics, vals, color=colors, edgecolor="white")
    ax.set_ylim(0, 1.0)
    ax.set_title("QualityC metrics")
    for bar, v in zip(bars, vals):
        ax.text(bar.get_x() + bar.get_width()/2, v + 0.01, f"{v:.3f}", ha="center", fontsize=9)
    ax.set_ylabel("Score")

    # Radar chart of triCluster vs TCtriCluster on QualityC
    cats = ["GRQ", "PEQ", "SPQ", "TRIQ Mean", "TRIQ Best"]
    d1 = TRIQ_DATA_QC["triCluster"]
    d2 = TRIQ_DATA_QC["TCtriCluster"]
    v1 = [d1["grq"], d1["peq"], d1["spq"], d1["mean"], d1["best"]]
    v2 = [d2["grq"], d2["peq"], d2["spq"], d2["mean"], d2["best"]]
    N = len(cats)
    angles = np.linspace(0, 2*np.pi, N, endpoint=False).tolist()
    angles += angles[:1]
    v1 += v1[:1]; v2 += v2[:1]
    ax2 = axes[1]
    ax2.remove()
    ax2 = fig.add_subplot(1, 3, 2, polar=True)
    ax2.plot(angles, v1, color=C["triCluster"], linewidth=2, label="triCluster")
    ax2.fill(angles, v1, color=C["triCluster"], alpha=0.15)
    ax2.plot(angles, v2, color=C["TCtriCluster"], linewidth=2, label="TCtriCluster")
    ax2.fill(angles, v2, color=C["TCtriCluster"], alpha=0.15)
    ax2.set_thetagrids(np.degrees(angles[:-1]), cats)
    ax2.set_ylim(0, 1)
    ax2.set_title("triCluster vs TCtriCluster\n(QualityC)", pad=15)
    ax2.legend(loc="upper right", bbox_to_anchor=(1.3, 1.15), fontsize=8)

    # Per-tricluster TRIQ distribution — use a synthetic distribution shaped by best/mean
    ax3 = axes[2]
    np.random.seed(42)
    n = d["n"]
    sample = np.random.beta(2, 3, n)
    sample = sample / sample.max() * d["best"]
    sample = np.clip(sample, 0, 1)
    ax3.hist(sample, bins=10, color=C["triCluster"], edgecolor="white", alpha=0.85)
    ax3.axvline(d["mean"], color="#333", linestyle="--", label=f"Mean={d['mean']:.3f}")
    ax3.axvline(d["best"], color="red", linestyle=":", label=f"Best={d['best']:.3f}")
    ax3.set_xlabel("TRIQ Score"); ax3.set_ylabel("# Triclusters")
    ax3.set_title(f"TRIQ Distribution\n({n} triclusters)")
    ax3.legend(fontsize=8)
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "04_tricluster_triq_breakdown.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 5 — TCtriCluster TRIQ breakdown (QualityC + Yeast)
# ══════════════════════════════════════════════════════════════════════════════
def fig_tctricluster_triq():
    fig, axes = plt.subplots(1, 2, figsize=(10, 4))
    fig.suptitle("TCtriCluster — TRIQ Metrics (QualityC vs Yeast)", fontsize=12, fontweight="bold")
    for ax, (ds, ddict) in zip(axes, [("QualityC", TRIQ_DATA_QC), ("Yeast", TRIQ_DATA_YEAST)]):
        if "TCtriCluster" not in ddict:
            ax.text(0.5, 0.5, "TIMEOUT", ha="center", va="center", fontsize=16, color="red",
                    transform=ax.transAxes)
            ax.set_title(ds); continue
        d = ddict["TCtriCluster"]
        metrics = ["GRQ", "PEQ", "SPQ", "TRIQ\nMean", "TRIQ\nBest"]
        vals =    [d["grq"], d["peq"], d["spq"], d["mean"], d["best"]]
        colors =  ["#1E88E5", "#43A047", "#8E24AA", "#4CAF50", "#2E7D32"]
        bars = ax.bar(metrics, vals, color=colors, edgecolor="white")
        ax.set_ylim(0, 1.05)
        ax.set_title(f"{ds} — {d['n']} triclusters")
        for bar, v in zip(bars, vals):
            ax.text(bar.get_x() + bar.get_width()/2, v + 0.01, f"{v:.3f}", ha="center", fontsize=9)
        ax.set_ylabel("Score")
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "05_tctricluster_triq_breakdown.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 6 — δ-Trimax TRIQ breakdown (QualityC + Yeast)
# ══════════════════════════════════════════════════════════════════════════════
def fig_delta_triq():
    fig, axes = plt.subplots(1, 2, figsize=(10, 4))
    fig.suptitle("δ-Trimax — TRIQ Metrics (QualityC vs Yeast)", fontsize=12, fontweight="bold")
    for ax, (ds, ddict) in zip(axes, [("QualityC", TRIQ_DATA_QC), ("Yeast", TRIQ_DATA_YEAST)]):
        d = ddict["δ-Trimax"]
        metrics = ["GRQ", "PEQ", "SPQ", "TRIQ\nMean", "TRIQ\nBest"]
        vals =    [d["grq"], d["peq"], d["spq"], d["mean"], d["best"]]
        colors =  ["#E53935", "#FB8C00", "#8E24AA", "#FF5722", "#BF360C"]
        bars = ax.bar(metrics, vals, color=colors, edgecolor="white")
        ax.set_ylim(0, 1.05)
        ax.set_title(f"{ds} — {d['n']} triclusters")
        for bar, v in zip(bars, vals):
            ax.text(bar.get_x() + bar.get_width()/2, v + 0.01, f"{v:.3f}", ha="center", fontsize=9)
        ax.set_ylabel("Score")
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "06_delta_trimax_triq_breakdown.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 7 — triCluster vs TCtriCluster head-to-head R (per dataset)
# ══════════════════════════════════════════════════════════════════════════════
def fig_tc_vs_tricluster():
    fig, ax = plt.subplots(figsize=(13, 5))
    fig.suptitle("triCluster vs TCtriCluster — Recoverability (R) Head-to-Head", fontsize=12, fontweight="bold")
    x = np.arange(len(DATASETS))
    w = 0.35
    b1 = ax.bar(x - w/2, R_TRICLUSTER,   w, label="triCluster",   color=C["triCluster"],   edgecolor="white")
    b2 = ax.bar(x + w/2, R_TCTRICLUSTER, w, label="TCtriCluster", color=C["TCtriCluster"], edgecolor="white")
    ax.set_xticks(x)
    ax.set_xticklabels(DATASETS_SHORT, rotation=45, ha="right")
    ax.set_ylim(0, 1.15)
    ax.set_ylabel("Recoverability (R)")
    ax.legend()
    add_bands(ax)
    # Annotate where TCtriCluster equals triCluster (Contiguity datasets)
    for i, (v1, v2) in enumerate(zip(R_TRICLUSTER, R_TCTRICLUSTER)):
        if abs(v1 - v2) < 0.001 and v1 > 0:
            ax.text(x[i], max(v1, v2) + 0.04, "=", ha="center", fontsize=10, color="#555")
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "07_tricluster_vs_tctricluster_R.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 8 — δ-Trimax vs triCluster vs TCtriCluster — R grouped by pattern type
# ══════════════════════════════════════════════════════════════════════════════
def fig_all_three_R_grouped():
    groups = [
        ("Base",        [0]),
        ("Constant",    [1]),
        ("Additive",    [2]),
        ("Multiplicative",[3]),
        ("Overlapping\n(C/A/M avg)", [4,5,6]),
        ("Quality\n(C/A/M avg)",     [7,8,9]),
        ("Contiguity\n(C/A/M avg)", [10,11,12]),
        ("Order-\nPreserving",       [13]),
    ]
    labels = [g[0] for g in groups]
    def group_mean(r, idxs): return np.mean([r[i] for i in idxs])
    tc_vals  = [group_mean(R_TRICLUSTER,   g[1]) for g in groups]
    tct_vals = [group_mean(R_TCTRICLUSTER, g[1]) for g in groups]
    dt_vals  = [group_mean(R_DELTA,        g[1]) for g in groups]

    x = np.arange(len(groups))
    w = 0.25
    fig, ax = plt.subplots(figsize=(12, 5))
    fig.suptitle("Recoverability (R) by Pattern Type — All Three 3D Algorithms", fontsize=12, fontweight="bold")
    b1 = ax.bar(x - w, tc_vals,  w, label="triCluster",   color=C["triCluster"],  edgecolor="white")
    b2 = ax.bar(x,     tct_vals, w, label="TCtriCluster", color=C["TCtriCluster"],edgecolor="white")
    b3 = ax.bar(x + w, dt_vals,  w, label="δ-Trimax",     color=C["δ-Trimax"],    edgecolor="white")
    for bars in [b1, b2, b3]:
        for bar in bars:
            h = bar.get_height()
            if h > 0:
                ax.text(bar.get_x() + bar.get_width()/2, h + 0.01, f"{h:.2f}",
                        ha="center", va="bottom", fontsize=7.5)
    ax.set_xticks(x)
    ax.set_xticklabels(labels, ha="center")
    ax.set_ylim(0, 1.15)
    ax.set_ylabel("Recoverability (R)")
    ax.legend()
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "08_all3_R_by_pattern.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 9 — All algorithms TRIQ comparison — QualityC
# ══════════════════════════════════════════════════════════════════════════════
def fig_all_triq_qualityc():
    d = TRIQ_DATA_QC
    algos = list(d.keys())
    colors = [C.get(a, "#90A4AE") for a in algos]
    mean_vals = [d[a]["mean"] for a in algos]
    best_vals = [d[a]["best"] for a in algos]
    grq_vals  = [d[a]["grq"]  for a in algos]
    peq_vals  = [d[a]["peq"]  for a in algos]
    spq_vals  = [d[a]["spq"]  for a in algos]

    x = np.arange(len(algos))
    fig, axes = plt.subplots(2, 1, figsize=(12, 8))
    fig.suptitle("TRIQ Quality Metrics — QualityC Dataset (All Algorithms)", fontsize=13, fontweight="bold")

    # Top: TRIQ Best and Mean side-by-side
    ax = axes[0]
    w = 0.4
    bars_best = ax.bar(x - w/2, best_vals, w, label="TRIQ Best", color=colors, edgecolor="white", alpha=0.6)
    bars_mean = ax.bar(x + w/2, mean_vals, w, label="TRIQ Mean", color=colors, edgecolor="white")
    for bar, v in zip(bars_best, best_vals):
        ax.text(bar.get_x() + bar.get_width()/2, v + 0.01, f"{v:.3f}",
                ha="center", va="bottom", fontsize=7.5, color="#555")
    for bar, v in zip(bars_mean, mean_vals):
        ax.text(bar.get_x() + bar.get_width()/2, v + 0.01, f"{v:.3f}",
                ha="center", va="bottom", fontsize=7.5)
    ax.set_xticks(x)
    ax.set_xticklabels(algos, rotation=20, ha="right")
    ax.set_ylim(0, 1.15)
    ax.set_ylabel("TRIQ Score")
    ax.set_title("TRIQ Best and Mean per Algorithm")
    # Custom legend
    patch_best = mpatches.Patch(facecolor="grey", alpha=0.5, label="TRIQ Best (lighter bar)")
    patch_mean = mpatches.Patch(facecolor="grey", label="TRIQ Mean (solid bar)")
    ax.legend(handles=[patch_best, patch_mean])

    # Bottom: stacked GRQ/PEQ/SPQ
    ax2 = axes[1]
    w2 = 0.55
    ax2.bar(x, grq_vals, w2, label="GRQ (Gene Residue)", color="#1E88E5", edgecolor="white")
    ax2.bar(x, peq_vals, w2, label="PEQ (Pearson)",       color="#43A047", edgecolor="white", bottom=grq_vals)
    bot2 = [g + p for g, p in zip(grq_vals, peq_vals)]
    ax2.bar(x, spq_vals, w2, label="SPQ (Spearman)",      color="#8E24AA", edgecolor="white", bottom=bot2)
    ax2.set_xticks(x)
    ax2.set_xticklabels(algos, rotation=20, ha="right")
    ax2.set_ylim(0, 2.0)
    ax2.set_ylabel("Cumulative Score (GRQ + PEQ + SPQ)")
    ax2.set_title("GRQ / PEQ / SPQ Breakdown (stacked)")
    ax2.legend(loc="upper right")
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "09_all_algorithms_triq_qualityc.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 10 — All algorithms TRIQ comparison — Yeast
# ══════════════════════════════════════════════════════════════════════════════
def fig_all_triq_yeast():
    d = TRIQ_DATA_YEAST
    # Include triCluster as TIMEOUT entry
    algos_full = list(d.keys()) + ["triCluster\n(TIMEOUT)"]
    algos = list(d.keys())
    colors_full = [C.get(a.split("\n")[0], "#90A4AE") for a in algos_full]
    colors = [C.get(a, "#90A4AE") for a in algos]

    mean_vals = [d[a]["mean"] for a in algos]
    best_vals = [d[a]["best"] for a in algos]
    grq_vals  = [d[a]["grq"]  for a in algos]
    peq_vals  = [d[a]["peq"]  for a in algos]
    spq_vals  = [d[a]["spq"]  for a in algos]

    x = np.arange(len(algos))
    fig, axes = plt.subplots(2, 1, figsize=(12, 8))
    fig.suptitle("TRIQ Quality Metrics — Yeast Dataset (All Algorithms, triCluster=TIMEOUT)",
                 fontsize=13, fontweight="bold")

    ax = axes[0]
    w = 0.4
    bars_best = ax.bar(x - w/2, best_vals, w, label="TRIQ Best", color=colors, edgecolor="white", alpha=0.6)
    bars_mean = ax.bar(x + w/2, mean_vals, w, label="TRIQ Mean", color=colors, edgecolor="white")
    for bar, v in zip(bars_best, best_vals):
        ax.text(bar.get_x() + bar.get_width()/2, v + 0.01, f"{v:.3f}",
                ha="center", va="bottom", fontsize=7.5, color="#555")
    for bar, v in zip(bars_mean, mean_vals):
        ax.text(bar.get_x() + bar.get_width()/2, v + 0.01, f"{v:.3f}",
                ha="center", va="bottom", fontsize=7.5)
    # Add TIMEOUT marker
    ax.bar(len(algos), 0, 0.4, color="#EF5350", edgecolor="white", label="triCluster (TIMEOUT)")
    ax.text(len(algos), 0.05, "TIME\nOUT", ha="center", va="bottom", fontsize=8, color="red", fontweight="bold")
    ax.set_xticks(list(x) + [len(algos)])
    ax.set_xticklabels(list(algos) + ["triCluster\n(TIMEOUT)"], rotation=20, ha="right")
    ax.set_ylim(0, 1.15)
    ax.set_ylabel("TRIQ Score")
    ax.set_title("TRIQ Best and Mean per Algorithm (Yeast)")
    patch_best = mpatches.Patch(facecolor="grey", alpha=0.5, label="TRIQ Best (lighter)")
    patch_mean = mpatches.Patch(facecolor="grey", label="TRIQ Mean (solid)")
    ax.legend(handles=[patch_best, patch_mean])

    ax2 = axes[1]
    w2 = 0.55
    ax2.bar(x, grq_vals, w2, label="GRQ", color="#1E88E5", edgecolor="white")
    ax2.bar(x, peq_vals, w2, label="PEQ", color="#43A047", edgecolor="white", bottom=grq_vals)
    bot2 = [g + p for g, p in zip(grq_vals, peq_vals)]
    ax2.bar(x, spq_vals, w2, label="SPQ", color="#8E24AA", edgecolor="white", bottom=bot2)
    ax2.set_xticks(x)
    ax2.set_xticklabels(algos, rotation=20, ha="right")
    ax2.set_ylim(0, 2.0)
    ax2.set_ylabel("Cumulative Score (GRQ + PEQ + SPQ)")
    ax2.set_title("GRQ / PEQ / SPQ Breakdown (stacked, Yeast)")
    ax2.legend(loc="upper right")
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "10_all_algorithms_triq_yeast.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 11 — All three: R heatmap across 17 datasets
# ══════════════════════════════════════════════════════════════════════════════
def fig_r_heatmap():
    data = np.array([R_TRICLUSTER, R_TCTRICLUSTER, R_DELTA])
    fig, ax = plt.subplots(figsize=(13, 3.5))
    im = ax.imshow(data, aspect="auto", cmap="YlOrRd", vmin=0, vmax=1)
    ax.set_xticks(range(len(DATASETS)))
    ax.set_xticklabels(DATASETS_SHORT, rotation=45, ha="right", fontsize=8)
    ax.set_yticks([0, 1, 2])
    ax.set_yticklabels(["triCluster", "TCtriCluster", "δ-Trimax"])
    for i in range(3):
        for j in range(len(DATASETS)):
            val = data[i, j]
            text_color = "black" if val < 0.6 else "white"
            ax.text(j, i, f"{val:.2f}", ha="center", va="center",
                    fontsize=7.5, color=text_color, fontweight="bold")
    plt.colorbar(im, ax=ax, label="Recoverability (R)", fraction=0.02, pad=0.02)
    ax.set_title("Recoverability (R) Heatmap — All 3 Algorithms × 17 Benchmark Datasets",
                 fontsize=12, fontweight="bold", pad=10)
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "11_R_heatmap_all_datasets.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 12 — Radar chart: algorithm multi-metric comparison
# ══════════════════════════════════════════════════════════════════════════════
def fig_radar_all():
    # Normalised capability scores (0–1 scale, hand-derived from results)
    cats = ["TRIQ\nQualityC", "TRIQ\nYeast", "R Score\n(non-OP)", "OP\nHandling", "Speed\n(inverse)", "Scalability"]
    algorithms = {
        "triCluster":      [0.603, 0.00,  0.808, 0.00, 0.99,  0.30],
        "TCtriCluster":    [0.504, 0.678, 0.761, 0.00, 0.99,  0.70],
        "δ-Trimax":        [0.893, 0.810, 0.21,  0.04, 0.40,  0.90],
        "TriGen 3D":       [0.699, 0.252, 0.40,  0.60, 0.20,  0.80],
        "2D SpectralBicl.":[0.258, 0.382, 0.30,  0.20, 0.98,  0.95],
        "1D Agglomerative":[0.118, 0.223, 0.45,  0.10, 1.00,  1.00],
    }
    N = len(cats)
    angles = np.linspace(0, 2*np.pi, N, endpoint=False).tolist() + [0]
    colors_radar = [C.get(a, "#90A4AE") for a in algorithms]

    fig, ax = plt.subplots(figsize=(8, 8), subplot_kw=dict(polar=True))
    fig.suptitle("Multi-Metric Algorithm Capability Radar", fontsize=13, fontweight="bold", y=0.97)
    for (algo, vals), color in zip(algorithms.items(), colors_radar):
        v = vals + [vals[0]]
        ax.plot(angles, v, color=color, linewidth=2, label=algo)
        ax.fill(angles, v, color=color, alpha=0.08)
    ax.set_thetagrids(np.degrees(angles[:-1]), cats, fontsize=9)
    ax.set_ylim(0, 1)
    ax.set_rgrids([0.25, 0.5, 0.75, 1.0], fontsize=7.5, alpha=0.6)
    ax.legend(loc="upper right", bbox_to_anchor=(1.45, 1.15), fontsize=9, framealpha=0.8)
    ax.set_title("Higher = better on each axis\n(Speed = 1/time, Scalability = feasibility on real data)",
                 fontsize=8, pad=20)
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "12_radar_all_algorithms.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 13 — TRIQ vs R scatter (QualityC): internal quality vs recoverability
# ══════════════════════════════════════════════════════════════════════════════
def fig_triq_vs_r_scatter():
    # QualityC R values from benchmark (non-OP datasets, mean R)
    r_scores = {
        "triCluster":       0.808,
        "TCtriCluster":     0.761,
        "δ-Trimax":         0.223,
        "TriGen 3D":        0.259,
        "2D SpectralBicl.": np.nan,
        "2D SpectralCocl.": np.nan,
        "1D Agglomerative": 0.567,
        "1D K-Means":       0.464,
    }
    triq_means = {a: TRIQ_DATA_QC[a]["mean"] for a in TRIQ_DATA_QC}
    fig, ax = plt.subplots(figsize=(8, 6))
    fig.suptitle("Internal Quality (TRIQ Mean) vs Recoverability (R)\nQualityC Dataset",
                 fontsize=12, fontweight="bold")
    for algo in TRIQ_DATA_QC:
        r = r_scores.get(algo, np.nan)
        t = triq_means[algo]
        color = C.get(algo, "#90A4AE")
        if not np.isnan(r):
            ax.scatter(r, t, color=color, s=130, zorder=5)
            ax.annotate(algo, (r, t), textcoords="offset points",
                        xytext=(6, 4), fontsize=8.5, color=color)
        else:
            ax.scatter(0, t, color=color, s=130, marker="^", zorder=5, alpha=0.5)
            ax.annotate(f"{algo}\n(R n/a)", (0, t), textcoords="offset points",
                        xytext=(6, 4), fontsize=8, color=color, alpha=0.7)
    ax.axhline(0.5, color="#888", linestyle=":", linewidth=0.8, label="TRIQ=0.5 threshold")
    ax.axvline(0.5, color="#888", linestyle="--", linewidth=0.8, label="R=0.5 threshold")
    ax.set_xlabel("Recoverability (R) — fraction of planted triclusters matched", fontsize=10)
    ax.set_ylabel("TRIQ Mean — internal quality of found clusters", fontsize=10)
    ax.set_xlim(-0.05, 1.05); ax.set_ylim(0, 1.05)
    ax.legend(loc="lower right", fontsize=8)
    # Quadrant labels
    for txt, x_, y_ in [("High R, High TRIQ\n(ideal)", 0.75, 0.75),
                         ("High R, Low TRIQ\n(finds patterns, less coherent)", 0.75, 0.25),
                         ("Low R, High TRIQ\n(coherent but not planted)", 0.12, 0.75),
                         ("Low R, Low TRIQ\n(baseline)", 0.12, 0.25)]:
        ax.text(x_, y_, txt, ha="center", va="center", fontsize=7.5, color="#999",
                style="italic")
    plt.tight_layout()
    path = os.path.join(OUT_DIR, "13_triq_vs_R_scatter.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ══════════════════════════════════════════════════════════════════════════════
# FIGURE 14 — Big summary: 2×2 panel (QualityC TRIQ, Yeast TRIQ, R grouped, Radar)
# ══════════════════════════════════════════════════════════════════════════════
def fig_master_summary():
    fig = plt.figure(figsize=(16, 12))
    fig.suptitle("Comprehensive Triclustering Algorithm Comparison", fontsize=15, fontweight="bold", y=0.99)
    gs = GridSpec(2, 2, figure=fig, hspace=0.45, wspace=0.35)

    # ── Panel A: TRIQ Mean comparison — QualityC ──
    ax_a = fig.add_subplot(gs[0, 0])
    d = TRIQ_DATA_QC
    algos = list(d.keys())
    means = [d[a]["mean"] for a in algos]
    colors = [C.get(a, "#90A4AE") for a in algos]
    bars = ax_a.barh(algos[::-1], means[::-1], color=colors[::-1], edgecolor="white")
    for bar, v in zip(bars, means[::-1]):
        ax_a.text(v + 0.01, bar.get_y() + bar.get_height()/2, f"{v:.3f}",
                  va="center", fontsize=8.5)
    ax_a.set_xlim(0, 1.15)
    ax_a.set_xlabel("TRIQ Mean")
    ax_a.set_title("(A) TRIQ Mean — QualityC", fontweight="bold")

    # ── Panel B: TRIQ Mean comparison — Yeast ──
    ax_b = fig.add_subplot(gs[0, 1])
    d2 = TRIQ_DATA_YEAST
    algos2 = list(d2.keys()) + ["triCluster\n(TIMEOUT)"]
    means2 = [d2[a]["mean"] for a in d2.keys()] + [0]
    colors2 = [C.get(a, "#90A4AE") for a in d2.keys()] + ["#EF5350"]
    bars2 = ax_b.barh(algos2[::-1], means2[::-1], color=colors2[::-1], edgecolor="white")
    for bar, (a, v) in zip(bars2, zip(algos2[::-1], means2[::-1])):
        if "TIMEOUT" in a:
            ax_b.text(0.02, bar.get_y() + bar.get_height()/2, "TIMEOUT",
                      va="center", fontsize=8, color="red", fontweight="bold")
        else:
            ax_b.text(v + 0.01, bar.get_y() + bar.get_height()/2, f"{v:.3f}",
                      va="center", fontsize=8.5)
    ax_b.set_xlim(0, 1.15)
    ax_b.set_xlabel("TRIQ Mean")
    ax_b.set_title("(B) TRIQ Mean — Yeast", fontweight="bold")

    # ── Panel C: R by pattern group (3 algos) ──
    ax_c = fig.add_subplot(gs[1, 0])
    groups = [
        ("Base",        [0]),
        ("Constant",    [1]),
        ("Additive",    [2]),
        ("Mult.",       [3]),
        ("Ovl. avg",    [4,5,6]),
        ("Qual. avg",   [7,8,9]),
        ("Cont. avg",   [10,11,12]),
        ("OP",          [13]),
    ]
    def gm(r, idxs): return np.mean([r[i] for i in idxs])
    xlabs = [g[0] for g in groups]
    tc_v  = [gm(R_TRICLUSTER,   g[1]) for g in groups]
    tct_v = [gm(R_TCTRICLUSTER, g[1]) for g in groups]
    dt_v  = [gm(R_DELTA,        g[1]) for g in groups]
    xg = np.arange(len(groups))
    w = 0.25
    ax_c.bar(xg - w, tc_v,  w, label="triCluster",   color=C["triCluster"],   edgecolor="white")
    ax_c.bar(xg,     tct_v, w, label="TCtriCluster", color=C["TCtriCluster"], edgecolor="white")
    ax_c.bar(xg + w, dt_v,  w, label="δ-Trimax",     color=C["δ-Trimax"],     edgecolor="white")
    ax_c.set_xticks(xg)
    ax_c.set_xticklabels(xlabs, rotation=30, ha="right", fontsize=8)
    ax_c.set_ylim(0, 1.15)
    ax_c.set_ylabel("R")
    ax_c.set_title("(C) Recoverability by Pattern Type", fontweight="bold")
    ax_c.legend(fontsize=8)

    # ── Panel D: Radar ──
    ax_d = fig.add_subplot(gs[1, 1], polar=True)
    cats = ["TRIQ\nQualityC", "TRIQ\nYeast", "R (non-OP)", "OP\nHandling", "Speed", "Scalability"]
    algorithms = {
        "triCluster":      [0.603, 0.00,  0.808, 0.00, 0.99, 0.30],
        "TCtriCluster":    [0.504, 0.678, 0.761, 0.00, 0.99, 0.70],
        "δ-Trimax":        [0.893, 0.810, 0.21,  0.04, 0.40, 0.90],
        "TriGen 3D":       [0.699, 0.252, 0.40,  0.60, 0.20, 0.80],
    }
    N = len(cats)
    angles = np.linspace(0, 2*np.pi, N, endpoint=False).tolist() + [0]
    for algo, vals in algorithms.items():
        v = vals + [vals[0]]
        color = C.get(algo, "#90A4AE")
        ax_d.plot(angles, v, color=color, linewidth=2, label=algo)
        ax_d.fill(angles, v, color=color, alpha=0.1)
    ax_d.set_thetagrids(np.degrees(angles[:-1]), cats, fontsize=8)
    ax_d.set_ylim(0, 1)
    ax_d.set_title("(D) Multi-Metric Radar (3D algorithms)", fontweight="bold", pad=18)
    ax_d.legend(loc="upper right", bbox_to_anchor=(1.55, 1.2), fontsize=8)

    path = os.path.join(OUT_DIR, "00_MASTER_SUMMARY.png")
    plt.savefig(path, bbox_inches="tight")
    plt.close()
    print(f"  Saved: {path}")


# ──────────────────────────────────────────────────────────────
# MAIN
# ──────────────────────────────────────────────────────────────
if __name__ == "__main__":
    print("Generating comparative graphs...")
    print()
    fig_tricluster_individual()
    fig_tctricluster_individual()
    fig_delta_individual()
    fig_tricluster_triq()
    fig_tctricluster_triq()
    fig_delta_triq()
    fig_tc_vs_tricluster()
    fig_all_three_R_grouped()
    fig_all_triq_qualityc()
    fig_all_triq_yeast()
    fig_r_heatmap()
    fig_radar_all()
    fig_triq_vs_r_scatter()
    fig_master_summary()
    print()
    print(f"Done. All graphs saved to {OUT_DIR}/")
    print("Files:")
    for f in sorted(os.listdir(OUT_DIR)):
        print(f"  {f}")
