# Protocolo · Auditor (auditoría periódica)

**Lo puede usar:** solo 🧠 modelo de frontera.
**Cuándo:** cada 5–10 PR mergeados, antes de un cambio grande (p. ej. una migración de P1) o cuando hay 2+ tareas `requiere 🧠` en "Bloqueadas".
Trabajá **por bloques**; al cerrar cada bloque, commit + push y (opcional) `/compact Conservar: auditoría AAAA-MM-DD, bloque N hecho, hallazgos en docs/audits/AAAA-MM-DD-periodic.md, próximo bloque. Descartar: salidas de comandos.`

## 0. Rama
```bash
git switch development && git pull && git switch -c docs/audit-AAAA-MM-DD
```

## 1. Situación
- Leé `docs/STATUS.md`, `docs/BACKLOG.md` y la última auditoría en `docs/audits/`.
- Cambios desde la anterior: `git log --oneline audit/<fecha-anterior>..development` (la primera vez: desde el merge de T-000) y los PR mergeados con sus revisiones (`gh pr list --state merged --limit 20`).

## 2. Trabajo de los agentes
- ¿Lo mergeado cumple de verdad los criterios? Elegí 2–3 PRs y re-verificá un criterio de cada uno.
- ¿Alguna revisión dejó pasar problemas (tests débiles, alcance, convenciones)?
- **Spot check de tests:** romper una línea de lógica en un servicio cubierto (`git stash` después) y ver si algún test falla. Opcional: PIT (`pitest-maven`) en `property/` y `security/`; Stryker en `src/services/`.

## 3. Salud
- `./scripts/verify.sh` y CI; cobertura vs umbral (**subí el umbral** si la cobertura creció: trinquete, nunca bajar).
- Tests lentos o inestables (`.verify-logs/`, tiempos de surefire).
- **Onboarding desde un clon limpio** en los dos modos del README (contenedores y desarrollo), al pie de la letra; corregí lo que falle y actualizá "Verificado el …".
- `.example` completos y coherentes con compose y `docs/SETUP.md`.
- Deriva documental: versiones, comandos y puertos iguales en README, AGENTS, SETUP, compose y scripts; links rotos.
- `npm audit` y dependencias desactualizadas (resumen).

## 4. Resolver y replanificar
- Decisiones del dueño tomadas desde la última vez → ADR en `docs/adr/` (plantilla `docs/agents/templates/adr.md`).
- Tareas 🧠 prioritarias: **de a una**, cada una en su rama/PR con `implementer.md` (no dentro del PR de la auditoría).
- Relevamiento liviano de lo que cambió (dimensiones de la auditoría inicial que se tocaron).
- Reordená la cola y refiná 5–10 tareas en "Listas" en orden de ejecución, con 3–5 para 🤖 que cumplan la Definición de Listo (S/M, < ~400 líneas y < ~8 archivos, test de referencia existente, "Leer antes" + "Archivos previstos" + `AGENTS.md` + `STATUS.md` + `BACKLOG.md` < ~40 % de 64K tokens: medí con `wc -c`).
- Si algo falla repetidamente (el 🤖 escala por lo mismo, revisiones flojas), ajustá `AGENTS.md`, plantillas o protocolos.

## 5. Cerrar
- Escribí `docs/audits/AAAA-MM-DD-periodic.md`: resumen ejecutivo con semáforo, qué cambió, hallazgos (severidad, evidencia, recomendación, tarea), cobertura y umbral, preguntas abiertas.
- Actualizá `docs/STATUS.md` (salud, última auditoría, foco) y archivá lo viejo (`docs/archive/tasks/`, `docs/archive/log/`).
- PR `docs: periodic audit AAAA-MM-DD` desde `docs/audit-AAAA-MM-DD`. Después del merge, el dueño (o vos si te lo pide) crea el tag:
```bash
git switch development && git pull && git tag audit/AAAA-MM-DD && git push origin audit/AAAA-MM-DD
```

## 6. Respuesta final (máx. 8 líneas) + Traspaso
Semáforo general, top 3 hallazgos, cambios en la cola. Sugerí **sesión nueva** y cerrá con:
> **Próximo paso:** revisar PR de la auditoría · **ejecuta: 👤 dueño** · luego T-NNN · **ejecuta: <🤖 | 🧠>**
> **Qué hacer:** GitHub → PR #N → revisar y mergear; después abrí <chat nuevo del agente de IntelliJ | sesión nueva de Claude Code> y escribí: «Seguí `docs/agents/implementer.md`».
