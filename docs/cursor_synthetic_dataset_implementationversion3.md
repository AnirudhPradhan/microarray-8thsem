# Synthetic dataset implementation
_Exported on 2/21/2026 at 22:47:59 GMT+5:30 from Cursor (2.5.17)_

---

**User**

okay after running this run for the synthetic dataset in @basepaper-implementation.pdf 

---

**Cursor**


Searching the workspace for code related to the paper's synthetic-dataset run.



Setting up and running the base paper's synthetic dataset experiments. Checking the Benchmark_Datasets structure and creating the run pipeline.



Creating a run script for the base-paper synthetic dataset experiments and setting up the pipeline:



Creating the run script and setting up the base-paper synthetic dataset for TriGen:



Verifying the output and creating a run script for future use:



Here’s what was done:

## 1. **Synthetic data generation (neucom-trigen)**

- Ran `synthetic_generator.py`
- Output: `neucom-trigen\data\tricgen_synthetic_tensor.npz` (1000×10×5 TriGen-style data)

## 2. **Base paper synthetic run (TriGen)**

TriGen was run on the base paper **QualityC** synthetic dataset (Constant pattern, 4th collection):

- **Dataset:** G-Tric QualityC (500×10×4) from `triclustering-algorithms-assessment`
- **Config:** `TrLab3.5\synthetic_qualityc_run.tricfg`
- **Result:** 5 triclusters in ~1.6 seconds

**Output location:**
- `TrLab3.5\resources\synthetic_qualityc_run\synthetic_qualityc_run\`
  - `.sol` – solution file  
  - `synthetic_qualityc_run_triq.csv` – TRIQ scores  
  - `coordinates/` – tricluster coordinates  
  - `arranged/` – arranged views  

## 3. **Run script**

`run_synthetic_baseline.ps1` can be used like this:

```powershell
# Run TriGen only (default)
.\run_synthetic_baseline.ps1

