# User Authentication Service

Spring Boot service providing account registration, credential verification, JWT-based token validation, session persistence, and a signup-email event.

## Architecture

`AuthController` exposes the authentication endpoints and delegates to `IAuthService` / `AuthService`. `UserRepo` and `SessionRepo` persist JPA entities in MySQL. Passwords are encoded with BCrypt. `KafkaClient` publishes a JSON email message to Kafka topic `signup`; `emailService` consumes that event. This service is registered with Eureka as `userservice`.

Key classes: `User`, `Role`, `Session`, `BaseModel`, `AuthController`, `UserController`, `AuthService`, `CustomUserDetailsService`, `SecurityConfig`, `SpringSecurity`, and `KafkaClient`.

## API (as mapped in source)

| Method | Path | Purpose |
|---|---|---|
| POST | `/auth/signup` | Create an account and publish a welcome-email event |
| POST | `/auth/login` | Check credentials, persist a session, and return a token in a response header |
| POST | `/auth/logout` | Logout flow (currently incomplete; service returns `null`) |
| POST | `/auth/validateToken` | Validate a token against a stored session |
| GET | `/user` | User lookup controller; source currently has no path variable mapping despite requiring `userId` |

## Configuration and local run

Requires Java 17, Maven (or the included `mvnw.cmd`), MySQL database `userauthenticationservice`, Kafka at `localhost:9092`, and Eureka at `localhost:8761`. The configured application port is `9000`.

Configure database credentials and the JWT signing key through environment-specific Spring properties; never commit real credentials. The signing key is currently generated at application startup, so sessions/tokens are not stable across restarts. Use a persistent secret-key configuration before relying on tokens across deployments.

From this directory:

```powershell
.\mvnw.cmd spring-boot:run
```

Build and run tests with `.\mvnw.cmd test`; package with `.\mvnw.cmd package`.

## Known gaps to address

- `logout()` is a stub and the controller dereferences its result.
- The `/user` controller mapping does not bind the `userId` parameter used by its method; the catalog's direct user lookup therefore needs correction and an integration test.
- Review both security filter-chain configurations together; the current configuration includes a permissive chain and a separate authorization-server chain.
- Keep secrets outside source/configuration files, use a stable signing key, and add request validation, token revocation/expiry tests, and migration-managed schema changes.
