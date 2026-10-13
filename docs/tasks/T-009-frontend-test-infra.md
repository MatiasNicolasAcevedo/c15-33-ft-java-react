# T-009 · Montar la infraestructura de tests del front
- Estado: En curso · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
- Fase: P0 Estabilizar
- Rama: `test/T-009-frontend-test-infra` (crearla desde `development` actualizado antes del primer commit) · Depende de: T-004, T-005 · Spec/ADR: —
- Revisa: 🧠 modelo de frontera (sesión nueva) + 👤

## Por qué
🔴 T-1: el front no tiene ningún test ni runner. Sin referencia, el 🤖 no puede tomar tareas de front con tests (T-011 está bloqueada por esto).

## Objetivo
`npm test` corre Vitest + React Testing Library + MSW con cobertura v8 y umbral, y hay un test de referencia de componente con API mockeada.

## Alcance
- Incluye: devDependencies `vitest`, `@vitest/coverage-v8`, `jsdom`, `@testing-library/react`, `@testing-library/jest-dom`, `@testing-library/user-event`, `msw` (versiones compatibles con Vite 5 y React 18: **no** subir Vite/React acá), `test` en `vite.config.js`, `src/test/setup.js`, handlers MSW en `src/test/msw/` (formas tomadas de `/v3/api-docs`), script `"test": "vitest run"` y `"test:coverage"`, umbral = cobertura medida redondeada hacia abajo, `docs/TESTING.md` §3/§5, `verify.sh` ya llama a `npm test` si existe.
- **No incluye:** arreglar el crash de la home (T-011), Playwright (T-013), TypeScript.

## Leer antes
- `docs/TESTING.md` · `frontend/airbnb_clone/AGENTS.md` · `package.json` · `vite.config.js`
- `src/components/shared/CardContainer/CardContainer.jsx` (candidato a test de referencia) · `src/services/apiRequests.jsx:228-246`

## Archivos previstos
- modificar: `package.json`, `package-lock.json`, `vite.config.js`, `.eslintrc.cjs` (globals de test), `docs/TESTING.md`, `frontend/airbnb_clone/README.md`, `AGENTS.md` (comando de un test)
- crear: `src/test/setup.js`, `src/test/msw/{handlers,server}.js`, `src/components/shared/HostCardItem/HostCardItem.test.jsx` (componente puro), `src/services/apiRequests.test.js` (MSW: `getAllProperties` devuelve la lista; con 500 devuelve `undefined` — comportamiento actual)

## Criterios de aceptación
- [ ] `npm test` y `npm run test:coverage` pasan; umbral configurado
- [ ] `npx vitest run src/components/shared/HostCardItem/HostCardItem.test.jsx` corre un solo archivo
- [ ] `./scripts/verify.sh --client` → `✅ lint ✅ build ✅ tests`
- [ ] `docs/TESTING.md` §3 apunta a las referencias reales

## Notas de implementación
- Hecho: #11/#12 mergeados confirmados, árbol limpio, development actualizado a `793fa11`; línea de base completa verde (118 tests back, cobertura OK, lint/build front OK). Rama exacta creada antes del primer commit.
- Pendiente: PR draft, dependencias, configuración, referencias, cobertura y gate final.
- Próximo paso: abrir PR draft e implementar el contrato de T-009. No modificar UI ni cliente de producción.

## Resultado
- PR: #N · Qué cambió: … · Tests agregados: … · Cobertura: …% · Pendientes → …