# Run synthetic generator AND TriGen
.\run_synthetic_baseline.ps1 -SyntheticGenerator -TriGen
```

## Other base paper datasets

More datasets in `triclustering-algorithms-assessment\Benchmark_Datasets` can be run by:

1. Copying their TriGen subfolder into `TrLab3.5\resources\00XX\`
2. Adding a dataset entry in `resources.xml`
3. Creating a new `.tricfg` that references that dataset

Datasets include **QualityA** (Additive), **QualityM** (Multiplicative), **QualityOP** (Order Preserving), **Baseline**, **Overlapping**, and **Contiguity**.

---

**User**

triclustering-algorithms-assessment\Benchmark_Datasets this the dataset used in @basepaper-implementation.pdf right?

---

**Cursor**


Yes. The datasets used in the base paper (Soares et al., Pattern Recognition 150, 2024) come from `triclustering-algorithms-assessment`.

The paper states:

> *"Data and materials availability: The generated datasets and the source code run for the experiments were made available at: https://github.com/dfmsoares/triclustering-algorithms-assessment"*

So `Benchmark_Datasets` in that repo contains the G-Tric synthetic data used in the paper’s five experiments:

| Collection | Folders in Benchmark_Datasets | Description |
|------------|------------------------------|-------------|
| **1st – Baseline** | `Background`, `BaseR` | 500×10×5, 40 triclusters, 4 pattern types |
| **2nd – Pattern-specific** | `Constant`, `Additive`, `Multiplicative`, `OrderPreserving` | One pattern type per dataset |
| **3rd – Overlapping** | `OverlappingC`, `OverlappingA`, `OverlappingM`, `OverlappingOP` | 40% overlapping triclusters |
| **4th – Quality** | `QualityC`, `QualityA`, `QualityM`, `QualityOP` | ~10% noise, ~5% errors |
| **5th – Contiguity** | `ContiguityC`, `ContiguityA`, `ContiguityM`, `ContiguityOP` | Temporal contiguity |

Each folder has the raw data (`*.tsv`), ground-truth triclusters (`*_trics.json` / `*_trics.txt`), and algorithm-specific formats (e.g. `TriGen/`, `Tricluster/`) for triCluster, TCtriCluster, δ-trimax, and TriGen.

---

**User**

then run on these for yeast why did you ran synthetic_generator.py?

---

**Cursor**


You’re right to question that.

**`synthetic_generator.py`** is for a different setup: it creates the **TriGen 2014 paper** synthetic data (1000×10×5 with area_a and area_b). That is not the base paper’s Benchmark_Datasets.

**Base paper datasets**  
The Benchmark_Datasets used in the base paper (Soares et al. 2024) are already generated and stored in `triclustering-algorithms-assessment\Benchmark_Datasets`. No run of `synthetic_generator.py` is needed for those.

**Yeast**  
Yeast (Spellman cell cycle) is already available as the `yeast` dataset in TrLab, and we successfully ran TriGen on the QualityC Benchmark_Datasets in the base paper.

So:

- **Benchmark_Datasets** → run TriGen directly on the existing G-Tric data.
- **Yeast** → run TriGen on the existing `yeast` dataset in TrLab.

`synthetic_generator.py` was run by mistake; it is for the TriGen 2014 paper synthetic setup and is not required for the base paper’s synthetic datasets or for yeast.

---

**User**

where is spellman cell data used in which paper? and show which dataset is used in which paper all the dataset you may go through the papers once

---

**Cursor**


Here’s the mapping across the papers and your setup:

---

## Dataset ↔ paper mapping

| **Dataset** | **Paper(s)** | **Description** |
|-------------|--------------|------------------|
| **Benchmark_Datasets** (G-Tric synthetic) | **Soares et al. Pattern Recognition 2024** (base paper) | Only dataset used. G-Tric-generated synthetic tensors (500×10×5, etc.) with planted triclusters. Location: `triclustering-algorithms-assessment\Benchmark_Datasets`. |
| **Spellman yeast cell cycle** | **TriGen (Gutiérrez-Avilés et al. Neurocomputing 2014)** | Real microarray data: 6178 genes × 4 conditions (alpha, cdc15, cdc28, elu) × 14 time points. Source: Spellman et al. (1998), GEO GSE22, GSE23, GSE24. In TrLab as `yeast`. |
| **TriGen synthetic (area_a, area_b)** | **TriGen (Gutiérrez-Avilés et al. Neurocomputing 2014)** | Synthetic 1000×10×5 with two planted areas. Produced by `synthetic_generator.py` in `neucom-trigen`. |
| **Inflammation / host response** | **TriGen (Gutiérrez-Avilés et al. Neurocomputing 2014)** | Calvano et al. Nature 437 (2005): 2155 genes × 2 conditions (endotoxin, placebo) × 6 time points. From Glue Grant/IHRI; not freely available. |
| **G-Tric generator** | **Lobo et al. BMC Bioinformatics 2021** | Tool that generates synthetic datasets. Used to create the base paper’s Benchmark_Datasets. |

---

## By paper

### Base paper (Soares et al. Pattern Recognition 2024)
- Uses **only** G-Tric synthetic data (Benchmark_Datasets).
- No real data; rationale: *“An objective evaluation in real datasets would not be possible since no true solutions are available.”*

### TriGen (Gutiérrez-Avilés et al. Neurocomputing 2014)
- **Section 4.1:** Custom synthetic dataset (1000×10×5) – implemented as `synthetic_generator.py`.
- **Section 4.2:** Spellman yeast cell cycle – Spellman et al. Mol Biol Cell (1998), GEO GSE22–24; used in TrLab as `yeast`.
- **Section 4.3:** Inflammation dataset – Calvano et al. Nature 437 (2005); not public.

---

## TrLab datasets

| TrLab dataset | Source | Paper |
|---------------|--------|-------|
| `yeast` | Spellman yeast | TriGen 2014 |
| `elutriation` | Spellman (elutriation) | TriGen 2014 |
| `gds4510`, `gds4472`, `gds4262`, `gds4442`, `gse4324` | GEO datasets | Various |
| `earthquake`, `ant*`, `milho*`, etc. | Non-biological | Other |
| `synthetic_qualityc` | Benchmark_Datasets/QualityC | Base paper 2024 |

Bottom line: Spellman yeast is used in the TriGen 2014 paper, not in the base paper (Soares et al. 2024), which uses only Benchmark_Datasets.

---

**User**

go through the paper the base paper dataset is inside triclustering-algoright,-assessment folder right there is a repo attached what is it? do not hallucinate give correct answer only

---

**Cursor**


Here are the answers from the base paper and your setup:

---

## 1. Base paper dataset location

**Yes.** The base paper dataset is in the `triclustering-algorithms-assessment` repo, under `Benchmark_Datasets`.

From the paper (ARTICLE INFO):

> **Dataset link:** https://github.com/dfmsoares/triclustering-algorithms-assessment

---

## 2. Repo referenced in the paper

The repository mentioned in the paper is:

**https://github.com/dfmsoares/triclustering-algorithms-assessment**

It is authored by **dfmsoares** (Diogo F. Soares, first author of the paper) and contains:

- **Generated datasets** (the Benchmark_Datasets used in the paper)
- **Source code** used to run the experiments

---

## 3. What you have locally

`d:\8th sem\triclustering-algorithms-assessment` is a local clone of that repo. `Benchmark_Datasets` inside it holds the synthetic datasets used in the base paper.

---

**User**

then run on the trigen app for GA for it which one was used in basepaper and the one used in trigen paper

---

**Cursor**


Creating configs and running both experiments.



Summary of what ran:

---

## Fitness used in each paper

| Paper | Dataset | Fitness / GA used |
|-------|---------|-------------------|
| **Base paper** (Soares et al. Pattern Recognition 2024) | Benchmark_Datasets (G-Tric synthetic) | **MSL**, LSL, MSR3D (all three reported; MSL best) |
| **TriGen paper** (Gutiérrez-Avilés et al. Neurocomputing 2014) | Yeast (Spellman), synthetic (1000×10×5) | **MSR3D** (volume + 3D mean square residue) – original version; MSL/LSL added later |

---

## Runs completed

### 1. Base paper
- **Config:** `synthetic_qualityc_run.tricfg`
- **Dataset:** synthetic_qualityc (Benchmark_Datasets/QualityC, 500×10×4)
- **Fitness:** MSL
- **GA:** N=5, G=20, I=10
- **Output:** `TrLab3.5\resources\synthetic_qualityc_run\`

### 2. TriGen paper
- **Config:** `yeast_trigenpaper.tricfg`
- **Dataset:** yeast (Spellman cell cycle, 6178×4×14)
- **Fitness:** MSR3D
- **GA:** N=5, G=20, I=10
- **Output:** `TrLab3.5\resources\yeast_trigenpaper_run\`

---

## Script for future runs

```powershell
.\run_trigen_both_papers.ps1
```

---

## Note

The base paper also reports LSL and MSR3D. The runs above use MSL for the base-paper dataset and MSR3D for the TriGen-paper dataset, matching the original setups. To reproduce all base-paper TriGen variants, run `synthetic_qualityc_run.tricfg` three times with `fitness=lsl`, `fitness=msl`, and `fitness=msr3d`.

---

**User**

explain me the config

---

**Cursor**


TriGen `.tricfg` parameters mean the following:

---

## TriGen config (.tricfg)

### 1. Dataset & output

| Parameter | Value | Meaning |
|-----------|-------|---------|
| `dataset` | `synthetic_qualityc` / `yeast` | Dataset from `resources.xml` |
| `out` | path | Output directory for `.sol` and reports |

---

### 2. GA parameters

| Parameter | Typical | Meaning |
|-----------|---------|---------|
| **N** | 5 | Number of triclusters to find |
| **G** | 20 | Generations per tricluster |
| **I** | 10 | Population size (individuals per generation) |
| **Ale** | 0.2 | Fraction of **random** individuals in initial population (`0.2 × I` ≈ 2 random, rest from data) |
| **Sel** | 0.5 | Fraction of population kept as parents (`0.5 × I` = 5 parents) |
| **Mut** | 0.1 | Mutation probability (10% chance to mutate each offspring) |

---

### 3. Fitness weights (multi-objective)

The fitness function combines homogeneity, overlap penalty, and size:

| Parameter | Typical | Meaning |
|-----------|---------|---------|
| **Wf** | 0.8 | Weight for coherence / homogeneity |
| **Wg** | 0.04 | Weight for gene-size contribution |
| **Wc** | 0.03 | Weight for sample/condition-size contribution |
| **Wt** | 0.03 | Weight for time-size contribution |
| **WOg** | 0.04 | Overlap penalty for genes |
| **WOc** | 0.03 | Overlap penalty for samples |
| **WOt** | 0.03 | Overlap penalty for times |

Lower fitness is better (more coherent, larger, less overlap).

---

### 4. Fitness function

| Parameter | Options | Meaning |
|-----------|---------|---------|
| `fitness` | `msl`, `lsl`, `msr3d` | Homogeneity measure used in the fitness |
| | | **MSL** – angle-based, supports time lags |
| | | **LSL** – similar time-lag measure |
| | | **MSR3D** – 3D mean square residue (original TriGen) |

---

### 5. Tricluster size bounds

| Parameter | Typical | Meaning |
|-----------|---------|---------|
| `minG` / `maxG` | 8–200 / 15–200 | Min/max genes per tricluster |
| `minC` / `maxC` | 2–6 / 2–4 | Min/max samples/conditions |
| `minT` / `maxT` | 2–4 / 3–8 | Min/max time points |

These depend on the dataset (e.g. synthetic vs yeast).

---

### 6. Algorithm components

| Parameter | Value | Meaning |
|-----------|-------|---------|
| `individual` | `coordinate tricluster` | Each individual = (genes, samples, times) |
| `datahierarchy` | `levelsGST` | How the search space is subdivided |
| `stoppingcriterion` | `find solutions` | Stop when N triclusters are found |
| `solutioncriterion` | `minimum fitness` | Choose tricluster with lowest fitness |
| `initialpop` | `tensors` | Initial population from tensor slices |
| `selection` | `tournament` | Tournament selection for parents |
| `crossover` | `one point` | Single-point crossover |
| `mutation` | `put, remove and change` | Add/remove/swap genes, samples, or times |

---

### 7. Other

| Parameter | Meaning |
|-----------|---------|
| `threads` | 0 = single-threaded; >0 = number of parallel threads for fitness evaluation |

---

## Execution flow

```
For each of N solutions:
  1. Create initial population of I individuals (Ale×I random, rest from tensors)
  2. For G generations:
     - Evaluate fitness (homogeneity + overlap + size)
     - Select best Sel×I parents (tournament)
     - Crossover → offspring
     - Mutate offspring with probability Mut
     - Replace population = parents + offspring
  3. Pick best individual → add to solutions
  4. Update hierarchy (avoid reusing same subspace in later solutions)
