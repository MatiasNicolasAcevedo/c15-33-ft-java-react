# Tablero

> **▶ IMPLEMENTAR T-010:** 🧠 Frontera — GPT-6.1 Sol · rama `ci/T-010-ci-workflow` · PR draft pendiente. #13/#14 mergeados; gate completo de inicio verde.
>
> **Prompt:** «Leé AGENTS.md, docs/STATUS.md y docs/tasks/T-010-ci-workflow.md. Seguí docs/agents/implementer.md retomando T-010 en ci/T-010-ci-workflow; abrir su PR draft y continuar con CI, pruebas de jobs y test roto. No iniciar T-012.»

**Leyenda:** 🤖 **Lite = Junie Lite (Free) en Air** · 🧠 **Frontera = p. ej. GPT-6.1 Sol / Claude Opus 5.5** · 👤 **Dueño**. Las categorías indican quién implementa; el campo **Revisa** de cada tarea puede pedir otro ejecutor.

> Reglas: **una tarea a la vez** (máx. 1 PR de tarea abierto) · "Listas" es la **cola en orden de ejecución**: se toma siempre la primera ·
> si la primera no te corresponde (🧠 o 👤), no saltees a la siguiente: avisá con el formato de "Traspasos" y terminá ·
> las 👤 que no bloquean nada van al final de "Listas" con "(no bloquea la cola)" ·
> formato: `- T-NNN · Pn · S/M/L · 🤖 Lite|🧠 frontera|👤 dueño · Título → [tarea](tasks/T-NNN-slug.md)`
> Próximo ID libre: T-017

## 🔨 En curso
- T-010 · P0 · S · 🧠 frontera · Agregar CI y Dependabot → [tarea](tasks/T-010-ci-workflow.md) · `ci/T-010-ci-workflow`, PR draft pendiente.

## ✅ Listas (cola en orden de ejecución, máx. 10)
- T-012 · P0 · M · 🧠 frontera · Alinear el contrato de imágenes entre front y back → [tarea](tasks/T-012-image-contract.md) · desbloqueada por los merges de T-003/T-004/T-009; segunda en la cola, antes de T-011.
- T-014 · P0 · S · 👤 dueño · Proteger la rama por defecto y limpiar ramas (no bloquea la cola) → [tarea](tasks/T-014-branch-protection.md) · protección básica disponible; agregar checks server/client después del merge de T-010.
- T-015 · P0 · S · 👤 dueño · Rotar el secreto JWT en todo entorno que lo haya usado (no bloquea la cola) → [tarea](tasks/T-015-rotate-jwt-secret.md) · desbloqueada por el merge de T-001; confirmar si existe deploy activo.

## ⛔ Bloqueadas (en el orden en que entrarán a la cola)
- T-011 · P0 · S · 🤖 Lite · Mostrar estados de carga, error y vacío en la home → [tarea](tasks/T-011-home-error-state.md) · bloqueada por: merge de T-012. T-004/T-009 satisfechas; el caso de catálogo no vacío necesita el contrato de imágenes alineado.
- T-013 · P0 · M · 🧠 frontera · Montar la infraestructura E2E con Playwright → [tarea](tasks/T-013-e2e-infra.md) · bloqueada por: T-010, T-011, T-012

**Orden de implementación revisado:** T-010 (🧠) → T-012 (🧠) → T-011 (🤖) → T-013 (🧠), con revisión y merge entre tareas. Al mergear T-012, mover T-011 a la primera posición de Listas, antes de las tareas del dueño; al integrar T-011 y estar T-010/T-012 mergeadas, hacer lo mismo con T-013. T-014/T-015 las ejecuta el dueño sin bloquear esta cola, pero siguen siendo requisitos de salida de P0.

**Motivo del orden:** el fixture real de T-009 expone `imageUrls`; `CardContainer` pasa `property.images` y `HostCardItem` accede a `images[0]`. El caso exitoso de T-011 rompe por una causa de T-012. Se resuelve primero en frontera, sin pedir a Lite que cambie el contrato ni que simule una respuesta incompatible con la API.

