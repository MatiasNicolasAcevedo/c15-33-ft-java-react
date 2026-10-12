# Estrategia de testing

Estable. Base de auditoría (2026-10-10): back **1 test, 16,8 % líneas / 1,0 % ramas**. Tras T-008: **118 tests, 82,54 % líneas (520/630), umbral 40 %**; front **sin tests**; E2E **no existe**. Gate: `./scripts/verify.sh`.

## 1. Pirámide y herramientas

| Capa | Herramientas | Qué cubre |
|---|---|---|
| Back · unitarios | JUnit 5 (Jupiter, vía `spring-boot-starter-test`), AssertJ, Mockito con mesura | servicios y lógica de dominio (`*ServiceImp`, `JwtService`, mapeos) |
| Back · slice web | `@WebMvcTest` + `spring-security-test` (`@WithMockUser`, `jwt()`) | controllers: validación, códigos HTTP, formato de error, permisos |
| Back · slice JPA | `@DataJpaTest` + Testcontainers | solo queries propias de los repositorios |
| Back · integración | `@SpringBootTest(webEnvironment = RANDOM_PORT)` + **Testcontainers MySQL 8.4** | flujos completos (registro → login → alta de propiedad) |
| Back · cobertura | **JaCoCo** con umbral en `verify` | — |
| Front · componentes | **Vitest** + **React Testing Library** (consultas por rol/label) + `@testing-library/user-event` | comportamiento visible, estados loading/error/empty |
| Front · API | **MSW** (mocks basados en `/v3/api-docs`) | `services/apiRequests.jsx` y pantallas que llaman a la API |
| Front · cobertura | Vitest coverage **v8** | — |
| E2E | **Playwright** (desktop 1280 + mobile 375), screenshots y traces en fallos | 3–5 smoke de flujos críticos contra `docker compose --profile app` |

Sin H2: la DB real es MySQL y los tests usan la misma vía Testcontainers ([ADR 0001](adr/0001-testcontainers-mysql.md)). Infra del back implementada en T-002; pendientes: front T-009 y E2E T-013.

## 2. Qué testear y qué no

- **Sí:** reglas de negocio (quién puede crear/editar/borrar), validaciones, permisos por rol y por dueño, contratos de API (status + cuerpo), mapeos entidad → DTO, estados de UI (cargando, error, vacío), formularios.
- **No:** getters/setters de Lombok, records, enums, código generado, el framework (que Spring cargue un bean "porque sí").

## 3. Convenciones

- Ubicación: `backend/src/test/java/` espejando el paquete (`com.clonair.back.property.PropertyServiceImpTest`). Front: al lado del componente (`Componente.test.jsx`).
- Nombres: `metodo_condicion_resultado` en Java (`save_whenUserIsNotOwner_throws`); `it('muestra un error cuando la API falla')` en el front.
- Estructura **Arrange / Act / Assert** con una línea en blanco entre bloques.
- Datos de prueba con builders/factories (`TestData.owner()`), nunca datos compartidos mutables.
- **Archivo de referencia por tipo** (copiá su estilo):

| Tipo | Referencia |
|---|---|
| Unitario back | [UserServiceImpTest](../backend/src/test/java/com/clonair/back/user/UserServiceImpTest.java) (Mockito puro); [JwtServiceTest](../backend/src/test/java/com/clonair/back/security/jwt/JwtServiceTest.java) para JWT |
| `@WebMvcTest` | [AuthControllerTest](../backend/src/test/java/com/clonair/back/security/auth/AuthControllerTest.java) (filtros y SecurityConfig reales, servicios mockeados, sin DB) |
| Integración back | [AuthFlowIntegrationTest](../backend/src/test/java/com/clonair/back/security/auth/AuthFlowIntegrationTest.java) (HTTP real, puerto aleatorio y MySQL 8.4) |
| Componente front | el que crea T-009 |
| E2E | el que crea T-013 |

T-008 agrega `ErrorHandlingIntegrationTest` (HTTP real: errores, JWT inválidos, health y mutaciones 201/204), `GlobalExceptionHandlerTest` (dominio, MVC y ausencia de detalles internos), `ProblemErrorControllerTest` (despacho del contenedor) y `AuthServiceTest` (duplicados y carrera de registro). El cliente JDK permite leer respuestas POST 401 con `WWW-Authenticate`: el cliente `HttpURLConnection` intenta reintentar en streaming y pierde el cuerpo.

