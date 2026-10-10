# T-000 · Relevar el proyecto y montar el harness para agentes
- Estado: Hecha · Prioridad: P0 · Esfuerzo: L · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `chore/T-000-agent-bootstrap` · Depende de: — · Spec/ADR: [0001](../adr/0001-testcontainers-mysql.md)–[0004](../adr/0004-keep-folder-layout.md)
- Revisa: 👤 dueño (o 🧠 sesión nueva / sub-agente si el dueño lo pide)

## Por qué
Proyecto de 2023 sin tests, CI, contenedores ni docs para levantarlo; se retoma para portfolio con agentes.

## Objetivo
Auditoría inicial + harness (compose, `.example`, `verify.sh`, `dev.sh`) + docs vivos y protocolos, en un PR.

## Alcance
- Incluye: docs, `.example`, compose y Dockerfiles de desarrollo, scripts, toolchain, `.github/` (template + título), `.claude/settings.json`, punteros de contexto, bit de ejecución de `mvnw`.
- **No incluye:** código de la app, versiones, configuración de producción.

## Criterios de aceptación
- [x] Auditoría con evidencia: [2026-10-10-initial](../audits/2026-10-10-initial.md)
- [x] `docker compose up -d --wait` (DB) y `--profile app` (todo) verificados desde cero
- [x] `.example` copiables sin editar; `verify.sh` y `dev.sh` probados
- [x] README verificado en ambos modos (con los ⚠️ que dependen de T-004/T-005/T-011)
- [x] Tablero con 10 tareas en cola y protocolos de agentes

## Resultado
- PR: [#2](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/2) (APROBADO por sub-agente revisor, squash merge a pedido del dueño) · Qué cambió: ver descripción del PR · Tests agregados: ninguno (modo A) · Pendientes → T-001…T-015
