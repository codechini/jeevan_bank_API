# Jeevan Bank API Specification

## 1. System Overview
A Spring Boot 3.x / Java 17+ Banking API using PostgreSQL and JWT Authentication.

## 2. Database Schema Reference
- **Users & Roles:** `User` table linked to `Role` (ADMIN, USER).
- **Banking Core:** `AccountHolder` links to `User`. `Account` links to `AccountHolder`.
- **Transactions:** All movements must be logged in the `Transaction` table.
- **Support Tables:** `Loan`, `Card`, `ChequeBookRequest`, and `TermsOfService`.

## 3. Security Requirements
- **Stateless JWT:** Use `jjwt` library.
- **Filter Chain:** Implement `JwtAuthenticationFilter` and `SecurityConfig`.
- **Password Storage:** Use `BCryptPasswordEncoder`.
- **Role-Based Access (RBAC):**
    - `/api/auth/**`: Permit All.
    - `/api/user/**`: Requires `ROLE_USER` or `ROLE_ADMIN`.
    - `/api/admin/**`: Requires `ROLE_ADMIN`.

## 4. Endpoint Implementation Logic
Referencing `API-endpoints.txt`:
- **Auth:** Login/Register must create both a `User` and an initial `AccountHolder` profile.
- **Transactions:** `/deposit`, `/withdraw`, and `/transfer` must be `@Transactional`. 
- **Validation:** Use `spring-boot-starter-validation` for all incoming DTOs (e.g., non-negative amounts, valid UUIDs).
- **Responses:** Use a consistent `ApiResponse<T>` wrapper.
