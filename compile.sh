#!/usr/bin/env bash
set -e

echo "===================================================================="
echo "  Compiling EduTrack Application (Pure Java SE)"
echo "===================================================================="

mkdir -p bin

echo "Compiling source files..."
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
rm -f sources.txt

echo "Compiling test files..."
find test -name "*.java" > test_sources.txt
javac -encoding UTF-8 -cp bin -d bin @test_sources.txt
rm -f test_sources.txt

echo "[SUCCESS] All source and test classes compiled cleanly into bin/"
