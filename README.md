
# Bug Tracker Backend - Handover Documentation

## 1. **Project Overview**

This is a Spring Boot-based Bug Tracking backend API designed to manage bugs, users, and comments with role-based security and JWT authentication. It exposes RESTful endpoints enabling the frontend to:

- Register and login users with JWT security.
- CRUD operations on Users, Bugs, and Comments.
- Role-based access control (ADMIN vs DEVELOPER).
- Filtering and retrieval of bugs based on assignment and reporting.
- Audit and traceability included via entity relationships.

## 2. **Key Features**

- **User Management**
  - Admins can create, read, update, and delete users.
  - Users have roles: ADMIN or DEVELOPER.
  - Passwords are securely encoded.
  - Users can only update their own info unless admin.

- **Bug Management**
  - Bugs have title, description, status, priority.
  - Enums for status: OPEN, IN_PROGRESS, RESOLVED, CLOSED.
  - Enums for priority: LOW, MEDIUM, HIGH, CRITICAL.
  - `assignee`: user assigned to fix the bug (Admin-only can assign).
  - `reportedBy`: user who reported the bug (tracked on creation).
  - Users can fetch bugs assigned to them or reported by them.

- **Comment Management**
  - Comments belong to Bugs and Users.
  - Only owners or admins can update/delete comments.
  - Username of comment author is included in API responses.

- **Security**
  - JWT-based authentication.
  - `@PreAuthorize` enforces method access based on roles and ownership.
  - Global exception handler for consistent error responses.

- **Data Integrity**
  - PostgreSQL database with ENUM types for status & priority.
  - Foreign key constraints protect relational integrity.

---

## 3. **Database Models**

### Roles
| Column | Type       | Description               |
|--------|------------|---------------------------|
| id     | SERIAL PK  | Role identifier           |
| name   | VARCHAR    | Role name (e.g., ADMIN)   |

### Users
| Column    | Type       | Description                      |
|-----------|------------|---------------------------------|
| id        | SERIAL PK  | User identifier                 |
| username  | VARCHAR    | Unique username                 |
| password  | VARCHAR    | Hashed password                 |
| email     | VARCHAR    | Unique email                   |
| role_id   | INTEGER FK | FK to roles table               |

### Bugs
| Column         | Type            | Description                         |
|----------------|-----------------|-----------------------------------|
| id             | SERIAL PK       | Bug identifier                    |
| title          | VARCHAR         | Bug title                        |
| description    | TEXT            | Bug description                  |
| status         | status_enum     | Bug status (OPEN, etc.)           |
| priority       | priority_enum   | Bug priority                     |
| created_at     | TIMESTAMP       | Creation timestamp               |
| updated_at     | TIMESTAMP       | Last update timestamp            |
| assignee_id    | INTEGER FK      | Assigned user (nullable)         |
| reported_by_id | INTEGER FK      | User who reported (not null)     |

### Comments
| Column     | Type        | Description                        |
|------------|-------------|----------------------------------|
| id         | SERIAL PK   | Comment identifier               |
| content    | TEXT        | Comment text                    |
| created_at | TIMESTAMP   | Created timestamp               |
| bug_id     | INTEGER FK  | Related bug                    |
| user_id    | INTEGER FK  | Comment author                 |

## 4. **API Endpoints**

### Authentication
- **POST /api/auth/register** — Register a new user
- **POST /api/auth/login** — Login and get JWT token

### Users (`/api/users`) — Secured (ADMIN only for list/delete/create, update self or admin)

| Method | Endpoint        | Description             | Notes                          |
|--------|-----------------|-------------------------|--------------------------------|
| GET    | `/api/users`     | List all users          | ADMIN only                     |
| GET    | `/api/users/{id}`| Get user by ID          | ADMIN only                     |
| POST   | `/api/users`     | Create user             | ADMIN only                     |
| PUT    | `/api/users/{id}`| Update user info        | User themself or ADMIN         |
| DELETE | `/api/users/{id}`| Delete user             | ADMIN only                    |

