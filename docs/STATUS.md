# Estado del proyecto
_Actualizado: 2026-10-12 · por: 🧠 Frontera (Codex) · sesión: T-008 implementación_

## ▶ Próxima acción

| Campo | Qué sigue |
|---|---|
| **Acción** | **Revisar PR #11 de T-008**: implementación terminada, merge pendiente |
| **Tarea** | [T-008](tasks/T-008-error-handling.md) |
| **Ejecutor** | **🧠 Frontera — GPT-6.1 Sol / Claude Opus 5.5** (revisión independiente, sesión nueva) |
| **Rama / PR** | `feat/T-008-error-handling` · [PR #11](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11) (ready) |
| **Prompt** | «Leé `AGENTS.md`, `docs/STATUS.md` y `docs/tasks/T-008-error-handling.md`. Seguí `docs/agents/reviewer.md` para el PR #11 de T-008 en la rama `feat/T-008-error-handling`. Revisá el commit publicado, errores MVC/dominio/seguridad, health público y compatibilidad del frontend. Publicá un veredicto independiente; no mergees ni inicies T-009.» |

**T-007 cerrada:** revisión independiente [APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10#issuecomment-6128981587) sobre `7ce95ad375af3a359203cc50f0980140542ed320`; el dueño mergeó #10 con squash `e1b07c4cf00423302b19199082239d8fda88c330`. Gate completo verde: 77 tests, 95,07 % PropertyServiceImp, 76,20 % global; mutación WIFI/WIFI rechazada. Inicio T-008: árbol limpio y ningún PR abierto.

**T-006 cerrada:** [PR #9](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/9) mergeado por el dueño con squash. Endpoint `DELETE /v1/api/property/{id}` corregido para mapear por ID en lugar de UUID. 2 tests agregados, verify.sh --server ✅ 57 tests OK.

**T-005 cerrada:** [PR #8](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/8) mergeado por el dueño con squash (`9dfe037`). 93 problemas de lint resueltos, cambios pedidos atendidos. `verify.sh --client` en verde.

**T-004 cerrada:** [PR #7](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7) mergeado por el dueño con squash (`c648fbd`). API_URL configurable vía `VITE_API_URL`, prefijo `/v1/api`, IP de producción eliminada de 4 archivos, prefijo `/api/` quitado de 14 rutas. Build ✅, lint 93 problemas (T-005).

**T-002 cerrada:** [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5#issuecomment-6104882015) y mergeado por el dueño con squash (`da3e6e6`). Verificación independiente: 31 tests sin fallos/errores/skips, sin env.properties ni DB de compose; cobertura 40,80 %, umbral 40 % y rechazo comprobado al exigir 100 %. `--server` verde; gate completo sin regresiones (client build OK, 93 problemas de lint de T-005). T-003 queda lista.

**T-001 cerrada:** [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817) y mergeado por el dueño con squash (`63e5bc2`). Revisión: server build y 25 tests OK, cobertura 23,86 %, arranque sin secreto rechazado, compose y login demo OK, JWT HS256 de 86400 s; client build OK, mismos 93 problemas de lint pendientes de T-005. T-015 queda lista para el dueño, sin bloquear la cola.

**Cambio a Lite:** después de T-001 → T-002 → T-003 (🧠), **T-004 → T-007 se implementan con 🤖 Junie Lite (Free)**. T-004 y T-007 las revisa 🧠. Se vuelve a frontera para implementar T-008. Entre tareas, primero revisión y merge del PR anterior; luego sesión nueva. Revalidá los PR abiertos al iniciar cada sesión.

**Prompt para Lite:** cuando la próxima acción corresponda a Junie Lite, el traspaso debe incluir un bloque `text` listo para copiar y pegar en Air, con tarea/archivo, protocolo y rama/PR si existen; el mismo prompt queda en esta tarjeta y en `BACKLOG.md`. La primera implementación de Lite será T-004, después de revisión y merge de T-003. Ver [AGENTS.md → Traspasos](../AGENTS.md#1-proyecto-mapa-y-ejecutores).

**Compactar y continuar:** solo durante el mismo trabajo largo, en un corte seguro y después de guardar las notas + commit + push. Si la interfaz ofrece compactación, usala y retomá **la misma tarea, rama y PR**. Para Lite, guardá el checkpoint y abrí otro chat. El procedimiento y prompt están en [AGENTS.md → Gestión de contexto](../AGENTS.md#5-gestión-de-contexto).

## En una línea
T-008 implementada; PR #11 pendiente de revisión independiente. T-009 espera al merge. T-007 mergeada y cerrada.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ✅ server + client | T-008: 118 tests sin fallos/errores/skips; lint/build client ✅; cobertura global 82,54 %, PropertyServiceImp 95,07 % |
| CI en development | sin CI | → T-010 (bloqueada por T-005) |
| Levantar en local (README) | ✅ verificado 2026-10-12 | T-008: compose build/wait OK; server/db healthy; health público UP, probes curl y registro/login desde front OK |
| Cobertura back / front | 82,54 % / sin tests | T-008: 118 tests (41 nuevos), 520/630 líneas; PropertyServiceImp 95,07 %; umbral 40 % conservado; front → T-009 |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | → Backlog P1 |
| Seguridad 🔴 abierta | rotación pendiente; T-003 mergeada | Registro como ADMIN y listado/borrado por USER corregidos en PR #6. T-015: dueño confirma deploy y rota el secreto histórico si corresponde |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 7/15 tareas mergeadas; T-008 en revisión · ver [ROADMAP](ROADMAP.md)

## Foco
- **PR de T-008 listo para revisión:** `feat/T-008-error-handling` · [PR #11](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11) (ready). Todos los cambios publicados; revisión independiente de 🧠 y merge del dueño pendientes.
- **Próximo paso / prompt:** ver la tarjeta **▶ Próxima acción** al inicio de este archivo y la cola de [BACKLOG](BACKLOG.md).

- **Hallazgo fuera de alcance:** cascada JPA de `Property.user` elimina al OWNER al borrar propiedad; registrado en Backlog P0, sin cambios al modelo de datos en T-008.

## Decisiones pendientes del dueño
- ¿El deploy anterior (Vercel + back) sigue activo? T-015 ya está lista tras el merge de T-001: el dueño debe rotar el secreto si hay deploy activo, o registrar que no lo hay.
- ¿Renombrar `development` → `main`? (T-014; los docs usan `development`)
- ¿Licencia? (proyecto de 6 personas; sugerido MIT tras consultarlo) (Backlog P3)
- ¿El rol ADMIN se asigna solo por base/seed? (T-003 asume que sí)
- ¿Reservas en el alcance del portfolio (P2) o se quita la UI?
- Crear el tag de la auditoría (T-000 ya está en `development`):
  `git switch development && git pull && git tag audit/2026-10-10 && git push origin audit/2026-10-10`

## Últimas sesiones (máx. 5; las anteriores → archive/log/AAAA-MM.md)
- 2026-10-12 · 🧠 Frontera (Codex) · T-008 implementada en PR #11: 118 tests, gate completo ✅, cobertura 82,54 %, compose healthy, probes y login/alta manual OK. T-007 cerrada tras squash e1b07c4. Próximo: revisión independiente; no iniciar T-009.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007: cambios pedidos del PR #10 atendidos. Tests de update y save corregidos. 77 tests OK, verify.sh --server ✅, cobertura 95,07 % PropertyServiceImp. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) (ready). Nueva revisión por 🧠.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007 completada: 18 tests de caracterización de PropertyServiceImp (94.3 % cobertura). 77 tests OK, verify.sh --server ✅. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) creado (draft). Revisión por 🧠.
- 2026-10-12 · 🤖 Junie Lite (Free) · Documentación corregida: T-006 (PR #9) ya estaba mergeada pero no reflejada como cerrada en STATUS.md y BACKLOG.md. T-007 establecida como próxima acción.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-006 completada: `@DeleteMapping("/{id}")` en PropertyController.java + PropertyControllerTest.java (2 tests). verify.sh --server ✅ 57 tests OK, cobertura OK. PR #9 mergeado.
