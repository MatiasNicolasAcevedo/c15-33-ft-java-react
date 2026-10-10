# AGENTS.md

Instrucciones para cualquier agente (🤖 agente gratuito de IntelliJ, 🧠 Claude Code) que llega sin contexto. Estable: el estado vivo está en `docs/STATUS.md`.

## 1. Proyecto, mapa y ejecutores

Clon de Airbnb (No Country, 2023) que se retoma para portfolio: API Spring Boot + SPA React. Monorepo:

```
backend/                 API Spring Boot ("server")        → backend/AGENTS.md
frontend/airbnb_clone/   SPA React + Vite ("client")       → frontend/airbnb_clone/AGENTS.md
scripts/                 verify.sh (gate) · dev.sh · seed-demo.sh
docker-compose.yml       db (siempre) · server y client (profile app)
docs/                    STATUS, BACKLOG, tasks/, agents/, audits/, adr/…
```

| Ícono | Ejecutor | Hace |
|---|---|---|
| 🧠 | **Modelo de frontera** (Claude Code u otro potente) | seguridad, arquitectura, infra de tests/CI/contenedores, migraciones, specs y tareas, revisiones riesgosas, auditorías |
| 🤖 | **Agente gratuito** (IntelliJ, modelo Qwen) | tareas chicas y muy especificadas, de a una, con sus tests; revisiones de PRs chicos sin riesgo |
| 👤 | **Dueño** | decide, configura GitHub, rota secretos, abre/mergea PRs, **cambia de agente** |

**Traspasos.** Ningún agente lanza a otro. Cuando el próximo paso es de otro ejecutor, terminá con:

> **Próximo paso:** T-NNN · <título> · **ejecuta: 🤖 agente gratuito (IntelliJ)** | **🧠 modelo de frontera** | **👤 dueño**
> **Qué hacer:** abrí <chat nuevo del agente de IntelliJ | una sesión nueva de Claude Code | GitHub → …> y escribí: «Seguí `docs/agents/implementer.md`» (o el protocolo que corresponda).

## 2. Stack (versiones reales)

Java 17 (compila también con 21) · Spring Boot 3.2.0 · Spring Security + jjwt 0.11.5 · Spring Data JPA (Hibernate, `ddl-auto=update`) · MySQL 8.4 · springdoc 2.2 · Maven Wrapper (Maven 3.9.5) · React 18.2 · Vite 5 · React Router 6 · Tailwind 3 + Material Tailwind · JavaScript (sin TS) · ESLint 8 · Node 24 (`.nvmrc`; funciona con 22).

## 3. Comandos verificados

Setup completo: ver [README](README.md) → "Levantar el proyecto en local" (no lo dupliques).

| Qué | Comando (desde la raíz salvo indicación) |
|---|---|
| Gate (build + lint + tests) | `./scripts/verify.sh` · `--server` · `--client` → resumen al final, detalle en `.verify-logs/` |
| Solo DB | `docker compose up -d --wait` |
| Todo en contenedores | `docker compose --profile app up -d --build --wait` |
| Desarrollo (DB + back + front) | `./scripts/dev.sh` (`Ctrl+C` corta todo; `--down` apaga la DB) |
| Usuario demo | `./scripts/seed-demo.sh` → `demo-owner@clonair.local` / `Demo1234!` |
| Un test del back | `cd backend && ./mvnw -q test -Dtest='Clase#metodo'` (con la DB arriba) |
| Un test del front | desde T-009: `cd frontend/airbnb_clone && npx vitest run <archivo>` |
| E2E | no existe todavía (T-013 crea `scripts/e2e.sh`) |

## 4. Protocolo de sesión

Detalle completo: [`docs/agents/implementer.md`](docs/agents/implementer.md) · [`reviewer.md`](docs/agents/reviewer.md) · [`auditor.md`](docs/agents/auditor.md) (solo 🧠).

1. Leé `docs/STATUS.md` y `docs/BACKLOG.md`; mirá los PR abiertos (`gh pr list` o `git ls-remote --heads origin`).
2. **Una tarea por sesión y una a la vez:** si hay un PR de tarea abierto, no empieces otra.
3. Tomá **siempre la primera** de "Listas". Si no te corresponde (es 🧠 y sos 🤖, o es 👤): avisá con el formato de Traspasos y terminá. No saltees.
4. **Creá la rama con el nombre exacto de la tarea antes del primer commit**; PR en draft enseguida.
5. Tests en el mismo PR. Al terminar: `verify.sh` en verde, tablero/tarea/STATUS actualizados, PR "ready", y sugerí **sesión nueva**.

## 5. Gestión de contexto

