# T-010 · Agregar CI (verify por parte) y Dependabot
- Estado: Bloqueada (por T-005: `verify.sh` todavía no pasa) · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `ci/T-010-ci-workflow` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-005 (y T-002 para el reporte de cobertura) · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
🔴 C-1: no hay CI. Regla: se agrega solo cuando `./scripts/verify.sh` pasa en `development`; Dependabot solo con CI en verde.

## Objetivo
Cada push a `development` y cada PR corren `verify.sh --server` y `--client`; Dependabot abre PRs semanales agrupados.

## Alcance
- Incluye: `.github/workflows/ci.yml`, `.github/dependabot.yml`, badge de CI en el README, checks requeridos en T-014 (avisar al dueño).
- **No incluye:** E2E (T-013), deploy.

## Leer antes
- `scripts/verify.sh` · `CONTRIBUTING.md` §8 · `docs/TESTING.md` §5

## Archivos previstos
- crear: `.github/workflows/ci.yml`, `.github/dependabot.yml` · modificar: `README.md` (badge), `docs/STATUS.md`

## YAML propuesto (ajustar y probar en el PR)
```yaml
name: CI
on:
  push: { branches: [development] }
  pull_request:
jobs:
  server:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v7
      - uses: actions/setup-java@v6
        with: { distribution: temurin, java-version: '17', cache: maven }
      - run: ./scripts/verify.sh --server
      - if: always()
        uses: actions/upload-artifact@v7
        with: { name: server-reports, path: "backend/target/site/jacoco\nbackend/target/surefire-reports\n.verify-logs" }
  client:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v7
      - uses: actions/setup-node@v7
        with: { node-version-file: frontend/airbnb_clone/.nvmrc, cache: npm, cache-dependency-path: frontend/airbnb_clone/package-lock.json }
      - run: ./scripts/verify.sh --client
      - if: always()
        uses: actions/upload-artifact@v7
        with: { name: client-logs, path: .verify-logs }
```
```yaml
# .github/dependabot.yml
version: 2
updates:
  - { package-ecosystem: maven, directory: /backend, schedule: { interval: weekly }, groups: { minor-patch: { update-types: [minor, patch] } }, commit-message: { prefix: "build(deps)" } }
  - { package-ecosystem: npm, directory: /frontend/airbnb_clone, schedule: { interval: weekly }, groups: { minor-patch: { update-types: [minor, patch] } }, commit-message: { prefix: "build(deps)" } }
  - { package-ecosystem: github-actions, directory: /, schedule: { interval: weekly }, commit-message: { prefix: "ci(deps)" } }
```
Versiones mayores vigentes al 2026-10-10 (`git ls-remote --tags`); volver a chequearlas al implementar.

## Criterios de aceptación
- [ ] El PR muestra los jobs `server` y `client` en verde
- [ ] Un commit de prueba con un test roto pone el job en rojo (revertirlo; anotarlo en "Notas")
- [ ] Badge de CI en el README apuntando a `development`
- [ ] En la respuesta final: pedirle al dueño que agregue `server` y `client` como checks requeridos (T-014)

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Pendientes → …
