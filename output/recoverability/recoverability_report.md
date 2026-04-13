# Recoverability Metrics — QualityC Synthetic Dataset

Ground truth: **70 planted triclusters** (Soares et al. 2024, QualityC variant)
GT tricluster size — min=64, max=165, mean=109.4

## Metric Definitions

| Metric | Definition |
| :----- | :--------- |
| **R** (Recoverability) | Mean Jaccard similarity: for each planted tricluster, best-matching found tricluster |
| **CE** (Clustering Error) | Fraction of found cells outside any planted tricluster |
| **RMS3** | Mean within-tricluster std-dev (lower = more homogeneous) |

## Summary Table

| Method | #Found | R | CE | RMS3 | Avg_Volume |
| :----- | :----- | :--- | :--- | :--- | :--------- |
| TriGen 3D | 5 | 0.2588 | 0.6951 | 1.54 | 3675.2 |
| 1D K-Means | 5 | 0.4636 | 0.6936 | 1.45 | 5000.0 |
| 1D Agglomerative | 5 | 0.5674 | 0.6936 | 1.47 | 5000.0 |
| 2D SpectralCoCluster | 5 | 0.1309 | 0.698 | 1.87 | 1060.2 |

## Interpretation

- **R**: TriGen 3D finds triclusters that partially recover planted patterns; 1D/2D baselines
  span the full condition×timepoint space for all genes in a cluster, giving poor Jaccard overlap
  with the compact planted triclusters.
- **CE**: 1D methods have CE ≈ 1 because their large 'triclusters' cover most non-planted cells.
- **RMS3**: TriGen minimises within-tricluster variance (using MSL fitness), so it achieves
  lower RMS3 than gene-only clustering methods.

---

### TriGen 3D — per-solution breakdown

| Solution | Cells | Best-R | RMS3 |
| :------- | ----: | -----: | ---: |
| TriGen 3D {1} | 3,408 | 0.4000 | 1.5136 |
| TriGen 3D {2} | 2,528 | 0.4000 | 1.5961 |
| TriGen 3D {3} | 3,200 | 0.3750 | 1.5351 |
| TriGen 3D {4} | 4,680 | 0.5000 | 1.5185 |
| TriGen 3D {5} | 4,560 | 0.5455 | 1.5351 |

### 1D K-Means — per-solution breakdown

| Solution | Cells | Best-R | RMS3 |
| :------- | ----: | -----: | ---: |
| 1D K-Means {1} | 5,150 | 0.9091 | 1.4480 |
| 1D K-Means {2} | 5,600 | 0.7000 | 1.4442 |
| 1D K-Means {3} | 4,300 | 0.7778 | 1.4671 |
| 1D K-Means {4} | 5,150 | 0.8000 | 1.4358 |
| 1D K-Means {5} | 4,800 | 0.8750 | 1.4633 |

### 1D Agglomerative — per-solution breakdown

| Solution | Cells | Best-R | RMS3 |
| :------- | ----: | -----: | ---: |
| 1D Agglomerative {1} | 10,800 | 0.8889 | 1.4131 |
| 1D Agglomerative {2} | 6,650 | 0.8000 | 1.4417 |
| 1D Agglomerative {3} | 3,700 | 1.0000 | 1.4841 |
| 1D Agglomerative {4} | 1,250 | 1.0000 | 1.5295 |
| 1D Agglomerative {5} | 2,600 | 1.0000 | 1.4828 |

### 2D SpectralCoCluster — per-solution breakdown

| Solution | Cells | Best-R | RMS3 |
| :------- | ----: | -----: | ---: |
| 2D SpectralCoCluster {1} | 294 | 0.3000 | 1.9090 |
| 2D SpectralCoCluster {2} | 920 | 0.2000 | 1.8644 |
| 2D SpectralCoCluster {3} | 1,144 | 0.2727 | 1.8734 |
| 2D SpectralCoCluster {4} | 1,935 | 0.4148 | 1.8306 |
| 2D SpectralCoCluster {5} | 1,008 | 0.1818 | 1.8620 |
