# Setup detallado

Complementa el [README](../README.md) (que tiene el paso a paso); acá va solo lo que no entra ahí.

## Variables de configuración

| Nombre | Para qué | Valor local | Obligatoria | Dónde se define en prod |
|---|---|---|---|---|
| `APP_PORT` | Puerto HTTP del back (`application.properties:2`) | `8080` | sí (no tiene default: X-2) | variable de entorno del servicio |
| `DB_PORT` | Puerto de MySQL en `localhost` (`application.properties:3`) | `3306` | sí | variable de entorno |
| `DB_NAME` | Nombre de la base | `clonair` | sí | variable de entorno |
| `DB_USER` | Usuario de la base | `clonair` | sí | variable de entorno / secreto |
| `DB_PASS` | Contraseña de la base | `clonair_local_only` (inventada) | sí | **secreto** del proveedor |
| `SPRING_DATASOURCE_URL` | Sobrescribe la URL JDBC completa (cuando la DB no está en `localhost`, p. ej. en compose) | no se usa en local | no | variable de entorno |
| `VITE_API_URL` | URL base del back para el front (build time) | `http://localhost:8080` | sí, desde T-004 | variable de build del hosting del front |
| `JWT_SECRET`, `JWT_EXPIRATION` | Firma y duración de tokens | — | se agregan en T-001 | **secreto** |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos | — | se agrega en T-003 | variable de entorno |

Archivos: `backend/src/main/resources/env.properties` (copia de su `.example`, leído desde el classpath) y `frontend/airbnb_clone/.env`. En compose, las mismas variables están en `docker-compose.yml` → `services.server.environment`.

## Puertos

| Servicio | Puerto | Cómo cambiarlo |
|---|---|---|
| Back | 8080 | `APP_PORT` en `env.properties` (y el mapeo `ports` de compose) |
| Front (dev, Vite) | 5173 | `npm run dev -- --port 5174` |
| Front (contenedor, nginx) | 8081 | `ports` del servicio `client` en `docker-compose.yml` |
| MySQL | 3306 | `DB_PORT=3307 docker compose up -d --wait` + `DB_PORT=3307` en `env.properties` |

## Sin Docker

Con un MySQL 8 propio: crear usuario y base con los valores de `env.properties` (o ajustar el archivo), y seguir el README desde "Levantar el back". `createDatabaseIfNotExist=true` crea la base si el usuario tiene permisos. No probado en la auditoría (⚠️).

## Datos y seed

- No hay seed en la base: `./scripts/seed-demo.sh` crea por API el usuario `demo-owner@clonair.local` / `Demo1234!` (OWNER, solo local). Es idempotente.
- El esquema lo crea Hibernate (`ddl-auto=update`). **Resetear todo:** `docker compose down -v` (borra el volumen `clonair_db-data`) y volver a levantar.
- Entrar a la base: `docker compose exec db mysql -uclonair -pclonair_local_only clonair`.

## IDE

- **IntelliJ IDEA:** abrir la raíz del repo; importar `backend/pom.xml` como proyecto Maven (SDK 17). Run configuration: `BackApplication` con working directory `backend/`; la config la toma de `src/main/resources/env.properties`. Para el front: `npm run dev` desde la ventana npm o la terminal.
- **VS Code:** Extension Pack for Java + ESLint. `.vscode/settings.json` ya existe.
- `.editorconfig` en la raíz define indentación y finales de línea.

## Debugging

- Back: `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"` y "Attach to remote JVM" al puerto 5005 (⚠️ no probado). SQL ya se loguea (`show-sql=true`).
- Front: React DevTools; las llamadas están todas en `src/services/apiRequests.jsx`.
- Logs del harness: `.verify-logs/` (verify) y `.dev-logs/` (dev.sh).

## 🤖 Agente gratuito de IntelliJ

⚠️ **No verificado**: no se pudo confirmar en docs de JetBrains qué agente gratuito con modelo Qwen trae IntelliJ ni qué archivo lee. Lo que sí documenta JetBrains: el chat de **AI Assistant** lee *project rules* en `.aiassistant/rules/*.md` y el agente **Junie** lee `.junie/guidelines.md` (o el `AGENTS.md` si se configura la ruta). Por eso el repo tiene ambos como **punteros de una línea** a `AGENTS.md`. Fuentes: [AI Assistant · Junie](https://www.jetbrains.com.cn/en-us/help/ai-assistant/junie-agent.html).

Cómo usarlo (ajustá si tu versión difiere y actualizá esta sección):

1. **Chat nuevo** para cada tarea, revisión o retoma (no hay `/compact`: compactar = persistir + chat nuevo).
2. Elegir **modo agente** (no solo chat), para que pueda editar archivos y correr comandos.
3. Si existe la opción de rules, marcar `.aiassistant/rules/agents.md` como *Always*.
4. Dar permiso para **ejecutar comandos en la terminal** del IDE (al menos `./scripts/verify.sh`, `./mvnw`, `npm`, `git`).
5. Frase de arranque (cubre el caso de que no lea los punteros): «Leé `AGENTS.md` y seguí `docs/agents/implementer.md`».
6. **Git:** el agente solo usa `git` (sin `gh` ni MCP). Crea la rama, commitea y hace `git push -u origin <rama>`; al final te deja un link `https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/compare/development...<rama>?expand=1` con título y cuerpo listos para pegar. **Vos abrís el PR** (en draft si recién empieza).

## 🧠 Modelo de frontera (Claude Code)

- Lee `CLAUDE.md` → `AGENTS.md`. Permisos seguros en `.claude/settings.json` (deniega push forzado, push a `development`/`main` y leer la config real).
- Tiene `gh` o el MCP de GitHub: abre PRs, publica revisiones como comentario y, solo si se lo pedís para un PR concreto, mergea (ver `CONTRIBUTING.md`).
- Sesión nueva por tarea; `/compact` solo a mitad de un trabajo largo (ver `AGENTS.md` → Gestión de contexto).
- Si corre en un contenedor con proxy (como la auditoría): Docker Hub puede devolver 429 (usar el mirror `mirror.gcr.io` en el daemon) y los `docker build` no confían en el CA del proxy (ver `docs/audits/2026-10-10-initial.md` → Verificación).
