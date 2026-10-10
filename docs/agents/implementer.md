# Protocolo · Implementador (una tarea por sesión)

**Lo pueden usar:** 🤖 agente gratuito (IntelliJ) y 🧠 modelo de frontera. Cada uno solo toma tareas cuyo "Ejecuta" sea él.
Seguí estos pasos en orden. Respuestas cortas; no pegues logs largos.

## 1. Orientarte (sin escribir código)
- Leé `AGENTS.md`, `docs/STATUS.md` y `docs/BACKLOG.md`.
- `git status` y `git log --oneline -10`.
- PRs abiertos: `gh pr list` · sin `gh`: `git fetch origin --prune && git ls-remote --heads origin` (una rama `<tipo>/T-NNN-*` = tarea tomada).

## 2. Primero lo que está en vuelo
- Si un PR tiene **cambios pedidos** y es de una tarea tuya: atendelo en su rama (`git switch <rama> && git pull`) y saltá al paso 6.
- Si hay **un PR de tarea abierto** esperando revisión o merge: no tomes otra. Terminá con el Traspaso: quién revisa (según la tarea, campo "Revisa") o «👤 mergear el PR #N».

## 3. Línea de base
```bash
git switch development && git pull && ./scripts/verify.sh
```
Si falla por algo que no es la tarea conocida (hoy: lint del front → T-005), creá o tomá la tarea que lo arregla.

## 4. Tomar la tarea
- Es **la primera** de "✅ Listas" en `docs/BACKLOG.md` (que no tenga PR ni rama remota).
- Si dice `Ejecuta: 🧠` y sos 🤖, o `👤`: **no la tomes ni saltees a otra**. Terminá con el Traspaso (abajo).
- **Antes de cualquier commit**, creá la rama con el nombre exacto del campo "Rama":
```bash
git switch -c <tipo>/T-NNN-slug
```
- Mové la tarea a "🔨 En curso" en `BACKLOG.md` y poné `Estado: En curso` en la tarea. Commit `docs(board): start T-NNN` con footer `Refs: T-NNN`, y push: `git push -u origin <rama>`.
- PR en **draft** con el template (`.github/pull_request_template.md`) y título Conventional Commit (en inglés, p. ej. `fix(client): read API base URL from env`):
  - con `gh`: `gh pr create --draft --base development --title "<título>" --body-file .github/pull_request_template.md`
  - sin `gh` (🤖): dale al dueño el link `https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/compare/development...<rama>?expand=1` y el título y cuerpo listos para pegar; pedile que lo abra como **draft**.

## 5. Contexto mínimo
Leé la tarea y **solo** lo de "Leer antes" y "Archivos previstos", por rangos de líneas. Nada de `docs/audits/`, `docs/archive/`, lockfiles, `target/`, `node_modules/`, `dist/`.

## 6. Implementar con tests
- Respetá "No incluye". Bugfix: **primero el test que falla**, commit, después el fix.
- Entre pasos corré el test más específico (`AGENTS.md` → "Un test"); al final `./scripts/verify.sh`.
- Si agregás o cambiás una variable: `.example`, `docker-compose.yml`, `docs/SETUP.md` y README en el mismo PR.
- Commits atómicos `tipo(alcance): descripción` + `Refs: T-NNN`. `git push` después de cada commit.

## 7. Si el contexto se alarga
Completá "Notas de implementación" (hecho / pendiente / próximo paso), commit y push. Después:
- 🧠: sugerí `/compact Conservar: tarea T-NNN, rama y PR, decisiones, archivos modificados, estado de verify y próximo paso. Descartar: logs y contenido de archivos ya procesados.` y «después escribime "seguí con T-NNN"».
- 🤖: «abrí un chat nuevo y escribí: Seguí `docs/agents/implementer.md` (retomando T-NNN)».

## 8. Reglas duras
No edites los criterios de aceptación · no borres, deshabilites ni debilites tests · no bajes umbrales · no cambies versiones mayores, arquitectura, seguridad, CI ni configuración de producción sin una tarea · no toques secretos ni leas la config real · no commitees a `development` · no mergees. Trabajo extra que descubras → una línea en "📥 Backlog".

## 9. Escalar (no insistas)
Después de **2 intentos fallidos**, una **decisión de diseño** o algo **fuera de alcance**:
1. Commiteá lo que esté sano; en "Notas" escribí qué probaste y qué falló.
2. Pasá la tarea a "⛔ Bloqueadas" con `bloqueada por: requiere 🧠` o `requiere dueño`. **La cola queda detenida en esa tarea.**
3. Comentá en el PR draft (o pasale el texto al dueño) y terminá con el Traspaso, p. ej. «ejecuta: 🧠 · abrí una sesión de Claude Code y escribí: Seguí `docs/agents/implementer.md` para retomar T-NNN».

## 10. Cerrar
- Verificá cada criterio de aceptación y marcalo con su evidencia.
- Completá "Notas" y "Resultado" (PR, qué cambió, tests, pendientes). Tarea a "🏁 Hecho reciente" con el PR (si hay más de 10, la más vieja a `docs/archive/tasks/`).
- `docs/STATUS.md`: salud (verify, cobertura), Foco (PR abierto, próximo paso), "Últimas sesiones" (máx. 5; las viejas a `docs/archive/log/AAAA-MM.md`).
- Descripción del PR completa (resumen de `verify.sh`; capturas 375/1280 px si hay UI) y pasarlo a **ready** (`gh pr ready` o pedírselo al dueño), indicando quién revisa.

## 11. Respuesta final (máx. 5 líneas) + Traspaso
Qué hiciste, estado de verify, link del PR. Después, el Traspaso para la revisión según "Revisa" de la tarea:

> **Próximo paso:** revisar PR #N (T-NNN) · **ejecuta: 🤖 agente gratuito (IntelliJ)** | **🧠 modelo de frontera**
> **Qué hacer:** abrí <un chat nuevo del agente de IntelliJ | una sesión nueva de Claude Code> y escribí: «Seguí `docs/agents/reviewer.md` para el PR #N».

Si te detuviste antes (paso 2, 4 o 9), el Traspaso dice quién sigue y qué escribir. Siempre sugerí **sesión nueva** para lo próximo.
