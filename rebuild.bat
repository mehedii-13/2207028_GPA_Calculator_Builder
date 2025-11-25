@echo off
echo ========================================
echo   GPA Calculator - Full Rebuild
echo ========================================
echo.

echo [1/4] Cleaning old build files...
if exist target\classes rmdir /s /q target\classes
if exist gpa_calculator.db del gpa_calculator.db
echo ✓ Cleaned

echo.
echo [2/4] Please do the following in IntelliJ:
echo    - Click: Build → Rebuild Project
echo    - Wait for build to complete
echo.
pause

echo.
echo [3/4] Database will be created fresh on next run
echo ✓ Ready

echo.
echo [4/4] Now run the application:
echo    - Click the green Play button ▶️ in IntelliJ
echo.
echo ========================================
echo   Rebuild Complete!
echo ========================================
pause