```

---

**User**

how did we prepared the Benchmark_Datasets for running?

---

**Cursor**


Here is how Benchmark_Datasets were prepared for running with TriGen:

---

## Preparation steps

### 1. **Use the TriGen subfolder**

Each Benchmark_Datasets dataset (e.g. QualityC) already has a `TriGen/` folder with time-slice CSVs:

- `dataset_quality_constant_data_z0.csv` … `z3.csv` → one CSV per time point (rows = objects, columns = attributes)
- `xs_names.txt` → object IDs (e.g. x0, x1, …)
- `ys_names.txt` → attribute IDs (e.g. y0, y1, …)
- `zs_names.txt` → context/time IDs (e.g. z0, z1, …)

### 2. **Copy into TrLab resources**

We copied the TriGen folder contents into a new TrLab resources folder:

```
triclustering-algorithms-assessment\Benchmark_Datasets\QualityC\TriGen\*
    → TrLab3.5\resources\0021\
```

### 3. **Rename files for TrLab**

TrLab expects specific filenames, so we renamed:

| Original | Renamed to |
|----------|------------|
| `dataset_quality_constant_data_z0.csv` | `synqc_t0.csv` |
| `dataset_quality_constant_data_z1.csv` | `synqc_t1.csv` |
| `dataset_quality_constant_data_z2.csv` | `synqc_t2.csv` |
| `dataset_quality_constant_data_z3.csv` | `synqc_t3.csv` |
| `xs_names.txt` | `synqc_genes.txt` |
| `ys_names.txt` | `synqc_samples.txt` |
| `zs_names.txt` | `synqc_times.txt` |

### 4. **Add dataset to `resources.xml`**

We added a dataset entry like:

```xml
<dataset description="Base paper synthetic - QualityC" 
         geneSize="500" id="0021" sampleSize="10" timeSize="4"
         minG="8" maxG="200" minC="2" maxC="6" minT="2" maxT="4"
         name="synthetic_qualityc" organism="Synthetic" type="e">
  <resources separator=",">
    <resource>synqc_t0.csv</resource>
    <resource>synqc_t1.csv</resource>
    <resource>synqc_t2.csv</resource>
    <resource>synqc_t3.csv</resource>
  </resources>
  <genes>synqc_genes.txt</genes>
  <samples>synqc_samples.txt</samples>
  <times>synqc_times.txt</times>
