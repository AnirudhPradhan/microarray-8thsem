"""
Data loading for NEUCOM TriGen - Spellman Yeast Cell Cycle.
Aligned with TriGen paper: pheromone, cdc15, elutriation; first 14 time points.

Reference: Gutiérrez-Avilés et al. Neurocomputing 132 (2014) 42–53
"""

import os
import gzip
import re
from pathlib import Path

import numpy as np
import pandas as pd
import requests

from config import (
    GEO_ACCESSIONS,
    GEO_FTP_BASE,
    GEO_PLATFORM_FILES,
    DATA_DIR,
    MAX_TIMEPOINTS,
    TENSOR_FILENAME,
)


def _get_geo_series_urls(accession: str) -> list[str]:
    num = int(re.search(r"GSE(\d+)", accession, re.I).group(1))
    base = f"{GEO_FTP_BASE}/GSEnnn/{accession}/matrix" if num < 1000 else None
    urls = []
    if base and accession in GEO_PLATFORM_FILES:
        for fname in GEO_PLATFORM_FILES[accession]:
            urls.append(f"{base}/{fname}")
    if num < 1000:
        urls.append(f"{GEO_FTP_BASE}/GSEnnn/{accession}/matrix/{accession}_series_matrix.txt.gz")
    if num >= 10:
        prefix = num // 10
        urls.append(f"{GEO_FTP_BASE}/GSE{prefix}nnn/{accession}/matrix/{accession}_series_matrix.txt.gz")
    if num >= 1000:
        prefix = num // 1000
        urls.append(f"{GEO_FTP_BASE}/GSE{prefix}nnn/{accession}/matrix/{accession}_series_matrix.txt.gz")
    return urls


def download_geo_series(accession: str, data_dir: str) -> str:
    os.makedirs(data_dir, exist_ok=True)
    candidates = [f"{accession}_series_matrix.txt.gz"]
    if accession in GEO_PLATFORM_FILES:
        candidates.extend(GEO_PLATFORM_FILES[accession])
    for fname in candidates:
        local_path = os.path.join(data_dir, fname)
        if os.path.exists(local_path):
            print(f"  Using cached: {local_path}")
            return local_path

    urls = _get_geo_series_urls(accession)
    last_error = None
    for url in urls:
        print(f"  Trying {url} ...")
        try:
            resp = requests.get(url, timeout=120)
            resp.raise_for_status()
            fname = url.split("/")[-1]
            local_path = os.path.join(data_dir, fname)
            with open(local_path, "wb") as f:
                f.write(resp.content)
            print(f"  Saved to {local_path}")
            return local_path
        except Exception as e:
            last_error = e
            continue

    raise RuntimeError(
        f"Failed to download {accession}. Manual: https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc={accession}"
    ) from last_error


def parse_geo_series_matrix(filepath: str) -> pd.DataFrame:
    open_fn = gzip.open if filepath.endswith(".gz") else open
    mode = "rt" if filepath.endswith(".gz") else "r"
    with open_fn(filepath, mode, encoding="utf-8") as f:
        content = f.read()

    if "!series_matrix_table_begin" in content:
        start = content.index("!series_matrix_table_begin") + len("!series_matrix_table_begin")
    else:
        start = 0

    lines = content[start:].strip().split("\n")
    if not lines:
        raise ValueError(f"No data table in {filepath}")

    header = lines[0].split("\t")
    data_lines = [l.split("\t") for l in lines[1:]]
    df = pd.DataFrame(data_lines, columns=header)
    id_col = df.columns[0]
    df = df.set_index(id_col)
    for c in df.columns:
        df[c] = pd.to_numeric(df[c], errors="coerce")
    return df


def build_gst_tensor(condition_data: dict[str, pd.DataFrame], genes: list | None = None, max_timepoints: int | None = None) -> np.ndarray:
    conditions = list(condition_data.keys())
    if genes is None:
        genes = list(condition_data[conditions[0]].index)
        for cond in conditions[1:]:
            genes = [g for g in genes if g in condition_data[cond].index]

    n_times = min(MAX_TIMEPOINTS, max(condition_data[c].shape[1] for c in conditions)) if max_timepoints is None else max_timepoints
    n_conditions = len(conditions)

    tensor = np.full((len(genes), n_conditions, n_times), np.nan)

    for j, cond in enumerate(conditions):
        df = condition_data[cond].reindex(genes)
        n_t = min(n_times, df.shape[1])
        tensor[:, j, :n_t] = df.iloc[:, :n_t].values

    return tensor


def load_spellman_yeast(data_dir: str | None = None) -> tuple[np.ndarray, dict]:
    if data_dir is None:
        data_dir = os.path.join(Path(__file__).parent, DATA_DIR)

    condition_data = {}
    for cond_name, accession in GEO_ACCESSIONS.items():
        local_path = download_geo_series(accession, data_dir)
        df = parse_geo_series_matrix(local_path)
        # TriGen: first 14 time points
        df = df.iloc[:, :MAX_TIMEPOINTS]
        condition_data[cond_name] = df

    gene_sets = [set(df.index) for df in condition_data.values()]
    common_genes = sorted(set.intersection(*gene_sets))
    if not common_genes:
        raise ValueError("No common genes across conditions")

    tensor = build_gst_tensor(condition_data, genes=common_genes, max_timepoints=MAX_TIMEPOINTS)
    n_times = [min(MAX_TIMEPOINTS, condition_data[c].shape[1]) for c in condition_data]

    meta = {
        "genes": common_genes,
        "conditions": list(GEO_ACCESSIONS.keys()),
        "timepoints_per_condition": dict(zip(GEO_ACCESSIONS, n_times)),
    }

    return tensor, meta


def main():
    base_dir = Path(__file__).parent
    data_dir = base_dir / DATA_DIR

    print("=== NEUCOM TriGen - Spellman Yeast Cell Cycle Setup ===\n")
    print("1. Loading data (first 14 time points, per TriGen paper)...")
    tensor, meta = load_spellman_yeast(str(data_dir))

    print(f"\n2. GST tensor shape: {tensor.shape}")
    print(f"   Genes: {tensor.shape[0]}, Conditions: {meta['conditions']}, Timepoints: {MAX_TIMEPOINTS}")

    out_raw = data_dir / TENSOR_FILENAME
    np.savez_compressed(out_raw, tensor=tensor, genes=np.array(meta["genes"]), conditions=np.array(meta["conditions"]))
    print(f"\n3. Saved raw tensor to {out_raw}")

    print("\n=== Setup complete ===")


if __name__ == "__main__":
    main()
