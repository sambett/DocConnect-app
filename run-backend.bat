@echo off
echo Starting DocConnect Backend...
echo.

echo Starting MySQL (if installed)...
net start MySQL80 2>nul

cd backend
mvn spring-boot:run

echo.
echo Backend should be running at http://localhost:8080/api
