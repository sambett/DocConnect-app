# DocConnect - Professor Availability System

A simple appointment and availability tracking system that allows students to monitor professor availability and get notified when professors become free. Built with Spring Boot, MySQL, and Docker for easy deployment.

## 🎯 What is DocConnect?

DocConnect is a basic appointment system where:
- **Professors** can update their availability status (Available, Busy, Away, In Meeting)
- **Students** can browse professors, set notifications, and mark favorites
- **Real-time tracking** of professor availability with automatic updates

## 🏗️ Tech Stack

- **Backend**: Spring Boot 3.2.3 with Spring Security
- **Database**: MySQL 8.0 with JPA/Hibernate
- **Authentication**: JWT tokens
- **Frontend**: Angular with Bootstrap (basic implementation)
- **Deployment**: Docker & Docker Compose
- 
### 🎯 Why MVC Works Best for DocConnect:

1. **Clear Separation**: Business logic (Model) stays separate from request handling (Controller)
2. **Scalability**: Easy to add new controllers or modify models without affecting other layers
3. **Testability**: Each layer can be unit tested independently
4. **REST API Design**: Controllers naturally map to REST endpoints
5. **Team Development**: Frontend and backend teams can work independently
6. **Maintenance**: Bug fixes and features can be isolated to specific layers

This pattern is particularly effective for **web applications with database persistence** where you need clean API design and future extensibility.

## 📊 Database Schema

The system uses MySQL with the following main entities managed by JPA/Hibernate:

- **`users`** - Base user information (students and professors)
- **`professors`** - Professor-specific data (department, office, working hours, status)
- **`notifications`** - Student notification preferences for professor availability
- **`favorites`** - Student favorite professors
- **`announcements`** - Professor announcements to students
- **`status_history`** - Track professor status changes over time

## 🚀 API Controllers

The Spring Boot backend provides REST endpoints through these controllers:

- **`AuthController`** - Registration, login, JWT token management
- **`ProfessorController`** - Professor profile and status management
- **`StudentController`** - Student dashboard, favorites, notifications
- **`NotificationController`** - Notification system management
- **`AnnouncementController`** - Professor announcements

## 🐳 Running with Docker (Recommended)

### Option 1: Pull from DockerHub (Fastest)

```bash
# Download the compose file
curl -O https://raw.githubusercontent.com/sambett/DocConnect-app/main/docker-compose.public.yml

# Run the application
docker-compose -f docker-compose.public.yml up -d
```

### Option 2: Clone and Run

```bash
# Clone the repository
git clone https://github.com/sambett/DocConnect-app.git
cd DocConnect-app

# Run with Docker Compose
docker-compose -f docker-compose.public.yml up -d
```

**Access the application**: http://localhost:3000

The setup includes:
- **Frontend container** (`sambett1/docconnect-frontend`) - Angular app served via Nginx
- **Backend container** (`sambett1/docconnect-backend`) - Spring Boot API
- **MySQL container** - Database initialized with scripts from `init-db/`

## 🔧 Development Setup

For local development without Docker:

```bash
# Backend
cd backend
./mvnw spring-boot:run

# Frontend  
cd frontend/docconnect-app
npm install
npm start

# Database
docker run -d -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0
```

## 📦 DockerHub Images

- Backend: [`sambett1/docconnect-backend`](https://hub.docker.com/r/sambett1/docconnect-backend)
- Frontend: [`sambett1/docconnect-frontend`](https://hub.docker.com/r/sambett1/docconnect-frontend)

## 🔒 Authentication Flow

1. Users register as either STUDENT or PROFESSOR
2. JWT tokens are issued upon successful login
3. Spring Security validates tokens for protected endpoints
4. Role-based access control for different features

## 📱 Frontend

The frontend is a basic Angular application with Bootstrap styling, developed quickly for demonstration purposes. It provides:
- User registration and login forms
- Student dashboard for browsing professors
- Professor dashboard for status management
- Basic responsive design

## 🚀 Future Work

- **Real-time WebSockets** for instant status updates
- **Email notifications** when professors become available  
- **Calendar integration** for appointment scheduling
- **Mobile app** development
- **Advanced analytics** for professor availability patterns

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.