T-003 agrega [AuthorizationIntegrationTest](../backend/src/test/java/com/clonair/back/security/AuthorizationIntegrationTest.java) para roles, catálogo público y CORS, y [CorsConfigTest](../backend/src/test/java/com/clonair/back/security/config/CorsConfigTest.java) para denegar configuración vacía y comodines. Sus preflight usan `JdkClientHttpRequestFactory`: `HttpURLConnection` descarta el encabezado `Origin`, lo que impediría probar CORS real.

La configuración de Spring en tests usa exclusivamente `application-test.properties` vía `spring.config.location`, sin cargar `application.properties` ni importar `env.properties`. No necesita `APP_PORT`, `JWT_SECRET` ni variables de DB externas. [TestcontainersConfiguration](../backend/src/test/java/com/clonair/back/TestcontainersConfiguration.java) declara la MySQL como bean con `@ServiceConnection`: Spring administra su ciclo de vida y comparte el contexto entre las clases de integración. El recurso `src/test/resources/mysql-test-conf/my.cnf` reemplaza la configuración antigua de Testcontainers 1.19.3 para ser compatible con MySQL 8.4 (sin `skip-host-cache`). La DB es efímera, sin el volumen ni los datos de compose. Cada caso crea su propio usuario con un nombre distinto; el orden no importa.

Mientras una referencia no exista, la tarea que la necesita está bloqueada por la tarea de infra.

**Correr un solo test**

- Back (desde `backend/`, Docker corriendo para integración; sin DB de compose ni configuración manual): `./mvnw -q test -Dtest=BackApplicationTests` · método: `./mvnw -q test -Dtest='BackApplicationTests#contextLoads'`.
- Front (desde T-009): `npx vitest run src/components/…/Archivo.test.jsx` · por nombre: `npx vitest run -t "texto del it"`.
- E2E (desde T-013): `npx playwright test tests/e2e/archivo.spec.ts`.

## 4. Reglas para todas las tareas

1. Los tests van **en el mismo PR** que el código.
2. **Bugfix = primero un test que falla** y reproduce el bug; después el fix.
3. **Caracterización antes de refactorizar o migrar** versiones.
4. **Prohibido** borrar, deshabilitar (`@Disabled`, `.skip`), debilitar asserts o bajar umbrales.
5. Deterministas: sin depender del orden, la hora (inyectar `Clock`), la red ni datos compartidos.

## 5. Cobertura como trinquete

Medir la base, fijar el umbral en ese valor redondeado hacia abajo y **no bajarlo nunca**; cada auditoría lo sube.

| Parte | Base | Umbral inicial | Meta |
|---|---|---|---|
| Back (línea) | 82,54 % (520/630, T-008; base T-007: 76,20 %) | **40 %** (JaCoCo en `verify`) | ≥ 70–80 % en servicios/dominio |
| Front (línea) | — | el valor medido en T-009 | ≥ 70 % en lógica (services, hooks) |

Medir y comprobar la cobertura del back (desde `backend/`):

```bash
./mvnw -B clean verify
```

Reporte en `backend/target/site/jacoco/index.html`. JaCoCo 0.8.14 ejecuta `prepare-agent`, `report` y `check` (LINE / COVEREDRATIO / BUNDLE, sin exclusiones) en el ciclo Maven. El gate distingue un fallo de cobertura de un fallo de tests.

## 6. Opcional

Mutation testing puntual en auditorías: PIT (`pitest-maven`) sobre `property/` y `security/`; Stryker en `services/`.

## Roadmap de testing (antes de cualquier migración)

1. ✅ T-002 · infra back: JaCoCo con umbral, Testcontainers, sin config manual, primer test de integración y de `@WebMvcTest`.
2. 🧠 T-009 · infra front: Vitest + RTL + MSW + cobertura (con Vite ya existente, no hace falta migrar de CRA).
3. 🧠 T-013 · infra E2E: Playwright + `scripts/e2e.sh` + job de CI.
4. 🤖 Caracterización: T-007 (propiedades) y una tarea por flujo crítico sin test (ver auditoría → Testing).
