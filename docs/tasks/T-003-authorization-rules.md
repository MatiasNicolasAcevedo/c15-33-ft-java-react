# T-003 · Corregir la autorización: roles en el registro, usuarios solo ADMIN, catálogo público y CORS
- Estado: Hecho (revisión independiente APROBADA; merge pendiente) · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
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
- [x] `./scripts/verify.sh --server` pasa y la cobertura no baja
- [x] Repetir los probes de la auditoría con `curl`: ADMIN por registro y borrado por USER ya no dan 2xx
- [x] `curl -s -o /dev/null -w '%{http_code}' http://localhost:8080/v1/api/property` → 200 sin token
- [x] `.example`, compose y `docs/SETUP.md` tienen `CORS_ALLOWED_ORIGINS`

### Evidencia
- Gate completo `./scripts/verify.sh`: server build + 57 tests + cobertura OK; client build OK, mismos 93 problemas de lint de la base (T-005). JaCoCo: 56,48 % (318/563), frente a 40,80 %; umbral 40 % conservado.
- Tests de regresión primero: commit `f6132ca`, 20 casos con 10 fallos y 0 errores antes del fix. Con el transporte HTTP corregido, la configuración CORS anterior vuelve a fallar (4 de 5 casos; 0 errores). El caso de subdominio usa ahora un origen válido.
- Probes repetidos con `curl` contra API y MySQL efímeras: registro ADMIN → 403 y COUNT de usuario = 0; listado USER → 403; borrado USER → 403 y víctima conservada; GET property sin token → 200.
- Para los probes se usó `http://localhost:18080`, porque el stack local existente ocupa 8080. Se cargó una copia de application.properties sin import de configuración real, con CORS_ALLOWED_ORIGINS y credenciales públicas de prueba. No se tocaron datos ni servicios de compose.
- Preflight a endpoint protegido: localhost:5173 y localhost:8081 → 200 con origen exacto; evil.example → 403 sin Access-Control-Allow-Origin.
- `docker compose --env-file backend/src/main/resources/env.properties.example config --quiet` → 0. Variable presente en .example, compose, SETUP y README.

## Notas de implementación
- Hecho: validación de rol antes de cifrar/persistir; registro limitado a USER/OWNER; listado y borrado de usuarios con hasAuthority("ADMIN"); GET de propiedades/imágenes público; permiso render eliminado. ADMIN se crea por base/seed, nunca por registro.
- CORS: CorsConfigurationSource integrada en Spring Security antes de autenticar; orígenes exactos por lista de configuración; vacía/ausente deniega solicitudes entre orígenes; comodines rechazados al iniciar.
- Tests: 20 casos de AuthorizationIntegrationTest (HTTP real, login/JWT, DB efímera) y 6 de CorsConfigTest (lista vacía y comodines). El cliente por defecto usa HttpURLConnection, que descarta Origin: los preflight usan JdkClientHttpRequestFactory para transmitirlo. Sin dependencias nuevas.
- Archivos: AuthService, SecurityConfig, CorsConfig, application.properties, env.properties.example, application-test.properties, dos clases de tests, compose, README, SETUP, ARCHITECTURE, TESTING, tablero y esta tarea.
- Rama: `fix/T-003-authorization-rules` · PR: [#6](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/6).
- Último commit de implementación: `376b5d6`; el cierre documental queda en el commit posterior con Refs: T-003.
- Pruebas: 57 tests, 0 fallos/errores/skips; cobertura 56,48 %; probes curl y compose OK. Contenedores efímeros eliminados y proceso de probes detenido; stack local conservado.
- Revisión independiente: [APROBADO, sin bloqueantes](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/6#issuecomment-6105109999), sobre `0625d79`: gate reproducido con 57 tests, 0 fallos/errores/skips y cobertura 56,48 %. Probes curl en API/DB efímeras (:18086): ADMIN 403 sin persistir, listado/borrado USER 403 y víctima conservada, ADMIN 200 con borrado efectivo, catálogo anónimo 200, CORS local permitido/evil rechazado. Entorno de revisión limpiado; stack existente conservado.
- Cambios documentales previos: inicialmente preservados en el working tree y excluidos de los commits del fix. El dueño autorizó explícitamente incorporarlos al PR #6 antes de T-004: cierres T-001/T-002, desbloqueo T-015 y reglas de prompts Lite. Se agrega además la regla de cierre en AGENTS y protocolos: inventario, diff revisado, rutas explícitas, documentación antes del último commit, commit/push, comprobación del árbol limpio y resolución de heredados antes de otra tarea.
- Cierre adicional: solo documentación, sin cambios de código, tests, dependencias, configuración ejecutable ni umbrales. Validación: diff y enlaces revisados, git diff --check y prompts idénticos en STATUS/BACKLOG. No se repite el gate funcional ya verificado porque el código ejecutable conserva el árbol de `0625d79`; commit de cierre identificable por su mensaje `docs(agents): publish pending records and require clean handoffs`, con Refs: T-003.
- Pendiente: merge del dueño. Sin bloqueos técnicos; lint conocido → T-005, códigos de error → T-008, contrato de imágenes → T-012.
- Próximo paso: GitHub → PR #6 → Squash and merge y borrar rama; registrar el squash y activar el prompt preparado de T-004 en STATUS/BACKLOG. T-004 no se inicia hasta el merge.

## Resultado
- PR: [#6](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/6), APROBADO en revisión independiente; merge pendiente.
- Qué cambió: registro sin escalada a ADMIN, administración de usuarios por authority ADMIN, catálogo público solo por GET y CORS con orígenes configurados.
- Tests agregados: AuthorizationIntegrationTest (20) y CorsConfigTest (6); 57 totales. Cobertura sube de 40,80 % a 56,48 %.
- Cierre documental autorizado: se incorporan pendientes previos y reglas de cierre con commit/push y árbol limpio; próxima acción sincronizada en STATUS/BACKLOG y prompt completo de T-004 preparado para después del merge.
- Pendientes: merge 👤; T-004 espera. No se inicia otra tarea.
