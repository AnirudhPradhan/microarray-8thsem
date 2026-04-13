"""
run_benchmark.py  (δ-Trimax)
------------------------------
Run δ-Trimax on the Soares et al. 2024 benchmark datasets.

Data format: D[time, gene, condition] — loaded from pre-formatted .npy files
in Benchmark_Datasets/*/DeltaTrimax/*.npy (shape: n_times × n_genes × n_conditions).

Parameters:
  delta  = data_variance × 0.005 (computed per dataset, ~0.5% of variance)
  lambda = 1.2 (standard Cheng-Church multiplier)
  n_triclusters = 50 (enough to cover GT counts of 30–70)
"""

import sys
import time
import csv
import numpy as np
from pathlib import Path

REPO_ROOT      = Path(__file__).resolve().parent.parent.parent
TRICLUSTER_DIR = REPO_ROOT / "algorithms" / "tricluster"

sys.path.insert(0, str(Path(__file__).resolve().parent))
sys.path.insert(0, str(TRICLUSTER_DIR))

from DeltaTrimax import DeltaTrimax
from data_io import load_ground_truth
from metrics import evaluate

BENCHMARK_ROOT = REPO_ROOT / "data" / "benchmark" / "Benchmark_Datasets"

# ---------------------------------------------------------------------------
# Dataset registry — .npy pre-formatted files (n_times × n_genes × n_conds)
# ---------------------------------------------------------------------------
DATASETS = {
    "BaseR":           ("BaseR/DeltaTrimax/dataset_base_r_data_formatted.npy",
                        "BaseR/dataset_base_r_trics.json"),
    "Constant":        ("Constant/DeltaTrimax/dataset_constant_data_formatted.npy",
                        "Constant/dataset_constant_trics.json"),
    "Additive":        ("Additive/DeltaTrimax/dataset_additive_data_formatted.npy",
                        "Additive/dataset_additive_trics.json"),
    "Multiplicative":  ("Multiplicative/DeltaTrimax/dataset_multiplicative_data_formatted.npy",
                        "Multiplicative/dataset_multiplicative_trics.json"),
    "OrderPreserving": ("OrderPreserving/DeltaTrimax/dataset_order_preserving_data_formatted.npy",
                        "OrderPreserving/dataset_order_preserving_trics.json"),
    "OverlappingC":    ("OverlappingC/DeltaTrimax/dataset_ov_constant_data.npy",
                        "OverlappingC/dataset_ov_constant_trics.json"),
    "OverlappingA":    ("OverlappingA/DeltaTrimax/dataset_ov_additive_data.npy",
                        "OverlappingA/dataset_ov_additive_trics.json"),
    "OverlappingM":    ("OverlappingM/DeltaTrimax/dataset_ov_multiplicative_data.npy",
                        "OverlappingM/dataset_ov_multiplicative_trics.json"),
    "OverlappingOP":   ("OverlappingOP/DeltaTrimax/dataset_ov_order_preserving_data.npy",
                        "OverlappingOP/dataset_ov_order_preserving_trics.json"),
    "QualityC":        ("QualityC/DeltaTrimax/dataset_quality_constant_data.npy",
                        "QualityC/dataset_quality_constant_trics.json"),
    "QualityA":        ("QualityA/DeltaTrimax/dataset_quality_additive_data.npy",
                        "QualityA/dataset_quality_additive_trics.json"),
    "QualityM":        ("QualityM/DeltaTrimax/dataset_quality_multiplicative_data.npy",
                        "QualityM/dataset_quality_multiplicative_trics.json"),
    "QualityOP":       ("QualityOP/DeltaTrimax/dataset_quality_order_preserving_data.npy",
                        "QualityOP/dataset_quality_order_preserving_trics.json"),
    "ContiguityC":     ("ContiguityC/DeltaTrimax/dataset_contiguity_constant_data.npy",
                        "ContiguityC/dataset_contiguity_constant_trics.json"),
    "ContiguityA":     ("ContiguityA/DeltaTrimax/dataset_contiguity_additive_data.npy",
                        "ContiguityA/dataset_contiguity_additive_trics.json"),
    "ContiguityM":     ("ContiguityM/DeltaTrimax/dataset_contiguity_multiplicative_data.npy",
                        "ContiguityM/dataset_contiguity_multiplicative_trics.json"),
    "ContiguityOP":    ("ContiguityOP/DeltaTrimax/dataset_contiguity_order_preserving_data.npy",
                        "ContiguityOP/dataset_contiguity_order_preserving_trics.json"),
}

