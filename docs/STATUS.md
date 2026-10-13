# Estado del proyecto
_Actualizado: 2026-10-13 · por: 🧠 Frontera (Codex) · sesión: cierre de implementación T-010_

## ▶ Próxima acción

| Campo | Qué sigue |
|---|---|
| **Acción** | **Revisar PR #15**: CI y Dependabot; merge del dueño después del veredicto |
| **Tarea** | [T-010](tasks/T-010-ci-workflow.md) · Implementación hecha, revisión pendiente |
| **Ejecutor** | **🧠 Frontera — GPT-6.1 Sol** (sesión nueva e independiente) |
| **Rama / PR** | `ci/T-010-ci-workflow` · [PR #15](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/15) |
| **Prompt** | «Leé AGENTS.md, docs/STATUS.md, docs/BACKLOG.md y docs/tasks/T-010-ci-workflow.md. Seguí docs/agents/reviewer.md para revisar de forma independiente el PR #15, T-010, rama ci/T-010-ci-workflow, base development. Comprobá el HEAD publicado, los jobs server/client y cobertura, las versiones/SHA de actions, Dependabot y la evidencia del test roto revertido. No mergees ni inicies T-012.» |

**T-010 implementada:** gate completo final verde (88 s; 118 back/6 front) y v8 separado verde (1,65 %). [CI con Dependabot](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38408551205) sobre `a56a618`: server/client SUCCESS; JaCoCo 40 % / v8 1 % intactos, artifacts y logs ocultos comprobados. [Test roto](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38408053751) rechazado por client; server verde, logs publicados. Probe `d307534` revertido en `29d7201`; sin diff final de tests. El cierre documental se publica antes de ready y se comprueba CI sobre el HEAD final. Siguiente: revisión de #15 en sesión nueva; T-012 espera su merge. Dueño: agregar **server** y **client** como checks requeridos en [T-014](tasks/T-014-branch-protection.md) después del merge.

**Inicio T-010 (2026-10-13):** árbol limpio, sin cambios heredados ni PR abiertos. #13 y #14 confirmados MERGED; #14 integrado en `6c71f894920f8725c0774a6cfefb88ad22c5aaed`. Development actualizado por fast-forward; gate completo verde (121 s, 118 tests back/6 front, JDK 21/Node 24). Rama exacta creada antes del primer commit. PR #15 abierto en draft al iniciar; Dependabot agregado después del primer CI verde. Implementación cerrada para revisión independiente.

**T-009 cerrada:** revisión independiente [APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/13#issuecomment-6146403632) sobre `e5de84062fc961983bfd78198d77f60044afe9d2`; el dueño mergeó [#13](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/13) con squash `dc2756330096d406ba0523ab7d9d39724191b97c` el 2026-10-13. Gate completo de revisión verde (95 s: 118 tests back/6 front), npm ci reproducible, client verde (17 s), cobertura v8 1,65 %, umbral 1 % y rechazo al exigir 100 % comprobados. #14 guarda este cierre autorizado y la cola revisada; inicio con árbol limpio y ningún PR abierto, sin cambios heredados.

**Inicio T-009:** árbol limpio, ningún PR abierto; #11 y #12 confirmados MERGED. #12 integrado en `793fa11adab5b229703a432345807f60a762766b`; development actualizado por fast-forward. Línea de base completa verde: 118 tests del back, cobertura OK, lint/build del front OK (todavía sin tests).

**T-008 cerrada:** revisión independiente [APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11#issuecomment-6129711504) sobre `4eb7e2fed41ea1bc629f4216e4e673ea4977d4ff`; el dueño mergeó [#11](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/11) con squash `1b8ec829c519b3d84a2c11543738eb8def7bf23b`. Gate de revisión: 118 tests sin fallos, lint/build verdes, cobertura global 82,59 %, PropertyServiceImp 95,07 %; compose healthy y probes OK. La limitación de repetición manual permanece explicitada en la tarea y el veredicto. #12 guarda este cierre y el traspaso, mergeado por el dueño en `793fa11`; T-009 iniciada.

**T-007 cerrada:** revisión independiente [APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10#issuecomment-6128981587) sobre `7ce95ad375af3a359203cc50f0980140542ed320`; el dueño mergeó #10 con squash `e1b07c4cf00423302b19199082239d8fda88c330`. Gate completo verde: 77 tests, 95,07 % PropertyServiceImp, 76,20 % global; mutación WIFI/WIFI rechazada. Inicio T-008: árbol limpio y ningún PR abierto.

**T-006 cerrada:** [PR #9](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/9) mergeado por el dueño con squash. Endpoint `DELETE /v1/api/property/{id}` corregido para mapear por ID en lugar de UUID. 2 tests agregados, verify.sh --server ✅ 57 tests OK.

**T-005 cerrada:** [PR #8](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/8) mergeado por el dueño con squash (`9dfe037`). 93 problemas de lint resueltos, cambios pedidos atendidos. `verify.sh --client` en verde.

**T-004 cerrada:** [PR #7](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7) mergeado por el dueño con squash (`c648fbd`). API_URL configurable vía `VITE_API_URL`, prefijo `/v1/api`, IP de producción eliminada de 4 archivos, prefijo `/api/` quitado de 14 rutas. Build ✅, lint 93 problemas (T-005).

**T-002 cerrada:** [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5#issuecomment-6104882015) y mergeado por el dueño con squash (`da3e6e6`). Verificación independiente: 31 tests sin fallos/errores/skips, sin env.properties ni DB de compose; cobertura 40,80 %, umbral 40 % y rechazo comprobado al exigir 100 %. `--server` verde; gate completo sin regresiones (client build OK, 93 problemas de lint de T-005). T-003 queda lista.

**T-001 cerrada:** [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4) aprobado sin bloqueantes en [revisión independiente](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4#issuecomment-6104606817) y mergeado por el dueño con squash (`63e5bc2`). Revisión: server build y 25 tests OK, cobertura 23,86 %, arranque sin secreto rechazado, compose y login demo OK, JWT HS256 de 86400 s; client build OK, mismos 93 problemas de lint pendientes de T-005. T-015 queda lista para el dueño, sin bloquear la cola.

**Orden revisado:** T-010 (🧠 CI) → T-012 (🧠 imágenes) → T-011 (🤖 home) → T-013 (🧠 E2E), con revisión y merge entre tareas. T-012 ya está Lista por los merges de T-003/T-004/T-009. T-011 espera T-012: el catálogo real devuelve imageUrls y la card todavía usa images[0], por lo que su caso exitoso rompe por un problema de contrato fuera del alcance de Lite. T-013 espera los tres merges. Al desbloquear T-011/T-013, colocarlas antes de las tareas del dueño en Listas; no saltear la primera tarea ni iniciar mientras haya un PR pendiente.

**Tareas del dueño:** T-014 puede aplicar protección básica ahora y completar checks server/client tras T-010; actualizar e2e cuando exista en T-013. T-015 sigue Lista desde T-001 y requiere confirmar deploy activo. No bloquean la cola, pero ambas son requisitos para cerrar P0. La cascada de borrado del OWNER sigue en Backlog P0 y debe refinarse/resolverse antes de declarar la fase segura.

**Prompt para Lite:** cuando la próxima acción corresponda a Junie Lite, el traspaso debe incluir un bloque `text` listo para copiar y pegar en Air, con tarea/archivo, protocolo y rama/PR si existen; el mismo prompt queda en esta tarjeta y en `BACKLOG.md`. Ver [AGENTS.md → Traspasos](../AGENTS.md#1-proyecto-mapa-y-ejecutores).

**Compactar y continuar:** solo durante el mismo trabajo largo, en un corte seguro y después de guardar las notas + commit + push. Si la interfaz ofrece compactación, usala y retomá **la misma tarea, rama y PR**. Para Lite, guardá el checkpoint y abrí otro chat. El procedimiento y prompt están en [AGENTS.md → Gestión de contexto](../AGENTS.md#5-gestión-de-contexto).

## En una línea
9/15 tareas P0 mergeadas (T-001 a T-009). T-010 implementada en PR #15, pendiente de revisión/merge; #14 mergeado (`6c71f89`). Camino: CI → imágenes → estados de la home → E2E; protección y rotación pendientes del dueño.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ✅ server + client | T-010: línea de base 121 s, gate final 88 s; 118 tests back/6 front, lint/build/tests y JaCoCo verdes; v8 separado verde |
| CI | ✅ en PR #15; development espera merge | [Run con Dependabot](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/runs/38408551205): server/client verdes; cobertura front separada, artifacts publicados; test roto rechazado y revertido |
| Levantar en local (README) | ✅ verificado 2026-10-12 | T-008: compose build/wait OK; server/db healthy; health público UP, probes curl y registro/login desde front OK |
| Cobertura back / front | 82,59 % / 1,65 % | Gate T-009: back 517/626 líneas, umbral 40 %; front 54/3254 líneas, umbral 1 % (floor de la base). Rechazo de cobertura al exigir 100 % verificado; HostCardItem 100 % |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | última auditoría: 16 altas, 8 moderadas | Medición histórica 2026-10-10, no actualizada en este cierre; → Backlog P1 |
| Seguridad 🔴 abierta | rotación pendiente; T-003 mergeada | Registro como ADMIN y listado/borrado por USER corregidos en PR #6. T-015: dueño confirma deploy y rota el secreto histórico si corresponde |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 9/15 tareas mergeadas (T-001 a T-009). Pendientes T-010 a T-015; T-016 fue mantenimiento aparte. No declarar P0 cerrada hasta completar sus criterios y resolver los hallazgos P0 abiertos · ver [ROADMAP](ROADMAP.md)

## Foco
- **PR abierto para revisión independiente:** T-010 en `ci/T-010-ci-workflow`, [PR #15](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/15). #13/#14 mergeados; gates local/remoto verdes, test roto rechazado y revertido. Sin otra implementación iniciada.
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
- 2026-10-13 · 🧠 Frontera (Codex) · T-010 implementada en [PR #15](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/15) desde development `6c71f89`, árbol inicial limpio y sin PR abiertos. Gate base 121 s/final 88 s, 118 back/6 front, v8 1,65 %. CI server/client verde, prueba d307534 rechazada y revertida en 29d7201; actions SHA vigentes, Dependabot tras CI verde y badge. Documentación/checks T-014 actualizados. Próximo: revisión independiente de frontera en sesión nueva; no mergear ni iniciar T-012.
- 2026-10-13 · 🧠 Frontera (Codex) · T-009 implementada, revisada APROBADA y mergeada por el dueño en #13 (`dc27563`). 6 tests, 1,65 % global / umbral 1 %, rechazo a 100 % y gates completo/client verdes (118 back). Cierre autorizado en #14, base dc27563 y árbol inicial limpio; revisión de estados y dependencias: T-012 Lista, T-011 espera T-012, T-013 espera los tres merges. Siguiente: integrar #14, luego T-010 en sesión nueva. Diff solo Markdown, sin repetir pruebas funcionales ni iniciar otra tarea.
- 2026-10-12 · 🧠 Frontera (Codex) · T-008 implementada, revisada y mergeada por el dueño en #11 (squash 1b8ec82). Gate de revisión: 118 tests, lint/build ✅, cobertura 82,59 %, compose/probes OK; límites manuales en la tarea. Cierre documental autorizado en #12, desde base 1b8ec82 y árbol limpio; diff/enlaces/cola verificados, sin repetir tests por cambios solo Markdown. Próximo: dueño integra #12, después sesión nueva de 🧠 para T-009.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007: cambios pedidos del PR #10 atendidos. Tests de update y save corregidos. 77 tests OK, verify.sh --server ✅, cobertura 95,07 % PropertyServiceImp. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) (ready). Nueva revisión por 🧠.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007 completada: 18 tests de caracterización de PropertyServiceImp (94.3 % cobertura). 77 tests OK, verify.sh --server ✅. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) creado (draft). Revisión por 🧠.
