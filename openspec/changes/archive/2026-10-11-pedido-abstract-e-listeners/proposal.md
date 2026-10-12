# Proposta - Pedido abstrato e ações configuráveis do cliente

## Por que

Hoje o componente traz uma classe concreta `model.Pedido` e executa, por conta
própria, a visualização do pedido e o fluxo de pagamento (via `JOptionPane`).
Isso acopla o componente a dados e a telas que pertencem ao sistema consumidor:
a tela do pedido e a tela de pagamento são do cliente e leem os dados do próprio
sistema.

## O que muda

- **Modelo**: sai a classe concreta `Pedido`; entra a classe **abstrata
  `PedidoAbstract`**, que carrega apenas o essencial do painel e a máquina de
  estados (GoF State). O cliente **estende** `PedidoAbstract`; o componente não
  tem pedido concreto.
- **Ação de visualizar pedido**: passa a ser obrigatoriamente configurada pelo
  cliente via **listener** `IVisualizarPedidoListener`; o cliente exibe a sua
  própria tela do pedido.
- **Ação de confirmar pagamento**: idem, via **listener
  `IConfirmarPagamentoListener`**; o cliente conduz o seu próprio fluxo de
  pagamento.
- Componente **não define** mais janelas de visualização nem de pagamento.
- **README próprio** no módulo `painel-pedidos` explicando o uso.

## Impacto

- Consumidores estendem `PedidoAbstract` e implementam os dois listeners.
- Sem os listeners, `iniciar()` lança exceção; sem implementá-los, as ações
  lançam exceção.
- Preservados: BR12/BR13 (State), filtros, ordenação, SLA, notificação, ingestão
  e instância única.

## Alternativas consideradas

- **Manter `Pedido` concreto**: mantém dados e telas do cliente dentro do
  componente.
- **Interface em vez de classe abstrata**: obrigaria a reimplementar a máquina de
  estados no cliente; a classe abstrata reaproveita o State já existente.
