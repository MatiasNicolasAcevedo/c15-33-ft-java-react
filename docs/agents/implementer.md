# Protocolo · Implementador (una tarea por sesión)

**Lo pueden usar:** 🤖 Junie Lite (Free) en Air y 🧠 modelo de frontera (p. ej. GPT-6.1 Sol / Claude Opus 5.5). Cada uno solo toma tareas cuyo "Ejecuta" sea él. Elegí la configuración correcta del modelo; el nombre de la aplicación no define el ejecutor.
Seguí estos pasos en orden. Respuestas cortas; no pegues logs largos.

## 1. Orientarte (sin escribir código)
- Leé `AGENTS.md`, `docs/STATUS.md` y `docs/BACKLOG.md`.
- `git status` y `git log --oneline -10`.
- Inventariá los cambios previos y resolvé su destino según `AGENTS.md` → **Cierre y cambios sin commit**; no los arrastres a una tarea nueva ni los incluyas sin revisar.
- PRs abiertos: `gh pr list` · sin `gh`: `git fetch origin --prune && git ls-remote --heads origin` (una rama `<tipo>/T-NNN-*` = tarea tomada).

## 2. Primero lo que está en vuelo
- **Retoma explícita de T-NNN en curso:** leé sus Notas de implementación y comprobá rama, último commit y PR. Si sigue en implementación (PR draft o todavía pendiente), retomá **la misma rama y PR** y saltá al paso 5, luego 6. No crees otra rama ni otro PR ni tomes otra tarea. Si el PR ya espera revisión/merge, hacé el traspaso correspondiente.
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
- Si el entorno de nube impide pushear ese nombre, aplicá únicamente la excepción **autorizada explícitamente por el dueño** de `AGENTS.md` / `docs/SETUP.md`; registrá evidencia y rama real en tarea, `STATUS.md` y PR. Un fallo de autenticación o conectividad por sí solo no justifica cambiar de nombre.
- Mové la tarea a "🔨 En curso" en `BACKLOG.md` y poné `Estado: En curso` en la tarea. Actualizá la tarjeta de `STATUS.md` y el aviso superior de `BACKLOG.md` con **Implementar/Retomar T-NNN**, ejecutor, rama y PR pendiente; no anuncies otra tarea mientras esta siga en vuelo. Commit `docs(board): start T-NNN` con footer `Refs: T-NNN`, y push: `git push -u origin <rama>`.
- PR en **draft** con el template (`.github/pull_request_template.md`) y título Conventional Commit (en inglés, p. ej. `fix(client): read API base URL from env`):
  - con `gh`: `gh pr create --draft --base development --title "<título>" --body-file .github/pull_request_template.md`
  - sin `gh` ni integración de GitHub: dale al dueño el link `https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/compare/development...<rama>?expand=1` y el título y cuerpo listos para pegar; pedile que lo abra como **draft**. Esta alternativa aplica a cualquier ejecutor sin esas herramientas.

## 5. Contexto mínimo
Leé la tarea y **solo** lo de "Leer antes" y "Archivos previstos", por rangos de líneas. Nada de `docs/audits/`, `docs/archive/`, lockfiles, `target/`, `node_modules/`, `dist/`.

## 6. Implementar con tests
- Respetá "No incluye". Bugfix: **primero el test que falla**, commit, después el fix.
- Entre pasos corré el test más específico (`AGENTS.md` → "Un test"); al final `./scripts/verify.sh`.
- Si agregás o cambiás una variable: `.example`, `docker-compose.yml`, `docs/SETUP.md` y README en el mismo PR.
- Commits atómicos `tipo(alcance): descripción` + `Refs: T-NNN`. `git push` después de cada commit.

## 7. Si el contexto se alarga
Aplicá `AGENTS.md` → **Gestión de contexto**: solo durante la misma tarea larga, en un corte seguro; alertas orientativas ~60 % 🧠 / ~50 % 🤖 si la interfaz muestra el dato, o señales de pérdida de contexto. No compactes entre tareas ni justo antes de terminar.

Antes de continuar, guardá el checkpoint: notas (hecho, pendiente, decisiones, archivos, pruebas/verify, bloqueos y próxima acción), rama/PR, último commit, `STATUS.md`, commit y push. Si falla el push, registrá que el checkpoint es local.