# Algorithm parameters
LAMBDA      = 1.2
N_TRICS     = 50   # extract up to 50 triclusters
DELTA_FRAC  = 0.005  # delta = data_variance × DELTA_FRAC


def run_dataset(name: str, npy_path: Path, gt_path: Path) -> dict:
    print(f"\n{'='*60}")
    print(f"Dataset: {name}")

    if not npy_path.exists():
        print(f"  [SKIP] .npy not found: {npy_path}")
        return {"dataset": name, "error": "no_npy"}
    if not gt_path.exists():
        print(f"  [SKIP] GT not found: {gt_path}")
        return {"dataset": name, "error": "no_gt"}

    # Load data — shape (n_times, n_genes, n_conditions)
    D = np.load(npy_path)
    n_times, n_genes, n_conds = D.shape
    print(f"  shape: times={n_times}, genes={n_genes}, conds={n_conds}")

    # Set delta relative to data variance
    delta = float(np.nanvar(D)) * DELTA_FRAC
    print(f"  delta={delta:.2f}  lambda={LAMBDA}  n_trics={N_TRICS}")

    # Load ground truth (GT uses gene=X, cond=Y, time=Z in 0-based)
    ground_truth = load_ground_truth(gt_path)
    print(f"  ground truth triclusters: {len(ground_truth)}")

    t0 = time.time()
    model = DeltaTrimax(D)
    raw = model.fit(
        delta=delta,
        lamda=LAMBDA,
        mask_mode="random",
        n_triclusters=N_TRICS,
        min_genes=2,
        min_kondisi=2,
        min_waktu=2,
        verbose=False,
    )
    elapsed = time.time() - t0

    # raw = [(gene_frozenset, kondisi_frozenset, waktu_frozenset), ...]
    # Convert to metrics.py format: {"X": genes, "Y": conditions, "Z": times}
    predicted = [
        {"X": sorted(g), "Y": sorted(k), "Z": sorted(w)}
        for g, k, w in raw
    ]
    print(f"  found {len(predicted)} triclusters  ({elapsed:.1f}s)")

    scores = evaluate(predicted, ground_truth, n_genes, n_conds, n_times)
    print(f"  R={scores['R']:.3f}  CE={scores['CE']:.3f}  RMS3={scores['RMS3']:.3f}")

    return {
        "dataset":   name,
        "n_genes":   n_genes,
        "n_conds":   n_conds,
        "n_times":   n_times,
        "n_gt":      len(ground_truth),
        "n_pred":    len(predicted),
        "R":         round(scores["R"],    4),
        "CE":        round(scores["CE"],   4),
        "RMS3":      round(scores["RMS3"], 4),
        "delta":     round(delta, 2),
        "time_s":    round(elapsed, 2),
    }


def main():
    results_dir = Path(__file__).parent / "results"
    results_dir.mkdir(exist_ok=True)
    out_csv = results_dir / "benchmark_results.csv"

    all_results = []

    for name, (npy_rel, gt_rel) in DATASETS.items():
        npy_path = BENCHMARK_ROOT / npy_rel
        gt_path  = BENCHMARK_ROOT / gt_rel

        try:
            row = run_dataset(name, npy_path, gt_path)
        except Exception as e:
            print(f"[ERROR] {name}: {e}")
            import traceback; traceback.print_exc()
            row = {"dataset": name, "error": str(e)}

        all_results.append(row)

    fieldnames = ["dataset", "n_genes", "n_conds", "n_times",
                  "n_gt", "n_pred", "R", "CE", "RMS3", "delta", "time_s"]
    with open(out_csv, "w", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames, extrasaction="ignore")
        writer.writeheader()
        writer.writerows(all_results)
    print(f"\nResults saved to {out_csv}")

    print("\n" + "="*76)
    print(f"{'Dataset':<20} {'#GT':>5} {'#Pred':>6} {'R':>7} {'CE':>7} {'RMS3':>7} {'Time':>7}")
    print("-"*76)
    for r in all_results:
        if "error" in r:
            print(f"{r['dataset']:<20}  ERROR: {r['error']}")
        else:
            print(
                f"{r.get('dataset',''):<20} "
                f"{r.get('n_gt',0):>5} "
                f"{r.get('n_pred',0):>6} "
                f"{r.get('R',0):>7.3f} "
                f"{r.get('CE',0):>7.3f} "
                f"{r.get('RMS3',0):>7.3f} "
                f"{r.get('time_s',0):>6.1f}s"
            )


if __name__ == "__main__":
    main()
