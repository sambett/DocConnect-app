# DocConnect

Real-time professor availability tracking application with student notifications.

## Project Overview

DocConnect is a web application that allows students to track professor availability in real-time and receive notifications when professors become available. The application is built using Angular for the frontend and Spring Boot for the backend.

## Features

- User Authentication with JWT tokens
- Professor Dashboard with status management
- Student Dashboard with professor search and filtering
- Real-time status updates
- User roles (Students and Professors)
- Responsive design

## Prerequisites

- Java 17 or higher
- Node.js and npm
- MySQL server

## Setup Instructions

### Backend Setup

1. Open a terminal and navigate to the backend directory:
   ```
   cd C:/Users/SelmaB/Desktop/DocConnect/backend
   ```

2. Build the project using Maven:
   ```
   mvn clean install
   ```

3. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

### Frontend Setup

1. Open a new terminal and navigate to the frontend directory:
   ```
   cd C:/Users/SelmaB/Desktop/DocConnect/frontend/docconnect-app
   ```

2. Install the required npm packages:
   ```
   npm install
   ```

3. Start the Angular development server:
   ```
   npm start
   ```

4. Access the application at [http://localhost:4200](http://localhost:4200)

## Application Structure

### Backend (Spring Boot)

- Controllers: Handle HTTP requests
- Services: Contain business logic
- Models: Define data structures
- Repositories: Interface with the database

### Frontend (Angular)

- Components: UI elements and views
- Services: Data communication and business logic
- Models: Data type definitions
- Guards: Route protection

## Authentication System

DocConnect uses a JWT-based authentication system:

1. User registers or logs in with email and password
2. Backend validates credentials and generates a JWT token
3. Frontend stores the token and includes it in subsequent requests
4. Token is validated on each request to protect routes

## Database

The application uses MySQL for data storage with the following tables:

- Users: Basic user information and authentication
- Professors: Additional professor-specific information
- StatusHistory: Professor status changes over time
- Announcements: Professor announcements
- Favorites: Student's favorite professors
- Notifications: Student notification preferences

## Contact

For support or questions, please contact the development team.

Happy coding!