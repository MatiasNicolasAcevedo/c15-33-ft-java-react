# T-002 · Montar la infraestructura de tests del back
- Estado: Hecha (implementación terminada; revisión y merge pendientes) · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
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
- [x] Sin `env.properties` y sin la DB de compose: `cd backend && ./mvnw -B clean verify` pasa (Docker corriendo)
- [x] `./scripts/verify.sh --server` muestra `✅ tests (≥5)`
- [x] Bajar el umbral a mano no hace falta: `target/site/jacoco/index.html` existe y `jacoco:check` corre en `verify`
- [x] `docs/TESTING.md` §3 apunta a los 3 archivos de referencia reales y §5 tiene el umbral nuevo
- [x] Ningún test borrado ni con `@Disabled`

Evidencia de criterios: clean verify sin config/compose OK; `--server` con 31 tests OK; HTML presente y check JaCoCo ejecutado en verify, con prueba negativa al 100 %; referencias enlazadas en TESTING §3 y umbral 40 % en §5; `rg @Disabled` sin coincidencias, ningún método de test eliminado.

## Notas de implementación
- Rama: `test/T-002-backend-test-infra` desde `development` (`63e5bc2`); [PR #5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5) listo para revisión.
- Línea de base: server build + 25 tests OK; client build OK, mismos 93 problemas de lint (T-005). Sin PR abiertos.
- Último commit de implementación: `bff9422`, publicado en origin.
- Hecho: Testcontainers, tres referencias, JaCoCo con umbral 40 %, gate sin compose/config local y documentación completos. Todos los criterios comprobados. Pendiente: revisión independiente y merge del dueño. Próximo paso: sesión nueva con frontera, leer `docs/agents/reviewer.md` para el PR #5.
- Pruebas específicas: `./mvnw.cmd -B -q test -Dtest=UserServiceImpTest,AuthControllerTest` OK (3 casos, sin DB).
- Compatibilidad: primer intento de integración detenido tras confirmar en el log de MySQL `unknown option --skip-host-cache`. Testcontainers 1.19.3 (BOM de Boot 3.2.0) monta un my.cnf antiguo; se reemplaza solo para tests vía `withConfigurationOverride("mysql-test-conf")`, con `host_cache_size=0`. Sin cambiar versiones. Fuentes: [config de Testcontainers 1.19.3](https://raw.githubusercontent.com/testcontainers/testcontainers-java/1.19.3/modules/mysql/src/main/resources/mysql-default-conf/my.cnf), [opciones removidas en MySQL 8.4](https://dev.mysql.com/doc/refman/8.4/en/added-deprecated-removed.html), [override de my.cnf](https://java.testcontainers.org/modules/databases/mysql/#overriding-mysql-mycnf-settings).
- Entorno: Java 21.0.9, Docker 28.5.1, Node 24.19.0, Windows + Git Bash; sin `env.properties` en main/resources ni en backend. DB de compose detenida durante clean verify y gate completo para comprobar aislamiento; restaurada a running/healthy al terminar. No quedaron contenedores de tests.
- Cambios locales previos: cierre de T-001 en STATUS/BACKLOG conservado; archivos de T-001 y T-015 sin editar ni incluir en commits de T-002.

## Resultado
- PR: [#5](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/5), listo para revisión independiente. Cierre: 2026-10-11.
- Qué cambió: MySQL 8.4 aislada con @ServiceConnection y configuración fija de tests, referencias Mockito/MVC/HTTP, JaCoCo 0.8.14 sin exclusiones (LINE / COVEREDRATIO / BUNDLE), gate sin config manual ni DB de compose.
- Tests agregados: 6 casos nuevos (2 unitarios, 1 MVC, 3 integración); `contextLoads` conserva su nombre y ahora comprueba una consulta SQL. Los 24 tests de JWT se conservan. Total: 31, cero fallos/errores/skips.
- Cobertura: 40,80 % (224/549 líneas), umbral 40 % redondeado hacia abajo; reporte HTML generado. Prueba negativa con POM temporal al 100 % rechazada por JaCoCo, sin modificar el POM real.
- `./scripts/verify.sh --server` OK (2026-10-11): build + 31 tests + cobertura; exit 0.
- `cd backend && ./mvnw -B clean verify` OK sin env.properties ni DB de compose. Gate completo: server build/tests/cobertura OK; client build OK, mismos 93 problemas de lint de la línea de base (T-005).
- Nota Windows: Surefire avisó que terminó el fork tras 30 s de espera al cerrar la JVM; Maven devolvió BUILD SUCCESS y los contenedores de tests se limpiaron. Investigar la demora queda en Backlog.
- Pendientes: revisión independiente con frontera y merge del dueño; T-005 conserva el fallo conocido del gate completo. Sin cambios de producción, seguridad, esquema persistente o versiones mayores.
