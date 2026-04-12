"""
run_benchmark.py
----------------
Run the Python triCluster implementation on the paper's benchmark datasets
and evaluate against ground-truth triclusters using R, CE, RMS3 metrics.

Datasets used (mirrors what Soares et al. 2024 evaluated):
  - BaseR            (baseline mixed patterns)
  - Constant         (pattern-specific)
  - Additive
  - Multiplicative
  - OrderPreserving
  - OverlappingC / OverlappingA / OverlappingM / OverlappingOP
  - QualityC / QualityA / QualityM / QualityOP
  - ContiguityC / ContiguityA / ContiguityM / ContiguityOP

Results are printed to console and saved to results/benchmark_results.csv.
"""

import sys
import time
import csv
from pathlib import Path

# Make sure the parent is on the path so we can import from sibling dirs
REPO_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(Path(__file__).resolve().parent))

from tricluster import TriCluster
from data_io import parse_tab, parse_tsv, load_ground_truth, triclusters_to_records
from metrics import evaluate

# ---------------------------------------------------------------------------
# Dataset registry  (tab_rel may be None → fall back to tsv_rel)
# ---------------------------------------------------------------------------

BENCHMARK_ROOT = REPO_ROOT / "data" / "benchmark" / "Benchmark_Datasets"

# (tab_path_relative | None,  tsv_path_relative,  json_path_relative)
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
    # Contiguity datasets have no pre-formatted .tab → load from .tsv
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

# ---------------------------------------------------------------------------
# triCluster parameters
# Benchmark data: raw values [0, 50000], multiplicative patterns dominate
# Using the default parameters from the original C++ implementation
# ---------------------------------------------------------------------------
TRICLUSTER_PARAMS = dict(
    winsz=0.03,
    min_times=2,
    min_samples=4,
    min_genes=8,
    mode="ratio",
    del_overlap=1.0,
)


def _resolve_tab(name: str, tab_rel: str) -> Path:
    """Find the .tab file, trying the pre-formatted path first."""
    p = BENCHMARK_ROOT / tab_rel
    if p.exists():
        return p
    # Some dataset names might differ — fall back to searching
    folder = BENCHMARK_ROOT / name
    tabs = list(folder.rglob("*.tab"))
    if tabs:
        return tabs[0]
    raise FileNotFoundError(f"Cannot find .tab for {name}")


def run_dataset(name: str, tab_path: Path | None, tsv_path: Path, gt_path: Path) -> dict:
    print(f"\n{'='*60}")
    print(f"Dataset: {name}")

    # Load data — prefer pre-formatted .tab, fall back to .tsv
    if tab_path and tab_path.exists():
        print(f"  tab : {tab_path}")
        data, gene_names, sample_names = parse_tab(tab_path)
    elif tsv_path.exists():
        print(f"  tsv : {tsv_path}")
        data, gene_names, sample_names = parse_tsv(tsv_path)
    else:
        print(f"  [SKIP] No data file found")
        return {"dataset": name, "error": "no_data"}

    n_genes, n_samples, n_times = data.shape
    print(f"  shape: genes={n_genes}, samples={n_samples}, times={n_times}")

    # Load ground truth
    if not gt_path.exists():
        print(f"  [SKIP] Ground truth not found: {gt_path}")
        return {"dataset": name, "error": "no_gt"}
    ground_truth = load_ground_truth(gt_path)
    print(f"  ground truth triclusters: {len(ground_truth)}")

    # Run triCluster
    tc = TriCluster(**TRICLUSTER_PARAMS)
    t0 = time.time()
    triclusters = tc.fit(data)
    elapsed = time.time() - t0
    predicted = triclusters_to_records(triclusters)
    print(f"  found {len(predicted)} triclusters  ({elapsed:.1f}s)")

    # Evaluate
    scores = evaluate(predicted, ground_truth, n_genes, n_samples, n_times)
    print(f"  R={scores['R']:.3f}  CE={scores['CE']:.3f}  RMS3={scores['RMS3']:.3f}")

    return {
        "dataset": name,
        "n_genes": n_genes,
        "n_samples": n_samples,
        "n_times": n_times,
        "n_gt": len(ground_truth),
        "n_pred": len(predicted),
        "R": round(scores["R"], 4),
        "CE": round(scores["CE"], 4),
        "RMS3": round(scores["RMS3"], 4),
        "time_s": round(elapsed, 2),
    }


def main():
    results_dir = Path(__file__).parent / "results"
    results_dir.mkdir(exist_ok=True)
    out_csv = results_dir / "benchmark_results.csv"

    all_results = []

    for name, (tab_rel, tsv_rel, gt_rel) in DATASETS.items():
        tab_path = BENCHMARK_ROOT / tab_rel if tab_rel else None
        tsv_path = BENCHMARK_ROOT / tsv_rel
        gt_path = BENCHMARK_ROOT / gt_rel

        try:
            row = run_dataset(name, tab_path, tsv_path, gt_path)
        except Exception as e:
            print(f"[ERROR] {name}: {e}")
            import traceback; traceback.print_exc()
            row = {"dataset": name, "error": str(e)}

        all_results.append(row)

    # Save CSV
    if all_results:
        fieldnames = [k for k in all_results[0] if k != "error"]
        fieldnames = list(dict.fromkeys(
            ["dataset", "n_genes", "n_samples", "n_times",
             "n_gt", "n_pred", "R", "CE", "RMS3", "time_s"]
        ))
        with open(out_csv, "w", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames, extrasaction="ignore")
            writer.writeheader()
            writer.writerows(all_results)
        print(f"\nResults saved to {out_csv}")

    # Summary table
    print("\n" + "="*70)
    print(f"{'Dataset':<20} {'#GT':>5} {'#Pred':>6} {'R':>7} {'CE':>7} {'RMS3':>7} {'Time':>7}")
    print("-"*70)
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
