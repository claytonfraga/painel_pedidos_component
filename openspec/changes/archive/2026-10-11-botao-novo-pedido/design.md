# Botao "Novo Pedido"

> Status: confirmed by clayton, 2026-10-11

## Situation

- Projeto: componente biblioteca (MVP Passive View), com dois listeners
  obrigatorios do cliente (`IVisualizarPedidoListener` e
  `IConfirmarPagamentoListener`).
- Decisao: ja comprometida - adicionar um botao "Novo Pedido" com listener.

## Problem

Ausencia. Nao ha como criar pedidos pelo painel; o componente nao gera dados
(por design), e o consumidor precisa de um gatilho de UI para inserir pedidos.

## Success

- **Worked if:** o botao "Novo Pedido" aparece a esquerda de "Visualizar pedido";
  o cliente implementa o listener; a quantidade (1..10) e escolhida no exemplo e
  os pedidos aparecem no painel com numero novo e a data atual.
- **Review:** apos o build completo - clayton.

## Boundary

- **In:** botao na View, `ICriarNovoPedidoListener`, ligacao na Presenter,
  implementacao no exemplo, testes.
- **Out:** geracao automatica no componente; quantidade no `.env`.
- **Inalterado:** BR12/BR13, filtros, SLA, notificacao, empurrao, maximizacao.

## Shape

A View ganha o botao; a Presenter o liga ao listener do cliente
(`ICriarNovoPedidoListener.criarNovoPedido()`); os pedidos devolvidos sao salvos
e o painel atualizado. A criacao (quais pedidos, quantos, numeros, data) e do
cliente. A porta de mao unica e a assinatura de `ICriarNovoPedidoListener`.

## Key decisions

1. **Listener obrigatorio** `ICriarNovoPedidoListener.criarNovoPedido()`
   (devolve `List<PedidoAbstract>`), no estilo dos demais; ausente, `iniciar()`
   lanca e a acao tambem.
2. **O componente nao cria pedidos.** Ele so invoca o listener e insere.
3. **A Presenter comanda a View** (liga o botao), como as outras acoes.
4. **Quantidade e limite (10) no cliente de exemplo**, nao no `.env`.
5. **As copias usam `criarCopia`** (novo numero + data atual) e iniciam em
   `Novo` (sem pagamento), copiando cliente/endereco/valor do seeder.

## Work

| Fatia | Entrega quando pronta | Status |
|---|---|---|
| [View](#view) | Botao "Novo Pedido" a esquerda de "Visualizar pedido". | clear |
| [Listener](#listener) | `ICriarNovoPedidoListener` + ligacao na Presenter. | clear |
| [Exemplo](#exemplo) | Listener que copia pedidos do seeder (1..10). | clear |
| [Testes](#testes) | Sem listener lanca; com listener insere. | clear |
