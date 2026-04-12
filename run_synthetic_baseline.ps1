# Run Base Paper Synthetic Dataset Experiments
# Reference: Soares et al. Pattern Recognition 150 (2024) 110303
# Datasets: G-Tric synthetic from triclustering-algorithms-assessment

param(
    [switch]$SyntheticGenerator = $false,  # Run neucom-trigen synthetic_generator.py (TriGen paper style)
    [switch]$TriGen = $true               # Run TriGen on base paper QualityC synthetic dataset
)

$ErrorActionPreference = "Stop"
$BaseDir = "d:\8th sem"
$TrLab = Join-Path $BaseDir "algorithms\TrLab3.5"
$Venv = Join-Path $BaseDir "venv\Scripts\python.exe"

Write-Host "=== Base Paper Synthetic Dataset Run ===" -ForegroundColor Cyan
Write-Host ""

# 1. Optional: Run neucom-trigen synthetic generator (TriGen 2014 paper style: 1000x10x5)
if ($SyntheticGenerator) {
    Write-Host "1. Running neucom-trigen synthetic_generator.py..." -ForegroundColor Yellow
    Push-Location (Join-Path $BaseDir "algorithms\neucom-trigen")
    & $Venv synthetic_generator.py
    Pop-Location
    Write-Host "   Output: algorithms\neucom-trigen\data\tricgen_synthetic_tensor.npz" -ForegroundColor Gray
    Write-Host ""
}

# 2. Run TriGen on base paper QualityC synthetic dataset
if ($TriGen) {
    Write-Host "2. Running TriGen on base paper synthetic (QualityC - Constant pattern)..." -ForegroundColor Yellow
    $tricfg = Join-Path $TrLab "synthetic_qualityc_run.tricfg"
    Push-Location $TrLab
    java -jar TriGenApp.jar $tricfg
    Pop-Location
    Write-Host "   Output: algorithms\TrLab3.5\resources\synthetic_qualityc_run\synthetic_qualityc_run\*.sol" -ForegroundColor Gray
    Write-Host ""
}

Write-Host "=== Done ===" -ForegroundColor Green
Write-Host ""
Write-Host "To run both synthetic generator AND TriGen:" -ForegroundColor Gray
Write-Host "  .\run_synthetic_baseline.ps1 -SyntheticGenerator -TriGen" -ForegroundColor Gray
Write-Host ""
Write-Host "Base paper datasets (data/benchmark/Benchmark_Datasets):" -ForegroundColor Gray
Write-Host "  QualityC, QualityA, QualityM, QualityOP - pattern-specific with noise/errors" -ForegroundColor Gray
Write-Host "  Additive, Constant, Multiplicative, OrderPreserving - 2nd collection" -ForegroundColor Gray
