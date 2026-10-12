# AGENTS.md

Instrucciones para cualquier agente (🤖 Junie Lite (Free), 🧠 modelo de frontera) que llega sin contexto. Estable: el estado vivo y la próxima acción están en `docs/STATUS.md`.

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
| 🧠 | **Frontera** (p. ej. **GPT-6.1 Sol** o **Claude Opus 5.5**) | seguridad, arquitectura, infra de tests/CI/contenedores, migraciones, specs y tareas, revisiones riesgosas, auditorías |
| 🤖 | **Junie Lite (Free)** en IntelliJ mediante el plugin **Air** | tareas chicas y muy especificadas, de a una, con sus tests; revisiones de PRs chicos sin riesgo |
| 👤 | **Dueño** | decide, configura GitHub, rota secretos, abre/mergea PRs, **cambia de agente** |

**Modelo y herramienta son cosas distintas.** GPT-6.1 Sol y Claude Opus 5.5 son ejemplos de modelos de frontera; Codex, Claude Code y Junie son agentes que los ejecutan. Usar Air o Junie no define la categoría: importa la configuración elegida. En este repo, **Lite** siempre significa **Junie Lite (Free)**, sin fijar el modelo interno ni un tamaño de contexto. Ver [SETUP](docs/SETUP.md) para las fuentes y el arranque.

**Traspasos.** Ningún agente lanza a otro. Cuando el próximo paso es de otro ejecutor, terminá con:

> **Próximo paso:** T-NNN · <título> · **ejecuta: <🤖 Junie Lite (Free) | 🧠 Frontera — GPT-6.1 Sol / Claude Opus 5.5 | 👤 dueño>**
> **Qué hacer:** abrí <chat nuevo de Junie Lite en Air | sesión nueva con el modelo de frontera elegido | GitHub → …> y escribí: «Leé `AGENTS.md` y seguí `docs/agents/implementer.md` para T-NNN» (o el protocolo que corresponda).

Completá el traspaso con **un solo ejecutor**, sin dejar las alternativas de la plantilla. En `STATUS.md`, la primera tarjeta muestra **acción, tarea, ejecutor, rama/PR y prompt**. Si hay un PR abierto, primero revisión/merge/cambios pedidos; si no, la primera de "Listas" en `BACKLOG.md`. Mantené ambas vistas sincronizadas.

**Prompt obligatorio para Junie Lite.** Cuando la próxima acción corresponda a **🤖 Junie Lite (Free)**, incluí en la respuesta final un bloque `text` con el **prompt completo, listo para copiar y pegar en un chat nuevo de Junie Lite en Air**. Debe indicar el ID real y el archivo de la tarea, el protocolo que corresponde (implementación, revisión o retoma) y la rama/PR si ya existen. Para una retoma o cambios pedidos, agregá el punto de continuación y conservá la rama/PR. No dejes placeholders, alternativas ni solo una indicación de cambiar de agente. Guardá el mismo prompt concreto en la tarjeta de `STATUS.md` y el aviso de `BACKLOG.md`. Esta regla también se aplica después de un merge; no habilita saltear la cola ni iniciar una tarea mientras haya un PR pendiente. El dueño pega el prompt; ningún agente lanza a Junie.

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
| Un test del back | `cd backend && ./mvnw -q test -Dtest='Clase#metodo'` (Docker corriendo para integración; sin DB de compose ni config manual) |
| Un test del front | desde T-009: `cd frontend/airbnb_clone && npx vitest run <archivo>` |
| E2E | no existe todavía (T-013 crea `scripts/e2e.sh`) |

## 4. Protocolo de sesión

Detalle completo: [`docs/agents/implementer.md`](docs/agents/implementer.md) · [`reviewer.md`](docs/agents/reviewer.md) · [`auditor.md`](docs/agents/auditor.md) (solo 🧠).

1. Leé `docs/STATUS.md` y `docs/BACKLOG.md`; mirá los PR abiertos (`gh pr list` o `git ls-remote --heads origin`).
2. **Una tarea por sesión y una a la vez:** si hay un PR de tarea abierto, no empieces otra.
3. Tomá **siempre la primera** de "Listas". Si no te corresponde (es 🧠 y sos 🤖, o es 👤): avisá con el formato de Traspasos y terminá. No saltees.
4. **Creá la rama con el nombre exacto de la tarea antes del primer commit**; PR en draft enseguida.
   Excepción: si el dueño autorizó explícitamente una rama asignada por el entorno de nube y el push del nombre exacto falla por esa restricción, registrá la evidencia y la rama real en la tarea, `STATUS.md` y el PR. Ver `docs/SETUP.md` → Prompt de inicio y contexto de nube.
5. Tests en el mismo PR. Al terminar: `verify.sh` en verde, tablero/tarea/STATUS actualizados, PR "ready", y sugerí **sesión nueva**.

## 5. Gestión de contexto

**Una tarea = una sesión de trabajo; puede continuar en un chat nuevo si necesita recuperar contexto.** Compactar o cambiar de chat para retomar no cierra la tarea ni habilita otra. Conservá su rama y PR.

