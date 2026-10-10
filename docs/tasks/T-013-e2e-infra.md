# T-013 · Montar la infraestructura E2E con Playwright
- Estado: Bloqueada (por T-010, T-011 y T-012) · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `test/T-013-e2e-infra` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-010, T-011, T-012 · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
No hay ninguna prueba de punta a punta; los flujos críticos 1, 3 y 5 (login, alta de propiedad, home/detalle) solo se verificaron a mano.

## Objetivo
`./scripts/e2e.sh` levanta `docker compose --profile app`, crea el usuario demo y corre 3 smoke tests en desktop (1280) y mobile (375), con screenshots y traces en fallos; un job `e2e` en CI.

## Alcance
- Incluye: carpeta `e2e/` en la raíz con su `package.json` y `playwright.config.ts` (`baseURL` http://localhost:8081), `scripts/e2e.sh`, job en `ci.yml` (artefactos del report), `docs/TESTING.md` y `AGENTS.md`.
- **No incluye:** tests de cada pantalla, visual regression.

## Tests
- Home carga y muestra al menos una propiedad (sembrada por API en `globalSetup`).
- Login con el usuario demo → el navbar muestra la sesión.
- OWNER crea una propiedad con imagen → aparece en "mis propiedades".

## Criterios de aceptación
- [ ] `./scripts/e2e.sh` pasa desde un clon limpio y deja el report en `e2e/playwright-report/`
- [ ] `npx playwright test tests/home.spec.ts` corre un solo spec (desde `e2e/`)
- [ ] Job `e2e` verde en el PR
- [ ] README y TESTING actualizados

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Pendientes → …
