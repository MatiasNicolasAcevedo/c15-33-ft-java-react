# T-011 · Mostrar estados de carga, error y vacío en la home en vez de romperse
- Estado: Bloqueada (por merge de T-012; T-004/T-009 satisfechas) · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🤖 Junie Lite (Free)
- Fase: P0 Estabilizar
- Rama: `fix/T-011-home-error-state` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-004, T-009, T-012 · Spec/ADR: —
- Revisa: 🤖 Junie Lite (Free) (chat nuevo)

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
- `src/services/apiRequests.test.js` y `src/test/msw/{handlers,server}.js` (MSW, creados en T-009); `src/components/shared/HostCardItem/HostCardItem.test.jsx` (RTL)

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
- Dependencias revisadas el 2026-10-13: T-009 mergeada en #13 (`dc27563`), runner y mocks disponibles. Se agrega T-012 como dependencia porque la respuesta real tiene imageUrls y CardContainer/HostCardItem esperan images: el caso de lista no vacía rompe antes de poder comprobar el título. Resolver ese contrato primero en frontera; después Lite usa el fixture actualizado, sin agregar un campo ficticio images al mock ni cambiar el contrato en esta tarea.
- Orden: T-010 → T-012 → T-011 → T-013. Al mergear T-012, pasar esta tarea a la primera de Listas y preparar su prompt completo de implementación en STATUS/BACKLOG. No iniciar mientras haya un PR pendiente.
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Pendientes → …
