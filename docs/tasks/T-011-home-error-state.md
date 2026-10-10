# T-011 · Mostrar estados de carga, error y vacío en la home en vez de romperse
- Estado: Bloqueada (por T-009: no hay infraestructura de tests de front) · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🤖 agente gratuito
- Fase: P0 Estabilizar
- Rama: `fix/T-011-home-error-state` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-004, T-009 · Spec/ADR: —
- Revisa: 🤖 agente gratuito (chat nuevo)

## Por qué
F-2: si la API falla, `getAllProperties` (`apiRequests.jsx:228-246`) devuelve `undefined`, `CardContainer.jsx:18` lo guarda y `CardContainer.jsx:76` hace `properties.map` → excepción → la home muestra la pantalla 404 (verificado con Playwright en la auditoría).

## Objetivo
La home muestra "Cargando…", un mensaje de error con botón "Reintentar", o el `EmptyState` existente si la lista viene vacía; nunca la pantalla 404.

## Alcance
- Incluye: `CardContainer.jsx` (estados `loading`/`error`), reutilizar `components/shared/EmptyState`.
- **No incluye:** cambiar `apiRequests.jsx` (salvo que el test lo exija y esté anotado), otras pantallas (crear líneas de Backlog), estilos nuevos.

## Leer antes
- `frontend/airbnb_clone/AGENTS.md` · `docs/TESTING.md` §3–§4
- `src/components/shared/CardContainer/CardContainer.jsx` (completo) · `src/components/shared/EmptyState/EmptyState.jsx`
- Test de referencia creado en T-009 (MSW)

## Archivos previstos
- modificar: `src/components/shared/CardContainer/CardContainer.jsx` · crear: `src/components/shared/CardContainer/CardContainer.test.jsx`

## Tests
- Primero el test que falla: MSW devuelve 500 en `GET /v1/api/property` → se ve el texto de error (por rol/texto) y no se lanza excepción.
- Lista vacía → `EmptyState`; lista con 1 propiedad → su título; "Reintentar" vuelve a llamar a la API.

## Criterios de aceptación
- [ ] Primer commit con el test fallando (anotar salida en "Notas")
- [ ] `./scripts/verify.sh --client` pasa y la cobertura no baja
- [ ] Con el front corriendo (`npm run dev`) y el back apagado, la home muestra el mensaje de error y no la pantalla 404

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Pendientes → …
