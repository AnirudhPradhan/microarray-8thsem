"""
compute_3d_triq_metrics.py
--------------------------
Compute TRIQ / GRQ / PEQ / SPQ for 3D triclustering algorithms:
  - triCluster (Zhao & Zaki 2005)
  - TCtriCluster (temporal contiguity variant)
  - delta-Trimax (Cheng-Church 3D MSR minimization)

Runs on both datasets used for 1D/2D/TriGen comparisons:
  - QualityC  : 500 genes x 10 conds x 5 times  (ratio mode, raw counts)
  - Yeast GST : top-500 genes x 3 conds x 14 times (additive mode, log2)

For each found tricluster, data[genes, conds, times] is extracted, flattened
to 2D (genes x conds*times), and evaluated with the same GRQ/PEQ/SPQ functions
used in compute_1d_2d_triq_metrics.py — making all algorithms directly comparable.

triCluster / TCtriCluster on Yeast run with a TIMEOUT_S hard limit.
If the EXPAND phase has not completed within that time the result is
recorded as TIMEOUT and the algorithm moves on.

Outputs:
  output/3d_triq/3d_triq_per_tric.csv   -- per-tricluster rows (all datasets)
  output/3d_triq/3d_triq_summary.csv    -- mean/best per algorithm x dataset
  output/3d_triq/3d_triq_report.md      -- markdown comparison table
"""

import sys
import time
import multiprocessing as mp
import numpy as np
import pandas as pd
from pathlib import Path
from scipy.stats import spearmanr

REPO_ROOT        = Path(__file__).resolve().parent
BENCHMARK_ROOT   = REPO_ROOT / "data" / "benchmark" / "Benchmark_Datasets"
TRICLUSTER_DIR   = REPO_ROOT / "algorithms" / "tricluster"
TCTRICLUSTER_DIR = REPO_ROOT / "algorithms" / "tctricluster"
DELTATRIMAX_DIR  = REPO_ROOT / "algorithms" / "deltaTrimax"
YEAST_NPZ        = REPO_ROOT / "algorithms" / "neucom-trigen" / "data" / "yeast_gst_tensor.npz"
OUT_DIR          = REPO_ROOT / "output" / "3d_triq"

sys.path.insert(0, str(TRICLUSTER_DIR))
sys.path.insert(0, str(TCTRICLUSTER_DIR))
sys.path.insert(0, str(DELTATRIMAX_DIR))

from tricluster import TriCluster
from tctricluster_numpy import TCtriCluster
from DeltaTrimax import DeltaTrimax
from data_io import parse_tab, parse_tsv, load_yeast_npz

# TRIQ weights (same as compute_1d_2d_triq_metrics.py)
W_GRQ, W_PEQ, W_SPQ = 0.4, 0.05, 0.05

# Top-N most variable genes to use for Yeast
YEAST_TOP_N = 500

# Hard timeout (seconds) for triCluster / TCtriCluster on Yeast
TIMEOUT_S = 120


# ---------------------------------------------------------------------------
# TRIQ metric functions  (identical to compute_1d_2d_triq_metrics.py)
# ---------------------------------------------------------------------------

def grq_submatrix(X: np.ndarray) -> float:
    if X.size < 4:
        return 0.0
    row_means = X.mean(axis=1, keepdims=True)
    col_means = X.mean(axis=0, keepdims=True)
    grand     = X.mean()
    residue   = X - row_means - col_means + grand
    msr       = np.mean(residue ** 2)
    var_x     = np.var(X) + 1e-10
    return float(max(0.0, 1.0 - msr / var_x))


def peq_submatrix(X: np.ndarray) -> float:
    if X.shape[0] < 2:
        return 0.0
    cor  = np.corrcoef(X)
    if cor.size < 4:
        return 0.0
    triu = np.triu_indices(cor.shape[0], k=1)
    vals = np.abs(np.nan_to_num(cor[triu], nan=0.0))
    return float(np.mean(vals))


