# Run TriGen on Base Paper and TriGen Paper Datasets
# GA settings from each paper

param(
    [switch]$BasePaper = $true,   # Benchmark_Datasets (synthetic_qualityc), fitness=MSL
    [switch]$TriGenPaper = $true  # Yeast (Spellman), fitness=MSR3D
)

$TrLab = "d:\8th sem\algorithms\TrLab3.5"

Write-Host "=== TriGen Runs: Base Paper + TriGen Paper ===" -ForegroundColor Cyan
Write-Host ""

if ($BasePaper) {
    Write-Host "1. Base Paper (Soares et al. Pattern Recognition 2024)" -ForegroundColor Yellow
    Write-Host "   Dataset: synthetic_qualityc (Benchmark_Datasets/QualityC)" -ForegroundColor Gray
    Write-Host "   Fitness: MSL (paper reports MSL, LSL, MSR3D; MSL best)" -ForegroundColor Gray
    Write-Host "   GA: N=5, G=20, I=10" -ForegroundColor Gray
    Push-Location $TrLab
    java -jar TriGenApp.jar "d:\8th sem\algorithms\TrLab3.5\synthetic_qualityc_run.tricfg"
    Pop-Location
    Write-Host "   Output: resources\synthetic_qualityc_run\*" -ForegroundColor Gray
    Write-Host ""
}

if ($TriGenPaper) {
    Write-Host "2. TriGen Paper (Gutiérrez-Avilés et al. Neurocomputing 2014)" -ForegroundColor Yellow
    Write-Host "   Dataset: yeast (Spellman cell cycle)" -ForegroundColor Gray
    Write-Host "   Fitness: MSR3D (original TriGen: volume + 3D MSR)" -ForegroundColor Gray
    Write-Host "   GA: N=5, G=20, I=10" -ForegroundColor Gray
    Push-Location $TrLab
    java -jar TriGenApp.jar "d:\8th sem\algorithms\TrLab3.5\yeast_trigenpaper.tricfg"
    Pop-Location
    Write-Host "   Output: resources\yeast_trigenpaper_run\*" -ForegroundColor Gray
    Write-Host ""
}

Write-Host "=== Done ===" -ForegroundColor Green
