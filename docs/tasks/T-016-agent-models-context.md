# T-016 · Aclarar ejecutores, compactación y traspasos de agentes

- Estado: En curso · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🧠 modelo de frontera
- Fase: Mantenimiento documental (fuera de las 15 tareas de P0 Estabilizar)
- Rama: `docs/T-016-agent-models-context` · Depende de: — · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤 dueño

## Por qué

El dueño pidió corregir la identificación del agente gratuito a Junie Lite (Free), precisar qué modelos corresponden a frontera y hacer visibles la próxima acción y las condiciones para compactar y retomar. Después aportó el traspaso de Claude Code y pidió publicar las correcciones antes de continuar.

## Objetivo

Las instrucciones, tareas y tablero distinguen Lite y frontera, permiten retomar la misma tarea con su rama y PR, y muestran quién ejecuta el siguiente paso.

## Alcance

- Incluye: nomenclatura de agentes, procedimiento de contexto, tarjetas de próxima acción, prompt de inicio y excepción autorizada de rama en nube; alinear la revisión de T-004 con la matriz existente de configuración.
- **No incluye:** implementar T-001, código de la app, dependencias, scripts, CI, Dockerfiles, compose, configuración real, cambios de datos o merge.
- Esta corrección se atiende antes de la cola por pedido explícito del dueño. T-001 conserva el primer lugar de "Listas"; no se toman dos tareas a la vez.

## Leer antes

- `AGENTS.md` §1, §4 y §5.
- `CONTRIBUTING.md` §1–§5.
- `docs/STATUS.md` y `docs/BACKLOG.md`.
- `docs/agents/implementer.md` y `reviewer.md`.
- `docs/SETUP.md` → agentes y prompt de inicio.

## Archivos previstos

- Modificar: `AGENTS.md`, `CONTRIBUTING.md`, `README.md`, `docs/SETUP.md`, `docs/STATUS.md`, `docs/BACKLOG.md`, protocolos y plantilla de tarea, encabezados de tareas Lite.
- Corregir explícitamente el supuesto no verificado del ejecutor gratuito en `docs/audits/2026-10-10-initial.md`, conservando sus hallazgos y pruebas históricas.
- Crear: este contrato de tarea.

## Verificación

- Sin tests nuevos: el cambio es documental. No modificar ni debilitar tests existentes.
- `git diff --check`, enlaces locales y búsqueda de nombres/supuestos retirados.
- Comprobar que el diff solo incluya Markdown y que "Ejecuta" / "Revisa" coincidan con la matriz.
- Intentar `scripts/verify.sh` y registrar el resultado real; el lint previo sigue pendiente de T-005. No atribuir resultados históricos a esta sesión.

## Criterios de aceptación

- [ ] No quedan referencias a Qwen ni al tamaño de contexto no verificado en la documentación operativa o su corrección histórica.
- [ ] Lite significa Junie Lite (Free); frontera incluye GPT-6.1 Sol / Claude Opus 5.5 y se distingue del agente/interfaz.
- [ ] Compactación y chat nuevo tienen checkpoint y prompt de retoma de la misma tarea, rama y PR.
- [ ] STATUS y BACKLOG muestran revisión/merge del PR documental antes de implementar T-001, con ejecutor y prompt.
- [ ] La excepción de rama en nube exige autorización explícita y evidencia; las instrucciones Docker de nube se distinguen de Windows local.
- [ ] Formato y enlaces locales verificados; diff limitado a Markdown; estado de verify documentado.

## Notas de implementación

- Hecho: revisión y correcciones locales; rama renombrada al nombre exacto antes del primer commit; `origin/development` verificado sin divergencia.
- Pendiente: commit, push, PR draft y validación final.
- Ejecutor/modelo: 🧠 Codex / GPT-6.1 Sol · Rama: `docs/T-016-agent-models-context` · PR: pendiente.
- Decisiones: conservar la cola T-001…T-015; registrar este mantenimiento aparte; el dueño revisa/mergea después de la revisión de frontera en otra sesión.
- Pruebas: formato y enlaces locales OK antes de publicar; gate completo todavía no ejecutado.
- Próxima acción: publicar el PR draft y completar la validación documental.

## Resultado

- PR: pendiente · Cambios: documentación de ejecutores, contexto y traspasos · Tests agregados: ninguno · Próxima implementación: T-001, después del merge.
