# Saving results in picture format
_Exported on 2/22/2026 at 22:24:41 GMT+5:30 from Cursor (2.5.20)_

---

**User**

save all the results 1d 2d 3d in pictures

refer @cursor_synthetic_dataset_implementationversion3.md 

---

**Cursor**


Creating a script to save visualizations of 1D, 2D, and 3D results as images, following the documented setup.



Creating a visualization script to save 1D, 2D, and 3D results as images, per `cursor_synthetic_dataset_implementationversion3.md`.



Checking for matplotlib in the project and whether we can use seaborn (sometimes includes matplotlib):



Script created. Matplotlib failed to install due to a file lock. Adding a requirements note and retrying.



1D, 2D, and 3D results are saved as images in `d:\8th sem\output_images\`.

## Images saved

| Type | Files |
|------|-------|
| **1D** | `1d_cluster_sizes_qualityc.png`, `1d_cluster_sizes_yeast.png`, `1d_metrics_qualityc.png`, `1d_metrics_yeast.png` |
| **2D** | `2d_heatmap_qualityc.png`, `2d_heatmap_yeast.png`, `2d_metrics_qualityc.png`, `2d_metrics_yeast.png` |
| **3D** | `3d_metrics_qualityc.png`, `3d_metrics_yeast.png`, `3d_triq_bar_qualityc.png`, `3d_triq_bar_yeast.png` |
| **Summary** | `comparison_1d_2d_3d.png` |

## Script

**`save_results_as_pictures.py`**:
- Uses output from `run_1d_2d_sklearn.py`, `compute_1d_2d_triq_metrics.py`, and TrLab 3D runs
- Writes to `d:\8th sem\output_images\`

Run it anytime with:
```powershell
cd "d:\8th sem"
.\venv\Scripts\python.exe save_results_as_pictures.py
```

## Picture contents

- **1D**: Cluster sizes for K‑Means vs Agglomerative; TRIQ/GRQ/PEQ/SPQ for 1D clusters
- **2D**: Cocluster heatmaps (rows/columns reordered by cluster); TRIQ/GRQ/PEQ/SPQ for 2D blocks
- **3D**: TRIQ, GRQ, PEQ, SPQ for TriGen triclusters (QualityC and Yeast)
- **Comparison**: Summary of 1D vs 2D vs 3D average TRIQ

---

**User**

we ran everything then we have these images right?

---

**Cursor**


Yes. You ran the full pipeline and produced those images:

1. **`run_1d_2d_sklearn.py`** – 1D (K‑Means, Agglomerative) and 2D (SpectralCoclustering, SpectralBiclustering) clustering
2. **`compute_1d_2d_triq_metrics.py`** – TRIQ, GRQ, PEQ, SPQ for 1D and 2D results
3. **TrLab TriGen** – 3D triclustering (QualityC and Yeast runs)
4. **`save_results_as_pictures.py`** – saves all of the above as pictures

So the images in `d:\8th sem\output_images\` come from those runs.  
If you rerun the scripts or TriGen, you can regenerate the images by running `save_results_as_pictures.py` again.

---

**User**

where are the .sol files for this?

---

**Cursor**


The TriGen `.sol` solution files are here:

| Dataset | Path |
|---------|------|
| **QualityC (Base paper)** | `d:\8th sem\TrLab3.5\resources\synthetic_qualityc_run\synthetic_qualityc_run\synthetic_qualityc_run.sol` |
| **Yeast (TriGen paper)** | `d:\8th sem\TrLab3.5\resources\yeast_trigenpaper_run\yeast_trigenpaper_run\yeast_trigenpaper_run.sol` |

In each run folder you should also find:
- `*_triq.csv` – TRIQ scores
- `coordinates/` – tricluster coordinates
- `arranged/` – arranged views (if generated)

---

**User**

for 1d and 2d?

---

**Cursor**


There are **no `.sol` files** for 1D and 2D.

`.sol` is a **TrLab/TriGen format** used only for **3D triclustering** results.

For **1D and 2D**, results are stored in:

| Output type | Location |
|-------------|----------|
| **1D & 2D** | `d:\8th sem\output_1d_2d\` |
| Cluster labels (1D) | `qualityc_1d_cluster_assignments.csv`, `yeast_1d_cluster_assignments.csv` |
| Cluster sizes (1D) | `qualityc_1d_cluster_sizes.csv`, `yeast_1d_cluster_sizes.csv` |
| Cocluster labels (2D) | `qualityc_2d_cocluster_row_labels.csv`, `yeast_2d_cocluster_row_labels.csv` |
| Bicluster sizes (2D) | `*_2d_spectral_bicluster_sizes.csv` |
| Combined labels | `qualityc_1d_2d_results.npz`, `yeast_1d_2d_results.npz` |
| TRIQ/GRQ/PEQ/SPQ | `1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv` |

These come from the scikit-learn scripts, which use CSV and NPZ instead of `.sol`.

---

**User**

how did we calc these metrics for them?

---

**Cursor**


TRIQ, GRQ, PEQ, and SPQ for 1D and 2D come from **`compute_1d_2d_triq_metrics.py`**, using the **2D versions** of the 3D TriGen metrics:

---

## 1. **GRQ (Gene Residual Quality)** — 2D MSR

For each submatrix (1D cluster or 2D block):

- **MSR (Mean Squared Residue):**
  \[
  \text{MSR} = \frac{1}{|I| \cdot |J|} \sum_{i,j} \bigl( a_{ij} - \bar{a}_{iJ} - \bar{a}_{Ij} + \bar{a}_{IJ} \bigr)^2
  \]
  where \(\bar{a}_{iJ}\) = row mean, \(\bar{a}_{Ij}\) = column mean, \(\bar{a}_{IJ}\) = global mean.

- **GRQ:**
  \[
  \text{GRQ} = \max\left(0,\; 1 - \frac{\text{MSR}}{\text{Var}(X)}\right)
  \]
  Higher GRQ → more coherent submatrix.

---

## 2. **PEQ (Pearson Enrichment Quality)**

- Pearson correlation matrix between rows (genes) in the submatrix.
- PEQ = mean of absolute Pearson correlations in the upper triangle.
- Higher PEQ → stronger average linear correlation.

---

## 3. **SPQ (Spearman Enrichment Quality)**

- Same as PEQ but using **Spearman** rank correlation.
- Higher SPQ → stronger monotonic structure.

---

## 4. **TRIQ (Tricluster Index of Quality)**

- Weighted average (without BIOQ here):
  \[
  \text{TRIQ} = \frac{0.4 \cdot \text{GRQ} + 0.05 \cdot \text{PEQ} + 0.05 \cdot \text{SPQ}}{0.5}
  \]

---

## How it’s applied

| Analysis | Submatrix used |
|----------|------------------|
| **1D K-Means** | Each K-Means cluster → genes in cluster × all features |
| **1D Agglomerative** | Each hierarchical cluster → same idea |
| **2D SpectralCoclustering** | Each (row cluster × col cluster) block |
| **2D SpectralBiclustering** | Each bicluster submatrix (row_mask × col_mask) |

So for 1D/2D, the submatrix is a **genes × features** block (e.g. cluster or block), and GRQ, PEQ, SPQ, TRIQ are computed on that block the same way.

---

**User**

elaborate I do not understand

---

**Cursor**


Here’s a simpler, step-by-step explanation.

---

## What we’re measuring

For each **cluster** (1D) or **block** (2D), we have a submatrix of numbers: rows = genes, columns = features. We want to know how coherent that block is:

- **GRQ**: How well the numbers fit an “additive” pattern (row + column effects).
- **PEQ** / **SPQ**: How similar the gene profiles are.
- **TRIQ**: A single number combining GRQ, PEQ, and SPQ.

---

## 1. GRQ (Gene Residual Quality)

**What it does:** Measures how close the block is to a simple additive pattern.

**Additive model:**  
If we had  
`value ≈ row_effect + column_effect + constant`,  
the block would be very coherent.

**Steps:**

1. For each cell \((i, j)\) in the block, compute the **residue**:
   \[
   \text{residue}_{ij} = a_{ij} - \bar{a}_{i} - \bar{a}_{j} + \bar{a}
   \]
   - \(\bar{a}_{i}\): mean of row \(i\)
   - \(\bar{a}_{j}\): mean of column \(j\)
   - \(\bar{a}\): mean of entire block  
   The residue is how far the cell is from the additive model.

2. **MSR (Mean Squared Residue):**
   - Square all residues.
   - Take the average.
   - Low MSR → numbers fit the additive pattern well.

3. **GRQ:**
   \[
   \text{GRQ} = \max\Big(0,\; 1 - \frac{\text{MSR}}{\text{variance of block}}\Big)
   \]
   - Normalized by variance so different scales are comparable.
   - Higher GRQ → block is more coherent (MSR is small relative to variance).

**Example:**

| Gene | f1 | f2 | f3 | row mean |
|------|----|----|----|----------|
| g1   | 2  | 4  | 6  | 4        |
| g2   | 3  | 5  | 7  | 5        |
| g3   | 4  | 6  | 8  | 6        |
| col mean | 3 | 5 | 7 | 5 |

Here, each cell is roughly `row_mean + col_mean - grand_mean`. Residues are small → MSR is low → GRQ is high.

---

## 2. PEQ (Pearson Enrichment Quality)

**What it does:** Measures how linearly correlated the gene profiles (rows) are.

**Steps:**

1. Each row = one gene’s profile across features.
2. Compute the **Pearson correlation** between every pair of rows.
3. PEQ = average of the **absolute** correlations (over all pairs).
   - We ignore the diagonal (correlation with itself = 1).
   - We use absolute value so positive and negative correlations both count as “related”.

**Intuition:**

- If genes rise and fall together → high positive correlation.
- If they move in opposite directions → high negative correlation.
- In both cases |correlation| ≈ 1 → PEQ is high.
- If profiles are random or unrelated → correlations near 0 → PEQ is low.

---

## 3. SPQ (Spearman Enrichment Quality)

**What it does:** Same idea as PEQ, but with **Spearman** instead of Pearson.

- Pearson: linear relationships (straight lines).
- Spearman: monotonic relationships (both go up/down, but not necessarily linearly).

**Steps:**

- Same as PEQ, but correlation is **Spearman rank correlation**.
- SPQ = mean of absolute Spearman correlations between gene pairs.

**Why both PEQ and SPQ:**  
Spearman is more robust to outliers and captures nonlinear-but-monotonic patterns. Together they give a fuller picture of how similar the profiles are.

---

## 4. TRIQ (Tricluster Index of Quality)

**What it does:** One overall quality score.

**Formula:**
\[
\text{TRIQ} = \frac{0.4 \cdot \text{GRQ} + 0.05 \cdot \text{PEQ} + 0.05 \cdot \text{SPQ}}{0.5}
\]

- GRQ has weight 0.4 (structure / additive coherence).
- PEQ and SPQ each have weight 0.05 (gene–gene similarity).
- The 0.5 in the denominator is the sum of weights (BIOQ is not used here).

So TRIQ is just a weighted average of GRQ, PEQ, and SPQ.

---

## How we apply this to 1D and 2D

**1D (e.g. K-Means):**

- Data: genes × features.
- K-Means gives 5 clusters of genes.
- For **each cluster**, we take its submatrix (genes in cluster × all features).
- We compute GRQ, PEQ, SPQ, TRIQ on that submatrix.
- Each cluster gets its own set of metrics.

**2D (e.g. SpectralCoclustering):**

- Rows split into 5 clusters, columns into 5 clusters.
- This defines 25 blocks: block \((i,j)\) = rows in row-cluster \(i\) × columns in col-cluster \(j\).
- For **each block**, we take that submatrix.
- We compute GRQ, PEQ, SPQ, TRIQ on that block.
- Each block gets its own set of metrics.

So:
- **1D**: one submatrix per cluster.
- **2D**: one submatrix per block.
- For each submatrix we compute the same four metrics: GRQ, PEQ, SPQ, TRIQ.

