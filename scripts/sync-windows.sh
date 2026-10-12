#!/usr/bin/env bash
#
# Sincroniza o projeto para o WSL/Windows (Downloads) e remove desktop.ini.
#
set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DESTINO="/mnt/c/Users/clayt/Downloads/painel_pedidos_mvp"

mkdir -p "$DESTINO"

rsync -a --delete --no-perms --no-owner --no-group \
  --exclude '.git/' \
  --exclude 'target/' \
  --exclude 'docs/' \
  --exclude 'desktop.ini' \
  --exclude '.opencode/' \
  --exclude '.claude/' \
  --exclude '.cursor/' \
  --exclude '.windsurf/' \
  --exclude '.agents/' \
  --exclude '.gemini/' \
  --exclude '.codex/' \
  --exclude '.github/' \
  "$RAIZ"/ "$DESTINO"/

for pasta in "$DESTINO" \
  /mnt/c/Users/clayt/Downloads/_PainelPedidos_MVP_PassiveView \
  /mnt/c/Users/clayt/Downloads/_PainelPedidos_MVP_PassiveView_Revisado; do
  [ -d "$pasta" ] && find "$pasta" -iname "desktop.ini" -type f -delete 2>/dev/null || true
done

echo "Sincronizado com $DESTINO"
echo "desktop.ini restantes: $(find "$DESTINO" -iname desktop.ini 2>/dev/null | wc -l)"
