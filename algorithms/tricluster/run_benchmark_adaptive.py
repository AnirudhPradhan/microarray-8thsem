"""
run_benchmark_adaptive.py
-------------------------
Same as run_benchmark.py but uses adaptive_winsz to compute a per-dataset
window size instead of the fixed winsz=0.03.

Results saved to results/benchmark_results_adaptive.csv.
Run both scripts then compare to see the improvement on Quality datasets.
"""

import sys
import time
import csv
from pathlib import Path

import numpy as np

REPO_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(Path(__file__).resolve().parent))

from tricluster import TriCluster
from data_io import parse_tab, parse_tsv, load_ground_truth, triclusters_to_records
from metrics import evaluate
from adaptive_winsz import compute_adaptive_winsz

BENCHMARK_ROOT = REPO_ROOT / "data" / "benchmark" / "Benchmark_Datasets"

DATASETS = {
    "BaseR":           ("BaseR/Tricluster/dataset_base_r_data_formated.tab",
                        "BaseR/dataset_base_r_data.tsv",
                        "BaseR/dataset_base_r_trics.json"),
    "Constant":        ("Constant/Tricluster/dataset_constant_data_formated.tab",
                        "Constant/dataset_constant_data.tsv",
                        "Constant/dataset_constant_trics.json"),
    "Additive":        ("Additive/Tricluster/dataset_additive_data_formated.tab",
                        "Additive/dataset_additive_data.tsv",
                        "Additive/dataset_additive_trics.json"),
    "Multiplicative":  ("Multiplicative/Tricluster/dataset_multiplicative_data_formated.tab",
                        "Multiplicative/dataset_multiplicative_data.tsv",
                        "Multiplicative/dataset_multiplicative_trics.json"),
    "OrderPreserving": ("OrderPreserving/Tricluster/dataset_order_preserving_data_formated.tab",
                        "OrderPreserving/dataset_order_preserving_data.tsv",
                        "OrderPreserving/dataset_order_preserving_trics.json"),
    "OverlappingC":    ("OverlappingC/Tricluster/dataset_ov_constant_data_formated.tab",
                        "OverlappingC/dataset_ov_constant_data.tsv",
                        "OverlappingC/dataset_ov_constant_trics.json"),
    "OverlappingA":    ("OverlappingA/Tricluster/dataset_ov_additive_data_formated.tab",
                        "OverlappingA/dataset_ov_additive_data.tsv",
                        "OverlappingA/dataset_ov_additive_trics.json"),
    "OverlappingM":    ("OverlappingM/Tricluster/dataset_ov_multiplicative_data_formated.tab",
                        "OverlappingM/dataset_ov_multiplicative_data.tsv",
                        "OverlappingM/dataset_ov_multiplicative_trics.json"),
    "OverlappingOP":   ("OverlappingOP/Tricluster/dataset_ov_order_preserving_data_formated.tab",
                        "OverlappingOP/dataset_ov_order_preserving_data.tsv",
                        "OverlappingOP/dataset_ov_order_preserving_trics.json"),
    "QualityC":        ("QualityC/Tricluster/dataset_quality_constant_data_formated.tab",
                        "QualityC/dataset_quality_constant_data.tsv",
                        "QualityC/dataset_quality_constant_trics.json"),
    "QualityA":        ("QualityA/Tricluster/dataset_quality_additive_data_formated.tab",
                        "QualityA/dataset_quality_additive_data.tsv",
                        "QualityA/dataset_quality_additive_trics.json"),
    "QualityM":        ("QualityM/Tricluster/dataset_quality_multiplicative_data_formated.tab",
                        "QualityM/dataset_quality_multiplicative_data.tsv",
                        "QualityM/dataset_quality_multiplicative_trics.json"),
    "QualityOP":       ("QualityOP/Tricluster/dataset_quality_order_preserving_data_formated.tab",
                        "QualityOP/dataset_quality_order_preserving_data.tsv",
                        "QualityOP/dataset_quality_order_preserving_trics.json"),
    "ContiguityC":     (None,
                        "ContiguityC/dataset_contiguity_constant_data.tsv",
                        "ContiguityC/dataset_contiguity_constant_trics.json"),
    "ContiguityA":     (None,
                        "ContiguityA/dataset_contiguity_additive_data.tsv",
                        "ContiguityA/dataset_contiguity_additive_trics.json"),
    "ContiguityM":     (None,
                        "ContiguityM/dataset_contiguity_multiplicative_data.tsv",
                        "ContiguityM/dataset_contiguity_multiplicative_trics.json"),
    "ContiguityOP":    (None,
                        "ContiguityOP/dataset_contiguity_order_preserving_data.tsv",
                        "ContiguityOP/dataset_contiguity_order_preserving_trics.json"),
}

