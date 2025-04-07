@echo off
echo Stopping any running Angular servers...
taskkill /f /im node.exe 2>nul
echo.

echo Starting DocConnect UI application...
echo.

cd frontend\docconnect-ui
call npm start

echo.
echo If the browser does not open automatically, please go to:
echo http://localhost:4200