</dataset>
```

Important details:

- `separator=","` because Benchmark CSVs use commas (yeast uses `;`)
- `type="e"` so no biological/GO analysis is run
- `geneSize`, `sampleSize`, `timeSize` match the data (500×10×4)
- `minG`, `maxG`, `minC`, `maxC`, `minT`, `maxT` match the dataset ranges

### 5. **Create a control file (.tricfg)**

We created `synthetic_qualityc_run.tricfg` with:

- `dataset=synthetic_qualityc` (name in `resources.xml`)
- `out=` pointing to the output folder
- GA and fitness settings (N, G, I, fitness, etc.)

---

## For other Benchmark_Datasets

To run QualityA, QualityM, QualityOP, Additive, etc., repeat the same workflow:

1. Copy from `Benchmark_Datasets\<name>\TriGen\` to a new `resources\00XX\` folder.
2. Rename z-slice CSVs to `synqa_t0.csv`, etc., and the name files to `synqa_genes.txt`, `synqa_samples.txt`, `synqa_times.txt`.
3. Add a corresponding `<dataset>` entry in `resources.xml`.
4. Create a new `.tricfg` file with `dataset=<new_name>`.

---

## Summary

The Benchmark_Datasets are already exported in TriGen format by the triclustering-algorithms-assessment repo. The only preparation needed was copying into TrLab, renaming files, adding the dataset to `resources.xml`, and creating the control `.tricfg`.

---

**User**

can we use 1d and 2d models on these 2 datasets refer @Final_year_report_final_7thSem.pdf 

---

**Cursor**


Yes. You can use 1D and 2D models on both datasets. From your report and the data layout, it works like this:

---

## 1D and 2D from your report

| Model | Techniques | What it does |
|-------|-----------|--------------|
| **1D** | K-Means, Hierarchical | Groups genes based on similarity over all conditions×times (global). |
| **2D** | Cheng & Church biclustering, OPSM | Finds submatrices (Gene × Condition). Ignores time order. |

---

## Using them on your two datasets

### 1. Base paper dataset (synthetic QualityC)

- **Shape:** 500 genes × 10 samples × 4 time points  
- **1D:** Flatten each gene to length 40 (10×4). Use K-Means or Hierarchical clustering on these 500 vectors.  
- **2D:** Use 2D views, e.g.:
  - Gene × Sample for each time slice, or
  - Gene × (Sample×Time) = 500×40 matrix  
  Then run biclustering on these matrices.

### 2. Yeast dataset (TriGen paper)

- **Shape:** 6178 genes × 4 samples × 14 time points  
- **1D:** Flatten each gene to length 56 (4×14). Run K-Means or Hierarchical on these 6178 vectors.  
- **2D:** Same idea as above:
  - Gene × Sample for each time, or
  - Gene × (Sample×Time) = 6178×56  
  Then run biclustering.

---

## Your report’s use of 1D

Phase 2 of your methodology uses K-Means on the flattened tensor to build seeds for the greedy MSR refinement:

> “The 3D tensor is flattened into a 2D matrix. We apply K-Means Clustering with a high k (e.g., k = 100) to partition the genes into small, tight groups. These small clusters serve as Seeds.”

So 1D-style clustering (via K-Means on a flattened view) is already part of your design.

---

## How to structure the comparison

| Step | 1D | 2D | 3D (TriGen) |
|------|----|----|-------------|
| Data view | Gene × (S×T) | Gene × S or Gene × (S×T) | Full tensor |
| Method | K-Means / Hierarchical | Cheng & Church / OPSM | TriGen |
| Output | Gene clusters | Biclusters | Triclusters |

---

## Implementation sketch

1. Load the same tensors (QualityC TSV or TriGen CSVs, yeast CSVs).
2. Reshape: `genes × (samples × times)`.
3. **1D:** Run K-Means (or Hierarchical) on the gene vectors.
4. **2D:** Run biclustering on the 2D matrix, optionally over time slices.
5. For your hybrid: use K-Means seeds as input to the greedy MSR refinement.

The Benchmark_Datasets and yeast data can both be reshaped in this way; you only need to convert them to the formats expected by your 1D/2D scripts (e.g., NumPy arrays or pandas DataFrames).

---

**User**

do it use sklearn

---

**Cursor**


Here’s the `sklearn`-based 1D/2D analysis setup and how to run it.

---

## Script: `run_1d_2d_sklearn.py`

Uses **scikit-learn** for 1D clustering and 2D biclustering on both datasets.

### Methods used

| Model | sklearn class | Description |
|-------|---------------|-------------|
| **1D – K-Means** | `KMeans` | Clusters genes using flattened (sample×time) vectors |
| **1D – Hierarchical** | `AgglomerativeClustering` | Agglomerative clustering on gene vectors |
| **2D – Coclustering** | `SpectralCoclustering` | Row and column partitions on Gene × (Sample×Time) |
| **2D – Biclustering** | `SpectralBiclustering` | Overlapping biclusters on Gene × (Sample×Time) |

### Datasets

1. **QualityC** (base paper) – 500 genes × 10 samples × 5 times → 500×50 matrix  
2. **Yeast** (TriGen paper) – 6178 genes × 4 samples × 14 times → 6178×56 matrix  

### Data flow

- Tensors reshaped to **genes × (samples × times)**
- Z-score standardization for 1D
- Non-negative shift for spectral biclustering
- Cluster labels saved in `output_1d_2d/`

---

## Run

```powershell
cd "d:\8th sem"
.\venv\Scripts\python.exe run_1d_2d_sklearn.py
```

---

## Output

```
output_1d_2d/
├── qualityc_1d_2d_results.npz   # K-Means, Agglomerative, Cocluster labels
└── yeast_1d_2d_results.npz     # Same for Yeast
```

Loaded in Python with: `np.load("output_1d_2d/qualityc_1d_2d_results.npz")`.

---

**User**

run and get me the results all four 

---

**Cursor**


Both runs completed. Here are the results for the four metrics:

---

## **1. Synthetic QualityC (Base paper)**

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| TRI {1}  | 0.700 | 0.750 | 0.501 | 0.501 |
| TRI {2}  | 0.699 | 0.749 | 0.501 | 0.501 |
| TRI {3}  | 0.695 | 0.744 | 0.502 | 0.502 |
| TRI {4}  | 0.699 | 0.748 | 0.501 | 0.502 |
| TRI {5}  | 0.700 | 0.749 | 0.502 | 0.504 |

**Best:** TRI {1}, TRIQ = 0.700

---

## **2. Yeast (TriGen paper)**

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| TRI {1}  | 0.269 | 0.541 | 0.531 | 0.518 |
| TRI {2}  | 0.254 | 0.536 | 0.397 | 0.387 |
| TRI {3}  | 0.244 | 0.527 | 0.332 | 0.321 |
| TRI {4}  | 0.250 | 0.538 | 0.352 | 0.347 |
| TRI {5}  | 0.242 | 0.520 | 0.340 | 0.332 |

**Best:** TRI {1}, TRIQ = 0.269

---

A summary file with these results is saved at `d:\8th sem\TRIQ_results_summary.md`.

---

**User**

these are 1d or 2d?

---

**Cursor**


Those results are **3D**, not 1D or 2D.

**TRIQ, GRQ, PEQ, SPQ** come from **TriGen**, which does **3D triclustering** on the full gene × sample × time tensor. The metrics evaluate each 3D tricluster (sub-tensor).

| Analysis | Method | Metrics |
|----------|--------|---------|
| **1D** | K-Means, Hierarchical (clustering on flattened gene vectors) | Cluster labels, inertia |
| **2D** | SpectralBiclustering, SpectralCoclustering | Row/column labels, biclusters |
| **3D** | TriGen (triclustering on full tensor) | **TRIQ, GRQ, PEQ, SPQ** |

So the table you have is from **3D triclustering** output, not from 1D clustering or 2D biclustering.

---

**User**

Give me the output of 1d and 2d over both the datasets of each algorithm

---

**Cursor**


Running the script to produce the outputs:



Here are the 1D and 2D results for each algorithm on both datasets.

---

## 1D and 2D outputs across both datasets

### Datasets
| Dataset | Shape | n_clusters |
|---------|-------|------------|
| **QualityC** (Base paper synthetic) | 500 genes × 50 features | 5 |
| **Yeast** (TriGen paper) | 6178 genes × 56 features | 5 |

---

## 1D clustering

### K-Means
| Cluster | QualityC (Base paper) | Yeast (TriGen paper) |
|---------|------------------------|----------------------|
| 0 | 103 | 1007 |
| 1 | 112 | 278 |
| 2 | 86 | 1887 |
| 3 | 103 | 1092 |
| 4 | 96 | 1914 |
| **Total** | **500** | **6178** |

### AgglomerativeClustering (hierarchical)
| Cluster | QualityC (Base paper) | Yeast (TriGen paper) |
|---------|------------------------|----------------------|
| 0 | 216 | 1499 |
| 1 | 133 | 1908 |
| 2 | 74 | 353 |
| 3 | 25 | 2336 |
| 4 | 52 | 82 |
| **Total** | **500** | **6178** |

---

## 2D biclustering

### SpectralCoclustering
| Cluster | QualityC Row | QualityC Col | Yeast Row | Yeast Col |
|---------|--------------|--------------|-----------|-----------|
| 0 | 49 | 6 | 245 | 0 |
| 1 | 92 | 10 | 1868 | 16 |
| 2 | 104 | 11 | 589 | 10 |
| 3 | 129 | 15 | 1916 | 22 |
| 4 | 126 | 8 | 1560 | 8 |
| **Total** | **500** | **50** | **6178** | **56** |

Note: Yeast has one column cluster (cluster 0) with 0 columns.

### SpectralBiclustering
5×5 bicluster grid; overlaps allowed. Summary by dataset:

**QualityC** – 25 biclusters:

| Bicluster group | Rows | Cols | Volume range |
|-----------------|------|------|--------------|
| 1–5 | 98 | 18–3 | 294–1764 |
| 6–10 | 104 | 18–3 | 312–1872 |
| 11–15 | 59 | 18–3 | 177–1062 |
| 16–20 | 127 | 18–3 | 381–2286 |
| 21–25 | 112 | 18–3 | 336–2016 |

**Yeast** – 25 biclusters:

| Bicluster group | Rows | Cols | Volume range |
|-----------------|------|------|--------------|
| 1–5 | 455 | 25–5 | 2275–11375 |
| 6–10 | 220 | 25–5 | 1100–5500 |
| 11–15 | 950 | 25–5 | 4750–23750 |
| 16–20 | 2032 | 25–5 | 10160–50800 |
| 21–25 | 2521 | 25–5 | 12605–63025 |

---

## Output files

All results are saved in `d:\8th sem\output_1d_2d\`:

| File | Content |
|------|---------|
| `qualityc_1d_cluster_assignments.csv` | K-Means & Agglomerative labels per gene |
| `qualityc_1d_cluster_sizes.csv` | 1D cluster sizes |
| `qualityc_2d_cocluster_sizes.csv` | SpectralCoclustering row/col sizes |
| `qualityc_2d_spectral_bicluster_sizes.csv` | SpectralBiclustering bicluster sizes |
| `yeast_*` | Same for Yeast |
| `SUMMARY_1d_2d_output.md` | Markdown summary |

---

**User**

what are the metrics there?

---

**Cursor**


In the current 1D/2D analysis, the quantities reported are:

### What’s currently computed

**1D clustering (K-Means, Agglomerative):**
- Cluster sizes – number of genes per cluster
- K-Means inertia – within-cluster sum of squares (only printed to console, not saved)

**2D biclustering:**
- SpectralCoclustering – row cluster sizes, column cluster sizes
- SpectralBiclustering – per-bicluster: `n_rows`, `n_cols`, and volume (`rows × cols`)

### What’s not computed

There are no standard evaluation metrics yet, such as:

- Silhouette score  
- Davies–Bouldin index  
- Calinski–Harabasz index  
- Biclustering measures (MSR, consensus score, etc.)  
- Adjusted Rand Index (would need ground truth labels)

So the “metrics” in our outputs are mainly **cluster/bicluster sizes** and **volume**; there are no formal clustering quality scores.

---

**User**

give these metrics for the 1d and 2d

---

**Cursor**


Inspecting how these metrics are computed in the codebase:



Implementing TRIQ, GRQ, PEQ, and SPQ for 1D and 2D clustering:



Creating a script to compute TRIQ, GRQ, PEQ, and SPQ for 1D and 2D results.



Optimizing the script for large yeast clusters. Using vectorized correlation computations:



Re-running the script:



Here are the **TRIQ, GRQ, PEQ, and SPQ** results for 1D and 2D clustering on both datasets:

---

## QualityC (Base paper)

### 1D K-Means (5 clusters)

| Solution   | TRIQ  | GRQ   | PEQ   | SPQ   |
|:----------|:------|:------|:------|:------|
| KMeans {1} | 0.100 | 0.094 | 0.127 | 0.126 |
| KMeans {2} | 0.093 | 0.086 | 0.121 | 0.120 |
| KMeans {3} | 0.106 | 0.101 | 0.126 | 0.126 |
| KMeans {4} | 0.073 | 0.062 | 0.118 | 0.116 |
| KMeans {5} | 0.087 | 0.078 | 0.122 | 0.122 |

### 1D Agglomerative (5 clusters)

| Solution            | TRIQ  | GRQ   | PEQ   | SPQ   |
|:--------------------|:------|:------|:------|:------|
| Agglomerative {1}   | 0.058 | 0.043 | 0.119 | 0.119 |
| Agglomerative {2}   | 0.071 | 0.058 | 0.121 | 0.121 |
| Agglomerative {3}   | 0.127 | 0.125 | 0.136 | 0.137 |
| Agglomerative {4}   | 0.220 | 0.224 | 0.207 | 0.202 |
| Agglomerative {5}   | 0.116 | 0.113 | 0.131 | 0.130 |

### 2D SpectralCoclustering (best 5 blocks)

| Solution      | TRIQ  | GRQ   | PEQ   | SPQ   |
|:--------------|:------|:------|:------|:------|
| Cocluster {16}| 0.271 | 0.246 | 0.373 | 0.372 |
| Cocluster {6} | 0.260 | 0.232 | 0.373 | 0.370 |
| Cocluster {1} | 0.233 | 0.198 | 0.373 | 0.380 |
| Cocluster {21}| 0.212 | 0.171 | 0.378 | 0.376 |
| Cocluster {5} | 0.219 | 0.198 | 0.306 | 0.302 |

### 2D SpectralBiclustering (5 biclusters)

| Solution      | TRIQ  | GRQ   | PEQ   | SPQ   |
|:--------------|:------|:------|:------|:------|
| Bicluster {1} | 0.158 | 0.147 | 0.201 | 0.202 |
| Bicluster {2} | 0.173 | 0.154 | 0.249 | 0.250 |
| Bicluster {3} | 0.339 | 0.300 | 0.495 | 0.493 |
| Bicluster {4} | 0.201 | 0.190 | 0.245 | 0.241 |
| Bicluster {5} | 0.417 | 0.360 | 0.638 | 0.650 |

---

## Yeast (TriGen paper)

### 1D K-Means (5 clusters)

| Solution   | TRIQ  | GRQ   | PEQ   | SPQ   |
|:----------|:------|:------|:------|:------|
| KMeans {1} | 0.277 | 0.272 | 0.297 | 0.295 |
| KMeans {2} | 0.373 | 0.359 | 0.426 | 0.436 |
| KMeans {3} | 0.112 | 0.100 | 0.167 | 0.159 |
| KMeans {4} | 0.212 | 0.203 | 0.248 | 0.242 |
| KMeans {5} | 0.125 | 0.112 | 0.177 | 0.177 |

### 1D Agglomerative (5 clusters)

| Solution            | TRIQ  | GRQ   | PEQ   | SPQ   |
|:--------------------|:------|:------|:------|:------|
| Agglomerative {1}   | 0.150 | 0.136 | 0.209 | 0.202 |
| Agglomerative {2}   | 0.134 | 0.117 | 0.204 | 0.198 |
| Agglomerative {3}   | 0.337 | 0.332 | 0.356 | 0.357 |
| Agglomerative {4}   | 0.074 | 0.054 | 0.158 | 0.153 |
| Agglomerative {5}   | 0.422 | 0.413 | 0.462 | 0.448 |

### 2D SpectralCoclustering (best 5 blocks)

| Solution      | TRIQ  | GRQ   | PEQ   | SPQ   |
|:--------------|:------|:------|:------|:------|
| Cocluster {2} | 0.376 | 0.374 | 0.395 | 0.368 |
| Cocluster {12}| 0.380 | 0.388 | 0.360 | 0.328 |
| Cocluster {5} | 0.395 | 0.395 | 0.400 | 0.388 |
| Cocluster {3} | 0.331 | 0.331 | 0.338 | 0.330 |
| Cocluster {4} | 0.327 | 0.320 | 0.358 | 0.357 |

### 2D SpectralBiclustering (5 biclusters)

| Solution      | TRIQ  | GRQ   | PEQ   | SPQ   |
|:--------------|:------|:------|:------|:------|
| Bicluster {1} | 0.166 | 0.146 | 0.245 | 0.239 |
| Bicluster {2} | 0.397 | 0.392 | 0.423 | 0.408 |
| Bicluster {3} | 0.419 | 0.415 | 0.471 | 0.395 |
| Bicluster {4} | 0.497 | 0.518 | 0.424 | 0.402 |
| Bicluster {5} | 0.433 | 0.431 | 0.445 | 0.438 |

---

These values are 2D versions of the 3D triclustering metrics:
- **GRQ**: MSR-based structural coherence of the submatrix  
- **PEQ**: Mean absolute Pearson correlation between rows  
- **SPQ**: Mean absolute Spearman correlation between rows  
- **TRIQ**: `(0.4×GRQ + 0.05×PEQ + 0.05×SPQ) / 0.5`

Full results are in:
- `output_1d_2d\1d_2d_TRIQ_metrics_report.md`
- `output_1d_2d\1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv`

---

**User**

in 2d what are {2},{12}???

---

**Cursor**


In 2D **SpectralCoclustering**, rows and columns are partitioned into 5 clusters each. That gives 25 blocks in a 5×5 grid. The numbers **{2}**, **{12}**, etc. are just block labels.

They correspond to **(row_cluster, col_cluster)**:

- **Cocluster {1}** = row cluster 0, column cluster 0  
- **Cocluster {2}** = row cluster 0, column cluster 1  
- **Cocluster {3}** = row cluster 0, column cluster 2  
- …  
- **Cocluster {6}** = row cluster 1, column cluster 0  
- **Cocluster {12}** = row cluster 2, column cluster 1  
- …  
- **Cocluster {25}** = row cluster 4, column cluster 4  

Mapping: `block_id = row_cluster × 5 + col_cluster + 1`.

So **{2}** is the block with rows from cluster 0 and columns from cluster 1, and **{12}** is the block with rows from cluster 2 and columns from cluster 1. They are labels for which block you are looking at, not cluster counts.

---

**User**

2D SpectralCoclustering (best 5 blocks) what are blocks and how many block we ran that you showed best 5

---

**Cursor**


Here’s the breakdown:

### What are blocks?

In **SpectralCoclustering**, rows and columns are both partitioned into 5 clusters. The data is then organized into a **5×5 grid** of blocks. Each block is one cell of that grid:

- **Block (i,j)** = submatrix of rows in row cluster `i` and columns in column cluster `j`.

So each block is one bicluster: a subset of genes × a subset of features.

---

### How many blocks were run?

- **QualityC:** all **25 blocks** (5 row clusters × 5 column clusters)
- **Yeast:** **24 blocks** (one block had 0 columns, so it was skipped)

So we evaluated 25 (or 24 for Yeast) blocks in total.

---

### “Best 5 blocks” — what does that mean?

All blocks were evaluated with TRIQ, GRQ, PEQ, SPQ. The “best 5” in the summary are simply the **top 5 blocks by TRIQ**. The rest were computed but not shown in that shortened table.

If you want the full list, it’s in `output_1d_2d\1d_2d_TRIQ_metrics_report.md` (and `1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv`) — all 25 QualityC blocks and 24 Yeast blocks are there.

---

**User**

give me the algorithm and formula for all the metrics

---

**Cursor**


Here are the **algorithm and formula** for each metric:

---

## 1. GRQ — Gene Residual Quality

**Meaning:** Structural coherence of the cluster/bicluster.

### 3D (TrLab/TriGen)

**Steps:**
1. Project the tricluster onto three views: Gene–Condition–Time (GCT), Gene–Time–Condition (GTC), Time–Gene–Condition (TGC).
2. For each view, compute gradient angles between adjacent points:
   - gradient = (y₂ − y₁) / (x₂ − x₁)
   - angle = atan(gradient), then map to [0, 2π] if negative
3. Compute pairwise angular differences across rows/columns for each view.
4. Average the mean angular spread over the three views: \( m = \frac{1}{3}\bigl(m_{GCT} + m_{GTC} + m_{TGC}\bigr) \)
5. Normalize:
   \[
   \text{GRQ} = 1 - \frac{m}{2\pi}
   \]
   with maximum angular spread \(2\pi\).

Higher GRQ → more coherent structure (smaller angular spread).

### 2D (1D/2D adaptation)

Use **Mean Squared Residue (MSR)**:

\[
\text{MSR} = \frac{1}{|I| \cdot |J|} \sum_{i \in I,\, j \in J} \bigl( a_{ij} - \bar{a}_{iJ} - \bar{a}_{Ij} + \bar{a}_{IJ} \bigr)^2
\]

where:
- \(a_{ij}\): entries in the submatrix
- \(\bar{a}_{iJ}\): row mean
- \(\bar{a}_{Ij}\): column mean  
- \(\bar{a}_{IJ}\): global mean

Then:

\[
\text{GRQ} = \max\left(0,\; 1 - \frac{\text{MSR}}{\text{Var}(X)}\right)
\]

Lower MSR and higher Var(X) → higher GRQ.

---

## 2. PEQ — Pearson Enrichment Quality

**Meaning:** Strength of linear correlation within the cluster.

### Algorithm (same for 3D and 2D)

1. Build a matrix with **rows = genes** (variables), **columns = features** (conditions×times in 3D; conditions in 2D).
2. Compute the Pearson correlation matrix between rows (gene profiles).
3. Take the upper-diagonal entries of the correlation matrix (excluding the diagonal).
4. Take the mean of their absolute values:

\[
\text{PEQ} = \frac{1}{\binom{n}{2}} \sum_{i < j} \bigl| r_{ij}^{\text{Pearson}} \bigr|
\]

where \(r_{ij}^{\text{Pearson}}\) is the Pearson correlation between gene \(i\) and gene \(j\).

Higher PEQ → stronger average linear correlation.

---

## 3. SPQ — Spearman Enrichment Quality

**Meaning:** Strength of monotonic relationships within the cluster.

### Algorithm (same as PEQ, but with Spearman)

Same steps as PEQ, but using Spearman instead of Pearson:

\[
\text{SPQ} = \frac{1}{\binom{n}{2}} \sum_{i < j} \bigl| r_{ij}^{\text{Spearman}} \bigr|
\]

Higher SPQ → stronger monotonic structure.

---

## 4. TRIQ — Tricluster Index of Quality

**Meaning:** Combined quality score.

### Formula (without BIOQ)

\[
\text{TRIQ} = \frac{w_{\text{GRQ}} \cdot \text{GRQ} + w_{\text{PEQ}} \cdot \text{PEQ} + w_{\text{SPQ}} \cdot \text{SPQ}}{w_{\text{GRQ}} + w_{\text{PEQ}} + w_{\text{SPQ}}}
\]

**Default weights (from TrLab):**

| Metric | Weight |
|--------|--------|
| GRQ    | 0.4    |
| PEQ    | 0.05   |
| SPQ    | 0.05   |

So:

\[
\text{TRIQ} = \frac{0.4 \cdot \text{GRQ} + 0.05 \cdot \text{PEQ} + 0.05 \cdot \text{SPQ}}{0.5}
\]

---

### Summary table

| Metric | Range | Higher means |
|--------|-------|--------------|
| GRQ | [0, 1] | More coherent structure |
| PEQ | [0, 1] | Stronger linear correlation |
| SPQ | [0, 1] | Stronger monotonic correlation |
| TRIQ | [0, 1] | Better overall quality |

---

**User**

give the 1d and 2d model's algorithm and script used

---

**Cursor**


---

## 1D and 2D models: algorithms and scripts

### Data representation

3D tensor (genes × samples × times) is flattened to a 2D matrix (genes × features), with features = samples × times.

---

## 1D models

### 1. K-Means

**Purpose:** Partition genes into `k` clusters by minimizing within-cluster sum of squares.

**Algorithm (sklearn):**
1. Standardize features: `StandardScaler` (zero mean, unit variance)
2. Initialize `k` centroids (K-Means++ by default)
3. Iterate until stable:
   - Assign each gene to closest centroid (Euclidean distance)
   - Update each centroid as mean of assigned genes
4. Objective: minimize inertia = ∑(gene − centroid)²

**Parameters used:** `n_clusters=5`, `random_state=42`, `n_init=10`

---

### 2. Agglomerative (hierarchical) clustering

**Purpose:** Group genes by merging closest clusters until `k` clusters remain.

**Algorithm (sklearn):**
1. Standardize features
2. Start with each gene as its own cluster (n clusters)
3. Repeat until k clusters remain:
   - Merge two clusters with smallest distance (Ward linkage by default)
   - Ward minimizes increase in within-cluster variance
4. Cut the dendrogram at `k` clusters to get labels

**Parameters used:** `n_clusters=5`, linkage = Ward (default)

---

## 2D models

### 3. Spectral Coclustering

**Purpose:** Partition rows (genes) and columns (features) so that high values concentrate in certain row–column blocks.

**Algorithm (sklearn):**
1. Make data non‑negative (shift by min + ε)
2. Build bipartite graph:
   - Rows and columns as nodes
   - Edge weights from data values
3. Use normalized spectral clustering (Laplacian) on the bipartite graph
4. Get row and column labels
5. Each (row_cluster, col_cluster) pair defines a block (5×5 → 25 blocks)

**Parameters used:** `n_clusters=5`, `random_state=42`

---

### 4. Spectral Biclustering

**Purpose:** Find overlapping biclusters with checkerboard-like structure.

**Algorithm (sklearn):**
1. Make data non‑negative
2. Construct row- and column-similarity matrices
3. Embed via row and column Laplacians (spectral approach)
4. Use k-means on the embedded space to get row and column subclusters
5. Biclusters = pairs of row and column subclusters (5×5 → 25 overlapping biclusters)

**Parameters used:** `n_clusters=(5, 5)`, `random_state=42`, `n_init=3`

---

## Scripts used

| Script | Path | Role |
|--------|------|------|
| **`run_1d_2d_sklearn.py`** | `d:\8th sem\run_1d_2d_sklearn.py` | Runs 1D and 2D models and saves cluster/bicluster assignments |
| **`compute_1d_2d_triq_metrics.py`** | `d:\8th sem\compute_1d_2d_triq_metrics.py` | Computes TRIQ, GRQ, PEQ, SPQ for 1D clusters and 2D blocks/biclusters |

**Libraries:**
- `sklearn.cluster`: `KMeans`, `AgglomerativeClustering`, `SpectralCoclustering`, `SpectralBiclustering`
- `sklearn.preprocessing`: `StandardScaler`
- `scipy.stats`: `spearmanr`
- `numpy`, `pandas`

---

## Run commands

```powershell
# Clustering + assignments
& "d:\8th sem\venv\Scripts\python.exe" "d:\8th sem\run_1d_2d_sklearn.py"

