# Property Management API

Property Management API is a Spring Boot REST API for managing users and their property records.

This project was started while following the Coursera / Packt course [**Zero to Hero: Master Java Spring Boot & JPA with Projects**](https://www.coursera.org/learn/packt-zero-to-hero-java-springboot-and-jpa-mastery-with-real-project-1ee3f). The original course repository is available at [PacktPublishing/Zero-to-Hero-Java-SpringBoot-and-JPA-Mastery-with-a-Real-Project](https://github.com/PacktPublishing/Zero-to-Hero-Java-SpringBoot-and-JPA-Mastery-with-a-Real-Project).

> **Current status:** This repository is still under development. Password verification exists in the login flow, but JWT/session-based authentication and endpoint authorization have not been implemented yet. The API should not be considered production-ready.

## Table of Contents

- [Learning Context](#learning-context)
- [Current Implementation Status](#current-implementation-status)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Layered Architecture](#layered-architecture)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Running the Project](#running-the-project)
- [Application Profiles](#application-profiles)
- [API Documentation](#api-documentation)
- [Error Responses](#error-responses)
- [Swagger / OpenAPI](#swagger--openapi)
- [Actuator](#actuator)
- [Testing](#testing)
- [Scope Boundaries](#scope-boundaries)
- [Roadmap](#roadmap)
- [License](#license)

## Learning Context

The project began as a learning project based on the course structure and domain presented in the Packt/Coursera training. It has subsequently been refactored and extended independently.

The main changes made so far include:

- Migrating the package structure to `com.mycompany.property_management`
- Replacing the original converter/DTO arrangement with separate request and response DTO packages
- Introducing explicit service, repository, mapper, entity and exception layers
- Adding create, update, patch, delete and retrieval flows for users and properties
- Adding BCrypt password hashing and password verification
- Adding validation and a global REST exception handler
- Updating the project to Java 21 and a newer Spring Boot version
- Adding Springdoc OpenAPI and Actuator configuration
- Adding environment-specific application profiles

The course repository remains the reference point for the original learning implementation; this repository is the current independent working version.

## Current Implementation Status

The project currently provides a working foundation for user and property management through a layered Spring Boot REST API.

The implementation is suitable as an evolving learning and portfolio project, but it is not production-ready yet. The login flow verifies credentials but does not issue an access token or create a session; endpoint authorization, containerization and CI are planned for subsequent iterations.

## Features

- Create users
- Fully update and partially update users
- Delete users
- Hash passwords with BCrypt
- Verify email/password credentials through a login endpoint
- Create, retrieve, list, update, patch and delete properties
- List properties belonging to a user
- Request validation
- Global exception handling
- Separate request and response DTOs
- Entity-to-DTO and DTO-to-entity mappers
- Profile-based H2 and MySQL configuration
- Swagger/OpenAPI integration
- Spring Boot Actuator endpoints

## Technology Stack

### Core

- Java 21
- Spring Boot 4.1.1
- Maven Wrapper

### Web and API

- Spring Web MVC
- Spring Validation

### Persistence

- Spring Data JPA
- Hibernate
- H2 Database
- MySQL Connector/J

### Security

- Spring Security Crypto
- BCrypt password hashing

> Full Spring Security authentication and authorization are not implemented yet.

### Documentation and Operations

- Springdoc OpenAPI / Swagger
- Spring Boot Actuator

### Developer Experience

- Lombok
- Profile-based application configuration

## Layered Architecture

The application follows a layered architecture:

```text
Request DTO -> Controller -> Service -> Repository -> Database
Response DTO <- Controller <- Service <- Repository <- Database
```

The request and response model flow is:

```text
Request DTO -> Mapper/Service -> Entity -> Repository
Repository -> Entity -> Mapper -> Response DTO
```

| Layer | Responsibility |
|---|---|
| Controller | Defines routes, receives HTTP requests, triggers validation and returns HTTP responses. |
| Request DTO | Defines the input contract of an endpoint and carries request validation rules. |
| Service | Contains application and business logic, coordinating repositories and mappers. |
| Entity | Represents the persistence model used by JPA/Hibernate. |
| Repository | Provides database access through Spring Data JPA. |
| Mapper | Converts request DTOs to entities and entities to response DTOs. |
| Response DTO | Defines data exposed to API clients without exposing persistence details such as passwords. |

The current implementation uses explicit mapper classes for the main entity/DTO conversions. The service layer coordinates these conversions and repository operations rather than exposing entities directly from controllers.

## Project Structure

The main source packages are organized as follows:

| Package or path | Responsibility |
|---|---|
| `config` | Application and bean configuration |
| `controller` | REST endpoints |
| `dto/request` | Incoming request models and validation rules |
| `dto/response` | API response models |
| `entity` | JPA persistence entities |
| `exception` | Custom exceptions and global exception handler |
| `mapper` | Entity and DTO conversions |
| `repository` | Spring Data JPA database access |
| `service` | Application business logic |
| `resources` | Application profiles and logging configuration |
| `test` | Automated tests |

Important files and directories:

```text
src/main/java/com/mycompany/property_management/
src/main/resources/
src/test/java/
pom.xml
mvnw
mvnw.cmd
```

## Requirements

- JDK 21 or newer
- Git
- Maven Wrapper, which downloads Maven 3.9.16

The default `local` profile uses an H2 file database, so MySQL is not required for the local setup.

## Running the Project

Clone the repository:

```bash
git clone https://github.com/mahmutsatilmis/property-management.git
cd property-management
```

### Run with the local profile

The `local` profile is active by default:

```bash
bash mvnw spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

Alternatively, build and run the jar:

```bash
bash mvnw clean package
java -jar target/property-management-0.0.1-SNAPSHOT.jar
```

> The `mvnw` file currently does not have the executable bit set. On Linux/macOS, use `bash mvnw` instead of `./mvnw` until the file mode is fixed.

### Select a profile explicitly

```bash
SPRING_PROFILES_ACTIVE=local bash mvnw spring-boot:run
```

Windows PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
bash mvnw spring-boot:run
```

## Application Profiles

### `local`

The default profile uses an H2 file database.

| Setting | Value |
|---|---|
| H2 database URL | `jdbc:h2:file:~/h2/propertydb` |
| H2 username | `sa` |
| H2 password | Empty |
| H2 console path | `/h2-console` |
| Hibernate DDL mode | `update` |

H2 console:

```text
http://localhost:8080/h2-console
```

Use the following values in the H2 console:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:file:~/h2/propertydb` |
| User Name | `sa` |
| Password | Leave empty |

### `dev`

The development profile uses MySQL and expects these environment variables:

```bash
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
SPRING_PROFILES_ACTIVE=dev
```

The default MySQL connection is:

```text
jdbc:mysql://127.0.0.1:3306/pmsdb_dev
```

Run with the development profile:

```bash
DB_USERNAME=your_mysql_username \
DB_PASSWORD=your_mysql_password \
SPRING_PROFILES_ACTIVE=dev \
bash mvnw spring-boot:run
```

### `test`, `acc` and `prod`

These profiles use MySQL. Provide `DB_USERNAME`, `DB_PASSWORD` and a suitable database URL for the selected environment. The `prod` profile uses schema validation (`ddl-auto=validate`); a database migration tool has not been added yet.

> Never commit real database passwords or secret values to the repository. Use environment variables instead.

## API Documentation

The complete API contract should be treated as the OpenAPI definition exposed by the running application. Swagger UI is the preferred place to inspect and try the current endpoints, request models, response models and documented status codes.

The main resource groups are:

- `/api/users` - user creation, update, deletion and credential verification
- `/api/properties` - property creation, retrieval, update and deletion

For a live, interactive reference, see [Swagger / OpenAPI](#swagger--openapi).

The README intentionally keeps only this resource-level overview. Endpoint details should be maintained in controller annotations and the OpenAPI definition so that the documentation stays close to the implementation.

## Error Responses

The global exception handler currently handles these cases:

| HTTP status | Situation |
|---|---|
| `400 Bad Request` | Request validation failed |
| `401 Unauthorized` | Login credentials are invalid |
| `404 Not Found` | User or property was not found |
| `409 Conflict` | Email address already exists |

Example:

```json
{
  "message": "User not found"
}
```

Validation errors are returned as field-level messages.

## Swagger / OpenAPI

The project includes Springdoc OpenAPI. When the application is running, the following endpoints are available:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

The current controllers expose `Users` and `Properties` tags. Since JWT authentication has not been implemented yet, Swagger does not currently define Bearer authentication.

## Actuator

The `dev` profile exposes these Actuator web endpoints:

```text
/actuator/health
/actuator/beans
/actuator/metrics
```

Access control should be added before exposing Actuator endpoints in a production environment.

## Testing

The current test source contains one test that verifies that the Spring application context loads:

```text
src/test/java/com/mycompany/property_management/PropertyManagementApplicationTests.java
```

Run the tests with:

```bash
bash mvnw test
```

The project requires Java 21 for compilation.

Planned test coverage:

- UserService unit tests
- PropertyService unit tests
- Controller / MockMvc tests
- Repository tests
- Validation and exception tests
- Authentication and authorization tests
- Integration tests

## Scope Boundaries

The current version focuses on core CRUD operations, persistence and basic credential verification. The following concerns are outside the current implementation scope:

- Token-based authentication and endpoint authorization
- Database migration management
- Containerized deployment
- Automated CI pipeline
- Advanced querying such as pagination, filtering and sorting

These are planned improvements rather than statements about the course-derived foundation of the project.

## Roadmap

Recommended development order:

1. Expand unit and integration test coverage
2. Add Spring Security and JWT authentication
3. Implement owner and role-based authorization
4. Add authentication and authorization tests
5. Add database migrations with Flyway or Liquibase
6. Add Dockerfile and Docker Compose
7. Add a GitHub Actions CI workflow
8. Add pagination, filtering and sorting
9. Measure and optimize the possible N+1 query in property listing
10. Harden production configuration and Actuator access

## License

No license has been defined for this repository yet.
