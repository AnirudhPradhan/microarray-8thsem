@echo off
REM TrLab Single Analysis - Launcher
REM Ensures correct working directory for path resolution
cd /d "%~dp0"
echo Starting TrLab - Single Analysis...
echo.
echo NOTE: Analysis may take 15-30 seconds. Please wait.
echo.
java -jar LabApp.jar
pause
