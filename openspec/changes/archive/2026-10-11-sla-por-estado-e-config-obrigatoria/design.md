# SLA por estado e configuracao obrigatoria

> Status: confirmed by clayton, 2026-10-11

## Situation

- Projeto: componente biblioteca; sem consumidor real.
- Decisao: ja comprometida - SLA fixo por estado em minutos; `.env` obrigatorio.
- Em voo: o CoR adaptado do SLA (`MonitorSlaService` + `RegraSla*Handler`) e o
  `LeitorConfiguracaoPainelPedido` sao o precedente.

## Problem

Correcao. O SLA de Pronto/Em rota depende de uma media por bairro em vez de um
valor fixo por estado; e a configuracao e opcional com defaults, o que permite
subir o componente sem `.env`.

## Success

- **Worked if:** os quatro estados tem SLA em minutos no `.env`; sem o arquivo,
  o componente lanca excecao.
- **Review:** apos o build completo - clayton.

## Boundary

- **In:** SLAs fixos por estado em minutos; `.env` obrigatorio; remocao da media
  por bairro e do `toString` de `PedidoAbstract`; nomes no singular.
- **Out:** telas do cliente; persistencia real.
- **Inalterado:** BR12/BR13, filtros, ordenacao, notificacao, ingressao,
  listeners, instancia unica.

## Shape

Cada estado com SLA passa a ter um limite fixo em minutos lido do
`painel_pedidos.env`; o `LeitorConfiguracaoPainelPedido` torna o arquivo e as
quatro chaves de SLA obrigatorios. O SLA por estado vive num unico mapa
(`situacao -> duracao`) em `ConfiguracaoPainelPedido` e e aplicado por um unico
`RegraSlaEstadoHandler` generico, no lugar dos quatro handlers quase identicos:
um SLA novo e uma entrada no mapa, sem nova classe nem construtor maior (OCP). A
media por bairro deixa de existir. A porta de mao unica e a assinatura de
`ConfiguracaoPainelPedido`.

## Key decisions

1. **SLA fixo por estado, em minutos.** Um unico `Map<String, Duration>`
   (situacao -> duracao) em `ConfiguracaoPainelPedido`; novos SLAs sao novas
   entradas, sem alterar o construtor (OCP).
2. **Um handler generico.** `RegraSlaEstadoHandler` consome o mapa; substitui os
   quatro handlers quase identicos. Novo SLA = nova entrada, sem nova classe.
3. **Sem media por bairro.** Removidos `ITempoEntregaService`,
   `TempoEntregaService` e `PedidoAbstract.getBairroEntrega()`.
4. **`.env` obrigatorio.** Arquivo ausente ou chave de SLA ausente lanca
   excecao.
5. **Sem `toString` em `PedidoAbstract`.**
6. **Nomes no singular** nos filtros restantes.

## Work

| Fatia | Entrega quando pronta | Status |
|---|---|---|
| [SLA por estado](#sla-por-estado) | 4 SLAs em minutos; handlers e contexto ajustados. | clear |
| [Config obrigatoria](#config-obrigatoria) | `.env` e chaves de SLA obrigatorios. | clear |
| [Limpeza](#limpeza) | Sem media por bairro; sem `toString`. | clear |
| [Nomes](#nomes) | Filtros no singular. | clear |
| [Docs](#docs) | README e OpenSpec atualizados. | clear |
