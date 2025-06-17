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
    due_date DATE NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_by VARCHAR(50) NOT NULL,
    CONSTRAINT fk_created_by FOREIGN KEY (created_by) REFERENCES users(username)
);

-- Insert a sample user
INSERT INTO users (username, password, role) VALUES
('admin', 'adminpass', 'ADMIN'),
('john', 'johnpass', 'USER');

-- Insert a sample task
INSERT INTO tasks (title, description, priority, due_date, completed, created_by) VALUES
('First Task', 'This is a sample task', 'HIGH', CURRENT_DATE + 5, false, 'john');