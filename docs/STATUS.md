# Estado del proyecto
_Actualizado: 2026-10-13 · por: 🧠 Frontera (Codex) · sesión: cierre T-009 y revisión de la cola_

## ▶ Próxima acción

| Campo | Qué sigue |
|---|---|
| **Acción** | **Revisar e integrar PR #14**: cierre documental de T-009, ya mergeada en #13 |
| **Tarea** | [T-009](tasks/T-009-frontend-test-infra.md) · Hecha; cierre documental pendiente |
| **Ejecutor** | **👤 dueño** (revisión del diff documental y merge) |
| **Rama / PR** | `docs/T-009-closeout` · [PR #14](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/14) |
| **Prompt** | «GitHub → PR #14 → revisar el diff documental y, si está correcto, Squash and merge. Registrar el merge y activar la próxima acción Implementar T-010 en STATUS/BACKLOG con el prompt de abajo. No iniciar T-010 mientras #14 siga abierto.» |

### Próxima implementación después de integrar #14

**T-010 · Agregar CI y Dependabot · ejecuta: 🧠 Frontera — GPT-6.1 Sol.** Rama prevista `ci/T-010-ci-workflow`, PR pendiente. Tarea Lista, sin iniciar.

```text
Leé AGENTS.md, docs/STATUS.md, docs/BACKLOG.md y docs/tasks/T-010-ci-workflow.md. Seguí docs/agents/implementer.md para implementar T-010 en una sesión nueva de 🧠 Frontera. Verificá git status y los PR abiertos; confirmá que #13 y el cierre documental #14 estén mergeados. Si hay un PR abierto, atendé primero la próxima acción de STATUS y no inicies T-010. Con el árbol limpio y development actualizado, corré la línea de base y creá la rama exacta ci/T-010-ci-workflow antes del primer commit; abrí su PR en draft. Implementá CI y Dependabot según la tarea, verificando las versiones vigentes de las actions. Conservá los umbrales de cobertura; verify.sh --client ejecuta la suite y npm run test:coverage comprueba el umbral v8 por separado. Comprobá los jobs server/client y el rechazo de un test roto, registrá evidencias y los checks que el dueño debe requerir en T-014. Actualizá tarea/STATUS/BACKLOG, guardá todos los cambios propios con commit y push y dejá el PR listo para revisión independiente. No mergees ni inicies T-012.
```

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
9/15 tareas P0 mergeadas (T-001 a T-009). Próxima acción: dueño integra #14; luego sesión nueva de 🧠 para T-010. Camino: CI → imágenes → estados de la home → E2E; protección y rotación pendientes del dueño.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ✅ server + client | Revisión T-009: gate completo ✅ (95 s), 118 tests back/6 front; npm ci reproducible; --client ✅ (17 s), cobertura y archivo individual ✅. Cierre #14 solo Markdown: diff/enlaces/estados verificados, sin repetir el gate |
| CI en development | sin CI de build/tests | → T-010, primera Lista tras #14; dependencias satisfechas. Check de título existente. Cobertura front se comprueba por separado con test:coverage |
| Levantar en local (README) | ✅ verificado 2026-10-12 | T-008: compose build/wait OK; server/db healthy; health público UP, probes curl y registro/login desde front OK |
| Cobertura back / front | 82,59 % / 1,65 % | Gate T-009: back 517/626 líneas, umbral 40 %; front 54/3254 líneas, umbral 1 % (floor de la base). Rechazo de cobertura al exigir 100 % verificado; HostCardItem 100 % |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | última auditoría: 16 altas, 8 moderadas | Medición histórica 2026-10-10, no actualizada en este cierre; → Backlog P1 |
| Seguridad 🔴 abierta | rotación pendiente; T-003 mergeada | Registro como ADMIN y listado/borrado por USER corregidos en PR #6. T-015: dueño confirma deploy y rota el secreto histórico si corresponde |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 9/15 tareas mergeadas (T-001 a T-009). Pendientes T-010 a T-015; T-016 fue mantenimiento aparte. No declarar P0 cerrada hasta completar sus criterios y resolver los hallazgos P0 abiertos · ver [ROADMAP](ROADMAP.md)

## Foco
- **PR abierto de cierre documental:** `docs/T-009-closeout` · [PR #14](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/14), pendiente del dueño. #13 confirmado MERGED en development (`dc27563`); ninguna implementación nueva iniciada.
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
- 2026-10-13 · 🧠 Frontera (Codex) · T-009 implementada, revisada APROBADA y mergeada por el dueño en #13 (`dc27563`). 6 tests, 1,65 % global / umbral 1 %, rechazo a 100 % y gates completo/client verdes (118 back). Cierre autorizado en #14, base dc27563 y árbol inicial limpio; revisión de estados y dependencias: T-012 Lista, T-011 espera T-012, T-013 espera los tres merges. Siguiente: integrar #14, luego T-010 en sesión nueva. Diff solo Markdown, sin repetir pruebas funcionales ni iniciar otra tarea.
- 2026-10-12 · 🧠 Frontera (Codex) · T-008 implementada, revisada y mergeada por el dueño en #11 (squash 1b8ec82). Gate de revisión: 118 tests, lint/build ✅, cobertura 82,59 %, compose/probes OK; límites manuales en la tarea. Cierre documental autorizado en #12, desde base 1b8ec82 y árbol limpio; diff/enlaces/cola verificados, sin repetir tests por cambios solo Markdown. Próximo: dueño integra #12, después sesión nueva de 🧠 para T-009.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007: cambios pedidos del PR #10 atendidos. Tests de update y save corregidos. 77 tests OK, verify.sh --server ✅, cobertura 95,07 % PropertyServiceImp. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) (ready). Nueva revisión por 🧠.
- 2026-10-12 · 🤖 Junie Lite (Free) · T-007 completada: 18 tests de caracterización de PropertyServiceImp (94.3 % cobertura). 77 tests OK, verify.sh --server ✅. [PR #10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) creado (draft). Revisión por 🧠.
- 2026-10-12 · 🤖 Junie Lite (Free) · Documentación corregida: T-006 (PR #9) ya estaba mergeada pero no reflejada como cerrada en STATUS.md y BACKLOG.md. T-007 establecida como próxima acción.
