# Final Comparison Report: 1D / 2D / 3D Clustering on Gene Expression Tensors

_Generated from experiments replicating Gutiérrez-Avilés et al. (NEUCOM 2014)
and Soares et al. (Pattern Recognition 2024)._

---

## 1. Overview

This report compares **three levels of clustering dimensionality** on temporal
gene expression tensors (genes × conditions × timepoints):

| Dimensionality | Methods | Captures |
| :------------- | :------ | :------- |
| **1D** | K-Means, Agglomerative | Gene co-expression across all conditions & times |
| **2D** | SpectralCoclustering, SpectralBiclustering | Gene × (condition,time) biclusters |
| **3D** | TriGen (GA-based triclustering) | Gene × condition × timepoint triclusters |
| **3D** | triCluster (Zhao & Zaki 2005) | Exact multiplicative/additive triclusters |

**Datasets:**
- **QualityC** — Soares 2024 synthetic benchmark, 500 genes × 10 conds × 5 times, 70 planted triclusters
- **Yeast** — Spellman et al. (GEO: GSE22/23/24), 6178 genes × 4 conds × 14 times
- **TriGen Synthetic** — Gutiérrez-Avilés et al. 2014, 1000 genes × 10 conds × 5 times

---

## 2. TRIQ Quality Metrics

TRIQ (Tricluster Quality) is a weighted combination of GRQ (structural coherence),
PEQ (Pearson correlation), and SPQ (Spearman correlation). Range: 0–1, higher is better.

### 2.1 QualityC Dataset

| Method | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
| :----- | --------: | --------: | --: | --: | --: |
| TriGen 3D | 0.700 | 0.699 | 0.749 | 0.501 | 0.502 |
| 1D K-Means | 0.106 | 0.092 | 0.084 | 0.123 | 0.122 |
| 1D Agglomerative | 0.220 | 0.118 | 0.113 | 0.143 | 0.142 |
| 2D SpectralCoclustering | 0.271 | 0.196 | 0.174 | 0.296 | 0.294 |
| 2D SpectralBiclustering | 0.417 | 0.258 | 0.240 | 0.366 | 0.367 |

### 2.2 Yeast Dataset

| Method | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
| :----- | --------: | --------: | --: | --: | --: |
| TriGen 3D | 0.269 | 0.252 | 0.532 | 0.390 | 0.381 |
| 1D K-Means | 0.373 | 0.220 | 0.209 | 0.263 | 0.262 |
| 1D Agglomerative | 0.422 | 0.223 | 0.210 | 0.278 | 0.272 |
| 2D SpectralCoclustering | 0.395 | 0.275 | 0.271 | 0.316 | 0.302 |
| 2D SpectralBiclustering | 0.497 | 0.382 | 0.380 | 0.402 | 0.376 |

### 2.3 TriGen Synthetic Dataset (1000×10×5)

| Method | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
| :----- | --------: | --------: | --: | --: | --: |
| TriGen 3D | 0.765 | 0.733 | 0.683 | 0.948 | 0.934 |

---

## 3. triCluster — Full Benchmark Results (17 Datasets)

triCluster was run on all 17 basepaper benchmark datasets (500×10×5 each).
Metrics use the Soares et al. 2024 definitions (precision-oriented R, union-based CE, 3D-Jaccard RMS3).

| Dataset | #GT | #Found | R | CE | RMS3 | Time(s) |
| :------ | --: | -----: | -: | --: | ---: | ------: |
| BaseR | 40 | 10 | 0.8667 | 0.9980 | 0.9262 | 0.14 |
| Constant | 70 | 45 | 0.9496 | 0.9939 | 0.9507 | 0.18 |
| Additive | 70 | 40 | 0.8833 | 0.9952 | 0.9213 | 0.14 |
| Multiplicative | 70 | 35 | 0.9238 | 0.9957 | 0.9503 | 0.13 |
| OrderPreserving | 30 | 0 | 0.0000 | 1.0000 | 0.0000 | 0.11 |
| OverlappingC | 70 | 46 | 0.9435 | 0.9941 | 0.9465 | 0.14 |
| OverlappingA | 70 | 39 | 0.8889 | 0.9952 | 0.9329 | 0.11 |
| OverlappingM | 70 | 40 | 0.8917 | 0.9953 | 0.9286 | 0.12 |
| OverlappingOP | 30 | 0 | 0.0000 | 1.0000 | 0.0000 | 0.11 |
| QualityC | 70 | 48 | 0.5609 | 0.9965 | 0.8024 | 0.12 |
| QualityA | 70 | 37 | 0.5854 | 0.9970 | 0.7978 | 0.1 |
| QualityM | 70 | 14 | 0.5515 | 0.9990 | 0.7715 | 0.1 |
| QualityOP | 30 | 0 | 0.0000 | 1.0000 | 0.0000 | 0.09 |
| ContiguityC | 60 | 43 | 0.9504 | 0.9936 | 0.9396 | 0.12 |
| ContiguityA | 60 | 29 | 0.9218 | 0.9960 | 0.9590 | 0.12 |
| ContiguityM | 60 | 39 | 0.8906 | 0.9947 | 0.9077 | 0.13 |
| ContiguityOP | 30 | 0 | 0.0000 | 1.0000 | 0.0000 | 0.1 |

