@echo off
echo.
echo ============================================
echo        🐳 DocConnect Docker Setup
echo ============================================
echo.

REM Check if Docker is running
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker is not running. Please start Docker Desktop first.
    echo.
    pause
    exit /b 1
)

echo ✅ Docker is running

REM Check if .env file exists
if not exist .env (
    echo.
    echo 📝 Creating .env file from template...
    copy .env.example .env >nul
    echo ✅ Created .env file - you can customize it later
)

echo.
echo 🚀 Starting DocConnect containers...
echo    This may take a few minutes for the first run...
echo.

REM Start the application
docker-compose up -d

if %errorlevel% equ 0 (
    echo.
    echo ============================================
    echo        🎉 DocConnect is starting up!
    echo ============================================
    echo.
    echo 🌐 Frontend: http://localhost:3000
    echo 🔌 Backend:  http://localhost:8080/api
    echo 🗄️  Database: localhost:3307
    echo.
    echo 📊 Check status: docker-compose ps
    echo 📋 View logs:   docker-compose logs -f
    echo 🛑 Stop all:    docker-compose down
    echo.
    echo ⏳ Please wait ~60 seconds for all services to be ready...
) else (
    echo.
    echo ❌ Failed to start containers. Check the logs:
    echo    docker-compose logs
)

echo.
pause
