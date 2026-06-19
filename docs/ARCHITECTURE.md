# Architecture

The Student Portal Management System is a full stack application with a Spring Boot REST API, Angular standalone frontend, MySQL persistence, JWT authentication, role-based authorization, audit logging, Swagger documentation and Dockerized deployment.

## Backend Layers

1. **Controller layer** validates HTTP input and maps REST endpoints to service methods.
2. **Service layer** contains business logic, transactions, soft deletes, duplicate checks, grading, reports and audit events.
3. **Repository layer** uses Spring Data JPA repositories and JPQL projections.
4. **Entity layer** models the database tables and inherits audit fields from `BaseEntity`.
5. **Security layer** validates JWT bearer tokens and enforces role-based access control.
6. **Exception layer** converts domain and validation errors into consistent JSON error responses.
7. **DTO/Mapper layer** separates API contracts from JPA entities.

## Frontend Layers

1. `features/` contains lazy-loaded standalone page components.
2. `core/services/` contains API services, one service per backend module.
3. `core/models/` contains DTO-compatible TypeScript interfaces.
4. `core/guards/` protects routes by authentication and role.
5. `core/interceptors/` injects JWT tokens and shows API errors.
6. `shared/components/layout/` provides the sidebar, toolbar and role-aware menu.

## Security Flow

1. User logs in with `/api/auth/login`.
2. Backend authenticates with Spring Security and returns a JWT.
3. Frontend stores the token in `localStorage` under `sp_token`.
4. `authInterceptor` sends `Authorization: Bearer <token>` on API calls.
5. `JwtAuthenticationFilter` validates the token, loads the user and sets the security context.
6. `@PreAuthorize` annotations enforce ADMIN, FACULTY and STUDENT access.

## Soft Delete and Auditing

All mutable entities inherit `deleted`, `createdDate`, `updatedDate`, `createdBy`, and `updatedBy`. Delete endpoints set `deleted=true`; list/search queries filter active rows. Create/update/delete/login/register actions are saved in `audit_logs`.
