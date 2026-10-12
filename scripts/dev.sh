#!/usr/bin/env bash
# Entorno de desarrollo: DB en contenedor + back (spring-boot:run) + front (Vite, hot reload).
# Hace lo mismo que el paso a paso del README (Opción B). Ctrl+C corta todo.
# Uso: ./scripts/dev.sh          levantar
#      ./scripts/dev.sh --down   apagar la DB (los datos quedan en el volumen)
set -uo pipefail
. "$(dirname "$0")/lib.sh"

if [ "${1:-}" = "--down" ]; then
  docker compose -f "$ROOT/docker-compose.yml" down
  exit
fi

LOGS="$ROOT/.dev-logs"; mkdir -p "$LOGS"
PGIDS=()

cleanup() {
  trap - INT TERM EXIT
  echo; echo "■ cortando back y front…"
  for g in "${PGIDS[@]}"; do kill -TERM -- "-$g" 2>/dev/null; done
  sleep 2
  for g in "${PGIDS[@]}"; do kill -KILL -- "-$g" 2>/dev/null; done
  echo "listo. La DB sigue corriendo: ./scripts/dev.sh --down para apagarla."
}
trap cleanup INT TERM EXIT

# Arranca un comando en su propio grupo de procesos (para cortar todo el árbol: npm → sh → node, mvnw → java).
start_group() { # dir log cmd...
  local dir=$1 log=$2; shift 2
  (cd "$dir" && exec setsid "$@" >"$log" 2>&1) &
  PGIDS+=("$!")
}

# 1. Configuración: copiar los .example si falta el archivo real
[ -f "$SERVER_ENV" ] || { cp "$SERVER_ENV.example" "$SERVER_ENV"; echo "• creado ${SERVER_ENV#"$ROOT"/} desde el .example"; }
[ -f "$CLIENT_ENV" ] || { cp "$CLIENT_DIR/.env.example" "$CLIENT_ENV"; echo "• creado ${CLIENT_ENV#"$ROOT"/} desde el .example"; }

# 2. DB
echo "▶ DB (docker compose up -d --wait db)"
load_server_env  # exporta DB_PORT para que compose mapee el mismo puerto que lee el back
db_up >"$LOGS/db.log" 2>&1 || { echo "❌ la DB no levantó: ver .dev-logs/db.log"; exit 1; }

# 3. Back
echo "▶ back: ./mvnw spring-boot:run (log: .dev-logs/server.log)"
start_group "$SERVER_DIR" "$LOGS/server.log" ./mvnw -B spring-boot:run
wait_http http://localhost:8080/actuator/health 180 || { echo "❌ el back no respondió en 180 s: tail .dev-logs/server.log"; exit 1; }
"$ROOT/scripts/seed-demo.sh" http://localhost:8080

# 4. Front
echo "▶ front: npm run dev (log: .dev-logs/client.log)"
if [ ! -d "$CLIENT_DIR/node_modules" ]; then (cd "$CLIENT_DIR" && npm ci --no-audit --no-fund >"$LOGS/client-install.log" 2>&1); fi
start_group "$CLIENT_DIR" "$LOGS/client.log" npm run dev -- --port 5173 --strictPort
wait_http http://localhost:5173 60 || { echo "❌ el front no respondió en 60 s: tail .dev-logs/client.log"; exit 1; }

cat <<MSG

✅ Entorno listo
   Front:   http://localhost:5173   (⚠️ hoy llama a la API de producción: T-004)
   Back:    http://localhost:8080   · Swagger: http://localhost:8080/swagger-ui
   DB:      localhost:${DB_PORT} (clonair / clonair_local_only)
   Usuario demo: demo-owner@clonair.local / Demo1234!
   Ctrl+C para cortar back y front.
MSG
wait
