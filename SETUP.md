# DocConnect Setup Guide

This guide will help you set up and run the DocConnect application properly.

## Frontend Setup

The frontend is an Angular application with Firebase authentication integration.

### Prerequisites
- Node.js (v14+)
- npm (v6+)

### Installation
1. Navigate to the frontend directory:
   ```
   cd frontend/docconnect-ui
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Start the application:
   ```
   npm start
   ```
   The application will be available at http://localhost:4200

### Configuration
- Environment configuration is in `src/environments/environment.ts`
- Firebase configuration is already set up with your project ID
- Mock data mode can be enabled/disabled with `useMockData` property

## Backend Setup

The backend is a Spring Boot application with MySQL/H2 database.

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL (optional, H2 in-memory DB is configured by default)

### Installation
1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Start the application:
   ```
   mvn spring-boot:run
   ```
   The API will be available at http://localhost:8080/api

### Database Configuration
- The application is currently configured to use H2 in-memory database
- To use MySQL, update `application.properties`:
  ```properties
  # MySQL configuration
  spring.datasource.url=jdbc:mysql://localhost:3306/docconnect?createDatabaseIfNotExist=true
  spring.datasource.username=root
  spring.datasource.password=YOUR_PASSWORD
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
  ```

## Authentication

### Firebase Authentication
1. The app uses Firebase for authentication
2. Firebase configuration is set up in `environment.ts`
3. For backend Firebase verification:
   - Go to Firebase Console > Project Settings > Service Accounts
   - Generate a new private key
   - Place the JSON file at `backend/src/main/resources/firebase-service-account.json`
   - Set `firebase.enabled=true` in `application.properties`

### Mock Authentication
For development, mock authentication can be used:
1. Set `useMockData: true` in `environment.ts`
2. Use these demo credentials:
   - Professor: email=`professor@example.com`, password=`password123`
   - Student: email=`student@example.com`, password=`password123`

## Troubleshooting

### MySQL Connection Issues
If you see "Communications link failure" errors:
- Verify MySQL is running: `service mysql status`
- Check credentials in `application.properties`
- Make sure database exists: `CREATE DATABASE docconnect;`

### Firebase Authentication Issues
If Firebase authentication fails:
- Check your Firebase project is properly configured
- Verify API key and project ID in `environment.ts`
- Make sure Email/Password authentication is enabled in Firebase Console

### Frontend-Backend Communication
If the frontend can't connect to the backend:
- Ensure backend is running at http://localhost:8080
- Check that CORS is properly configured
- Verify proxying is set up correctly in `proxy.conf.json`
