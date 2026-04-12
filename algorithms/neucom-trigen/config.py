"""
Configuration for NEUCOM TriGen setup.
Spellman Yeast Cell Cycle (GST Microarray Tensor) - aligned with TriGen paper.

Reference: Gutiérrez-Avilés et al. Neurocomputing 132 (2014) 42–53
"""

# GEO accessions for Spellman et al. yeast cell cycle (TriGen used: pheromone, cdc15, cdc28, elutriation)
# We use 3 conditions (cdc28 not in standard GEO - combined dataset)
GEO_ACCESSIONS = {
    "pheromone": "GSE22",   # Alpha-factor = pheromone
    "cdc15": "GSE23",
    "elutriation": "GSE24",
}

# TriGen paper: first 14 time points for compact dataset
MAX_TIMEPOINTS = 14

# Platform-specific matrix filenames
GEO_PLATFORM_FILES = {
    "GSE22": ["GSE22-GPL59_series_matrix.txt.gz", "GSE22-GPL60_series_matrix.txt.gz"],
    "GSE23": ["GSE23-GPL59_series_matrix.txt.gz", "GSE23-GPL60_series_matrix.txt.gz"],
    "GSE24": ["GSE24-GPL59_series_matrix.txt.gz", "GSE24-GPL60_series_matrix.txt.gz"],
}

GEO_FTP_BASE = "https://ftp.ncbi.nlm.nih.gov/geo/series"
DATA_DIR = "data"
TENSOR_FILENAME = "yeast_gst_tensor.npz"
