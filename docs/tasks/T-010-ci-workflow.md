# T-010 · Agregar CI (verify por parte) y Dependabot
- Estado: Hecha (implementación; revisión independiente y merge pendientes en PR #15) · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🧠 modelo de frontera
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
- [x] El PR muestra los jobs `server` y `client` en verde
- [x] Un commit de prueba con un test roto pone el job en rojo (revertirlo; anotarlo en "Notas")
- [x] Badge de CI en el README apuntando a `development`
- [x] En la respuesta final: pedirle al dueño que agregue `server` y `client` como checks requeridos (T-014)

## Notas de implementación
- Preparación 2026-10-13: #13 integrado con squash `dc27563`; línea de base de revisión verde (118 tests back/6 front). El cierre #14 no implementa CI. Al iniciar, revalidar PR abiertos, development y gate según el protocolo.
- Cobertura actual: verify.sh --server exige JaCoCo; verify.sh --client corre lint/build/tests y no mide v8. Para exigir el umbral del front en CI, agregar un paso explícito `npm run test:coverage` desde frontend/airbnb_clone y conservar 1 %. Documentar en el PR qué pasos verifican cada umbral; no confundir la suite verde con la cobertura comprobada.
- Inicio 2026-10-13: sin PR abiertos, árbol limpio y sin cambios heredados. #13/#14 MERGED; development actualizado a `6c71f89`. Línea de base completa verde: 121 s, 118 tests back/6 front, cobertura JaCoCo OK. Rama exacta `ci/T-010-ci-workflow` creada antes del primer commit. PR #15 abierto en draft desde el commit inicial `29ddbd8`. Workflow implementado: SHA de actions oficiales verificados con git ls-remote el 2026-10-13, permisos contents: read, sin credenciales persistidas, cachés Maven/npm, cancelación de runs anteriores, timeouts y artifacts de reportes/logs con always(). La carpeta oculta .verify-logs se incluye explícitamente; no se suben archivos de configuración. Primera ejecución CI [38407632151](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38407632151) sobre `d57b213`: server/client SUCCESS, 118 tests back (83 s, JDK 17), 6 front (14 s, Node 24), cobertura v8 1,65 % y artifacts server-reports/client-reports publicados. actionlint 1.7.12 OK (binario oficial con checksum verificado); YAML de Dependabot validado.
- Actions fijadas por SHA a las versiones oficiales vigentes verificadas el 2026-10-13: [checkout v7.0.1](https://github.com/actions/checkout/releases/tag/v7.0.1) (`3d3c42e`), [setup-java v6.0.1](https://github.com/actions/setup-java/releases/tag/v6.0.1) (`de7274f`), [setup-node v7.1.0](https://github.com/actions/setup-node/releases/tag/v7.1.0) (`949feb2`), [upload-artifact v7.0.2](https://github.com/actions/upload-artifact/releases/tag/v7.0.2) (`cf430e0`). Fuentes de inputs: README oficial de cada action. `.verify-logs` requiere include-hidden-files; las rutas de upload se limitan a reportes y logs, sin configuración real.
- Prueba negativa: commit `d307534b9a73f5edaf7fb1501a0f5205b7d4ba91` cambia únicamente la aserción de precio de $120 a $121 manteniendo value={120}. Archivo individual local exit 1. Run [38408053751](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38408053751): client FAILURE en Verify client (lint/build verdes, test rojo, exit 1); paso de cobertura skipped; upload de logs SUCCESS; server SUCCESS. Revertido mediante `29d72010443fc9f42294bf9c06681b9b2239f196`, sin diff final de tests ni umbrales.
- Dependabot incorporado después del CI verde: Maven /backend, npm /frontend/airbnb_clone y github-actions /, semanal, minor/patch agrupados, mayores individuales para revisión de frontera y prefijos build(deps)/ci(deps). Se activa al integrar la configuración en la rama por defecto; sin auto-merge. [Referencia oficial](https://docs.github.com/en/code-security/reference/supply-chain-security/dependabot-options-reference).
- Badge apunta explícitamente a development. T-014 contiene los nombres exactos de checks server/client, el check de título existente y la acción del dueño después del merge; no se modificó el ruleset. Gate completo final verde (88 s: 118 back/6 front, JDK 21/Node 24), seguido de npm run test:coverage verde (1,65 %). [CI con Dependabot 38408551205](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38408551205) sobre `a56a618cf7edb8e2d624ebbae6d502634b97a877`: server/client SUCCESS. Artifact de la primera ejecución inspeccionado: JaCoCo 517/626 líneas (82,59 %) y .verify-logs/server.log incluido. Sin cambios de tests ni umbrales en el diff final.

## Resultado
- PR: [#15](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/15) · Rama: `ci/T-010-ci-workflow` · Último commit de implementación: `a56a618cf7edb8e2d624ebbae6d502634b97a877`; cierre documental posterior en el HEAD del PR (consultar `gh pr view 15 --json headRefOid`).
- Hecho: CI server/client, cobertura separada, actions oficiales verificadas y fijadas por SHA, artifacts siempre, cachés, permisos mínimos, Dependabot semanal agrupado y badge development. Evidencias verdes/rojas y revert registradas arriba; actionlint y YAML OK. Tarea/STATUS/BACKLOG/T-014 sincronizados y todos los cambios propios se guardan con commit/push; sin cambios heredados ni bloqueos.
- Pendiente: revisión independiente de 🧠 en sesión nueva y merge del dueño; después configurar los checks **server** y **client** en T-014. La cola queda detenida en #15; T-012 no iniciada.
- Próxima acción concreta: `gh pr view 15`, comprobar HEAD/checks y seguir `docs/agents/reviewer.md`. Prompt: «Leé AGENTS.md, docs/STATUS.md, docs/BACKLOG.md y docs/tasks/T-010-ci-workflow.md. Seguí docs/agents/reviewer.md para revisar de forma independiente el PR #15, T-010, rama ci/T-010-ci-workflow, base development. Comprobá el HEAD publicado, los jobs server/client y cobertura, las versiones/SHA de actions, Dependabot y la evidencia del test roto revertido. No mergees ni inicies T-012.»
