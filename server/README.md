# Devblog API

Devblog API is the backend for the Devblog application. It provides authentication and post management with JWT-based security and PostgreSQL persistence. The domain model also includes follows and likes for user interactions.

## Highlights

- Stateless authentication with JWT
- PostgreSQL-backed persistence via Spring Data JPA
- Clean controller-service-repository layering
- DTO-based request and response handling

## Tech Stack

- **Framework:** Spring Boot 4.0.6
- **Language:** Java 25
- **Build Tool:** Gradle
- **Database:** PostgreSQL
- **Security:** Spring Security with JWT authentication

## Getting Started

### Prerequisites

- Java 25
- PostgreSQL
- A JWT signing secret with at least 32 bytes of entropy

### Environment Variables

The application reads configuration from environment variables and also imports a local `.env` file if present.

Create a `.env` file in the project root with:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<database>
SPRING_DATASOURCE_USERNAME=<database-user>
SPRING_DATASOURCE_PASSWORD=<database-password>
JWT_SECRET=<at-least-32-byte-signing-secret>
```

An `.env.example` file is included as a template.

### Run the Application

```bash
./gradlew bootRun
```

The API starts on `http://localhost:8080` by default.

## API Overview

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`

### Posts

- `POST /api/posts`
- `PUT /api/posts/{postId}`
- `DELETE /api/posts/{postId}`
- `GET /api/posts/search?query=...&page=...&size=...`

## Security

- `/api/auth/**` is public
- All other routes require a valid JWT in the `Authorization: Bearer <token>` header
- The application is stateless and does not use server-side sessions

## Project Structure

- `src/main/java/com/devblog/server/controller` - HTTP endpoints
- `src/main/java/com/devblog/server/service` - business logic
- `src/main/java/com/devblog/server/repository` - data access interfaces
- `src/main/java/com/devblog/server/security` - JWT filter and Spring Security setup
- `src/main/java/com/devblog/server/model` - JPA entities
- `src/main/java/com/devblog/server/dto` - request and response payloads
