"""
triCluster — Python re-implementation (optimised with numpy bitmasks)
Based on: Zhao et al., "triCluster: An Effective Algorithm for Mining Coherent
Clusters in 3D Microarray Data" (SIGMOD 2005)

Algorithm
---------
Phase 1  Build edge matrix: for each time slice t and each sample pair (i,j),
         find all groups of genes whose expression ratios vj/vi fall within a
         multiplicative window [r, r*(1+winsz)].  Each such group is an "edge"
         (gene-set) for (i,j,t).  For log-scale / negative data use
         mode='additive': window on differences |vj−vi| ≤ winsz.

Phase 2  EXPAND — recursive enumeration of maximal (sample × gene) biclusters
         at each time slice.  Gene sets are stored as numpy bool arrays for
         fast bitwise intersection.  Only non-dominated biclusters are kept.

Phase 3  EXPAND_T — merge compatible biclusters across time → triclusters.

Phase 4  Post-process: remove strict-subset triclusters.
"""

from __future__ import annotations
import numpy as np


# ---------------------------------------------------------------------------
# Public interface
# ---------------------------------------------------------------------------

class TriCluster:
    """
    Parameters
    ----------
    winsz      : multiplicative tolerance (ratio mode) or absolute tolerance
                 (additive mode).
    min_times  : minimum number of time points in a tricluster.
    min_samples: minimum number of samples/conditions.
    min_genes  : minimum number of genes.
    mode       : 'ratio'    — multiplicative window (raw counts, ≥ 0)
                 'additive' — absolute-range window  (log-scale / negative)
    del_overlap: fraction threshold for removing contained triclusters (1.0 =
                 remove strict subsets).
    """

    def __init__(
        self,
        winsz: float = 0.03,
        min_times: int = 2,
        min_samples: int = 4,
        min_genes: int = 8,
        mode: str = "ratio",
        del_overlap: float = 1.0,
    ):
        self.winsz = winsz
        self.min_times = min_times
        self.min_samples = min_samples
        self.min_genes = min_genes
        self.mode = mode
        self.del_overlap = del_overlap

    # ------------------------------------------------------------------
    def fit(self, data: np.ndarray) -> list[tuple[frozenset, frozenset, frozenset]]:
        """
        Parameters
        ----------
        data : ndarray, shape (n_genes, n_samples, n_times)

        Returns
        -------
        List of (gene_set, sample_set, time_set) — frozensets of 0-based indices.
        """
        n_genes, n_samples, n_times = data.shape

        # Phase 1 — edge matrix (numpy bool masks)
        edges = self._build_edges(data, n_genes, n_samples, n_times)

        # Phase 2 — biclusters per time slice
        biclusters: list[list[tuple[frozenset, np.ndarray]]] = []
        for t in range(n_times):
            bics = self._find_biclusters(edges[t], n_samples, n_genes)
            biclusters.append(bics)

        # Phase 3 — merge into triclusters
        triclusters = self._find_triclusters(biclusters, n_times)

        # Phase 4 — post-process
        triclusters = self._remove_contained(triclusters)
        return triclusters

    # ------------------------------------------------------------------
    # Phase 1 — build edge matrix (numpy bool masks)
    # ------------------------------------------------------------------

    def _build_edges(self, data, n_genes, n_samples, n_times):
        """
        edges[t][(i,j)] = list of numpy bool arrays of length n_genes
        """
        edges = []
        for t in range(n_times):
            edges_t: dict[tuple[int, int], list[np.ndarray]] = {}
            for i in range(n_samples):
                for j in range(i + 1, n_samples):
                    masks = self._compute_edge_masks(data, i, j, t, n_genes)
                    if masks:
                        edges_t[(i, j)] = masks
            edges.append(edges_t)
        return edges

    def _compute_edge_masks(
        self, data: np.ndarray, si: int, sj: int, t: int, n_genes: int
    ) -> list[np.ndarray]:
        """Return numpy bool masks for all valid ratio/diff windows."""
        vi_arr = data[:, si, t]
        vj_arr = data[:, sj, t]

        # Build (score, gene_index) pairs
        valid = ~(np.isnan(vi_arr) | np.isnan(vj_arr))
        if self.mode == "ratio":
            valid &= (vi_arr != 0) & (vj_arr != 0)
            with np.errstate(divide="ignore", invalid="ignore"):
                scores = np.where(valid, vj_arr / vi_arr, np.nan)
            valid &= (scores > 0)
        else:  # additive
            scores = np.where(valid, vj_arr - vi_arr, np.nan)

        gene_indices = np.where(valid)[0]
        if len(gene_indices) < self.min_genes:
            return []

        vals = scores[gene_indices]
        order = np.argsort(vals)
        sorted_genes = gene_indices[order]
        sorted_vals = vals[order]

        # Find the single LARGEST window (matches original C++ behaviour)
        n = len(sorted_genes)
        best_size = 0
        best_left = 0
        right = 0

        for left in range(n):
            if right < left:
                right = left
            while (
                right + 1 < n
                and self._within_window(sorted_vals[left], sorted_vals[right + 1])
            ):
                right += 1
            size = right - left + 1
            if size > best_size:
                best_size = size
                best_left = left

        if best_size < self.min_genes:
            return []

        mask = np.zeros(n_genes, dtype=bool)
        mask[sorted_genes[best_left : best_left + best_size]] = True
        return [mask]

    def _within_window(self, lo: float, hi: float) -> bool:
        if self.mode == "ratio":
            return hi <= lo * (1.0 + self.winsz)
        return (hi - lo) <= self.winsz

    # ------------------------------------------------------------------
    # Phase 2 — recursive bicluster enumeration (EXPAND)
    # Uses numpy bool arrays for gene sets (fast bitwise ops)
    # Keeps only non-dominated biclusters
    # ------------------------------------------------------------------

    def _find_biclusters(
        self,
        edges_t: dict[tuple[int, int], list[np.ndarray]],
        n_samples: int,
        n_genes: int,
    ) -> list[tuple[frozenset, np.ndarray]]:
        """Return maximal non-dominated (sample_set, gene_mask) biclusters."""
        all_genes = np.ones(n_genes, dtype=bool)
        results: list[tuple[frozenset, np.ndarray]] = []

        self._expand(
            cur_samples=[],
            cur_genes=all_genes,
            candidates=list(range(n_samples)),
            edges=edges_t,
            n_genes=n_genes,
            results=results,
        )

        return results

    def _expand(
        self,
        cur_samples: list[int],
        cur_genes: np.ndarray,
        candidates: list[int],
        edges: dict,
        n_genes: int,
        results: list,
    ):
        # Record if meets minimum size and is not dominated
        if (
            len(cur_samples) >= self.min_samples
            and int(cur_genes.sum()) >= self.min_genes
        ):
            s_set = frozenset(cur_samples)
            self._add_if_maximal(s_set, cur_genes, results)

        for idx, x in enumerate(candidates):
            remaining = candidates[idx + 1 :]

            if not cur_samples:
                # First sample added — gene set starts as full universe
                self._expand(
                    cur_samples=[x],
                    cur_genes=cur_genes,
                    candidates=remaining,
                    edges=edges,
                    n_genes=n_genes,
                    results=results,
                )
            else:
                y = cur_samples[-1]
                pair = (min(y, x), max(y, x))
                edge_list = edges.get(pair, [])
                for edge_mask in edge_list:
                    new_genes = cur_genes & edge_mask  # numpy bitwise AND
                    if int(new_genes.sum()) >= self.min_genes:
                        self._expand(
                            cur_samples=cur_samples + [x],
                            cur_genes=new_genes,
                            candidates=remaining,
                            edges=edges,
                            n_genes=n_genes,
                            results=results,
                        )

    def _add_if_maximal(
        self,
        s_set: frozenset,
        g_mask: np.ndarray,
        results: list,
    ):
        """Add (s_set, g_mask) only if no existing result strictly dominates it."""
        for (es, eg) in results:
            if s_set <= es and np.all(~g_mask | eg):  # g_mask ⊆ eg
                return  # new is a subset of existing → skip
        results.append((s_set, g_mask))

    # ------------------------------------------------------------------
    # Phase 3 — merge biclusters across time (EXPAND_T)
    # ------------------------------------------------------------------

    def _find_triclusters(
        self,
        biclusters: list[list[tuple[frozenset, np.ndarray]]],
        n_times: int,
    ) -> list[tuple[frozenset, frozenset, frozenset]]:
        results: list[tuple[frozenset, frozenset, frozenset]] = []

        self._expand_t(
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

    def _expand_t(
        self,
        cur_times: list[int],
        cur_samples,        # frozenset | None
        cur_genes,          # np.ndarray | None
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
                results.append(
                    (
                        frozenset(np.where(cur_genes)[0]),
                        cur_samples,
                        frozenset(cur_times),
                    )
                )

        for idx, t in enumerate(candidates):
            remaining = candidates[idx + 1 :]
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
                    self._expand_t(
                        cur_times=cur_times + [t],
                        cur_samples=new_samples,
                        cur_genes=new_genes,
                        candidates=remaining,
                        biclusters=biclusters,
                        results=results,
                    )

    # ------------------------------------------------------------------
    # Phase 4 — remove strict-subset triclusters
    # ------------------------------------------------------------------

    def _remove_contained(
        self, triclusters: list[tuple[frozenset, frozenset, frozenset]]
    ) -> list[tuple[frozenset, frozenset, frozenset]]:
        keep = []
        for i, (gi, si, zi) in enumerate(triclusters):
            dominated = False
            for j, (gj, sj, zj) in enumerate(triclusters):
                if i == j:
                    continue
                if gi <= gj and si <= sj and zi <= zj and (gi, si, zi) != (gj, sj, zj):
                    dominated = True
                    break
            if not dominated:
                keep.append((gi, si, zi))
        return keep
