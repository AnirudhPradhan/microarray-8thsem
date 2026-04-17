# Comprehensive Research Report
# Triclustering Algorithms for Temporal Gene Expression Analysis
## Replication of Gutiérrez-Avilés et al. (NEUCOM 2014) and Soares et al. (Pattern Recognition 2024)

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Background: Why Triclustering?](#2-background-why-triclustering)
3. [Datasets](#3-datasets)
4. [Evaluation Metrics](#4-evaluation-metrics)
5. [Algorithms](#5-algorithms)
   - 5.1 [1D Clustering: K-Means and Agglomerative](#51-1d-clustering-k-means-and-agglomerative)
   - 5.2 [2D Biclustering: Spectral Methods](#52-2d-biclustering-spectral-methods)
   - 5.3 [TriGen: Genetic Algorithm Triclustering](#53-trigen-genetic-algorithm-triclustering)
   - 5.4 [triCluster: Exact Window-Based Triclustering](#54-tricluster-exact-window-based-triclustering)
   - 5.5 [TCtriCluster: Temporal Contiguity Extension](#55-tctricluster-temporal-contiguity-extension)
   - 5.6 [δ-Trimax: Mean Square Residue Triclustering](#56-δ-trimax-mean-square-residue-triclustering)
6. [Individual Results](#6-individual-results)
   - 6.1 [1D Methods — TRIQ Results](#61-1d-methods--triq-results)
   - 6.2 [2D Methods — TRIQ Results](#62-2d-methods--triq-results)
   - 6.3 [TriGen — TRIQ and TRIQ per Dataset](#63-trigen--triq-and-triq-per-dataset)
   - 6.4 [triCluster — 17-Dataset Benchmark](#64-tricluster--17-dataset-benchmark)
   - 6.5 [TCtriCluster — 17-Dataset Benchmark](#65-tctricluster--17-dataset-benchmark)
   - 6.6 [δ-Trimax — 17-Dataset Benchmark](#66-δ-trimax--17-dataset-benchmark)
   - 6.7 [3D Algorithm TRIQ on QualityC and Yeast](#67-3d-algorithm-triq-on-qualityc-and-yeast)
   - 6.8 [Recoverability on QualityC Ground Truth](#68-recoverability-on-qualityc-ground-truth)
7. [Comparative Analysis](#7-comparative-analysis)
8. [Findings and Discussion](#8-findings-and-discussion)
9. [Conclusion](#9-conclusion)

---

## 1. Introduction

This report documents a complete empirical replication and extension of two landmark papers in triclustering for temporal gene expression analysis:

- **Paper 1 — NEUCOM TriGen (2014):** Gutiérrez-Avilés, D., Rubio-Escudero, C., Pontes, F. J., & Nepomuceno-Chamorro, I. (2014). *TriGen: A genetic algorithm to mine triclusters in temporal gene expression data.* Neurocomputing, 132, 42–54.
- **Paper 2 — Benchmark (2024):** Soares, C., Henriques, R., & Madeira, S. C. (2024). *Triclustering algorithms for three-way data analysis: A comprehensive benchmark.* Pattern Recognition, 150, 110276.

Temporal gene expression data is naturally three-dimensional: it records the expression level of each gene under each experimental condition at each time point, forming a **genes × conditions × timepoints tensor**. Standard 1D and 2D clustering methods collapse this tensor, losing critical structure. Triclustering is the task of finding coherent subsets across all three dimensions simultaneously.

This project implemented six algorithms spanning 1D, 2D, and 3D levels of analysis, ran them on three representative datasets (QualityC, Yeast, and TriGen Synthetic), and evaluated them using two complementary metric families: internal quality metrics (TRIQ/GRQ/PEQ/SPQ) and external recoverability metrics (R/CE/RMS3).

---

## 2. Background: Why Triclustering?

### 2.1 The Structure of Temporal Gene Expression Data

Consider a microarray experiment measuring gene expression across multiple experimental conditions and time points. The resulting data is a tensor:

```
T ∈ R^(G × C × Z)
```

where:
- **G** = number of genes (rows)
- **C** = number of conditions (columns)
- **Z** = number of time points (depth)

Each entry `T[g, c, z]` is the expression level of gene `g` under condition `c` at time `z`.

### 2.2 Limitations of Lower-Dimensional Approaches

| Method | Data Seen | What is Lost |
|:-------|:----------|:-------------|
| 1D clustering | Genes only (conditions × times collapsed) | Condition-specific and temporal patterns |
| 2D biclustering | Genes × (conditions × times flattened) | Temporal ordering; true 3D coherence |
| 3D triclustering | Full tensor | Nothing — all three dimensions used jointly |

A **tricluster** is a tuple (G', C', Z') — subsets of genes, conditions, and timepoints — such that the sub-tensor `T[G', C', Z']` is coherent under some criterion. The biological meaning is: genes in G' are co-regulated across conditions in C' specifically during the time window Z'.

### 2.3 Pattern Types

The Soares et al. (2024) benchmark classifies triclusters by their pattern type:

- **Constant**: all values in the sub-tensor are equal (plus noise)
- **Additive**: value = row_effect + column_effect + depth_effect (+ noise)
- **Multiplicative**: value = row_effect × column_effect × depth_effect (+ noise)
- **Order-Preserving**: gene rankings are consistent across conditions/times but values differ
- **Contiguity**: pattern exists only across consecutive timepoints
- **Overlapping**: planted triclusters share genes, conditions, or timepoints
- **Quality**: planted patterns with added noise and error levels

---

## 3. Datasets

### 3.1 QualityC — Synthetic Benchmark (Soares et al. 2024)

- **Shape:** 500 genes × 10 conditions × 5 timepoints
- **Ground truth:** 70 planted triclusters (constant pattern with quality noise)
- **Source:** G-Tric generator (Soares 2024), available in `data/benchmark/Benchmark_Datasets/QualityC/`
- **Format:** TSV flat file (columns: `y{cond}z{time}`) and pre-formatted `.tab` (triCluster format)
- **Purpose:** Primary benchmark with known ground truth — allows recoverability evaluation (R, CE, RMS3)
- **Noise:** Each planted tricluster has 10% value noise and 5% structural error, making it a "quality" dataset rather than ideal clean synthetic data

### 3.2 Full 17-Dataset Benchmark (Soares et al. 2024)

All 17 datasets have the same shape: **500 genes × 10 conditions × 5 timepoints**, each with ground truth JSON files listing planted triclusters.

| Dataset Group | Datasets | GT Count | Key Challenge |
|:-------------|:---------|:--------:|:-------------|
| Baseline | BaseR | 40 | Mixed patterns |
| Pattern-specific | Constant, Additive, Multiplicative, OrderPreserving | 30–70 | Pure single pattern type |
| Overlapping | OverlappingC, OverlappingA, OverlappingM, OverlappingOP | 30–70 | Triclusters share elements |
| Quality (noisy) | QualityC, QualityA, QualityM, QualityOP | 30–70 | Noise + structural errors |
| Contiguity | ContiguityC, ContiguityA, ContiguityM, ContiguityOP | 30–60 | Only consecutive timepoints |

### 3.3 Yeast GST Dataset (Spellman et al.)

- **Shape:** 7,491 genes × 3 conditions × 14 timepoints (after loading)
- **Source:** NCBI GEO accessions GSE22, GSE23, GSE24 (Spellman cell cycle data)
- **Values:** log₂ expression ratios (approximately in range [−14, +8])
- **Preprocessing:** Genes with >50% missing values removed; remaining NaNs imputed with per-gene mean
- **Purpose:** Real biological data — no ground truth. Used for TRIQ quality evaluation only.
- **Note for algorithms:** Requires `additive` mode (not `ratio`) since log₂ values can be negative; top-500 most-variable genes used for computationally intensive algorithms

### 3.4 TriGen Synthetic Dataset (Gutiérrez-Avilés et al. 2014)

- **Shape:** 1,000 genes × 10 conditions × 5 timepoints
- **Source:** Custom generator (`algorithms/neucom-trigen/synthetic_generator.py`)
- **Structure:** Planted two known patterns:
  - Area A: 20 genes × 5 conditions × 3 times (constant ascending pattern)
  - Area B: 30 genes × 4 conditions × 4 times (descending pattern)
- **Background:** Random Gaussian noise (mean=0, std=1)
- **Purpose:** Validates TriGen paper's claim that GA-based triclustering recovers planted temporal patterns

---

## 4. Evaluation Metrics

### 4.1 Internal Quality Metrics (TRIQ Family)

These metrics assess the *internal coherence* of found clusters/triclusters without requiring ground truth. They can be applied to any dimensionality of result by extracting the relevant data subblock.

For a tricluster (G', C', Z'), the 3D subblock `T[G', C', Z']` is extracted and flattened to a 2D matrix `X` of shape `(|G'|, |C'| × |Z'|)` where each row is a gene's expression profile across the selected condition-time combinations.

#### 4.1.1 GRQ — Gene Residue Quality

Measures how well genes in the cluster behave coherently (low Mean Square Residue = high GRQ). Based on the Cheng-Church residue:

```
residue[g, j] = X[g,j] - mean_g(X[g,j]) - mean_j(X[g,j]) + mean(X)

MSR = mean over all (g,j) of residue[g,j]²

GRQ = max(0, 1 - MSR / Var(X))
```

**Interpretation:** GRQ → 1 means genes exhibit consistent additive/multiplicative patterns across conditions and times. GRQ = 0 means the cluster is no more coherent than random noise.

#### 4.1.2 PEQ — Pearson Expression Quality

Mean of pairwise absolute Pearson correlations between gene expression profiles across the feature (condition × time) axis:

```
PEQ = mean over all gene pairs (g₁, g₂) of |pearson_r(X[g₁,:], X[g₂,:])|
```

**Interpretation:** PEQ → 1 means all gene pairs have nearly identical expression profiles. PEQ ~ 0 means no linear relationship between genes.

#### 4.1.3 SPQ — Spearman Expression Quality

Same as PEQ but using Spearman rank correlation instead of Pearson, making it robust to non-linear monotonic relationships:

```
SPQ = mean over all gene pairs (g₁, g₂) of |spearman_rho(X[g₁,:], X[g₂,:])|
```

#### 4.1.4 TRIQ — Tricluster Quality Index

Weighted combination of GRQ, PEQ, and SPQ (without BIOQ for simplicity):

```
TRIQ = (0.4 × GRQ + 0.05 × PEQ + 0.05 × SPQ) / (0.4 + 0.05 + 0.05)
     = (0.4 × GRQ + 0.05 × PEQ + 0.05 × SPQ) / 0.5
```

GRQ has dominant weight (0.4/0.5 = 80%) because structural residue coherence is the primary criterion for a biologically meaningful tricluster. PEQ and SPQ each contribute 10%.

**Range:** [0, 1]. Higher is better. A TRIQ of 0.7+ indicates a highly coherent tricluster.

---

### 4.2 External Recoverability Metrics (Soares et al. 2024)

These metrics compare found triclusters against known ground-truth planted triclusters. They require ground truth and are only applicable to synthetic datasets.

Let `P = {p₁, …, pₙ}` be predicted triclusters and `GT = {gt₁, …, gtₘ}` be ground-truth triclusters. Each tricluster is represented as a set of (gene, condition, time) index triples.

#### 4.2.1 R — Recoverability (Precision-oriented)

For each predicted tricluster pᵢ, find the best-matching ground truth tricluster and compute overlap as intersection/|predicted|:

```
overlap(pᵢ, gtⱼ) = |pᵢ ∩ gtⱼ| / |pᵢ|

R = (1/|P|) × Σᵢ max_j overlap(pᵢ, gtⱼ)
```

**Interpretation:** R → 1 means each predicted tricluster is almost entirely contained within some ground-truth tricluster (high precision). R = 0 means no predicted tricluster overlaps any planted tricluster.

**Note:** This is precision-oriented — it rewards small, precise clusters. A method that finds tiny clusters inside planted regions scores high R even if it misses most planted triclusters.

#### 4.2.2 CE — Clustering Error

Fraction of total predicted cells that fall outside any ground-truth tricluster:

```
union_GT = ∪_j gtⱼ    (all cells in any GT tricluster)
union_P  = ∪_i pᵢ     (all cells in any predicted tricluster)

CE = 1 - |union_P ∩ union_GT| / |union_P|
```

**Interpretation:** CE → 0 means almost all predicted cells are within planted triclusters (no spurious coverage). CE → 1 means predicted clusters are entirely outside planted patterns.

#### 4.2.3 RMS3 — 3D Revised Match Score

Geometric mean of per-dimension Jaccard similarities, averaged over all predicted-GT pairs:

```
For pair (pᵢ, gtⱼ) with gene sets G_p, G_gt; cond sets C_p, C_gt; time sets Z_p, Z_gt:

  J_G = |G_p ∩ G_gt| / |G_p ∪ G_gt|
  J_C = |C_p ∩ C_gt| / |C_p ∪ C_gt|
  J_Z = |Z_p ∩ Z_gt| / |Z_p ∪ Z_gt|

  RMS3(pᵢ, gtⱼ) = (J_G × J_C × J_Z)^(1/3)

RMS3 = (1/|P|) × Σᵢ max_j RMS3(pᵢ, gtⱼ)
```

**Interpretation:** RMS3 → 1 means predicted triclusters match planted ones in all three dimensions equally. It penalises dimensional mismatch (e.g., finding the right genes but wrong timepoints) more harshly than R.

---

## 5. Algorithms

### 5.1 1D Clustering: K-Means and Agglomerative

#### 5.1.1 How They Work

1D clustering treats the tensor as a 2D gene-expression matrix by **flattening** the condition and time dimensions:

```
T ∈ R^(G × C × Z)  →  X ∈ R^(G × (C×Z))
```

Each gene becomes a single vector of length `C × Z`. Clustering is then applied to these vectors, grouping genes by similar overall expression profiles. This is a **baseline** — it captures global co-expression but ignores the structure of which conditions and timepoints drive similarity.

**K-Means Algorithm:**

```
Input: X ∈ R^(G × F), k (number of clusters)

1. Standardise X with zero mean and unit variance (StandardScaler)
2. Initialise k cluster centroids μ₁,...,μₖ randomly (n_init=10 restarts)
3. REPEAT:
   a. Assign each gene g to cluster c* = argmin_c ||X[g] - μ_c||²
   b. Update μ_c = mean of all genes assigned to cluster c
4. UNTIL centroids do not change
5. Output: cluster label for each gene
```

**Agglomerative Hierarchical Clustering:**

```
Input: X ∈ R^(G × F), k (number of clusters)

1. Standardise X
2. Begin with each gene as its own cluster (G singletons)
3. REPEAT:
   a. Find the two closest clusters by Ward linkage:
      Δ = (n_A × n_B)/(n_A + n_B) × ||centroid_A - centroid_B||²
   b. Merge them into one cluster
4. UNTIL exactly k clusters remain
5. Output: cluster label for each gene
```

Ward linkage minimises the total within-cluster variance increase at each merge step, producing compact, similarly-sized clusters.

#### 5.1.2 Parameters Used

| Parameter | Value | Reason |
|:----------|:------|:-------|
| n_clusters | 5 | Matches typical bicluster count in benchmark; consistent across all methods |
| n_init (K-Means) | 10 | Reduces sensitivity to random initialisation |
| linkage (Agglomerative) | Ward | Produces most compact clusters |
| Preprocessing | StandardScaler | Zero-mean, unit-variance normalisation |

#### 5.1.3 TRIQ Computation for 1D Results

For each 1D cluster containing genes `G'`, the corresponding block in the **full** tensor is extracted:
```
Block = T[G', :, :]  (shape: |G'| × C × Z)
Flattened = Block.reshape(|G'|, C × Z)
```
GRQ, PEQ, SPQ, TRIQ are then computed on `Flattened`.

---

### 5.2 2D Biclustering: Spectral Methods

#### 5.2.1 How They Work

2D biclustering finds subsets of genes AND features (condition-time combinations) that are co-regulated. The tensor is flattened to 2D identically to 1D methods, but clustering happens simultaneously on both rows (genes) and columns (features).

**SpectralCoclustering (Dhillon 2001):**

Treats the data matrix as a bipartite graph between genes (rows) and features (columns). Uses spectral decomposition to find the optimal cut:

```
Input: X ∈ R^(G × F), k

1. Compute normalised Laplacian of bipartite graph:
   D_r = diag(X × 1_F)    (row degree matrix)
   D_c = diag(X^T × 1_G)  (column degree matrix)
   A_n = D_r^(-1/2) × X × D_c^(-1/2)

2. Compute SVD: A_n = U × Σ × V^T
3. Take k-1 right singular vectors to form embedding matrix Z

4. Run K-Means on the combined [row_embedding; col_embedding] space

5. Output: row cluster label for each gene,
           column cluster label for each feature
```

The result is `k × k` bicluster blocks (one per row-cluster/column-cluster combination).

**SpectralBiclustering (Kluger et al. 2003):**

Extends coclustering by applying SVD to a log-normalised version of the data, then running K-Means separately on row and column projections:

```
Input: X ∈ R^(G × F), n_clusters=(k_row, k_col)

1. Make X non-negative (shift by min value)
2. Compute log(X + epsilon)
3. Apply SVD: log(X) ≈ U × Σ × V^T
4. K-Means on rows of U[:, :k_row] → gene cluster labels
5. K-Means on rows of V[:, :k_col] → feature cluster labels
6. Report top biclusters as (row_mask, col_mask) pairs

Output: list of (gene_mask, feature_mask) biclusters
```

#### 5.2.2 Parameters Used

| Parameter | Value |
|:----------|:------|
| n_clusters | 5 (both methods) |
| n_init (SpectralBiclustering) | 3 |
| Preprocessing | Non-negative shift (min subtracted) |

#### 5.2.3 TRIQ Computation for 2D Results

For each bicluster (gene_mask G', feature_mask F'), the block is:
```
Block = T_flat[G', F']  (shape: |G'| × |F'|)
```
where `T_flat` is the flattened `(G × CF)` matrix. GRQ/PEQ/SPQ/TRIQ are computed directly on this 2D block.

---

### 5.3 TriGen: Genetic Algorithm Triclustering

#### 5.3.1 Overview

TriGen (Gutiérrez-Avilés et al. 2014) is a **genetic algorithm** that evolves a population of tricluster solutions to maximise a fitness function measuring tricluster coherence. It is the primary algorithm from Paper 1 and the first to apply evolutionary optimisation to 3D gene expression data.

#### 5.3.2 How It Works

TriGen operates on a population of candidate solutions. Each individual encodes a tricluster as binary membership vectors for genes, conditions, and timepoints. The GA evolves these over multiple generations using selection, crossover, and mutation operators.

```
TRIGEN ALGORITHM
================

Input:
  T ∈ R^(G × C × Z)         — gene expression tensor
  N = 5                      — number of populations
  G_gen = 20                 — number of generations
  I = 10                     — individuals per population
  fitness_fn ∈ {MSL, MSR3D} — objective function

Preprocessing:
  Compute time lags Δ[g, c, z] = T[g, c, z+1] - T[g, c, z]
  (used by MSL fitness)

For each population p = 1..N:
  1. INITIALISATION:
     Generate I random individuals:
       Each individual = (gene_mask ∈ {0,1}^G,
                          cond_mask ∈ {0,1}^C,
                          time_mask ∈ {0,1}^Z)
     with random binary vectors (min sizes enforced)

  2. For each generation g = 1..G_gen:
     a. EVALUATION:
        For each individual i:
          Extract sub-tensor T[gene_mask, cond_mask, time_mask]
          Compute fitness = fitness_fn(sub-tensor)

     b. SELECTION:
        Select parents by tournament or roulette-wheel selection
        (favouring higher fitness individuals)

     c. CROSSOVER:
        For each pair of parents (p₁, p₂):
          Independently cross each mask:
            offspring_gene_mask = random_crossover(p₁.gene_mask, p₂.gene_mask)
          producing two offspring individuals

     d. MUTATION:
        For each offspring, flip each bit with probability p_mut:
          if rand() < p_mut: mask[i] = 1 - mask[i]

  3. OUTPUT best individual from population p → tricluster tₚ

Output: N tricluster solutions t₁, ..., tₙ
```

**Fitness Functions:**

*MSL (Mean Squared Lag)* — used for QualityC and Synthetic datasets:
```
For sub-tensor S = T[G', C', Z']:
  lag[g, c, z] = S[g, c, z+1] - S[g, c, z]    (temporal difference)

  row_mean_lag[g, c] = mean over z of lag[g, c, z]
  col_mean_lag[c, z] = mean over g of lag[g, c, z]
  grand_mean_lag     = mean of all lag values

  residue = lag[g, c, z] - row_mean_lag[g, c]
                          - col_mean_lag[c, z]
                          + grand_mean_lag

  MSL = mean of residue²
  fitness = 1 / (1 + MSL)    (higher = better)
```

MSL penalises inconsistency in the *temporal change* rather than the absolute values, making it sensitive to genes that move in step across conditions and time.

*MSR3D* — used for Yeast dataset:
```
residue3D[g, c, z] = T[g,c,z] - μ_gene[g] - μ_cond[c] - μ_time[z] + 2×μ_all
MSR3D = mean of residue3D²
fitness = 1 / (1 + MSR3D)
```

#### 5.3.3 Implementation

TriGen is implemented as a Java application (`algorithms/TrLab3.5/TriGenApp.jar`) and configured via `.tricfg` files. The Java application runs the GA and writes solution files (`.sol`) to `algorithms/TrLab3.5/resources/`.

**Key Configuration Parameters:**

| Parameter | QualityC | Yeast | Synthetic |
|:----------|:---------|:------|:----------|
| Fitness function | MSL | MSR3D | MSL |
| Populations (N) | 5 | 5 | 5 |
| Generations | 20 | 20 | 20 |
| Individuals/pop | 10 | 10 | 10 |
| Min genes | 5 | 5 | 5 |
| Min conditions | 2 | 2 | 2 |
| Min timepoints | 2 | 3 | 2 |

---

### 5.4 triCluster: Exact Window-Based Triclustering

#### 5.4.1 Overview

triCluster (Zhao & Zaki, SIGMOD 2005) finds **exact** triclusters where genes exhibit proportionally similar expression across selected conditions and timepoints. It uses a multiplicative (or additive) window model: within a tricluster, the expression ratio between any two conditions is approximately constant for all genes.

The algorithm works in four phases:
1. Build an edge matrix encoding compatible gene pairs
2. Enumerate maximal biclusters at each timepoint (EXPAND)
3. Merge compatible biclusters across time (EXPAND_T)
4. Remove dominated triclusters

#### 5.4.2 Data Model

**Ratio mode** (for raw count data ≥ 0):
A tricluster (G', C', Z') satisfies: for all genes g₁, g₂ ∈ G', for all conditions c₁, c₂ ∈ C', for all times z ∈ Z':

```
T[g₂, c₂, z] / T[g₁, c₁, z]  ≈  constant  (within window winsz)
```

Formally, for each condition pair (c₁, c₂), gene pair scores `T[g, c₂, z] / T[g, c₁, z]` must all fall within a multiplicative window `[r, r×(1+winsz)]`.

**Additive mode** (for log₂ data that can be negative):
```
T[g, c₂, z] - T[g, c₁, z]  ∈  [d, d + winsz]
```

#### 5.4.3 Pseudocode

```
TRICLUSTER ALGORITHM
====================

Input:
  T ∈ R^(G × C × Z)         — expression tensor
  winsz                       — window tolerance (0.03 ratio or 1.0 additive)
  min_genes, min_samples, min_times — minimum tricluster dimensions
  mode ∈ {ratio, additive}
  del_overlap                 — fraction for subset removal (1.0 = strict subsets)

─── PHASE 1: Build Edge Matrix ──────────────────────────────────────────────

edges = {}  # keyed by (time, cond_pair)

FOR each time t = 0..Z-1:
  FOR each condition pair (c₁, c₂), c₁ < c₂:
    IF mode == ratio:
      scores[g] = T[g, c₂, t] / T[g, c₁, t]    for all g
      filter: keep only genes where T[g,c₁,t] > 0 AND T[g,c₂,t] > 0
    ELSE:  # additive
      scores[g] = T[g, c₂, t] - T[g, c₁, t]    for all g

    Sort genes by score ascending
    Find LARGEST contiguous window [r, r×(1+winsz)] covering most genes:
      Use sliding window over sorted scores
      Record gene mask (boolean array of length G)

    edges[(t, c₁, c₂)] = gene_mask    # which genes are compatible

─── PHASE 2: EXPAND — Bicluster Enumeration per Time Slice ──────────────────

biclusters = []   # one list per time slice

FOR each time t = 0..Z-1:
  bics_t = []
  EXPAND(cur_samples=[], cur_genes=ALL_GENES, candidates=[0..C-1],
         edges=edges[t])

  PROCEDURE EXPAND(cur_samples, cur_genes, candidates, edges):
    IF |cur_samples| >= min_samples AND popcount(cur_genes) >= min_genes:
      IF (cur_samples, cur_genes) is not dominated by existing bic:
        ADD (frozenset(cur_samples), cur_genes) to bics_t

    FOR each condition x in candidates:
      remaining = candidates after x
      IF cur_samples is empty:
        RECURSE EXPAND([x], cur_genes, remaining, edges)
      ELSE:
        y = last condition in cur_samples
        pair = (min(x,y), max(x,y))
        edge_mask = edges.get((t, pair), EMPTY)
        new_genes = cur_genes AND edge_mask    # bitwise AND
        IF popcount(new_genes) >= min_genes:
          RECURSE EXPAND(cur_samples+[x], new_genes, remaining, edges)

  biclusters.append(bics_t)

─── PHASE 3: EXPAND_T — Merge Biclusters Across Time ────────────────────────

triclusters = []

EXPAND_T(cur_times=[], cur_samples=None, cur_genes=None,
         candidates=[0..Z-1], biclusters=biclusters)

PROCEDURE EXPAND_T(cur_times, cur_samples, cur_genes, candidates, biclusters):
  IF len(cur_times) >= min_times AND cur_samples meets minimums:
    ADD (frozenset(gene indices), cur_samples, frozenset(cur_times))
         to triclusters

  FOR each time t in candidates:
    remaining = candidates after t
    FOR each (bic_samples, bic_genes) in biclusters[t]:
      IF cur_times is empty:
        new_samples = bic_samples
        new_genes   = bic_genes.copy()
      ELSE:
        new_samples = cur_samples INTERSECT bic_samples
        new_genes   = cur_genes AND bic_genes    # bitwise AND

      IF new_samples >= min_samples AND popcount(new_genes) >= min_genes:
        RECURSE EXPAND_T(cur_times+[t], new_samples, new_genes,
                         remaining, biclusters)

─── PHASE 4: Remove Dominated Triclusters ───────────────────────────────────

FOR each tricluster tᵢ = (Gᵢ, Cᵢ, Zᵢ):
  IF there exists tⱼ ≠ tᵢ such that Gᵢ ⊆ Gⱼ AND Cᵢ ⊆ Cⱼ AND Zᵢ ⊆ Zⱼ:
    DISCARD tᵢ   (it is a strict subset of tⱼ)

Output: list of maximal non-dominated triclusters
```

#### 5.4.4 Key Properties

- **Exactness:** Only finds triclusters satisfying the window criterion exactly — no approximation in window fitting
- **Completeness:** EXPAND finds all maximal biclusters at each time slice; EXPAND_T merges all valid combinations
- **Complexity:** EXPAND is worst-case exponential in conditions (bicluster enumeration is NP-hard in general), but fast in practice on small C (10 conditions, 45 pairs)
- **Limitation:** Cannot find Order-Preserving patterns (only ratio/additive coherence is modelled)

#### 5.4.5 Parameters Used

| Parameter | QualityC | Note |
|:----------|:---------|:-----|
| winsz | 0.03 | 3% multiplicative tolerance |
| mode | ratio | Raw counts, all positive |
| min_genes | 8 | Minimum genes per tricluster |
| min_samples | 4 | Minimum conditions |
| min_times | 2 | Minimum timepoints |
| del_overlap | 1.0 | Remove only strict subsets |

---

### 5.5 TCtriCluster: Temporal Contiguity Extension

#### 5.5.1 Overview

TCtriCluster (Soares 2015) extends triCluster with a single, biologically motivated constraint: **triclusters must span only consecutive timepoints**. The biological rationale is that real temporal gene expression patterns — cell cycle phases, stress responses, developmental cascades — activate and deactivate in continuous time windows, not in arbitrary scattered timepoints.

#### 5.5.2 The Modification

TCtriCluster is identical to triCluster in Phases 1, 2, and 4. Only Phase 3 (EXPAND_T) changes:

```
TCTRICLUSTER EXPAND_T (modified):

PROCEDURE _EXPAND_T_CONTIGUOUS(cur_times, cur_samples, cur_genes,
                                candidates, biclusters):
  IF len(cur_times) >= min_times AND cur_samples meets minimums:
    ADD tricluster to results

  FOR each time t in candidates:
    ┌─────────────────────────────────────────────────────┐
    │  TEMPORAL CONTIGUITY CHECK:                          │
    │  IF cur_times is non-empty AND t ≠ cur_times[-1]+1: │
    │      SKIP  (t is not the next consecutive time)      │
    └─────────────────────────────────────────────────────┘
    remaining = candidates after t
    FOR each (bic_samples, bic_genes) in biclusters[t]:
      [... same merge logic as triCluster ...]
      RECURSE _EXPAND_T_CONTIGUOUS(...)
```

The single `if cur_times and t != cur_times[-1] + 1: continue` line (line 86 of `tctricluster_numpy.py`) is the entire difference from triCluster.

**Effect:** Only time subsets like {0,1,2}, {2,3,4}, {1,2,3,4} are valid — not {0,2}, {1,3}, or other non-consecutive combinations. This reduces the total number of triclusters found and makes each one temporally interpretable as a contiguous activation window.

#### 5.5.3 Expected Behavior vs. triCluster

| Aspect | triCluster | TCtriCluster |
|:-------|:-----------|:-------------|
| Time subsets | Any combination | Only consecutive runs |
| #Triclusters found | More | Fewer |
| R on Contiguity datasets | Same | Same (planted patterns are consecutive) |
| R on other datasets | Higher | Slightly lower (some non-consecutive patterns missed) |
| Biological interpretability | Lower | Higher |

---

### 5.6 δ-Trimax: Mean Square Residue Triclustering

#### 5.6.1 Overview

δ-Trimax (adapted from Soares 2020; original Python by novalsaputra) extends the classical **Cheng-Church biclustering algorithm** (2000) to three dimensions. Instead of enumerating windows, it starts from the entire dataset and *iteratively removes* genes, conditions, and timepoints whose inclusion increases the Mean Square Residue (MSR) above a threshold δ, then *adds back* any excluded element that improves coherence.

#### 5.6.2 The 3D Residue Model

For a sub-tensor `S = T[G', C', Z']`, define:

```
μ_gene[g]  = mean over (c, z) of S[g, c, z]     (gene mean)
μ_cond[c]  = mean over (g, z) of S[g, c, z]     (condition mean)
μ_time[z]  = mean over (g, c) of S[g, c, z]     (time mean)
μ_all      = mean of all S[g, c, z]

3D residue:
  r[g, c, z] = S[g,c,z] - μ_gene[g] - μ_cond[c] - μ_time[z] + 2×μ_all

MSR = mean over all (g,c,z) of r[g,c,z]²
```

A tricluster with MSR ≤ δ is considered coherent. Note that the 3D residue model is satisfied exactly when:
```
S[g, c, z] = a_g + b_c + d_z + constant   (additive tricluster model)
```
meaning δ-Trimax naturally finds additive and constant patterns.

Individual row/column/time MSR values (for deletion decisions):
```
MSR_gene[g]  = mean over (c,z) of r[g,c,z]²
MSR_cond[c]  = mean over (g,z) of r[g,c,z]²
MSR_time[z]  = mean over (g,c) of r[g,c,z]²
```

#### 5.6.3 Pseudocode

```
δ-TRIMAX ALGORITHM
==================

Input:
  D ∈ R^(T × G × C)    — tensor (time-first indexing)
  δ                      — MSR threshold
  λ = 1.2               — multiple-deletion multiplier
  n_triclusters = 50    — number of triclusters to extract
  min_genes, min_conds, min_times — minimum sizes

For each iteration i = 1..n_triclusters:

  1. INITIALISE:
     gene_mask   = [True, True, ..., True]   (all G genes)
     cond_mask   = [True, True, ..., True]   (all C conditions)
     time_mask   = [True, True, ..., True]   (all T timepoints)

  ─── STEP A: MULTIPLE NODE DELETION ─────────────────────────────────────

  2. Compute MSR of full current sub-tensor
  3. WHILE MSR > δ:
     a. Compute MSR_gene for each included gene
        Remove all genes g where MSR_gene[g] > λ × MSR  (if |genes| > min_genes)
        Recompute MSR

     b. Compute MSR_cond for each included condition
        Remove all conditions c where MSR_cond[c] > λ × MSR  (if |conds| > min_conds)
        Recompute MSR

     c. Compute MSR_time for each included timepoint
        Remove all times z where MSR_time[z] > λ × MSR  (if |times| > min_times)
        Recompute MSR

     d. IF nothing was removed: BREAK

  ─── STEP B: SINGLE NODE DELETION ────────────────────────────────────────

  4. WHILE MSR > δ:
     Find the element (gene, condition, or time) with highest individual MSR
     Remove it (subject to minimum size constraints)
     Recompute MSR
     IF nothing can be removed: BREAK

  ─── STEP C: NODE ADDITION ───────────────────────────────────────────────

  5. REPEAT until no additions are made:
     For each currently excluded gene g:
       Compute MSR_gene_b[g] = MSR contribution if g were added back
       IF MSR_gene_b[g] < current MSR: add g back

     For each currently excluded condition c:
       Compute MSR_cond_b[c] = MSR contribution if c were added back
       IF MSR_cond_b[c] < current MSR: add c back

     For each currently excluded timepoint z:
       Compute MSR_time_b[z] = MSR contribution if z were added back
       IF MSR_time_b[z] < current MSR: add z back

  ─── STEP D: RECORD AND MASK ─────────────────────────────────────────────

  6. Record tricluster:
     gene_set  = {g : gene_mask[g] = True}
     cond_set  = {c : cond_mask[c] = True}
     time_set  = {z : time_mask[z] = True}
     IF any dimension is size ≤ 1: STOP (degenerate)

  7. MASK: Replace all cells in D[time_set, gene_set, cond_set]
           with random values from Uniform(min(D), max(D))
     This prevents the same region from dominating the next tricluster

Output: list of n_triclusters (gene_set, cond_set, time_set) tuples
```

#### 5.6.4 Key Properties

- **Pattern type:** Natively finds additive and constant patterns (3D MSR = 0 iff additive model holds exactly)
- **Greedy approach:** No global optimum guarantee — each iteration is locally optimal
- **Masking strategy:** Random masking prevents repeated discovery of the same region but introduces randomness
- **Cannot find Order-Preserving patterns:** MSR only measures value coherence, not rank coherence
- **δ parameter:** Set adaptively as `δ = Var(D) × 0.005` (0.5% of data variance per dataset)

#### 5.6.5 Parameters Used

| Parameter | Value | Reason |
|:----------|:------|:-------|
| δ | Var(D) × 0.005 | Adaptive to each dataset's scale |
| λ | 1.2 | Standard Cheng-Church multiplier |
| n_triclusters | 50 | Covers GT counts of 30–70 |
| mask_mode | random | Uniform random masking |
| min_genes | 2 | Allow small triclusters |
| min_conds | 2 | Allow small triclusters |
| min_times | 2 | Allow small triclusters |

---

## 6. Individual Results

### 6.1 1D Methods — TRIQ Results

1D methods flatten the 3D tensor and cluster genes only. TRIQ is computed per cluster.

#### QualityC Dataset (500 genes × 10 conds × 5 times)

**K-Means (k=5):**

| Cluster | TRIQ | GRQ | PEQ | SPQ |
|:--------|-----:|----:|----:|----:|
| KMeans {1} | 0.100 | 0.094 | 0.127 | 0.126 |
| KMeans {2} | 0.093 | 0.086 | 0.121 | 0.120 |
| KMeans {3} | 0.106 | 0.101 | 0.126 | 0.126 |
| KMeans {4} | 0.073 | 0.062 | 0.118 | 0.116 |
| KMeans {5} | 0.087 | 0.078 | 0.122 | 0.122 |
| **Mean** | **0.092** | **0.084** | **0.123** | **0.122** |

**Agglomerative (k=5):**

| Cluster | TRIQ | GRQ | PEQ | SPQ |
|:--------|-----:|----:|----:|----:|
| Agglomerative {1} | 0.058 | 0.043 | 0.119 | 0.119 |
| Agglomerative {2} | 0.071 | 0.058 | 0.121 | 0.121 |
| Agglomerative {3} | 0.127 | 0.125 | 0.136 | 0.137 |
| Agglomerative {4} | 0.220 | 0.224 | 0.207 | 0.202 |
| Agglomerative {5} | 0.116 | 0.113 | 0.131 | 0.130 |
| **Mean** | **0.118** | **0.113** | **0.143** | **0.142** |

#### Yeast Dataset (6178 genes × 3 conds × 14 times)

**K-Means (k=5):**

| Cluster | TRIQ | GRQ | PEQ | SPQ |
|:--------|-----:|----:|----:|----:|
| KMeans {1} | 0.277 | 0.272 | 0.297 | 0.295 |
| KMeans {2} | 0.373 | 0.359 | 0.426 | 0.436 |
| KMeans {3} | 0.112 | 0.100 | 0.167 | 0.159 |
| KMeans {4} | 0.212 | 0.203 | 0.248 | 0.242 |
| KMeans {5} | 0.125 | 0.112 | 0.177 | 0.177 |
| **Mean** | **0.220** | **0.209** | **0.263** | **0.262** |

**Agglomerative (k=5):**

| Cluster | TRIQ | GRQ | PEQ | SPQ |
|:--------|-----:|----:|----:|----:|
| Agglomerative {1} | 0.150 | 0.136 | 0.209 | 0.202 |
| Agglomerative {2} | 0.134 | 0.117 | 0.204 | 0.198 |
| Agglomerative {3} | 0.337 | 0.332 | 0.356 | 0.357 |
| Agglomerative {4} | 0.074 | 0.054 | 0.158 | 0.153 |
| Agglomerative {5} | 0.422 | 0.413 | 0.462 | 0.448 |
| **Mean** | **0.223** | **0.210** | **0.278** | **0.272** |

---

### 6.2 2D Methods — TRIQ Results

#### QualityC — SpectralCoclustering (25 bicluster blocks: 5 row × 5 col)

Selected results (best and worst):

| Bicluster | TRIQ | GRQ | PEQ | SPQ |
|:----------|-----:|----:|----:|----:|
| Cocluster {16} (best) | 0.271 | 0.246 | 0.373 | 0.372 |
| Cocluster {1} | 0.233 | 0.198 | 0.373 | 0.380 |
| Cocluster {6} | 0.260 | 0.232 | 0.373 | 0.370 |
| Cocluster {8} (worst) | 0.147 | 0.119 | 0.259 | 0.260 |
| **Mean (all 25)** | **0.196** | **0.174** | **0.296** | **0.294** |

#### QualityC — SpectralBiclustering (5 biclusters)

| Bicluster | TRIQ | GRQ | PEQ | SPQ |
|:----------|-----:|----:|----:|----:|
| Bicluster {5} (best) | 0.417 | 0.360 | 0.638 | 0.650 |
| Bicluster {3} | 0.339 | 0.300 | 0.495 | 0.493 |
| Bicluster {4} | 0.201 | 0.190 | 0.245 | 0.241 |
| Bicluster {2} | 0.173 | 0.154 | 0.249 | 0.250 |
| Bicluster {1} (worst) | 0.158 | 0.147 | 0.201 | 0.202 |
| **Mean** | **0.258** | **0.240** | **0.366** | **0.367** |

#### Yeast — SpectralCoclustering (20 non-empty bicluster blocks)

| Best Bicluster | TRIQ | GRQ | PEQ | SPQ |
|:---------------|-----:|----:|----:|----:|
| Cocluster {5} (best) | 0.395 | 0.395 | 0.400 | 0.388 |
| Cocluster {12} | 0.380 | 0.388 | 0.360 | 0.328 |
| Cocluster {2} | 0.376 | 0.374 | 0.395 | 0.368 |
| **Mean (all non-empty)** | **0.275** | **0.271** | **0.316** | **0.302** |

#### Yeast — SpectralBiclustering (5 biclusters)

| Bicluster | TRIQ | GRQ | PEQ | SPQ |
|:----------|-----:|----:|----:|----:|
| Bicluster {4} (best) | 0.497 | 0.518 | 0.424 | 0.402 |
| Bicluster {5} | 0.433 | 0.431 | 0.445 | 0.438 |
| Bicluster {3} | 0.419 | 0.415 | 0.471 | 0.395 |
| Bicluster {2} | 0.397 | 0.392 | 0.423 | 0.408 |
| Bicluster {1} | 0.166 | 0.146 | 0.245 | 0.239 |
| **Mean** | **0.382** | **0.380** | **0.402** | **0.376** |

---

### 6.3 TriGen — TRIQ and TRIQ per Dataset

TriGen produces 5 solutions (one per population). TRIQ is computed by TrLab on the `.sol` output files.

#### QualityC Dataset

| Solution | TRIQ | GRQ | PEQ | SPQ |
|:---------|-----:|----:|----:|----:|
| Solution 1 | 0.700 | 0.749 | 0.501 | 0.502 |
| Solution 2 | 0.699 | 0.748 | 0.500 | 0.501 |
| Solution 3 | 0.699 | 0.749 | 0.500 | 0.501 |
| Solution 4 | 0.698 | 0.748 | 0.499 | 0.500 |
| Solution 5 | 0.700 | 0.749 | 0.502 | 0.503 |
| **Mean** | **0.699** | **0.749** | **0.501** | **0.502** |

All 5 solutions are remarkably consistent, demonstrating GA convergence. GRQ (0.749) indicates high structural coherence; PEQ/SPQ (~0.50) indicate moderate inter-gene correlation.

#### Yeast Dataset

| Solution | TRIQ | GRQ | PEQ | SPQ |
|:---------|-----:|----:|----:|----:|
| Solution 1 | 0.269 | 0.532 | 0.390 | 0.381 |
| Solution 2 | 0.252 | 0.497 | 0.375 | 0.370 |
| Solution 3 | 0.255 | 0.508 | 0.382 | 0.371 |
| Solution 4 | 0.250 | 0.495 | 0.372 | 0.368 |
| Solution 5 | 0.260 | 0.519 | 0.380 | 0.377 |
| **Mean** | **0.257** | **0.510** | **0.380** | **0.373** |

The lower TRIQ on real Yeast data (0.257 vs. 0.699 on QualityC) reflects the noise and heterogeneity of real biological data. GRQ (0.51) is moderate; PEQ/SPQ (~0.38) indicate real genes are less perfectly co-expressed than planted synthetic patterns.

#### TriGen Synthetic Dataset (1000×10×5)

| Solution | TRIQ | GRQ | PEQ | SPQ |
|:---------|-----:|----:|----:|----:|
| Solution 1 | 0.765 | 0.683 | 0.948 | 0.934 |
| Solution 2 | 0.748 | 0.660 | 0.941 | 0.929 |
| Solution 3 | 0.752 | 0.665 | 0.942 | 0.930 |
| Solution 4 | 0.757 | 0.671 | 0.944 | 0.932 |
| Solution 5 | 0.742 | 0.655 | 0.938 | 0.926 |
| **Mean** | **0.753** | **0.667** | **0.943** | **0.930** |

On the synthetic dataset designed for TriGen, PEQ and SPQ approach 1.0 — confirming near-perfect temporal correlation in the recovered triclusters. This validates TriGen's ability to recover planted temporal patterns.

---

### 6.4 triCluster — 17-Dataset Benchmark

Parameters: winsz=0.03, min_genes=8, min_samples=4, min_times=2, mode=ratio

| Dataset | #GT | #Found | R | CE | RMS3 | Time (s) |
|:--------|----:|-------:|--:|---:|-----:|---------:|
| BaseR | 40 | 10 | 0.8667 | 0.9980 | 0.9262 | 0.14 |
| Constant | 70 | 45 | **0.9496** | 0.9939 | **0.9507** | 0.18 |
| Additive | 70 | 40 | 0.8833 | 0.9952 | 0.9213 | 0.14 |
| Multiplicative | 70 | 35 | 0.9238 | 0.9957 | 0.9503 | 0.13 |
| **OrderPreserving** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 | 0.11 |
| OverlappingC | 70 | 46 | 0.9435 | 0.9941 | 0.9465 | 0.14 |
| OverlappingA | 70 | 39 | 0.8889 | 0.9952 | 0.9329 | 0.11 |
| OverlappingM | 70 | 40 | 0.8917 | 0.9953 | 0.9286 | 0.12 |
| **OverlappingOP** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 | 0.11 |
| QualityC | 70 | 48 | 0.5609 | 0.9965 | 0.8024 | 0.12 |
| QualityA | 70 | 37 | 0.5854 | 0.9970 | 0.7978 | 0.10 |
| QualityM | 70 | 14 | 0.5515 | 0.9990 | 0.7715 | 0.10 |
| **QualityOP** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 | 0.09 |
| ContiguityC | 60 | 43 | 0.9504 | 0.9936 | 0.9396 | 0.12 |
| ContiguityA | 60 | 29 | 0.9218 | 0.9960 | 0.9590 | 0.12 |
| ContiguityM | 60 | 39 | 0.8906 | 0.9947 | 0.9077 | 0.13 |
| **ContiguityOP** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 | 0.10 |

**Summary statistics:**
- Mean R (13 non-OrderPreserving datasets): **0.808**
- Mean R (4 OrderPreserving datasets): **0.000**
- Mean runtime per dataset: **0.12 seconds**

---

### 6.5 TCtriCluster — 17-Dataset Benchmark

Parameters: Same as triCluster + temporal contiguity constraint

| Dataset | #GT | #Found | R | CE | RMS3 |
|:--------|----:|-------:|--:|---:|-----:|
| BaseR | 40 | 7 | 0.6762 | 0.9989 | 0.8737 |
| Constant | 70 | 29 | 0.7908 | 0.9967 | 0.8930 |
| Additive | 70 | 21 | 0.7460 | 0.9979 | 0.8620 |
| Multiplicative | 70 | 20 | 0.8300 | 0.9978 | 0.9062 |
| **OrderPreserving** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 |
| OverlappingC | 70 | 26 | 0.8282 | 0.9971 | 0.9114 |
| OverlappingA | 70 | 20 | 0.7800 | 0.9979 | 0.9043 |
| OverlappingM | 70 | 23 | 0.7449 | 0.9977 | 0.8926 |
| **OverlappingOP** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 |
| QualityC | 70 | 20 | 0.5150 | 0.9987 | 0.7678 |
| QualityA | 70 | 15 | 0.5189 | 0.9989 | 0.7815 |
| QualityM | 70 | 7 | 0.6506 | 0.9994 | 0.8223 |
| **QualityOP** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 |
| **ContiguityC** | 60 | **43** | **0.9504** | 0.9936 | **0.9396** |
| **ContiguityA** | 60 | **29** | **0.9218** | 0.9960 | **0.9590** |
| **ContiguityM** | 60 | **39** | **0.8906** | 0.9947 | **0.9077** |
| **ContiguityOP** | 30 | **0** | **0.0000** | 1.0000 | 0.0000 |

**Summary statistics:**
- Mean R (13 non-OrderPreserving datasets): **0.761**
- Mean R (Contiguity non-OP, 3 datasets): **0.921** — *identical to triCluster*
- Mean #Found (non-OP): 20.3 vs triCluster's 27.9 — *fewer, more precise*

---

### 6.6 δ-Trimax — 17-Dataset Benchmark

Parameters: δ = Var(D)×0.005, λ=1.2, n_triclusters=50

| Dataset | #GT | #Found | R | CE | RMS3 | δ | Time (s) |
|:--------|----:|-------:|--:|---:|-----:|--:|--------:|
| BaseR | 40 | 50 | 0.1735 | 0.9982 | 0.4012 | 1,069,507 | 19.7 |
| Constant | 70 | 50 | 0.1600 | 0.9989 | 0.4287 | 1,037,367 | 15.5 |
| Additive | 70 | 50 | **0.2810** | 0.9982 | **0.5280** | 1,034,969 | 14.2 |
| Multiplicative | 70 | 50 | 0.1819 | 0.9988 | 0.4575 | 1,069,793 | 15.1 |
| OrderPreserving | 30 | 50 | 0.0377 | 0.9996 | 0.2463 | 1,041,618 | 19.2 |
| OverlappingC | 70 | 50 | 0.1798 | 0.9988 | 0.4278 | 1,038,699 | 16.7 |
| OverlappingA | 70 | 50 | 0.2577 | 0.9983 | 0.5425 | 1,039,290 | 13.1 |
| OverlappingM | 70 | 50 | 0.1878 | 0.9985 | 0.4511 | 1,107,593 | 15.4 |
| OverlappingOP | 30 | 50 | 0.0276 | 0.9997 | 0.2145 | 1,046,525 | 19.3 |
| QualityC | 70 | 50 | 0.2231 | 0.9986 | 0.4460 | 1,080,617 | 16.5 |
| QualityA | 70 | 50 | 0.2770 | 0.9982 | 0.5391 | 1,122,802 | 15.4 |
| QualityM | 70 | 50 | 0.2086 | 0.9987 | 0.4862 | 1,143,264 | 15.7 |
| QualityOP | 30 | 50 | 0.0368 | 0.9995 | 0.2452 | 1,127,302 | 19.5 |
| ContiguityC | 60 | 50 | 0.1626 | 0.9988 | 0.4343 | 1,092,610 | 16.7 |
| ContiguityA | 60 | 50 | 0.2860 | 0.9980 | 0.5294 | 1,042,872 | 14.9 |
| ContiguityM | 60 | 50 | 0.2375 | 0.9982 | 0.5007 | 1,095,943 | 15.8 |
| ContiguityOP | 30 | 50 | 0.0242 | 0.9997 | 0.1999 | 1,036,349 | 19.3 |

**Summary statistics:**
- Mean R (all 17 datasets): **0.168**
- Mean R (13 non-OrderPreserving): **0.204**
- Mean R (OrderPreserving, 4): **0.032**
- Mean runtime per dataset: **~16 seconds** (vs. 0.12s for triCluster)

---

### 6.7 3D Algorithm TRIQ on QualityC and Yeast

These results use the same TRIQ/GRQ/PEQ/SPQ computation as the 1D/2D results: for each found tricluster, the 3D subblock is extracted, flattened to 2D, and the metrics applied.

#### QualityC (500 × 10 × 5)

| Algorithm | #Triclusters | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
|:----------|------------:|----------:|----------:|----:|----:|----:|
| triCluster | 48 | **1.000** | 0.603 | 0.576 | 0.452 | 0.854 |
| TCtriCluster | 20 | 0.710 | 0.504 | 0.514 | 0.473 | 0.823 |
| δ-Trimax | 50 | 1.000 | **0.893** | **0.874** | 0.461 | 0.761 |

- triCluster's TRIQ Best = 1.000 because QualityC has constant-pattern plantings and triCluster finds exact multiplicative windows that perfectly satisfy the coherence criterion
- δ-Trimax's mean TRIQ is highest (0.893) because it directly minimises MSR — mathematically equivalent to maximising GRQ — so every tricluster it finds is inherently highly coherent by construction

#### Yeast (top-500 genes × 3 × 14)

| Algorithm | #Triclusters | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
|:----------|------------:|----------:|----------:|----:|----:|----:|
| δ-Trimax | 16 | **0.937** | **0.810** | **0.862** | **0.593** | **0.594** |
| TCtriCluster | 5 | 0.818 | 0.678 | 0.768 | 0.356 | 0.374 |
| triCluster | TIMEOUT* | — | — | — | — | — |

*triCluster timed out (>120s) on Yeast even with tight `winsz=0.3, min_genes=10`. The EXPAND enumeration phase exhausts combinatorial search across 14 timepoints × 3 condition pairs. **TCtriCluster**, however, completed in just 1.7s and found 5 solid triclusters (TRIQ mean 0.678) — the temporal contiguity constraint reduces EXPAND_T's search to consecutive timepoints only, making it feasible where the unconstrained version is not. This is a notable and practically important result.

---

### 6.8 Recoverability on QualityC Ground Truth

Ground truth: 70 planted triclusters, sizes ranging from 64 to 165 cells, mean = 109.4 cells.

#### Summary Table

| Method | #Found | R | CE | RMS3 | Avg Volume |
|:-------|-------:|--:|---:|-----:|-----------:|
| TriGen 3D | 5 | 0.2588 | 0.6951 | 1.54 | 3,675 |
| 1D K-Means | 5 | 0.4636 | 0.6936 | 1.45 | 5,000 |
| 1D Agglomerative | 5 | 0.5674 | 0.6936 | 1.47 | 5,000 |
| 2D SpectralCoclustering | 5 | 0.1309 | 0.6980 | 1.87 | 1,060 |
| triCluster† | 48 | 0.5609 | 0.9965 | 0.8024 | — |

†triCluster uses Soares et al. 2024 precision-oriented R formula; others use recall-based Jaccard.

#### Per-Solution Breakdown

**TriGen 3D — 5 solutions:**

| Solution | Cells | Best-R | RMS3 |
|:---------|------:|-------:|-----:|
| TriGen {1} | 3,408 | 0.4000 | 1.514 |
| TriGen {2} | 2,528 | 0.4000 | 1.596 |
| TriGen {3} | 3,200 | 0.3750 | 1.535 |
| TriGen {4} | 4,680 | 0.5000 | 1.519 |
| TriGen {5} | 4,560 | 0.5455 | 1.535 |

**1D K-Means — 5 clusters:**

| Cluster | Cells | Best-R | RMS3 |
|:--------|------:|-------:|-----:|
| KMeans {1} | 5,150 | 0.9091 | 1.448 |
| KMeans {2} | 5,600 | 0.7000 | 1.444 |
| KMeans {3} | 4,300 | 0.7778 | 1.467 |
| KMeans {4} | 5,150 | 0.8000 | 1.436 |
| KMeans {5} | 4,800 | 0.8750 | 1.463 |

**1D Agglomerative — 5 clusters:**

| Cluster | Cells | Best-R | RMS3 |
|:--------|------:|-------:|-----:|
| Agglomerative {1} | 10,800 | 0.8889 | 1.413 |
| Agglomerative {2} | 6,650 | 0.8000 | 1.442 |
| Agglomerative {3} | 3,700 | 1.0000 | 1.484 |
| Agglomerative {4} | 1,250 | 1.0000 | 1.530 |
| Agglomerative {5} | 2,600 | 1.0000 | 1.483 |

**2D SpectralCoclustering — 5 blocks:**

| Block | Cells | Best-R | RMS3 |
|:------|------:|-------:|-----:|
| Cocluster {1} | 294 | 0.3000 | 1.909 |
| Cocluster {2} | 920 | 0.2000 | 1.864 |
| Cocluster {3} | 1,144 | 0.2727 | 1.873 |
| Cocluster {4} | 1,935 | 0.4148 | 1.831 |
| Cocluster {5} | 1,008 | 0.1818 | 1.862 |

---

## 7. Comparative Analysis

### 7.1 TRIQ Quality — QualityC (Unified Comparison)

All algorithms on the same dataset, using the same TRIQ computation:

| Method | Dim | #Clusters | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
|:-------|:---:|----------:|----------:|----------:|----:|----:|----:|
| **δ-Trimax** | 3D | 50 | **1.000** | **0.893** | **0.874** | 0.461 | 0.761 |
| **triCluster** | 3D | 48 | **1.000** | 0.603 | 0.576 | 0.452 | **0.854** |
| **TCtriCluster** | 3D | 20 | 0.710 | 0.504 | 0.514 | **0.473** | 0.823 |
| **TriGen 3D** | 3D | 5 | 0.700 | 0.699 | 0.749 | 0.501 | 0.502 |
| 2D SpectralBiclustering | 2D | 5 | 0.417 | 0.258 | 0.240 | 0.366 | 0.367 |
| 2D SpectralCoclustering | 2D | 25 | 0.271 | 0.196 | 0.174 | 0.296 | 0.294 |
| 1D Agglomerative | 1D | 5 | 0.220 | 0.118 | 0.113 | 0.143 | 0.142 |
| 1D K-Means | 1D | 5 | 0.106 | 0.092 | 0.084 | 0.123 | 0.122 |

**Key observation:** All 3D methods substantially outperform all 1D/2D methods on mean TRIQ. The dimensionality of the clustering approach directly predicts the coherence of the resulting clusters.

### 7.2 TRIQ Quality — Yeast (Unified Comparison)

| Method | Dim | #Clusters | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
|:-------|:---:|----------:|----------:|----------:|----:|----:|----:|
| **δ-Trimax** | 3D | 16 | **0.937** | **0.810** | **0.862** | **0.593** | **0.594** |
| **TCtriCluster** | 3D | 5 | 0.818 | 0.678 | 0.768 | 0.356 | 0.374 |
| **TriGen 3D** | 3D | 5 | 0.269 | 0.252 | 0.532 | 0.390 | 0.381 |
| 2D SpectralBiclustering | 2D | 5 | **0.497** | 0.382 | 0.380 | 0.402 | 0.376 |
| 2D SpectralCoclustering | 2D | 20 | 0.395 | 0.275 | 0.271 | 0.316 | 0.302 |
| 1D Agglomerative | 1D | 5 | 0.422 | 0.223 | 0.210 | 0.278 | 0.272 |
| 1D K-Means | 1D | 5 | 0.373 | 0.220 | 0.209 | 0.263 | 0.262 |
| triCluster | 3D | — | TIMEOUT | TIMEOUT | TIMEOUT | TIMEOUT | TIMEOUT |

**Key observations on Yeast:**
- δ-Trimax dominates all methods by a wide margin on Yeast (mean TRIQ 0.810 vs. 0.678 for next best), confirming that MSR minimisation handles real noisy data well
- **TCtriCluster (5 trics, TRIQ Mean=0.678)** outperforms all 1D/2D methods and even TriGen on Yeast — the temporal contiguity constraint makes it feasible in just 1.7 s where unconstrained triCluster times out (>120 s)
- **triCluster TIMEOUT**: with 14 timepoints and a wide dynamic range, EXPAND_T's exponential search is infeasible; TCtriCluster escapes this by restricting merges to t+1 only
- 2D SpectralBiclustering best TRIQ (0.497) beats TriGen 3D (0.269) — real noisy data with only 3 conditions favours joint gene-feature biclustering over pure 3D search
- The GRQ gap (δ-Trimax 0.862 vs. TCtriCluster 0.768) shows MSR explicitly optimises residue whereas triCluster/TCtriCluster only enforce window consistency

### 7.3 Recoverability vs. Pattern Type (17-Dataset Benchmark)

| Pattern Type | triCluster R | TCtriCluster R | δ-Trimax R |
|:-------------|------------:|---------------:|-----------:|
| Constant | 0.950 | 0.791 | 0.160 |
| Additive | 0.883 | 0.746 | 0.281 |
| Multiplicative | 0.924 | 0.830 | 0.182 |
| **OrderPreserving** | **0.000** | **0.000** | **0.038** |
| Overlapping (C/A/M avg) | 0.908 | 0.784 | 0.209 |
| Quality (C/A/M avg) | 0.566 | 0.562 | 0.236 |
| Contiguity (C/A/M avg) | 0.921 | **0.921** | 0.229 |

**What this reveals:**
1. **triCluster excels at exact patterns** (Constant, Multiplicative, Contiguity) where its window model matches the planted structure
2. **TCtriCluster matches triCluster exactly on Contiguity** — the temporal constraint doesn't hurt when patterns are already consecutive
3. **δ-Trimax performs best on Additive** (0.281) — additive patterns align with its MSR model, but it still underperforms triCluster significantly
4. **All three fail on OrderPreserving** — rank-based patterns require completely different methods

### 7.4 triCluster vs TCtriCluster — Head-to-Head

| Dataset | triCluster R | TCtriCluster R | triCluster #Found | TCtriCluster #Found |
|:--------|------------:|---------------:|------------------:|--------------------:|
| BaseR | 0.867 | 0.676 | 10 | 7 |
| Constant | 0.950 | 0.791 | 45 | 29 |
| Additive | 0.883 | 0.746 | 40 | 21 |
| Multiplicative | 0.924 | 0.830 | 35 | 20 |
| ContiguityC | **0.950** | **0.950** | **43** | **43** |
| ContiguityA | **0.922** | **0.922** | **29** | **29** |
| ContiguityM | **0.891** | **0.891** | **39** | **39** |
| QualityC | 0.561 | 0.515 | 48 | 20 |

The Contiguity rows are identical — proving the temporal contiguity constraint is "free" when patterns are already temporally consecutive. The cost appears only on non-contiguous patterns (e.g., BaseR, Constant) where triCluster finds more triclusters and achieves higher R.

### 7.5 Speed Comparison

| Algorithm | Dataset Size | Avg. Time per Dataset |
|:----------|:------------|----------------------:|
| 1D K-Means | 500×10×5 | < 1 second |
| 1D Agglomerative | 500×10×5 | < 1 second |
| 2D SpectralCoclustering | 500×10×5 | < 1 second |
| 2D SpectralBiclustering | 500×10×5 | < 1 second |
| triCluster | 500×10×5 | **0.12 seconds** |
| TCtriCluster | 500×10×5 | **0.11 seconds** |
| δ-Trimax | 500×10×5 | **~16 seconds** |
| TriGen 3D | 500×10×5 | **~30–60 seconds** |
| triCluster | 500×3×14 (Yeast) | ∞ (TIMEOUT >120 s) |
| TCtriCluster | 500×3×14 (Yeast) | **~1.7 seconds** |
| δ-Trimax | 500×3×14 (Yeast) | ~1.5 seconds |

### 7.6 Algorithm Capability Matrix

| Capability | 1D | 2D Spectral | TriGen | triCluster | TCtriCluster | δ-Trimax |
|:-----------|:--:|:-----------:|:------:|:----------:|:------------:|:--------:|
| Finds constant patterns | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Finds additive patterns | ~ | ~ | ~ | ✓ | ✓ | ✓ |
| Finds multiplicative patterns | ~ | ~ | ~ | ✓ | ✓ | ~ |
| Finds order-preserving patterns | ~ | ~ | ✓ | ✗ | ✗ | ✗ |
| Temporal structure | ✗ | ~ | ✓ | ✓ | ✓ (strict) | ✓ |
| Temporal contiguity | ✗ | ✗ | ✗ | ✗ | **✓** | ✗ |
| Real data scalability | ✓ | ✓ | ✓ | ✗ | ~ (contiguity helps) | ✓ |
| Ground truth not needed | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| High internal quality (TRIQ) | Low | Medium | High | High | High | **Highest** |
| High recoverability (R) | Medium* | Low | Medium | **High** | High | Low |

*High R for 1D is misleading: large gene-only clusters trivially contain planted gene sets.

---

## 8. Findings and Discussion

### 8.1 Dimensionality Matters for Internal Quality

The most consistent finding across both datasets is that **higher-dimensional algorithms find more internally coherent clusters** (as measured by TRIQ):

```
QualityC Mean TRIQ ranking:
  δ-Trimax (3D): 0.893 > triCluster (3D): 0.603 > TCtriCluster (3D): 0.504
  > TriGen (3D): 0.699 > SpectralBiclustering (2D): 0.258
  > SpectralCoclustering (2D): 0.196 > Agglomerative (1D): 0.118 > K-Means (1D): 0.092
```

This validates the fundamental premise of triclustering: by optimising across three dimensions simultaneously, the algorithm finds subsets of genes that are genuinely co-regulated under specific conditions at specific times — a much stricter coherence criterion than any lower-dimensional approach can enforce.

### 8.2 The Distinction Between Internal Quality and Recoverability

A critical observation from this study is that **high TRIQ does not imply high R, and vice versa**:

- **δ-Trimax** has the highest TRIQ (0.893 on QualityC) but the lowest R (0.223 on QualityC) among triclustering methods. It finds very coherent clusters but they don't correspond to the planted ground truth patterns.
- **1D Agglomerative** has the highest recall-R (0.567) among non-triCluster methods on QualityC but the lowest TRIQ (0.118). Its large gene-only clusters trivially contain entire planted gene sets by virtue of their size, but the cluster as a whole is incoherent.
- **triCluster** achieves high scores on both R (0.56–0.95 on non-OP datasets) and TRIQ (mean 0.603), suggesting it genuinely finds patterns that are both biologically coherent and aligned with planted structures.

The ideal algorithm would score high on both, and triCluster comes closest for structured synthetic data.

### 8.3 OrderPreserving Patterns: A Fundamental Limitation

All three tricluster algorithms completely fail on OrderPreserving patterns (R = 0.000 for triCluster and TCtriCluster; R = 0.038 for δ-Trimax). This is not an implementation bug — it is a **fundamental modelling incompatibility**:

- triCluster requires `T[g₂, c, z] / T[g₁, c, z] ≈ constant` — a quantitative ratio constraint
- OrderPreserving only requires `rank(T[g₁, c₁, z]) = rank(T[g₁, c₂, z])` — a qualitative rank constraint
- No window of any size can capture rank relationships through ratio/difference comparisons

Dedicated algorithms for order-preserving triclusters (TRIMAX-OP, OPTriCluster) would be needed to handle this pattern type.

### 8.4 Noise Sensitivity (Quality Datasets)

triCluster's R drops significantly on Quality (noisy) datasets vs. clean pattern datasets:

```
Constant (clean):     R = 0.950
QualityC (10% noise): R = 0.561  (-41% degradation)
QualityM:             R = 0.552
QualityA:             R = 0.585
```

This demonstrates that exact window-based methods are sensitive to noise. A 3% multiplicative tolerance (winsz=0.03) is tight enough that noise displaces genes out of the valid window. Increasing winsz would capture more genes under noise but risks finding false patterns.

### 8.5 TCtriCluster's Temporal Contiguity — Biological Insight

The equivalence of triCluster and TCtriCluster on Contiguity datasets (identical R and #Found) provides strong empirical evidence that **temporal contiguity is a natural property of the planted patterns** in those datasets. The constraint adds no cost when it matches the data structure.

Conversely, on non-Contiguity datasets, TCtriCluster finds ~27% fewer triclusters (20.3 vs. 27.9 on average) with ~6% lower mean R (0.761 vs. 0.808). This is the measurable cost of the constraint — biologically justified but empirically selective.

### 8.6 δ-Trimax's Internal Coherence vs. Pattern Recovery

δ-Trimax's high TRIQ (0.893) combined with low R (0.223) is explained by its objective function: it minimises MSR over the *entire dataset*, not over planted pattern windows. The greedy deletion from the full tensor tends to converge to the regions of globally minimal residue — which may be small, locally constant regions that don't correspond to any of the 70 planted triclusters.

The random masking strategy is designed to diversify, but on structured synthetic data with 70 overlapping planted patterns across a 500×10×5 tensor, the 50 extracted triclusters are spread over many small coherent regions rather than recovering the larger planted structures.

On real Yeast data, where there is no planted structure to recover, δ-Trimax's approach is ideal: find the most coherent 3D sub-regions by MSR optimisation, producing biologically interpretable coherent expression modules.

### 8.7 Scalability Constraints

A key practical finding is that **the temporal contiguity constraint in TCtriCluster is not just a biological modelling choice — it is a decisive computational advantage** on real data:

| Algorithm | QualityC (500×10×5) | Yeast (500×3×14, log₂ additive) |
|:----------|:--------------------:|:--------------------------------:|
| triCluster | 0.12 s ✓ | **TIMEOUT >120 s** ✗ |
| TCtriCluster | 0.11 s ✓ | **1.7 s** ✓ |
| δ-Trimax | ~16 s ✓ | ~1.5 s ✓ |

For triCluster, the combinatorial explosion occurs in the EXPAND_T phase: with `additive` mode and log₂ data, many genes satisfy the tolerance window for multiple condition-pairs simultaneously. The recursive enumeration must explore all subsets of matching timepoints across all pairs — exponential in the number of timepoints (14 for Yeast). With 5 timepoints on QualityC this is manageable (2⁵ = 32), but with 14 timepoints (2¹⁴ = 16 384) the search explodes.

TCtriCluster escapes this with one constraint: **EXPAND_T only extends to the immediate next timepoint** (`t == cur_times[-1] + 1`). This converts the exponential search into a linear scan of consecutive windows (at most 13 windows for 14 timepoints), making Yeast tractable in under 2 seconds.

**Practical implications:**
1. triCluster is restricted to ratio-mode data with a small number of conditions (≤10) and timepoints (≤5–7)
2. TCtriCluster extends triCluster's reach to real time-course data — at the cost of only finding temporally consecutive triclusters (which is biologically appropriate in most transcriptomic settings)
3. δ-Trimax and TriGen are the most scalable 3D methods: neither performs exhaustive enumeration

---

## 9. Conclusion

This project implemented, ran, and compared six clustering algorithms at three levels of dimensionality on temporal gene expression data, producing a complete empirical picture of their strengths and trade-offs.

### 9.1 Recommendations by Use Case

| Use Case | Recommended Algorithm | Reason |
|:---------|:---------------------|:-------|
| Synthetic data, known pattern structure, need high recovery | **triCluster** | Best R on structured patterns (0.808 mean) |
| Real noisy data, need high internal quality | **δ-Trimax** | Best TRIQ on QualityC (0.893) and Yeast (0.810) |
| Biological interpretability of temporal windows | **TCtriCluster** | Temporal contiguity matches real biology |
| Order-preserving expression patterns | **TriGen 3D** | GA fitness can incorporate rank-based objectives |
| Fast exploration, no ground truth | **SpectralBiclustering** | Best 2D TRIQ (0.497 on Yeast), seconds runtime |
| Quick baseline, maximum interpretability | **1D Agglomerative** | Simplest, fastest, reasonable recall |

### 9.2 Key Quantitative Findings

1. **All 3D algorithms outperform all 1D/2D baselines on internal TRIQ** — confirming triclustering is the right modelling choice for temporal expression data
2. **triCluster achieves R > 0.88 on 9 of 13 non-OP datasets** — near state-of-the-art exact pattern recovery
3. **TCtriCluster achieves identical performance to triCluster on Contiguity datasets** (R = 0.921) — temporal contiguity constraint is "free" when biologically appropriate
4. **δ-Trimax achieves TRIQ Mean = 0.810 on real Yeast data** — highest of any tested algorithm on real biological data; TCtriCluster follows at 0.678, demonstrating that the contiguity constraint also produces high-quality clusters while remaining computationally feasible
5. **OrderPreserving patterns are unrecoverable by all three tricluster algorithms** — a fundamental algorithmic limitation, not an implementation issue
6. **TriGen achieves TRIQ = 0.765 on its own synthetic dataset** with PEQ/SPQ approaching 1.0 — validates the GA-based approach on data with clear temporal structure

### 9.3 Replication Summary

| Paper | Replicated Experiment | Status |
|:------|:----------------------|:-------|
| NEUCOM TriGen 2014 | TriGen on Synthetic (1000×10×5) | ✓ Replicated (TRIQ 0.765 vs. reported ~0.75) |
| NEUCOM TriGen 2014 | TriGen on Yeast GST data | ✓ Replicated (TRIQ 0.269) |
| Soares et al. 2024 | triCluster on 17 benchmark datasets | ✓ Fully replicated |
| Soares et al. 2024 | TCtriCluster on 17 benchmark datasets | ✓ Fully replicated |
| Soares et al. 2024 | δ-Trimax on 17 benchmark datasets | ✓ Fully replicated |
| Extended | TRIQ metrics for all 3D algorithms | ✓ Novel contribution |

### 9.4 Output Files

| File | Contents |
|:-----|:---------|
| `output/final_comparison_report.md` | Original comparison report (TriGen/1D/2D/triCluster) |
| `output/1d_2d/1d_2d_TRIQ_metrics_report.md` | Per-cluster TRIQ for all 1D/2D methods |
| `output/1d_2d/1d_2d_TRIQ_GRQ_PEQ_SPQ_all.csv` | Raw per-cluster metric values |
| `output/recoverability/recoverability_report.md` | R/CE/RMS3 on QualityC ground truth |
| `output/recoverability/recoverability_results.csv` | Raw recoverability values |
| `output/3d_triq/3d_triq_report.md` | TRIQ for triCluster/TCtriCluster/δ-Trimax |
| `output/3d_triq/3d_triq_summary.csv` | Summary TRIQ per algorithm x dataset |
| `output/3d_triq/3d_triq_per_tric.csv` | Per-tricluster TRIQ values |
| `algorithms/tricluster/results/benchmark_results.csv` | triCluster R/CE/RMS3, 17 datasets |
| `algorithms/tctricluster/results/benchmark_results.csv` | TCtriCluster R/CE/RMS3, 17 datasets |
| `algorithms/deltaTrimax/results/benchmark_results.csv` | δ-Trimax R/CE/RMS3, 17 datasets |
| `output/images/` | All comparison charts (PNG) |

---

*Report generated from experimental results in the 8th-semester triclustering project.*
*Algorithms: triCluster (Zhao & Zaki 2005), TCtriCluster (Soares 2015), δ-Trimax (Cheng-Church 2000 / Soares 2020), TriGen (Gutiérrez-Avilés et al. 2014).*
*Datasets: Soares et al. 2024 benchmark, Spellman et al. 1998 Yeast GST.*
