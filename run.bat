@echo off
if not exist "bin\com\edutrack\EduTrackApp.class" (
    echo Binaries not found. Running compilation first...
    call compile.bat
)

echo Starting EduTrack CLI Application...
java "-Dfile.encoding=UTF-8" -cp bin com.edutrack.EduTrackApp %*
