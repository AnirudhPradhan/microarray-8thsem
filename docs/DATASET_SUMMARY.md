# Dataset Summary: Replicated from Papers

This document summarizes the datasets replicated from both reference papers.

---

## NEUCOM TriGen (2014)

| Dataset | Status | Location | Generate |
|---------|--------|----------|----------|
| **Synthetic** | Replicated | `neucom-trigen\data\tricgen_synthetic_tensor.npz` | `python synthetic_generator.py` |
| **Yeast Cell Cycle** | Replicated | `neucom-trigen\data\yeast_gst_tensor.npz` | `python data_loader.py` |
| **Inflammation** | Documented | — | See paper; requires Glue Grant data |

### Synthetic (Section 4.1)
- 1000 genes × 10 conditions × 5 time points
- Per-condition ranges: [1,15], [7,35], [60,75], [0,25), [30,100], [71,135], [160,375], [5,30], [25,40], [10,30]
- **Area a:** 20×5×3, constant=1
- **Area b:** 30×4×4, ascending then descending pattern

### Yeast (Section 4.2)
- Spellman et al. (GEO: GSE22, GSE23, GSE24)
- 3 conditions (pheromone, cdc15, elutriation), 14 time points, ~7.7k genes

---

## Basepaper (2024)

| Collection | Status | Location |
|------------|--------|----------|
| **Baseline** | Replicated | `basepaper\datasets\Background\`, `BaseR\` |
| **Pattern-specific** | Replicated | `Constant\`, `Additive\`, `Multiplicative\`, `OrderPreserving\` |
| **Overlapping** | Replicated | `OverlappingC\`, `OverlappingA\`, `OverlappingM\`, `OverlappingOP\` |
| **Quality** | Replicated | `QualityC\`, `QualityA\`, `QualityM\`, `QualityOP\` |
| **Contiguity** | Replicated | `ContiguityC\`, `ContiguityA\`, `ContiguityM\`, `ContiguityOP\` |

**Source:** Cloned from https://github.com/dfmsoares/triclustering-algorithms-assessment

### Baseline (1st experiment)
- 500 × 10 × 5, real-valued, [0, 50000]
- 40 triclusters (constant, additive, multiplicative, order-preserving)

---

## Quick Run

**TriGen:**
```bash
cd neucom-trigen
python synthetic_generator.py
python data_loader.py
```

**Basepaper:** Datasets are pre-copied in `basepaper\datasets\`.
