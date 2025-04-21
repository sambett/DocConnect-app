@echo off
echo Checking for processes using port 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo Found process using port 8080: %%a
    taskkill /F /PID %%a
    echo Killed process %%a
)

echo Checking for processes using port 8081...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do (
    echo Found process using port 8081: %%a
    taskkill /F /PID %%a
    echo Killed process %%a
)

echo All conflicting processes terminated.
echo You can now start DocConnect.
