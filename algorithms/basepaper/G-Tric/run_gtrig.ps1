# G-Tric: Lobo et al. BMC Bioinformatics 2021
# Requires: Java 11+, Maven
# Use this in PowerShell (run_gtrig.bat for CMD)

Set-Location $PSScriptRoot

Write-Host "=== G-Tric - Synthetic Triclustering Dataset Generator ===" -ForegroundColor Cyan
Write-Host ""

# Build G-Tric core first
Write-Host "1. Building G-Tric..." -ForegroundColor Yellow
Set-Location G-Tric
mvn clean install -DskipTests -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed. Ensure Maven and Java 11+ are installed." -ForegroundColor Red
    Set-Location $PSScriptRoot
    exit 1
}
Set-Location ..

# Build and run Demo (PowerShell: quote -D args so -D is not parsed as PowerShell parameter)
Write-Host "2. Building and running G-Tric Demo..." -ForegroundColor Yellow
Set-Location Demo\G-Tric-Demo
mvn exec:java "-Dexec.mainClass=DatasetGenerator.DatasetGenerator" -q
Set-Location ..\..

Write-Host ""
Write-Host "Datasets saved to Demo\G-Tric-Demo\GeneratedDatasets\" -ForegroundColor Green
