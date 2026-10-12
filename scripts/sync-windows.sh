#!/usr/bin/env bash
#
# Sincroniza o projeto para o WSL/Windows (Downloads) e remove desktop.ini.
#
set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DESTINO="/mnt/c/Users/clayt/Downloads/PainelPedidos_MVP_PassiveView"

mkdir -p "$DESTINO"
find "$DESTINO" -mindepth 1 -delete 2>/dev/null || true

copiar() {
  [ -e "$RAIZ/$1" ] && cp -r "$RAIZ/$1" "$DESTINO/"
}

copiar pom.xml
copiar src
copiar openspec
copiar scripts
copiar .github
copiar .gitignore
copiar .env.example
copiar .env

for pasta in "$DESTINO" \
  /mnt/c/Users/clayt/Downloads/_PainelPedidos_MVP_PassiveView \
  /mnt/c/Users/clayt/Downloads/_PainelPedidos_MVP_PassiveView_Revisado; do
  [ -d "$pasta" ] && find "$pasta" -iname "desktop.ini" -type f -delete 2>/dev/null || true
done

echo "Sincronizado com $DESTINO"
echo "desktop.ini restantes: $(find "$DESTINO" -iname desktop.ini 2>/dev/null | wc -l)"
