# Devblog API

This is the backend REST API for the Devblog application, built with Java and Spring Boot.

## Tech Stack

- **Framework**: [Spring Boot 3](https://spring.io/projects/spring-boot)
- **Language**: Java 17+
- **Security**: Spring Security with JWT Authentication
- **Build Tool**: Gradle

## Development Setup

1. Ensure you have Java 17+ installed.
2. Clone the repository and navigate to the `server` folder.
3. Set the required environment variables before starting the app, or create a local `.env` file from `.env.example`:

   ```bash
   export SPRING_DATASOURCE_URL='jdbc:postgresql://<host>:<port>/<database>'
   export SPRING_DATASOURCE_USERNAME='<database-user>'
   export SPRING_DATASOURCE_PASSWORD='<database-password>'
   export JWT_SECRET='<at-least-32-byte-signing-secret>'
   ```

   The app also imports a local `.env` file automatically if present.

4. Start the application using Gradle:

   ```bash
   ./gradlew bootRun
   ```

5. By default, the API will run on `http://localhost:8080`.

## Architecture Overview

The backend uses a standard controller-service-repository layered architecture:
- `controllers/` - REST API endpoints handling HTTP requests and responses.
- `services/` - Core business logic.
- `repositories/` - Data access interfaces.
- `security/` - JWT filters, utilities, and Spring Security configuration.
- `models/` - JPA Entities mapping to database tables.
- `dto/` - Data Transfer Objects for API request/response payloads.
