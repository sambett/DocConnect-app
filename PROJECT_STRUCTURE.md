# DocConnect Project Structure

## Overview

DocConnect is a web application that connects students with professors, allowing students to see professor availability in real-time and get notified when professors become available.

## Project Structure

The project is organized into two main parts:

### Frontend (Angular)

Located in `frontend/docconnect-ui`:
- Angular 17 application
- Uses Firebase for authentication
- Tailwind CSS for styling
- Contains both professor and student interfaces

### Backend (Spring Boot)

Located in `backend`:
- Spring Boot application
- MySQL database for data storage 
- RESTful API for frontend communication
- Firebase Admin SDK for token verification

## Important Files

- `start-docconnect.bat`: Script to start the frontend application
- `AUTHENTICATION_FLOW.md`: Explains the authentication process
- `README.md`: Original project documentation

## How to Run the Application

1. **Start the Backend**:
   - Navigate to the `backend` directory
   - Run `mvn spring-boot:run` (requires Maven and Java)

2. **Start the Frontend**:
   - Run `start-docconnect.bat` from the project root
   - Or navigate to `frontend/docconnect-ui` and run `npm start`

3. **Access the Application**:
   - Open your browser to `http://localhost:4200`

## Notes

- The project uses Firebase for authentication (both frontend and backend)
- MySQL is used for storing user data, professor statuses, and other application data
- The `docconnect-app` directory contains an older version of the frontend that is no longer used
