# 0003 · Mantener autenticación JWT stateless

- Estado: Aceptado · Fecha: 2026-10-10 · Decide: 🧠 (auditoría T-000)

## Contexto
Auth con JWT HS256 firmado con un secreto hardcodeado (`JwtService.java:28`), expiración de 1000 días (`:48`), sin reglas por rol en `SecurityConfig` y token en `localStorage` del front.

## Decisión
Se mantiene JWT stateless (el front y Swagger ya lo usan), pero:
- secreto y expiración por configuración (`JWT_SECRET`, `JWT_EXPIRATION`), sin default inseguro en prod (T-001);
- reglas de autorización declarativas en `SecurityConfig` (roles y dueño) y registro limitado a USER/OWNER (T-003);
- errores 401/403 correctos con `ProblemDetail` (T-008).

## Alternativas
- Sesiones con cookie: más seguro contra XSS, pero cambia front y back a la vez. Se reevalúa en P2 (cookie `HttpOnly` para el token: S-8).
- OAuth2 Resource Server de Spring con `NimbusJwtDecoder`: buena opción al migrar a Boot 4 (P1), reemplazando el filtro propio.

## Consecuencias
- Rotar el secreto invalida todos los tokens emitidos (aceptable: no hay usuarios reales conocidos).
