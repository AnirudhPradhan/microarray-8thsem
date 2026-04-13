# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an 8th-semester research project implementing and comparing **triclustering algorithms** (3D clustering on gene×condition×time tensors). It replicates experiments from two academic papers:
- **NEUCOM TriGen (2014)**: Gutiérrez-Avilés et al. — genetic algorithm triclustering on temporal gene expression
- **Base Paper (2024)**: Soares et al. — comprehensive benchmark of triclustering algorithms (Pattern Recognition 150)

## Setup

```bash
# Python environment
python -m venv venv
venv/Scripts/activate

# Install all dependencies
pip install numpy pandas scipy scikit-learn requests matplotlib
# Or from individual requirements files:
pip install -r requirements_visualization.txt
pip install -r neucom-trigen/requirements.txt
```

Java (11+) is required for TriGen and G-Tric. No Maven build step is needed — JARs are prebuilt in `TrLab3.5/`.

## Common Commands

### Data Generation
```bash
cd algorithms/neucom-trigen
python synthetic_generator.py   # → data/tricgen_synthetic_tensor.npz (1000×10×5)
python data_loader.py           # → data/yeast_gst_tensor.npz (downloads from NCBI GEO)
```

### 3D Triclustering (TriGen — Windows)
```powershell
.\run_trigen_both_papers.ps1        # Run TriGen on QualityC + Yeast datasets
.\run_synthetic_baseline.ps1        # Synthetic generation + TriGen baseline
# Or directly:
cd algorithms/TrLab3.5
java -jar TriGenApp.jar synthetic_qualityc_run.tricfg
java -jar TriGenApp.jar yeast_trigenpaper.tricfg
```

### 1D/2D Baseline Clustering
```bash
python run_1d_2d_sklearn.py              # K-Means, Hierarchical, Spectral Biclustering
python compute_1d_2d_triq_metrics.py     # TRIQ/GRQ/PEQ/SPQ metrics for 1D/2D results
python save_results_as_pictures.py       # Generate PNGs → output_images/
```

### Benchmark Triclustering (Soares et al. 2024 — 17 datasets)
```bash
python algorithms/tricluster/run_benchmark.py      # triCluster on all 17 datasets
python algorithms/tctricluster/run_benchmark.py    # TCtriCluster (temporal contiguity)
python algorithms/deltaTrimax/run_benchmark.py     # δ-Trimax on all 17 datasets
python compute_recoverability_metrics.py           # R/CE/RMS3 vs ground truth (QualityC)
python generate_final_report.py                    # Regenerate final_comparison_report.md + charts
```

## Architecture

### Data Flow
```
Data Sources                Algorithms              Evaluation
─────────────               ──────────              ──────────
G-Tric (Java)  ──┐          TriGen GA (Java)   →   TRIQ/GRQ/PEQ/SPQ
Spellman GEO   ──┼──────→   K-Means 1D (sklearn)→  compute_1d_2d_triq_metrics.py
Benchmark TSVs ──┘          Hierarchical 1D    →   output_1d_2d/*.md reports
                            Spectral Bi/Co 2D
```

### Key Components

