# T-006 · Corregir el endpoint `DELETE /v1/api/property/{id}`
- Estado: Lista · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🤖 Junie Lite (Free)
- Fase: P0 Estabilizar
- Rama: `fix/T-006-delete-property-mapping` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-002 · Spec/ADR: —
- Revisa: 🤖 Junie Lite (Free) (chat nuevo)

## Por qué
`PropertyController.java:90-91` declara `@DeleteMapping()` sin `{id}` pero lee `@PathVariable String id`: el front (`apiRequests.jsx` → `deleteProperty`, usado en `AdminPanel.jsx:75` y `PropertiesUser`) llama a `DELETE /…/property/{id}` y nunca funciona (probe → 403).

## Objetivo
`DELETE /v1/api/property/{id}` llega a `PropertyService.delete(id, token)`.

## Alcance
- Incluye: cambiar la anotación a `@DeleteMapping("/{id}")` y un test `@WebMvcTest` que lo pruebe.
- **No incluye:** cambiar el servicio, el código de respuesta (sigue 200 vacío; 204 es parte de T-008), permisos (T-003), el front.

## Leer antes
- `backend/AGENTS.md` (25 líneas)
- `backend/src/main/java/com/clonair/back/property/PropertyController.java:85-95`
- El test de referencia `@WebMvcTest` que creó T-002: `backend/src/test/java/com/clonair/back/security/auth/AuthControllerTest.java` (copiá su configuración de seguridad y mocks)
- `docs/TESTING.md` §3–§4

## Archivos previstos
- modificar: `backend/src/main/java/com/clonair/back/property/PropertyController.java` (1 línea)
- crear: `backend/src/test/java/com/clonair/back/property/PropertyControllerTest.java`

## Tests
- **Primero el test (debe fallar antes del fix):** `@WebMvcTest(PropertyController.class)` con `@MockBean PropertyService`; request `DELETE /v1/api/property/abc` con header `Authorization: Bearer x` y usuario autenticado (como en la referencia) → status 200 y `verify(propertyService).delete("abc", "Bearer x")`.
- Caso de error: `DELETE /v1/api/property` (sin id) → no 2xx y el servicio nunca se llama (`verifyNoInteractions`).

## Pasos sugeridos
1. Crear el test copiando el setup de `AuthControllerTest`; correrlo: `./mvnw -q test -Dtest=PropertyControllerTest` → debe fallar. Commit `test(server): reproduce delete property mapping bug` con `Refs: T-006`.
2. Cambiar la anotación; correr el test → pasa. Commit `fix(server): map delete property by id`.
3. `./scripts/verify.sh --server`.

## Criterios de aceptación
- [ ] El primer commit tiene el test fallando (anotar la salida en "Notas")
- [ ] `./scripts/verify.sh --server` pasa y la cobertura no baja
- [ ] Con el back corriendo: `curl -s -o /dev/null -w '%{http_code}' -X DELETE -H "Authorization: Bearer <token del dueño>" http://localhost:8080/v1/api/property/<id>` → 200 y `GET` de ese id deja de devolver 200

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Pendientes → …
