# Proposta - Componente reutilizavel de Painel de Acompanhamento de Pedidos

## Por que

O projeto e hoje uma POC executavel: para reusar o painel, um sistema de
delivery precisa fazer fork/clone do repositorio e cortar simulacao, seed e
`main` na mao. Nao existe API para injetar pedidos nem para receber um "novo
pedido" de uma origem real.

## O que muda

- O projeto vira **biblioteca Maven** reutilizavel, empacotada como jar que
  contem todas as suas dependencias, para consumo como dependencia (JitPack).
- **Saem**: a simulacao de novos pedidos, os dados mockados, o seed e o `.env`.
- **Entram**: configuracao por `painel_pedidos.env`; porta de dados
  `IPedidoRepository`; porta/leitura de novos pedidos `IFonteNovoPedido` +
  `LeitorNovoPedidoService` (GoF Observer) com empurrao por
  `receberNovosPedidos(...)`; fachada `PainelPedidos`; View embedavel
  (`PainelPedidoPanel`); projeto de exemplo.
- Empacotamento multi-modulo: `painel-pedidos-parent` + `painel-pedidos`
  (biblioteca) + `exemplo` (consumidor), Java 21.

## Impacto

- **Consumidores** passam a depender do jar e a implementar as portas.
- **Requisitos preservados**: BR12/BR13 (GoF State), filtros de periodo,
  ordenacao por prioridade/FIFO, SLA, notificacao (CoR adaptado), instancia
  unica e diretrizes visuais.
- **Requisitos removidos**: US23 (simulacao de novos pedidos) e a configuracao
  por `.env`.
- **Contrato de dados**: o consumidor usa `Pedido` e implementa
  `IPedidoRepository`.

## Alternativas consideradas

- **Flag para desligar seed/simulacao** - mais barato, mas nao resolve: sem
  porta de ingestao o painel fica sem fonte de "novo pedido".
- **Publicar o jar da aplicacao como esta** - mantem simulacao/seed e nao
  oferece API de integracao.
- **Portas de comando por acao** (sistema do consumidor como fonte da verdade)
  - so vence se o painel deixar de ser escritor do estado; nao e o caso hoje.
