# Back · Clonair API (Spring Boot)

API REST del clon de Airbnb: auth con JWT, usuarios, propiedades e imágenes. Para levantar **todo** el proyecto, ver el [README raíz](../README.md).

## Configurar y correr solo el back

Desde la raíz del repo, una vez:

```bash
cp backend/src/main/resources/env.properties.example backend/src/main/resources/env.properties
```

```bash
docker compose up -d --wait
```

**Resultado esperado:** `Container clonair-db-1 Healthy`.

Desde `backend/`:

```bash
./mvnw spring-boot:run
```

**Resultado esperado:** `Started BackApplication in … seconds`; `curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/v3/api-docs` → `200`. Swagger UI: http://localhost:8080/swagger-ui.

> `env.properties` se lee desde el **classpath** (`src/main/resources/`), porque `application.properties:1` lo importa con ruta relativa. Alternativa: exportar las mismas variables en el entorno.

## Comandos

| Qué | Comando (desde `backend/`) | Notas |
|---|---|---|
| Build + tests | `./mvnw -B clean verify` | necesita la DB (compose) y la config |
| Solo tests | `./mvnw -q test` | ~20 s |
| Un test (clase) | `./mvnw -q test -Dtest=BackApplicationTests` | |
| Un test (método) | `./mvnw -q test -Dtest='BackApplicationTests#contextLoads'` | |
| Correr | `./mvnw spring-boot:run` | puerto `APP_PORT` (8080) |
| Imagen Docker local | `docker compose --profile app build server` (desde la raíz) | `backend/Dockerfile`, tests salteados en la imagen |
| Gate del repo | `../scripts/verify.sh --server` | levanta la DB si hace falta |

Windows: `mvnw.cmd` en lugar de `./mvnw`.

## Estructura

```
src/main/java/com/clonair/back/
  BackApplication.java        # main + bean OpenAPI
  security/                   # auth/ (login, register), config/ (Security, CORS, beans), jwt/ (filtro y servicio), request/, utils/
  user/                       # entidad User (UserDetails), controller, service, DTOs (records)
  property/                   # entidad Property + enums (Category, SubCategory, ServiceType), controller, service, DTOs
  image/                      # entidad Image (LONGBLOB), controller, service
  location/                   # entidad Location
src/main/resources/
  application.properties      # config común (importa env.properties)
  env.properties.example      # config local de ejemplo
src/test/java/…/BackApplicationTests.java   # único test hoy
```

Convenciones y límites para agentes: [`AGENTS.md`](AGENTS.md).
