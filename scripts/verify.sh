#!/usr/bin/env bash
# EL gate rápido: build (clean) + lint + tests de cada parte. Sale ≠ 0 si algo falla.
# Uso: ./scripts/verify.sh [--server] [--client]   (sin opciones: ambas partes)
# Detalle en .verify-logs/ (ignorado por git).
set -uo pipefail
. "$(dirname "$0")/lib.sh"

DO_SERVER=0 DO_CLIENT=0
for a in "$@"; do
  case $a in
    --server) DO_SERVER=1 ;;
    --client) DO_CLIENT=1 ;;
    -h|--help) sed -n 2,4p "$0"; exit 0 ;;
    *) echo "opción desconocida: $a"; exit 2 ;;
  esac
done
[ $DO_SERVER = 0 ] && [ $DO_CLIENT = 0 ] && DO_SERVER=1 DO_CLIENT=1

LOGS="$ROOT/.verify-logs"; mkdir -p "$LOGS"
FAIL=0 SUMMARY=()

verify_server() {
  local log="$LOGS/server.log" s="server:" jdk
  jdk=$(java -version 2>&1 | grep -oE 'version "[0-9]+' | grep -oE '[0-9]+$' || echo "?")
  echo "▶ server: ./mvnw clean verify (Testcontainers; log: .verify-logs/server.log)"
  (cd "$SERVER_DIR" && ./mvnw -B clean verify) >"$log" 2>&1
  local rc=$?
  local tests
  tests=$(grep -E '^\[(INFO|WARNING|ERROR)\] Tests run:' "$log" | tail -1 | sed -E 's/.*Tests run: ([0-9]+), Failures: ([0-9]+), Errors: ([0-9]+), Skipped: ([0-9]+).*/\1 \2 \3 \4/')
  if [ $rc -eq 0 ]; then
    s+=" ✅ build ✅ tests (${tests%% *}) ✅ cobertura"
  elif grep -q 'Coverage checks have not been met' "$log"; then
    s+=" ✅ build ✅ tests (${tests%% *}) ❌ cobertura"; FAIL=1
    grep -m3 'Rule violated' "$log" | sed 's/^/    /'
  elif grep -q 'COMPILATION ERROR\|release version\|invalid target release' "$log"; then
    s+=" ❌ build"; FAIL=1
    grep -m3 -E 'ERROR.*\.java|release version|invalid target' "$log" | sed 's/^/    /'
    echo "    pista: el proyecto pide Java 17+ (pom.xml: java.version). Tenés JDK $jdk. Ver README → Prerrequisitos."
  else
    read -r n f e _ <<<"${tests:-0 0 0 0}"
    s+=" ✅ build ❌ tests ($((f + e)) de $n fallan)"; FAIL=1
    grep -E '<<< (FAILURE|ERROR)!' "$log" | head -5 | sed 's/^/    /'
  fi
  SUMMARY+=("$s [JDK $jdk]")
}

verify_client() {
  local s="client:" rc
  echo "▶ client: npm ci + lint + build (+ test si existe) (logs: .verify-logs/client-*.log)"
  cd "$CLIENT_DIR" || return
  if [ ! -d node_modules ] || [ package-lock.json -nt node_modules ]; then
    npm ci --no-audit --no-fund >"$LOGS/client-install.log" 2>&1 || { SUMMARY+=("client: ❌ npm ci (ver .verify-logs/client-install.log)"); FAIL=1; return; }
  fi
  npm run lint >"$LOGS/client-lint.log" 2>&1; rc=$?
  if [ $rc -eq 0 ]; then s+=" ✅ lint"; else
    s+=" ❌ lint ($(grep -oE '[0-9]+ problems' "$LOGS/client-lint.log" | head -1))"; FAIL=1; fi
  npm run build >"$LOGS/client-build.log" 2>&1; rc=$?
  if [ $rc -eq 0 ]; then s+=" ✅ build"; else s+=" ❌ build"; FAIL=1; tail -5 "$LOGS/client-build.log" | sed 's/^/    /'; fi
  if npm run | grep -qE '^  test$'; then
    npm test >"$LOGS/client-test.log" 2>&1; rc=$?
    if [ $rc -eq 0 ]; then s+=" ✅ tests"; else s+=" ❌ tests"; FAIL=1; fi
  else
    s+=" ⚠️ sin tests (T-009)"
  fi
  SUMMARY+=("$s [Node $(node -v)]")
  cd "$ROOT" || return
}

START=$SECONDS
[ $DO_SERVER = 1 ] && verify_server
[ $DO_CLIENT = 1 ] && verify_client
echo
OUT=""; for x in "${SUMMARY[@]}"; do OUT+="${OUT:+ · }$x"; done; echo "$OUT"
echo "($((SECONDS - START)) s) $([ $FAIL = 0 ] && echo '✅ VERIFY OK' || echo '❌ VERIFY FALLÓ')"
exit $FAIL
