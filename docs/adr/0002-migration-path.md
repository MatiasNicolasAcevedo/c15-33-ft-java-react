# 0002 · Camino de migración de versiones

- Estado: Aceptado · Fecha: 2026-10-10 · Decide: 🧠 (auditoría T-000)

## Contexto
Boot 3.2.0 (sin soporte OSS desde fines de 2024), jjwt 0.11, Vite 5, React 18, ESLint 8 (EOL), 24 vulnerabilidades npm. No hay red de tests (16,8 % de líneas, 0 en el front).

## Decisión
1. **Primero la red de tests (P0):** T-002, T-009, T-013 y caracterización de los flujos críticos.
2. **Back, en PRs separados (P1):** Boot 3.2 → 3.5.x (resolver deprecaciones) → Java 21 → Boot 4.x con OpenRewrite (`rewrite-maven-plugin`, receta `UpgradeSpringBoot_4_0`) revisado a mano; jjwt 0.13 en su propio PR. Java 25 después de Boot 4.
3. **Front, en PRs separados (P1):** `npm audit fix` (sin `--force`) → ESLint 9 flat config → Vite 8 → React 19 → React Router 7 → Tailwind 4 (evaluar Material Tailwind, que no lo soporta).
4. Cada paso: `verify.sh` y E2E en verde, cobertura que no baja.

## Alternativas
- Saltar directo a Boot 4: demasiados cambios juntos (Security 7, Jackson 3, starters) sin tests.
- Reescribir el front: fuera de alcance; el valor de portfolio está en terminarlo.

## Consecuencias
- P1 son ~10 PRs chicos, casi todos 🧠 (versiones mayores); los minor/patch los toma Dependabot.
