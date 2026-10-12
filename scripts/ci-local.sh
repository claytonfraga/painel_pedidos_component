#!/usr/bin/env bash
#
# Pipeline de CI/CD local (os mesmos comandos do GitHub Actions).
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

echo "== Componente: mvn -U clean verify =="
( cd painel-pedidos && "$MVN" -U clean verify )

echo
echo "Artefato do componente:"
ls -1 painel-pedidos/target/*.jar 2>/dev/null || echo "  (nenhum jar encontrado)"

echo
echo "== Exemplo (consome o artefato publicado) =="
echo "Pulado por padrao: depende da versao publicada no JitPack."
echo "Para construir: ( cd exemplo && \\$MVN -U clean verify )"
