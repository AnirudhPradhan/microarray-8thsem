# Next Steps — Beating Current Results

---

## Metrics We Use (and Why)

We have 7 metrics total. Here is what each one means in plain terms, and whether we actually need it.

---

### The Two Primary Metrics

#### TRIQ — Tricluster Quality Index
**What it measures:** How tight and internally consistent the groups you found are — without any answer key.

For each found tricluster, extract the sub-block of expression values (genes × conditions × times) and ask: do these genes move together in a coherent way? TRIQ is a score from 0 to 1. Above 0.7 is considered highly coherent.

**Formula:** `TRIQ = (0.4 × GRQ + 0.05 × PEQ + 0.05 × SPQ) / 0.5`

**Analogy:** Grading a football team's coordination — are they playing together as a unit? No one is checking if they won the game, just whether their passes are crisp.

**When to use:** Always — applies to real data (Yeast) and synthetic data alike. No ground truth needed.

---

#### R — Recoverability
**What it measures:** Out of all the groups your algorithm found, how many actually match the secretly planted groups?

For each predicted tricluster, find the best-matching planted tricluster and compute what fraction of the predicted cells are inside it. Average this across all predictions.

**Formula:** `R = mean over predictions of [ |predicted ∩ best_planted| / |predicted| ]`

**Analogy:** A teacher marking your answers against the answer key. R = 1.0 means every answer was correct. R = 0.0 means nothing matched.

**When to use:** Only on synthetic datasets where we know the ground truth (QualityC and the 17-dataset benchmark). Cannot be used on Yeast.

---

### The Three TRIQ Components (supporting only — already inside TRIQ)

#### GRQ — Gene Residue Quality
**What it measures:** Whether genes follow a consistent additive or multiplicative pattern across all conditions and times in the group. Based on the Cheng-Church residue (how far each value deviates from what a perfectly additive model would predict).

**Formula:** `GRQ = max(0, 1 − MSR / Var(X))` where MSR is the mean squared residue.

**Why we don't report it separately:** It already contributes 80% of TRIQ's weight. If TRIQ is high, GRQ is high. It's useful for debugging (if TRIQ is low, check GRQ first) but not as a standalone comparison metric.

---

#### PEQ — Pearson Expression Quality
**What it measures:** Whether pairs of genes have the same shaped expression curve. Uses Pearson correlation — detects straight-line (linear) relationships.

**Formula:** Mean of all pairwise absolute Pearson correlations between gene profiles.

**Why we don't report it separately:** Contributes only 10% to TRIQ. Captured by TRIQ. Use it only to understand *why* TRIQ is low.

---

#### SPQ — Spearman Expression Quality
**What it measures:** Same as PEQ but uses rank correlation — so it detects the relationship even if one gene's values are 10× bigger than another's.

**Why we don't report it separately:** Also 10% of TRIQ. Rarely disagrees with PEQ. Captured by TRIQ.

---

### The Two Recoverability Companions (supporting only — move with R)

#### RMS3 — 3D Revised Match Score
**What it measures:** A stricter version of R. Instead of just asking "is your predicted group inside a planted group?", it asks "do your predicted genes, conditions, AND timepoints all separately match the planted group?" Computes a Jaccard similarity in each dimension and takes the geometric mean.

**Why we don't chase it separately:** In practice R and RMS3 move together almost perfectly across all our experiments. triCluster's window method naturally finds the right conditions and times when it finds the right genes, so RMS3 rarely disagrees with R. We report it as a supporting number but don't optimise for it.

| Algorithm | R (Constant) | RMS3 (Constant) |
|:----------|:------------:|:---------------:|
| triCluster | 0.950 | 0.951 |
| TCtriCluster | 0.791 | 0.893 |
| δ-Trimax | 0.160 | 0.429 |

---

#### CE — Clustering Error
**What it measures:** What fraction of the cells you predicted fall completely outside any planted group. CE = 0 means everything you predicted is inside planted groups. CE = 1 means everything is noise.

**Why we ignore it:** Look at our results — every algorithm sits at ~0.999 regardless of quality:

| Algorithm | CE |
|:----------|:--:|
| triCluster | 0.9965 |
| TCtriCluster | 0.9987 |
| δ-Trimax | 0.9986 |

This happens because planted triclusters cover only ~5–10% of the total cells in a 500×10×5 dataset. Any algorithm finding small, precise clusters will automatically have CE ≈ 1.0 — it is dominated by dataset geometry, not algorithm quality. **CE is uninformative for our comparisons.**

---

### Summary Table

| Metric | Role | Use it? | Why |
|:-------|:----:|:-------:|:----|
| **TRIQ** | Primary | **Yes** | Single number for internal quality; works on all data |
| **R** | Primary | **Yes** | Single number for recoverability; the main benchmark target |
| GRQ | Supporting | Debugging only | Already 80% of TRIQ |
| PEQ | Supporting | Debugging only | Already inside TRIQ |
| SPQ | Supporting | Debugging only | Already inside TRIQ |
| RMS3 | Supporting | Report alongside R | Moves with R; useful secondary confirmation |
| **CE** | Ignore | **No** | Stuck at ~0.999 for all algorithms; uninformative |

**Bottom line: the entire comparison reduces to two numbers — TRIQ and R. Everything else is either baked in or redundant or useless at this dataset scale.**

---

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
2. ~~**Option 1 (Adaptive winsz)**~~ — attempted, see findings below
3. **Option 3 (OrderPreserving)** — most novel contribution, currently zero coverage across 4 datasets
4. **Option 4 (Hybrid)** — most complex, do after 2 is validated

---

## Gap 2 — Adaptive Winsz: Experiment Results

**Status: Attempted — net negative, revising expectations**

### What was implemented
`algorithms/tricluster/adaptive_winsz.py` — computes per-dataset window size from
the tightest multiplicative window covering 10% of genes per condition pair.

`algorithms/tricluster/run_benchmark_adaptive.py` — full benchmark runner using
adaptive winsz instead of fixed 0.03.

### Results

| Group | Baseline R | Adaptive R | Delta |
|:------|:----------:|:----------:|:-----:|
| Quality datasets (3) | 0.566 | 0.574 | +0.008 |
| Clean structured (10) | 0.911 | 0.875 | -0.036 |
| All non-OP (13) | 0.831 | 0.806 | -0.026 |

### Why it failed
The adaptive formula cannot distinguish "noisy planted cluster (needs wider window)"
from "diverse background patterns (needs narrow window)". Both produce wide ratio
spread. BaseR and Contiguity datasets receive winsz~0.15, adding false positives
that hurt the precision-oriented R metric.

### Why Quality R cannot reach 0.75
Quality datasets have two types of damage:
- **Value noise** (10%): genes displaced in ratio space — fixable with wider window
- **Structural errors** (5%): cells randomly removed from planted clusters — NOT fixable

Structural errors set a hard ceiling on achievable R at ~0.62–0.65 regardless of
window size. The original expectation of 0.56 → 0.75 was too optimistic.

### Revised Gap 2 assessment
- Maximum achievable improvement on Quality datasets: ~+3–4% R
- Any approach that widens the window globally degrades clean datasets by ~4–9%
- Net effect is negative — not worth pursuing further without a smarter noise detector

**Next: proceed to Gap 3 (MSR post-refinement)**
