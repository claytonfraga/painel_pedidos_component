# SLA por estado em minutos e configuracao obrigatoria

## Por que

Hoje o SLA de "Pronto para entrega" e "Em rota de entrega" vem de uma media por
bairro (`ITempoEntregaService`), e nao de um valor fixo por estado; e o
`painel_pedidos.env` e opcional (defaults no codigo). O componente deve ter um
SLA fixo, em minutos, para cada estado, e a configuracao deve ser obrigatoria no
uso pelo cliente.

## O que muda

- **SLA fixo por estado, em minutos**, no `painel_pedidos.env`:
  `SLA_NOVO_MINUTOS`, `SLA_EM_PREPARO_MINUTOS`,
  `SLA_PRONTO_PARA_ENTREGA_MINUTOS`, `SLA_EM_ROTA_DE_ENTREGA_MINUTOS`.
- Remove a media por bairro: saem `ITempoEntregaService`,
  `TempoEntregaService`, `SLA_ENTREGA_BAIRRO_MINUTOS` e
  `PedidoAbstract.getBairroEntrega()`.
- **`painel_pedidos.env` obrigatorio**: ausente, o componente lanca excecao; as
  chaves de SLA por estado tambem sao obrigatorias.
- **Sem `toString` em `PedidoAbstract`.**
- Nomes de classe/filtro no singular (`FiltroCompleto`, `FiltroSemana`,
  `FiltroMes`).

## Impacto

- Consumidores passam a informar os quatro SLAs em minutos no `.env`, que passa a
  ser obrigatorio.
- Preservados: BR12/BR13, filtros, ordenacao, notificacao, ingestao, listeners e
  instancia unica.

## Alternativas consideradas

- **Manter a media por bairro**: conflita com "SLA fixo por estado, em minutos".
- **Manter `.env` opcional**: conflita com "configuracao obrigatoria".
