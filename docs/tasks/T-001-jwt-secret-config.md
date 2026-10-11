# T-001 · Sacar el secreto y la expiración del JWT a configuración
- Estado: En revisión · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `fix/T-001-jwt-secret-config` (crearla desde `development` actualizado antes del primer commit) · Depende de: — · Spec/ADR: [0003](../adr/0003-jwt-stateless-auth.md)
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
🔴 S-1: el secreto de firma está hardcodeado en `backend/src/main/java/com/clonair/back/security/jwt/JwtService.java:28` (y en el historial): cualquiera forja tokens. 🟠 S-5: expiración de 1000 días (`:48`). S-6: `getUserIdFromToken` (`:143-150`) usa otra derivación de clave y nadie lo llama.

## Objetivo
El back firma con `JWT_SECRET` y expira según `JWT_EXPIRATION` (default 24 h); sin secreto válido no arranca.

## Alcance
- Incluye: `JwtService` con `@Value` (o `@ConfigurationProperties`), validación de largo ≥ 256 bits al iniciar, borrar `getUserIdFromToken`, variables nuevas en `application.properties`, `env.properties.example`, `docker-compose.yml`, `docs/SETUP.md`.
- **No incluye:** cambiar el algoritmo, migrar jjwt, reglas de autorización (T-003), errores 401 (T-008), rotar en producción (T-015, 👤).

## Leer antes
- `backend/src/main/java/com/clonair/back/security/jwt/JwtService.java` (completo, 151 líneas)
- `backend/src/main/resources/application.properties:1-6`
- `docs/SETUP.md` → tabla de variables · `CONTRIBUTING.md` §9

## Archivos previstos
- modificar: `JwtService.java`, `application.properties`, `env.properties.example`, `docker-compose.yml`, `docs/SETUP.md`, `README.md` (tabla de prod) · crear: `backend/src/test/java/com/clonair/back/security/jwt/JwtServiceTest.java`

## Tests
- Agregar `JwtServiceTest` (JUnit 5 + AssertJ, **sin Spring**, será la referencia de unitarios): token generado → `getUsernameFromToken` devuelve el usuario; `isTokenValid` true para el mismo usuario y false para otro; token firmado con otra clave → lanza `JwtException`; token expirado (expiración negativa o `Clock`) → inválido; secreto corto → falla al construir.

## Pasos sugeridos
1. `application.properties`: `jwt.secret=${JWT_SECRET}` y `jwt.expiration=${JWT_EXPIRATION:86400000}`.
2. Constructor de `JwtService` recibe secreto y expiración; decodifica Base64 una vez y valida `keyBytes.length >= 32`.
3. `.example`: `JWT_SECRET=` con un valor nuevo de `openssl rand -base64 32` (nunca el viejo) + comentario; `JWT_EXPIRATION=86400000`. Lo mismo en `services.server.environment` del compose.
4. Confirmar con `git grep` que el valor viejo (el de `JwtService.java:28` en `development`) no aparece en ningún archivo nuevo. No lo copies en docs, commits ni PR.

## Criterios de aceptación
- [x] `./scripts/verify.sh --server` pasa (incluye `JwtServiceTest`) y la cobertura no baja
- [x] `git grep -n "SECRET_KEY" -- backend` no devuelve nada y no queda ningún literal de clave en `JwtService.java`
- [x] Sin `JWT_SECRET` el back no arranca y el log lo dice claramente
- [x] `docker compose --profile app up -d --build --wait` + `./scripts/seed-demo.sh` → `✅ login demo OK`
- [x] Un token tiene `exp - iat` = 86400 s (decodificar el payload)

Evidencia (2026-10-10):
- Gate `--server`: ✅ build + 25 tests (24 unitarios de JWT y `contextLoads`), JDK 21. Gate completo: server OK y client build OK; persisten exactamente los 93 problemas de lint de la base (T-005), sin regresiones.
- JaCoCo 0.8.14 por CLI, sin cambiar el POM: líneas 131/549 = **23,86 %** (base 16,8 %); ramas **3,44 %** (base 1,0 %); líneas de `JwtService` **97,56 %**.
- `git grep` no encuentra `SECRET_KEY`; comparación automática contra el literal anterior de `development` no encuentra ese valor en los archivos versionados actuales, sin imprimirlo.
- Jar sin `JWT_SECRET`: exit 1 y `Could not resolve placeholder 'JWT_SECRET'`; no se leyó configuración real.
- Compose con build y wait: DB, server y client levantados; `seed-demo.sh` creó el demo y confirmó `✅ login demo OK`.
- Payload del login demo decodificado sin mostrar el token: subject `demo-owner@clonair.local`, `exp - iat = 86400` segundos.

## Notas de implementación
- Hecho: implementación y aceptación completas. Rama `fix/T-001-jwt-secret-config`; [PR #4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4), listo para revisión independiente. Base `47debed`; último commit de implementación `e57b35c` (cierre documental posterior).
- Línea de base: `./scripts/verify.sh` → server build + 1 test OK; client build OK, mismos 93 problemas de lint (T-005); JDK 21 / Node 24.19.0.
- Regresión reproducida y publicada en `c277f53`: duración obtenida `86400000000` ms frente a `86400000` ms esperados; test falló antes del fix.
- Implementado: constructor con clave Base64 validada y decodificada una vez, expiración configurable, HS256 conservado, eliminación del helper sin llamadas; `.example`, compose, README y SETUP actualizados. Unitarios `JwtServiceTest` OK (sin contexto Spring).
- Decisiones: Base64 estricto (`java.util.Base64`); `iat` y `exp` parten de un mismo instante. Los tokens expirados siguen lanzando `ExpiredJwtException`, sin cambiar el manejo de errores de T-008. Clave pública nueva generada con `openssl rand -base64 32` solo para local; no es una rotación de producción.
- Entorno Windows: el primer build de Docker falló con `./mvnw: not found` por CRLF. Se normalizó únicamente el checkout local de `backend/mvnw` a LF, sin diferencias de contenido ni cambios del wrapper en el PR; el segundo build pasó. La prevención permanente con `.gitattributes` queda en Backlog.
- Archivos modificados: `JwtService.java`, `JwtServiceTest.java`, `application.properties`, `env.properties.example`, `docker-compose.yml`, README, SETUP, STATUS, BACKLOG y esta tarea. No se leyó ni modificó configuración real ni de producción.
- Pendiente: revisión independiente con frontera y merge del dueño. Próxima acción: sesión nueva con «Leé `AGENTS.md` y seguí `docs/agents/reviewer.md` para el PR #4». El stack local queda levantado para probarlo.

## Resultado
- PR: [#4](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/4), base `development`.
- Qué cambió: firma HS256 con secreto obligatorio de al menos 256 bits decodificados, duración configurable con default 24 h, helper sin uso eliminado y configuración local/producción documentada.
- Tests agregados: 24 casos unitarios sin contexto Spring sobre subject, identidad, firma ajena, expiración, duración configurable, HS256 y secretos ausentes/cortos/malformados. Test de regresión publicado antes del fix.
- Pendientes: revisión y merge; T-015 (👤 rotar en producción); T-005 (lint preexistente); finales LF permanentes en Backlog.