def spq_submatrix(X: np.ndarray) -> float:
    if X.shape[0] < 2:
        return 0.0
    try:
        res = spearmanr(X, axis=1)
        cor = np.asarray(res.statistic if hasattr(res, "statistic") else res[0])
        if cor.ndim == 0:
            return float(abs(cor))
        triu = np.triu_indices(cor.shape[0], k=1)
        vals = np.abs(np.nan_to_num(cor[triu], nan=0.0))
        return float(np.mean(vals)) if vals.size > 0 else 0.0
    except Exception:
        return 0.0


def triq_from_components(grq: float, peq: float, spq: float) -> float:
    return (W_GRQ * grq + W_PEQ * peq + W_SPQ * spq) / (W_GRQ + W_PEQ + W_SPQ)


def metrics_for_3d_block(data_gst: np.ndarray, genes, samples, times) -> dict:
    g = sorted(genes)
    s = sorted(samples)
    t = sorted(times)
    if not g or not s or not t:
        return {"TRIQ": 0.0, "GRQ": 0.0, "PEQ": 0.0, "SPQ": 0.0}
    block_3d = data_gst[np.ix_(g, s, t)]
    block_2d = block_3d.reshape(len(g), -1)
    grq  = grq_submatrix(block_2d)
    peq  = peq_submatrix(block_2d)
    spq  = spq_submatrix(block_2d)
    triq = triq_from_components(grq, peq, spq)
    return {"TRIQ": triq, "GRQ": grq, "PEQ": peq, "SPQ": spq}


# ---------------------------------------------------------------------------
# Dataset loaders
# ---------------------------------------------------------------------------

def load_qualityc_gst() -> np.ndarray:
    tab = BENCHMARK_ROOT / "QualityC" / "Tricluster" / "dataset_quality_constant_data_formated.tab"
    tsv = BENCHMARK_ROOT / "QualityC" / "dataset_quality_constant_data.tsv"
    if tab.exists():
        data, _, _ = parse_tab(tab)
    else:
        data, _, _ = parse_tsv(tsv)
    return data


def load_yeast_gst() -> np.ndarray:
    data, genes, _, _ = load_yeast_npz(YEAST_NPZ)
    n_genes = data.shape[0]
    if n_genes > YEAST_TOP_N:
        variances = data.reshape(n_genes, -1).var(axis=1)
        idx = variances.argsort()[::-1][:YEAST_TOP_N]
        data = data[idx]
        print(f"  Selected top {YEAST_TOP_N} of {n_genes} most-variable genes")
    return data


# ---------------------------------------------------------------------------
# Subprocess worker — runs in a separate process so we can kill it on timeout
# ---------------------------------------------------------------------------

def _tricluster_worker(queue: mp.Queue, algo_cls, data_gst: np.ndarray, params: dict):
    """Runs in a child process; puts result list into queue."""
    sys.path.insert(0, str(TRICLUSTER_DIR))
    sys.path.insert(0, str(TCTRICLUSTER_DIR))
    tc  = algo_cls(**params)
    raw = tc.fit(data_gst)
    queue.put(raw)


def run_with_timeout(algo_cls, data_gst: np.ndarray, params: dict,
                     label: str, timeout: int):
    """
    Run triCluster or TCtriCluster in a child process.
    Returns raw tricluster list, or None on timeout.
    """
    queue = mp.Queue()
    p = mp.Process(target=_tricluster_worker,
                   args=(queue, algo_cls, data_gst, params))
    p.start()
    p.join(timeout=timeout)

    if p.is_alive():
        print(f"  {label}: TIMEOUT after {timeout}s — killing process")
        p.terminate()
        p.join()
        return None

    if not queue.empty():
        return queue.get()
    return []


# ---------------------------------------------------------------------------
# Algorithm runners
# ---------------------------------------------------------------------------

