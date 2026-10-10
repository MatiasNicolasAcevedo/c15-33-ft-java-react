# Protocolo · Revisor (un PR por sesión)

**Lo puede usar:** el ejecutor que indique la tarea en "Revisa" (🤖 agente gratuito en chat nuevo, o 🧠 modelo de frontera). Siempre en una **sesión distinta** a la que implementó.

## 0. ¿Me corresponde?
Abrí la tarea del PR (`docs/tasks/T-NNN-*.md`). Si dice `Revisa: 🧠` y sos 🤖: no revises; terminá con el Traspaso «ejecuta: 🧠 · abrí una sesión nueva de Claude Code y escribí: Seguí `docs/agents/reviewer.md` para el PR #N».

## 1. Leer
`AGENTS.md`, `CONTRIBUTING.md` (§3–§5), `docs/TESTING.md` (§2–§4), la descripción del PR, la tarea y el **diff completo**:
- con `gh`: `gh pr view N` y `gh pr diff N`
- sin `gh`: `git fetch origin <rama> && git diff development...origin/<rama>` (`--stat` primero si es largo)

## 2. Verificar vos mismo (no confíes solo en el PR)
```bash
git switch --detach origin/<rama> && ./scripts/verify.sh
```
Estado de CI: `gh pr checks N` (o la pestaña Checks si no hay `gh`). Corré los comandos de cada criterio de aceptación.

## 3. Revisar, en este orden
1. **Criterios de aceptación:** uno por uno, con evidencia (salida de comando o archivo:línea).
2. **Alcance y rama:** el nombre de la rama es exactamente el de la tarea; nada de "No incluye"; un solo PR de tarea abierto.
3. **Tests:** ¿fallarían si el código estuviera mal? (romper mentalmente una línea); ¿hay tests borrados, `@Disabled`/`.skip`, asserts debilitados o umbral bajado?; ¿bajó la cobertura?; bugfix con test primero.
4. **Corrección:** casos borde, nulls, errores.
5. **Seguridad:** secretos, permisos, entradas sin validar.
6. **Convenciones:** las de `AGENTS.md` §8 y `backend/AGENTS.md` / `frontend/airbnb_clone/AGENTS.md`.
7. **Configuración:** `.example`, compose y `docs/SETUP.md` al día; sin archivos de config reales ni datos reales.
8. **Docs y registro:** tablero, tarea ("Notas" y "Resultado"), `STATUS.md`, título del PR en Conventional Commits, tamaño (< ~400 líneas sin lockfiles).

## 4. Publicar el veredicto
Como review si la cuenta lo permite; si no, **comentario** en el PR (`gh pr comment N --body-file review.md`; sin `gh`, dale el texto al dueño para que lo pegue):

```markdown
**Veredicto:** APROBADO | CAMBIOS REQUERIDOS | ESCALAR A 🧠/DUEÑO
Revisó: <🤖 agente gratuito | 🧠 modelo de frontera> · verify.sh: <resumen>
### Criterios
- ✅/❌ <criterio> — <evidencia>
### Bloqueantes
- `archivo:línea` — problema → corrección sugerida
### Nits (opcionales, máx. 5)
### Fuera de alcance → Backlog
```

## 5. Reglas
- Sugerí, no reescribas: no hagas commits en la rama del PR (salvo que el dueño lo pida).
- Nada fuera de alcance se exige en este PR: va a Backlog.
- **Segunda ronda:** revisá solo lo pedido + regresiones. Si tampoco se aprueba: `ESCALAR A 🧠/DUEÑO`.

## 6. Merge
Lo hace el **dueño** (squash + borrar rama). Excepción: el dueño te pidió explícitamente "revisá y mergeá el PR #N" (modo sub-agente): con `APROBADO` y CI en verde (sin CI: `verify.sh` sin regresiones respecto de `development`), squash merge (`gh pr merge N --squash --delete-branch`). Nunca commitees a `development`.

### 6b. Modo sub-agente
Si un 🧠 te lanzó como sub-agente: trabajá **solo** con este archivo y el PR (no asumas nada de la sesión que te lanzó) y devolvé el veredicto con el formato del paso 4.

## 7. Respuesta final (máx. 5 líneas) + Traspaso
> **Próximo paso:** <👤 mergear el PR #N | atender cambios del PR #N (T-NNN)> · **ejecuta: 👤 dueño | 🤖 agente gratuito (IntelliJ) | 🧠 modelo de frontera** (quien implementó)
> **Qué hacer:** <GitHub → PR #N → Squash and merge | abrí un chat nuevo del agente de IntelliJ / una sesión nueva de Claude Code y escribí: «Seguí `docs/agents/implementer.md` (cambios pedidos en el PR #N)»>.
