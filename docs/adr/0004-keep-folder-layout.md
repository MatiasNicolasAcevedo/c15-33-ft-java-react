# 0004 · Mantener `backend/` y `frontend/airbnb_clone/`

- Estado: Aceptado · Fecha: 2026-10-10 · Decide: 🧠 (auditoría T-000)

## Contexto
La estructura recomendada para el harness es `client/` + `server/`, pero el repo usa `backend/` y `frontend/airbnb_clone/`, referenciados por el historial, los links del README y el posible deploy.

## Decisión
No se mueven carpetas. Los docs dicen "server" = `backend/` y "client" = `frontend/airbnb_clone/`.

## Alternativas
Mover con `git mv` en un PR propio: historia con `--follow`, pero rompe links externos y la config de Vercel/Render (desconocida).

## Consecuencias
Rutas más largas en scripts y docs. Reevaluar en P3 (Backlog) si el dueño lo pide.
