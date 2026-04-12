# TRIQ, GRQ, PEQ, SPQ for 1D and 2D Clustering

Adapted from 3D triclustering metrics. Each cluster/bicluster block is evaluated.


## 1D K-Means – QualityC

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| KMeans {1}  | 0.100 | 0.094 | 0.127 | 0.126 |
| KMeans {2}  | 0.093 | 0.086 | 0.121 | 0.120 |
| KMeans {3}  | 0.106 | 0.101 | 0.126 | 0.126 |
| KMeans {4}  | 0.073 | 0.062 | 0.118 | 0.116 |
| KMeans {5}  | 0.087 | 0.078 | 0.122 | 0.122 |

## 1D Agglomerative – QualityC

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| Agglomerative {1}  | 0.058 | 0.043 | 0.119 | 0.119 |
| Agglomerative {2}  | 0.071 | 0.058 | 0.121 | 0.121 |
| Agglomerative {3}  | 0.127 | 0.125 | 0.136 | 0.137 |
| Agglomerative {4}  | 0.220 | 0.224 | 0.207 | 0.202 |
| Agglomerative {5}  | 0.116 | 0.113 | 0.131 | 0.130 |

## 2D SpectralCoclustering – QualityC

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| Cocluster {1}  | 0.233 | 0.198 | 0.373 | 0.380 |
| Cocluster {2}  | 0.196 | 0.174 | 0.279 | 0.287 |
| Cocluster {3}  | 0.170 | 0.146 | 0.269 | 0.265 |
| Cocluster {4}  | 0.176 | 0.165 | 0.222 | 0.222 |
| Cocluster {5}  | 0.219 | 0.198 | 0.306 | 0.302 |
| Cocluster {6}  | 0.260 | 0.232 | 0.373 | 0.370 |
| Cocluster {7}  | 0.175 | 0.149 | 0.275 | 0.276 |
| Cocluster {8}  | 0.147 | 0.119 | 0.259 | 0.260 |
| Cocluster {9}  | 0.164 | 0.149 | 0.223 | 0.223 |
| Cocluster {10}  | 0.220 | 0.197 | 0.317 | 0.313 |
| Cocluster {11}  | 0.210 | 0.170 | 0.369 | 0.369 |
| Cocluster {12}  | 0.193 | 0.173 | 0.274 | 0.276 |
| Cocluster {13}  | 0.134 | 0.102 | 0.261 | 0.262 |
| Cocluster {14}  | 0.141 | 0.122 | 0.219 | 0.215 |
| Cocluster {15}  | 0.206 | 0.179 | 0.321 | 0.314 |
| Cocluster {16}  | 0.271 | 0.246 | 0.373 | 0.372 |
| Cocluster {17}  | 0.159 | 0.131 | 0.272 | 0.273 |
| Cocluster {18}  | 0.194 | 0.177 | 0.264 | 0.261 |
| Cocluster {19}  | 0.127 | 0.105 | 0.216 | 0.215 |
| Cocluster {20}  | 0.187 | 0.156 | 0.313 | 0.308 |
| Cocluster {21}  | 0.212 | 0.171 | 0.378 | 0.376 |
| Cocluster {22}  | 0.203 | 0.184 | 0.280 | 0.277 |
| Cocluster {23}  | 0.176 | 0.154 | 0.262 | 0.262 |
| Cocluster {24}  | 0.170 | 0.158 | 0.219 | 0.219 |
| Cocluster {25}  | 0.167 | 0.131 | 0.310 | 0.307 |

## 2D SpectralBiclustering – QualityC

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| Bicluster {1}  | 0.158 | 0.147 | 0.201 | 0.202 |
| Bicluster {2}  | 0.173 | 0.154 | 0.249 | 0.250 |
| Bicluster {3}  | 0.339 | 0.300 | 0.495 | 0.493 |
| Bicluster {4}  | 0.201 | 0.190 | 0.245 | 0.241 |
| Bicluster {5}  | 0.417 | 0.360 | 0.638 | 0.650 |

## 1D K-Means – Yeast

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| KMeans {1}  | 0.277 | 0.272 | 0.297 | 0.295 |
| KMeans {2}  | 0.373 | 0.359 | 0.426 | 0.436 |
| KMeans {3}  | 0.112 | 0.100 | 0.167 | 0.159 |
| KMeans {4}  | 0.212 | 0.203 | 0.248 | 0.242 |
| KMeans {5}  | 0.125 | 0.112 | 0.177 | 0.177 |

## 1D Agglomerative – Yeast

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| Agglomerative {1}  | 0.150 | 0.136 | 0.209 | 0.202 |
| Agglomerative {2}  | 0.134 | 0.117 | 0.204 | 0.198 |
| Agglomerative {3}  | 0.337 | 0.332 | 0.356 | 0.357 |
| Agglomerative {4}  | 0.074 | 0.054 | 0.158 | 0.153 |
| Agglomerative {5}  | 0.422 | 0.413 | 0.462 | 0.448 |

## 2D SpectralCoclustering – Yeast

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| Cocluster {2}  | 0.376 | 0.374 | 0.395 | 0.368 |
| Cocluster {3}  | 0.331 | 0.331 | 0.338 | 0.330 |
| Cocluster {4}  | 0.327 | 0.320 | 0.358 | 0.357 |
| Cocluster {5}  | 0.395 | 0.395 | 0.400 | 0.388 |
| Cocluster {7}  | 0.235 | 0.226 | 0.276 | 0.266 |
| Cocluster {8}  | 0.204 | 0.177 | 0.320 | 0.307 |
| Cocluster {9}  | 0.182 | 0.168 | 0.244 | 0.236 |
| Cocluster {10}  | 0.330 | 0.320 | 0.377 | 0.362 |
| Cocluster {12}  | 0.380 | 0.388 | 0.360 | 0.328 |
| Cocluster {13}  | 0.272 | 0.261 | 0.321 | 0.311 |
| Cocluster {14}  | 0.178 | 0.166 | 0.227 | 0.220 |
| Cocluster {15}  | 0.338 | 0.338 | 0.349 | 0.332 |
| Cocluster {17}  | 0.217 | 0.209 | 0.253 | 0.245 |
| Cocluster {18}  | 0.205 | 0.180 | 0.307 | 0.300 |
| Cocluster {19}  | 0.142 | 0.122 | 0.224 | 0.217 |
| Cocluster {20}  | 0.259 | 0.237 | 0.356 | 0.332 |
| Cocluster {22}  | 0.208 | 0.198 | 0.251 | 0.243 |
| Cocluster {23}  | 0.261 | 0.247 | 0.322 | 0.310 |
| Cocluster {24}  | 0.135 | 0.117 | 0.214 | 0.207 |
| Cocluster {25}  | 0.290 | 0.274 | 0.368 | 0.343 |

## 2D SpectralBiclustering – Yeast

| Solution | TRIQ  | GRQ   | PEQ   | SPQ   |
| :------- | :---- | :---- | :---- | :---- |
| Bicluster {1}  | 0.166 | 0.146 | 0.245 | 0.239 |
| Bicluster {2}  | 0.397 | 0.392 | 0.423 | 0.408 |
| Bicluster {3}  | 0.419 | 0.415 | 0.471 | 0.395 |
| Bicluster {4}  | 0.497 | 0.518 | 0.424 | 0.402 |
| Bicluster {5}  | 0.433 | 0.431 | 0.445 | 0.438 |