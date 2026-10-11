# T-016 · Aclarar ejecutores, compactación y traspasos de agentes

- Estado: En revisión · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🧠 modelo de frontera
- Fase: Mantenimiento documental (fuera de las 15 tareas de P0 Estabilizar)
- Rama: `docs/T-016-agent-models-context` · Depende de: — · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤 dueño

## Por qué

El dueño pidió identificar el ejecutor Lite como Junie Lite (Free), precisar qué modelos corresponden a frontera y hacer visibles la próxima acción y las condiciones para compactar y retomar. Después aportó el traspaso de Claude Code y pidió continuar con la publicación de las correcciones.

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

- [x] No quedan referencias al nombre del modelo interno ni al tamaño de contexto no verificado en la documentación operativa o su corrección histórica. Evidencia: búsqueda con `rg` sin coincidencias en Markdown de AGENTS, README, CONTRIBUTING, docs (sin archive) y punteros de agentes.
- [x] Lite significa Junie Lite (Free); frontera incluye GPT-6.1 Sol / Claude Opus 5.5 y se distingue del agente/interfaz. Evidencia: `AGENTS.md` §1 y `docs/SETUP.md` → agentes.
- [x] Compactación y chat nuevo tienen checkpoint y prompt de retoma de la misma tarea, rama y PR. Evidencia: `AGENTS.md` §5 e `implementer.md` pasos 2 y 7.
- [x] STATUS y BACKLOG muestran revisión/merge del PR documental antes de implementar T-001, con ejecutor y prompt. Evidencia: tarjeta superior en ambos archivos y PR #3.
- [x] La excepción de rama en nube exige autorización explícita y evidencia; las instrucciones Docker de nube se distinguen de Windows local. Evidencia: SETUP → Prompt de inicio, CONTRIBUTING §1 y `reviewer.md` paso 3.
- [x] Formato y enlaces locales verificados; diff limitado a Markdown; estado de verify documentado. Evidencia: 17 archivos Markdown, `git diff --check origin/development` OK, enlaces locales OK; gate con server build/test OK y client build OK, lint con los mismos 93 problemas previos (T-005).

## Notas de implementación

- Hecho: correcciones publicadas; rama exacta antes del primer commit; base sin divergencia; PR #3; validación de formato, enlaces locales, nomenclatura y alcance OK (17 archivos Markdown); gate completo ejecutado y resultado registrado.
- Pendiente: revisión independiente 🧠 y merge del dueño; T-001 sigue sin iniciar.
- Ejecutor/modelo: 🧠 Codex / GPT-6.1 Sol · Rama: `docs/T-016-agent-models-context` · PR: [#3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) · Commit de implementación: `75ecb25`.
- Decisiones: conservar la cola T-001…T-015; registrar este mantenimiento aparte; el dueño revisa/mergea después de la revisión de frontera en otra sesión.
- Pruebas: `git diff --check origin/development` OK; enlaces locales y búsqueda de nombres/supuestos retirados OK; diff solo Markdown. Gate completo con Git Bash `--login`: server ✅ build ✅ tests (1), JDK 21; client ❌ lint (93 problemas) ✅ build, Node 24.19.0, sin tests hasta T-009; exit 1, 132 s. Coincide con la línea de base registrada: lint pendiente de T-005. No se afirma que el gate completo esté verde.
- Entorno: la primera invocación sin `--login` no encontró `dirname`; la invocación con `--login` inició correctamente y usó `env.properties.example`. No se modificó configuración real.
- Próxima acción: en una sesión nueva de frontera, «Leé `AGENTS.md` y seguí `docs/agents/reviewer.md` para el PR #3 (T-016)»; después merge del dueño. Checkpoint de implementación publicado en origin.

## Resultado

- PR: [#3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) · Cambios: documentación de ejecutores, contexto y traspasos · Tests agregados: ninguno · Próxima implementación: T-001, después del merge.
