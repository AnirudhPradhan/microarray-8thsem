"""
tctricluster_wrapper.py
-----------------------
Programmatic wrapper for TCtriCluster. Accepts a numpy array and returns
triclusters as (gene_frozenset, sample_frozenset, time_frozenset) tuples.
"""

from __future__ import annotations
import copy
import io
import sys
from contextlib import redirect_stdout
from pathlib import Path

import numpy as np

sys.path.insert(0, str(Path(__file__).parent))
from TCtriCluster import Array3D, Cubes, Cluster


def run(
    data: np.ndarray,
    winsz: float = 0.03,
    min_times: int = 2,
    min_samples: int = 4,
    min_genes: int = 8,
    del_overlap: float = 1.0,
    verbose: bool = False,
) -> list[tuple[frozenset, frozenset, frozenset]]:
    """
    Run TCtriCluster on a 3D numpy array.

    Parameters
    ----------
    data        : ndarray shape (n_genes, n_samples, n_times)
    winsz       : ratio window size (default 0.03 — same as triCluster)
    min_times   : minimum timepoints per tricluster (temporal contiguity enforced)
    min_samples : minimum samples per tricluster
    min_genes   : minimum genes per tricluster
    del_overlap : deletion threshold (1.0 = no deletion)
    verbose     : if False, suppress all print output from TCtriCluster

    Returns
    -------
    List of (gene_frozenset, sample_frozenset, time_frozenset)
    """
    n_genes, n_samples, n_times = data.shape

    # Build Array3D — constructor takes (n_times, n_samples, n_genes)
    array3d = Array3D(n_times, n_samples, n_genes)
    for t in range(n_times):
        array3d.tName[t] = str(t)
    for s in range(n_samples):
        array3d.sName[s] = f"S-{s}"
    for g in range(n_genes):
        array3d.gName[g] = f"G-{g}"

    # Fill data: array3d.setdat(t, s, g, val), data[g, s, t]
    for t in range(n_times):
        for s in range(n_samples):
            for g in range(n_genes):
                array3d.setdat(t, s, g, float(data[g, s, t]))

    support = [min_times, min_samples, min_genes]
    eDelta = [-1.0, -1.0, -1.0]
    invalidNum = []
    mv_threshold = 0.0

    cubes = Cubes()
    cluster = Cluster(array3d, cubes)

    if verbose:
        cluster.getCubes(winsz, support, eDelta, invalidNum, mv_threshold)
    else:
        with redirect_stdout(io.StringIO()):
            cluster.getCubes(winsz, support, eDelta, invalidNum, mv_threshold)

    if del_overlap < 1.0:
        cubes.delet(del_overlap)

    results = []
    for cube in cubes._cubeVec:
        if not cube.deleted:
            results.append((
                frozenset(cube.G),
                frozenset(cube.S),
                frozenset(cube.T),
            ))

    return results
