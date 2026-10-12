# T-008 · Agregar manejo global de errores con ProblemDetail y health check
- Estado: Hecho · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
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
- [x] `./scripts/verify.sh` pasa y la cobertura no baja → gate completo verde (88 s): 118 tests, 0 failures/errors/skips; client lint/build verdes. Cobertura global 82,54 % (520/630), base T-007 76,20 %; PropertyServiceImp 95,07 % (135/142), sin baja; umbral 40 % conservado.
- [x] Re-correr los probes de la auditoría con `curl`: todos devuelven el "esperado" → status y cuerpos del alcance comprobados sobre la imagen final; tabla debajo. URLs de imágenes siguen fuera de T-008 (T-012).
- [x] `docker compose --profile app up -d --build --wait` usa `/actuator/health` y queda healthy → dos builds/waits OK; server y db healthy, client running. `docker inspect` confirma el probe HTTP exacto.
- [x] El front sigue funcionando en los flujos de login y alta (manual, anotado en "Notas") → alta OWNER desde formulario, mensaje de éxito, login y saludo `Bienvenido: T008`.

## Notas de implementación
- Inicio: árbol limpio, sin cambios heredados; `gh pr list` sin abiertos, #10 MERGED confirmado. Base `e1b07c4cf00423302b19199082239d8fda88c330`; T-007 cerrada en esta rama, sin commits a development ni reutilizar su rama.
- Rama / PR: `feat/T-008-error-handling` · [#11](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11), mergeado por el dueño el 2026-10-12 con squash `1b8ec829c519b3d84a2c11543738eb8def7bf23b`. Implementación: `70716bd`; commit final revisado: `4eb7e2fed41ea1bc629f4216e4e673ea4977d4ff`.
- Hecho: excepciones de dominio, advice basado en `ResponseEntityExceptionHandler`, ProblemDetail para filtros/entrypoint/access-denied y despacho `/error`; JWT malformado/expirado/firma inválida/sujeto ausente/usuario inexistente → 401; auth ausente → 401, permisos → 403. Credenciales malas y usuario inexistente devuelven idéntico cuerpo, sin enumeración. Errores internos → 500 genérico sin SQL/stack trace.
- Decisiones: mantener las reglas de T-003; un Bearer inválido produce 401 también en rutas públicas. Solo `health` expuesto HTTP/JMX, sin detalles/componentes, con comprobación de DB. Registro 201 + token, alta de propiedad 201 sin cuerpo, delete propiedad/usuario 204 sin cuerpo. El controller de usuario delega errores al advice en lugar de convertir toda excepción en 404.
- Duplicados: precheck de username y traducción de la restricción única ante carrera; otros fallos de integridad permanecen internos, sin clasificarlos todos como 409.
- Archivos: `backend/.../common/error/*`, servicios/controller de property y user, auth/config/filtro JWT, pom y application.properties, configuración y tests del back; compose, dev.sh y seed-demo.sh; frontend `apiRequests.jsx` (403 → 401 en invalidación de token); README, AGENTS raíz/back, ARCHITECTURE, SETUP, TESTING, tarea y tablero. No se leyeron ni cambiaron configuraciones reales o secretos, ni esquema, permisos o contrato de URLs de imágenes.
- Tests primero: commit `ff5080e`; 14 casos de regresión fallaron contra la base antes del fix (14 failures, 0 errors). Fix verificado con los tests específicos y gate completo. El primer test POST 401 del fix encontró `HttpRetryException` del cliente HttpURLConnection; se reemplazó por el cliente JDK para comprobar el cuerpo/challenge. No era un fallo de la API.
- Tests agregados: 41 (20 integración, 10 advice, 8 despacho `/error`, 3 AuthService). Verifican códigos, content-type, campos de ProblemDetail, ausencia de detalles internos, JWT, roles, mutaciones 201/204, DB y health. Se conservaron todos los tests previos y sus efectos; solo se ajustaron expectativas al contrato nuevo.
- Verificación final: gate completo 118 tests sin fallos/errores/skips, JDK 21 / Node 24.19.0; cobertura 82,54 % global y 95,07 % PropertyServiceImp. Advice, fallback, SecurityProblemHandler y AuthService: 100 % de líneas. Build Docker con Java 17 OK. Persiste el aviso conocido de cierre de Surefire en Windows; BUILD SUCCESS y gate verde.
- Probes: curl sobre compose final; errores de API `application/problem+json`, status/body coincidentes, credenciales malas/usuario ausente con mismo cuerpo. CORS conserva su rechazo previo (403 sin Allow-Origin, cuerpo propio del filtro CORS); no se cambió ese contrato.
- Manual frontend (Brave, http://localhost:8081): alta de OWNER ficticio `t008-ui-20261012@clonair.local`, nombre T008, país Armenia y contacto inventado. Registro exitoso confirmado en consola; formulario se cierra y guarda token. Login posterior exitoso, `Bienvenido: T008` visible; repetido tras recargar la imagen final del frontend. Se usó teclado para los controles del diálogo; la entrada sintética por fill/click no entregaba todos los eventos de React. No se cambió UI ni infraestructura T-009.
- Compatibilidad adicional: prueba puntual Node de las funciones reales del cliente con respuestas simuladas: 401 borra token, 403 lo conserva; login 200/registro 201 guardan token, deletes 204 completan sin parsear JSON. Lint/build verdes. `seed-demo.sh` ejecutado dos veces contra compose: duplicado 409 y login 200, idempotencia OK; sintaxis Bash de scripts OK.
- Hallazgo fuera de alcance → Backlog P0: `Property.user` tiene `@Cascade(ALL)` y el delete de una propiedad elimina al OWNER. Confirmado por logs y probe posterior de login (401). Preexiste en la base y se conservó; corregirlo requiere una tarea sobre datos/cascadas. URLs blob de imágenes → T-012. No bloquean la revisión del alcance de T-008.
- Revisión independiente: [APROBADO, sin bloqueantes de código](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11#issuecomment-6129711504), sobre `4eb7e2fed41ea1bc629f4216e4e673ea4977d4ff`. Gate completo verde (92 s): 118 tests sin fallos/errores/skips, lint/build client verdes; cobertura global 82,59 % (517/626) en esa ejecución y PropertyServiceImp 95,07 % (135/142). Compose build/wait y probes repetidos OK; funciones reales del cliente verificadas con fetch simulado para 401/403/200/201/204.
- Límite de esa revisión: no se repitió el alta manual por bloqueo de aprobación automática de la cuenta ficticia persistente; login confirmado por log del cliente, sin confirmar saludo/cierre del diálogo. La prueba manual de implementación permanece como evidencia separada; el merge informado por el dueño no se registra como una nueva prueba manual. El probe directo a `/actuator/env` se omitió por bloqueo de aprobación; su falta de exposición pasó en los tests aislados.
- Cierre documental autorizado por el dueño después del merge: rama `docs/T-008-closeout`, creada desde el squash de #11, para guardar revisión/merge y sincronizar la cola. Árbol limpio al inicio, sin cambios heredados; no se inicia T-009. El commit de cierre se identifica con `git log -1` en esa rama.
- Pendiente de implementación: ninguno. Pendiente de proceso: integrar el PR de cierre documental indicado en STATUS. Stack local dejado corriendo para inspección.
- Próximo paso: integrar el cierre documental y abrir una sesión nueva de 🧠 Frontera para T-009, siguiendo `docs/agents/implementer.md` y `docs/tasks/T-009-frontend-test-infra.md`; rama prevista `test/T-009-frontend-test-infra`. Confirmar primero que no haya PR abiertos.

### Probes HTTP de la auditoría reejecutados

| Caso | Resultado final |
|---|---|
| Register OWNER | 201 + token |
| Register ADMIN / rol inválido | 400 ProblemDetail |
| Username duplicado | 409 ProblemDetail |
| Login correcto | 200 + token/role |
| Password incorrecta / email inexistente | 401 ProblemDetail idéntico |
| JSON inválido | 400 ProblemDetail |
| GET catálogo sin token | 200 (público desde T-003) |
| Endpoint protegido sin token / Bearer inválido | 401 ProblemDetail |
| GET propiedad inexistente | 404 ProblemDetail |
| GET user como USER / OWNER | 403 ProblemDetail |
| POST propiedad multipart como OWNER | 201; URLs preexistentes pendientes de T-012 |
| DELETE propiedad con ID (ruta corregida en T-006) | 204 sin cuerpo |
| Preflight evil.example | 403, sin Allow-Origin |
| GET health anónimo | 200, solo `status: UP` |

## Resultado
- PR: [#11](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11) · Revisión independiente APROBADA sobre `4eb7e2f`; mergeado por el dueño con squash `1b8ec82`.
- Qué cambió: errores con status correcto y ProblemDetail, seguridad 401/403, health público y probes de readiness; HTTP 201/204 y compatibilidad del cliente/seed. T-007 cerrada tras su merge confirmado.
- Validación: implementación con 118 tests, gate completo verde, cobertura global 82,54 %, compose healthy, curl y login/alta manual OK; revisión independiente con gate verde y cobertura 82,59 %, con el límite manual explicitado arriba.
- Pendientes → integrar cierre documental; URLs de imágenes en T-012 y cascada de borrado en Backlog. Próxima implementación: T-009 en una sesión nueva.
