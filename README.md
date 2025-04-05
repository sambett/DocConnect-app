# DocConnect

DocConnect is a real-time professor availability tracking application that helps students and professors connect more efficiently.

## Project Structure

The project is divided into two main parts:

1. **Backend** - Spring Boot application with MySQL database
2. **Frontend** - Angular application with Tailwind CSS

## Features

- Role-based authentication (Student/Professor) using Firebase Auth
- Real-time professor status updates (Available, Busy, Away, In Meeting)
- Student notification system when professors become available
- Favoriting professors for quick access
- Searching and filtering professors
- Asking questions to professors
- Managing announcements

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- MySQL Database
- Firebase Admin SDK for authentication
- Maven for dependency management

### Frontend
- Angular 17
- Tailwind CSS
- Firebase Authentication
- RxJS for reactive programming

## Setup Instructions

### Backend Setup

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

The backend will start on port 8080.

### Frontend Setup

1. Navigate to the frontend directory:
   ```
   cd frontend/docconnect-ui
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Run the development server:
   ```
   npm start
   ```

The Angular application will start on port 4200. Access it at http://localhost:4200.

## Database Configuration

The application uses MySQL. You can update the database connection details in the `application.properties` file:

```
spring.datasource.url=jdbc:mysql://localhost:3306/docconnect
spring.datasource.username=root
spring.datasource.password=
```

## Firebase Authentication Setup

1. Create a Firebase project at https://firebase.google.com/
2. Enable Email/Password authentication
3. Get your Firebase configuration and update:
   - Backend: `firebase-service-account.json` file in resources folder
   - Frontend: `environment.ts` and `environment.development.ts` files

## API Endpoints

### Authentication
- POST /api/auth/register - Register a new user
- POST /api/auth/verify-token - Verify Firebase token

### Professors
- GET /api/professors - Get all professors
- GET /api/professors/{id} - Get professor by ID
- GET /api/professors/{id}/status - Get professor's current status
- POST /api/professors/{id}/status - Update professor's status
- GET /api/professors/{id}/waiting-students - Get count of waiting students

### Students
- GET /api/students/{id}/notifications - Get student's notifications
- POST /api/students/{id}/notifications/{professorId} - Create notification
- DELETE /api/students/{id}/notifications/{professorId} - Cancel notification
- GET /api/students/{id}/favorites - Get student's favorite professors
- POST /api/students/{id}/favorites/{professorId} - Add professor to favorites
- DELETE /api/students/{id}/favorites/{professorId} - Remove professor from favorites

## Deployment

### Backend
The Spring Boot application can be deployed as a JAR file on any Java-compatible server.

### Frontend
The Angular application can be built for production with:
```
npm run build
```

The build artifacts will be stored in the `dist/` directory, which can be deployed to any web server.
