-- Create ENUM types for bug status and priority to ensure data integrity.
CREATE TYPE status_enum AS ENUM ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED');
CREATE TYPE priority_enum AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');

-- Table for user roles (e.g., ADMIN, DEVELOPER)
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Table for application users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT fk_role
        FOREIGN KEY(role_id)
        REFERENCES roles(id)
);

-- Table for tracking bugs
CREATE TABLE bugs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status status_enum DEFAULT 'OPEN',
    priority priority_enum DEFAULT 'MEDIUM',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    assignee_id INTEGER,
    CONSTRAINT fk_assignee
        FOREIGN KEY(assignee_id)
        REFERENCES users(id)
);

-- Table for comments on bugs
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    bug_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_bug
        FOREIGN KEY(bug_id)
        REFERENCES bugs(id)
        ON DELETE CASCADE, -- If a bug is deleted, its comments are also deleted.
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
);

-- Table for logging activity on bugs
CREATE TABLE activity_log (
    id SERIAL PRIMARY KEY,
    details TEXT NOT NULL,
    "timestamp" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    bug_id INTEGER NOT NULL,
    CONSTRAINT fk_bug_activity
        FOREIGN KEY(bug_id)
        REFERENCES bugs(id)
        ON DELETE CASCADE -- If a bug is deleted, its activity log is also deleted.
);
