# Pedido abstrato e acoes configuraveis do cliente

> Design da mudanca. Plano a partir deste documento.
> Status: confirmed by clayton, 2026-10-11

## Situation

- Projeto: componente ja extraido (biblioteca multi-modulo), sem consumidor real
  ainda.
- Decisao: **ja comprometida** por clayton - o modelo de pedido deve virar
  abstrato e as acoes de visualizar/pagamento devem ser do cliente.
- Em voo: o GoF State (`PedidoState`/`Pedido*State`) e o contrato
  `IPedidoRepository` sao o precedente; as regras de codigo sao restricao.
- O que esta em jogo: muda a API publica do modelo e a forma de estender as
  acoes; sem consumidor, ainda e barato decidir.

## Problem

Acoplamento. O componente traz um `Pedido` concreto e executa, por conta propria,
a tela de visualizacao e o fluxo de pagamento (via `JOptionPane`). Isso obriga o
componente a conhecer dados e telas que pertencem ao sistema consumidor.

- **Quem e prejudicado:** o sistema de delivery, que precisa que a tela do
  pedido e a de pagamento sejam as suas, lendo os seus dados.
- **Substituto hoje:** fork do componente para remover o `Pedido` concreto e o
  `JOptionPane`.
- **Numero que mudaria a decisao:** quantos consumidores dependem do `Pedido`
  concreto hoje. Resposta: zero.

## Success

- **Worked if:** um consumidor estende `PedidoAbstract` e implementa os dois
  listeners; sem eles, `iniciar()` e as acoes lancam excecao.
- **Sinal cedo:** testes verdes cobrindo os dois caminhos (configurado e nao
  configurado); o exemplo roda com `PedidoExemplo` e listeners proprios.
- **Review:** apos o build completo - clayton.

## Boundary

- **In:** `PedidoAbstract`; listeners de visualizacao e pagamento; validacao em
  `iniciar()`; README do modulo `painel-pedidos`; testes.
- **Out:** telas completas de pedido/pagamento (ficam no cliente);
  persistencia real.
- **Inalterado:** BR12/BR13 (State), filtros, ordenacao, SLA, notificacao,
  ingestao, instancia unica.

## Shape

O componente passa a ter, no lugar de um `Pedido` concreto, uma classe
**abstrata `PedidoAbstract`** que carrega apenas o essencial do painel e a
maquina de estados GoF State; o cliente **estende** essa classe. As acoes de
"Visualizar pedido" e "Confirmar pagamento" saem do componente e viram dois
**listeners obrigatorios** (`IVisualizarPedidoListener` e
`IConfirmarPagamentoListener`); sem eles `iniciar()` lanca excecao, e as acoes
tambem. A porta de mao unica e a assinatura de `PedidoAbstract` (o cliente
implementa `criarCopia`); a alternativa mais pesada - interface em vez de classe
abstrata - obrigaria a reimplementar o State no cliente, o que nao se justifica.

## Key decisions

1. **`PedidoAbstract` e classe abstrata, nao interface.** Reaproveita a maquina
   de estados ja implementada; o cliente estende e implementa apenas
   `criarCopia`.
2. **O componente nao tem pedido concreto.** Nenhuma classe `model.Pedido`
   concreta permanece.
3. **Acoes do cliente por listener obrigatorio.** `IVisualizarPedidoListener` e
   `IConfirmarPagamentoListener`; ausentes, `iniciar()` lanca e a acao lanca.
4. **O componente nao abre janelas de visualizacao nem de pagamento.** Quem as
   exibe e o cliente.
5. **Fechar fecha apenas o painel.** O botao "Fechar" encerra a janela do painel
   (`dispose`), nunca encerra a aplicacao.
6. **Nomes no singular e sufixo de papel.** Classes/interfaces sem plural
   (`PainelPedido`, `IAcaoNotificacaoNovoPedido`) e classes de servico com
   sufixo `Service` (`AgendadorMonitorSlaService`).
7. **Validacao no ponto de inicio.** `iniciar()` valida os listeners
   obrigatorios antes de criar a janela (falha barata e testavel).

## Work

| Fatia | Entrega quando pronta | Status |
|---|---|---|
| [Modelo abstrato](#modelo-abstrato) | `PedidoAbstract` + State usando-a; nenhum `Pedido` concreto. | clear |
| [Listeners](#listeners) | `IVisualizarPedidoListener` e `IConfirmarPagamentoListener`; Presenter aciona. | clear |
| [Fachada](#fachada) | `PainelPedido` valida em `iniciar()` e liga as acoes. | clear |
| [Fechar](#fechar) | "Fechar" so encerra o painel. | clear |
| [Exemplo](#exemplo) | `PedidoExemplo` + listeners de exemplo. | clear |
| [README do modulo](#readme-do-modulo) | `painel-pedidos/README.md` de uso. | clear |
| [Testes](#testes) | Caminhos configurado/nao configurado e modelo. | clear |

Ordem: Modelo abstrato -> Listeners -> Fachada -> Fechar -> Exemplo -> README ->
Testes.

### Modelo abstrato

**Entrega** a hierarquia abstrata. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Componente | Sem classe `Pedido` concreta | so `PedidoAbstract` |
| Cliente | Estende `PedidoAbstract` e implementa `criarCopia` | tipo concreto proprio |

### Listeners

**Entrega** as portas de acao. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Sem listener | Acao lanca excecao | mensagem clara |
| Com listener | Cliente exibe sua tela | comportamento do cliente |

### Fachada

**Entrega** `PainelPedido`. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Sem listeners | `iniciar()` lanca antes de criar a janela | painel nao abre |
| Com listeners | Painel abre e acoes delegam | painel do cliente |

### Fechar

**Entrega** o comportamento de fechamento. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Fechar | Apenas o painel e encerrado | aplicacao segue viva |

### Exemplo

**Entrega** o consumidor de demonstracao. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Inicio | `PedidoExemplo` + listeners proprios | painel funcional |

### README do modulo

**Entrega** o guia de uso. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Leitura | Passos de uso do componente | guia de integracao |

### Testes

**Entrega** a garantia. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Nao configurado | Excecao | `IllegalStateException` |
| Configurado | Listener acionado | pedido entregue a acao |
