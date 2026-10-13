# T-009 · Montar la infraestructura de tests del front
- Estado: Hecha (PR #13 revisado y mergeado por el dueño) · Prioridad: P0 · Esfuerzo: M · Ejecuta: 🧠 modelo de frontera
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
- [x] `npm test` y `npm run test:coverage` pasan; umbral configurado
- [x] `npx vitest run src/components/shared/HostCardItem/HostCardItem.test.jsx` corre un solo archivo
- [x] `./scripts/verify.sh --client` → `✅ lint ✅ build ✅ tests`
- [x] `docs/TESTING.md` §3 apunta a las referencias reales

## Notas de implementación
- Rama: `test/T-009-frontend-test-infra` · [PR #13](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/13) · commit de implementación: `c632aca` (publicado); commit inicial: `be3ef72`. El cierre documental se guarda después; el revisor debe verificar el HEAD publicado del PR.
- Inicio: árbol limpio, ningún PR abierto; #11/#12 confirmados MERGED, development actualizado a `793fa11`; línea de base completa verde (118 tests back, cobertura OK, lint/build front OK).
- Hecho: dependencias exactas de tests compatibles (Vitest/v8 3.2.7, RTL 16.3.0 y peer DOM, jsdom 26.1.0, jest-dom 6.9.1, user-event 14.6.8, MSW 2.15.0). Vite 5.0.2 y React/React DOM 18.2.0 conservados. Setup aislado, handlers y 6 tests de referencia; cobertura global de todo src, excluyendo solo tests/support.
- Decisiones: globals desactivados y funciones de Vitest importadas explícitamente, sin globals de ESLint; coverage ignorada por Git/lint; URL fija exclusiva de tests, handlers reseteados, storages/DOM limpiados y spies restaurados. user-event queda disponible para futuras interacciones; HostCardItem es puro y no tiene controles.
- Contrato MSW: `/v3/api-docs` obtenido el 2026-10-13 con una prueba temporal usando application-test.properties y Testcontainers MySQL 8.4. HTTP 200 → lista PropertyResponse, imageUrls, Location.id string y enums verificados. La prueba temporal fue retirada antes del gate final; sin cambios al backend. HTTP 500 usa ProblemDetail. HostCardItem conserva images; getAllProperties conserva undefined en errores. UI/home T-011 e imágenes T-012 pendientes.
- Archivos: package.json/lock, vite.config.js, ESLint/.gitignore; src/test/setup.js y msw/handlers.js/server.js; HostCardItem.test.jsx y apiRequests.test.js; TESTING §3/§5, READMEs/reglas del front y raíz, tarea/STATUS/BACKLOG y archivo de entradas antiguas del tablero/sesiones.
- Pruebas: npm test ✅ (2 archivos/6 tests); npm run test:coverage ✅ (6 tests); npx vitest run src/components/shared/HostCardItem/HostCardItem.test.jsx ✅ (solo 1 archivo/2 tests). Lint ✅. verify.sh ✅ (97 s: server 118 tests y cobertura, client lint/build/6 tests); verify.sh --client ✅ (14 s). Java 21, Node 24 (gate 24.19.0). npm ci del gate ✅, lockfile reproducible.
- Cobertura: líneas/statements 1,65 % (54/3254), ramas 7,59 % (6/79), funciones 2,27 % (2/88). HostCardItem 100 % (20/20), apiRequests.jsx 11,34 % (32/282). Umbral global de líneas = floor(1,65) = 1 %, sin umbrales de otras métricas. Back en gate final: 82,59 % (517/626), umbral 40 % intacto.
- Trinquete: override CLI --coverage.thresholds.lines=100 rechazado con exit 1; configuración guardada intacta y cobertura con el umbral 1 % nuevamente verde. Reportes locales ignorados en coverage/.
- Revisión independiente [APROBADA](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/13#issuecomment-6146403632) sobre el HEAD publicado `e5de84062fc961983bfd78198d77f60044afe9d2`: gate completo verde (95 s, 118 tests back/6 front), npm ci reproducible, gate client verde (17 s), test individual 2/2, cobertura 1,65 % y rechazo del umbral 100 % con exit 1; umbral original 1 % intacto. Sin bloqueantes.
- Merge confirmado en GitHub: #13 MERGED el 2026-10-13, squash `dc2756330096d406ba0523ab7d9d39724191b97c` en development.
- Cierre documental autorizado por el dueño después del merge: rama `docs/T-009-closeout`, [PR #14](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/14), base `dc27563`, primer commit `0c0b582`. Árbol limpio al inicio, sin cambios heredados; se revisan cola/dependencias y documentación, sin iniciar T-010. Validación: diff solo Markdown, enlaces locales, estados/dependencias y prompts sincronizados; git diff --check. Se conserva la evidencia de tests anterior y no se repite el gate por este cierre sin cambios ejecutables. Último commit de cierre: git log -1 en esa rama; comprobar su coincidencia con el HEAD publicado de #14.
- Archivos del cierre #14: README, docs/STATUS, BACKLOG, ROADMAP y TESTING; contratos T-003/T-005/T-008/T-009/T-010/T-011/T-012/T-013/T-014/T-016. Validación final: 15 archivos Markdown, 58 enlaces locales existentes (plantilla T-NNN excluida), prompts del dueño/T-010 idénticos, criterios de aceptación intactos, estados y dependencias coherentes sin ciclos. T-015 conserva su estado Lista sin asumir acciones del dueño.
- Pendiente: integrar el cierre documental; T-009 ya está hecha. T-012 queda Lista; T-011 espera T-012 por el contrato de imágenes al renderizar un catálogo no vacío. T-013 espera los merges de T-010/T-011/T-012.
- Próxima acción concreta: dueño revisa e integra el PR documental; después sesión nueva de 🧠 Frontera para la primera Lista, T-010. Prompt concreto en STATUS/BACKLOG.

## Resultado
- [PR #13](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/13) · revisión independiente APROBADA y merge por el dueño con squash `dc27563`. Vitest + RTL + MSW + cobertura v8 configurados, 6 tests agregados, cobertura 1,65 % y umbral 1 %. Todos los criterios verificados; sin cambios de producción. Pendiente documental: integrar [#14](https://github.com/MatiasNicolasAcevedo/c15-33-ft-java-react/pull/14); próxima implementación T-010, seguida de T-012 y T-011. El problema de la home y el contrato de imágenes conservan sus tareas.
