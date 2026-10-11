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
| `JWT_SECRET` | Clave de firma HS256: Base64 de al menos 32 bytes (256 bits); se valida al iniciar | clave pública solo local en `.example` y compose | **sí, sin default en el back** | **secreto** propio del proveedor, generado con `openssl rand -base64 32` |
| `JWT_EXPIRATION` | Duración de tokens en milisegundos | `86400000` (24 h) | no; default `86400000` | variable de entorno |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos | — | se agrega en T-003 | variable de entorno |

Archivos: `backend/src/main/resources/env.properties` (copia de su `.example`, leído desde el classpath) y `frontend/airbnb_clone/.env`. En compose, las mismas variables están en `docker-compose.yml` → `services.server.environment`.

Si ya tenías un `env.properties` local, agregá `JWT_SECRET` y `JWT_EXPIRATION` desde el `.example` actualizado. Sin secreto, con Base64 inválido o con menos de 32 bytes decodificados, el back no arranca: el error identifica `JWT_SECRET` sin imprimir su valor. Compose usa la clave pública del `.example` solo para desarrollo y permite sobrescribir ambas variables desde el entorno. En producción usá un secreto propio; su rotación invalida los tokens anteriores (T-015).

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

## 🤖 Junie Lite (Free) en Air

**Lite** es la configuración gratuita de **Junie** que se usa mediante el [plugin Air para IntelliJ](https://plugins.jetbrains.com/plugin/33314-air). Air integra agentes; no es un modelo. JetBrains documenta Junie Lite gratuito con una cuenta JetBrains y aclara que las condiciones y los modelos disponibles pueden cambiar. Por eso las tareas usan el nombre **Junie Lite (Free)**, sin depender del modelo interno. Fuente: [anuncio oficial de Air y Junie Lite](https://blog.jetbrains.com/ai/2026/10/air-in-ides-eap/).

Cómo usarlo:

1. Abrir Air en IntelliJ, iniciar sesión con tu cuenta JetBrains y elegir **Junie Lite**. Verificá esa configuración en la sesión: Junie con un modelo de frontera pertenece a 🧠.
2. Abrir un **chat nuevo** para cada tarea o revisión. Para retomar la misma tarea en otro chat, primero guardar el checkpoint de `AGENTS.md` → Gestión de contexto; conservar rama y PR.
3. Junie documenta la lectura de `AGENTS.md`. El puntero `.junie/guidelines.md` del repo usa el formato heredado; `.aiassistant/rules/agents.md` sirve para AI Assistant si también lo usás, no define la configuración de Air. Fuente: [instrucciones y orden de búsqueda de Junie](https://junie.jetbrains.com/docs/guidelines-and-memory.html).
4. Habilitar las herramientas necesarias para editar y ejecutar comandos del proyecto (`verify.sh`, `mvnw`, `npm`, `git`). Comprobar qué herramientas ofrece la instalación; no asumir ausencia de terminal, `gh` o MCP por ser Lite.
5. Frase de arranque: «Leé `AGENTS.md` y seguí `docs/agents/implementer.md` para T-NNN». La tarea debe estar asignada a **🤖 Lite**; no saltear una tarea 🧠 anterior.
6. **PR:** si dispone de `gh` o una integración de GitHub, usarla según el protocolo. Si no, crear la rama, commit y push; dejar al dueño el link `https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/compare/development...<rama>?expand=1`, título y cuerpo listos para abrir el PR en **draft**.

## 🧠 Modelo de frontera — GPT-6.1 Sol / Claude Opus 5.5

- Elegí, por ejemplo, **GPT-6.1 Sol** en Codex o **Claude Opus 5.5** en Claude Code / un agente compatible. La categoría la define el modelo elegido, no la aplicación. Son ejemplos aceptados para las tareas 🧠; no es obligatorio usar Claude Code.
- Arranque común: «Leé `AGENTS.md` y seguí `docs/agents/implementer.md` para T-NNN». Claude Code tiene además el puntero `CLAUDE.md` → `AGENTS.md` y permisos propios en `.claude/settings.json`; esos permisos no se trasladan automáticamente a otros agentes.
- Comprobá si hay `gh` o integración de GitHub. El protocolo contempla el traspaso al dueño si no los hay; disponer de herramientas no autoriza un merge sin pedido explícito para ese PR (ver `CONTRIBUTING.md`).
- Sesión nueva para otra tarea o revisión. Durante **la misma tarea larga**, persistí las notas y compactá si la interfaz lo permite; después retomá el mismo ID, rama y PR. En **Codex CLI**, `/compact` resume el chat; comprobá su disponibilidad en otras interfaces. Fuente: [comandos oficiales de Codex](https://learn.chatgpt.com/docs/developer-commands?surface=cli). El procedimiento completo y el prompt de retoma están en `AGENTS.md` → Gestión de contexto.
- Si corre en un contenedor con proxy (como la auditoría): Docker Hub puede devolver 429 (usar el mirror `mirror.gcr.io` en el daemon) y los `docker build` no confían en el CA del proxy (ver `docs/audits/2026-10-10-initial.md` → Verificación).

## Prompt de inicio y contexto de nube

Para la próxima tarea, abrí una sesión nueva con **🧠 GPT-6.1 Sol o Claude Opus 5.5** y usá:

```text
Leé AGENTS.md y seguí docs/agents/implementer.md para T-001.
Primero comprobá STATUS, BACKLOG, git status y los PR abiertos.
Al terminar, dejame el traspaso para la revisión que indique la tarea.
No mergees salvo que te lo pida explícitamente para ese PR.
Si necesitás compactar o cambiar de chat, primero guardá el punto de retoma
como indica AGENTS.md; continuá la misma tarea, rama y PR.
```

El dueño aportó este contexto para **la nube de Claude Code**. Si trabajás allí, podés agregar el siguiente bloque al prompt. Las instrucciones de `dockerd` y `/etc/docker/daemon.json` son para ese entorno Linux; en el Windows local de IntelliJ, comprobá Docker Desktop y el motor disponible.

```text
Contexto del entorno (solo si corrés en la nube de Claude Code):
- La rama por defecto es development. Tenés mi permiso para crear la rama
  exacta indicada por la tarea y pushearla aunque el entorno te asigne otra.
  Si el push con ese nombre falla por una restricción del entorno, usá la
  asignada y registrá el fallo y el nombre real en la tarea, STATUS y el PR.
- Si docker no responde, comprobá el daemon; si no está iniciado, arrancá
  dockerd en background en ese entorno.
- Si Docker Hub devuelve 429, configurá el mirror https://mirror.gcr.io
  en /etc/docker/daemon.json y reiniciá dockerd, conservando la configuración
  existente. El ajuste es del entorno, no del repositorio.
- Si docker build falla con PKIX path building failed por el CA del proxy,
  registrá el bloqueo. No modifiques el Dockerfile del repo para evitar
  la validación TLS; consultá docs/SETUP.md → Modelo de frontera.
```

La excepción de nombre de rama requiere esa **autorización explícita del dueño en la sesión**. Primero intentá la rama de la tarea; si necesitás la asignada, documentá la evidencia y conservá el ID T-NNN. No aplica al trabajo local habitual. El contexto de nube proviene del traspaso recibido; no se volvió a probar en esta revisión documental.
