# PRD - Painel de Acompanhamento de Pedidos (componente reutilizavel)

> Status: componente implementado e empacotado como biblioteca.
> Fontes normativas: `specs/painel-acompanhamento-pedidos/spec.md` e os
> `*.feature` da mesma pasta.

## 1. Visao e objetivo

Biblioteca Swing que permite ao atendente de um delivery acompanhar e conduzir os
pedidos por etapas operacionais, cancelar com credito, criar um novo pedido a
partir de um cancelado, filtrar por periodo/data e priorizar/ordenar a fila. O
componente e incorporado por outro sistema como dependencia; a origem dos dados
e a entrada de novos pedidos ficam no sistema consumidor.

## 2. Contexto e problema

- **Personas:** atendente (operacao), administrador e operador.
- **Problema:** reusar o painel hoje exigiria fork/clone do repositorio e corte
  manual de simulacao, seed e `main`; nao havia API para injetar dados nem para
  receber um novo pedido de uma origem real.

## 3. Escopo

**Dentro:**
- Painel com 6 colunas de situacao e acoes por estado (US21).
- Busca, filtro por periodo padrao e por data especifica (US21, US22).
- Ingestao de novos pedidos por **empurrao** (GoF Observer), com alerta sonoro,
  foco, mensagem e destaque (US26).
- Prioridade por SLA e ordenacao em fila (FIFO) por tempo no estado (US24).
- Execucao unica opcional (US25).
- Distribuicao como dependencia Maven (jar com dependencias) e View embedavel
  (US27).

**Fora:**
- Persistencia real (SQL/H2) - fica no consumidor, atras de `IPedidoRepository`.
- Autenticacao real.
- Integracao com gateway de pagamento (apenas registro da forma).
- Publicacao no JitPack (a cargo do mantenedor).

## 4. Regras de negocio

- **BR12 (ciclo de vida):** situacoes Novo, Em preparo, Pronto para entrega, Em
  rota de entrega, Entregue e Cancelado. Novo -> Em preparo somente apos
  pagamento; Em preparo -> Pronto; Pronto -> Em rota; Em rota -> Entregue;
  qualquer estado nao cancelado -> Cancelado; Cancelado nao retorna.
- **BR13 (cancelamento e credito):** cancelamento em qualquer pedido nao
  cancelado, inclusive Entregue; sem pagamento nao gera credito; com pagamento
  gera credito igual ao valor pago; pedido Cancelado permite "Criar novo pedido"
  (novo numero, nova data/hora, situacao Novo, sem pagamento).
- **US23 (pagamento):** a acao de um pedido Novo conduz ao fluxo de pagamento; ao
  confirmar, passa a Em preparo.

## 5. Requisitos funcionais

### US21 - Acompanhar pedidos (`@US21-001`..`018`)
Colunas com contagem e tela maximizada; card com numero, horario, cliente,
endereco, valor, forma de pagamento e status; busca; periodo padrao; transicoes e
acoes por estado; cancelamento; criar novo a partir de Cancelado; ocultar
"Executar acao" quando nao houver acao; selecao com um clique; navegacao por
Tab; visualizar detalhes.

### US22 - Filtrar por periodo e data (`@US22-001`..`004`)
Filtros padrao (Hoje, Ultimos 7 dias, Ultimos 30 dias, Todos) e data especifica;
a data selecionada define tambem a data de novos pedidos.

### US26 - Receber novos pedidos sem simulacao (`@US26-001`..`003`)
O consumidor **empurra** os novos pedidos via
`receberNovosPedidos(List<PedidoAbstract>)` (GoF Observer: consumidor = sujeito,
componente = observador); ao receber: salvar via `IPedidoRepository`, atualizar o
painel, selecionar/focar, alerta sonoro, mensagem por cardinalidade e destaque.
Nenhum pedido e gerado nem lido automaticamente pelo componente.

### US24 - Prioridade, SLA e ordenacao (`@US24-001`..`007`)
SLA por estado (Novo, Em preparo, Pronto, Em rota); label vermelho "Prioridade"
removido ao mudar de estado; ordenacao em fila: prioridade no topo e, dentro de
cada raia, o pedido ha mais tempo no estado no topo.

### US25 - Instancia unica (`@US25-001`)
Singleton thread-safe + trava de arquivo impedem a segunda abertura (capacidade
opcional oferecida ao consumidor).

### US27 - Componente reutilizavel (`@US27-001`..`005`)
Jar com dependencias; configuracao por `painel_pedidos.env`; modulo de exemplo;
View embedavel (`PainelPedidoView`); Look and Feel definido pela aplicacao
cliente.

### US28 - Acoes do cliente e modelo abstrato (`@US28-001`..`004`)
O componente nao possui pedido concreto: expoe `PedidoAbstract` (o cliente
estende). "Visualizar pedido" e "Confirmar pagamento" sao listeners
obrigatorios do cliente; sem eles, excecao. "Fechar" encerra apenas o painel.
Nomes no singular; servicos com sufixo `Service`.

