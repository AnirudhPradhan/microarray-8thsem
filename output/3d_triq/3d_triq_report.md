# TRIQ / GRQ / PEQ / SPQ for 3D Triclustering Algorithms

Each tricluster's 3D subblock is extracted and flattened to
(genes x conds*times) before computing metrics — identical method
to the 1D/2D evaluations in `compute_1d_2d_triq_metrics.py`.

## QualityC  —  500 genes x 10 conditions x 5 timepoints (synthetic, planted triclusters)

| Method | #Triclusters | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
| :----- | -----------: | --------: | --------: | --: | --: | --: |
| triCluster | 48 | 1.000 | 0.603 | 0.576 | 0.452 | 0.854 |
| TCtriCluster | 20 | 0.710 | 0.504 | 0.514 | 0.473 | 0.823 |
| delta-Trimax | 50 | 1.000 | 0.893 | 0.874 | 0.461 | 0.761 |
| TriGen 3D | — | 0.700 | 0.699 | 0.749 | 0.501 | 0.502 |
| 2D SpectralBiclustering | — | 0.417 | 0.258 | 0.240 | 0.366 | 0.367 |
| 2D SpectralCoclustering | — | 0.271 | 0.196 | 0.174 | 0.296 | 0.294 |
| 1D Agglomerative | — | 0.220 | 0.118 | 0.113 | 0.143 | 0.142 |
| 1D K-Means | — | 0.106 | 0.092 | 0.084 | 0.123 | 0.122 |

## Yeast  —  top-500 genes x 3 conditions x 14 timepoints (real Spellman data, log2)

| Method | #Triclusters | TRIQ Best | TRIQ Mean | GRQ | PEQ | SPQ |
| :----- | -----------: | --------: | --------: | --: | --: | --: |
| TCtriCluster | 5 | 0.818 | 0.678 | 0.768 | 0.356 | 0.374 |
| delta-Trimax | 16 | 0.937 | 0.810 | 0.862 | 0.593 | 0.594 |
| TriGen 3D | — | 0.269 | 0.252 | 0.532 | 0.390 | 0.381 |
| 2D SpectralBiclustering | — | 0.497 | 0.382 | 0.380 | 0.402 | 0.376 |
| 2D SpectralCoclustering | — | 0.395 | 0.275 | 0.271 | 0.316 | 0.302 |
| 1D Agglomerative | — | 0.422 | 0.223 | 0.210 | 0.278 | 0.272 |
| 1D K-Means | — | 0.373 | 0.220 | 0.209 | 0.263 | 0.262 |