## 📥 Backlog (sin refinar)
**P0 Estabilizar**
- 🧠 Corregir cascada de borrado en `Property.user`: `@Cascade(ALL)` elimina al OWNER al borrar su propiedad. Preexistente, confirmado en probes de T-008; requiere tarea de datos y test de integración que preserve al usuario.
- 🧠 Diagnosticar demora de cierre de Surefire/Testcontainers en Windows: T-002 pasa 31 tests y limpia los contenedores, pero Surefire termina el fork tras 30 s de espera al cerrar la JVM.
- 🧠 Fijar LF para scripts Bash y `backend/mvnw` con `.gitattributes`: en Windows el checkout CRLF rompe el build Docker con `./mvnw: not found` (detectado en T-001; normalizado solo localmente para validar).
- 🤖 Default `8080` para `APP_PORT` (`application.properties:2`) (X-2)
- 🤖 Caracterización: un test por flujo crítico sin cubrir (auditoría → Testing, flujos 6–8) — refinar tras T-009/T-013
- 🧠 Revisar `useEffect` sin dependencias que llama a un setter (`Forms.jsx:199`) y los `eslint-disable` de T-005
- 🧠 `.claude/settings.json`: negar también `git push origin HEAD:development` y variantes con refspec (revisión del PR #2)

**P1 Actualizar** ([ADR 0002](adr/0002-migration-path.md))
- 🧠 Spring Boot 3.2 → 3.5.x · 🧠 Java 17 → 21 · 🧠 Boot 3.5 → 4.x con OpenRewrite · 🧠 jjwt 0.11 → 0.13 (propiedad `jjwt.version`)
- 🤖 `npm audit fix` sin `--force` · 🧠 ESLint 9 flat config · 🧠 Vite 8 · 🧠 React 19 · 🧠 React Router 7 · 🧠 Tailwind 4 (evaluar Material Tailwind)
- 🧠 Flyway con baseline y `ddl-auto=validate` (A-5)
- 🤖 Quitar `swagger-models` explícito (D-2) · 🤖 Borrar restos WAR (`ServletInitializer`, `webapp/META-INF/context.xml`) (A-11)
- 🤖 Logger SLF4J en lugar de `System.out` (A-10) · 🤖 `show-sql` solo en local; propiedades Hikari reales (A-6, A-7)
- 🧠 Config sin `env.properties` en el classpath: perfil `local` + variables de entorno (S-7)
- 🧠 Spotless/Checkstyle en el back (Q-2) · 🤖 `engines` en `package.json` (V-2)

**P2 Terminar**
- 🧠 Spec y tareas de reservas (`docs/specs/reservations.md`) + "mis viajes" reales
- 🤖 Reactivar `PUT /v1/api/user/{id}` (edición de perfil) tras T-003/T-008
- 🧠 DTOs en todas las respuestas (`Image`, `Location`) (A-1) · 🧠 Bean Validation `@Valid` (A-3)
- 🧠 Permisos con `@AuthenticationPrincipal` en lugar de validar el JWT a mano (A-4)
- 🧠 Paginación + `@EntityGraph` (A-8) · 🧠 Imágenes fuera de la DB y validación de tipo/tamaño (A-9, S-9)
- 🧠 Rutas protegidas en el front (F-3) · 🧠 Cliente HTTP único (F-4) · 🤖 Quitar ruta `/test` y `Test.jsx` (F-5)
- 🧠 Token en cookie `HttpOnly` (S-8, ADR) · 🧠 Panel admin con permisos reales

**P3 Pulir**
- 🧠 Revisión responsive 375/768/1280 con capturas y accesibilidad básica · 🤖 Code splitting por ruta (F-6)
- 🧠 Migración gradual a TypeScript (F-7) · 🤖 Limpiar comentarios didácticos y nombres mezclados (Q-3)
- 👤 Licencia · 🧠 README con capturas/GIF nuevos y badges · 🧠 Demo online · 🧠 `v1.0.0`
- 🧠 Evaluar mover a `client/` + `server/` ([ADR 0004](adr/0004-keep-folder-layout.md))

## 🏁 Hecho reciente (máx. 10; las más viejas → archive/tasks/)
- T-009 · P0 · M · 🧠 frontera · Infra de tests del front → [tarea](tasks/T-009-frontend-test-infra.md) · [PR #13](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/13) · **revisión independiente APROBADA y mergeada por el dueño** con squash `dc27563`; 6 tests, cobertura global 1,65 %, umbral 1 %, rechazo a 100 % y gates completo/client verdes. Cierre documental mergeado en #14 (`6c71f89`); T-012 desbloqueada, T-011 espera T-012.
- T-008 · P0 · M · 🧠 frontera · ProblemDetail y health → [tarea](tasks/T-008-error-handling.md) · [PR #11](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11) · **revisión independiente APROBADA y mergeada por el dueño** con squash `1b8ec82`; 118 tests, gate completo ✅, cobertura de revisión 82,59 %, PropertyServiceImp 95,07 %, compose/probes OK. Límites de repetición manual registrados en la tarea; cierre documental mergeado en #12 (`793fa11`).
- T-007 · P0 · M · 🤖 Lite + corrección 🧠 · Tests de PropertyServiceImp → [tarea](tasks/T-007-property-service-characterization.md) · [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) · revisión independiente APROBADA y squash `e1b07c4`; 77 tests, gate completo verde, cobertura global 76,20 %.
- T-006 · P0 · S · 🤖 Lite · Corregir el endpoint `DELETE /v1/api/property/{id}` → [tarea](tasks/T-006-delete-property-mapping.md) · [PR #9](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/9) · **mergeada con squash**; endpoint mapeado por ID, 2 tests agregados, verify.sh --server ✅.
- T-005 · P0 · S · 🤖 Lite · Dejar `npm run lint` en verde → [tarea](tasks/T-005-frontend-lint.md) · [PR #8](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/8) · **mergeada con squash `9dfe037`**; 93 problemas de lint resueltos, lint ✅, build ✅.
- T-004 · P0 · S · 🤖 Lite · Hacer configurable la URL de la API en el front y usar el prefijo /v1/api → [tarea](tasks/T-004-frontend-api-base-url.md) · [PR #7](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7) · **mergeada con squash `c648fbd`**; build ✅, sin IPs hardcodeadas, bundle con localhost:8080/v1/api.
- T-003 · P0 · M · 🧠 frontera · Corregir la autorización → [tarea](tasks/T-003-authorization-rules.md) · [PR #6](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/6) · **APROBADA en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/6#issuecomment-6105109999) y mergeada**; 57 tests OK, cobertura 56,48 % (base 40,80 %), umbral 40 %; probes curl y compose OK; gate sin regresiones (93 problemas de lint de T-005).
- T-002 · P0 · M · 🧠 frontera · Montar la infraestructura de tests del back → [tarea](tasks/T-002-backend-test-infra.md) · [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5) · **APROBADA en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5#issuecomment-6104882015) y mergeada por el dueño con squash `da3e6e6`**; 31 tests OK sin config/compose, cobertura 40,80 % con umbral 40 %, `--server` verde; gate completo sin regresiones (93 problemas de lint de T-005).
- T-001 · P0 · S · 🧠 frontera · Sacar el secreto y la expiración del JWT a configuración → [tarea](tasks/T-001-jwt-secret-config.md) · [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) · **APROBADA en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817) y mergeada por el dueño con squash `63e5bc2`**; server 25 tests OK, cobertura 23,86 %, compose y login demo OK, sin regresiones en el gate.
- T-016 · P0 · S · 🧠 frontera · Aclarar ejecutores, compactación y traspasos → [tarea](tasks/T-016-agent-models-context.md) · [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) (revisión independiente APROBADA, mergeado por el dueño con squash `47debed`; mantenimiento documental fuera de las 15 tareas de P0).
