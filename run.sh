#!/usr/bin/env bash
set -e

if [ ! -f "bin/com/edutrack/EduTrackApp.class" ]; then
    echo "Binaries not found. Running compilation first..."
    bash compile.sh
fi

echo "Starting EduTrack CLI Application..."
java -Dfile.encoding=UTF-8 -cp bin com.edutrack.EduTrackApp "$@"