- 🧠: compactá **si la interfaz lo permite** (Codex CLI: `/compact`); si no, abrí un chat nuevo con el mismo ejecutor.
- 🤖: abrí un chat nuevo de **Junie Lite (Free)** después de guardar el checkpoint.
- En ambos casos, usá el prompt de retoma de `AGENTS.md`: **misma T-NNN, rama y PR**; no empieces la próxima de "Listas". La retoma entra por el paso 2 de este protocolo.

## 8. Reglas duras
No edites los criterios de aceptación · no borres, deshabilites ni debilites tests · no bajes umbrales · no cambies versiones mayores, arquitectura, seguridad, CI ni configuración de producción sin una tarea · no toques secretos ni leas la config real · no commitees a `development` · no mergees. Trabajo extra que descubras → una línea en "📥 Backlog".

## 9. Escalar (no insistas)
Después de **2 intentos fallidos**, una **decisión de diseño** o algo **fuera de alcance**:
1. Commiteá lo que esté sano; en "Notas" escribí qué probaste y qué falló.
2. Pasá la tarea a "⛔ Bloqueadas" con `bloqueada por: requiere 🧠` o `requiere dueño`. **La cola queda detenida en esa tarea.**
3. Actualizá la próxima acción de `STATUS.md` y el aviso superior de `BACKLOG.md` con **Retomar T-NNN**, el bloqueo y el ejecutor que debe resolverlo. Comentá en el PR draft (o pasale el texto al dueño) y terminá con el Traspaso, p. ej. «ejecuta: 🧠 Frontera — GPT-6.1 Sol / Claude Opus 5.5 · abrí una sesión nueva con ese modelo y escribí: Leé `AGENTS.md` y seguí `docs/agents/implementer.md` para retomar T-NNN».

## 10. Cerrar
- Verificá cada criterio de aceptación y marcalo con su evidencia.
- Completá "Notas" y "Resultado" (PR, qué cambió, tests, pendientes). Tarea a "🏁 Hecho reciente" con el PR (si hay más de 10, la más vieja a `docs/archive/tasks/`).
- `docs/STATUS.md`: salud (verify, cobertura), Foco (PR abierto), "Últimas sesiones" (máx. 5; las viejas a `docs/archive/log/AAAA-MM.md`). En la tarjeta **▶ Próxima acción** y el aviso superior de `BACKLOG.md`, poné **Revisar PR #N**, su tarea y el ejecutor del campo "Revisa"; la siguiente implementación espera al merge del dueño.
- Después de actualizar los documentos, revisá el diff y agregá rutas explícitas; hacé el commit de cierre y push. Comprobá `git status --short` y que HEAD coincide con la rama publicada. No pases el PR a ready ni cierres el traspaso con cambios propios sin publicar; para heredados o bloqueos, aplicá y registrá la regla de `AGENTS.md` → **Cierre y cambios sin commit**.
- Descripción del PR completa (resumen de `verify.sh`; capturas 375/1280 px si hay UI) y pasarlo a **ready** (`gh pr ready` o pedírselo al dueño), indicando quién revisa.

## 11. Respuesta final (máx. 5 líneas) + Traspaso
Qué hiciste, estado de verify, link del PR. Después, el Traspaso para la revisión según "Revisa" de la tarea:

> **Próximo paso:** revisar PR #N (T-NNN) · **ejecuta: <🤖 Junie Lite (Free) | 🧠 Frontera — GPT-6.1 Sol / Claude Opus 5.5>**
> **Qué hacer:** abrí <un chat nuevo de Junie Lite en Air | una sesión nueva con el modelo de frontera elegido> y escribí: «Leé `AGENTS.md` y seguí `docs/agents/reviewer.md` para el PR #N».

Completá las alternativas con **un solo ejecutor**. Si te detuviste antes (paso 2, 4 o 9), el Traspaso dice quién sigue y qué escribir. Siempre sugerí **sesión nueva** para lo próximo.

Si quien sigue es **🤖 Junie Lite (Free)**, entregá además el prompt completo en un bloque `text`, listo para copiar y pegar, con tarea, archivo, protocolo y rama/PR cuando correspondan, según `AGENTS.md` → Traspasos. Reflejá ese mismo prompt en `STATUS.md` y `BACKLOG.md`.