# TRIQ/GRQ/PEQ/SPQ metrics
& "d:\8th sem\venv\Scripts\python.exe" "d:\8th sem\compute_1d_2d_triq_metrics.py"
```

---

**User**

give the algorithm not standardscaler

---

**Cursor**


---

## 1D and 2D algorithms (without preprocessing)

### 1D algorithms

#### K-Means

**Goal:** Partition \( n \) genes into \( k \) clusters by minimizing within-cluster variance.

**Algorithm:**
1. Fix \( k \) and choose initial centroids \( \mu_1, \ldots, \mu_k \) (e.g. K-Means++).
2. **Assignment:** For each gene vector \( \mathbf{x}_i \), assign to nearest centroid:
   \[
   c_i = \arg\min_{j} \|\mathbf{x}_i - \mu_j\|^2
   \]
3. **Update:** For each cluster \( j \), set:
   \[
   \mu_j = \frac{1}{|C_j|} \sum_{i \in C_j} \mathbf{x}_i
   \]
4. Repeat steps 2–3 until cluster assignments do not change.
5. **Objective:** Minimize inertia  
   \( \sum_{j=1}^{k} \sum_{i \in C_j} \|\mathbf{x}_i - \mu_j\|^2 \).

---

#### Agglomerative (hierarchical) clustering

**Goal:** Build a hierarchy by repeatedly merging the two closest clusters until \( k \) clusters remain.

**Algorithm:**
1. Start with \( n \) clusters (one per gene).
2. Compute pairwise distances between genes.
3. Repeat until there are \( k \) clusters:
   - Pick the two clusters with smallest distance.
   - Merge them.
   - Recompute distances (Ward: minimize increase in within-cluster variance).
4. **Ward distance** for clusters \( A \) and \( B \):
   \[
   d(A,B) = \frac{|A||B|}{|A|+|B|} \|\mu_A - \mu_B\|^2
   \]
5. Cut the dendrogram at height that yields \( k \) clusters.

---

### 2D algorithms

#### Spectral Coclustering

**Goal:** Partition rows (genes) and columns (features) so that high values concentrate in certain blocks.

**Algorithm:**
1. Build bipartite graph: rows and columns as nodes, edge weights from \( a_{ij} \).
2. Define normalized bipartite Laplacian \( L \) from the adjacency matrix.
3. Compute eigenvectors of \( L \) for the smallest eigenvalues.
4. Embed rows and columns in a low-dimensional space.
5. Run K-Means on the embedded points to get:
   - \( k \) row clusters,
   - \( k \) column clusters.
6. Each (row cluster, column cluster) pair is a block.

---

#### Spectral Biclustering

**Goal:** Find overlapping biclusters that approximate checkerboard structure in the matrix.

**Algorithm:**
1. Form row and column similarity matrices from the data.
2. Build normalized Laplacians for rows and columns separately.
3. Compute leading eigenvectors of row and column Laplacians.
4. Embed rows and columns in a low-dimensional space.
5. Apply K-Means with \( k \times k \) centroids to the embedded points.
6. Each bicluster is the intersection of a row subcluster and a column subcluster.
7. Biclusters may overlap (a gene/feature can be in several biclusters).

---

---

**User**

in algorithm like pseudo code

---

**Cursor**


---

## 1D and 2D Algorithms (Pseudo-code)

---

### 1. K-Means (1D)

```
Input:  X[n × d] (n genes, d features), k (number of clusters)
Output: labels[n] (cluster index per gene)

