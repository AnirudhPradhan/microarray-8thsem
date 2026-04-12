# TriGen Results – All Four Metrics (TRIQ, GRQ, PEQ, SPQ)

## 1. Synthetic QualityC (Base Paper Dataset)

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| TRI {1}  | 0.700 | 0.750 | 0.501 | 0.501 |
| TRI {2}  | 0.699 | 0.749 | 0.501 | 0.501 |
| TRI {3}  | 0.695 | 0.744 | 0.502 | 0.502 |
| TRI {4}  | 0.699 | 0.748 | 0.501 | 0.502 |
| TRI {5}  | 0.700 | 0.749 | 0.502 | 0.504 |

**Best solution:** TRI {1}, TRIQ = 0.700  
**Mean TRIQ:** 0.699, **Std dev:** 0.002

---

## 2. Yeast (TriGen Paper Dataset)

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| TRI {1}  | 0.269 | 0.541 | 0.531 | 0.518 |
| TRI {2}  | 0.254 | 0.536 | 0.397 | 0.387 |
| TRI {3}  | 0.244 | 0.527 | 0.332 | 0.321 |
| TRI {4}  | 0.250 | 0.538 | 0.352 | 0.347 |
| TRI {5}  | 0.242 | 0.520 | 0.340 | 0.332 |

**Best solution:** TRI {1}, TRIQ = 0.269  
**Mean TRIQ:** 0.252, **Std dev:** 0.011

---

## Metric definitions

| Metric | Meaning |
|--------|---------|
| **TRIQ** | Overall tricluster quality (weighted combination of GRQ, PEQ, SPQ, BIOQ) |
| **GRQ** | Gene Residual Quality – structural coherence (MSL-based homogeneity) |
| **PEQ** | Pearson Enrichment Quality – linear correlation across slices |
| **SPQ** | Spearman Enrichment Quality – monotonic correlation |

---

**Source files:**
- `TrLab3.5/resources/synthetic_qualityc_run/synthetic_qualityc_run/synthetic_qualityc_run_triq.csv`
- `TrLab3.5/resources/yeast_trigenpaper_run/yeast_trigenpaper_run/yeast_trigenpaper_run_triq.csv`
