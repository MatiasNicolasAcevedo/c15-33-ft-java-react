# Estrategia de testing

Estable. Base de auditoría (2026-10-10): back **1 test, 16,8 % líneas / 1,0 % ramas**. Tras T-008: **118 tests, 82,54 % líneas (520/630), umbral 40 %**; front T-009: **6 tests, 1,65 % líneas (54/3254), umbral 1 %**; E2E **no existe**. Gate: `./scripts/verify.sh`.

La revisión independiente de T-008 sobre `4eb7e2f` reprodujo el gate con 118 tests y midió 82,59 % (517/626) en su ejecución; PropertyServiceImp conservó 95,07 % (135/142). Se conserva la medición de implementación como referencia y ambas evidencias en la tarea, sin atribuirles el mismo conteo de líneas.

T-009 fue aprobada en revisión independiente sobre `e5de840` y mergeada en #13 (`dc27563`). Se reprodujeron los 6 tests, cobertura 1,65 %, umbral 1 %, rechazo al exigir 100 %, archivo individual y ambos gates. Próxima caracterización de UI: primero T-012 alinea imágenes y actualiza los mocks reales; después T-011 usa ese contrato para probar una home con catálogo no vacío. No agregar images al fixture de la API para ocultar el desalineamiento actual con imageUrls.

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

Sin H2: la DB real es MySQL y los tests usan la misma vía Testcontainers ([ADR 0001](adr/0001-testcontainers-mysql.md)). Infra del back implementada en T-002, front en T-009; pendiente E2E T-013.

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
| Componente front | [HostCardItem.test.jsx](../frontend/airbnb_clone/src/components/shared/HostCardItem/HostCardItem.test.jsx) (RTL: título, país, precio, primera imagen y cambio de props) |
| API front | [apiRequests.test.js](../frontend/airbnb_clone/src/services/apiRequests.test.js) (MSW: catálogo, vacío, HTTP 500 y error de red) |
| E2E | el que crea T-013 |

T-008 agrega `ErrorHandlingIntegrationTest` (HTTP real: errores, JWT inválidos, health y mutaciones 201/204), `GlobalExceptionHandlerTest` (dominio, MVC y ausencia de detalles internos), `ProblemErrorControllerTest` (despacho del contenedor) y `AuthServiceTest` (duplicados y carrera de registro). El cliente JDK permite leer respuestas POST 401 con `WWW-Authenticate`: el cliente `HttpURLConnection` intenta reintentar en streaming y pierde el cuerpo.

T-003 agrega [AuthorizationIntegrationTest](../backend/src/test/java/com/clonair/back/security/AuthorizationIntegrationTest.java) para roles, catálogo público y CORS, y [CorsConfigTest](../backend/src/test/java/com/clonair/back/security/config/CorsConfigTest.java) para denegar configuración vacía y comodines. Sus preflight usan `JdkClientHttpRequestFactory`: `HttpURLConnection` descarta el encabezado `Origin`, lo que impediría probar CORS real.

La configuración de Spring en tests usa exclusivamente `application-test.properties` vía `spring.config.location`, sin cargar `application.properties` ni importar `env.properties`. No necesita `APP_PORT`, `JWT_SECRET` ni variables de DB externas. [TestcontainersConfiguration](../backend/src/test/java/com/clonair/back/TestcontainersConfiguration.java) declara la MySQL como bean con `@ServiceConnection`: Spring administra su ciclo de vida y comparte el contexto entre las clases de integración. El recurso `src/test/resources/mysql-test-conf/my.cnf` reemplaza la configuración antigua de Testcontainers 1.19.3 para ser compatible con MySQL 8.4 (sin `skip-host-cache`). La DB es efímera, sin el volumen ni los datos de compose. Cada caso crea su propio usuario con un nombre distinto; el orden no importa.

Mientras una referencia no exista, la tarea que la necesita está bloqueada por la tarea de infra.

**Correr un solo test**

- Back (desde `backend/`, Docker corriendo para integración; sin DB de compose ni configuración manual): `./mvnw -q test -Dtest=BackApplicationTests` · método: `./mvnw -q test -Dtest='BackApplicationTests#contextLoads'`.
- Front (desde `frontend/airbnb_clone/`): `npx vitest run src/components/shared/HostCardItem/HostCardItem.test.jsx` · por nombre: `npx vitest run -t "texto del it"`.
- E2E (desde T-013): `npx playwright test tests/e2e/archivo.spec.ts`.

**Infra del front (T-009)**

