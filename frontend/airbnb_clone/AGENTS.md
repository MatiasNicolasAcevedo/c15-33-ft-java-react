# frontend/airbnb_clone/AGENTS.md

Solo lo específico del front. Lo general está en el [AGENTS.md raíz](../../AGENTS.md).

## Comandos (desde `frontend/airbnb_clone/`)

- `npm ci` · `npm run dev` (http://localhost:5173) · `npm run build` · `npm run lint` · gate: `../../scripts/verify.sh --client`.
- Tests: `npm test` · cobertura v8 y umbral: `npm run test:coverage` · un archivo: `npx vitest run src/components/shared/HostCardItem/HostCardItem.test.jsx`.
- Config: `.env` (copia de `.env.example`), variables `VITE_*`. No la leas ni la commitees.

## Convenciones actuales

- Componentes funcionales `.jsx` con `export const Nombre = (...) => {}`; una carpeta por componente (`components/cards/DetailCardHead/DetailCardHead.jsx` + `index.js` que re-exporta).
- Rutas: todas en `src/routes/Routes.jsx` (`createBrowserRouter`, hijas de `Root`).
- API: **todas** las llamadas en `src/services/apiRequests.jsx` (`fetch`, token en `localStorage`). Los componentes no llaman a `fetch` directo (excepción histórica: `components/Test.jsx`).
- Estado de sesión: `src/context/authContext.jsx` (`useAuth()` → `user`, `isAdmin`, `isOwner`).
- Estilos: Tailwind en `className` + componentes de `@material-tailwind/react`; íconos FontAwesome.
- Indentación mayormente 2 espacios (hay archivos con 4: respetá el del archivo que tocás).
- Referencia de componente: `components/shared/HostCardItem/HostCardItem.jsx` (simple) y `components/shared/CardContainer/CardContainer.jsx` (con datos de la API).
- Referencias de tests: `components/shared/HostCardItem/HostCardItem.test.jsx` (RTL) y `services/apiRequests.test.js` (MSW). Setup y handlers en `src/test/`; importá las funciones desde `vitest`, sin globals. Ver [TESTING](../../docs/TESTING.md) §3/§5.

## Cuidado

- Archivos largos (leé por rangos): `components/forms/Forms.jsx` (443), `components/admin/AdminPanel.jsx` (390), `components/owner/PropertiesContainer/PropertiesContainer.jsx` (377), `components/property/PropertyRegister.jsx` (344), `services/apiRequests.jsx` (335).
- No abras `package-lock.json` ni `dist/`.
- T-004 alineó las rutas con `/v1/api` y la base configurable. El contrato de imágenes sigue desalineado (`images` vs `imageUrls`): se corrige en T-012.