QUALITYC_PARAMS = dict(winsz=0.03, min_times=2, min_samples=4,
                        min_genes=8, mode="ratio", del_overlap=1.0)

# winsz=0.3 keeps EXPAND fast (tight windows → few genes per edge).
# min_genes=10 is low enough to actually find triclusters.
YEAST_PARAMS = dict(winsz=0.3, min_times=3, min_samples=2,
                    min_genes=10, mode="additive", del_overlap=1.0)


def run_tricluster(data_gst: np.ndarray, params: dict,
                   dataset: str, use_timeout: bool = False) -> list[dict]:
    t0 = time.time()
    if use_timeout:
        raw = run_with_timeout(TriCluster, data_gst, params,
                               "triCluster", TIMEOUT_S)
        timed_out = raw is None
        raw = raw or []
    else:
        tc  = TriCluster(**params)
        raw = tc.fit(data_gst)
        timed_out = False
    elapsed = time.time() - t0

    status = f"TIMEOUT after {TIMEOUT_S}s" if timed_out else f"{len(raw)} triclusters in {elapsed:.1f}s"
    print(f"  triCluster: {status}")

    rows = []
    for i, (genes, samples, times) in enumerate(raw):
        m = metrics_for_3d_block(data_gst, genes, samples, times)
        rows.append({"Algorithm": "triCluster", "Dataset": dataset, "Tric": i + 1,
                     "n_genes": len(genes), "n_conds": len(samples), "n_times": len(times),
                     "timed_out": timed_out, **m})
    return rows


def run_tctricluster(data_gst: np.ndarray, params: dict,
                     dataset: str, use_timeout: bool = False) -> list[dict]:
    t0 = time.time()
    if use_timeout:
        raw = run_with_timeout(TCtriCluster, data_gst, params,
                               "TCtriCluster", TIMEOUT_S)
        timed_out = raw is None
        raw = raw or []
    else:
        tc  = TCtriCluster(**params)
        raw = tc.fit(data_gst)
        timed_out = False
    elapsed = time.time() - t0

    status = f"TIMEOUT after {TIMEOUT_S}s" if timed_out else f"{len(raw)} triclusters in {elapsed:.1f}s"
    print(f"  TCtriCluster: {status}")

    rows = []
    for i, (genes, samples, times) in enumerate(raw):
        m = metrics_for_3d_block(data_gst, genes, samples, times)
        rows.append({"Algorithm": "TCtriCluster", "Dataset": dataset, "Tric": i + 1,
                     "n_genes": len(genes), "n_conds": len(samples), "n_times": len(times),
                     "timed_out": timed_out, **m})
    return rows


def run_deltatrimax(data_gst: np.ndarray, dataset: str) -> list[dict]:
    D     = data_gst.transpose(2, 0, 1)
    delta = float(np.nanvar(D)) * 0.005
    model = DeltaTrimax(D)
    t0    = time.time()
    raw   = model.fit(delta=delta, lamda=1.2, mask_mode="random", n_triclusters=50,
                      min_genes=2, min_kondisi=2, min_waktu=2, verbose=False)
    elapsed = time.time() - t0
    print(f"  delta-Trimax: {len(raw)} triclusters in {elapsed:.1f}s")
    rows = []
    for i, (genes, conds, times) in enumerate(raw):
        m = metrics_for_3d_block(data_gst, genes, conds, times)
        rows.append({"Algorithm": "delta-Trimax", "Dataset": dataset, "Tric": i + 1,
                     "n_genes": len(genes), "n_conds": len(conds), "n_times": len(times),
                     "timed_out": False, **m})
    return rows


