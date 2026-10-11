# T-003 · Corregir la autorización: roles en el registro, usuarios solo ADMIN, catálogo público y CORS
- Estado: En curso · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `fix/T-003-authorization-rules` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-002 · Spec/ADR: [0003](../adr/0003-jwt-stateless-auth.md)
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
🔴 S-2: el registro acepta `"role":"ADMIN"` (`AuthService.java:61`; probe → 200). 🔴 S-3: cualquier usuario lista todos los usuarios (`UserController.java:29-32`) y **borra a cualquiera** (`UserController.java:49-57`; probe → 200). 🟠 S-4: CORS `*` (`CorsConfig.java:17-19`). Además la home es pública pero `GET /v1/api/property` exige token (`SecurityConfig.java:38`).

## Objetivo
Solo USER/OWNER se auto-registran; listar y borrar usuarios requiere ADMIN; el catálogo (GET de propiedades e imágenes) es público; CORS solo para orígenes configurados.

## Alcance
- Incluye: validación de rol en `AuthService.register` (rechazar ADMIN y valores inválidos), reglas en `SecurityConfig` (`hasAuthority("ADMIN")`: las authorities no tienen prefijo `ROLE_`), `permitAll` para `GET /v1/api/property/**` y `GET /v1/api/img/**`, quitar `/v1/api/render/*`, `CorsConfig` con `CORS_ALLOWED_ORIGINS` (local: `http://localhost:5173,http://localhost:8081`) en `.example`, compose y SETUP.
- **No incluye:** códigos de error 400/401/409 correctos (T-008), dueño de la propiedad en update/delete (ya existe en el servicio; T-007 lo caracteriza), panel admin del front, cookie para el token.

## Leer antes
- `backend/src/main/java/com/clonair/back/security/config/SecurityConfig.java` (57 líneas) · `CorsConfig.java` (23)
- `backend/src/main/java/com/clonair/back/security/auth/AuthService.java:54-69`
- `backend/src/main/java/com/clonair/back/user/UserController.java:19-59`
- Auditoría → Probes HTTP y Seguridad

## Archivos previstos
- modificar: `SecurityConfig.java`, `CorsConfig.java`, `AuthService.java`, `application.properties`, `env.properties.example`, `docker-compose.yml`, `docs/SETUP.md`, `docs/ARCHITECTURE.md` (tabla de endpoints) · crear: `security/AuthorizationIntegrationTest.java`

## Tests
- Integración (molde de T-002): register con ADMIN → no 2xx y el usuario no existe; register con rol inválido → no 2xx; USER → `GET /v1/api/user` 403 y `DELETE /v1/api/user/{id}` 403 (y el usuario sigue existiendo); ADMIN (creado por repositorio en el test) → 200; anónimo → `GET /v1/api/property` 200; preflight CORS desde `http://evil.example` → sin `Access-Control-Allow-Origin`; desde `http://localhost:5173` → permitido.

## Criterios de aceptación
- [ ] `./scripts/verify.sh --server` pasa y la cobertura no baja
- [ ] Repetir los probes de la auditoría con `curl`: ADMIN por registro y borrado por USER ya no dan 2xx
- [ ] `curl -s -o /dev/null -w '%{http_code}' http://localhost:8080/v1/api/property` → 200 sin token
- [ ] `.example`, compose y `docs/SETUP.md` tienen `CORS_ALLOWED_ORIGINS`

## Notas de implementación
- Hecho: rama creada desde development actualizado (da3e6e6); sin PR abiertos. Línea de base: server build, 31 tests y cobertura OK; client build OK y 93 problemas de lint conocidos (T-005).
- Pendiente: tests de regresión primero, luego reglas de roles, catálogo público, CORS y documentación.
- Rama: `fix/T-003-authorization-rules` · PR: draft pendiente.
- Próximo paso: abrir PR draft y ejecutar AuthorizationIntegrationTest antes del fix.
- Cambios documentales previos preservados en el working tree y excluidos de los commits de T-003.

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Pendientes → …
