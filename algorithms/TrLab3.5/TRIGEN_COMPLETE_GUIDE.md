# TriGen / TrLab: Complete Technical Guide
## From Data Representation to Algorithm and Analysis

---

## 1. Gene Data: What It Is and How It's Represented

### 1.1 Three-Way Temporal Data

Gene expression data in TrLab is **three-way temporal data** (a 3D tensor):

| Dimension | Meaning | Example (Yeast) |
|-----------|---------|-----------------|
| **Genes (G)** | Objects / features | 6,178 genes |
| **Samples (C)** | Conditions / attributes | 4 cell-cycle experiments (alpha, cdc15, cdc28, elu) |
| **Times (T)** | Temporal context | 14 time points (Time 0 … Time 13) |

Each value \( a_{g,s,t} \) is the expression level of gene \( g \) in sample \( s \) at time \( t \).

### 1.2 File Structure

Data lives under `resources/<dataset_id>/` (e.g. `resources/0001/` for yeast).

**Metadata files:**
- `yeast_genes.txt` — one gene ID per line (e.g. YAL001C, YAL002W, …)
- `yeast_samples.txt` — condition names (alpha, cdc15, cdc28, elu)
- `yeast_times.txt` — time labels (Time 0 … Time 13)

**Data files (one CSV per time point):**
- `yeast_t0.csv`, `yeast_t1.csv`, … `yeast_t13.csv`
- One file per time slice
- Rows = genes, columns = samples
- Separator: `;` (or `,` in other datasets)

**Example structure of `yeast_t0.csv`:**
```
-0.15;-0.16;-0.19;0.3     ← gene 1, samples 1..4
...                        ← gene 2
...                        ← gene 6178
```

### 1.3 In-Memory Representation

`Common.buindDatasetFromFile()` builds:

```java
double[][][] dataset = new double[geneSize][sampleSize][timeSize];
// dataset[g][s][t] = expression of gene g, sample s, time t
```

- Each CSV is read row-by-row; each row becomes one gene for that time slice.
- Columns in a row map to samples; values are parsed as doubles.

---

## 2. Configuration: resources.xml and Control File

### 2.1 `resources.xml` — Dataset Catalog

Defines all datasets and where to find their files:

```xml
<dataset id="0001" name="yeast" geneSize="6178" sampleSize="4" timeSize="14"
         minG="15" maxG="200" minC="2" maxC="4" minT="3" maxT="8" type="b">
  <resources separator=";">
    <resource>yeast_t0.csv</resource>
    <resource>yeast_t1.csv</resource>
    ...
  </resources>
  <genes>yeast_genes.txt</genes>
  <samples>yeast_samples.txt</samples>
  <times>yeast_times.txt</times>
</dataset>
```

- `type="b"` → biological (Gene Ontology enrichment)
- `type="e"` → other / non-biological
- `minG/maxG`, `minC/maxC`, `minT/maxT` constrain tricluster sizes.

Path resolution: base = directory of `resources.xml`, then `dataset_id/` + file name.

### 2.2 Control File (.tricfg or .properties)

Algorithm and run parameters (can be in a `.tricfg` or `.properties` file):

```
dataset = yeast
fitness = msl
N = 5          # number of solutions to find
G = 20         # generations per solution
I = 10         # population size
Ale = 0.2
Sel = 0.5
Mut = 0.1
Wf = 0.8       # weight for fitness
Wg, Wc, Wt     # weights for gene/sample/time size
WOg, WOc, WOt  # weights for overlap penalty
...
individual = coordinate tricluster
datahierarchy = levelsGST
stoppingcriterion = find solutions
solutioncriterion = minimum fitness
initialpop = tensors
selection = tournament
crossover = one point
mutation = put, remove and change
```

Defaults come from bundled `algorithm.properties` if keys are missing.

---

## 3. Running TriGenApp

### 3.1 Command

```bash
java -jar TriGenApp.jar "path/to/control.tricfg"
```

Or, when `out` is omitted in the control file, the output base path is derived from the control file path (extension removed).

### 3.2 Execution Flow (main → TriGen → Analysis)

```
Console.main(args)
  │
  ├─ TriGenRun.loadControl(args[0])     → builds Control from .tricfg
  ├─ FootBridge.buildParameters(control)
  ├─ TriGenBuilder.buildTriGen(control.getImplementation())
  │
  ├─ TriGen.runAlgorithm()              ← evolutionary loop
  │     ├─ produceInitialPopulation()
  │     ├─ for each generation G:
  │     │     evaluate(population)        ← fitness
  │     │     select(parents)
  │     │     crossover(children)
  │     │     mutate(children)
  │     │     population = parents + mutatedChildren
  │     └─ chooseTheBest() → add to solutions
  │
  ├─ Parser.buildSolutionFile()         → write .sol file
  └─ Facade.buildCompleteResultsFiles() → TRIQ, coordinates, arranged, genes, etc.
```

---

## 4. TriGen Algorithm: Multi-Objective Evolutionary Triclustering

### 4.1 Core Loop (Genetic Algorithm)

```
repeat N times (N solutions):
  population = produceInitialPopulation()
  for generation = 1 to G:
    evaluate(population)      # fitness for each individual
    parents = select(population)
    children = crossover(parents)
    mutatedChildren = mutate(children)
    population = parents + mutatedChildren
  best = chooseTheBest(population)
  solutions.add(best)
  updateDataHierarchy(best)   # exclude used subspace for next solution
```

### 4.2 Representation

