# Estado del proyecto
_Actualizado: 2026-10-12 · por: 🧠 Frontera (Codex) · sesión: cierre documental de T-008 después del merge_

## ▶ Próxima acción

| Campo | Qué sigue |
|---|---|
| **Acción** | **Revisar e integrar PR #12**: cierre documental de T-008, ya mergeada en #11 |
| **Tarea** | [T-008](tasks/T-008-error-handling.md) |
| **Ejecutor** | **👤 dueño** (revisión del diff documental y merge) |
| **Rama / PR** | `docs/T-008-closeout` · [PR #12](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/12) |
| **Prompt** | «GitHub → PR #12 → revisar el diff documental y, si está correcto, Squash and merge. Después abrir una sesión nueva de 🧠 Frontera — GPT-6.1 Sol con el prompt de T-009 de abajo; revalidar PR abiertos antes de comenzar.» |

### Próxima implementación después de integrar #12

**Implementar T-009 · Montar la infraestructura de tests del front · ejecuta: 🧠 Frontera — GPT-6.1 Sol.** Rama prevista: `test/T-009-frontend-test-infra`; PR pendiente, tarea todavía Lista.

```text
Leé AGENTS.md, docs/STATUS.md, docs/BACKLOG.md y docs/tasks/T-009-frontend-test-infra.md. Seguí docs/agents/implementer.md para implementar T-009 en una sesión nueva de 🧠 Frontera. Verificá git status y los PR abiertos; confirmá que #11 y el cierre documental #12 estén mergeados. Si hay un PR abierto, atendé primero la próxima acción de STATUS y no inicies T-009. Con el árbol limpio y development actualizado, corré la línea de base y creá la rama exacta test/T-009-frontend-test-infra antes del primer commit; abrí su PR en draft. Actualizá STATUS/BACKLOG para reflejar el merge de #12 y el inicio de T-009. Implementá Vitest, React Testing Library, MSW y cobertura v8 con versiones compatibles con Vite 5 y React 18, siguiendo el contrato de la tarea. Conservá el comportamiento actual de la home y del cliente; los arreglos de UI pertenecen a T-011. Verificá npm test, npm run test:coverage, el test individual indicado y scripts/verify.sh; documentá cobertura y umbral, hacé commit y push de todos los cambios propios y dejá el PR listo para revisión independiente. No mergees ni inicies T-010.
```

**T-008 cerrada:** revisión independiente [APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11#issuecomment-6129711504) sobre `4eb7e2fed41ea1bc629f4216e4e673ea4977d4ff`; el dueño mergeó [#11](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11) con squash `1b8ec829c519b3d84a2c11543738eb8def7bf23b`. Gate de revisión: 118 tests sin fallos, lint/build verdes, cobertura global 82,59 %, PropertyServiceImp 95,07 %; compose healthy y probes OK. La limitación de repetición manual permanece explicitada en la tarea y el veredicto. #12 guarda este cierre y el traspaso; T-009 no iniciada.

**T-007 cerrada:** revisión independiente [APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10#issuecomment-6128981587) sobre `7ce95ad375af3a359203cc50f0980140542ed320`; el dueño mergeó #10 con squash `e1b07c4cf00423302b19199082239d8fda88c330`. Gate completo verde: 77 tests, 95,07 % PropertyServiceImp, 76,20 % global; mutación WIFI/WIFI rechazada. Inicio T-008: árbol limpio y ningún PR abierto.

**T-006 cerrada:** [PR #9](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/9) mergeado por el dueño con squash. Endpoint `DELETE /v1/api/property/{id}` corregido para mapear por ID en lugar de UUID. 2 tests agregados, verify.sh --server ✅ 57 tests OK.

**T-005 cerrada:** [PR #8](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/8) mergeado por el dueño con squash (`9dfe037`). 93 problemas de lint resueltos, cambios pedidos atendidos. `verify.sh --client` en verde.

**T-004 cerrada:** [PR #7](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7) mergeado por el dueño con squash (`c648fbd`). API_URL configurable vía `VITE_API_URL`, prefijo `/v1/api`, IP de producción eliminada de 4 archivos, prefijo `/api/` quitado de 14 rutas. Build ✅, lint 93 problemas (T-005).

**T-002 cerrada:** [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5#issuecomment-6104882015) y mergeado por el dueño con squash (`da3e6e6`). Verificación independiente: 31 tests sin fallos/errores/skips, sin env.properties ni DB de compose; cobertura 40,80 %, umbral 40 % y rechazo comprobado al exigir 100 %. `--server` verde; gate completo sin regresiones (client build OK, 93 problemas de lint de T-005). T-003 queda lista.

**T-001 cerrada:** [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817) y mergeado por el dueño con squash (`63e5bc2`). Revisión: server build y 25 tests OK, cobertura 23,86 %, arranque sin secreto rechazado, compose y login demo OK, JWT HS256 de 86400 s; client build OK, mismos 93 problemas de lint pendientes de T-005. T-015 queda lista para el dueño, sin bloquear la cola.

**Ejecutor actual de la cola:** T-008 está mergeada; T-009 y T-010 corresponden a 🧠 Frontera. T-011 vuelve a 🤖 Junie Lite (Free) después de T-009. Entre tareas, primero revisión y merge del PR anterior; luego sesión nueva. Revalidá los PR abiertos al iniciar cada sesión.

**Prompt para Lite:** cuando la próxima acción corresponda a Junie Lite, el traspaso debe incluir un bloque `text` listo para copiar y pegar en Air, con tarea/archivo, protocolo y rama/PR si existen; el mismo prompt queda en esta tarjeta y en `BACKLOG.md`. Ver [AGENTS.md → Traspasos](../AGENTS.md#1-proyecto-mapa-y-ejecutores).

**Compactar y continuar:** solo durante el mismo trabajo largo, en un corte seguro y después de guardar las notas + commit + push. Si la interfaz ofrece compactación, usala y retomá **la misma tarea, rama y PR**. Para Lite, guardá el checkpoint y abrí otro chat. El procedimiento y prompt están en [AGENTS.md → Gestión de contexto](../AGENTS.md#5-gestión-de-contexto).

## En una línea
T-008 aprobada y mergeada en #11; #12 publica el cierre documental. Próxima implementación: T-009, tras integrar #12, en una sesión nueva.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ✅ server + client | Revisión T-008: 118 tests sin fallos/errores/skips; lint/build client ✅; cobertura global 82,59 %, PropertyServiceImp 95,07 %. El cierre #12 solo modifica Markdown |
| CI en development | sin CI de build/tests | → T-010, Lista después de T-009; dependencias T-002/T-005 satisfechas. Check de título existente |
| Levantar en local (README) | ✅ verificado 2026-10-12 | T-008: compose build/wait OK; server/db healthy; health público UP, probes curl y registro/login desde front OK |
| Cobertura back / front | 82,59 % revisión / sin tests | T-008: 118 tests (41 nuevos), 517/626 líneas en revisión; implementación: 82,54 % (520/630). PropertyServiceImp 95,07 %; umbral 40 % conservado; front → T-009 |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | → Backlog P1 |
| Seguridad 🔴 abierta | rotación pendiente; T-003 mergeada | Registro como ADMIN y listado/borrado por USER corregidos en PR #6. T-015: dueño confirma deploy y rota el secreto histórico si corresponde |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 8/15 tareas mergeadas (T-001 a T-008); T-009 Lista · ver [ROADMAP](ROADMAP.md)

## Foco
- **Cierre documental pendiente de integración:** `docs/T-008-closeout` · [PR #12](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/12). T-008 ya cerrada en #11; revisar/mergear este mantenimiento antes de implementar T-009.
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
- 2026-10-12 · 🧠 Frontera (Codex) · T-008 implementada, revisada y mergeada por el dueño en #11 (squash 1b8ec82). Gate de revisión: 118 tests, lint/build ✅, cobertura 82,59 %, compose/probes OK; límites manuales en la tarea. Cierre documental autorizado en #12, desde base 1b8ec82 y árbol limpio; diff/enlaces/cola verificados, sin repetir tests por cambios solo Markdown. Próximo: dueño integra #12, después sesión nueva de 🧠 para T-009.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007: cambios pedidos del PR #10 atendidos. Tests de update y save corregidos. 77 tests OK, verify.sh --server ✅, cobertura 95,07 % PropertyServiceImp. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) (ready). Nueva revisión por 🧠.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007 completada: 18 tests de caracterización de PropertyServiceImp (94.3 % cobertura). 77 tests OK, verify.sh --server ✅. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) creado (draft). Revisión por 🧠.
- 2026-10-12 · 🤖 Junie Lite (Free) · Documentación corregida: T-006 (PR #9) ya estaba mergeada pero no reflejada como cerrada en STATUS.md y BACKLOG.md. T-007 establecida como próxima acción.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-006 completada: `@DeleteMapping("/{id}")` en PropertyController.java + PropertyControllerTest.java (2 tests). verify.sh --server ✅ 57 tests OK, cobertura OK. PR #9 mergeado.
