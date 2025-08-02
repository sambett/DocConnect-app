# 🐳 DocConnect - Docker Setup

This guide shows you how to run DocConnect using Docker. The entire application stack (database, backend, frontend) will run in containers.

## 🚀 Quick Start

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd DocConnect
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env file with your preferred settings (optional)
   ```

3. **Start the application**
   ```bash
   docker-compose up -d
   ```

4. **Access the application**
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:8080/api
   - **Database**: localhost:3307 (from host machine)

## 🛠️ Available Commands

### Start all services
```bash
docker-compose up -d
```

### View logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f docconnect-backend
docker-compose logs -f docconnect-frontend
docker-compose logs -f docconnect-database
```

### Stop all services
```bash
docker-compose down
```

### Stop and remove all data
```bash
docker-compose down -v
```

### Rebuild containers (after code changes)
```bash
# Rebuild all
docker-compose up --build -d

# Rebuild specific service
docker-compose up --build docconnect-backend -d
```

## 🏗️ Architecture

```
┌─────────────────┐         ┌──────────────┐         ┌─────────────┐
│ Nginx           │         │ Spring Boot  │         │ MySQL       │
│ (Frontend)      │◄──────►│ (Backend)    │◄──────►│ (Database)  │
│ Port 3000       │  HTTP   │ Port 8080    │  JDBC   │ Port 3307   │
└─────────────────┘         └──────────────┘         └─────────────┘
```

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the root directory to customize settings:

```env
# Database
DB_ROOT_PASSWORD=your_root_password
DB_NAME=docconnect
DB_USERNAME=docconnect_user
DB_PASSWORD=your_password

# Application
JWT_SECRET=your-jwt-secret-key
SPRING_PROFILES_ACTIVE=docker
SHOW_SQL=false
```

### Port Configuration

Default ports:
- Frontend: `3000`
- Backend: `8080`
- Database: `3307` (mapped from container's 3306)

To change ports, modify the `ports` section in `docker-compose.yml`.

## 🗄️ Database

### Data Persistence
Database data is stored in a Docker volume named `mysql_data`. This means your data persists even when containers are stopped/restarted.

### Connecting from Host
```bash
mysql -h localhost -P 3307 -u docconnect_user -p
```

### Database Initialization
The `init-db/init.sql` file runs automatically when the database container starts for the first time.

## 🐛 Troubleshooting

### Container won't start
```bash
# Check container status
docker-compose ps

# View detailed logs
docker-compose logs <service-name>
```

### Port conflicts
If you get port conflicts, modify the ports in `docker-compose.yml`:
```yaml
ports:
  - "3001:80"  # Change 3000 to 3001
```

### Database connection issues
```bash
# Restart database
docker-compose restart docconnect-database

# Check database logs
docker-compose logs docconnect-database
```

### Clean slate restart
```bash
# Stop everything and remove volumes
docker-compose down -v

# Remove all DocConnect images
docker images | grep docconnect | awk '{print $3}' | xargs docker rmi

# Start fresh
docker-compose up --build -d
```

## 🔄 Development Workflow

### Making Backend Changes
1. Make your code changes
2. Rebuild the backend container:
   ```bash
   docker-compose up --build docconnect-backend -d
   ```

### Making Frontend Changes
1. Make your code changes
2. Rebuild the frontend container:
   ```bash
   docker-compose up --build docconnect-frontend -d
   ```

### Database Schema Changes
The database schema is managed by Spring Boot JPA. Changes will be applied automatically when the backend starts.

## 📊 Health Checks

All services include health checks:
- **Database**: MySQL ping
- **Backend**: Spring Boot Actuator health endpoint
- **Frontend**: Nginx health endpoint

Check health status:
```bash
docker-compose ps
```

## 🔒 Security Notes

1. **Change default passwords** in production
2. **Use strong JWT secrets** in production
3. **Don't expose database port** in production
4. **Use HTTPS** in production
5. **Keep Docker images updated**

## 🌐 Production Deployment

For production deployment, consider:
1. Using managed database services (AWS RDS, etc.)
2. Using container orchestration (Kubernetes, Docker Swarm)
3. Setting up reverse proxy with SSL/TLS
4. Implementing monitoring and logging
5. Setting up CI/CD pipelines

## 🤝 Contributing

When contributing:
1. Test your changes with Docker locally
2. Update documentation if needed
3. Ensure health checks pass
4. Follow security best practices

---

## 📞 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review container logs
3. Create an issue with logs and configuration details
