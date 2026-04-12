# Ontologizer Diagnostic Script
# Run this to test if Ontologizer works with your setup

$basePath = "D:\8th sem\TrLab3.5"
$ontologyPath = "$basePath\resources\terms\go-basic.obo"
$associationPath = "$basePath\resources\associations\yeast_gene_association-web.sgd"
$populationPath = "$basePath\resources\0001\yeast_genes.txt"

# Create a small study set (first 50 genes from tricluster 1)
$studyPath = "$basePath\test_study_genes.txt"
@(
    "YBL044W", "YBL045C", "YBL046W", "YBL047C", "YBL048W",
    "YBL049W", "YBL050W", "YBL051C", "YBL052C", "YBL053W"
) | Set-Content $studyPath -Encoding UTF8

$outputPath = "$basePath\test_ontologizer_output"

Write-Host "Testing Ontologizer..."
Write-Host "  Ontology: $ontologyPath"
Write-Host "  Association: $associationPath"
Write-Host "  Population: $populationPath"
Write-Host "  Study: $studyPath"
Write-Host "  Output: $outputPath"
Write-Host ""

New-Item -ItemType Directory -Force -Path $outputPath | Out-Null

$cmd = @(
    "java", "-jar", "$basePath\Ontologizer.jar",
    "-g", $ontologyPath,
    "-a", $associationPath,
    "-p", $populationPath,
    "-s", $studyPath,
    "-o", $outputPath,
    "-m", "Bonferroni",
    "-c", "Term-For-Term"
)

Write-Host "Running: java -jar Ontologizer.jar -g <ontology> -a <association> -p <population> -s <study> -o <output> ..."
Write-Host ""

& $cmd[0] $cmd[1..($cmd.Length-1)] 2>&1 | Tee-Object -Variable output

Write-Host ""
Write-Host "Exit code: $LASTEXITCODE"
Write-Host ""
Write-Host "Output files in $outputPath`:"
Get-ChildItem $outputPath -ErrorAction SilentlyContinue | ForEach-Object { Write-Host "  - $($_.Name)" }
