-- DocConnect Database Initialization
-- This script runs when the MySQL container starts for the first time

CREATE DATABASE IF NOT EXISTS docconnect;
USE docconnect;

-- Grant privileges to the application user
GRANT ALL PRIVILEGES ON docconnect.* TO 'docconnect_user'@'%';
FLUSH PRIVILEGES;

-- Tables will be created automatically by Spring Boot JPA
-- This is just for database and user setup
