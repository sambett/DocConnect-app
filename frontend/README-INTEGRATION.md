# DocConnect Frontend-Backend Integration

This document provides information about how the frontend has been structured to connect with the backend.

## Service Architecture

We've created the following services to handle communication with the backend:

1. **API Service** (`api.service.ts`)
   - Generic service for HTTP requests
   - Handles authorization headers

2. **Auth Service** (`auth.service.ts`)
   - Manages user authentication
   - Stores and retrieves tokens
   - Handles login/logout/registration

3. **Professor Service** (`professor.service.ts`)
   - Manages professor-specific operations
   - Updates status, creates announcements, fetches status history

4. **Student Service** (`student.service.ts`)
   - Manages student-specific operations
   - Handles favorites, notifications

## Authentication Flow

1. User logs in through the login component
2. Auth service stores the token
3. Auth interceptor adds the token to all HTTP requests
4. Protected routes use the auth guard to prevent unauthorized access

## Dashboard Implementation

### Professor Dashboard
- Shows current status
- Allows changing status (Available, Busy, Away, In Meeting)
- Shows how many students are waiting for notification
- Lets professors create announcements

### Student Dashboard
- Lists all professors with their current status
- Allows filtering by favorites and available professors
- Allows setting notification requests for when professors become available
- Shows professor details including announcements

## Next Steps to Complete Integration

1. **Update Environment Configuration**
   - Edit `environments/environment.ts` to include your Firebase configuration and backend URL

2. **Implement Register Component**
   - Create a registration form similar to the login form

3. **Test Authentication Integration**
   - Verify tokens are properly sent to and validated by the backend

4. **Implement Real-time Updates (Optional)**
   - For real-time status updates, consider WebSocket integration
   - Implement a notification service using Firebase Cloud Messaging

5. **Handle Error Cases**
   - Improve error handling throughout the application
   - Add loading indicators

## Environment Setup

We've added Tailwind CSS for styling. To make sure everything works correctly:

1. Install dependencies:
   ```
   npm install
   ```

2. Start the development server:
   ```
   npm start
   ```

3. Build for production:
   ```
   npm run build
   ```

## API Endpoints

The application interacts with the following endpoints:

- **Authentication**
  - `/auth/login`: User login
  - `/auth/register`: User registration

- **Professors**
  - `/professors`: Get all professors
  - `/professors/{id}`: Get professor by ID
  - `/professors/{id}/status`: Update professor status
  - `/professors/{id}/announcements`: Create an announcement

- **Students**
  - `/students/{id}/favorites`: Get favorite professors
  - `/students/{id}/favorites`: Add/remove favorite professor
  - `/students/{id}/notifications`: Get/set notification preferences

## Troubleshooting

- If HTTP requests are failing, check browser console for CORS issues
- If authentication isn't working, check the token format in the request headers
- For styling issues, make sure Tailwind CSS is properly configured
