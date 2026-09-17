#!/usr/bin/env bash
set -e

if [ ! -f "bin/com/edutrack/TestRunner.class" ]; then
    echo "Binaries not found. Running compilation first..."
    bash compile.sh
fi

echo "Executing Automated Test Suite..."
java -Dfile.encoding=UTF-8 -cp bin com.edutrack.TestRunner
