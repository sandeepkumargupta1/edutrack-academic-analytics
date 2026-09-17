@echo off
if not exist "bin\com\edutrack\TestRunner.class" (
    echo Binaries not found. Running compilation first...
    call compile.bat
)

echo Executing Automated Test Suite...
java "-Dfile.encoding=UTF-8" -cp bin com.edutrack.TestRunner
