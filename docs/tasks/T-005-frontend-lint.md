# T-005 · Dejar `npm run lint` en verde
- Estado: Lista · Prioridad: P0 · Esfuerzo: S · Ejecuta: 🤖 Junie Lite (Free)
- Fase: P0 Estabilizar
- Rama: `chore/T-005-frontend-lint` (crearla desde `development` actualizado antes del primer commit) · Depende de: — · Spec/ADR: —
- Revisa: 🤖 Junie Lite (Free) (chat nuevo)

## Por qué
Q-1: `npm run lint` falla con 93 problemas (66 `react/prop-types`, 17 `no-unused-vars`, 2 `no-undef`, 1 `no-prototype-builtins`, 7 warnings que cuentan por `--max-warnings 0`). Es lo único que deja `verify.sh` en rojo y bloquea la CI (T-010).

## Objetivo
`npm run lint` sale con código 0 **sin cambiar el comportamiento** de ningún componente.

## Alcance
- Incluye: (1) en `.eslintrc.cjs` desactivar `react/prop-types` (decisión 🧠: JS sin PropTypes; la migración a TS está en Backlog P3) y agregar un `overrides` con `env: { node: true }` para `tailwind.config.js` y `postcss.config.js`; (2) borrar variables/props sin uso; (3) `CountryList.jsx:6` → `Object.prototype.hasOwnProperty.call(countries, key)`; (4) los 7 warnings con `// eslint-disable-next-line <regla> -- comportamiento existente, ver Backlog` en la línea indicada.
- **No incluye:** agregar dependencias a los `useEffect` (puede crear loops: queda en Backlog), mover `useAuth` a otro archivo, Prettier, formatear archivos enteros, cualquier otro cambio.

## Leer antes
- `frontend/airbnb_clone/AGENTS.md` · `frontend/airbnb_clone/.eslintrc.cjs` (20 líneas)
- Salida de `npx eslint . --ext js,jsx -f unix | grep -v prop-types` (la lista exacta de archivos y líneas)

## Archivos previstos
- modificar: `.eslintrc.cjs`, `src/components/Test.jsx`, `cards/DetailCardInfo/DetailCardInfo.jsx`, `countries/CountryList.jsx`, `filteredProperties/FilteredFilters.jsx`, `forms/Forms.jsx`, `owner/PropertiesContainer/PropertiesContainer.jsx`, `owner/PropertiesUpdateForm/PropertiesUpdateForm.jsx`, `shared/HostCardItem/HostCardItem.jsx`, `user/PersonalInfoForm/PersonalInfoForm.jsx`, `context/authContext.jsx`, `services/apiRequests.jsx` (≈ 12 archivos, cambios de 1–3 líneas cada uno)

## Tests
- No hay infraestructura de tests de front (T-009). Verificación: lint + build.

## Pasos sugeridos
1. Cambiar `.eslintrc.cjs` y correr `npm run lint` (deberían quedar ~27 problemas).
2. Arreglar archivo por archivo con `npx eslint <archivo>`; en desestructuraciones, quitar solo el nombre que sobra. Si un nombre sin uso es un `useState`, dejar el setter si se usa (`const [, setX]`).
3. Para `Forms.jsx:199` (useEffect sin array de dependencias que llama a un setter): **no** lo cambies; deshabilitá la regla en esa línea y anotá en "Notas" que es candidato a bug.
4. `npm run build` y revisar el diff: solo líneas de lint.

## Criterios de aceptación
- [ ] `cd frontend/airbnb_clone && npm run lint` → código 0, sin warnings
- [ ] `npm run build` pasa
- [ ] `./scripts/verify.sh --client` → `client: ✅ lint ✅ build`
- [ ] `git diff --stat development` ≤ 15 archivos y ninguno fuera de `frontend/airbnb_clone/` salvo docs del tablero
- [ ] Cada `eslint-disable` tiene el comentario `-- comportamiento existente, ver Backlog`

## Notas de implementación
- Hecho: … · Pendiente: … · Próximo paso: …

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: — · Pendientes → …
