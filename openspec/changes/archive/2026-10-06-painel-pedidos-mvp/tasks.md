# Tasks: Painel de Acompanhamento de Pedidos (MVP Passive View)

## Fundacao

- [x] Model `Pedido` (dados, pagamento, credito, prioridade, tempo no estado)
- [x] GoF State do ciclo de vida (`PedidoState` + estados concretos)
- [x] `IPedidoRepository` + `PedidoEmMemoriaRepository`
- [x] View passiva `IPainelPedidoView` + `PainelPedidoView` (NetBeans)
- [x] Presenter `PainelPedidoPresenter` (MVP Passive View)

## US21 - Acompanhar pedidos

- [x] 6 colunas com contagem; tela maximizada
- [x] Card com dados minimos e status; visualizacao detalhada
- [x] Busca por numero/cliente (Enter ou botao)
- [x] Acoes por estado; ocultar "Executar acao" sem acao
- [x] Cancelamento (sem/com pagamento e Entregue); criar novo a partir de Cancelado
- [x] Selecao por um clique e navegacao por Tab

## US22 - Filtrar por periodo e data

- [x] Filtros padrao (`IFiltroPeriodo`/`IFabricaFiltroPeriodo`)
- [x] Data especifica com datepicker exibido so nessa opcao
- [x] `IFonteDataPedido` define a data dos novos pedidos

## US23 - Simular novos pedidos

- [x] `SimuladorNovoPedido` (1..N por ciclo, intervalo aleatorio)
- [x] Chain of Responsibility adaptado (`RegraHandler` + `*Handler` + `Service`)
- [x] Observer de novos pedidos (`NovoPedidoObserver`/`PainelPedidoNovoPedidoObserver`)
- [x] Alerta sonoro (Strategy `IEstrategiaSom`), mensagem e destaque piscante

## US24 - Prioridade, SLA e ordenacao

- [x] SLA por estado (Novo, Em preparo, Pronto/Em rota pela media do bairro)
- [x] `MonitorSlaService` + `RegraSla*Handler` + `AgendadorMonitorSla`
- [x] Observer de prioridade; label vermelho removido ao mudar de estado
- [x] ~5% dos Novo do seeder priorizados
- [x] Ordenacao por tempo no estado (`IOrdenacaoPedidos`)

## US25 - Instancia unica

- [x] `InstanciaUnicaAplicacao` (Singleton thread-safe) + `ITravaInstanciaUnica`

## Documentacao

- [x] PRD (`prd.md`), especificacao (`spec.md`), design (`design.md`)
- [x] Features Gherkin US21..US25 no padrao (`# language`, `# Fontes`, tags, IDs)
- [x] `README.md` do OpenSpec e indice de historias

## Validacao

- [x] `mvn -U clean verify` sem erros
- [x] Restricoes verificadas (sem lambda/Stream/enum/instanceof/Runnable;
      sem atributos estaticos exceto Singleton)
