# T-001 · Sacar el secreto y la expiración del JWT a configuración
- Estado: En curso · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🧠 modelo de frontera
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
- [ ] `./scripts/verify.sh --server` pasa (incluye `JwtServiceTest`) y la cobertura no baja
- [ ] `git grep -n "SECRET_KEY" -- backend` no devuelve nada y no queda ningún literal de clave en `JwtService.java`
- [ ] Sin `JWT_SECRET` el back no arranca y el log lo dice claramente
- [ ] `docker compose --profile app up -d --build --wait` + `./scripts/seed-demo.sh` → `✅ login demo OK`
- [ ] Un token tiene `exp - iat` = 86400 s (decodificar el payload)

## Notas de implementación
- Hecho: protocolo leído; development actualizado (`47debed`); sin PR abiertos ni rama remota de T-001. Rama `fix/T-001-jwt-secret-config`; PR pendiente.
- Línea de base: `./scripts/verify.sh` → server build + 1 test OK; client build OK, mismos 93 problemas de lint (T-005); JDK 21 / Node 24.19.0.
- Pendiente: test de regresión, configuración y validación del secreto, unitarios y verificaciones de aceptación. Próximo paso: abrir el PR draft y reproducir la expiración incorrecta con `JwtServiceTest`.

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Pendientes → T-015 (👤 rotar en prod)