### Bugs (`/api/bugs`) — Secured (ADMIN and DEVELOPER, with assignment admins-only)

| Method | Endpoint               | Description                          | Notes                                |
|--------|------------------------|------------------------------------|-------------------------------------|
| GET    | `/api/bugs`            | List all bugs                      | ADMIN/DEVELOPER                     |
| GET    | `/api/bugs/{id}`       | Get bug by ID                     | ADMIN/DEVELOPER                     |
| POST   | `/api/bugs`            | Create a new bug                  | ADMIN/DEVELOPER; only Admin can assign assignee |
| PUT    | `/api/bugs/{id}`       | Update bug                       | ADMIN/DEVELOPER; only Admin can assign assignee |
| DELETE | `/api/bugs/{id}`       | Delete bug                      | ADMIN only                         |
| GET    | `/api/bugs/assigned`   | Get bugs assigned to current user | User or ADMIN                     |
| GET    | `/api/bugs/reported`   | Get bugs reported by current user | User or ADMIN                     |

### Comments (`/api/comments`) — Secured (ADMIN and DEVELOPER)

| Method | Endpoint                  | Description                   | Notes                         |
|--------|---------------------------|-------------------------------|-------------------------------|
| GET    | `/api/comments/bug/{bugId}`| List comments for a bug       | ADMIN/DEVELOPER               |
| POST   | `/api/comments`           | Create comment                | ADMIN/DEVELOPER               |
| PUT    | `/api/comments/{id}`      | Update comment               | Owner or ADMIN               |
| DELETE | `/api/comments/{id}`      | Delete comment               | Owner or ADMIN               |

## 5. **Security & Access Control**

- JWT tokens are required for all API endpoints except `/api/auth/register` and `/api/auth/login`.
- Role `ADMIN` and `DEVELOPER` distinguish API access.
- Only ADMIN can:
  - Create users, delete users, delete bugs.
  - Assign assignees to bugs.
  - Delete comments.
- Developers/Users can:
  - Create/update bugs (excluding assignment).
  - View bugs assigned to or reported by them.
  - Update/delete their own comments.
- `@PreAuthorize` annotations and `AuthenticationFacade` service enforce these rules.

## 6. **Entities & DTOs**

- Entities are not exposed directly via APIs — all communication is via DTOs.
- User DTOs:
  - **UserCreateRequest**: for user creation (includes password).
  - **UserUpdateRequest**: for updates (password optional).
  - **UserResponse**: for safely returning user info (excludes password, includes id and role).
- Bug DTOs:
  - Includes reporter's username and assignee info.
- Comment DTOs:
  - Includes author username.

## 7. **Testing Instructions**

- Use the provided **Postman Collection JSON** to import all endpoints.
- Use `/api/auth/login` to fetch JWT tokens.
- Set the JWT token in Postman `Authorization` headers as `Bearer {{token}}`.
- Execute user, bug, and comment APIs according to roles.
- Review example request bodies and parameters in the collection.
- Errors are returned in consistent JSON format handled by global exception handler.

## 8. **To Note for Frontend Team**

- Always send JWT token in the `Authorization` header for secured endpoints.
- Expect consistent error messages in JSON with status codes like 400, 401, 403, 404.
- Use proper DTOs for requests and parse DTO responses.
- Manage user roles to toggle frontend access/features.
- Use `/api/bugs/assigned` and `/api/bugs/reported` endpoints to show personalized bug lists.
- Comments include commenter username for display.

## 9. **Further Recommendations**

- Frontend can implement filtering and pagination delegates; backend currently supports basic multi-criteria filtering in bugs.
- Frontend should handle role-based views based on JWT token decoded info or by user role API call.
- Testing with Postman before frontend integration is highly recommended.

*End of Bug Tracker Backend Handover*
