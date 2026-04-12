"""
data_io.py — I/O helpers for triCluster

Handles:
  - Parsing the .tab format used by the benchmark datasets
  - Loading JSON ground-truth tricluster files
  - Converting the yeast .npz tensor to the same internal format
  - Writing a .tab file for the yeast dataset
"""

from __future__ import annotations
import json
import re
import numpy as np
from pathlib import Path


# ---------------------------------------------------------------------------
# .tab parser  (format already present in Benchmark_Datasets/*/Tricluster/)
# ---------------------------------------------------------------------------

def parse_tab(path: str | Path) -> tuple[np.ndarray, list[str], list[str]]:
    """
    Parse the triCluster .tab input format.

    File structure
    --------------
    Total Times:   <T>
    Total Samples: <S>
    Total Genes:   <G>
    Time <t>
    ID  NAME  S_0  S_1  ...  S_{S-1}
    0   G-0   val  val  ...
    ...
    Time <t+1>
    ...

    Returns
    -------
    data       : ndarray shape (G, S, T)  — float64
    gene_names : list of G gene name strings
    sample_names: list of S sample name strings
    """
    path = Path(path)
    lines = path.read_text(encoding="utf-8", errors="replace").splitlines()

    # Parse header
    n_times = int(lines[0].split("\t")[1])
    n_samples = int(lines[1].split("\t")[1])
    n_genes = int(lines[2].split("\t")[1])

    data = np.full((n_genes, n_samples, n_times), np.nan, dtype=np.float64)
    gene_names: list[str] = [""] * n_genes
    sample_names: list[str] = []

    i = 3
    t = -1
    while i < len(lines):
        line = lines[i].strip()
        if not line:
            i += 1
            continue
        if line.startswith("Time"):
            t = int(line.split()[1])
            i += 1  # skip to header row
            # Next non-empty line is the column header
            while i < len(lines) and not lines[i].strip():
                i += 1
            if i < len(lines) and lines[i].strip().startswith("ID"):
                parts = lines[i].strip().split("\t")
                if not sample_names:
                    sample_names = parts[2:]
                i += 1
            continue
        # Data row
        parts = line.split("\t")
        if len(parts) >= 3 and parts[0].isdigit():
            gene_idx = int(parts[0])
            gene_names[gene_idx] = parts[1]
            for s, val_str in enumerate(parts[2:]):
                if s < n_samples:
                    try:
                        data[gene_idx, s, t] = float(val_str)
                    except ValueError:
                        data[gene_idx, s, t] = np.nan
        i += 1

    return data, gene_names, sample_names


# ---------------------------------------------------------------------------
# Ground-truth JSON loader
# ---------------------------------------------------------------------------

def load_ground_truth(path: str | Path) -> list[dict]:
    """
    Load the G-Tric JSON ground truth file.

    Returns a list of dicts, each with keys:
      'X' : list of 0-based gene row indices
      'Y' : list of 0-based sample column indices
      'Z' : list of 0-based context/time indices
    """
    path = Path(path)
    with open(path, encoding="utf-8") as f:
        raw = json.load(f)

    trics_raw = raw.get("Triclusters", raw)
    result = []
    for key, val in trics_raw.items():
        result.append({
            "X": list(val["X"]),
            "Y": list(val["Y"]),
            "Z": list(val["Z"]),
        })
    return result


# ---------------------------------------------------------------------------
# Yeast .npz → internal ndarray
# ---------------------------------------------------------------------------

