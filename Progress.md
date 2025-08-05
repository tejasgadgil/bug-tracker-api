# Bug Tracker Application — Completed Work & Handover Documentation

## Overview
This application is a Bug Tracker backend built with Spring Boot, Spring Security, Spring Data JPA, and JWT-based authentication. It exposes REST APIs for user management, authentication, and bug tracking.

---

## 1. Project Structure & Packages

- **Model**: JPA entities such as `User`, `Role`, `Bug`, `Comment`, and `ActivityLog`.
- **Repository**: Spring Data JPA repositories for CRUD operations on entities.
- **Service**: Business logic layer, notably `UserService` managing user registration and loading user details.
- **Security**: Spring Security configuration, JWT utilities, and filters.
- **Controller**: REST controllers (e.g., `AuthController`) to expose authentication endpoints.
- **Payload**: DTO classes used for request and response payloads in APIs.

---

## 2. Security Layer

- **UserDetails & UserDetailsService**  
  Implemented a custom `UserDetailsImpl` to wrap users for Spring Security.  
  `UserService` implements `UserDetailsService` to load user info from the database.

- **JWT Authentication**
    - `JwtTokenUtil` handles generation and validation of JWT tokens using HS512 algorithm, requiring a 512-bit secret key.
    - `JwtAuthTokenFilter` extracts and validates JWT token on each request, sets SecurityContext accordingly.
    - `SecurityConfig` class uses modern Spring Security 6 configuration, defines beans with constructor injection to avoid circular dependencies.
    - Configured stateless session management and public access to `/api/auth/**` for login and registration.

---

## 3. User Management

- `UserService` provides:
    - Registration method with validation to check unique username and email, encoding passwords, and assigning roles.
    - User existence check methods for username/email.
    - Loading users by username for authentication.

- `Role` entity/table supports roles like ADMIN and DEVELOPER.

- Implemented a `DataInitializer` Spring component that automatically inserts "ADMIN" and "DEVELOPER" roles into the database on application startup if they don’t already exist.

---

## 4. Database & Schema

- PostgreSQL is used as the database.
- Manual schema SQL for:
    - Enums `status_enum` and `priority_enum` for bug status and priority.
    - Tables: `roles`, `users`, `bugs`, `comments`, `activity_log` with appropriate foreign key constraints and cascading actions.
- Application configured to connect to `bug-tracker` database.
- You have to create the `bug-tracker` database manually in PostgreSQL before running the app.

---

## 5. Authentication Endpoints

- `/api/auth/register` POST: register new users.  
  Requires DTO with username, email, password, and roleName.  
  Returns success or error messages.

- `/api/auth/login` POST: authenticates user, returns JWT token and user info.  
  Requires DTO with username and password.

- JWT token must be included as Bearer token in Authorization header for protected API calls.

---

## 6. Testing Instructions

- Use Postman or any REST client to test the above endpoints.
- Set `Content-Type: application/json` in requests.
- For `/api/auth/login`, copy the JWT token and include it as `Authorization: Bearer <token>` for accessing secured endpoints.
- Ensure JWT secret configured in `application.properties` is at least 64 characters long (512 bits) for HS512.
- Use Postman environment variables to store and reuse JWT tokens easily.

---

## 7. Known Issues & Improvement Areas

- Validation annotations can be added to DTOs for better input validation.
- Refresh token mechanism is not implemented yet.
- Role-based access restrictions are not fully fleshed out (can add `@PreAuthorize` or similar on controllers).
- Better error handling and logging can be added.
- Integration and unit tests should be added for all layers.
- Consider using migration tools like Flyway or Liquibase for database schema management.
- Frontend integration and API documentation (e.g., Swagger/OpenAPI) can be added.

---

## 8. How to Run

- Create PostgreSQL database `bug-tracker`.
- Ensure roles exist in DB (`ADMIN`, `DEVELOPER`) - will auto-create on startup.
- Configure JDBC URL, username, password, and JWT secret in `application.properties`.
- Run the Spring Boot app as a Java application or with `mvn spring-boot:run`.
- Use Postman to test APIs as described.
- Monitor logs for troubleshooting.

---

## 9. Important Files

- `SecurityConfig.java`: Spring Security setup.
- `JwtAuthTokenFilter.java`: JWT token validation filter.
- `JwtTokenUtil.java`: JWT utility for token creation/validation.
- `UserService.java`: Core user management and authentication service.
- `DataInitializer.java`: Preloads role data.
- `Role.java` and `User.java`: Core entities related to authentication.
- SQL schema scripts for manual database setup.
- DTOs and Controller for authentication.

---

