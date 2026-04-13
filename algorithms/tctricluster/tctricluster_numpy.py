"""
tctricluster_numpy.py
---------------------
Fast numpy-based implementation of TCtriCluster.

TCtriCluster (Soares 2015) = triCluster + temporal contiguity constraint:
only biclusters from *consecutive* timepoints are merged into triclusters.

This implementation reuses the full triCluster numpy infrastructure (numpy
bool gene masks, fast bitwise AND) and adds the single temporal constraint
to _expand_t.

Reference: tricluster.py by the same project — only _expand_t differs.
"""

from __future__ import annotations
import sys
from pathlib import Path

import numpy as np

# Reuse triCluster base
sys.path.insert(0, str(Path(__file__).resolve().parent.parent / "tricluster"))
from tricluster import TriCluster


class TCtriCluster(TriCluster):
    """
    TCtriCluster — same as TriCluster but with temporal contiguity.

    Triclusters are only formed from *consecutively numbered* timepoints.
    E.g., {0, 1, 2} is valid; {0, 2} is not.
    """

    def _find_triclusters(
        self,
        biclusters: list[list[tuple[frozenset, np.ndarray]]],
        n_times: int,
    ) -> list[tuple[frozenset, frozenset, frozenset]]:
        results: list[tuple[frozenset, frozenset, frozenset]] = []

        self._expand_t_contiguous(
            cur_times=[],
            cur_samples=None,
            cur_genes=None,
            candidates=list(range(n_times)),
            biclusters=biclusters,
            results=results,
        )

        # Deduplicate
        seen: set = set()
        unique = []
        for tc in results:
            key = (tc[0], tc[1], tc[2])
            if key not in seen:
                seen.add(key)
                unique.append(tc)
        return unique

    def _expand_t_contiguous(
        self,
        cur_times: list[int],
        cur_samples,
        cur_genes,
        candidates: list[int],
        biclusters: list,
        results: list,
    ):
        if cur_times and len(cur_times) >= self.min_times:
            if (
                cur_samples is not None
                and len(cur_samples) >= self.min_samples
                and cur_genes is not None
                and int(cur_genes.sum()) >= self.min_genes
            ):
                results.append((
                    frozenset(np.where(cur_genes)[0]),
                    cur_samples,
                    frozenset(cur_times),
                ))

        for idx, t in enumerate(candidates):
            # ── TEMPORAL CONTIGUITY CONSTRAINT ───────────────────────────────
            # Only extend to the immediate next timepoint.
            if cur_times and t != cur_times[-1] + 1:
                continue
            # ─────────────────────────────────────────────────────────────────

            remaining = candidates[idx + 1:]
            for (bic_samples, bic_genes) in biclusters[t]:
                if cur_times:
                    new_samples = cur_samples & bic_samples
                    new_genes = cur_genes & bic_genes
                else:
                    new_samples = bic_samples
                    new_genes = bic_genes.copy()

                if (
                    len(new_samples) >= self.min_samples
                    and int(new_genes.sum()) >= self.min_genes
                ):
                    self._expand_t_contiguous(
                        cur_times=cur_times + [t],
                        cur_samples=new_samples,
                        cur_genes=new_genes,
                        candidates=remaining,
                        biclusters=biclusters,
                        results=results,
                    )
