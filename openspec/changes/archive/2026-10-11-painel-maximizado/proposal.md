# Painel sempre inicia maximizado (Presenter comanda a View)

## Por que

Hoje a janela se maximiza sozinha no construtor da View
(`PainelPedidoJanela` chama `setExtendedState`), misturando uma decisao de
apresentacao com a View e ferindo a passividade do MVP. O painel deve **sempre**
iniciar maximizado, e esse comando deve vir da Presenter.

## O que muda

- `IPainelPedidoView` ganha `maximizar()`; a Presenter chama esse metodo ao
  iniciar.
- `PainelPedidoJanela` deixa de se maximizar no construtor; passa a executar a
  maximizacao **quando comandada**.
- `PainelPedidoView` executa a maximizacao na janela que o hospeda (segue
  embedavel).

## Impacto

- Comportamento visual: o painel sempre abre maximizado.
- A maximizacao passa a ser comandada pela Presenter (View passiva).

## Alternativas consideradas

- **Manter `setExtendedState` na View**: viola a passividade da View e o MVP
  Passive View adotado.
