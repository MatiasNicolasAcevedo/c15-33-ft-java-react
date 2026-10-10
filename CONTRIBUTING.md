# Cómo contribuir

Estable. Aplica a personas y agentes. Ejecutores: 🧠 modelo de frontera · 🤖 agente gratuito (IntelliJ) · 👤 dueño (ver [AGENTS.md](AGENTS.md)).

> La rama por defecto es **`development`** (equivale a `main` en GitHub Flow). Renombrarla es una decisión pendiente del dueño.

## 1. Ramas (GitHub Flow)

- `development` siempre en verde; **nadie commitea directo** a ella.
- **Antes del primer commit de cualquier tarea se crea su rama** desde `development` actualizado, con el nombre exacto de la tarea:

```bash
git switch development && git pull && git switch -c <tipo>/T-NNN-slug-corto
```

- Tipos: `feat`, `fix`, `test`, `refactor`, `chore`, `docs`, `ci`, `build`, `perf`. Vida corta; si `development` avanzó: `git fetch origin && git rebase origin/development` (solo en tu rama).

## 2. Commits (Conventional Commits)

```
tipo(alcance): descripción en imperativo, en inglés

Cuerpo opcional: el porqué.

Refs: T-NNN
```

- Atómicos (un cambio lógico por commit). Alcances usados: `server`, `client`, `board`, `docs`, `ci`.
- Incompatibles: `feat(server)!: …` + `BREAKING CHANGE: …` en el footer.
- Prohibido: "wip", "fix", "cambios", "Update README.md".

## 3. Pull requests

- **Uno por tarea, chico** (< ~400 líneas sin lockfiles ni generados) y **uno abierto a la vez**: no se empieza otra tarea hasta que el PR anterior se mergea o se cierra.
- **Título = Conventional Commit** (lo valida `.github/workflows/pr-title.yml`; se mergea con squash).
- Se abre en **draft apenas se crea la rama** (primer commit: `docs(board): start T-NNN`) y pasa a "ready" con `./scripts/verify.sh` en verde y el [template](.github/pull_request_template.md) completo.
- Sin `gh` (🤖): `git push -u origin <rama>` y el dueño abre el PR desde `https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/compare/development...<rama>?expand=1`.

## 4. Revisión

En una **sesión nueva** con [`docs/agents/reviewer.md`](docs/agents/reviewer.md). La tarea dice en "Revisa" quién revisa.

| PR | Implementa | Revisa | Quién lanza la revisión |
|---|---|---|---|
| Tarea 🤖 S, sin riesgo | 🤖 agente gratuito | 🤖 agente gratuito, **chat nuevo** | 👤 abre el chat nuevo |
| Tarea 🤖 M, o que toca back y front, seguridad, datos, configuración o CI | 🤖 agente gratuito | 🧠 modelo de frontera | 👤 abre una sesión de frontera |
| Tarea 🧠 | 🧠 modelo de frontera | 🧠 sesión nueva o sub-agente (si el dueño lo pide) + 👤 | 👤 |
| Dependabot | bot | 🤖; versión mayor → tarea 🧠 | 👤 |

- Máximo **2 rondas**; después se escala a 🧠 o al dueño.
- Sin cuenta bot, la revisión es un **comentario** con veredicto explícito: `APROBADO` / `CAMBIOS REQUERIDOS` / `ESCALAR A 🧠/DUEÑO`.
- Quien implementó termina diciéndole al dueño exactamente qué abrir y qué escribir (formato "Traspasos" de `AGENTS.md`).

## 5. Merge

Squash, con CI en verde (mientras no haya CI: `verify.sh` sin regresiones respecto de `development`) y revisión aprobada. Lo hace el **dueño**, o el 🧠 solo cuando el dueño se lo pide explícitamente para ese PR ("revisá y mergeá el PR #N": sub-agente revisor). Después: borrar la rama.

## 6. Protección de la rama por defecto

Tarea 👤 [T-014](docs/tasks/T-014-branch-protection.md): ruleset con PR obligatorio, checks requeridos, rama actualizada, historia lineal, sin force push; solo squash; borrar ramas al mergear; y limpieza de ramas mergeadas.

## 7. Releases

Al cerrar cada fase del [ROADMAP](docs/ROADMAP.md): tag `v0.x.0` y notas automáticas.

```bash
gh release create v0.1.0 --generate-notes --target development
```

## 8. Dependabot

Maven, npm y GitHub Actions; semanal; minor/patch agrupados; prefijos `build(deps)` / `ci(deps)` (compatibles con el check de título). **Se activa solo con CI en verde** (T-010 trae el YAML).

## 9. Configuración de ejemplo (regla permanente)

**Todo archivo de configuración con valores por entorno tiene su `.example` commiteado y el archivo real está en `.gitignore`.** Todo cambio que agregue o renombre una variable actualiza, en el mismo PR: su `.example` (con comentario y valor local válido), `docker-compose.yml`, `docs/SETUP.md` (tabla de variables) y el README si cambia un paso. Secretos de ejemplo: inventados y solo locales (`openssl rand -base64 32`).
