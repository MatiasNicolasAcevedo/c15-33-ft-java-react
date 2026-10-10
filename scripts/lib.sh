#!/usr/bin/env bash
# Funciones compartidas por los scripts del harness. No ejecutar directo.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SERVER_DIR="$ROOT/backend"
CLIENT_DIR="$ROOT/frontend/airbnb_clone"
SERVER_ENV="$SERVER_DIR/src/main/resources/env.properties"
CLIENT_ENV="$CLIENT_DIR/.env"

# Exporta las variables del back desde el archivo real o, si falta, desde el .example.
# Deja en SERVER_ENV_USED el archivo leído (no llamar dentro de $(...): perdería los export).
load_server_env() {
  local f="$SERVER_ENV"
  [ -f "$f" ] || f="$SERVER_ENV.example"
  set -a
  # shellcheck disable=SC1090
  . <(grep -E '^[A-Z_]+=' "$f")
  set +a
  SERVER_ENV_USED="$f"
}

# Levanta solo la DB del docker-compose.yml y espera a que esté healthy.
db_up() {
  command -v docker >/dev/null || { echo "❌ falta Docker (ver README → Prerrequisitos)"; return 1; }
  docker compose -f "$ROOT/docker-compose.yml" up -d --wait db
}

# Espera a que una URL responda 2xx/3xx. Uso: wait_http URL SEGUNDOS
wait_http() {
  local url=$1 secs=$2 i
  for ((i = 0; i < secs; i++)); do
    curl -fs -o /dev/null "$url" && return 0
    sleep 1
  done
  return 1
}
