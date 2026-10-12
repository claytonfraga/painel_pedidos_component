#!/usr/bin/env bash
#
# Pipeline de CI/CD local (mesmo comando do GitHub Actions: mvn -U clean verify).
#
set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$RAIZ"

if command -v mvn >/dev/null 2>&1; then
  MVN="mvn"
elif [ -x "$HOME/.local/opt/apache-maven-3.9.9/bin/mvn" ]; then
  MVN="$HOME/.local/opt/apache-maven-3.9.9/bin/mvn"
else
  echo "Maven nao encontrado (instale mvn ou ajuste o caminho neste script)." >&2
  exit 1
fi

echo "== Build e validacao: mvn -U clean verify =="
"$MVN" -U clean verify

echo
echo "Artefato(s) gerado(s):"
ls -1 target/*.jar 2>/dev/null || echo "  (nenhum jar encontrado)"