| Situación | Acción | Continuación |
|---|---|---|
| Misma tarea larga, sub-etapa cerrada, logs acumulados o señales de pérdida de contexto | Persistir el punto de retoma y compactar si la interfaz lo permite | Retomar **la misma T-NNN**, rama y PR |
| Contexto usado cerca de ~60 % en 🧠 o ~50 % en 🤖, si la interfaz muestra ese dato | Buscar el próximo corte seguro y persistir; son alertas orientativas del repo | Compactar o abrir un chat nuevo para **la misma tarea** |
| Lite, o cualquier interfaz sin compactación disponible | Persistir y abrir un chat nuevo con el mismo ejecutor | Usar el prompt de retoma de abajo |
| Tarea, revisión o auditoría terminada; o cambio de ejecutor | Cerrar con traspaso y abrir una sesión nueva | Seguir la próxima acción indicada en `STATUS.md` |
| Falta poco para terminar | Terminar y registrar el resultado | Sesión nueva para la siguiente acción; no compactar entre tareas |

**Antes de compactar o cambiar de chat:**
1. Completá "Notas de implementación" en la tarea: hecho, pendiente, decisiones, archivos modificados, pruebas/verify (resultado o no ejecutado), bloqueos y **próximo comando o acción concreta**. Anotá rama, PR (o pendiente) y último commit. En auditorías, usá el documento de la auditoría.
2. Actualizá `STATUS.md` con la acción **Retomar T-NNN**, el ejecutor y la rama/PR; mantené la tarea en curso. Hacé commit de los cambios propios que se puedan guardar sin secretos y push. Si el push falla, dejá escrito que solo existe el checkpoint local; no anuncies que está publicado.
3. Pedí conservar tarea, rama/PR, decisiones, estado de pruebas y próximo paso; descartar logs repetidos y contenido de archivos ya procesados.

**Cómo compactar:** depende del agente y su interfaz, no del modelo. En Codex CLI, usá `/compact`; en Air u otra interfaz, comprobá si ofrece esa acción. Si permite instrucciones para el resumen, usá: «Conservar: T-NNN, rama y PR, decisiones, archivos modificados, pruebas/verify, bloqueos y próximo paso. Descartar: logs repetidos y archivos ya procesados». Para Lite, el procedimiento del repo es **persistir + chat nuevo**, sin asumir comandos disponibles. Ver [SETUP](docs/SETUP.md) para la referencia de comandos.

**Prompt para continuar**, después de compactar o en el chat nuevo:
> Leé `AGENTS.md`, `docs/STATUS.md` y las Notas de implementación de `docs/tasks/T-NNN-slug.md`. Seguí `docs/agents/implementer.md` **retomando T-NNN** en la rama `<rama>` y el PR `<#N o pendiente>`. Verificá `git status` y el último commit; continuá desde el próximo paso registrado. No crees otra rama ni otro PR para esta retoma.

**Higiene:** salida acotada (`-q`, `| tail -n 60`, logs a archivo + `grep`); grep/glob y rangos de líneas; no abrir `package-lock.json`, `target/`, `node_modules/`, `dist/`, `.verify-logs/`, generados ni `docs/archive/`; respuestas cortas. No asumas un tamaño fijo de contexto para Lite ni inventes un porcentaje si la interfaz no lo muestra.

### Cierre y cambios sin commit (obligatorio)

- Al iniciar, inventariá `git status --short`: distinguí los cambios previos de los propios y comprobá su diff antes de incluirlos. No uses `git add .` ni `git add -A` para incorporar archivos sin revisar; agregá rutas explícitas.
- Antes de anunciar una implementación lista para revisión, hacer un traspaso o cambiar de tarea, guardá **todos los cambios propios**, incluida la documentación: tarea, `STATUS.md`, `BACKLOG.md`, protocolos y reglas del agente. Actualizá los documentos **antes del último commit**, hacé commit y push en la rama correspondiente, y volvé a comprobar `git status --short`. El cierre normal debe dejar el árbol limpio.
- Los cambios heredados no pasan automáticamente a la próxima tarea. Resolvé su destino antes del cambio de tarea: incorporalos solo si pertenecen al alcance o el dueño lo autorizó explícitamente; en otro caso, conservá los archivos y registrá rutas, motivo y acción pendiente en la tarea y `STATUS.md`. No los borres, descartes ni ocultes con stash para aparentar un cierre limpio.
- Comprobá que el commit local coincide con el publicado en la rama del PR; un commit local sin push no está publicado. Si falla commit/push o quedan archivos pendientes, informá el estado exacto y el próximo paso; no anuncies el cierre completo ni habilites la siguiente tarea. Las excepciones deben quedar explícitas y registradas, no solo mencionadas en el chat.
- En revisión, respetá la prohibición de commitear sin pedido del dueño: podés publicar un veredicto sobre un commit concreto, pero debés señalar cualquier pendiente local que impida el traspaso limpio. Si el dueño autoriza guardarlo, revisá el diff agregado y actualizá la evidencia del PR.

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
- Errores API: `application/problem+json` con 400 (request inválido), 401 (autenticación), 403 (permiso), 404 (no encontrado), 409 (conflicto), 500 (interno sin detalles). Usá las excepciones de `common/error`; no devuelvas errores a mano en controllers.
- Health público: `GET /actuator/health` → 200 + `{"status":"UP"}`. Es el probe de compose y dev.sh; Swagger sigue en `/v3/api-docs`.
- El front usa `VITE_API_URL` con prefijo `/v1/api` desde T-004; default local `http://localhost:8080`.
- `verify.sh --server` necesita Docker (MySQL efímera vía Testcontainers; sin DB de compose ni config manual).
- En contenedores con proxy TLS (cloud): Docker Hub puede dar 429 y `docker build` puede fallar con `PKIX path building failed` (ver `docs/SETUP.md` → Modelo de frontera).
