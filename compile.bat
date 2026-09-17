@echo off
echo ====================================================================
echo   Compiling EduTrack Application (Pure Java SE)
echo ====================================================================

if not exist "bin" mkdir bin

echo Compiling source files...
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    del sources.txt 2>nul
    exit /b %ERRORLEVEL%
)
del sources.txt 2>nul

echo Compiling test files...
dir /s /b test\*.java > test_sources.txt
javac -encoding UTF-8 -cp bin -d bin @test_sources.txt
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Test compilation failed!
    del test_sources.txt 2>nul
    exit /b %ERRORLEVEL%
)
del test_sources.txt 2>nul

echo [SUCCESS] All source and test classes compiled cleanly into bin/
