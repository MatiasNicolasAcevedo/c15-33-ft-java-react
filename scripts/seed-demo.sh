#!/usr/bin/env bash
# Crea el usuario demo (OWNER) vía la API. Idempotente: si ya existe, solo prueba el login.
# Uso: ./scripts/seed-demo.sh [URL_DEL_BACK]   (por defecto http://localhost:8080)
# Credenciales SOLO para local.
set -uo pipefail
API="${1:-http://localhost:8080}/v1/api/auth"
USER_JSON='{"username":"demo-owner@clonair.local","password":"Demo1234!","firstname":"Demo","lastname":"Owner","country":"AR","role":"OWNER"}'
LOGIN_JSON='{"username":"demo-owner@clonair.local","password":"Demo1234!"}'

code=$(curl -s -o /dev/null -w '%{http_code}' -X POST "$API/register" -H 'Content-Type: application/json' -d "$USER_JSON")
case "$code" in
  201) echo "usuario demo creado" ;;
  409) echo "usuario demo ya existía (register → $code)" ;;
  *) echo "❌ registro demo falló (HTTP $code)"; exit 1 ;;
esac
code=$(curl -s -o /dev/null -w '%{http_code}' -X POST "$API/login" -H 'Content-Type: application/json' -d "$LOGIN_JSON")
if [ "$code" = 200 ]; then
  echo "✅ login demo OK · usuario: demo-owner@clonair.local · contraseña: Demo1234!"
else
  echo "❌ login demo falló (HTTP $code)"; exit 1
fi
