# Estado del proyecto
_Actualizado: 2026-10-10 · por: 🧠 Codex · sesión: implementación de T-001_

## ▶ Próxima acción

| Campo | Qué sigue |
|---|---|
| **Acción** | **Revisar PR #4 de T-001**; luego merge del dueño |
| **Tarea** | [T-001 · Sacar el secreto y la expiración del JWT a configuración](tasks/T-001-jwt-secret-config.md) |
| **Ejecutor** | **🧠 FRONTERA — GPT-6.1 Sol o Claude Opus 5.5** |
| **Sesión** | Implementación terminada; revisión independiente en **sesión nueva** con frontera |
| **Rama / PR** | `fix/T-001-jwt-secret-config` · [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) listo para revisión |
| **Prompt** | «Leé `AGENTS.md` y seguí `docs/agents/reviewer.md` para el PR #4» |

**T-016 cerrada:** [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) aprobado en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3#issuecomment-6104448623) y mergeado por el dueño con squash (`47debed`). Gate sin regresiones: server build y 1 test OK; client build OK, lint con los 93 problemas previos pendientes de T-005.

**Cambio a Lite:** después de T-001 → T-002 → T-003 (🧠), **T-004 → T-007 se implementan con 🤖 Junie Lite (Free)**. T-004 y T-007 las revisa 🧠. Se vuelve a frontera para implementar T-008. Entre tareas, primero revisión y merge del PR anterior; luego sesión nueva. Revalidá los PR abiertos al iniciar cada sesión.

**Compactar y continuar:** solo durante el mismo trabajo largo, en un corte seguro y después de guardar las notas + commit + push. Si la interfaz ofrece compactación, usala y retomá **la misma tarea, rama y PR**. Para Lite, guardá el checkpoint y abrí otro chat. El procedimiento y prompt están en [AGENTS.md → Gestión de contexto](../AGENTS.md#5-gestión-de-contexto).

## En una línea
Harness montado y verificado desde un clon limpio (compose, `.example`, `verify.sh`, `dev.sh`, docs, tablero y protocolos) en el PR de T-000. La app levanta en local, pero el front todavía llama a producción y casi no hay tests.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ❌ sin regresiones | T-001, 2026-10-10: `--server` ✅ build + 25 tests; gate completo: client ✅ build, ❌ mismos 93 problemas de lint → T-005 |
| CI en development | sin CI | → T-010 (bloqueada por T-005) |
| Levantar en local (README) | ⚠️ verificado 2026-10-10 | T-001: compose con build/wait ✅, login demo ✅ y JWT de 24 h ✅; checkout Windows requirió LF local en `mvnw`; el front llama a producción (T-004) y la home se rompe (T-011) |
| Cobertura back / front | 23,86 % / sin tests | JaCoCo CLI en T-001 (base 16,8 %); sin umbral todavía (T-002, T-009) |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | → Backlog P1 |
| Seguridad 🔴 abierta | 2 + rotación pendiente | T-001 implementada: secreto por configuración; falta rotar el histórico en prod (T-015). Registro como ADMIN y borrado de usuarios → T-003 |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 0/15 tareas · ver [ROADMAP](ROADMAP.md)

## Foco
- PR abierto (máx. 1): [#4 · T-001](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4), listo para revisión en `fix/T-001-jwt-secret-config`. T-002 espera revisión y merge de T-001.
- **Próximo paso / prompt:** ver la tarjeta **▶ Próxima acción** al inicio de este archivo y la cola de [BACKLOG](BACKLOG.md).

## Decisiones pendientes del dueño
- ¿El deploy anterior (Vercel + back) sigue activo? (define T-015)
- ¿Renombrar `development` → `main`? (T-014; los docs usan `development`)
- ¿Licencia? (proyecto de 6 personas; sugerido MIT tras consultarlo) (Backlog P3)
- ¿El rol ADMIN se asigna solo por base/seed? (T-003 asume que sí)
- ¿Reservas en el alcance del portfolio (P2) o se quita la UI?
- Crear el tag de la auditoría (T-000 ya está en `development`):
  `git switch development && git pull && git tag audit/2026-10-10 && git push origin audit/2026-10-10`

## Últimas sesiones (máx. 5; las anteriores → archive/log/AAAA-MM.md)
- 2026-10-10 · 🧠 Codex · T-001 implementada en [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4), lista para revisión independiente. Secreto validado al construir, HS256 y duración configurable de 24 h; 24 unitarios nuevos. `--server` verde (25 tests), gate completo sin regresiones (93 problemas de lint de T-005), cobertura 23,86 %, arranque sin secreto rechazado, compose y login demo OK, payload de 86400 s. T-015 sigue pendiente del dueño.
- 2026-10-10 · 🧠 Codex · revisión independiente de T-016: APROBADO, sin bloqueantes, una aclaración opcional; gate sin regresiones (server build + 1 test OK, client build OK, lint con 93 problemas). Dueño mergeó [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) con squash (`47debed`); STATUS y BACKLOG apuntan a implementar T-001 en sesión nueva con frontera.
- 2026-10-10 · 🧠 Codex / GPT-6.1 Sol · T-016 implementada y publicada en [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3), pendiente de revisión; checks documentales OK; gate sin diferencias respecto de la línea de base (server OK, client build OK, 93 problemas de lint pendientes de T-005); T-001 sigue sin iniciar.
- 2026-10-10 · 🧠 Claude Code (sub-agente) · revisó T-000 · APROBADO, sin bloqueantes; nits 1 y 4 corregidos en el PR · PR #2 mergeado
- 2026-10-10 · 🧠 Claude Code · auditó y montó el harness (T-000) · PR #2