- **Individual** = one tricluster: subsets of genes, samples, and times.
- **Coordinate tricluster** = three index sets: `(genes[], samples[], times[])`.

### 4.3 Fitness Function (MSL variant)

Configured as `fitness = msl` in the control file. It combines several terms:

```java
// StrMsl: Mean Square residue–like measure over angles
// Uses angular differences in GC, GT, TG slices
eval_base = multi.calculate(individual);  // homogeneity

// StrSizes: volume-related (normalized gene/sample/time sizes)
(prg, prc, prt) = getAmount(individual);

// StrMatching: overlap with previously found triclusters
(sg, sc, st) = getAmount(individual);

// Combined fitness (minimized):
fitness = (wf*neval + wog*sg + woc*sc + wot*st + cprg*wg + cprc*wc + cprt*wt)
          / (wf + wog + woc + wot + wg + wc + wt)
```

- `neval` — homogeneity (lower is better).
- `sg, sc, st` — overlap penalties.
- `prg, prc, prt` — size contribution (encourages larger coherent triclusters).

### 4.4 Operators

- **Initial population**: `tensors` — random triclusters within min/max size bounds.
- **Selection**: `tournament` — select best individuals.
- **Crossover**: `one point` — exchange parts of genes/samples/times between parents.
- **Mutation**: `put, remove and change` — add/remove/swap genes, samples, or times.

### 4.5 Data Hierarchy

After each solution, the used subspace is marked in `DataHierarchy` so that the next run focuses on different regions, reducing redundancy.

---

## 5. Post-Run Analysis

### 5.1 `Experiment.computeAnalysis()`

```java
computeAnalysisSolutionLevel()  → triqAnalysis.computeTRIQ()
computeAnalysisExperimentLevel() → rank solutions, compute mean/stdev
```

### 5.2 TRIQ: Tricluster Quality Score

`Btriq` implements TRIQ as a weighted combination:

```
TRIQ = (wGrq*GRQ + wPeq*PEQ + wSpq*SPQ + wBioq*BIOQ) / (wGrq + wPeq + wSpq + wBioq)
```

| Component | Role |
|-----------|------|
| **GRQ** | Structural / geometric quality |
| **PEQ** | Pearson correlation in gene–condition–time slices |
| **SPQ** | Spearman correlation |
| **BIOQ** | Biological significance (Gene Ontology enrichment) |

BIOQ needs GO gene associations (e.g. yeast_gene_association-web.sgd). Without them, BIOQ is empty and we use the patched behavior (empty slots).

### 5.3 Output Files

Written to `resources/resources/` (or the configured output folder):

| File | Content |
|------|---------|
| `resources.sol` | Properties-style file with config + tricluster coordinates |
| `resources_triq.csv` | TRIQ scores per solution |
| `resources_sig.csv` | GO significance report (pa) |
| `resources_sign.csv` | GO significance report (p) |
| `genes/resources_tri_1.txt` … | Gene IDs per tricluster |
| `coordinates/resources_co_*.csv` | Tricluster coordinates |
| `arranged/resources_ar_*.csv` | Arranged tricluster data |

---

## 6. LabApp: Visualizing and Analysing Results

### 6.1 Role

LabApp is for **analysing** existing `.sol` files, not for running TriGen.

### 6.2 Usage

1. Run: `java -jar LabApp.jar`
2. Point to a solution file, e.g.  
   `D:\8th sem\TrLab3.5\resources\resources\resources.sol`
3. Click **Analyse** to run TRIQ, generate reports, and visualise triclusters.

### 6.3 Analysis Path (same as TriGenApp)

LabApp reuses the same analysis pipeline: `Experiment.computeAnalysis()` → `Btriq.computeTRIQ()` → GRQ, PEQ, SPQ, BIOQ, SignificanceReports.

---

## 7. Summary Flow Diagram

```
resources.xml (dataset defs)     control.tricfg (N, G, I, fitness, ...)
         │                                    │
         └──────────────┬─────────────────────┘
                        ▼
              InputFacade.buildControl()
                        │
                        ▼
         DatasetsLoader → Common (double[][][])
                        │
                        ▼
              TriGen.runAlgorithm()
                        │
         [InitialPop → Evaluate → Select → Crossover → Mutate] × G generations
                        │
                        ▼
              List<AlgorithmIndividual> (triclusters)
                        │
                        ▼
         Parser.buildSolutionFile()      Facade.buildCompleteResultsFiles()
                        │                            │
                        ▼                            ▼
                 resources.sol              TRIQ, coordinates, genes, sig
                        │
                        ▼
              LabApp loads .sol → Analyse → Reports & visualisation
```

---

## 8. Code References (decompiled sources)

| File | Responsibility |
|------|----------------|
| `algentrypoint/Console.java` | Main entry, orchestration |
| `input/workflows/TriGenRun.java` | Load control from path |
| `input/InputFacade.java` | Build Control, Options, Dataset |
| `input/datasets/DatasetsLoader.java` | Parse resources.xml, resolve paths |
| `input/datasets/Common.java` | Build 3D tensor from CSV files |
| `algcore/TriGen.java` | Evolutionary loop |
| `fitnessfunctions/Msl.java` | MSL-based fitness |
| `strfitness/StrMsl.java` | Homogeneity (angle-based) |
| `analysis/Experiment.java` | Experiment-level analysis |
| `analysis/Btriq.java` | TRIQ = GRQ + PEQ + SPQ + BIOQ |
| `analysis/biological/BIOQ.java` | GO enrichment |
| `labentrypoint/Facade.java` | Write all result files |
