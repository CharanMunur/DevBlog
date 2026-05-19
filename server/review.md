# Code Review

## Overall assessment

The codebase has a clear layered structure and the intent is easy to follow. Controllers stay thin, the service layer carries most of the business logic, and the JWT-based security flow is straightforward.

That said, the current implementation is better described as an early-stage prototype than production-ready backend code. The biggest issues are around security, duplicate logic, missing authorization checks, and inconsistent project conventions.

## What is working well

- The controller-service-repository split is easy to understand.
- Authentication is isolated into dedicated security classes.
- DTOs are used instead of exposing entities directly from controllers.
- The codebase is small enough that the flow from request to persistence is readable.

## Main code-quality issues

### 1. Secrets are committed in source control

`src/main/resources/application.properties` contains a live database URL, username, and password. This is the most serious issue in the repository.

- Risk: credential leakage, environment coupling, and accidental production access.
- Impact: anyone with the repo can connect to the database as configured.
- Recommendation: move secrets to environment variables or an external secrets manager immediately.

### 2. Authorization is incomplete on mutating operations

Several service methods trust the caller too much:

- `src/main/java/com/devblog/server/service/PostService.java`
- `src/main/java/com/devblog/server/service/LikeService.java`
- `src/main/java/com/devblog/server/service/UserService.java`

Examples:

- `editPost()` loads the current user but never checks that the post belongs to that user.
- `deletePost()` only checks existence, not ownership.
- `likePost()` and `followuser()` do not prevent duplicate relationships.

This is a correctness and security problem, not just a style issue. The code should explicitly enforce ownership and uniqueness rules.

### 3. Repeated manual DTO mapping adds noise and duplication

`PostService` and `UserService` repeatedly construct response objects field by field.

- `src/main/java/com/devblog/server/service/PostService.java`
- `src/main/java/com/devblog/server/service/UserService.java`

This creates a lot of boilerplate and makes the code harder to maintain. If a field is added or renamed, it has to be updated in many places.

Recommendation:

- Introduce mapper helpers, a dedicated mapper class, or MapStruct.
- Extract common response-building logic into private methods if the project stays small.

### 4. Authentication helpers assume a valid security context

`src/main/java/com/devblog/server/util/AuthUtils.java` directly reads `SecurityContextHolder.getContext().getAuthentication().getName()`.

If the filter chain does not populate authentication, this can fail with a null pointer or misleading error path. The method also hides the distinction between "not authenticated" and "user not found".

Recommendation:

- Guard against missing authentication.
- Return a clearer security exception when the request is unauthenticated.

### 5. Naming and style are inconsistent

There are several naming issues that make the code feel unfinished:

- `followuser()` should be `followUser()`
- `unLikePost()` should be `unlikePost()` or `unlikePostById()`
- Commented explanations in repositories are useful, but the naming should already make the intent obvious

These are small issues individually, but they reduce polish and make the codebase feel less disciplined.

### 6. The project setup and documentation disagree

The README says Spring Boot 3 and Java 17+, while `build.gradle` uses Spring Boot 4.0.6 and Java 25.

- `README.md`
- `build.gradle`

That mismatch will confuse contributors and makes the repo harder to bootstrap reliably.

Recommendation:

- Align README, Gradle, and runtime expectations.
- Document the exact JDK and Spring Boot versions the project actually targets.

### 7. Entity modeling is functional but not robust

The JPA entities are simple and readable, but they rely heavily on Lombok-generated `@Data` and manual relationship management.

- `src/main/java/com/devblog/server/model/User.java`
- `src/main/java/com/devblog/server/model/Post.java`
- `src/main/java/com/devblog/server/model/Follow.java`
- `src/main/java/com/devblog/server/model/Like.java`

Concerns:

- `@Data` on entities can cause equality and lazy-loading surprises.
- There are no obvious constraints preventing duplicate follows or likes.
- `createdAt` / `updatedAt` timestamps are set manually instead of using a shared auditing strategy.

Recommendation:

- Prefer explicit `@Getter` / `@Setter` over `@Data` for JPA entities.
- Add database constraints where the business rules require uniqueness.
- Consider JPA auditing for timestamps.

### 8. Test coverage is essentially absent

The only test is a context load test.

- `src/test/java/com/devblog/server/ServerApplicationTests.java`

That means the current suite does not validate authentication, authorization, persistence behavior, or API contracts.

Recommendation:

- Add service-level tests for ownership and duplicate-prevention rules.
- Add controller tests for status codes and validation.
- Add repository tests for custom query methods.

## Smaller observations

- `PostController` exposes only create, update, delete, and search routes. If this is intentional, fine; otherwise the API surface looks incomplete relative to the service layer.
- `PostService.searchPost()` only searches titles, which may be too narrow for a blog backend.
- `SecurityConfig` is clean and readable, but `csrf().disable()` should be a deliberate decision that is documented, not just default boilerplate.

## Bottom line

The code is understandable and the architecture is in the right shape, but the quality is held back by missing authorization checks, repeated mapping code, weak defensive handling, and unsafe configuration management. If the goal is a maintainable backend, the next step should be tightening security and reducing duplication before adding more features.
