# T-002 · Montar la infraestructura de tests del back
- Estado: Lista · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `test/T-002-backend-test-infra` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-001 · Spec/ADR: [0001](../adr/0001-testcontainers-mysql.md)
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
🔴 T-1: 1 test sin asserts que necesita MySQL externa y `env.properties` (`BackApplicationTests.java`); cobertura 16,8 % sin umbral. Sin red no se puede migrar ni arreglar seguridad con confianza.

## Objetivo
`./mvnw verify` corre sin configuración manual (Testcontainers), mide cobertura con umbral y deja un test de referencia por tipo.

## Alcance
- Incluye: `jacoco-maven-plugin` (prepare-agent, report, check ≥ 0.16 líneas en BUNDLE), `spring-boot-testcontainers` + `org.testcontainers:mysql` + `junit-jupiter` (versiones del BOM de Boot), config de test (`src/test/resources/application-test.properties` o `@SpringBootTest(properties=…)`) con puerto aleatorio y `JWT_SECRET` de test, `BackApplicationTests` con Testcontainers, 3 tests de referencia, actualizar `docs/TESTING.md` §3 y `scripts/verify.sh` (quitar la dependencia de la DB de compose para el back si ya no hace falta).
- **No incluye:** arreglar bugs que los tests descubran (→ Backlog o tareas T-003/T-006/T-008), cambiar versiones de Boot/Java.

## Leer antes
- `docs/adr/0001-testcontainers-mysql.md` · `docs/TESTING.md` §1, §3, §5
- `backend/pom.xml` · `backend/src/main/resources/application.properties`
- `backend/src/main/java/com/clonair/back/security/config/SecurityConfig.java:32-55`

## Archivos previstos
- modificar: `backend/pom.xml`, `BackApplicationTests.java`, `scripts/verify.sh`, `docs/TESTING.md`, `AGENTS.md` (comandos) · crear: `TestcontainersConfiguration.java`, `security/auth/AuthControllerTest.java` (`@WebMvcTest`), `security/auth/AuthFlowIntegrationTest.java`, `user/UserServiceImpTest.java` (Mockito), `src/test/resources/application-test.properties`

## Tests
- `AuthFlowIntegrationTest` (`@SpringBootTest(RANDOM_PORT)` + MySQL 8.4): register OWNER → 200 + token; login correcto → 200; login con password incorrecta → no 2xx (hoy 403; T-008 lo pasa a 401: dejar el assert como `isNot2xx` con comentario).
- `AuthControllerTest` (`@WebMvcTest(AuthController.class)` + mocks de `AuthService`/`JwtService`, importando la config de seguridad necesaria): login delega en el servicio y devuelve el JSON esperado. Debe servir de molde para T-006.
- `UserServiceImpTest` (Mockito puro): `getOne` con id inexistente lanza; `getByUsername` mapea a `UserResponse`.

## Pasos sugeridos
1. JaCoCo 0.8.14 con `check` en la fase `verify`; reporte en `target/site/jacoco/`.
2. `@TestConfiguration` con `@Bean @ServiceConnection MySQLContainer<?>("mysql:8.4")`; anotar los tests de integración con `@Import`.
3. Resolver `server.port=${APP_PORT}` y `jwt.*` en tests sin variables de entorno.
4. Medir la nueva cobertura y fijar el umbral en ese valor redondeado hacia abajo (nunca menos de 0.16).

## Criterios de aceptación
- [ ] Sin `env.properties` y sin la DB de compose: `cd backend && ./mvnw -B clean verify` pasa (Docker corriendo)
- [ ] `./scripts/verify.sh --server` muestra `✅ tests (≥5)`
- [ ] Bajar el umbral a mano no hace falta: `target/site/jacoco/index.html` existe y `jacoco:check` corre en `verify`
- [ ] `docs/TESTING.md` §3 apunta a los 3 archivos de referencia reales y §5 tiene el umbral nuevo
- [ ] Ningún test borrado ni con `@Disabled`

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Cobertura: …% (umbral …) · Pendientes → …
