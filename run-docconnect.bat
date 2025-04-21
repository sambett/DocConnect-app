@echo off
echo Starting DocConnect (Backend and Frontend)...
echo.

echo Starting MySQL (if installed)...
net start MySQL80 2>nul

echo Starting Backend...
start cmd /k "cd backend && mvn spring-boot:run"

echo Waiting for backend to initialize...
timeout /t 20

echo Starting Frontend...
start cmd /k "cd frontend\docconnect-app && npm start"

echo.
echo DocConnect is starting up:
echo - Backend: http://localhost:8080/api
echo - Frontend: http://localhost:4200
echo.
echo NOTE: Make sure MySQL is running with a 'docconnect' database
echo       or update application.properties to use H2 in-memory DB.
