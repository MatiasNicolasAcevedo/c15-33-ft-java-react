# Estado del proyecto
_Actualizado: 2026-10-10 · por: 🧠 modelo de frontera (Claude Code) · sesión: auditoría (T-000)_

## En una línea
Harness montado y verificado desde un clon limpio (compose, `.example`, `verify.sh`, `dev.sh`, docs, tablero y protocolos) en el PR de T-000. La app levanta en local, pero el front todavía llama a producción y casi no hay tests.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ❌ | server ✅ build ✅ tests (1) · client ❌ lint (93 problemas) ✅ build → T-005 |
| CI en development | sin CI | → T-010 (bloqueada por T-005) |
| Levantar en local (README) | ⚠️ verificado 2026-10-10 | contenedores ✅ y desarrollo ✅ desde clon limpio; el front llama a producción (T-004) y la home se rompe (T-011) |
| Cobertura back / front | 16,8 % / sin tests | sin umbral todavía (T-002, T-009) |
| E2E smoke | no existe | → T-013 |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | → Backlog P1 |
| Seguridad 🔴 abierta | 3 | secreto JWT (T-001/T-015), registro como ADMIN y borrado de usuarios (T-003) |
| Última auditoría | 2026-10-10 | [inicial](audits/2026-10-10-initial.md) |

## Fase actual
P0 Estabilizar · 0/15 tareas · ver [ROADMAP](ROADMAP.md)

## Foco
- PR abierto (máx. 1): [#2](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/2) T-000 (esperando revisión y merge del 👤)
- **Próximo paso:** revisar y mergear el PR #2 · **ejecuta: 👤 dueño** → después T-001 · Sacar el secreto y la expiración del JWT a configuración · **ejecuta: 🧠 modelo de frontera**
- **Qué hacer:** GitHub → PR #2 → revisar y **Squash and merge**. Después abrí una sesión nueva de Claude Code y escribí: «Seguí `docs/agents/implementer.md`»
- **Después:** T-002 (🧠) → T-003 (🧠) → T-004 (🤖) _(en T-004 se cambia al 🤖 hasta T-007; vuelve el 🧠 en T-008)_

## Decisiones pendientes del dueño
- ¿El deploy anterior (Vercel + back) sigue activo? (define T-015)
- ¿Renombrar `development` → `main`? (T-014; los docs usan `development`)
- ¿Licencia? (proyecto de 6 personas; sugerido MIT tras consultarlo) (Backlog P3)
- ¿El rol ADMIN se asigna solo por base/seed? (T-003 asume que sí)
- ¿Reservas en el alcance del portfolio (P2) o se quita la UI?
- Después del merge de T-000, crear el tag de la auditoría:
  `git switch development && git pull && git tag audit/2026-10-10 && git push origin audit/2026-10-10`

## Últimas sesiones (máx. 5; las anteriores → archive/log/AAAA-MM.md)
- 2026-10-10 · 🧠 Claude Code · auditó y montó el harness (T-000) · PR #2 en revisión
