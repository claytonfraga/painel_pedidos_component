# Tarefas - Pedido abstrato e acoes configuraveis do cliente

## 1. OpenSpec

- [x] Change `2026-10-11-pedido-abstract-e-listeners` (proposal, design, spec
      delta, tasks).
- [x] Atualizar `project.md`, `prd.md`, `spec.md`, `AGENTS.md` e features
      (US21, US28).

## 2. Modelo abstrato

- [x] Renomear `model/Pedido` para `model/PedidoAbstract` (abstrata).
- [x] `PedidoAbstract` guarda os essenciais do painel + GoF State; declara
      `criarCopia` abstrata.
- [x] `PedidoState` e concretos passam a referenciar `PedidoAbstract`.
- [x] Atualizar `IPedidoRepository`, `IFonteNovoPedido`, notificacao, SLA,
      ordenacao e Presenter para `PedidoAbstract`.

## 3. Listeners

- [x] Criar `IVisualizarPedidoListener` e `IConfirmarPagamentoListener`.
- [x] Presenter aciona os listeners; ausentes, lanca excecao.

## 4. Fachada

- [x] Renomear `PainelPedidos` para `PainelPedido` (singular).
- [x] `usarVisualizarPedidoListener` / `usarConfirmarPagamentoListener`.
- [x] `iniciar()` valida os listeners antes de criar a janela.

## 5. Fechar

- [x] "Fechar" apenas encerra o painel (`dispose`); nunca encerra a aplicacao.

## 6. Exemplo

- [x] `PedidoExemplo extends PedidoAbstract`.
- [x] Listeners de exemplo (`VisualizacaoPedidoExemplo`,
      `ConfirmacaoPagamentoExemplo`) com `JOptionPane`.
- [x] `ExemploPainelPedidos` liga os listeners.

## 7. Nomes

- [x] Sem plural em classes/interfaces (`PainelPedido`,
      `IAcaoNotificacaoNovoPedido`).
- [x] Sufixo `Service` nos servicos (`AgendadorMonitorSlaService`).

## 8. README do modulo

- [x] `painel-pedidos/README.md` explicando o uso.

## 9. Testes

- [x] `PedidoDeTeste extends PedidoAbstract` (apoio de teste).
- [x] Presenter: acao sem listener lanca; com listener funciona.
- [x] Fachada: `iniciar()` sem listeners lanca.
- [x] Ajustar testes existentes para `PedidoAbstract`.

## 10. Build e sincronizacao

- [x] `mvn -U clean verify` verde.
- [x] Sincronizar para `/mnt/c/Users/clayt/Downloads/painel_pedidos_mvp`.
