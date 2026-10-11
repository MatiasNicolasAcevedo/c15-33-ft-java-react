# Tablero

> **▶ SIGUIENTE: implementar [T-001 · Sacar el secreto y la expiración del JWT a configuración](tasks/T-001-jwt-secret-config.md)**
>
> **Ejecuta: 🧠 FRONTERA — GPT-6.1 Sol o Claude Opus 5.5 · T-001 en curso.**
>
> Prompt: «Leé `AGENTS.md` y seguí `docs/agents/implementer.md` para retomar T-001». Rama: `fix/T-001-jwt-secret-config`; PR draft pendiente. Ver [STATUS](STATUS.md).

**Leyenda:** 🤖 **Lite = Junie Lite (Free) en Air** · 🧠 **Frontera = p. ej. GPT-6.1 Sol / Claude Opus 5.5** · 👤 **Dueño**. Las categorías indican quién implementa; el campo **Revisa** de cada tarea puede pedir otro ejecutor.

> Reglas: **una tarea a la vez** (máx. 1 PR de tarea abierto) · "Listas" es la **cola en orden de ejecución**: se toma siempre la primera ·
> si la primera no te corresponde (🧠 o 👤), no saltees a la siguiente: avisá con el formato de "Traspasos" y terminá ·
> las 👤 que no bloquean nada van al final de "Listas" con "(no bloquea la cola)" ·
> formato: `- T-NNN · Pn · S/M/L · 🤖 Lite|🧠 frontera|👤 dueño · Título → [tarea](tasks/T-NNN-slug.md)`
> Próximo ID libre: T-017

## 🔨 En curso / en revisión (con su PR)
- T-001 · P0 · S · 🧠 frontera · Sacar el secreto y la expiración del JWT a configuración → [tarea](tasks/T-001-jwt-secret-config.md) · `fix/T-001-jwt-secret-config` · PR draft pendiente.

## ✅ Listas (cola en orden de ejecución, máx. 10)
- T-002 · P0 · M · 🧠 frontera · Montar la infraestructura de tests del back → [tarea](tasks/T-002-backend-test-infra.md)
- T-003 · P0 · M · 🧠 frontera · Corregir la autorización: roles en el registro, usuarios solo ADMIN, catálogo público y CORS → [tarea](tasks/T-003-authorization-rules.md)
- T-004 · P0 · S · **🤖 LITE — cambio de ejecutor** · Hacer configurable la URL de la API en el front y usar el prefijo /v1/api → [tarea](tasks/T-004-frontend-api-base-url.md)
- T-005 · P0 · S · 🤖 Lite · Dejar `npm run lint` en verde → [tarea](tasks/T-005-frontend-lint.md)
- T-006 · P0 · S · 🤖 Lite · Corregir el endpoint `DELETE /v1/api/property/{id}` → [tarea](tasks/T-006-delete-property-mapping.md)
- T-007 · P0 · M · 🤖 Lite · Escribir tests de caracterización de `PropertyServiceImp` → [tarea](tasks/T-007-property-service-characterization.md)
- T-008 · P0 · M · 🧠 frontera · Agregar manejo global de errores con ProblemDetail y health check → [tarea](tasks/T-008-error-handling.md)
- T-009 · P0 · M · 🧠 frontera · Montar la infraestructura de tests del front → [tarea](tasks/T-009-frontend-test-infra.md)
- T-014 · P0 · S · 👤 dueño · Proteger la rama por defecto y limpiar ramas (no bloquea la cola) → [tarea](tasks/T-014-branch-protection.md)

## ⛔ Bloqueadas (en el orden en que entrarán a la cola)
- T-010 · P0 · S · 🧠 frontera · Agregar CI (verify por parte) y Dependabot → [tarea](tasks/T-010-ci-workflow.md) · bloqueada por: `verify.sh` en rojo hasta T-005
- T-011 · P0 · S · 🤖 Lite · Mostrar estados de carga, error y vacío en la home → [tarea](tasks/T-011-home-error-state.md) · bloqueada por: T-009 (sin tests de front)
- T-012 · P0 · M · 🧠 frontera · Alinear el contrato de imágenes entre front y back → [tarea](tasks/T-012-image-contract.md) · bloqueada por: T-003, T-004, T-009
- T-015 · P0 · S · 👤 dueño · Rotar el secreto JWT en todo entorno que lo haya usado → [tarea](tasks/T-015-rotate-jwt-secret.md) · bloqueada por: T-001
- T-013 · P0 · M · 🧠 frontera · Montar la infraestructura E2E con Playwright → [tarea](tasks/T-013-e2e-infra.md) · bloqueada por: T-010, T-011, T-012

## 📥 Backlog (sin refinar)
**P0 Estabilizar**
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
- T-016 · P0 · S · 🧠 frontera · Aclarar ejecutores, compactación y traspasos → [tarea](tasks/T-016-agent-models-context.md) · [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) (revisión independiente APROBADA, mergeado por el dueño con squash `47debed`; mantenimiento documental fuera de las 15 tareas de P0).
- T-000 · P0 · L · 🧠 frontera · Relevar el proyecto y montar el harness para agentes → [tarea](tasks/T-000-agent-bootstrap.md) · PR [#2](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/2) (aprobado por sub-agente 🧠 y mergeado con squash)
