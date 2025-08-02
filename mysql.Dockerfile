FROM mysql:8.0

# Copy initialization scripts
COPY init-db/*.sql /docker-entrypoint-initdb.d/

# Set default environment variables
ENV MYSQL_ROOT_PASSWORD=docconnect_root_2024
ENV MYSQL_DATABASE=docconnect  
ENV MYSQL_USER=docconnect_user
ENV MYSQL_PASSWORD=docconnect_pass_2024

# Expose port
EXPOSE 3306

# Health check
HEALTHCHECK --interval=10s --timeout=20s --retries=10 --start-period=40s \
  CMD mysqladmin ping -h localhost || exit 1
