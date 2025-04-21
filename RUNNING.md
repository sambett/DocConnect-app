# Running DocConnect

This guide will help you run the DocConnect application, which consists of a Spring Boot backend and an Angular frontend.

## Prerequisites

- Java 17 or higher
- Node.js and npm
- MySQL database (with a database named 'docconnect')

## Quick Start

1. **Before starting, make sure no other applications are using ports 8081 or 4200**
   - Run the `kill-ports.bat` script to free up ports if needed

2. **Start the entire application**
   - Run `run-docconnect.bat` script which will:
     - Start MySQL if installed
     - Start the backend on port 8081
     - Wait 20 seconds for backend initialization
     - Start the frontend on port 4200

3. **Access the application**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8081/api

## Running Components Separately

### Backend Only

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

3. The backend will be available at: http://localhost:8081/api

### Frontend Only

1. Navigate to the frontend directory:
   ```
   cd frontend/docconnect-app
   ```

2. Start the Angular development server:
   ```
   npm start
   ```

3. The frontend will be available at: http://localhost:4200

## Troubleshooting

### Port Already in Use

If you see an error message like "Web server failed to start. Port 8081 was already in use":

1. Run the `kill-ports.bat` script to terminate processes using ports 8081 and 4200
2. Try starting the application again

### Database Connection Issues

If the backend fails to connect to the database:

1. Make sure MySQL is running
2. Verify that a database named 'docconnect' exists
3. Check the database credentials in `backend/src/main/resources/application.properties`

### Other Issues

- Check backend logs in the terminal window
- Check frontend logs in the browser console (F12)

## API Documentation

The backend provides the following main endpoints:

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login with email and password
- `GET /api/auth/user` - Get current user details
- `GET /api/professors` - Get all professors
- `POST /api/professors/{id}/status` - Update professor status
