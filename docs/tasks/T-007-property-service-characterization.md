# T-007 · Escribir tests de caracterización de `PropertyServiceImp`
- Estado: Lista · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🤖 Junie Lite (Free)
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
- [ ] ≥ 10 tests nuevos, todos verdes; `./scripts/verify.sh --server` pasa
- [ ] Cobertura de líneas de `PropertyServiceImp` ≥ 60 % (`backend/target/site/jacoco/com.clonair.back.property/PropertyServiceImp.html`)
- [ ] Spot check: cambiar `Role.OWNER` por `Role.USER` en `PropertyServiceImp.java:70` hace fallar al menos un test (revertir después; anotarlo en "Notas")
- [ ] Ningún archivo de `src/main` modificado (`git diff --stat development -- backend/src/main` vacío)

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Pendientes → …
