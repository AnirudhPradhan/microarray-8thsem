# NEUCOM TriGen Setup

Environment and data setup for **TriGen** (Gutiérrez-Avilés et al., Neurocomputing 2014).

## Reference

- **TriGen:** Gutiérrez-Avilés D, Rubio-Escudero C, Martínez-Álvarez F, Riquelme JC. *TriGen: A genetic algorithm to mine triclusters in temporal gene expression data*. Neurocomputing 132 (2014) 42–53.

## Datasets (as in TriGen Paper)

### 1. Synthetic (Section 4.1)
- **Shape:** 1000 genes × 10 conditions × 5 time points
- **Planted areas:** Area a (20×5×3, constant=1), Area b (30×4×4, ascending/descending pattern)
- **Generate:** `python synthetic_generator.py`
- **Output:** `data/tricgen_synthetic_tensor.npz`

### 2. Yeast Cell Cycle (Section 4.2)
- **Source:** Spellman PT et al. (GEO: GSE22, GSE23, GSE24)
- **Conditions:** pheromone, cdc15, elutriation (3; cdc28 not in standard GEO)
- **Time points:** First 14 per condition
- **Generate:** `python data_loader.py`
- **Output:** `data/yeast_gst_tensor.npz`

### 3. Inflammation and Host Response (Section 4.3)
- **Paper:** 2155 genes × 2 conditions (endotoxin, placebo) × 6 time points
- **Source:** Calvano et al. Nature 437 (2005); gene selection from Rubio-Escudero AI Commun 2012
- **Note:** Exact dataset requires Glue Grant / IHRI program access; structure documented in paper.

## Run

```bash
cd neucom-trigen
pip install -r requirements.txt
python synthetic_generator.py   # Synthetic
python data_loader.py            # Yeast
```

## Output

- `data/tricgen_synthetic_tensor.npz` – Synthetic (1000×10×5) with ground_truth
- `data/yeast_gst_tensor.npz` – Gene × Condition × Time (genes × 3 × 14)