`npm test` ejecuta `vitest run`, sin watch; `npm run test:coverage` agrega cobertura
v8 y verifica el umbral. `verify.sh --client` ya detecta el script `test` y ejecuta
la suite después de lint/build; la medición de cobertura se corre por separado.

Vitest y `@vitest/coverage-v8` están fijados en **3.2.7** (ambos iguales, compatibles
con Vite 5), jsdom **26.1.0**, RTL **16.3.0** con su peer `@testing-library/dom`
**10.4.2**, jest-dom **6.9.1**, user-event **14.6.8** y MSW **2.15.0**. Se conservan
Vite **5.0.2** y React/React DOM **18.2.0** del lockfile. Fuentes:
[configuración de Vitest 3](https://v3.vitest.dev/config/),
[RTL](https://testing-library.com/docs/react-testing-library/intro/) y
[MSW en Node](https://mswjs.io/api/setup-server/).

El [setup](../frontend/airbnb_clone/src/test/setup.js) carga jest-dom para Vitest,
usa jsdom y abre/cierra el servidor MSW. Después de cada caso limpia el DOM y
ambos storages y restablece los handlers; `restoreMocks` restaura los spies.
Importá `describe`, `it`, `expect` y `vi` desde `vitest`: no se habilitan globals de
test en ESLint. Para interacciones, usá `userEvent.setup()` y esperá sus acciones
con `await`; el componente puro de referencia no tiene controles interactivos.

Los [handlers](../frontend/airbnb_clone/src/test/msw/handlers.js) siguen
`GET /v1/api/property` → `PropertyResponse[]` y `Location` de `/v3/api-docs`,
obtenido el 2026-10-13 con configuración de tests y MySQL efímera. `Location.id`
es string, las categorías son enums y la API devuelve `imageUrls`. La referencia
pura conserva la prop `images` de HostCardItem: la alineación corresponde a T-012.
El caso 500 usa `application/problem+json` y conserva `undefined` como resultado
actual del cliente; los estados de error de la home corresponden a T-011.

`test.env` fija `VITE_API_URL` a `http://localhost:8080` para que los tests sean
independientes del entorno local. MSW usa `onUnhandledRequest: 'error'`: ningún
test necesita API, DB ni Docker. Para variar una respuesta en un caso, usá
`server.use(http.get(...))`; el setup restablece los handlers al terminar.

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
| Front (línea) | **1,65 % (54/3254, T-009)** | **1 %** (v8 en `npm run test:coverage`) | ≥ 70 % en lógica (services, hooks) |

Medir y comprobar la cobertura del back (desde `backend/`):

```bash
./mvnw -B clean verify
```

Reporte en `backend/target/site/jacoco/index.html`. JaCoCo 0.8.14 ejecuta `prepare-agent`, `report` y `check` (LINE / COVEREDRATIO / BUNDLE, sin exclusiones) en el ciclo Maven. El gate distingue un fallo de cobertura de un fallo de tests.

Medir el front desde `frontend/airbnb_clone/`: `npm run test:coverage`.
Reportes ignorados por Git y ESLint en `coverage/index.html` y
`coverage/coverage-summary.json`. `coverage.include` abarca **todo** `src/**/*.js`
y `src/**/*.jsx`, incluidos archivos sin tests; solo excluye tests y `src/test/`.
La base pequeña refleja la SPA todavía sin caracterizar: HostCardItem tiene
100 % (20/20) y `apiRequests.jsx` 11,34 % (32/282), con getAllProperties cubierto.
Ramas globales: 7,59 %; funciones: 2,27 %. El umbral global de líneas es
`floor(1,65) = 1`; no hay umbrales iniciales de ramas, funciones ni statements.
Se comprobó el rechazo con `npx vitest run --coverage --coverage.thresholds.lines=100`
(exit 1), sin modificar el umbral guardado, y luego la cobertura volvió a pasar.

## 6. Opcional

Mutation testing puntual en auditorías: PIT (`pitest-maven`) sobre `property/` y `security/`; Stryker en `services/`.

## Roadmap de testing (antes de cualquier migración)

1. ✅ T-002 · infra back: JaCoCo con umbral, Testcontainers, sin config manual, primer test de integración y de `@WebMvcTest`.
2. ✅ T-009 · infra front: Vitest + RTL + MSW + cobertura v8; sin migrar Vite ni React.
3. 🧠 T-013 · infra E2E: Playwright + `scripts/e2e.sh` + job de CI.
4. 🤖 Caracterización: T-007 (propiedades) y una tarea por flujo crítico sin test (ver auditoría → Testing).
