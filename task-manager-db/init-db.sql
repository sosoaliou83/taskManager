-- Drop tables if they exist (for fresh start)
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'VALIDATOR', 'ADMIN'))
);

-- Create tasks table
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    due_date TIMESTAMP NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50),
    last_updated_on TIMESTAMP
);

-- Insert default users
INSERT INTO users (username, password, role) VALUES
('admin', 'admin', 'ADMIN'),
('validator', 'validator', 'VALIDATOR'),
('john', 'john', 'USER');

-- Insert default tasks
INSERT INTO tasks (title, description, priority, due_date, completed, deleted, created_by, created_date, updated_by, last_updated_on) VALUES
('Initialize Backend', 'Set up Spring Boot structure and dependencies', 'HIGH', '2025-06-25 00:00:00', false, false, 'admin', '2025-06-13 09:00:00', 'admin', '2025-06-13 09:40:34'),
('Design Database', 'Define PostgreSQL schema and relationships', 'HIGH', '2025-06-23 00:00:00', false, false, 'admin', '2025-06-14 10:15:00', 'admin', '2025-06-14 11:10:22'),
('Build Frontend UI', 'Create Angular components for task display', 'MEDIUM', '2025-06-26 00:00:00', false, false, 'john', '2025-06-15 08:30:00', 'john', '2025-06-15 12:00:00'),
('Setup Docker', 'Containerize backend and database', 'HIGH', '2025-06-22 00:00:00', false, false, 'validator', '2025-06-16 14:00:00', 'validator', '2025-06-16 14:40:40'),
('Implement Auth', 'Add role-based access control', 'HIGH', '2025-06-24 00:00:00', false, false, 'admin', '2025-06-17 11:20:00', 'validator', '2025-06-17 12:30:00'),
('Write Unit Tests', 'Test service and controller layers', 'MEDIUM', '2025-06-27 00:00:00', false, false, 'john', '2025-06-17 09:00:00', 'admin', '2025-06-17 17:45:00'),
('Fix Bugs', 'Resolve priority bugs reported by QA', 'HIGH', '2025-06-21 00:00:00', true, false, 'validator', '2025-06-18 13:00:00', 'john', '2025-06-18 13:50:00'),
('Deploy Staging', 'Push app to staging environment', 'MEDIUM', '2025-06-20 00:00:00', true, false, 'admin', '2025-06-19 16:30:00', 'validator', '2025-06-19 17:10:10'),
('Review Code', 'Conduct code review and suggestions', 'LOW', '2025-06-25 00:00:00', false, true, 'john', '2025-06-15 08:00:00', 'validator', '2025-06-15 09:30:00'),
('Update Docs', 'Document all APIs and setup steps', 'LOW', '2025-06-30 00:00:00', false, false, 'validator', '2025-06-18 10:45:00', 'admin', '2025-06-18 11:30:00');