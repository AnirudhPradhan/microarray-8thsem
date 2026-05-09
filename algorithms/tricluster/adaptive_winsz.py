"""
adaptive_winsz.py
-----------------
Estimate the optimal triCluster window size from the dataset itself.

Core idea
---------
For each condition pair (i, j) at each time t, sort all gene ratios
v[g,j,t] / v[g,i,t].  The minimum-width multiplicative window that covers
exactly `min_genes` consecutive sorted ratios is the smallest window that
could ever produce a valid bicluster for that pair.

Taking the median of these per-pair minimums gives a data-driven winsz that:
  - stays small for clean data  (ratios cluster tightly → small spans)
  - grows for noisy data        (ratios spread out  → larger spans)

This is the same sliding-window logic that triCluster itself uses internally —
we just turn it into a measurement tool.
"""

import numpy as np


def compute_adaptive_winsz(
    data: np.ndarray,
    target_fraction: float = 0.10,
    percentile: float = 50,
    min_winsz: float = 0.01,
    max_winsz: float = 0.40,
) -> float:
    """
    Compute adaptive window size from the dataset's empirical ratio spread.

    For each condition pair at each time slice, find the tightest multiplicative
    window that covers target_fraction of all valid genes (in log-ratio space).
    The percentile of these per-pair minimum spans gives the natural window width
    needed for this dataset's noise level:
      - Clean datasets  (0% noise) : small spans → winsz ~0.01–0.06
      - Noisy datasets (10% noise) : wider spans → winsz ~0.06–0.15

    Parameters
    ----------
    data            : ndarray (n_genes, n_samples, n_times) — raw positive values
    target_fraction : fraction of genes the window must cover per condition pair
                      0.10 = 10% → 50 genes for a 500-gene dataset
    percentile      : which percentile of per-pair spans to use as winsz
                      50 = median (balanced between tight/loose condition pairs)
    min_winsz       : hard floor
    max_winsz       : hard ceiling

    Returns
    -------
    winsz : float
    """
    n_genes, n_samples, n_times = data.shape
    target_size = max(8, int(target_fraction * n_genes))
    best_spans = []

    for t in range(n_times):
        for i in range(n_samples):
            for j in range(i + 1, n_samples):
                vi = data[:, i, t]
                vj = data[:, j, t]
                valid = (vi > 0) & (vj > 0) & np.isfinite(vi) & np.isfinite(vj)
                n_valid = int(valid.sum())
                if n_valid < target_size:
                    continue

                # Work in log space: log(vj/vi) = log(vj) - log(vi)
                # span in log space → convert back: winsz = exp(log_span) - 1
                log_r = np.sort(np.log(vj[valid] / vi[valid]))

                best = float("inf")
                for k in range(n_valid - target_size + 1):
                    span = log_r[k + target_size - 1] - log_r[k]
                    if span < best:
                        best = span

                if best < float("inf"):
                    best_spans.append(best)

    if not best_spans:
        return 0.03  # fallback

    log_span = float(np.percentile(best_spans, percentile))
    winsz = float(np.exp(log_span) - 1)
    return float(np.clip(winsz, min_winsz, max_winsz))
