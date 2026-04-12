# G-Tric Algorithm (Lobo et al. BMC Bioinformatics 2021)

**Reference:** Lobo J, Henriques R, Madeira SC. *G-Tric: generating three-way synthetic datasets with triclustering solutions*. BMC Bioinformatics 22 (2021) 16.

## What G-Tric Is

G-Tric is a **synthetic data generator** that creates 3D datasets with planted triclusters. It is used to benchmark triclustering algorithms (e.g., TriGen, triCluster, δ-TRIMAX) by providing datasets with known ground truth.

## Requirements

- **Java 11+**
- **Maven**

## Build & Run

### Option 1: Batch script (Windows)

```batch
cd "d:\8th sem\basepaper\G-Tric"
run_gtrig.bat
```

### Option 2: Manual (PowerShell)

In **PowerShell**, quote the `-D` argument so it isn’t parsed as a PowerShell parameter:

```powershell
cd "d:\8th sem\basepaper\G-Tric"
cd G-Tric
mvn clean install -DskipTests

cd ..\Demo\G-Tric-Demo
mvn exec:java "-Dexec.mainClass=DatasetGenerator.DatasetGenerator"
```

Or use the PowerShell script:
```powershell
.\run_gtrig.ps1
```

### Option 3: GUI

```bash
cd G-Tric
mvn javafx:run
```

## Output

Generated datasets go to `Demo/G-Tric-Demo/GeneratedDatasets/`.

Config files in `Demo/G-Tric-Demo/config_files/` define:
- **Base**: dataset_base_B/C/R/S.xml
- **Overlapping**: dataset_base_*_overlapping_*.xml
- **Quality**: dataset_base_*_quality_*.xml
- **Scalability**: dataset_base_*_S*.xml
- **Real World**: dataset_real_world_*.xml

## Config Format

XML files control dimensions, pattern types (constant, additive, multiplicative, order-preserving), overlapping, noise, and contiguity.
