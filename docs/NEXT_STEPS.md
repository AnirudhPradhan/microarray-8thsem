# Next Steps — Beating Current Results

## Current Scoreboard

### TRIQ (Internal Quality)

| Algorithm | QualityC TRIQ | Yeast TRIQ |
|:----------|:-------------:|:----------:|
| δ-Trimax | **0.893** | **0.810** |
| TriGen | 0.699 | 0.257 |
| triCluster | 0.603 | TIMEOUT |
| TCtriCluster | 0.504 | 0.678 |
| SpectralBiclustering | 0.258 | 0.382 |

### Recoverability R (17-dataset benchmark)

| Algorithm | Mean R (non-OP, 13 datasets) | Quality datasets avg | OrderPreserving |
|:----------|:---:|:---:|:---:|
| triCluster | **0.808** | 0.566 | **0.000** |
| TCtriCluster | 0.761 | 0.562 | **0.000** |
| δ-Trimax | 0.168 | 0.236 | 0.038 |

---

## The Three Gaps to Attack

### Gap 1 — OrderPreserving: R = 0.000 (all algorithms)
Every algorithm scores zero on 4 of 17 datasets (OrderPreserving, OverlappingOP, QualityOP, ContiguityOP).
A rank-based triclustering method would immediately add ~0.4+ to mean R across all 17 datasets.

### Gap 2 — Quality (noisy) datasets: triCluster drops 0.95 → 0.56
triCluster uses a fixed hard window `winsz=0.03`. At 10% noise, genes get displaced outside the window.
An adaptive or fuzzy window that scales with local variance per dataset could recover this without breaking clean-pattern performance.

### Gap 3 — TRIQ and R are inversely correlated
δ-Trimax wins TRIQ (0.893) but has terrible R (0.168). triCluster wins R (0.808) but mediocre TRIQ (0.603).
No algorithm wins both. A post-processing MSR refinement on triCluster results could push TRIQ toward 0.8+ without losing R.

---

## Proposed Improvements

| # | Idea | Target metric | Expected gain | Effort |
|:--|:-----|:-------------|:-------------|:-------|
| 1 | **Adaptive-winsz triCluster** — scale `winsz` by per-dataset variance before running | Quality R | 0.56 → ~0.75 | Low |
| 2 | **MSR post-refinement** — after triCluster finds triclusters, remove genes whose residue exceeds threshold | TRIQ on QualityC | 0.603 → ~0.80 | Low |
| 3 | **OrderPreserving triclustering** — rank-based expansion (check if gene rankings are consistent across conditions/times) | OP R | 0.000 → ~0.5+ | Medium |
| 4 | **Hybrid triCluster + δ-Trimax** — triCluster seeds regions, δ-Trimax refines them | Both TRIQ and R | Best of both | Medium |

---

## Priority Order

1. **Option 2 (MSR post-refinement)** — quickest win, directly fixes the TRIQ vs R gap
2. **Option 1 (Adaptive winsz)** — straightforward parameter tuning, big R gain on noisy datasets
3. **Option 3 (OrderPreserving)** — most novel contribution, currently zero coverage across 4 datasets
4. **Option 4 (Hybrid)** — most complex, do after 1+2 are validated