### US29 - Criar novos pedidos pelo painel (`@US29-001`..`003`)
Botao "Novo Pedido" a esquerda de "Visualizar pedido", com listener obrigatorio
`ICriarNovoPedidoListener` (`criarNovoPedido()` devolve `List<PedidoAbstract>`).
O componente salva o retorno e atualiza o painel; quem cria os pedidos e o
cliente. No exemplo, o listener pergunta a quantidade (1 a 10, limite fixo no
codigo) e copia pedidos do seeder com novo numero e a data atual.

## 6. Requisitos nao funcionais

- Java 21, Maven, encoding UTF-8, **sem Spring**; multi-modulo.
- Arquitetura **MVP Passive View**; a View e passiva e a Presenter coordena.
- Padroes: **State**, **Strategy** (filtro, som, ordenacao), **Chain of
  Responsibility adaptado**, **Observer** (ingestao e atualizacoes),
  **Adapter** (dotenv). A instancia unica e do componente (trava de arquivo),
  nao um Singleton do consumidor.
- Restricoes de codigo: sem lambda, Stream, `enum`, `instanceof`, `Runnable`;
  sem atributos estaticos; excecoes unchecked;
  `Objects.requireNonNull`; classes no singular; interfaces com prefixo `I`.
- Configuracao por `painel_pedidos.env` (encapsulado em
  `LeitorConfiguracaoPainelPedido`).
- Persistencia atras de `IPedidoRepository` (implementada pelo consumidor).
- Testes com JUnit Jupiter + Hamcrest.
- Look and Feel definido pela aplicacao cliente (o componente nao o define);
  tela maximizada; foco/teclado (Tab).

## 7. Configuracao (`painel_pedidos.env`)

`SLA_NOVO_MINUTOS`, `SLA_EM_PREPARO_MINUTOS`,
`SLA_PRONTO_PARA_ENTREGA_MINUTOS`, `SLA_EM_ROTA_DE_ENTREGA_MINUTOS` (os quatro
SLAs por estado sao obrigatorios), `SLA_MONITOR_INTERVALO_SEGUNDOS`,
`NOTIFICACAO_DURACAO_SEGUNDOS`, `SOM_ATIVO`, `TITULO_JANELA`. O arquivo e
**obrigatorio** no uso do componente; as demais chaves usam defaults
(documentados em `painel_pedidos.env.example`).

## 8. Metricas de sucesso

- Um consumidor adiciona o jar, implementa `IPedidoRepository` e executa o
  painel sem copiar o repositorio.
- `mvn -U clean verify` verde, com jar gordo gerado e print do painel produzido
  por teste headless.
- Pedidos que ultrapassam o SLA ganham o label "Prioridade" e sobem na raia.
- Novos pedidos lidos/empurrados sao notificados (som, mensagem e destaque).

## 9. Riscos e limitacoes

- Sem persistencia no componente: os dados vivem no consumidor.
- O SLA e fixo por estado, em minutos; nao ha ajuste por bairro.
- A API publica ainda nao tem consumidor real; ajustes sao baratos.

## 10. Rastreabilidade

| Historia | Feature | Principais classes |
|---|---|---|
| US21 | us21-acompanhar-pedidos | `PainelPedidoPresenter`, `Pedido`, `Pedido*State`, `IPedidoRepository`, `IPainelPedidoView`, `PainelPedidoView` |
| US22 | us22-filtro-periodo-data | `IFiltroPeriodo`/`Filtro*`, `IFabricaFiltroPeriodo`, `IFonteDataPedido` |
| US24 | us24-prioridade-sla-ordenacao | `MonitorSlaService`, `RegraSlaEstadoHandler`, `AgendadorMonitorSlaService`, `PrioridadeObserver`, `IOrdenacaoPedido` |
| US25 | us25-instancia-unica | `PainelPedidoFacade`, `TravaInstanciaUnicaArquivo` |
| US26 | us26-ingestao-novos-pedidos | `PainelPedidoFacade.receberNovosPedidos`, `PainelPedidoNovoPedidoObserver`, `NotificacaoNovoPedidoService`, `RegraHandler`+`*Handler`, `IEstrategiaSom` |
| US27 | nao-funcional | `PainelPedidoFacade`, `ConfiguracaoPainelPedido`, `LeitorConfiguracaoPainelPedido`, `PainelPedidoView` |
| US28 | us28-acoes-do-cliente | `PedidoAbstract`, `IVisualizarPedidoListener`, `IConfirmarPagamentoListener`, `PainelPedidoFacade` |
| US29 | us29-botao-novo-pedido | `ICriarNovoPedidoListener`, `PainelPedidoPresenter`, `PainelPedidoView` |