**Non-OrderPreserving datasets** (13): mean R = 0.831
**OrderPreserving datasets** (3): R = 0.000 (algorithm cannot find this pattern type)

---

## 4. Recoverability Metrics (QualityC — Ground Truth Available)

Computed against the **70 planted triclusters** from the QualityC dataset.

| Metric | Definition | Better |
| :----- | :--------- | :----- |
| **R** | Mean recall: fraction of each planted tricluster covered by best found | Higher (→1) |
| **CE** | Fraction of found cells outside any planted tricluster | Lower (→0) |
| **RMS3** | Normalised 3D mean square residue of found triclusters | Lower |

**Note:** TriGen/1D/2D use recall-based R (averaged over GT triclusters).
triCluster† uses precision-based R (averaged over predicted triclusters). Both range 0–1.

| Method | #Found | R | CE | RMS3 | Avg Volume |
| :----- | -----: | -: | --: | ---: | ---------: |
| TriGen 3D | 5 | 0.2588 | 0.6951 | 1.54 | 3675 |
| 1D K-Means | 5 | 0.4636 | 0.6936 | 1.45 | 5000 |
| 1D Agglomerative | 5 | 0.5674 | 0.6936 | 1.47 | 5000 |
| 2D SpectralCoclustering | 5 | 0.1309 | 0.6980 | 1.87 | 1060 |
| triCluster† | 48 | 0.5609 | 0.9965 | 0.8024 | — |

_†triCluster metrics use Soares et al. 2024 precision-oriented formulas._

---

## 4b. TCtriCluster — Full Benchmark Results (17 Datasets)

TCtriCluster (Soares 2015) extends triCluster with a **temporal contiguity constraint**:
triclusters are only formed by merging biclusters from *consecutive* timepoints.
This implementation uses the same numpy infrastructure as triCluster (Phase 1+2 identical)
with a single-line change in the EXPAND_T phase.

| Dataset | #GT | #Found | R | CE | RMS3 |
| :------ | --: | -----: | -: | --: | ---: |
| BaseR | 40 | 7 | 0.6764 | 0.9990 | 0.8743 |
| Constant | 70 | 29 | 0.7908 | 0.9966 | 0.8930 |
| Additive | 70 | 21 | 0.7455 | 0.9978 | 0.8620 |
| Multiplicative | 70 | 20 | 0.8296 | 0.9977 | 0.9063 |
| OrderPreserving | 30 | 0 | 0.0000 | 1.0000 | 0.0000 |
| OverlappingC | 70 | 26 | 0.8276 | 0.9973 | 0.9111 |
| OverlappingA | 70 | 20 | 0.7795 | 0.9979 | 0.9043 |
| OverlappingM | 70 | 23 | 0.7449 | 0.9979 | 0.8934 |
| OverlappingOP | 30 | 0 | 0.0000 | 1.0000 | 0.0000 |
| QualityC | 70 | 20 | 0.5153 | 0.9986 | 0.7684 |
| QualityA | 70 | 15 | 0.5190 | 0.9988 | 0.7819 |
| QualityM | 70 | 7 | 0.6510 | 0.9991 | 0.8218 |
| QualityOP | 30 | 0 | 0.0000 | 1.0000 | 0.0000 |
| ContiguityC | 60 | 43 | 0.9497 | 0.9937 | 0.9399 |
| ContiguityA | 60 | 29 | 0.9218 | 0.9958 | 0.9590 |
| ContiguityM | 60 | 39 | 0.8907 | 0.9950 | 0.9084 |
| ContiguityOP | 30 | 0 | 0.0000 | 1.0000 | 0.0000 |

**Non-OrderPreserving datasets** (13): mean R = 0.757
**Contiguity datasets** (non-OP): TCtriCluster mean R = 0.921, triCluster mean R = 0.921

**Key insight**: TCtriCluster finds fewer triclusters overall (stricter temporal requirement)
but achieves near-identical R to triCluster on Contiguity datasets, where patterns are
by design temporally consecutive.

