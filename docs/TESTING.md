# Estrategia de testing

Estable. Línea de base (2026-10-10): back **1 test, 16,8 % líneas / 1,0 % ramas**; front **sin tests**; E2E **no existe**. Gate: `./scripts/verify.sh`.

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

Sin H2: la DB real es MySQL y los tests usan la misma vía Testcontainers ([ADR 0001](adr/0001-testcontainers-mysql.md)). Infra pendiente: back T-002, front T-009, E2E T-013.

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
| Unitario back | `JwtServiceTest` (lo crea T-001) |
| `@WebMvcTest` | el que crea T-002 (`AuthControllerTest`) |
| Integración back | el que crea T-002 (`AuthFlowIntegrationTest`) |
| Componente front | el que crea T-009 |
| E2E | el que crea T-013 |

Mientras una referencia no exista, la tarea que la necesita está bloqueada por la tarea de infra.

**Correr un solo test**

- Back (desde `backend/`, con la DB de compose arriba): `./mvnw -q test -Dtest=BackApplicationTests` · método: `./mvnw -q test -Dtest='BackApplicationTests#contextLoads'`.
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
| Back (línea) | 16,8 % | 16 % (lo fija T-002 con JaCoCo) | ≥ 70–80 % en servicios/dominio |
| Front (línea) | — | el valor medido en T-009 | ≥ 70 % en lógica (services, hooks) |

Medir hoy la base del back sin tocar el `pom.xml` (desde `backend/`):

```bash
./mvnw org.jacoco:jacoco-maven-plugin:0.8.14:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.14:report
```

Reporte en `backend/target/site/jacoco/index.html`.

## 6. Opcional

Mutation testing puntual en auditorías: PIT (`pitest-maven`) sobre `property/` y `security/`; Stryker en `services/`.

## Roadmap de testing (antes de cualquier migración)

1. 🧠 T-002 · infra back: JaCoCo con umbral, Testcontainers, sin config manual, primer test de integración y de `@WebMvcTest`.
2. 🧠 T-009 · infra front: Vitest + RTL + MSW + cobertura (con Vite ya existente, no hace falta migrar de CRA).
3. 🧠 T-013 · infra E2E: Playwright + `scripts/e2e.sh` + job de CI.
4. 🤖 Caracterización: T-007 (propiedades) y una tarea por flujo crítico sin test (ver auditoría → Testing).