1. Initialize k centroids μ[1..k] (e.g., K-Means++)

2. REPEAT until convergence:
    3. FOR each gene i = 1 to n:
           labels[i] ← argmin_j ||X[i] - μ[j]||²
       
    4. FOR each cluster j = 1 to k:
           C[j] ← { i : labels[i] = j }
           μ[j] ← mean(X[i] for i in C[j])
       
    5. IF labels did not change THEN break

6. RETURN labels
```

---

### 2. Agglomerative Clustering (1D)

```
Input:  X[n × d] (n genes, d features), k (number of clusters)
Output: labels[n] (cluster index per gene)

1. Initialize: each gene i is its own cluster, C = { {1}, {2}, ..., {n} }
   n_clusters ← n

2. Compute distance matrix D[n×n] where D[i,j] = ||X[i] - X[j]||²

3. WHILE n_clusters > k:
       4. (a, b) ← pair of clusters with minimum Ward distance
       5. Merge C[a] and C[b] into one cluster
       6. n_clusters ← n_clusters - 1
       7. Update distances for new cluster

4. Assign cluster IDs 0..k-1 to final clusters
5. FOR each gene i: labels[i] ← ID of cluster containing i

6. RETURN labels
```

---

### 3. Spectral Coclustering (2D)

```
Input:  A[n × m] (n genes, m features), k (number of row/col clusters)
Output: row_labels[n], col_labels[m]