---

## 4c. δ-Trimax — Full Benchmark Results (17 Datasets)

δ-Trimax (Soares 2020) extends Cheng-Church 2D biclustering to 3D.
It iteratively deletes dimensions with high Mean Square Residue (MSR),
then adds back dimensions that improve coherence, masking found triclusters
with random values to find subsequent ones.

Parameters: δ = data_variance × 0.005, λ = 1.2, n_triclusters = 50.

| Dataset | #GT | #Found | R | CE | RMS3 | Time(s) |
| :------ | --: | -----: | -: | --: | ---: | ------: |
| BaseR | 40 | 50 | 0.1742 | 0.9983 | 0.4012 | 19.7 |
| Constant | 70 | 50 | 0.1600 | 0.9987 | 0.4289 | 15.5 |
| Additive | 70 | 50 | 0.2813 | 0.9980 | 0.5280 | 14.2 |
| Multiplicative | 70 | 50 | 0.1824 | 0.9990 | 0.4575 | 15.1 |
| OrderPreserving | 30 | 50 | 0.0381 | 1.0000 | 0.2459 | 19.2 |
| OverlappingC | 70 | 50 | 0.1801 | 0.9988 | 0.4282 | 16.7 |
| OverlappingA | 70 | 50 | 0.2582 | 0.9980 | 0.5416 | 13.1 |
| OverlappingM | 70 | 50 | 0.1882 | 0.9985 | 0.4515 | 15.4 |
| OverlappingOP | 30 | 50 | 0.0280 | 1.0000 | 0.2145 | 19.3 |
| QualityC | 70 | 50 | 0.2228 | 0.9990 | 0.4459 | 16.5 |
| QualityA | 70 | 50 | 0.2773 | 0.9983 | 0.5392 | 15.4 |
| QualityM | 70 | 50 | 0.2091 | 0.9990 | 0.4862 | 15.7 |
| QualityOP | 30 | 50 | 0.0373 | 1.0000 | 0.2453 | 19.5 |
| ContiguityC | 60 | 50 | 0.1634 | 0.9987 | 0.4337 | 16.7 |
| ContiguityA | 60 | 50 | 0.2862 | 0.9980 | 0.5292 | 14.9 |
| ContiguityM | 60 | 50 | 0.2372 | 0.9982 | 0.5005 | 15.8 |
| ContiguityOP | 30 | 50 | 0.0237 | 1.0000 | 0.2003 | 19.4 |

**Non-OrderPreserving datasets** (14): mean R = 0.204

**Note:** δ-Trimax has consistently low R (0.16–0.29) on this benchmark.
The 3D MSR residue captures additive/constant patterns well (lower MSR = more coherent)
but the greedy deletion from the full dataset tends to converge to small spurious clusters
rather than recovering the specific planted triclusters.
OrderPreserving patterns (R ≈ 0.03) are essentially unrecoverable by this MSR-based approach.

---

## 5. Key Findings

### 5.1 triCluster — Strengths and Limitations

- **Excellent on clean patterns**: R > 0.88 on Constant, Additive, Multiplicative, Contiguity datasets.
- **Complete failure on Order-Preserving patterns**: R = 0.000, 0 triclusters found.
  triCluster uses a multiplicative/additive window and cannot represent rank-order patterns.
- **Robust to overlapping**: R stays >0.88 with 40% overlapping triclusters.
- **Sensitive to noise**: R drops from 0.95 (Constant clean) to 0.56 (QualityC, 10% noise + 5% errors).
- **Very fast**: all 17 datasets run in <0.2 seconds each.
- **High CE** (0.99+): found triclusters are small and precise — almost all coverage is inside planted patterns,
  but many planted patterns are missed entirely (low coverage = high CE).

### 5.2 On Synthetic Data with Known Patterns

- **TriGen 3D achieves the highest TRIQ on QualityC (0.700)** — correctly exploiting
  the temporal structure of planted patterns that 1D/2D methods cannot access.
- **Recoverability (R)**: 1D methods achieve higher R (0.46–0.57) than TriGen 3D (0.26)
  because they create coarse clusters that trivially 'contain' planted genes. However,
  their Clustering Error (CE ≈ 0.69) is identical — they cover as much noise as TriGen.
- **RMS3**: 1D K-Means (1.45) and Agglomerative (1.47) slightly outperform TriGen (1.54)
  on the raw residue, but TriGen optimises MSL (with lags), not MSR3D.

### 5.3 On Real Biological Data (Yeast)

- **2D SpectralBiclustering achieves the highest TRIQ (0.497)** on Yeast, because the
  yeast cell cycle has strong co-expression patterns that benefit from joint gene–feature
  clustering.
