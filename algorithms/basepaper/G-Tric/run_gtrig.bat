@echo off
REM G-Tric: Lobo et al. BMC Bioinformatics 2021
REM Requires: Java 11+, Maven
REM Build and run G-Tric Demo to generate synthetic datasets

cd /d "%~dp0"

echo === G-Tric - Synthetic Triclustering Dataset Generator ===
echo.

REM Build G-Tric core first
echo 1. Building G-Tric...
cd G-Tric
call mvn clean install -DskipTests -q
if errorlevel 1 (
    echo Build failed. Ensure Maven and Java 11+ are installed.
    pause
    exit /b 1
)
cd ..

REM Build and run Demo (generates datasets from config_files)
echo 2. Building and running G-Tric Demo...
cd Demo\G-Tric-Demo
call mvn exec:java "-Dexec.mainClass=DatasetGenerator.DatasetGenerator" -q
cd ..\..

echo.
echo Datasets saved to Demo\G-Tric-Demo\GeneratedDatasets\
pause