BASE_PARAMS = dict(
    min_times=2,
    min_samples=4,
    min_genes=8,
    mode="ratio",
    del_overlap=1.0,
)


def run_dataset(name: str, tab_path, tsv_path: Path, gt_path: Path) -> dict:
    print(f"\n{'='*60}")
    print(f"Dataset: {name}")

    if tab_path and tab_path.exists():
        data, gene_names, sample_names = parse_tab(tab_path)
    elif tsv_path.exists():
        data, gene_names, sample_names = parse_tsv(tsv_path)
    else:
        print("  [SKIP] No data file found")
        return {"dataset": name, "error": "no_data"}

    n_genes, n_samples, n_times = data.shape
    print(f"  shape: genes={n_genes}, samples={n_samples}, times={n_times}")

    if not gt_path.exists():
        print(f"  [SKIP] Ground truth not found")
        return {"dataset": name, "error": "no_gt"}
    ground_truth = load_ground_truth(gt_path)
    print(f"  ground truth triclusters: {len(ground_truth)}")

    # Compute adaptive winsz from this dataset
    winsz = compute_adaptive_winsz(data, target_fraction=0.10)
    print(f"  adaptive winsz = {winsz:.4f}  (baseline was 0.0300)")

    tc = TriCluster(winsz=winsz, **BASE_PARAMS)
    t0 = time.time()
    triclusters = tc.fit(data)
    elapsed = time.time() - t0
    predicted = triclusters_to_records(triclusters)
    print(f"  found {len(predicted)} triclusters  ({elapsed:.1f}s)")

    scores = evaluate(predicted, ground_truth, n_genes, n_samples, n_times)
    print(f"  R={scores['R']:.4f}  CE={scores['CE']:.4f}  RMS3={scores['RMS3']:.4f}")

    return {
        "dataset":   name,
        "n_genes":   n_genes,
        "n_samples": n_samples,
        "n_times":   n_times,
        "n_gt":      len(ground_truth),
        "n_pred":    len(predicted),
        "winsz":     round(winsz, 4),
        "R":         round(scores["R"], 4),
        "CE":        round(scores["CE"], 4),
        "RMS3":      round(scores["RMS3"], 4),
        "time_s":    round(elapsed, 2),
    }


def main():
    results_dir = Path(__file__).parent / "results"
    results_dir.mkdir(exist_ok=True)
    out_csv = results_dir / "benchmark_results_adaptive.csv"

    all_results = []

    for name, (tab_rel, tsv_rel, gt_rel) in DATASETS.items():
        tab_path = BENCHMARK_ROOT / tab_rel if tab_rel else None
        tsv_path = BENCHMARK_ROOT / tsv_rel
        gt_path  = BENCHMARK_ROOT / gt_rel

        try:
            row = run_dataset(name, tab_path, tsv_path, gt_path)
        except Exception as e:
            print(f"[ERROR] {name}: {e}")
            import traceback; traceback.print_exc()
            row = {"dataset": name, "error": str(e)}

        all_results.append(row)

    fieldnames = ["dataset", "n_genes", "n_samples", "n_times",
                  "n_gt", "n_pred", "winsz", "R", "CE", "RMS3", "time_s"]
    with open(out_csv, "w", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames, extrasaction="ignore")
        writer.writeheader()
        writer.writerows(r for r in all_results if "error" not in r)
    print(f"\nResults saved to {out_csv}")

    # ── Side-by-side comparison ──────────────────────────────────────────
    # Load baseline for comparison
    baseline_csv = results_dir / "benchmark_results.csv"
    baseline = {}
    if baseline_csv.exists():
        import csv as _csv
        with open(baseline_csv) as f:
            for row in _csv.DictReader(f):
                baseline[row["dataset"]] = float(row["R"])

    print("\n" + "="*75)
    print(f"{'Dataset':<20} {'winsz':>7} {'R (adaptive)':>13} {'R (baseline)':>13} {'Delta R':>8}")
    print("-"*75)
    for r in all_results:
        if "error" in r:
            print(f"{r['dataset']:<20}  ERROR")
            continue
        base_r = baseline.get(r["dataset"], float("nan"))
        delta  = r["R"] - base_r if not np.isnan(base_r) else float("nan")
        delta_str = f"{delta:+.4f}" if not np.isnan(delta) else "    n/a"
        print(
            f"{r['dataset']:<20} "
            f"{r['winsz']:>7.4f} "
            f"{r['R']:>13.4f} "
            f"{base_r:>13.4f} "
            f"{delta_str:>8}"
        )


if __name__ == "__main__":
    main()