def run_dataset(name: str, data_gst: np.ndarray,
                tc_params: dict, use_timeout: bool) -> list[dict]:
    all_rows = []
    for fn, label in [
        (lambda d: run_tricluster(d, tc_params, name, use_timeout),  "triCluster"),
        (lambda d: run_tctricluster(d, tc_params, name, use_timeout), "TCtriCluster"),
        (lambda d: run_deltatrimax(d, name),                           "delta-Trimax"),
    ]:
        print(f"\n  Running {label} on {name}...")
        try:
            all_rows.extend(fn(data_gst))
        except Exception as e:
            print(f"  [ERROR] {label}: {e}")
            import traceback; traceback.print_exc()
    return all_rows


# ---------------------------------------------------------------------------
# Reporting helpers
# ---------------------------------------------------------------------------

REFERENCE = {
    "QualityC": [
        ("TriGen 3D",               0.700, 0.699, 0.749, 0.501, 0.502),
        ("2D SpectralBiclustering", 0.417, 0.258, 0.240, 0.366, 0.367),
        ("2D SpectralCoclustering", 0.271, 0.196, 0.174, 0.296, 0.294),
        ("1D Agglomerative",        0.220, 0.118, 0.113, 0.143, 0.142),
        ("1D K-Means",              0.106, 0.092, 0.084, 0.123, 0.122),
    ],
    "Yeast": [
        ("TriGen 3D",               0.269, 0.252, 0.532, 0.390, 0.381),
        ("2D SpectralBiclustering", 0.497, 0.382, 0.380, 0.402, 0.376),
        ("2D SpectralCoclustering", 0.395, 0.275, 0.271, 0.316, 0.302),
        ("1D Agglomerative",        0.422, 0.223, 0.210, 0.278, 0.272),
        ("1D K-Means",              0.373, 0.220, 0.209, 0.263, 0.262),
    ],
}


def make_summary(df: pd.DataFrame) -> pd.DataFrame:
    rows = []
    for dataset in df["Dataset"].unique():
        for algo in ["triCluster", "TCtriCluster", "delta-Trimax"]:
            sub = df[(df["Dataset"] == dataset) & (df["Algorithm"] == algo)]
            if len(sub) == 0:
                continue
            timed_out = bool(sub["timed_out"].any())
            if timed_out or sub["TRIQ"].isna().all():
                rows.append({"Algorithm": algo, "Dataset": dataset,
                             "n_triclusters": "TIMEOUT", "TRIQ Best": "—",
                             "TRIQ Mean": "—", "GRQ": "—", "PEQ": "—", "SPQ": "—"})
            else:
                rows.append({
                    "Algorithm":     algo,
                    "Dataset":       dataset,
                    "n_triclusters": len(sub),
                    "TRIQ Best":     round(float(sub["TRIQ"].max()),  3),
                    "TRIQ Mean":     round(float(sub["TRIQ"].mean()), 3),
                    "GRQ":           round(float(sub["GRQ"].mean()),  3),
                    "PEQ":           round(float(sub["PEQ"].mean()),  3),
                    "SPQ":           round(float(sub["SPQ"].mean()),  3),
                })
    return pd.DataFrame(rows)


