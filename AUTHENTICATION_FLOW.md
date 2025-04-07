# DocConnect Authentication Flow

## Overview

DocConnect uses Firebase for authentication and MySQL for storing user data. This document explains the authentication flow.

## Authentication Process

1. **User Registration**:
   - User enters registration details (email, password, name, role)
   - Firebase creates the authentication account
   - Backend stores the user details in MySQL with Firebase UID
   - Now the user exists in both Firebase and MySQL

2. **User Login**:
   - User enters email and password
   - Firebase validates credentials
   - Upon successful login, backend loads user details from MySQL
   - User is redirected to appropriate dashboard (student or professor)

## Implementation Details

### Frontend Components (docconnect-ui)
- `firebase.config.ts`: Contains the Firebase initialization
- `auth.service.ts`: Handles Firebase authentication and communicates with backend
- `auth.interceptor.ts`: Adds authentication token to all API requests

### Backend Components
- `AuthController.java`: API endpoints for registration and login
- `FirebaseAuthService.java`: Verifies Firebase tokens
- `UserService.java`: Handles database operations for users

## How to Run the Application

Use the `start-docconnect.bat` script to start the application. This will:
1. Navigate to the frontend/docconnect-ui directory
2. Start the Angular development server
3. Open the application in your browser

## Troubleshooting

If you encounter authentication issues:
1. Check browser console (F12) for errors
2. Verify Firebase connection in the Network tab
3. Ensure MySQL contains the correct user records
4. Try clearing browser cache or using incognito mode
