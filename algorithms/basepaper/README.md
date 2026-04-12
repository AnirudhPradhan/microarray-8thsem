# Basepaper Dataset Setup

Environment and data setup for **Soares et al. Pattern Recognition 2024** – *Comprehensive assessment of triclustering algorithms for three-way temporal data analysis*.

## Reference

- **Paper:** Soares DF, Henriques R, Madeira SC. Pattern Recognition 150 (2024) 110303
- **Dataset source:** https://github.com/dfmsoares/triclustering-algorithms-assessment
- **G-Tric:** Lobo J et al. BMC Bioinformatics 22 (2021) 16

## Datasets (as in Basepaper)

The `datasets/` folder contains the **5 experiment collections** from the paper (Table 2):

| Collection | Folder(s) | Description |
|------------|-----------|-------------|
| **1st – Baseline** | `Background`, `BaseR` | 500×10×5 real-valued, 40 triclusters, [0,50000] |
| **2nd – Pattern-specific** | `Constant`, `Additive`, `Multiplicative`, `OrderPreserving` | One pattern type per dataset |
| **3rd – Overlapping** | `OverlappingC`, `OverlappingA`, `OverlappingM`, `OverlappingOP` | 40% overlapping triclusters |
| **4th – Quality** | `QualityC`, `QualityA`, `QualityM`, `QualityOP` | Noise 10%, errors 5% |
| **5th – Contiguity** | `ContiguityC`, `ContiguityA`, `ContiguityM`, `ContiguityOP` | Contiguity in temporal dimension |

Each folder contains:
- `*_data.tsv` or `*.tsv` – tensor (objects × attributes × contexts)
- `*_trics.json` / `*_trics.txt` – ground truth triclusters

## Loading (example)

```python
import pandas as pd
import numpy as np

# Baseline (500×10×5)
df = pd.read_csv("datasets/Background/background.tsv", sep="\t", index_col=0)
# Columns: y0z0, y1z0, ... (10 attrs × 5 contexts)
tensor = df.values.reshape(500, 10, 5)
```

## G-Tric (optional)

To generate new datasets with G-Tric:
- **Location:** `G-Tric/`
- **Run:** See `G-Tric-README.md`
- **Requirements:** Java 11+, Maven
