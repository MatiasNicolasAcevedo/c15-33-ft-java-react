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
⚠️ Hoy falla: la URL de la API está hardcodeada a producción (`src/services/apiRequests.jsx:1`) y la home cae en la pantalla de error. Se arregla en [T-004](../../docs/tasks/T-004-frontend-api-base-url.md) (usar `VITE_API_URL`) y [T-011](../../docs/tasks/T-011-home-error-state.md).

## Comandos

| Qué | Comando (desde `frontend/airbnb_clone/`) | Notas |
|---|---|---|
| Instalar | `npm ci` | usa `package-lock.json` |
| Desarrollo | `npm run dev` | http://localhost:5173 |
| Build | `npm run build` | sale en `dist/` (~7 s) |
| Lint | `npm run lint` | ⚠️ hoy falla: 93 problemas ([T-005](../../docs/tasks/T-005-frontend-lint.md)) |
| Tests | — | no hay todavía ([T-009](../../docs/tasks/T-009-frontend-test-infra.md)); un solo test será `npx vitest run <archivo>` |
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
