# 1D and 2D Analysis Output – Both Datasets

## Datasets
1. **QualityC** (Base paper synthetic) – 500 genes × 50 features
2. **Yeast** (TriGen paper) – 6178 genes × 56 features

---

## QualityC (Base paper)

### 1D Clustering
| Algorithm | Cluster sizes |
|-----------|---------------|
| **K-Means** | [np.int64(103), np.int64(112), np.int64(86), np.int64(103), np.int64(96)] |
| **AgglomerativeClustering** | [np.int64(216), np.int64(133), np.int64(74), np.int64(25), np.int64(52)] |

### 2D Biclustering
| Algorithm | Row cluster sizes | Col cluster sizes |
|-----------|-------------------|-------------------|
| **SpectralCoclustering** | [np.int64(49), np.int64(92), np.int64(104), np.int64(129), np.int64(126)] | [np.int64(6), np.int64(10), np.int64(11), np.int64(15), np.int64(8)] |

| **SpectralBiclustering** | Bicluster | Rows | Cols | Volume |
|--------------------------|-----------|------|------|--------|
| | 1 | 98 | 18 | 1764 |
| | 2 | 98 | 12 | 1176 |
| | 3 | 98 | 4 | 392 |
| | 4 | 98 | 13 | 1274 |
| | 5 | 98 | 3 | 294 |
| | 6 | 104 | 18 | 1872 |
| | 7 | 104 | 12 | 1248 |
| | 8 | 104 | 4 | 416 |
| | 9 | 104 | 13 | 1352 |
| | 10 | 104 | 3 | 312 |
| | 11 | 59 | 18 | 1062 |
| | 12 | 59 | 12 | 708 |
| | 13 | 59 | 4 | 236 |
| | 14 | 59 | 13 | 767 |
| | 15 | 59 | 3 | 177 |
| | 16 | 127 | 18 | 2286 |
| | 17 | 127 | 12 | 1524 |
| | 18 | 127 | 4 | 508 |
| | 19 | 127 | 13 | 1651 |
| | 20 | 127 | 3 | 381 |
| | 21 | 112 | 18 | 2016 |
| | 22 | 112 | 12 | 1344 |
| | 23 | 112 | 4 | 448 |
| | 24 | 112 | 13 | 1456 |
| | 25 | 112 | 3 | 336 |

---

## Yeast (TriGen paper)

### 1D Clustering
| Algorithm | Cluster sizes |
|-----------|---------------|
| **K-Means** | [np.int64(1007), np.int64(278), np.int64(1887), np.int64(1092), np.int64(1914)] |
| **AgglomerativeClustering** | [np.int64(1499), np.int64(1908), np.int64(353), np.int64(2336), np.int64(82)] |

### 2D Biclustering
| Algorithm | Row cluster sizes | Col cluster sizes |
|-----------|-------------------|-------------------|
| **SpectralCoclustering** | [np.int64(245), np.int64(1868), np.int64(589), np.int64(1916), np.int64(1560)] | [np.int64(0), np.int64(16), np.int64(10), np.int64(22), np.int64(8)] |

| **SpectralBiclustering** | Bicluster | Rows | Cols | Volume |
|--------------------------|-----------|------|------|--------|
| | 1 | 455 | 25 | 11375 |
| | 2 | 455 | 7 | 3185 |
| | 3 | 455 | 11 | 5005 |
| | 4 | 455 | 8 | 3640 |
| | 5 | 455 | 5 | 2275 |
| | 6 | 220 | 25 | 5500 |
| | 7 | 220 | 7 | 1540 |
| | 8 | 220 | 11 | 2420 |
| | 9 | 220 | 8 | 1760 |
| | 10 | 220 | 5 | 1100 |
| | 11 | 950 | 25 | 23750 |
| | 12 | 950 | 7 | 6650 |
| | 13 | 950 | 11 | 10450 |
| | 14 | 950 | 8 | 7600 |
| | 15 | 950 | 5 | 4750 |
| | 16 | 2032 | 25 | 50800 |
| | 17 | 2032 | 7 | 14224 |
| | 18 | 2032 | 11 | 22352 |
| | 19 | 2032 | 8 | 16256 |
| | 20 | 2032 | 5 | 10160 |
| | 21 | 2521 | 25 | 63025 |
| | 22 | 2521 | 7 | 17647 |
| | 23 | 2521 | 11 | 27731 |
| | 24 | 2521 | 8 | 20168 |
| | 25 | 2521 | 5 | 12605 |

---
