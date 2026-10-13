# T-014 · Proteger la rama por defecto y limpiar ramas
- Estado: Lista (no bloquea la cola) · Prioridad: P0 · Esfuerzo: S · Ejecuta: 👤 dueño
- Fase: P0 Estabilizar
- Rama: — (configuración en GitHub) · Depende de: merge de T-000 (para que exista `pr-title.yml` en `development`) · Spec/ADR: —
- Revisa: —

## Por qué
G-1/G-2: `development` no tiene protección verificable, hay merges sin PR en el historial y ramas muertas (`feat/add-swagger`, ramas de sesiones de agentes ya mergeadas).

**Disponibilidad revisada 2026-10-13:** T-000 está mergeada y conventional-title existe, por lo que el dueño puede aplicar la protección básica y limpiar ramas ahora. Los checks server/client quedan pendientes hasta que T-010 esté mergeada y sus jobs existan; después debe incorporarlos al ruleset. T-013 agregará e2e. No bloquea la cola de implementación, pero la protección completa sigue siendo requisito de salida de P0. No se da por configurada sin evidencia del dueño.

## Pasos (GitHub web)
1. **Settings → General → Pull Requests:** dejar tildado solo **Allow squash merging** (destildar merge commits y rebase); en "Default commit message" elegir **Pull request title and description**; tildar **Automatically delete head branches**.
2. **Settings → Rules → Rulesets → New ruleset → New branch ruleset:**
   - Name: `protect-development` · Enforcement status: **Active** · Target branches: **Include default branch**.
   - Tildar: **Restrict deletions** · **Require linear history** · **Require a pull request before merging** (Required approvals: **0** mientras seas el único humano; tildar "Require conversation resolution") · **Require status checks to pass** → "Require branches to be up to date" y agregar `conventional-title` (aparece después del primer PR con el workflow); cuando T-010 se mergee, agregar `server` y `client` · **Block force pushes**.
   - Bypass list: vacía (o solo vos, si querés poder arreglar emergencias).
3. (Opcional) **Renombrar `development` → `main`:** Settings → General → Default branch → ✏️. Si lo hacés, pedile a una sesión 🧠 que actualice docs y scripts (`git grep -n development`).

## Limpieza de ramas (terminal, desde la raíz del repo)
Ver qué ramas remotas ya están mergeadas:

```bash
git fetch --prune origin && git branch -r --merged origin/development | grep -vE 'origin/(development|HEAD)'
```

Borrarlas (revisá la lista antes):

```bash
git branch -r --merged origin/development | grep -vE 'origin/(development|HEAD)' | sed 's#origin/##' | xargs -r -n1 git push origin --delete
```

## Criterios de aceptación
- [ ] Un `git push origin development` directo es rechazado
- [ ] Un PR con título `arreglos varios` queda con el check `conventional-title` en rojo
- [ ] `git ls-remote --heads origin` muestra solo `development` y las ramas de PR abiertos
- [ ] Contestá en `docs/STATUS.md` → "Decisiones pendientes" lo de renombrar a `main` (o avisale a la próxima sesión 🧠)

## Resultado
- Fecha: … · Notas: …

## Evidencia de CI para configurar los checks (T-010)
- Workflow `CI`, PR [#15](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/15), rama `ci/T-010-ci-workflow`. Los nombres exactos que el dueño debe requerir son **`server`** y **`client`**, con GitHub Actions como origen, además del `conventional-title` existente. El job `server` exige JaCoCo 40 % mediante `verify.sh --server`; `client` ejecuta la suite con `verify.sh --client` y exige v8 1 % con un paso separado `npm run test:coverage`.
- Primera ejecución [38407632151](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38407632151) verde en ambos jobs sobre `d57b213`: 118 tests back con JDK 17; 6 tests front con Node 24, cobertura 1,65 %. Reportes y logs publicados como `server-reports` y `client-reports`.
- Prueba negativa [38408053751](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38408053751) sobre `d307534`: `client` rechaza una aserción de precio intencionalmente incorrecta, y los logs se suben aun con el test rojo. Se revierte antes del cierre de T-010; ver su tarea para el run final.
- Después del merge de T-010, abrir Settings → Rules → Rulesets → `protect-development` → Require status checks to pass, agregar **`server`** y **`client`** y exigir la rama actualizada. Registrar aquí evidencia de la configuración; este PR no modifica el ruleset ni da T-014 por hecha. E2E se agrega en T-013.
