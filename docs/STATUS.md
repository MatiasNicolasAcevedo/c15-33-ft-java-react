# Estado del proyecto
_Actualizado: 2026-10-10 · por: 🧠 modelo de frontera (Claude Code) · sesión: auditoría (T-000)_

## En una línea
Bootstrap en curso: **PC3** alcanzado (contenedores, harness y docs). Faltan tablero, tareas y protocolos (PC4) y pruebas en seco.

## Salud
| Check | Estado | Nota |
|---|---|---|
| verify.sh | ❌ | server ✅ (1 test) · client ❌ lint (93) → T-005 |
| CI en development | sin CI | |
| Levantar en local (README) | ⚠️ 2026-10-10 | contenedores y desarrollo levantan; el front apunta a producción → T-004 |
| Cobertura back / front | 16,8 % / sin tests | línea, medida con JaCoCo CLI |
| E2E smoke | no existe | |
| Dependencias vulnerables | npm: 16 altas, 8 moderadas | |
| Última auditoría | 2026-10-10 | [en curso](audits/2026-10-10-initial.md) |

## Foco
- PR abierto: T-000 `chore/T-000-agent-bootstrap` (draft)
- **Próximo paso:** T-000 · seguir el bootstrap desde el PC3 · **ejecuta: 🧠 modelo de frontera**
- **Qué hacer:** abrí una sesión de Claude Code y escribí: «Seguí el bootstrap desde el PC3: leé `docs/STATUS.md` y `docs/audits/2026-10-10-initial.md`»
