# Estado del proyecto
_Actualizado: 2026-10-10 · por: 🧠 modelo de frontera (Claude Code) · sesión: auditoría (T-000)_

## En una línea
Bootstrap en curso: **PC4** alcanzado (harness, docs, tablero y protocolos). Faltan las pruebas en seco desde un clon limpio (Fase 4).

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ❌ | server ✅ build ✅ tests (1) · client ❌ lint (93 problemas) ✅ build → T-005 |
| CI en development | sin CI | → T-010 (bloqueada por T-005) |
| Levantar en local (README) | ⚠️ verificado 2026-10-10 | contenedores ✅ y desarrollo ✅; front → API de producción (T-004) y home rota (T-011) |
| Cobertura back / front | 16,8 % / sin tests | sin umbral todavía (T-002, T-009) |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | → Backlog P1 |
| Seguridad 🔴 abierta | 3 | secreto JWT (T-001/T-015), registro como ADMIN y borrado de usuarios (T-003) |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 0/15 tareas · ver [ROADMAP](ROADMAP.md)

## Foco
- PR abierto (máx. 1): [#2](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/2) T-000 (draft)
- **Próximo paso:** T-000 · pruebas en seco y cierre (Fase 4) · **ejecuta: 🧠 modelo de frontera**
- **Qué hacer:** abrí una sesión de Claude Code y escribí: «Seguí el bootstrap desde el PC4: leé `docs/STATUS.md`»
- **Después:** T-001 (🧠) → T-002 (🧠) → T-003 (🧠) → T-004 (🤖)

## Decisiones pendientes del dueño
- ¿El deploy anterior (Vercel + back) sigue activo? (define T-015)
- ¿Renombrar `development` → `main`? (T-014; los docs usan `development`)
- ¿Licencia? (proyecto de 6 personas; sugerido MIT tras consultarlo) (Backlog P3)
- ¿El rol ADMIN se asigna solo por base/seed? (T-003 asume que sí)
- ¿Reservas en el alcance del portfolio (P2) o se quita la UI?

## Últimas sesiones (máx. 5; las anteriores → archive/log/AAAA-MM.md)
- 2026-10-10 · 🧠 Claude Code · auditó y montó el harness (T-000) · PR #2 (draft)
