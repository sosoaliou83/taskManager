# Use official base image
FROM postgres:15

# Optional: Set environment variables (you can still override via docker-compose)
ENV POSTGRES_DB=task_manager_db
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# Optional: Copy init scripts (SQL or .sh) into the container
# They run automatically on container first startup
# Place your SQL files in the same folder as this Dockerfile
COPY init-db.sql /docker-entrypoint-initdb.d/

# Optional: Expose default PostgreSQL port
EXPOSE 5432