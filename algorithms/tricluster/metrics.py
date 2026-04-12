"""
metrics.py — Triclustering evaluation metrics

Implements the three metrics used in Soares et al. 2024:
  R    — Recoverability (higher is better, max 1.0)
  CE   — Clustering Error (lower is better, min 0.0)
  RMS3 — 3D Revised Match Score / Jaccard-based (higher is better, max 1.0)
"""

from __future__ import annotations
import numpy as np


def _volume(tc: dict) -> int:
    return len(tc["X"]) * len(tc["Y"]) * len(tc["Z"])


def _intersection_volume(a: dict, b: dict) -> int:
    xi = len(set(a["X"]) & set(b["X"]))
    yi = len(set(a["Y"]) & set(b["Y"]))
    zi = len(set(a["Z"]) & set(b["Z"]))
    return xi * yi * zi


def _r_score(t_pred: dict, t_true: dict) -> float:
    """r(t, t') = |t ∩ t'| / |t'|"""
    denom = _volume(t_true)
    if denom == 0:
        return 0.0
    return _intersection_volume(t_pred, t_true) / denom


def _jaccard_3d(t_pred: dict, t_true: dict) -> float:
    """Geometric mean of per-dimension Jaccard scores."""
    dims = ["X", "Y", "Z"]
    scores = []
    for d in dims:
        a_set = set(t_pred[d])
        b_set = set(t_true[d])
        union = len(a_set | b_set)
        inter = len(a_set & b_set)
        scores.append(inter / union if union > 0 else 0.0)
    return float(np.cbrt(scores[0] * scores[1] * scores[2]))


# ---------------------------------------------------------------------------
# Public metric functions
# ---------------------------------------------------------------------------

def recoverability(
    predicted: list[dict], ground_truth: list[dict]
) -> float:
    """
    R(T, T') = mean over t in T of  max_{t' in T'}  r(t, t')

    Measures what fraction of the ground-truth elements are covered by the
    best-matching predicted tricluster, averaged over all ground-truth items.
    NOTE: the paper averages over T (predicted), some versions over T' (ground truth).
    We follow the paper's eq. (5):  average over predicted triclusters.
    """
    if not predicted or not ground_truth:
        return 0.0
    total = 0.0
    for tp in predicted:
        best = max(_r_score(tp, tt) for tt in ground_truth)
        total += best
    return total / len(predicted)


def clustering_error(
    predicted: list[dict], ground_truth: list[dict],
    n_genes: int, n_samples: int, n_times: int,
) -> float:
    """
    CE(T, T') = (|U| - sum_{t in T} max_{t' in T'} r(t,t')) / |U|

    |U| = |U_T ∪ U_T'|  where U_S = union of all element triples in solution S.
    """
    # Build union of (g, s, z) triples for predicted
    pred_elements: set[tuple[int, int, int]] = set()
    for tc in predicted:
        for g in tc["X"]:
            for s in tc["Y"]:
                for z in tc["Z"]:
                    pred_elements.add((g, s, z))

    # Build union of (g, s, z) triples for ground truth
    true_elements: set[tuple[int, int, int]] = set()
    for tc in ground_truth:
        for g in tc["X"]:
            for s in tc["Y"]:
                for z in tc["Z"]:
                    true_elements.add((g, s, z))

    U = pred_elements | true_elements
    size_U = len(U)
    if size_U == 0:
        return 0.0

    # sum of best r-scores
    total_r = sum(
        max(_r_score(tp, tt) for tt in ground_truth)
        for tp in predicted
    ) if predicted and ground_truth else 0.0

    return (size_U - total_r) / size_U


def rms3(predicted: list[dict], ground_truth: list[dict]) -> float:
    """
    RMS3(T, T') = (1/|T|) * sum_{t in T, max-Jaccard t' in T'} Jaccard_3D(t, t')
    """
    if not predicted or not ground_truth:
        return 0.0
    total = 0.0
    for tp in predicted:
        best = max(_jaccard_3d(tp, tt) for tt in ground_truth)
        total += best
    return total / len(predicted)


def evaluate(
    predicted: list[dict],
    ground_truth: list[dict],
    n_genes: int,
    n_samples: int,
    n_times: int,
) -> dict[str, float]:
    """Return all three metrics as a dict."""
    return {
        "R":    recoverability(predicted, ground_truth),
        "CE":   clustering_error(predicted, ground_truth, n_genes, n_samples, n_times),
        "RMS3": rms3(predicted, ground_truth),
        "n_predicted": len(predicted),
        "n_ground_truth": len(ground_truth),
    }
