<h1 align="center">🏡 Clonair · Clon de Airbnb</h1>

<p align="center">Plataforma para publicar, buscar y reservar alojamientos · Spring Boot + React · proyecto de equipo de No Country (c15-33-ft-java-react)</p>

<p align="center">
  <a href="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/workflows/ci.yml?query=branch%3Adevelopment"><img alt="CI" src="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/actions/workflows/ci.yml/badge.svg?branch=development"></a>
  <img alt="Estado" src="https://img.shields.io/badge/estado-en%20retoma-orange">
  <img alt="Java" src="https://img.shields.io/badge/Java-17-blue">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F">
  <img alt="React" src="https://img.shields.io/badge/React-18-61DAFB">
</p>

<p align="center">
  <img src="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/assets/109118183/4fe0900f-c101-48cb-b7bd-3a4f9ced4aa2" alt="Pantalla principal del clon de Airbnb" width="686">
</p>

## Qué es

Clon de Airbnb desarrollado en 5 semanas (dic-2023) durante la simulación laboral de [No Country](https://www.nocountry.tech/). Hoy se está **retomando** para dejarlo como pieza de portfolio: tests, CI, contenedores y documentación. Estado y plan en [`docs/STATUS.md`](docs/STATUS.md) y [`docs/ROADMAP.md`](docs/ROADMAP.md).

**Features**

- ✅ Registro e inicio de sesión con JWT (roles USER y OWNER).
- ✅ Alta de propiedades con imágenes, servicios y ubicación (OWNER).
- 🚧 Búsqueda y filtros por categoría/subcategoría (UI lista; el front llama al back local pero la home se rompe: T-011).
- 🚧 Detalle de propiedad e imágenes (T-012).
- 🚧 Reservas y "mis viajes" (solo UI; sin back).
- 🚧 Panel de administración (UI; permisos a corregir: T-003).

🎬 [Video demostración](https://youtu.be/30RxYVl8nh8)

## Stack

| Parte | Tecnologías |
|---|---|
| Back (`backend/`) | Java 17 · Spring Boot 3.2.0 (Web, Data JPA, Security) · JWT (jjwt 0.11.5) · MySQL 8.4 · springdoc-openapi 2.2 · Maven Wrapper |
| Front (`frontend/airbnb_clone/`) | React 18.2 · Vite 5 · React Router 6 · Tailwind CSS 3 + Material Tailwind · JavaScript |
| Infra local | Docker Compose (MySQL + back + front con nginx) |

Arquitectura, modelo de datos y endpoints: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

## Levantar el proyecto en local

### Prerrequisitos

| Herramienta | Versión | Cómo comprobarla | Cómo instalarla |
|---|---|---|---|
| Git | cualquiera reciente | `git --version` | https://git-scm.com/downloads |
| Docker + Compose v2 | Docker 24+ | `docker compose version` | https://docs.docker.com/get-docker/ |
| JDK (solo opción B) | 17 (también compila con 21) | `java -version` | SDKMAN: `sdk env install` en `backend/` (usa `backend/.sdkmanrc`) |
| Node.js (solo opción B) | 24 LTS (verificado con 22 y 24) | `node -v` | nvm: `nvm install` en `frontend/airbnb_clone/` (usa `.nvmrc`) |

> Para la **opción A** alcanza con Git y Docker. ⚠️ `sdk env install` y `nvm install` no se pudieron probar en la auditoría (sin SDKMAN/nvm en el entorno).

### Configurar variables de entorno

Cada parte tiene un archivo de ejemplo commiteado. **Los valores sirven tal cual para local**: copialos sin editar. Los archivos reales están en `.gitignore`.

| Archivo de ejemplo | Copiar a | Quién lo lee | Qué contiene |
|---|---|---|---|
| `backend/src/main/resources/env.properties.example` | `backend/src/main/resources/env.properties` | Spring Boot (`spring.config.import`, desde el classpath) | puerto del back, conexión a MySQL, JWT (clave solo local, duración 24 h) y orígenes CORS |
| `frontend/airbnb_clone/.env.example` | `frontend/airbnb_clone/.env` | Vite (`VITE_*`) | URL del back |

Desde la raíz del repo:

```bash
cp backend/src/main/resources/env.properties.example backend/src/main/resources/env.properties
```

```bash
cp frontend/airbnb_clone/.env.example frontend/airbnb_clone/.env
```

> ⚠️ El archivo del back va en `src/main/resources/`: en `backend/env.properties` **no** se lee.
> Si ya tenías un `env.properties`, agregá las variables JWT y `CORS_ALLOWED_ORIGINS` del `.example` actualizado. El back requiere `JWT_SECRET` en Base64 con al menos 32 bytes decodificados; `JWT_EXPIRATION` es opcional y vale `86400000` ms (24 h) por defecto.
> Para el front local, `CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:8081`. Si cambiás el host o puerto del front, actualizá esa lista de orígenes exactos (sin comodines); ausente/vacía rechaza solicitudes entre orígenes.
> **Producción:** definir en el proveedor las variables `APP_PORT`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`, `JWT_SECRET` (clave propia generada con `openssl rand -base64 32`, nunca la pública del `.example`), `CORS_ALLOWED_ORIGINS` (orígenes exactos del front propio) y, opcionalmente, `JWT_EXPIRATION` (y `SPRING_DATASOURCE_URL` si la DB no está en `localhost`). Detalle de cada variable en [`docs/SETUP.md`](docs/SETUP.md).

### Opción A · Todo en contenedores (la más simple)

1. Clonar y entrar al repo:

```bash
git clone https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react.git
```

```bash
cd c15-33-ft-java-react
```

2. Copiar los `.example` (bloques de arriba).
3. Construir y levantar DB + back + front (la primera vez tarda unos minutos):

```bash
docker compose --profile app up -d --build --wait
```

**Resultado esperado:** `Container clonair-db-1 Healthy`, `Container clonair-server-1 Healthy`, `Container clonair-client-1 Healthy`.

4. Crear el usuario demo y comprobar el login:

```bash
./scripts/seed-demo.sh
```

**Resultado esperado:** `✅ login demo OK · usuario: demo-owner@clonair.local · contraseña: Demo1234!`

5. Abrir: front http://localhost:8081 · API http://localhost:8080 · Swagger http://localhost:8080/swagger-ui
   ⚠️ Hoy falla: la home muestra la pantalla de error (ver [T-011](docs/tasks/T-011-home-error-state.md)).
6. Apagar y borrar los datos:

```bash
docker compose --profile app down -v
```

### Opción B · Desarrollo (DB en contenedor; back y front locales con hot reload)

**Inicio rápido:** `./scripts/dev.sh` hace todos los pasos de abajo (copia los `.example` si faltan, levanta la DB, el back, el usuario demo y el front) y `Ctrl+C` corta todo. `./scripts/dev.sh --down` apaga la DB.

Paso a paso (cada comando en su bloque, desde la carpeta indicada):

**1. Clonar** (ver opción A, pasos 1–2) y copiar los `.example`.

**2. Fijar versiones** (opcional si ya tenés JDK 17+ y Node 22+). Desde `backend/`: `sdk env install` · desde `frontend/airbnb_clone/`: `nvm install`.

**3. Levantar la DB** (desde la raíz):

```bash
docker compose up -d --wait
```

**Resultado esperado:** `Container clonair-db-1 Healthy`.

**4. Levantar el back** (en una terminal nueva, desde `backend/`):

```bash
./mvnw spring-boot:run
```

**Resultado esperado:** el log termina en `Started BackApplication in … seconds`. Chequeo (desde otra terminal):

```bash
curl -s -w '\n%{http_code}\n' http://localhost:8080/actuator/health
```

→ `200` y `{"status":"UP"}` en `/actuator/health` (público; incluye la conexión a MySQL sin exponer detalles).

**5. Crear el usuario demo** (desde la raíz):

```bash
./scripts/seed-demo.sh
```

**Resultado esperado:** `✅ login demo OK`.

**6. Levantar el front** (en una terminal nueva, desde `frontend/airbnb_clone/`):

```bash
npm ci
```

```bash
npm run dev
```

**Resultado esperado:** `VITE … ready` y `Local: http://localhost:5173/`. Abrir esa URL.
⚠️ Hoy falla: la home muestra la pantalla de error ([T-011](docs/tasks/T-011-home-error-state.md)).

**7. Verificar todo** (desde la raíz):

```bash
./scripts/verify.sh
```

**Resultado esperado hoy:** `server: ✅ build ✅ tests (31) ✅ cobertura` y `client: ❌ lint (93 problems) ✅ build` → ⚠️ el lint falla hasta [T-005](docs/tasks/T-005-frontend-lint.md).

**8. Apagar y limpiar:** `Ctrl+C` en las terminales del back y del front, y desde la raíz:

```bash
docker compose down -v
```

### Windows

- Usar **Git Bash o WSL** para los `.sh`. Antes de clonar: `git config --global core.autocrlf input` (los scripts necesitan finales de línea LF).
- En `cmd`/PowerShell el back se levanta con `mvnw.cmd spring-boot:run` y los archivos se copian con `copy backend\src\main\resources\env.properties.example backend\src\main\resources\env.properties`.
- Docker Desktop con WSL2.

### Problemas frecuentes

| Síntoma | Causa | Solución |
|---|---|---|
| `Failed to parse the host:port pair 'localhost:${DB_PORT}'` | `env.properties` no está en el classpath | copiarlo a `backend/src/main/resources/env.properties` (no a `backend/`) |
| `Access denied for user 'clonair'` | `env.properties` viejo con otra contraseña, o volumen creado con otras credenciales | volver a copiar el `.example` y `docker compose down -v` |
| `Bind for 0.0.0.0:3306 failed: port is already allocated` | hay otro MySQL local | apagarlo, o `DB_PORT=3307 docker compose up -d --wait` y `DB_PORT=3307` en `env.properties` |
| `./mvnw: Permission denied` | checkout viejo sin bit de ejecución | `chmod +x backend/mvnw` o `sh mvnw …` |
| `invalid target release: 17` | JDK menor a 17 | instalar JDK 17 o 21 (ver Prerrequisitos) |
| El front muestra "Error code: 404" en la home | la home no maneja estados de carga/error | pendiente T-011 |
| API devuelve 400 / 401 / 403 / 404 / 409 | errores con `application/problem+json` | 400: JSON/valor inválido; 401: sesión o credenciales inválidas; 403: sin permiso; 404: recurso inexistente; 409: username duplicado. Leer `detail`; los errores internos devuelven 500 sin detalles sensibles |
| `seed-demo.sh` → `register → 000` | el back todavía no arrancó | esperar el `200` del paso 4 |

_Verificado el 2026-10-10 en Ubuntu 24.04 con Java 21.0.12 (y 17.0.20 en contenedor), Node 22.22 (y 24 en contenedor), Docker 29.8.2 y Compose v5.6._

## Tests y calidad

`./scripts/verify.sh` es el gate único (build + lint + tests de ambas partes; `--server` o `--client` para una sola). Estrategia, cobertura y cómo correr un solo test: [`docs/TESTING.md`](docs/TESTING.md). El back tiene 118 tests; los de integración usan MySQL 8.4 efímera vía Testcontainers y puerto HTTP aleatorio. Para verificar el back basta Docker corriendo: `./scripts/verify.sh --server` (o `cd backend && ./mvnw -B clean verify`), sin `env.properties` ni DB de compose. JaCoCo genera el reporte en `backend/target/site/jacoco/index.html` y comprueba un umbral de líneas del 40 % en `verify` (cobertura medida tras T-008: 82,54 %).

El front tiene 6 tests con Vitest, React Testing Library y MSW desde T-009 (PR #13 mergeado). Desde `frontend/airbnb_clone/`: `npm test` corre la suite; `npm run test:coverage` mide cobertura v8 y comprueba el umbral de líneas del 1 % (base 1,65 %). No necesita API ni Docker. El gate `--client` ejecuta lint/build/tests; la cobertura del front se comprueba por separado. E2E sigue pendiente de T-013.

## Cómo se trabaja

Flujo de ramas, commits y PRs en [`CONTRIBUTING.md`](CONTRIBUTING.md). El repo está preparado para desarrollo asistido por agentes ([`AGENTS.md`](AGENTS.md)): 🤖 **Junie Lite (Free)** mediante Air en IntelliJ toma tareas chicas y bien especificadas, 🧠 un **modelo de frontera** (p. ej. **GPT-6.1 Sol / Claude Opus 5.5**) lo complejo, y 👤 el dueño decide y mergea. La tarjeta al inicio de [`docs/STATUS.md`](docs/STATUS.md) indica la **próxima acción, tarea, ejecutor y prompt**; [`docs/BACKLOG.md`](docs/BACKLOG.md) muestra la cola y los cambios entre frontera y Lite. Para compactar y retomar la misma tarea, seguí [`AGENTS.md` → Gestión de contexto](AGENTS.md#5-gestión-de-contexto).

## Estado y roadmap

[`docs/STATUS.md`](docs/STATUS.md) (dónde estamos y qué sigue) · [`docs/BACKLOG.md`](docs/BACKLOG.md) (tablero) · [`docs/ROADMAP.md`](docs/ROADMAP.md) (fases) · [auditoría inicial](docs/audits/2026-10-10-initial.md).

## Capturas y diagramas

<img src="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/assets/109118183/b8602f63-7b27-4697-ab6d-c07477f5e91d" alt="Listado de propiedades" width="675">

<img src="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/assets/109118183/c7588f6e-0940-49a9-b3e3-6aba0d2144e9" alt="Detalle de una propiedad" width="675">

<img src="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/assets/109118183/f1cd07c0-1c35-4cc1-9829-545179a942f3" alt="Formulario de carga de propiedad" width="675">

**Diagrama entidad-relación**

<img src="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/assets/109118183/fcf1236a-bc8b-42c3-9ddf-b5189bab03d1" alt="Diagrama entidad-relación" width="528">

**Swagger (endpoints)**

<img src="https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/assets/109118183/672b7335-90c5-4f6d-bfeb-e72175e23f88" alt="Endpoints en Swagger" width="680">

<!-- Capturas nuevas (desktop y mobile) en docs/images/ cuando el front vuelva a funcionar en local (Backlog P3). -->

## Equipo

| Persona | Rol | LinkedIn |
|---|---|---|
| Mateo Jeremías Pinto | Team Lead | [perfil](https://www.linkedin.com/in/mateo-jeremias-pg/) |
| Tomas Augusto Bran | Frontend | [perfil](https://www.linkedin.com/in/tomas-augusto-bran-70745616a/) |
| Leandro Rossi | Frontend | [perfil](https://www.linkedin.com/in/leandro-rossi-a964a020/) |
| Brayan Sanchez | Frontend | [perfil](https://www.linkedin.com/in/brayan-sanchez-2355b1199/) |
| Francisco Ziegler | Backend | [perfil](https://www.linkedin.com/in/franciscoziegler/) |
| Matias Nicolas Acevedo | Backend · mantenedor de la retoma | [perfil](https://www.linkedin.com/in/matias-nicolas-acevedo/) |

Gracias a [No Country](https://www.nocountry.tech/) por la simulación.

## Licencia

Todavía sin licencia (decisión pendiente del dueño: ver [`docs/STATUS.md`](docs/STATUS.md)). Hasta entonces, todos los derechos reservados a sus autores.
