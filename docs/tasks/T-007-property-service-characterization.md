# T-007 · Escribir tests de caracterización de `PropertyServiceImp`
- Estado: Lista para revisión · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🤖 Junie Lite (Free)
- Fase: P0 Estabilizar
- Rama: `test/T-007-property-service-characterization` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-002, T-006 · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (tarea M)

## Por qué
Flujos críticos 3 y 4 de la auditoría (alta por OWNER, edición/borrado solo por el dueño) no tienen ningún test; la lógica de permisos vive a mano en `PropertyServiceImp.java:60-171`. Antes de refactorizarla (T-008, Backlog P2) hay que fijar el comportamiento actual.

## Objetivo
Tests unitarios (Mockito, sin Spring) que fallan si cambia cualquiera de las reglas actuales de `save`, `update`, `delete` y `getOne`.

## Alcance
- Incluye: solo tests nuevos en `backend/src/test/java/com/clonair/back/property/`.
- **No incluye:** cambiar código de `src/main` (si encontrás un bug, escribilo en "Notas" y en Backlog; el test documenta el comportamiento **actual** con un comentario `// comportamiento actual: …`).

## Leer antes
- `backend/AGENTS.md` (25 líneas) · `docs/TESTING.md` §2–§4
- `backend/src/main/java/com/clonair/back/property/PropertyServiceImp.java:1-171` (por rangos)
- `backend/src/main/java/com/clonair/back/security/utils/Utils.java` (lee `SecurityContextHolder`)
- Referencia Mockito de T-002: `backend/src/test/java/com/clonair/back/user/UserServiceImpTest.java`

## Archivos previstos
- crear: `backend/src/test/java/com/clonair/back/property/PropertyServiceImpTest.java` (y, si hace falta, `PropertyTestData.java` con builders)

## Tests
Mocks: `JwtService`, `PropertyRepository`, `ImageService`, `UserService`. Antes de cada test, poner un `UsernamePasswordAuthenticationToken` con el `User` en `SecurityContextHolder`; limpiarlo en `@AfterEach`.
- `getOne`: id inexistente → lanza `Exception` "Property not found".
- `save`: token sin `Bearer ` → lanza; token inválido (`isTokenValid` false) → lanza; usuario USER → lanza "El usuario no es un OWNER" y no guarda; OWNER → `propertyRepository.save` con `user` = el OWNER y las imágenes asociadas.
- `update`: OWNER que no es dueño → lanza y no guarda; dueño → guarda con los campos nuevos.
- `delete`: dueño → `propertyRepository.delete(property)`; otro OWNER → lanza; USER → lanza.

## Pasos sugeridos
1. Un `@Nested` por método; nombres `metodo_condicion_resultado`.
2. `./mvnw -q test -Dtest=PropertyServiceImpTest` entre pasos; `./scripts/verify.sh --server` al final.
3. Si el contexto se llena: persistir "Notas", commit, push y chat nuevo.

## Criterios de aceptación
- [x] ≥ 10 tests nuevos, todos verdes; `./scripts/verify.sh --server` pasa → **18 tests OK, 77 tests totales, BUILD SUCCESS**
- [x] Cobertura de líneas de `PropertyServiceImp` ≥ 60 % → **95,07 % (135/142 líneas)**
- [x] Spot check: cambiar `Role.OWNER` por `Role.USER` en `PropertyServiceImp.java:70` hace fallar al menos un test → **9 tests fallaron** (6 failures + 3 errors en Save, Update, Delete); revertido
- [x] Ningún archivo de `src/main` modificado → **`git diff --stat development -- backend/src/main` vacío**

## Notas de implementación
- Hecho: 18 tests unitarios (Mockito puro) en `PropertyServiceImpTest.java` organizados en 4 clases @Nested (GetOne, Save, Update, Delete). Cubren: token missing/invalid, usuario no OWNER, OWNER que no es dueño, casos exitosos de save/update/delete, y getOne con/sin propiedad.
- Decisión: usar `SecurityContextHolder` con `UsernamePasswordAuthenticationToken` para simular el contexto de seguridad; mocks de `JwtService`, `PropertyRepository`, `ImageService`, `UserService`.
- Pruebas: `./mvnw test -Dtest='PropertyServiceImpTest'` → 18 tests OK. `verify.sh --server` → 77 tests OK, cobertura OK.
- Spot check: cambiar `Role.OWNER` por `Role.USER` en `PropertyServiceImp.java:70` hizo fallar 9 tests (6 failures + 3 errors en Save, Update, Delete). Revertido.
- Cobertura PropertyServiceImp: 95,07 % (135/142 líneas). Bundle: 76,20 % (429/563 líneas).
- Sin cambios en `src/main` (`git diff --stat development -- backend/src/main` vacío).
- Cambios del PR #10 atendidos: test de update con valores distintos y verificación de todos los campos; test de save con imágenes no vacías y verificación de asociación bidireccional por identidad.
- Segunda revisión: [escalación](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10#issuecomment-6128830751) del commit `7e19637`: los servicios se verificaban solo por cantidad. El dueño autorizó expresamente a 🧠 Frontera (Codex) a corregirlo en la misma rama y PR.
- Corrección: la lista de servicios guardados debe ser exactamente `[SWIMMING_POOL, PARKING_LOT]`; comparación de enums, sin equals/hashCode de entidades. No se modifica `src/main`.
- Verificación de la corrección: gate completo `bash ./scripts/verify.sh` ✅ (54 s), 77 tests sin fallos/errores/skips, lint/build del client ✅. Mutación aislada de los servicios de update a `[WIFI, WIFI]`: 18 tests, 1 failure, 0 errors; fuente temporal restaurado. Antes de esta corrección la misma mutación dejaba los 18 tests verdes.
- Pendiente: nueva revisión por 🧠 modelo de frontera.
- Rama / PR: `test/T-007-property-service-characterization` · #10. Punto de partida: `7e1963792f1d5897676692af85e9d890374688b4`; el commit de corrección incluye estas notas y se identifica con `git log -1`.
- Próximo paso: sesión nueva de revisión independiente del commit corregido; publicar el veredicto sobre la comprobación exacta de servicios. No iniciar T-008 antes del merge.

## Resultado
- PR: [#10](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/10) (ready) · Qué cambió: 1 archivo con 18 tests de caracterización; update verifica todos los valores, incluidos los servicios exactos, y save verifica las asociaciones por identidad · Tests agregados: 18 (GetOne: 2, Save: 5, Update: 6, Delete: 5) · Cobertura: 95,07 % PropertyServiceImp, 76,20 % bundle · Pendientes → nueva revisión independiente 🧠
