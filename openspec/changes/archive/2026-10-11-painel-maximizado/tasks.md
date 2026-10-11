# Tarefas - Painel maximizado comandado pela Presenter

## 1. OpenSpec

- [x] Change `2026-10-11-painel-maximizado`.
- [x] Atualizar `spec.md`, `design.md` e `us21`.

## 2. View

- [x] `IPainelPedidoView.maximizar()`.
- [x] `PainelPedidoView`: remover `setExtendedState` do construtor; implementar
      `maximizar()`.
- [x] `PainelPedidoPanel.maximizar()`: maximiza a janela hospedeira.

## 3. Presenter

- [x] `PainelPedidoPresenter.iniciar()` chama `painelPedidoView.maximizar()`.
- [x] `PainelPedidoFacade.iniciar()` chama `painelPedidoPresenter.iniciar()`.

## 4. Teste e build

- [x] Teste: a Presenter comanda `maximizar()` ao iniciar.
- [x] `mvn -U clean verify` verde e sincronizacao.
