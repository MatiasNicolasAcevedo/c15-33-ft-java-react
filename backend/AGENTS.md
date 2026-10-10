# backend/AGENTS.md

Solo lo específico del back. Lo general está en el [AGENTS.md raíz](../AGENTS.md).

## Comandos (desde `backend/`, con `docker compose up -d --wait` hecho en la raíz)

- Tests: `./mvnw -q test` · uno: `./mvnw -q test -Dtest='Clase#metodo'` · gate: `../scripts/verify.sh --server`.
- Correr: `./mvnw spring-boot:run` → `curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/v3/api-docs` = 200.
- Config: `src/main/resources/env.properties` (copia del `.example`). No la leas ni la commitees.

## Convenciones actuales

- Paquete por feature bajo `com.clonair.back`: `user/`, `property/`, `image/`, `location/`, `security/{auth,config,jwt,request,utils}`.
- Controller (`@RestController`, `@RequestMapping("v1/api/<recurso>")`, `@RequiredArgsConstructor`) → interfaz `XService` → `XServiceImp` (`@Service`) → `XRepository extends JpaRepository<X, String>`.
- IDs `String` UUID (`@GenericGenerator(strategy = "uuid2")`). Enums como `@Enumerated(EnumType.STRING)`.
- DTOs de salida y entrada como `record` (`PropertyResponse`, `UserResponse`, `PropertyRequest`); los de auth son clases Lombok `@Data @Builder`.
- Indentación 4 espacios (tabs solo en `pom.xml` y archivos generados por Initializr). Comentarios en español.
- Referencias: controller `property/PropertyController.java` · servicio `property/PropertyServiceImp.java` · tests: ver `docs/TESTING.md` §3.

## Cuidado

- Errores: los servicios lanzan `Exception` genérica y todo termina en 403 hasta T-008. No agregues `try/catch` que devuelvan 400/404 a mano: esperá a T-008 o pedilo como tarea.
- `security/**`, `SecurityConfig`, `JwtService`, `CorsConfig` → solo tareas 🧠.
- `@Data` en entidades con relaciones bidireccionales (`User` ↔ `Property` ↔ `Image`): no uses `equals/hashCode/toString` de entidades en tests ni logs (recursión).
- Archivos largos: `PropertyServiceImp.java` (276 líneas), `ImageServiceImp.java` (219): leé por rangos.
