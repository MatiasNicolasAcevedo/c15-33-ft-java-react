# Front · Clonair (React + Vite)

SPA del clon de Airbnb. Para levantar **todo** el proyecto, ver el [README raíz](../../README.md).

## Configurar y correr solo el front

Desde la raíz del repo, una vez:

```bash
cp frontend/airbnb_clone/.env.example frontend/airbnb_clone/.env
```

Desde `frontend/airbnb_clone/`:

```bash
npm ci
```

```bash
npm run dev
```

**Resultado esperado:** `Local: http://localhost:5173/`.
⚠️ Hoy falla: la home muestra la pantalla de error ([T-011](../../docs/tasks/T-011-home-error-state.md)).

## Comandos

| Qué | Comando (desde `frontend/airbnb_clone/`) | Notas |
|---|---|---|
| Instalar | `npm ci` | usa `package-lock.json` |
| Desarrollo | `npm run dev` | http://localhost:5173 |
| Build | `npm run build` | sale en `dist/` (~7 s) |
| Lint | `npm run lint` | verde desde T-005 |
| Tests | `npm test` | Vitest + RTL + MSW; no necesita API ni Docker |
| Cobertura | `npm run test:coverage` | v8; umbral global de líneas 1 %, base 1,65 % (T-009); reporte `coverage/index.html` |
| Un archivo de tests | `npx vitest run src/components/shared/HostCardItem/HostCardItem.test.jsx` | referencia de componente puro; API mockeada en `src/services/apiRequests.test.js` |
| Preview del build | `npm run preview` | |
| Gate del repo | `../../scripts/verify.sh --client` | |

## Estructura

```
src/
  main.jsx, App.jsx           # entrada: ThemeProvider (Material Tailwind) → App → Router
  routes/Routes.jsx           # todas las rutas (createBrowserRouter)
  services/apiRequests.jsx    # TODAS las llamadas a la API (fetch + token en localStorage)
  context/authContext.jsx     # usuario logueado y rol
  components/                 # por feature: cards/, filters/, filteredProperties/, owner/, user/, admin/, shared/, …
                              # cada carpeta exporta desde su index.js
```

Convenciones y límites para agentes: [`AGENTS.md`](AGENTS.md).

Los tests fijan `VITE_API_URL=http://localhost:8080` y MSW intercepta las llamadas;
las peticiones sin handler se rechazan. El setup limpia el DOM, los handlers y el
storage entre casos, y Vitest restaura los spies. Para agregar tests y mantener el
umbral, ver [TESTING](../../docs/TESTING.md) §3/§5. La home sigue pendiente de T-011;
el contrato `images`/`imageUrls` se alinea en T-012.
