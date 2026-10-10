# T-008 · Agregar manejo global de errores con ProblemDetail y health check
- Estado: Lista · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `feat/T-008-error-handling` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-002, T-003 · Spec/ADR: [0003](../adr/0003-jwt-stateless-auth.md)
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
A-2: los servicios lanzan `Exception` genérica (`PropertyServiceImp.java:34,85,88,91`, `UserServiceImp.java:24,44`) y `/error` no es público, así que **todo error es 403** (probes: JSON inválido, 404, credenciales, duplicado). No hay health endpoint (compose y scripts usan `/v3/api-docs`).

## Objetivo
Errores con el status correcto y cuerpo `application/problem+json`; `GET /actuator/health` público.

## Alcance
- Incluye: excepciones de dominio (`NotFoundException`, `ForbiddenException`, `ConflictException`, `BadRequestException`), `@RestControllerAdvice` con `ProblemDetail`, `spring.mvc.problemdetails.enabled=true`, `AuthenticationEntryPoint` → 401 y `AccessDeniedHandler` → 403, `/error` permitido, 201/204 en create/delete si no rompe al front (revisar `apiRequests.jsx`), `spring-boot-starter-actuator` solo con `health` expuesto, healthcheck de compose y `wait_http` de `scripts/dev.sh` apuntando a `/actuator/health`, docs (README "Problemas frecuentes", AGENTS gotchas).
- **No incluye:** Bean Validation en todos los requests (Backlog P2), DTOs, cambios de permisos (T-003).

## Leer antes
- Auditoría → Probes HTTP (tabla de esperados) y A-2
- `SecurityConfig.java` · `PropertyServiceImp.java:30-171` · `UserServiceImp.java` · `AuthController.java`

## Archivos previstos
- crear: `backend/src/main/java/com/clonair/back/common/error/*` · tests `common/error/GlobalExceptionHandlerTest.java` y casos en el test de integración
- modificar: servicios (tipos de excepción), `SecurityConfig.java`, `pom.xml`, `application.properties`, `docker-compose.yml`, `scripts/dev.sh`, `README.md`, `AGENTS.md`, `docs/ARCHITECTURE.md`

## Tests
- Integración: cada fila de la tabla de probes de la auditoría con el status esperado (400 JSON inválido, 401 sin token / token inválido / credenciales malas, 404 inexistente, 409 username duplicado) y `Content-Type: application/problem+json`; `GET /actuator/health` → 200 sin token.

## Criterios de aceptación
- [ ] `./scripts/verify.sh` pasa y la cobertura no baja
- [ ] Re-correr los probes de la auditoría con `curl`: todos devuelven el "esperado"
- [ ] `docker compose --profile app up -d --build --wait` usa `/actuator/health` y queda healthy
- [ ] El front sigue funcionando en los flujos de login y alta (manual, anotado en "Notas")

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Pendientes → …
