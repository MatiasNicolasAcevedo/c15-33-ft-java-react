# Estado del proyecto
_Actualizado: 2026-10-11 · por: 🧠 Codex · sesión: implementación de T-003_

## ▶ Próxima acción

| Campo | Qué sigue |
|---|---|
| **Acción** | **Implementar T-003** |
| **Tarea** | [T-003 · Corregir la autorización](tasks/T-003-authorization-rules.md) |
| **Ejecutor** | **🧠 FRONTERA — GPT-6.1 Sol** |
| **Sesión** | T-003 en implementación; conservar la misma rama y PR |
| **Rama / PR** | `fix/T-003-authorization-rules` · PR draft pendiente |
| **Prompt** | «Leé AGENTS.md, docs/STATUS.md y las Notas de implementación de docs/tasks/T-003-authorization-rules.md. Seguí docs/agents/implementer.md retomando T-003 en la rama fix/T-003-authorization-rules. Verificá git status y el último commit; continuá desde el próximo paso registrado. No crees otra rama ni otro PR.» |

**T-001 cerrada:** [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817) y mergeado por el dueño con squash (`63e5bc2`). Revisión: server build y 25 tests OK, cobertura 23,86 %, arranque sin secreto rechazado, compose y login demo OK, JWT HS256 de 86400 s; client build OK, mismos 93 problemas de lint pendientes de T-005. T-015 queda lista para el dueño, sin bloquear la cola.

**Cambio a Lite:** después de T-001 → T-002 → T-003 (🧠), **T-004 → T-007 se implementan con 🤖 Junie Lite (Free)**. T-004 y T-007 las revisa 🧠. Se vuelve a frontera para implementar T-008. Entre tareas, primero revisión y merge del PR anterior; luego sesión nueva. Revalidá los PR abiertos al iniciar cada sesión.

**Compactar y continuar:** solo durante el mismo trabajo largo, en un corte seguro y después de guardar las notas + commit + push. Si la interfaz ofrece compactación, usala y retomá **la misma tarea, rama y PR**. Para Lite, guardá el checkpoint y abrí otro chat. El procedimiento y prompt están en [AGENTS.md → Gestión de contexto](../AGENTS.md#5-gestión-de-contexto).

## En una línea
T-003 en implementación. Línea de base: 31 tests del back OK, cobertura 40,80 %; client build OK y 93 problemas de lint conocidos (T-005). T-002 mergeada en da3e6e6.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ❌ sin regresiones | T-002, 2026-10-11: `--server` ✅ build + 31 tests + cobertura; gate completo: client ✅ build, ❌ mismos 93 problemas de lint → T-005 |
| CI en development | sin CI | → T-010 (bloqueada por T-005) |
| Levantar en local (README) | ⚠️ verificado 2026-10-10 | T-001: compose con build/wait ✅, login demo ✅ y JWT de 24 h ✅; checkout Windows requirió LF local en `mvnw`; el front llama a producción (T-004) y la home se rompe (T-011) |
| Cobertura back / front | 40,80 % / sin tests | T-002: JaCoCo 0.8.14 en verify, umbral LINE/BUNDLE de 40 % (224/549 líneas); front → T-009 |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | → Backlog P1 |
| Seguridad 🔴 abierta | 2 + rotación pendiente | T-001 mergeada: secreto por configuración; falta resolver la rotación del histórico en prod (T-015, lista para el dueño). Registro como ADMIN y borrado de usuarios → T-003 |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 1/15 tareas · ver [ROADMAP](ROADMAP.md)

## Foco
- T-003 en curso en `fix/T-003-authorization-rules`; PR draft pendiente.
- **Próximo paso / prompt:** ver la tarjeta **▶ Próxima acción** al inicio de este archivo y la cola de [BACKLOG](BACKLOG.md).

## Decisiones pendientes del dueño
- ¿El deploy anterior (Vercel + back) sigue activo? T-015 ya está lista tras el merge de T-001: el dueño debe rotar el secreto si hay deploy activo, o registrar que no lo hay.
- ¿Renombrar `development` → `main`? (T-014; los docs usan `development`)
- ¿Licencia? (proyecto de 6 personas; sugerido MIT tras consultarlo) (Backlog P3)
- ¿El rol ADMIN se asigna solo por base/seed? (T-003 asume que sí)
- ¿Reservas en el alcance del portfolio (P2) o se quita la UI?
- Crear el tag de la auditoría (T-000 ya está en `development`):
  `git switch development && git pull && git tag audit/2026-10-10 && git push origin audit/2026-10-10`

## Últimas sesiones (máx. 5; las anteriores → archive/log/AAAA-MM.md)
- 2026-10-11 · 🧠 Codex · T-002 implementada y publicada en [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5), lista para revisión independiente. 31 tests OK sin env.properties ni DB de compose, cobertura 40,80 % (224/549) y umbral 40 %; prueba negativa de JaCoCo al 100 % OK. `--server` verde; gate completo sin regresiones (client build OK, 93 problemas de lint de T-005). DB de compose restaurada healthy; contenedores de tests limpiados. Demora de cierre Surefire en Windows registrada en Backlog; T-003 espera revisión y merge.
- 2026-10-10 · 🧠 Codex · T-001 implementada y [revisión independiente APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817), sin bloqueantes; dueño mergeó [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) con squash (`63e5bc2`). Verificación independiente: 25 tests OK, cobertura 23,86 %, rechazo sin secreto, compose y login demo OK, JWT HS256 de 86400 s; gate sin regresiones (93 problemas de lint de T-005). STATUS y BACKLOG apuntan a T-002 en sesión nueva; T-015 desbloqueada para el dueño.
- 2026-10-10 · 🧠 Codex · revisión independiente de T-016: APROBADO, sin bloqueantes, una aclaración opcional; gate sin regresiones (server build + 1 test OK, client build OK, lint con 93 problemas). Dueño mergeó [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) con squash (`47debed`); STATUS y BACKLOG apuntan a implementar T-001 en sesión nueva con frontera.
- 2026-10-10 · 🧠 Codex / GPT-6.1 Sol · T-016 implementada y publicada en [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3), pendiente de revisión; checks documentales OK; gate sin diferencias respecto de la línea de base (server OK, client build OK, 93 problemas de lint pendientes de T-005); T-001 sigue sin iniciar.
- 2026-10-10 · 🧠 Claude Code (sub-agente) · revisó T-000 · APROBADO, sin bloqueantes; nits 1 y 4 corregidos en el PR · PR #2 mergeado
