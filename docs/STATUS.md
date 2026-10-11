# Estado del proyecto
_Actualizado: 2026-10-11 · por: 🧠 Codex · sesión: revisión de T-003 y cierre documental autorizado por el dueño_

## ▶ Próxima acción

| Campo | Qué sigue |
|---|---|
| **Acción** | **Revisar PR #7 de T-004**; implementación completada, espera revisión independiente |
| **Tarea** | [T-004 · Hacer configurable la URL de la API en el front](tasks/T-004-frontend-api-base-url.md) |
| **Ejecutor** | **🧠 Frontera — GPT-6.1 Sol / Claude Opus 5.5** |
| **Rama / PR** | `fix/T-004-frontend-api-base-url` · [PR #7](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7) |
| **Prompt** | «Leé `AGENTS.md` y seguí `docs/agents/reviewer.md` para el PR #7» |

**T-002 cerrada:** [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5#issuecomment-6104882015) y mergeado por el dueño con squash (`da3e6e6`). Verificación independiente: 31 tests sin fallos/errores/skips, sin env.properties ni DB de compose; cobertura 40,80 %, umbral 40 % y rechazo comprobado al exigir 100 %. `--server` verde; gate completo sin regresiones (client build OK, 93 problemas de lint de T-005). T-003 queda lista.

**T-001 cerrada:** [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817) y mergeado por el dueño con squash (`63e5bc2`). Revisión: server build y 25 tests OK, cobertura 23,86 %, arranque sin secreto rechazado, compose y login demo OK, JWT HS256 de 86400 s; client build OK, mismos 93 problemas de lint pendientes de T-005. T-015 queda lista para el dueño, sin bloquear la cola.

**Cambio a Lite:** después de T-001 → T-002 → T-003 (🧠), **T-004 → T-007 se implementan con 🤖 Junie Lite (Free)**. T-004 y T-007 las revisa 🧠. Se vuelve a frontera para implementar T-008. Entre tareas, primero revisión y merge del PR anterior; luego sesión nueva. Revalidá los PR abiertos al iniciar cada sesión.

**Prompt para Lite:** cuando la próxima acción corresponda a Junie Lite, el traspaso debe incluir un bloque `text` listo para copiar y pegar en Air, con tarea/archivo, protocolo y rama/PR si existen; el mismo prompt queda en esta tarjeta y en `BACKLOG.md`. La primera implementación de Lite será T-004, después de revisión y merge de T-003. Ver [AGENTS.md → Traspasos](../AGENTS.md#1-proyecto-mapa-y-ejecutores).

**Compactar y continuar:** solo durante el mismo trabajo largo, en un corte seguro y después de guardar las notas + commit + push. Si la interfaz ofrece compactación, usala y retomá **la misma tarea, rama y PR**. Para Lite, guardá el checkpoint y abrí otro chat. El procedimiento y prompt están en [AGENTS.md → Gestión de contexto](../AGENTS.md#5-gestión-de-contexto).

## En una línea
T-004 implementada y publicada en PR #7: API_URL configurable vía VITE_API_URL, prefijo /v1/api, IP de producción eliminada. Espera revisión de frontera. Back: 57 tests y cobertura 56,48 %.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ❌ sin regresiones | T-004, 2026-10-11: client ✅ build, ❌ mismos 93 problemas de lint (T-005); server no ejecutado |
| CI en development | sin CI | → T-010 (bloqueada por T-005) |
| Levantar en local (README) | ⚠️ verificado 2026-10-10 | T-001: compose con build/wait ✅, login demo ✅ y JWT de 24 h ✅; checkout Windows requirió LF local en `mvnw`; el front usa localhost:8080/v1/api (T-004) pero la home se rompe (T-011) |
| Cobertura back / front | 56,48 % / sin tests | T-003: 318/563 líneas (base 40,80 %); umbral 40 % conservado; front → T-009 |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | → Backlog P1 |
| Seguridad 🔴 abierta | rotación pendiente; T-003 mergeada | Registro como ADMIN y listado/borrado por USER corregidos en PR #6. T-015: dueño confirma deploy y rota el secreto histórico si corresponde |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 2/15 tareas · ver [ROADMAP](ROADMAP.md)

## Foco
- PR abierto (máx. 1): [#7 · T-004](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7), ready para revisión en `fix/T-004-frontend-api-base-url`; espera 🧠.
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
- 2026-10-11 · 🤖 Junie Lite (Free) · T-004 implementada y publicada en [PR #7](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7): config.js con API_URL desde VITE_API_URL + /v1/api; IP hardcodeada eliminada de 4 archivos; prefijo /api/ quitado de 14 rutas; docs actualizados. Build ✅, lint 93 problemas (igual que development). PR ready para revisión de frontera.
- 2026-10-11 · 🧠 Codex · T-003 implementada y [APROBADA en revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/6#issuecomment-6105109999) del commit `0625d79`, [PR #6](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/6) mergeada. Gate: 57 tests OK, cobertura 56,48 %; client build OK, 93 problemas de lint (T-005). Próxima: T-004 con Lite.
- 2026-10-11 · 🧠 Codex · T-002 implementada, [revisión independiente APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5#issuecomment-6104882015) y [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5) mergeado por el dueño con squash (`da3e6e6`). 31 tests OK sin env.properties ni DB de compose, cobertura 40,80 % (224/549), umbral 40 % y prueba negativa al 100 % OK. Gate sin regresiones (93 problemas de lint de T-005); demora de Surefire en Backlog. STATUS/BACKLOG apuntan a T-003 en sesión nueva. A pedido del dueño, AGENTS y protocolos exigen un prompt completo para copiar y pegar cuando toque Junie Lite. Esta actualización es documental; conserva la evidencia de la revisión y los cambios locales previos de T-001/T-015.
- 2026-10-10 · 🧠 Codex · T-001 implementada y [revisión independiente APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817), sin bloqueantes; dueño mergeó [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) con squash (`63e5bc2`). Verificación independiente: 25 tests OK, cobertura 23,86 %, rechazo sin secreto, compose y login demo OK, JWT HS256 de 86400 s; gate sin regresiones (93 problemas de lint de T-005). STATUS y BACKLOG apuntan a T-002 en sesión nueva; T-015 desbloqueada para el dueño.
- 2026-10-10 · 🧠 Codex · revisión independiente de T-016: APROBADO, sin bloqueantes, una aclaración opcional; gate sin regresiones (server build + 1 test OK, client build OK, lint con 93 problemas). Dueño mergeó [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3) con squash (`47debed`); STATUS y BACKLOG apuntan a implementar T-001 en sesión nueva con frontera.
- 2026-10-10 · 🧠 Codex / GPT-6.1 Sol · T-016 implementada y publicada en [PR #3](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/3), pendiente de revisión; checks documentales OK; gate sin diferencias respecto de la línea de base (server OK, client build OK, 93 problemas de lint pendientes de T-005); T-001 sigue sin iniciar.
