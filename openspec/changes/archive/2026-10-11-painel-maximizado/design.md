# Painel maximizado comandado pela Presenter

> Status: confirmed by clayton, 2026-10-11

## Situation

- Projeto: componente biblioteca (MVP Passive View).
- Decisao: ja comprometida - o painel inicia sempre maximizado e a Presenter
  comanda a View.
- Em voo: `IPainelPedidoView` (abstracao) e `PainelPedidoPanel`/`PainelPedidoView`
  sao o precedente.

## Problem

Correcao. A maximizacao esta dentro do construtor da View; a View nao deve
decidir apresentacao por conta propria.

## Success

- **Worked if:** a Presenter comanda `maximizar()` e a janela inicia maximizada;
  a View nao se maximiza sozinha.
- **Review:** apos o build completo - clayton.

## Boundary

- **In:** `maximizar()` na abstracao da View; chamada pela Presenter; remocao do
  `setExtendedState` do construtor da janela; teste.
- **Out:** demais decisoes visuais.
- **Inalterado:** BR12/BR13, filtros, ordenacao, SLA, notificacao, ingestao,
  listeners, instancia unica.

## Shape

A abstracao `IPainelPedidoView` ganha `maximizar()`; a Presenter chama esse
metodo ao iniciar. A implementacao de janela apenas executa
`setExtendedState(MAXIMIZED_BOTH)` quando comandada, e o painel embedavel delega
a maximizacao a janela que o hospeda. A porta de mao unica e a assinatura de
`IPainelPedidoView`.

## Key decisions

1. **`maximizar()` na View, comandada pela Presenter.** A View permanece
   passiva.
2. **A janela nao se maximiza no construtor.** So quando recebe o comando.
3. **Painel embedavel delega** a maximizacao a janela hospedeira.
4. **Maximizar antes de exibir e sem reposicionar depois.** O comando vem antes
   de `setVisible`; nao ha `setLocationRelativeTo` depois (ele desfaz o
   maximizado no Windows).

## Work

| Fatia | Entrega quando pronta | Status |
|---|---|---|
| [Maximizar](#maximizar) | `maximizar()` na View; Presenter comanda. | clear |
| [Teste](#teste) | Presenter chama `maximizar()` no inicio. | clear |
