# 🚀 DocConnect - Professor Availability System

A modern full-stack application built with **Spring Boot**, **Angular**, and **MySQL**, fully containerized with **Docker**.

## ⚡ Quick Start (Pull from DockerHub)

**Option 1: Download compose file only**
```bash
curl -O https://raw.githubusercontent.com/sambett1/DocConnect/main/docker-compose.public.yml
docker-compose -f docker-compose.public.yml up -d
```

**Option 2: Clone repository**
```bash
git clone https://github.com/sambett1/DocConnect.git
cd DocConnect
docker-compose -f docker-compose.public.yml up -d
```

**That's it!** Access the app at: http://localhost:3000

## 🎯 What You Get

- **🔐 Complete Authentication System** (Register/Login)
- **👨‍🏫 Professor Dashboard** with real-time status management
- **👨‍🎓 Student Dashboard** with smart notifications
- **🔔 Notification System** with database persistence
- **⭐ Favorites System** for students
- **📱 Responsive Design** with modern UI/UX
- **🐳 Production-Ready Docker Setup**

## 🏗️ Architecture

- **Frontend:** Angular 17+ with Tailwind CSS
- **Backend:** Spring Boot 3.2+ with JWT Authentication
- **Database:** MySQL 8.0 with JPA/Hibernate
- **Deployment:** Docker Compose with multi-stage builds

## 🚀 Features

### For Students:
- Browse professors by department
- Real-time availability status
- Set notifications when professors become available
- Favorite professors for quick access
- Smart filtering and search

### For Professors:
- Update availability status instantly
- Post announcements to students
- Manage office hours and location
- View student engagement analytics

## 🔧 Development

To run locally with hot-reload:

```bash
# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend/docconnect-app
npm start

# Database
docker run -d -p 3307:3306 --name mysql -e MYSQL_ROOT_PASSWORD=root mysql:8.0
```

## 📊 Tech Stack Details

- **Spring Security** with JWT tokens
- **Angular Reactive Forms** with validation
- **RESTful API** design patterns
- **Docker multi-stage builds** for optimization
- **MySQL database** with proper relationships
- **Nginx reverse proxy** for production
- **Health checks** and container orchestration

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

MIT License - feel free to use for learning and commercial projects!

---

**Built with ❤️ by [sambett1](https://github.com/sambett1)**