1. Build bipartite affinity matrix:
        W = [ 0    A  ]
            [ Aᵀ   0  ]   (block structure, size (n+m)×(n+m))

2. Degree matrix D[i,i] = sum_j W[i,j]
   Laplacian L = D - W  (or normalized: L_norm = I - D^(-1/2) W D^(-1/2))

3. Compute k smallest eigenvectors v[1], v[2], ..., v[k] of L

4. Stack into embedding: V = [v[1] | v[2] | ... | v[k]]  (shape (n+m)×k)

5. Split V into V_rows (first n rows) and V_cols (last m rows)

6. row_labels ← KMeans(V_rows, k)
   col_labels ← KMeans(V_cols, k)

7. RETURN row_labels, col_labels
```

---

### 4. Spectral Biclustering (2D)

```
Input:  A[n × m] (n genes, m features), (k_row, k_col)
Output: biclusters (list of (row_mask, col_mask) pairs)

1. Row affinity:  W_row = A × Aᵀ   (n×n)
   Col affinity: W_col = Aᵀ × A    (m×m)

2. Row Laplacian L_row from W_row (normalized)
   Col Laplacian L_col from W_col (normalized)

3. U ← k_row eigenvectors of L_row (smallest eigenvalues)
   V ← k_col eigenvectors of L_col (smallest eigenvalues)

