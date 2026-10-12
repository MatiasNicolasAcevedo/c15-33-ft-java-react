# backend/AGENTS.md

Solo lo específico del back. Lo general está en el [AGENTS.md raíz](../AGENTS.md).

## Comandos (desde `backend/`)

- Tests (Docker corriendo para integración; sin DB de compose ni `env.properties`): `./mvnw -q test` · uno: `./mvnw -q test -Dtest='Clase#metodo'` · gate: `../scripts/verify.sh --server`.
- Correr: `./mvnw spring-boot:run` → `curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/actuator/health` = 200.
- Config: `src/main/resources/env.properties` (copia del `.example`). No la leas ni la commitees.

## Convenciones actuales

- Paquete por feature bajo `com.clonair.back`: `user/`, `property/`, `image/`, `location/`, `security/{auth,config,jwt,request,utils}`.
- Controller (`@RestController`, `@RequestMapping("v1/api/<recurso>")`, `@RequiredArgsConstructor`) → interfaz `XService` → `XServiceImp` (`@Service`) → `XRepository extends JpaRepository<X, String>`.
- IDs `String` UUID (`@GenericGenerator(strategy = "uuid2")`). Enums como `@Enumerated(EnumType.STRING)`.
- DTOs de salida y entrada como `record` (`PropertyResponse`, `UserResponse`, `PropertyRequest`); los de auth son clases Lombok `@Data @Builder`.
- Indentación 4 espacios (tabs solo en `pom.xml` y archivos generados por Initializr). Comentarios en español.
- Referencias: controller `property/PropertyController.java` · servicio `property/PropertyServiceImp.java` · tests: ver `docs/TESTING.md` §3.

## Cuidado

- Errores: usá `common/error/{NotFoundException,ForbiddenException,ConflictException,BadRequestException}`; el advice devuelve ProblemDetail. Autenticación → 401; permisos → 403. No agregues try/catch que cambien el status o el formato en controllers.
- `security/**`, `SecurityConfig`, `JwtService`, `CorsConfig` → solo tareas 🧠.
- `@Data` en entidades con relaciones bidireccionales (`User` ↔ `Property` ↔ `Image`): no uses `equals/hashCode/toString` de entidades en tests ni logs (recursión).
- Archivos largos: `PropertyServiceImp.java` (276 líneas), `ImageServiceImp.java` (219): leé por rangos.