1. **Una tarea = una sesión.** Al cerrar una tarea, revisión o auditoría no sigas con otra: sugerí sesión nueva (mejor que compactar).
2. Sugerí compactar **solo a mitad de un trabajo largo** y si: cerraste una sub-etapa; acumulaste logs o archivos que ya no sirven; el contexto pasa ~60 % (🧠) o ~50 % (🤖); o hay señales de degradación (releer archivos, olvidar decisiones, repetir intentos fallidos).
3. **Antes de compactar, persistí:** "Notas de implementación" de la tarea (hecho / pendiente / próximo paso), commit y push.
4. Cómo: 🧠 `/compact Conservar: tarea T-NNN, rama y PR, decisiones, archivos modificados, estado de verify y próximo paso. Descartar: logs y contenido de archivos ya procesados.` + «después escribime "seguí con T-NNN"». 🤖 (sin `/compact`): «abrí un chat nuevo y escribí: Seguí `docs/agents/implementer.md` (retomando T-NNN)».
5. **No compactar** justo antes de terminar ni entre tareas.
6. Higiene: salida acotada (`-q`, `| tail -n 60`, logs a archivo + `grep`); grep/glob y rangos de líneas; no abrir `package-lock.json`, `target/`, `node_modules/`, `dist/`, `.verify-logs/`, generados ni `docs/archive/`; respuestas cortas.

## 6. Configuración

- **Regla:** todo archivo de configuración con valores por entorno tiene su `.example` commiteado; el real está en `.gitignore`. Nunca leas ni commitees los reales.
- Back: `backend/src/main/resources/env.properties.example` → copiar a `backend/src/main/resources/env.properties` (**classpath**; en `backend/` no se lee).
- Front: `frontend/airbnb_clone/.env.example` → `frontend/airbnb_clone/.env`.
- Al agregar o renombrar una variable, en el mismo PR: `.example` (comentario + valor local válido), `docker-compose.yml`, tabla de `docs/SETUP.md` y README si cambia un paso.

## 7. Mapa de documentación

| Archivo | Qué contiene | Cuándo leerlo |
|---|---|---|
| `docs/STATUS.md` | dónde estamos y próximo paso | siempre, primero |
| `docs/BACKLOG.md` | tablero y cola | siempre |
| `docs/tasks/T-NNN-*.md` | contrato de cada tarea | la tarea que tomás |
| `docs/agents/*.md` | protocolos de sesión | el que te indiquen |
| `CONTRIBUTING.md` | ramas, commits, PR, revisión, merge | al crear rama/PR o revisar |
| `docs/TESTING.md` | estrategia y referencias de tests | al escribir tests |
| `docs/SETUP.md` | variables, puertos, IDE, agentes | al tocar configuración |
| `docs/ARCHITECTURE.md` | diagramas, modelo, endpoints | si la tarea toca varias capas |
| `docs/adr/` | decisiones | si la tarea lo enlaza |
| `docs/audits/`, `docs/archive/` | historia | **no se leen** salvo que la tarea lo indique |

## 8. Convenciones actuales del código (no las ideales)

- Back: paquete por feature (`user`, `property`, `image`, `security`); controller → interfaz `XService` + `XServiceImp` → `JpaRepository`. Lombok (`@Data`, `@RequiredArgsConstructor`), DTOs como `record`. Referencia de capa: `property/PropertyController.java`, `property/PropertyServiceImp.java`. Detalle en `backend/AGENTS.md`.
- Front: componentes funcionales `.jsx` con `export const`, una carpeta por componente con `index.js`, llamadas a la API **solo** en `src/services/apiRequests.jsx`, Tailwind en `className`. Detalle en `frontend/airbnb_clone/AGENTS.md`.
- Tests: ver tabla de referencias en `docs/TESTING.md` §3.

## 9. Git (resumen de [CONTRIBUTING](CONTRIBUTING.md))

Rama por defecto **`development`**. Rama por tarea `<tipo>/T-NNN-slug` creada antes del primer commit. Conventional Commits en inglés con `Refs: T-NNN`. Un PR chico por tarea, título Conventional Commit, squash; lo mergea el dueño.

## 10. Límites (no se toca sin una tarea que lo pida)

Versiones mayores de dependencias · esquema de DB y datos · seguridad/autenticación (`backend/.../security/**`) · deploy, CI y `docker-compose.yml` · secretos y configuración real o de producción · borrar, deshabilitar o debilitar tests o umbrales · commitear a `development` · mergear sin revisión · "arreglos de paso" (el trabajo extra va a Backlog).

## 11. Gotchas del entorno

- `env.properties` en la ruta equivocada → `Failed to parse the host:port pair 'localhost:${DB_PORT}'`.
- Sin `@RestControllerAdvice`, **todo error de la API es 403** (hasta T-008): no asumas que 403 = sin permiso; mirá el log del back.
- No hay health endpoint: usá `GET /v3/api-docs` → 200.
- El front llama a una IP de producción hardcodeada hasta T-004.
- `verify.sh` necesita Docker (levanta la DB de compose).
- En contenedores con proxy TLS (cloud): Docker Hub puede dar 429 y `docker build` puede fallar con `PKIX path building failed` (ver `docs/SETUP.md` → Modelo de frontera).