def load_yeast_npz(
    path: str | Path,
    impute: bool = True,
    max_nan_fraction: float = 0.5,
) -> tuple[np.ndarray, list[str], list[str], list[str]]:
    """
    Load yeast_gst_tensor.npz  (shape: genes × conditions × timepoints).

    Parameters
    ----------
    impute           : if True, fill NaN with per-gene mean; genes with
                       > max_nan_fraction NaNs are removed entirely.
    max_nan_fraction : drop gene rows with more than this fraction of NaNs.

    Returns
    -------
    data        : ndarray (G, S, T) — float64
    gene_names  : list of G gene id strings
    cond_names  : list of S condition strings
    time_labels : list of T time-point label strings
    """
    path = Path(path)
    npz = np.load(path, allow_pickle=True)
    tensor = npz["tensor"].astype(np.float64)   # (genes, conditions, times)
    genes = list(npz["genes"].astype(str))
    conditions = list(npz["conditions"].astype(str))
    n_genes, n_conds, n_times = tensor.shape
    time_labels = [str(i) for i in range(n_times)]

    if impute:
        # Drop genes with too many NaNs
        nan_frac = np.isnan(tensor).sum(axis=(1, 2)) / (n_conds * n_times)
        keep_mask = nan_frac <= max_nan_fraction
        tensor = tensor[keep_mask]
        genes = [g for g, k in zip(genes, keep_mask) if k]

        # Impute remaining NaNs with gene-level mean
        for g in range(tensor.shape[0]):
            row = tensor[g]
            row_mean = np.nanmean(row)
            if np.isnan(row_mean):
                row_mean = 0.0
            tensor[g] = np.where(np.isnan(row), row_mean, row)

    return tensor, genes, conditions, time_labels


# ---------------------------------------------------------------------------
# Write .tab file  (so we can inspect or re-use the formatted yeast data)
# ---------------------------------------------------------------------------

def parse_tsv(path: str | Path) -> tuple[np.ndarray, list[str], list[str]]:
    """
    Parse the flat TSV format used by benchmark datasets.

    Column header format: y{sample_idx}z{time_idx}
    Row header: x{gene_idx}

    Returns
    -------
    data        : ndarray (G, S, T)
    gene_names  : list of G strings
    sample_names: list of S strings
    """
    path = Path(path)
    with open(path, encoding="utf-8") as f:
        reader = __import__("csv").reader(f, delimiter="\t")
        header = next(reader)  # e.g. ['X', 'y0z0', 'y1z0', ..., 'y0z1', ...]
        rows = list(reader)

    # Parse (sample, time) pairs from header (skip first 'X' column)
    import re as _re
    col_meta = []
    for h in header[1:]:
        m = _re.match(r"y(\d+)z(\d+)", h)
        if m:
            col_meta.append((int(m.group(1)), int(m.group(2))))

    n_samples = max(s for s, _ in col_meta) + 1
    n_times = max(z for _, z in col_meta) + 1
    n_genes = len(rows)

    data = np.full((n_genes, n_samples, n_times), np.nan, dtype=np.float64)
    gene_names = []

    for g, row in enumerate(rows):
        gene_names.append(row[0])
        for col_i, (s, t) in enumerate(col_meta):
            try:
                data[g, s, t] = float(row[col_i + 1])
            except (ValueError, IndexError):
                data[g, s, t] = np.nan

    sample_names = [f"S_{s}" for s in range(n_samples)]
    return data, gene_names, sample_names


def write_tab(
    path: str | Path,
    data: np.ndarray,
    gene_names: list[str],
    sample_names: list[str],
):
    """Write data (G, S, T) in triCluster .tab format."""
    path = Path(path)
    n_genes, n_samples, n_times = data.shape
    lines: list[str] = [
        f"Total Times:\t{n_times}",
        f"Total Samples:\t{n_samples}",
        f"Total Genes:\t{n_genes}",
    ]
    for t in range(n_times):
        lines.append(f"Time\t{t}")
        header = "ID\tNAME\t" + "\t".join(sample_names)
        lines.append(header)
        for g in range(n_genes):
            vals = "\t".join(
                f"{data[g, s, t]:.4f}" if not np.isnan(data[g, s, t]) else "NA"
                for s in range(n_samples)
            )
            lines.append(f"{g}\t{gene_names[g]}\t{vals}")
    path.write_text("\n".join(lines), encoding="utf-8")


# ---------------------------------------------------------------------------
# Convert algorithm output → list-of-dicts (same schema as ground truth)
# ---------------------------------------------------------------------------

def triclusters_to_records(
    triclusters: list[tuple[frozenset, frozenset, frozenset]],
) -> list[dict]:
    """Convert [(gene_set, sample_set, time_set), ...] → list of dicts."""
    return [
        {"X": sorted(g), "Y": sorted(s), "Z": sorted(z)}
        for g, s, z in triclusters
    ]
