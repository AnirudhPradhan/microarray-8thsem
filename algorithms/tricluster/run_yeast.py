"""
run_yeast.py
------------
Run triCluster on the real Yeast GST dataset (Spellman et al.).

Dataset:  algorithms/neucom-trigen/data/yeast_gst_tensor.npz
Shape:    ~7681 genes × 3 conditions × 14 time points

Since this is a real dataset (no ground truth), we:
  1. Load and preprocess the tensor (impute NaNs, optionally subset)
  2. Run triCluster in 'additive' mode (values are log2-scale, can be negative)
  3. Report the found triclusters: sizes, summary statistics
  4. Save results to results/yeast_triclusters.csv

Notes on parameters for yeast log2 data
-----------------------------------------
- Values range approx [-14, +8], mean ≈ -0.09
- 'additive' mode: window = |diff_max - diff_min| <= winsz
- winsz=1.0 means genes whose pairwise differences across conditions
  span ≤ 1.0 log2 unit are grouped together (≈ 2× fold-change tolerance)
- With only 3 conditions, min_samples=2 is the practical minimum
- min_genes=10 is reasonable for a 7000-gene space
"""

import sys
import time
import csv
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(Path(__file__).resolve().parent))

from tricluster import TriCluster
from data_io import load_yeast_npz, write_tab, triclusters_to_records

YEAST_NPZ = REPO_ROOT / "algorithms" / "neucom-trigen" / "data" / "yeast_gst_tensor.npz"
RESULTS_DIR = Path(__file__).parent / "results"

# ---------------------------------------------------------------------------
# Parameters
# ---------------------------------------------------------------------------
YEAST_PARAMS = dict(
    winsz=1.0,          # log2 additive tolerance (≈2× fold-change)
    min_times=3,        # use at least 3 of 14 time points
    min_samples=2,      # only 3 conditions total, so 2 is the minimum
    min_genes=10,       # at least 10 genes per tricluster
    mode="additive",
    del_overlap=1.0,
)

# Optionally limit to top N most variable genes for speed
TOP_N_GENES = 2000   # set to None to use all ~7681 genes (slower)


def select_top_variable(data, gene_names, n):
    """Select the n genes with highest variance across the full tensor."""
    import numpy as np
    variances = data.reshape(data.shape[0], -1).var(axis=1)
    idx = variances.argsort()[::-1][:n]
    return data[idx], [gene_names[i] for i in idx]


def main():
    RESULTS_DIR.mkdir(exist_ok=True)

    print("Loading yeast tensor …")
    data, genes, conditions, time_labels = load_yeast_npz(YEAST_NPZ)
    n_genes, n_samples, n_times = data.shape
    print(f"  Loaded: {n_genes} genes × {n_samples} conditions × {n_times} time points")
    print(f"  Conditions: {conditions}")

    if TOP_N_GENES and n_genes > TOP_N_GENES:
        print(f"  Selecting top {TOP_N_GENES} most variable genes …")
        data, genes = select_top_variable(data, genes, TOP_N_GENES)
        n_genes = data.shape[0]
        print(f"  Reduced to {n_genes} genes")

    # Optionally write the .tab file for reference
    tab_out = RESULTS_DIR / "yeast_formatted.tab"
    write_tab(tab_out, data, genes, conditions)
    print(f"  Saved formatted .tab -> {tab_out}")

    # Run triCluster
    print(f"\nRunning triCluster (additive mode, winsz={YEAST_PARAMS['winsz']}) …")
    tc = TriCluster(**YEAST_PARAMS)
    t0 = time.time()
    triclusters = tc.fit(data)
    elapsed = time.time() - t0
    predicted = triclusters_to_records(triclusters)
    print(f"  Found {len(predicted)} triclusters  ({elapsed:.1f}s)")

    if not predicted:
        print("\nNo triclusters found. Try increasing winsz or decreasing min thresholds.")
        return

    # Summary statistics
    import numpy as np
    gene_counts = [len(t["X"]) for t in predicted]
    sample_counts = [len(t["Y"]) for t in predicted]
    time_counts = [len(t["Z"]) for t in predicted]
    print(f"\nTricluster size statistics:")
    print(f"  Genes   : min={min(gene_counts)}, max={max(gene_counts)}, mean={np.mean(gene_counts):.1f}")
    print(f"  Samples : min={min(sample_counts)}, max={max(sample_counts)}, mean={np.mean(sample_counts):.1f}")
    print(f"  Times   : min={min(time_counts)}, max={max(time_counts)}, mean={np.mean(time_counts):.1f}")

    # Save results
    csv_out = RESULTS_DIR / "yeast_triclusters.csv"
    with open(csv_out, "w", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["tricluster_id", "n_genes", "n_samples", "n_times",
                         "genes", "samples", "time_points"])
        for i, t in enumerate(predicted):
            writer.writerow([
                i,
                len(t["X"]), len(t["Y"]), len(t["Z"]),
                ";".join(genes[g] for g in t["X"]),
                ";".join(conditions[s] for s in t["Y"]),
                ";".join(time_labels[z] for z in t["Z"]),
            ])
    print(f"\nResults saved -> {csv_out}")

    # Print top-10 largest triclusters
    predicted_sorted = sorted(predicted, key=lambda t: len(t["X"]) * len(t["Y"]) * len(t["Z"]), reverse=True)
    print("\nTop-10 triclusters by volume:")
    print(f"  {'#':>4}  {'Genes':>6}  {'Samp':>5}  {'Times':>6}  {'Volume':>8}")
    for i, t in enumerate(predicted_sorted[:10]):
        vol = len(t["X"]) * len(t["Y"]) * len(t["Z"])
        print(f"  {i+1:>4}  {len(t['X']):>6}  {len(t['Y']):>5}  {len(t['Z']):>6}  {vol:>8}")


if __name__ == "__main__":
    main()