4. Concatenate row+col embeddings: Z = [U ; V]  or use joint embedding

5. K-Means on rows:  row_subclusters ← KMeans(U, k_row)
   K-Means on cols:  col_subclusters ← KMeans(V, k_col)

6. FOR r = 1 to k_row:
       FOR c = 1 to k_col:
           row_mask[r,c] ← (row_subclusters == r)
           col_mask[r,c] ← (col_subclusters == c)
           bicluster[r,c] ← (row_mask[r,c], col_mask[r,c])

7. RETURN biclusters
```

---

---

**User**

input and output sample example for all

---

**Cursor**


---

## Input and output examples for all algorithms

---

### Common input example

**Example data matrix** (genes × features, simplified for illustration):

```
         f0    f1    f2    f3    f4    f5    f6    f7    ...  f49
gene_0   2.1   2.3   2.0   2.2   2.4   2.1   2.3   2.2   ...  2.1
gene_1  -0.5  -0.3  -0.6  -0.4  -0.2  -0.5  -0.3  -0.4  ... -0.5
gene_2   1.8   2.0   1.9   1.7   2.1   1.8   2.0   1.9   ...  2.0
gene_3  -0.8  -0.6  -0.9  -0.7  -0.5  -0.8  -0.6  -0.7  ... -0.6
gene_4   0.1   0.3   0.0   0.2   0.4   0.1   0.3   0.2   ...  0.1
...
gene_499 1.5  1.7  1.4  1.6  1.8  1.5  1.7  1.6  ...  1.5

Shape: 500 genes × 50 features (QualityC)  OR  6178 genes × 56 features (Yeast)
```

---

### 1. K-Means (1D)

**Input:**
```
X[500 × 50]  (matrix above)
k = 5
```

**Output:**
```
labels[500] = [0, 2, 0, 2, 1, 3, 0, 2, 4, 1, ...]

Interpretation: gene_i is in cluster labels[i]
Cluster sizes: [103, 112, 86, 103, 96]
```

**Example mapping:**

| gene | cluster (label) |
|------|-----------------|
| gene_0 | 0 |
| gene_1 | 2 |
| gene_2 | 0 |
| gene_3 | 2 |
| gene_4 | 1 |
| … | … |

---

### 2. Agglomerative (1D)

**Input:**
```
X[500 × 50]  (same matrix)
k = 5
```

**Output:**
```
labels[500] = [1, 0, 1, 0, 2, 3, 1, 0, 4, 2, ...]

Interpretation: gene_i is in cluster labels[i]
Cluster sizes: [216, 133, 74, 25, 52]
```

**Example mapping:**

| gene | cluster (label) |
|------|-----------------|
| gene_0 | 1 |
| gene_1 | 0 |
| gene_2 | 1 |
| gene_3 | 0 |
| gene_4 | 2 |
| … | … |

---

### 3. Spectral Coclustering (2D)

**Input:**
```
A[500 × 50]  (same matrix)
k = 5
```

**Output:**
```
row_labels[500] = [2, 0, 2, 0, 4, 3, 2, 0, 1, 4, ...]
col_labels[50]  = [1, 2, 1, 3, 0, 1, 2, 4, 1, 3, ...]

Interpretation:
  - gene_i in row_cluster row_labels[i]
  - feature_j in col_cluster col_labels[j]
  - Block (i,j) = genes with row_label=i ∩ features with col_label=j
```

**Example row/column assignment:**

| gene | row_cluster | feature | col_cluster |
|------|-------------|---------|-------------|
| gene_0 | 2 | f0 | 1 |
| gene_1 | 0 | f1 | 2 |
| gene_2 | 2 | f2 | 1 |
| … | … | … | … |

**Blocks (5×5 grid, 25 total):**

```
Block {1}  = row_cluster 0 × col_cluster 0  →  49 genes × 6 features
Block {2}  = row_cluster 0 × col_cluster 1  →  49 genes × 10 features
...
Block {16} = row_cluster 3 × col_cluster 0  →  129 genes × 6 features
...
```

---

### 4. Spectral Biclustering (2D)

**Input:**
```
A[500 × 50]  (same matrix)
k_row = 5, k_col = 5
```

**Output:**
```
Bicluster 1: row_mask[500], col_mask[50]
  - row_mask = [T,T,T,...,F,F,...]  (98 genes True)
  - col_mask = [T,T,T,...,T,F,...] (18 features True)
  - Bicluster 1 = 98 × 18 submatrix

Bicluster 2: row_mask[500], col_mask[50]
  - 98 genes × 12 features

Bicluster 3: 104 genes × 4 features
...
Bicluster 5: 98 genes × 3 features
```

**Example bicluster content:**

| Bicluster | Genes (example) | Features (example) | Size |
|-----------|-----------------|-------------------|------|
| 1 | gene_0, gene_2, gene_5, ... | f0, f1, f2, f5, f6, ... | 98 × 18 |
| 2 | gene_0, gene_2, gene_5, ... | f3, f7, f10, ... | 98 × 12 |
| 3 | gene_6, gene_10, gene_15, ... | f0, f1, f2, f5, ... | 104 × 18 |
| … | … | … | … |

---

### Summary

| Algorithm | Input | Output |
|-----------|-------|--------|
| **K-Means** | X[n×d], k | labels[n] – one cluster per gene |
| **Agglomerative** | X[n×d], k | labels[n] – one cluster per gene |
| **SpectralCoclustering** | A[n×m], k | row_labels[n], col_labels[m] – 25 blocks |
| **SpectralBiclustering** | A[n×m], (5,5) | 25 biclusters (row_mask, col_mask), may overlap |

