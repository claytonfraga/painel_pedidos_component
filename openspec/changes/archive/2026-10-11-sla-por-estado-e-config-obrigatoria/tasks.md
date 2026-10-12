# Tarefas - SLA por estado e configuracao obrigatoria

## 1. OpenSpec

- [x] Change `2026-10-11-sla-por-estado-e-config-obrigatoria`.
- [x] Atualizar `spec.md`, `AGENTS.md`, `project.md`, `prd.md`, `us22` e `us24`.

## 2. SLA por estado (minutos)

- [x] `ConfiguracaoPainelPedido`: `slaNovo`, `slaEmPreparo`,
      `slaProntoParaEntrega`, `slaEmRotaDeEntrega` (Duration).
- [x] `LeitorConfiguracaoPainelPedido`: ler as quatro chaves de SLA.
- [x] `RegraSlaProntoParaEntregaHandler` e `RegraSlaEmRotaDeEntregaHandler`
      recebem `Duration`.
- [x] SLA por estado num unico `Map<String, Duration>` e `RegraSlaEstadoHandler`
      generico (OCP); `RegraSlaNovoHandler`/`EmPreparo`/`Pronto`/`EmRota`
      removidos.
- [x] `ContextoSla` e `AgendadorMonitorSlaService` sem `ITempoEntregaService`.
- [x] `PainelPedido` monta as quatro regras com o SLA do estado.

## 3. Config obrigatoria

- [x] `LeitorConfiguracaoPainelPedido` lanca excecao se o `.env` faltar.
- [x] Chaves de SLA por estado obrigatorias.

## 4. Limpeza

- [x] Remover `ITempoEntregaService`, `TempoEntregaService`,
      `SLA_ENTREGA_BAIRRO_MINUTOS` e `PedidoAbstract.getBairroEntrega()`.
- [x] Remover `toString` de `PedidoAbstract`.

## 5. Nomes

- [x] `FiltroTodos` -> `FiltroCompleto`.
- [x] `FiltroUltimos7Dias` -> `FiltroSemana`.
- [x] `FiltroUltimos30Dias` -> `FiltroMes`.

## 6. Configuracao e docs

- [x] `painel_pedidos.env.example` e `exemplo/painel_pedidos.env` com os SLAs.
- [x] README (raiz e `painel-pedidos`) atualizados.

## 7. Testes e build

- [x] `LeitorConfiguracaoPainelPedidoTest`: arquivo obrigatorio + chaves.
- [x] Ajustar testes que usam `ConfiguracaoPainelPedido` (10 campos).
- [x] `mvn -U clean verify` verde e sincronizacao.
