# 🎓 DocConnect

<p align="center">
  <a href="https://github.com/sambett/DocConnect">
    <img src="https://img.shields.io/badge/Version-1.2.0-blue" alt="Version">
  </a>
  <a href="https://github.com/sambett/DocConnect/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-green" alt="License">
  </a>
  <a href="https://github.com/sambett/DocConnect/issues">
    <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" alt="PRs Welcome">
  </a>
</p>

<p align="center">
  <strong>A real-time professor availability tracking platform that streamlines student-professor communication</strong>
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#technology-stack">Tech Stack</a> •
  <a href="#getting-started">Getting Started</a> •
  <a href="#screenshots">Screenshots</a> •
  <a href="#architecture">Architecture</a> •
  <a href="#contributing">Contributing</a>
</p>

## 🌟 Overview

DocConnect is a modern web application designed to solve the common problem of student-professor availability gaps in academic institutions. It provides real-time status updates, notification systems, and streamlined communication channels to enhance the academic experience.

## ✨ Features

### For Students
- **🔍 Real-time Status Tracking**: See professors' availability status (Available, Busy, Away, In Meeting) in real-time
- **🔔 Smart Notifications**: Get notified when professors become available
- **⭐ Favorites System**: Star frequently contacted professors for quick access
- **🔎 Advanced Search**: Filter professors by department, name, or availability status
- **📢 Announcement Board**: Stay updated with professor announcements
- **📱 Mobile Responsive**: Access from any device with a seamless experience

### For Professors
- **🎯 Status Management**: Set your availability with one-click status updates
- **👁️ Waiting Students Counter**: See how many students are waiting for you
- **📝 Announcement Creation**: Post important updates for your students
- **📊 Status History**: Track your availability patterns over time
- **⚙️ Profile Management**: Update your contact information and office hours
- **📧 Email Notifications**: Receive alerts when students request meetings

## 🛠️ Technology Stack

### Backend
- **Java 21** with **Spring Boot 3.2.3**
  - Spring Security for authentication and authorization
  - Spring Data JPA for database operations
  - JWT for secure token management
- **MySQL** database for persistent storage
- **Maven** for dependency management and build automation
- **RESTful API** architecture for frontend communication

### Frontend
- **Angular 17** for a robust, component-based architecture
- **TypeScript** for type-safe development
- **Tailwind CSS** for modern, utility-first styling
- **RxJS** for reactive state management
- **Angular Router** for seamless navigation
- **Forms Module** for dynamic form handling

### Development Tools
- **Git** for version control
- **Docker** for containerization (optional)
- **Postman** for API testing
- **VS Code** / **IntelliJ IDEA** as recommended IDEs

## 🚀 Getting Started

### Prerequisites
- Java JDK 21 or higher
- Node.js 18+ and npm 9+
- MySQL 8.0+
- Git
- Maven 3.8+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/sambett/DocConnect.git
   cd DocConnect
   ```

2. **Backend Setup**
   ```bash
   cd backend
   
   # Configure MySQL database
   # Update application.properties with your MySQL credentials
   
   # Build the project
   mvn clean install
   
   # Run the backend server
   mvn spring-boot:run
   ```
   The backend will start on http://localhost:8080

3. **Frontend Setup**
   ```bash
   cd frontend/docconnect-app
   
   # Install dependencies
   npm install
   
   # Start the development server
   npm start
   ```
   The frontend will be available at http://localhost:4200

4. **Database Setup**
   ```sql
   -- Create database
   CREATE DATABASE docconnect;
   
   -- Tables will be automatically created by JPA on first run
   ```

### Configuration

1. **Backend Configuration** (`backend/src/main/resources/application.properties`)
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/docconnect?createDatabaseIfNotExist=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # JWT Configuration
   jwt.secret=your_jwt_secret_key
   jwt.expiration=86400000
   ```

2. **Frontend Configuration** (`frontend/docconnect-app/src/environments/environment.ts`)
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080/api'
   };
   ```

## 📸 Screenshots

### Professor Dashboard
<img src="screenshots/professor-dashboard.png" alt="Professor Dashboard" width="800">

### Student Dashboard
<img src="screenshots/student-dashboard.png" alt="Student Dashboard" width="800">

### Notification System
<img src="screenshots/notifications.png" alt="Notifications" width="800">

## 🏗️ Architecture

### System Architecture
```
┌─────────────────┐         ┌──────────────┐         ┌─────────────┐
│  Angular        │         │ Spring Boot  │         │   MySQL     │
│  Frontend       │◄──────►│  Backend     │◄──────►│  Database   │
│  (Port 4200)    │  REST   │  (Port 8080) │  JPA    │             │
└─────────────────┘  API    └──────────────┘         └─────────────┘
```

### Database Schema
```
users (id, full_name, email, password_hash, role, created_at, updated_at)
professors (id, user_id, department, office_location, working_hours, email_contact, status, created_at, updated_at)
announcements (id, professor_id, content, posted_at)
notifications (id, student_id, professor_id, notification_set_at, notified)
favorites (id, student_id, professor_id, created_at)
status_history (id, professor_id, status, timestamp)
```

## 🔐 API Documentation

### Authentication Endpoints
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate user
- `GET /api/auth/user` - Get current user details
- `POST /api/auth/verify-token` - Verify JWT token

### Professor Endpoints
- `GET /api/professors` - Get all professors
- `GET /api/professors/{id}` - Get professor by ID
- `PUT /api/professors/{id}` - Update professor profile
- `POST /api/professors/{id}/status` - Update professor status
- `GET /api/professors/{id}/status-history` - Get status history
- `DELETE /api/professors/{id}/status-history` - Clear status history

### Announcement Endpoints
- `GET /api/announcements/professor/{professorId}` - Get announcements for a professor
- `POST /api/announcements/professor/{professorId}` - Create new announcement
- `PUT /api/announcements/{id}` - Update announcement
- `DELETE /api/announcements/{id}` - Delete announcement

### Notification Endpoints
- `POST /api/notifications` - Create notification
- `GET /api/notifications/professor/{professorId}` - Get waiting students count
- `GET /api/notifications/student/{studentId}` - Get student notifications
- `DELETE /api/notifications/{id}` - Delete notification
- `DELETE /api/notifications/professor/{professorId}` - Mark all as seen

### Favorite Endpoints
- `GET /api/favorites/student/{studentId}` - Get favorite professors
- `POST /api/favorites` - Add professor to favorites
- `DELETE /api/favorites` - Remove from favorites

## 🧪 Testing

### Backend Testing
```bash
cd backend
mvn test
```

### Frontend Testing
```bash
cd frontend/docconnect-app
npm test
```

## 🚀 Deployment

### Backend Deployment
The Spring Boot application can be deployed as a JAR file:
```bash
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment
Build the Angular application for production:
```bash
ng build --configuration production
```
The build artifacts will be stored in the `dist/` directory.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Selma Bettaieb** - [sambett](https://github.com/sambett)

## 🙏 Acknowledgments

- Thanks to all contributors who have helped shape DocConnect
- Special thanks to the academic community for feedback and suggestions
- Built with ❤️ for students and professors worldwide

## 🔮 Future Enhancements

- [ ] WebSocket for instant real-time updates
- [ ] Email notifications integration
- [ ] Calendar integration for scheduling
- [ ] Mobile applications (iOS/Android)
- [ ] Advanced analytics dashboard
- [ ] Multi-language support
- [ ] Dark mode theme
- [ ] File sharing capabilities

---

<p align="center">Made with ❤️ by the DocConnect Team</p>
