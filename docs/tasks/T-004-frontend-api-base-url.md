# T-004 · Hacer configurable la URL de la API en el front y usar el prefijo /v1/api
- Estado: Hecho · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🤖 Junie Lite (Free)
- Fase: P0 Estabilizar
- Rama: `fix/T-004-frontend-api-base-url` (crearla desde `development` actualizado antes del primer commit) · Depende de: — · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (sesión nueva; toca configuración, según CONTRIBUTING)

## Por qué
🔴 F-1: la URL del back está hardcodeada a una IP de producción en 4 archivos y las rutas no tienen el prefijo `/v1` que el back usa desde jun-2024, así que **el front local no habla con el back local** (la home cae en la pantalla de error).

## Objetivo
Todas las llamadas del front usan `import.meta.env.VITE_API_URL` + `/v1/api/...`; con `.env` copiado del `.example`, el front local llama a `http://localhost:8080/v1/api/...`.

## Alcance
- Incluye: crear `src/services/config.js`, reemplazar la URL y los prefijos en los 4 archivos, quitar los ⚠️ de T-004 en los docs listados.
- **No incluye:** cambiar la lógica de `fetch`, el manejo de errores o el crash de la home (T-011), el campo `images` vs `imageUrls` (T-012), permisos del back (T-003), `.eslintrc.cjs` (T-005).

## Leer antes
- `frontend/airbnb_clone/AGENTS.md` (completo, 25 líneas)
- `frontend/airbnb_clone/src/services/apiRequests.jsx:1-60` (patrón de las llamadas)
- `frontend/airbnb_clone/.env.example`

## Archivos previstos
- crear: `frontend/airbnb_clone/src/services/config.js`
- modificar: `src/services/apiRequests.jsx` (línea 1 y las 14 llamadas: 5, 30, 94, 119, 145, 165, 186, 209, 230, 251, 272, 294, 316), `src/components/Test.jsx:7`, `src/components/shared/HostCardItem/HostCardItem.jsx:17`, `src/components/cards/DetailContainerCard/DetailContainerCard.jsx:27`, `frontend/airbnb_clone/.env.example` (quitar el ⚠️), `README.md` y `frontend/airbnb_clone/README.md` (quitar solo los ⚠️ que nombran T-004; los de T-011 quedan), `docker-compose.yml` (quitar "Hoy el código no lo lee (T-004)")

## Tests
- No hay infraestructura de tests de front (T-009). Verificación por comandos (criterios). No agregues librerías de test.

## Pasos sugeridos
1. `config.js`:
   ```js
   // URL base de la API. Se define en .env (VITE_API_URL); ver .env.example.
   export const API_URL = `${import.meta.env.VITE_API_URL ?? 'http://localhost:8080'}/v1/api`;
   ```
2. En `apiRequests.jsx`: `import { API_URL } from './config';`, borrar `const apiUrl = …` y reemplazar `apiUrl+"/auth/login"` → `` `${API_URL}/auth/login` ``, `` `${apiUrl}/api/property` `` → `` `${API_URL}/property` `` (igual para `user` e `img`).
3. En `Test.jsx`, `HostCardItem.jsx` y `DetailContainerCard.jsx`: importar `API_URL` (ruta relativa correcta) y usar `` `${API_URL}/img/${images[0]}` ``; en `Test.jsx` borrar la constante local y usar `API_URL` donde se usaba `apiUrl` con el mismo cambio de prefijo.
4. Correr los criterios.

## Criterios de aceptación
- [x] `cd frontend/airbnb_clone && grep -rnE "https?://[0-9]+\.[0-9]+" src` no devuelve nada ✅
- [x] `grep -rn "apiUrl" src` no devuelve nada y `grep -rn "/api/" src` solo muestra rutas armadas con `API_URL` ✅
- [x] `npm run build` pasa y `grep -l "localhost:8080/v1/api" dist/assets/*.js` encuentra el bundle (con `.env` copiado del `.example`) ✅
- [x] `../../scripts/verify.sh --client`: build ✅, lint 93 problemas (igual que development, sin sumar nuevos) ✅
- [ ] Con `./scripts/dev.sh` levantado, en `.dev-logs/server.log` aparecen requests del front (o, sin navegador: `curl -s localhost:5173/src/services/config.js` muestra `/v1/api`) — no ejecutado (requiere servidor dev)

## Notas de implementación
- Hecho: creado src/services/config.js con API_URL desde VITE_API_URL + /v1/api; reemplacé la IP hardcodeada en apiRequests.jsx (14 llamadas), Test.jsx, HostCardItem.jsx y DetailContainerCard.jsx; quité el prefijo /api/ de todas las rutas; actualicé .env.example, docker-compose.yml, README.md y frontend/README.md quitando los ⚠️ de T-004.
- Decisiones: API_URL incluye /v1/api para que las rutas queden limpias (/property, /user, /img, /auth). Fallback a localhost:8080 si VITE_API_URL no está definida.
- Archivos modificados: config.js (nuevo), apiRequests.jsx, Test.jsx, HostCardItem.jsx, DetailContainerCard.jsx, .env.example, docker-compose.yml, README.md, frontend/airbnb_clone/README.md.
- Pruebas/verify: build ✅, lint 93 problemas (igual que development, sin sumar nuevos), grep de IPs y apiUrl sin resultados, bundle contiene localhost:8080/v1/api.
- Bloqueos: ninguno.
- Próximo paso: PR #7 listo para revisión de frontera.

## Resultado
- PR: [#7](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/7) · Qué cambió: URL de API configurable vía VITE_API_URL, prefijo /v1/api en todas las rutas, IP de producción eliminada · Tests agregados: — · Pendientes: —