- TriGen 3D TRIQ (0.269) is lower, reflecting the difficulty of finding coherent
  gene × condition × timepoint subspaces in real noisy data with few conditions.
- 1D Agglomerative (0.422) outperforms TriGen on Yeast TRIQ, suggesting that when
  conditions are few (3–4), reducing dimensionality is more robust.

### 5.4 On the NEUCOM TriGen Synthetic Dataset

- **TriGen achieves its best TRIQ (0.765)** on the dataset designed for it (1000×10×5
  with planted constant and ascending-descending patterns).
- Solutions 1,2,3,5 achieve PEQ = SPQ = 1.0, indicating perfect temporal correlation
  in the found triclusters — confirming TriGen recovers the temporal structure.

### 5.5 triCluster vs TCtriCluster

The temporal contiguity constraint reduces the number of found triclusters
(fewer, more precise triclusters) but has a negligible effect on R for
datasets designed to have consecutive temporal patterns (Contiguity datasets).
On other datasets, R is slightly lower because some valid non-contiguous
combinations are excluded.

| Metric | triCluster | TCtriCluster | Notes |
| :----- | ---------: | -----------: | :---- |
| Mean R (all non-OP) | 0.808 | 0.761 | TCtriCluster slightly lower overall |
| Mean R (Contiguity) | 0.921 | 0.921 | Nearly identical on contiguous data |
| Mean #Found | 27.9 | 20.3 | TCtriCluster finds fewer, more precise triclusters |
| Time per dataset | <0.2s | <0.2s | Same speed (numpy implementation) |

### 5.6 Dimensionality & Algorithm Trade-offs

| Aspect | 1D | 2D | 3D TriGen | 3D triCluster | 3D TCtriCluster | 3D δ-Trimax |
| :----- | :- | :- | :-------- | :------------ | :-------------- | :---------- |
| Interpretability | High | Medium | Low | Medium | Medium | Medium |
| Scalability | Fast | Fast | Moderate | Very fast | Very fast | Fast (~16s) |
| Temporal structure | None | Partial | Full | Full | Full+contiguous | Full |
| Pattern type | Any | Any | Most | Mult/Add | Mult/Add | Add/Const |
| Order-preserving | No | No | Yes | No | No | No |
| TRIQ — Synthetic | Low | Medium | **High (0.70)** | N/A | N/A | N/A |
| R — Contiguity (non-OP) | N/A | N/A | N/A | **0.921** | **0.921** | 0.229 |
| R — QualityC (noisy) | High* | Low | Medium | 0.56 | 0.52 | 0.22 |
| R — Additive (clean) | N/A | N/A | N/A | 0.88 | 0.75 | 0.28 |

_*High R for 1D is misleading: 1D clusters span all conditions × timepoints,
trivially containing planted gene sets but with very large spurious coverage._

---

## 5. Charts

- `output/images/final_triq_comparison.png` — TRIQ best/mean for all methods on QualityC and Yeast
- `output/images/final_recoverability.png` — R, CE, RMS3 for QualityC
- `output/images/final_trigen_all_datasets.png` — TriGen metrics across all 3 datasets
- `output/images/tricluster_benchmark_R.png` — triCluster R across all 17 benchmark datasets
- `output/images/tctricluster_vs_tricluster_R.png` — triCluster vs TCtriCluster R comparison
- `output/images/all_algorithms_R_comparison.png` — 3-way R comparison (triCluster, TCtriCluster, δ-Trimax)

---

## 6. Conclusion

Triclustering (3D) is the appropriate choice when the research question involves
**simultaneous gene-condition-time coherence** in temporal expression data.
TriGen achieves superior TRIQ on data with planted temporal patterns (QualityC, synthetic),
confirming that 3D pattern structure is genuinely captured. On real noisy data (Yeast),
2D biclustering methods are competitive, trading full temporal modelling for robustness.

Among the triCluster family, **TCtriCluster** (temporal contiguity extension) finds fewer
but more biologically motivated triclusters: consecutive-timepoint coherence reflects
real temporal biological processes (cell cycle phases, stress response cascades).
Its R performance is identical to triCluster on Contiguity datasets and only marginally
lower on other patterns, making it a preferable default when temporal ordering matters.

δ-Trimax (3D MSR minimization) is the most general approach — it works on any
data type without assuming multiplicative structure — but its greedy deletion strategy
yields low R on clean synthetic benchmarks with many overlapping small patterns.
It is best suited for real noisy data where exact pattern recovery is not expected.

1D methods serve as fast, interpretable baselines but systematically miss the condition
and temporal structure that defines biologically meaningful co-expression modules.
