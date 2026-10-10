# Arquitectura

Estado real al 2026-10-10 (auditoría inicial). Lo que está roto se marca ⚠️ con su tarea.

## Componentes

```mermaid
flowchart LR
  subgraph Navegador
    SPA["SPA React 18 + Vite<br/>frontend/airbnb_clone"]
  end
  subgraph "docker compose (local)"
    C["client · nginx :8081<br/>(profile app)"]
    S["server · Spring Boot :8080<br/>(profile app o ./mvnw)"]
    DB[("db · MySQL 8.4 :3306<br/>volumen clonair_db-data")]
  end
  SPA -- "fetch + JWT (Bearer)" --> S
  C -- "sirve dist/" --> SPA
  S -- "JPA/Hibernate (ddl-auto=update)" --> DB
  SPA -. "⚠️ hoy: IP de producción hardcodeada (T-004)" .-> PROD["API de producción<br/>⚠️ estado desconocido"]
```

## Flujo de un request

```mermaid
sequenceDiagram
  participant F as Front (apiRequests.jsx)
  participant J as JwtAuthenticationFilter
  participant C as Controller
  participant S as Service (*ServiceImp)
  participant R as Repository (Spring Data)
  participant D as MySQL
  F->>J: GET /v1/api/property (Authorization: Bearer …)
  J->>J: valida firma/expiración, carga UserDetails
  J->>C: SecurityContext autenticado
  C->>S: getAll()
  S->>R: findAll()
  R->>D: SELECT …
  D-->>R: filas
  S-->>C: List<PropertyResponse> (record)
  C-->>F: 200 JSON
  Note over C,F: ⚠️ cualquier excepción → 403 (sin @RestControllerAdvice; /error no es público) → T-008
```

## Autenticación

```mermaid
sequenceDiagram
  participant U as Usuario
  participant F as Front
  participant A as AuthController /v1/api/auth
  participant M as AuthenticationManager (DAO + BCrypt)
  participant JS as JwtService
  U->>F: email + contraseña
  F->>A: POST /login
  A->>M: authenticate()
  M-->>A: OK / BadCredentials (→ 403)
  A->>JS: getToken(user)  (HS256, ⚠️ secreto hardcodeado: T-001)
  A-->>F: { token, role[] }
  F->>F: localStorage.setItem('token')  (⚠️ S-8)
```

- Stateless (`SessionCreationPolicy.STATELESS`), CSRF deshabilitado. Públicos: `/v1/api/auth/*`, `/swagger-ui/**`, `/v3/api-docs/**` (`SecurityConfig.java:47-55`).
- Roles `USER`, `OWNER`, `ADMIN` (`Role.java`) como authorities **sin prefijo** `ROLE_`. No hay reglas por rol en `SecurityConfig`: el chequeo OWNER está a mano en `PropertyServiceImp` (⚠️ T-003).

## Modelo de datos

```mermaid
erDiagram
  USER ||--o{ PROPERTY : "publica (OneToMany, cascade ALL)"
  PROPERTY ||--|| LOCATION : "OneToOne"
  PROPERTY ||--o{ IMAGE : "OneToMany mappedBy property"
  PROPERTY ||--o{ PROPERTY_SERVICE_TYPES : "ElementCollection (enum)"
  USER { string id PK "uuid2" string username UK string password "BCrypt" string firstname string lastname string country string role string contact text description }
  PROPERTY { string id PK string title string category string subCategory string description double value boolean active int bathroom int bed int bedroom }
  LOCATION { string id PK string country string city }
  IMAGE { string id PK string mime string name blob content "LONGBLOB" string url "⚠️ blob:http://127.0.0.1:5500/…" }
```

Sin migraciones: Hibernate crea/actualiza el esquema. No existe entidad de reservas.

## Endpoints principales

Prefijo `/v1/api`. Contrato vivo: http://localhost:8080/v3/api-docs (Swagger UI en `/swagger-ui`).

| Endpoint | Pantalla (front) | Estado |
|---|---|---|
| `POST auth/register`, `POST auth/login` | `components/forms/Forms.jsx` | ⚠️ el front llama a `/auth/*` (T-004) |
| `GET property` | Home `shared/CardContainer` y filtros | ⚠️ requiere token; la home es pública (T-003) |
| `GET property/{id}` | `cards/DetailContainerCard` | ⚠️ idem |
| `GET property/user/{id}` | `owner/PropertiesUser`, `admin/AdminPanel` | |
| `POST property` (multipart) | `property/PropertyRegister` | ✅ verificado por curl |
| `PUT property/{id}` (multipart) | `owner/PropertiesUpdateForm` | |
| `DELETE property` | `owner/PropertiesUser`, `admin/AdminPanel` | ⚠️ falta `{id}` (T-006) |
| `GET user`, `GET user/{id}`, `GET user/username/{u}` | `admin/AdminPanel`, `user/PersonalInfoForm` | ⚠️ cualquier usuario lista todos (T-003) |
| `PUT user/{id}` | `user/PersonalInfoForm` | ⚠️ comentado en el back (Backlog P2) |
| `DELETE user/{id}` | `admin/AdminPanel` | ⚠️ cualquier usuario borra a cualquiera (T-003) |
| `GET/POST/PUT/DELETE img…` | `<img>` en cards | ⚠️ requiere token y URLs inválidas (T-012) |
| — (reservas) | `cards/DetailCardReservation`, `user/TripsUser` | ⚠️ no existe (Backlog P2) |

## Arquitectura objetivo

- Tests con la misma DB que producción vía Testcontainers → [ADR 0001](adr/0001-testcontainers-mysql.md).
- Migración por escalones protegida por tests (Boot 3.2 → 3.5 → 4.x, Java 17 → 21/25, Vite/React/Router por separado) → [ADR 0002](adr/0002-migration-path.md).
- Se mantiene JWT stateless, con secreto y expiración por configuración, reglas por rol en `SecurityConfig` y errores `ProblemDetail` → [ADR 0003](adr/0003-jwt-stateless-auth.md).
- Se mantienen las carpetas `backend/` y `frontend/airbnb_clone/` → [ADR 0004](adr/0004-keep-folder-layout.md).
- A futuro (P2): DTOs en todas las respuestas, Flyway, imágenes fuera de la DB, paginación.