| Component | Language | Purpose |
|-----------|----------|---------|
| `algorithms/neucom-trigen/` | Python | TriGen 2014 paper: synthetic data generation (`synthetic_generator.py`), Yeast GEO download (`data_loader.py`) |
| `algorithms/TrLab3.5/` | Java | TriGen GUI/CLI runner; configs in `*.tricfg`; outputs `.sol` solution files to `resources/` |
| `algorithms/G-Tric/` | Java/Maven | Synthetic 3D dataset generator for benchmark experiments |
| `algorithms/tricluster/` | Python | triCluster (Zhao & Zaki 2005): numpy-based exact multiplicative triclustering on all 17 benchmark datasets |
| `algorithms/tctricluster/` | Python | TCtriCluster: extends triCluster with temporal contiguity (consecutive timepoints only); `tctricluster_numpy.py` overrides `_expand_t` with one-line constraint |
| `algorithms/deltaTrimax/` | Python | δ-Trimax: 3D Cheng-Church MSR minimisation; `run_benchmark.py` uses pre-formatted `.npy` files from `DeltaTrimax/` subfolders |
| `data/benchmark/` | Data | Benchmark TSV/NPY datasets from Soares et al. 2024 (500×10×5, ground truth JSON per dataset) |
| `run_1d_2d_sklearn.py` | Python | Flattens 3D tensors to run 1D/2D sklearn clustering as baselines |
| `compute_1d_2d_triq_metrics.py` | Python | Computes TRIQ (overall), GRQ (residue), PEQ (Pearson), SPQ (Spearman) |
| `compute_recoverability_metrics.py` | Python | R/CE/RMS3 recoverability metrics vs QualityC ground truth |
| `generate_final_report.py` | Python | Regenerates `output/final_comparison_report.md` + all charts from hardcoded results |
| `save_results_as_pictures.py` | Python | Reads `output_1d_2d/` NPZ/CSV files; generates heatmaps and bar charts |

### Datasets

**NEUCOM TriGen (2014):**
- Synthetic: `algorithms/neucom-trigen/data/tricgen_synthetic_tensor.npz` — 1000 genes × 10 conditions × 5 timepoints
- Yeast: `algorithms/neucom-trigen/data/yeast_gst_tensor.npz` — ~7700 genes × 3 conditions × 14 timepoints (GEO accessions GSE22/23/24)

**Base Paper (2024) — Benchmark Datasets** (`data/benchmark/Benchmark_Datasets/`):
- Baseline, Pattern-specific (Constant/Additive/Multiplicative/OrderPreserving), Overlapping, Quality, Contiguity variants

### TriGen Configuration (`.tricfg` files)
- GA parameters: N=5 populations, G=20 generations, I=10 individuals
- Fitness functions: `msl` (base paper / QualityC), `msr3d` (TriGen paper / Yeast)
- Configs live in `algorithms/TrLab3.5/` — outputs go to `algorithms/TrLab3.5/resources/`

### Outputs
- `output/1d_2d/` — NPZ cluster assignments, CSV summaries, Markdown metrics reports
- `output/images/` — PNG heatmaps and bar charts (including `all_algorithms_R_comparison.png`)
- `output/final_comparison_report.md` — full comparison report (all 4 tricluster algorithms)
- `output/recoverability/` — R/CE/RMS3 results vs QualityC ground truth
- `algorithms/TrLab3.5/resources/` — TriGen `.sol` solution files and TRIQ metric logs
- `algorithms/tricluster/results/benchmark_results.csv` — triCluster R/CE/RMS3 on all 17 datasets
- `algorithms/tctricluster/results/benchmark_results.csv` — TCtriCluster results
- `algorithms/deltaTrimax/results/benchmark_results.csv` — δ-Trimax results

### Benchmark Result Summary (17 datasets × 500×10×5)
| Algorithm | Mean R (non-OP) | OrderPreserving | Notes |
|-----------|----------------|-----------------|-------|
| triCluster | 0.808 | 0.000 | Exact multiplicative windows |
| TCtriCluster | 0.761 | 0.000 | Same + temporal contiguity constraint |
| δ-Trimax | ~0.21 | ~0.03 | Greedy MSR, δ=var×0.005, λ=1.2 |

## Documentation Files
- `docs/DATASET_SUMMARY.md` — maps papers to datasets
- `docs/TRIQ_results_summary.md` — TriGen TRIQ results (QualityC=0.700, Yeast=0.269)
- `docs/cursor_*.md` files — implementation notes for each subsystem (data loading, synthetic generation, TriGen replication, visualization)
- `algorithms/neucom-trigen/README.md`, `algorithms/basepaper/README.md`, `algorithms/G-Tric/README.md` — subsystem docs
- `papers/` — reference PDFs (base paper, NEUCOM TriGen, final year report)