def build_report(summary_df: pd.DataFrame) -> str:
    lines = [
        "# TRIQ / GRQ / PEQ / SPQ for 3D Triclustering Algorithms",
        "",
        "Each tricluster's 3D subblock is extracted and flattened to",
        "(genes x conds*times) before computing metrics — identical method",
        "to the 1D/2D evaluations in `compute_1d_2d_triq_metrics.py`.",
        "",
    ]
    for dataset in summary_df["Dataset"].unique():
        sub = summary_df[summary_df["Dataset"] == dataset]
        desc = {
            "QualityC": "500 genes x 10 conditions x 5 timepoints (synthetic, planted triclusters)",
            "Yeast":    f"top-{YEAST_TOP_N} genes x 3 conditions x 14 timepoints (real Spellman data, log2)",
        }.get(dataset, dataset)
        lines += [
            f"## {dataset}  —  {desc}",
            "",
            "| Method | #Triclusters | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |",
            "| :----- | -----------: | --------: | --------: | --: | --: | --: |",
        ]
        for _, r in sub.iterrows():
            nt = r["n_triclusters"]
            tb = r["TRIQ Best"]
            tm = r["TRIQ Mean"]
            grq = r["GRQ"]; peq = r["PEQ"]; spq = r["SPQ"]
            if nt == "TIMEOUT":
                lines.append(f"| {r['Algorithm']} | TIMEOUT | — | — | — | — | — |")
            elif nt == 0:
                lines.append(f"| {r['Algorithm']} | 0 (none found) | — | — | — | — | — |")
            else:
                lines.append(
                    f"| {r['Algorithm']} | {nt} "
                    f"| {tb:.3f} | {tm:.3f} "
                    f"| {grq:.3f} | {peq:.3f} | {spq:.3f} |"
                )
        for (method, best, mean, grq, peq, spq) in REFERENCE.get(dataset, []):
            lines.append(
                f"| {method} | — "
                f"| {best:.3f} | {mean:.3f} "
                f"| {grq:.3f} | {peq:.3f} | {spq:.3f} |"
            )
        lines.append("")
    return "\n".join(lines)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main():
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    all_rows: list[dict] = []

    # --- QualityC (no timeout needed — always fast) ---
    print("\n" + "=" * 60)
    print("Dataset: QualityC")
    print("=" * 60)
    data_qc = load_qualityc_gst()
    print(f"  shape: genes={data_qc.shape[0]}, conds={data_qc.shape[1]}, times={data_qc.shape[2]}")
    all_rows.extend(run_dataset("QualityC", data_qc, QUALITYC_PARAMS, use_timeout=False))

    # --- Yeast (timeout guards triCluster / TCtriCluster) ---
    print("\n" + "=" * 60)
    print("Dataset: Yeast")
    print("=" * 60)
    data_yeast = load_yeast_gst()
    print(f"  shape: genes={data_yeast.shape[0]}, conds={data_yeast.shape[1]}, times={data_yeast.shape[2]}")
    print(f"  triCluster / TCtriCluster timeout: {TIMEOUT_S}s")
    print(f"  Yeast params: winsz={YEAST_PARAMS['winsz']}, min_genes={YEAST_PARAMS['min_genes']}, "
          f"mode={YEAST_PARAMS['mode']}")
    all_rows.extend(run_dataset("Yeast", data_yeast, YEAST_PARAMS, use_timeout=True))

    # --- Save ---
    df = pd.DataFrame(all_rows)
    df.to_csv(OUT_DIR / "3d_triq_per_tric.csv", index=False)
    summary_df = make_summary(df)
    summary_df.to_csv(OUT_DIR / "3d_triq_summary.csv", index=False)
    report = build_report(summary_df)
    (OUT_DIR / "3d_triq_report.md").write_text(report, encoding="utf-8")

    # --- Console ---
    for dataset in df["Dataset"].unique():
        sub_s = summary_df[summary_df["Dataset"] == dataset]
        print(f"\n{'='*70}")
        print(f"  {dataset}")
        print(f"  {'Algorithm':<20} {'#Trics':>8} {'Best':>8} {'Mean':>8} {'GRQ':>8} {'PEQ':>8} {'SPQ':>8}")
        print(f"  {'-'*68}")
        for _, r in sub_s.iterrows():
            nt = r["n_triclusters"]
            if nt in ("TIMEOUT", 0, "0"):
                print(f"  {r['Algorithm']:<20} {str(nt):>8}")
            else:
                print(f"  {r['Algorithm']:<20} {nt:>8} "
                      f"{r['TRIQ Best']:>8.3f} {r['TRIQ Mean']:>8.3f} "
                      f"{r['GRQ']:>8.3f} {r['PEQ']:>8.3f} {r['SPQ']:>8.3f}")
    print(f"\nOutputs saved to: {OUT_DIR}")


if __name__ == "__main__":
    mp.freeze_support()   # needed on Windows
    main()
