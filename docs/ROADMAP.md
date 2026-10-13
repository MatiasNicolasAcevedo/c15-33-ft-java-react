# Roadmap

Fases en orden. Cada una cierra con un tag `v0.x.0` (ver `CONTRIBUTING.md` → Releases). Detalle de tareas en [BACKLOG](BACKLOG.md).

**Estado al 2026-10-13:** T-001 a T-009 mergeadas (9/15 tareas P0). Primero integrar el cierre documental #14; después T-010 (CI) → T-012 (imágenes) → T-011 (home) → T-013 (E2E), con revisión y merge entre tareas. T-012 precede a T-011 para que su test de catálogo no vacío use el contrato real de imágenes. T-014/T-015 las ejecuta el dueño sin bloquear la cola; protección completa, rotación o constancia de ausencia de deploy y hallazgos P0 resueltos siguen siendo necesarios para salir de la fase. Próxima acción y prompts: STATUS/BACKLOG.

## P0 · Estabilizar → `v0.1.0`
**Objetivo:** que funcione, se pueda verificar y sea seguro de cambiar.
**Criterio de salida (verificable):**
- [ ] `./scripts/verify.sh` en verde y CI (`ci.yml`) en verde en `development` (T-005, T-010).
- [ ] README seguido al pie de la letra desde un clon limpio en ambos modos, **sin ⚠️** (T-004, T-011, T-012).
- [ ] Front local habla con el back local; login y alta de propiedad funcionan en el navegador.
- [ ] Sin hallazgos 🔴 abiertos: secreto JWT por config y rotado, registro sin ADMIN, permisos de usuarios (T-001, T-003, T-015).
- [ ] Infra de tests back/front/E2E con umbrales de cobertura (T-002, T-009, T-013) y flujos críticos 1–5 cubiertos (T-006, T-007).
- [ ] Protección de `development` activa (T-014).

## P1 · Actualizar → `v0.2.0`
**Objetivo:** versiones soportadas, en pasos chicos protegidos por la red de P0 ([ADR 0002](adr/0002-migration-path.md)).
**Criterio de salida:** Boot ≥ 3.5 (ideal 4.x) con Java 21, Node 24, Vite/React/Router actuales; `npm audit` sin altas; Dependabot activo con PRs mergeándose; Flyway con baseline.

## P2 · Terminar → `v0.3.0`
**Objetivo:** funcionalidad completa, cada feature con su spec y sus tests.
**Criterio de salida:** reservas (spec `docs/specs/reservations.md`), edición de perfil, imágenes servibles, panel admin con permisos, DTOs + validación + paginación; E2E de cada flujo.

## P3 · Pulir para portfolio → `v1.0.0`
**Objetivo:** presentable.
**Criterio de salida:** responsive revisado a 375/768/1280 con capturas, accesibilidad básica, cobertura ≥ 70 % en servicios y lógica de front, README con capturas/GIF y badges, licencia, demo online.
