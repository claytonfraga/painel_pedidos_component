# PRD - Painel de Acompanhamento de Pedidos (MVP Passive View)

> Status: implementado. Documento derivado do codigo efetivo (78 classes).
> Fontes normativas: `specs/painel-acompanhamento-pedidos/spec.md` e os
> `*.feature` da mesma pasta.

## 1. Visao e objetivo

Aplicacao desktop (Swing) que permite ao atendente acompanhar e conduzir os
pedidos do delivery por etapas operacionais, cancelar com credito, criar um novo
pedido a partir de um cancelado, filtrar por periodo/data, receber pedidos
simulados em tempo real e priorizar/ordenar pelo tempo no estado.

## 2. Contexto e problema

- **Personas:** atendente (operacao do dia a dia), administrador (mesmas
  capacidades), operador (abre a aplicacao).
- **Problema:** acompanhar muitos pedidos em estados distintos, sem perder os
  que estao atrasados, e conduzi-los corretamente conforme as regras.

## 3. Escopo

**Dentro:**
- Painel com 6 colunas de situacao e acoes por estado (US21).
- Busca, filtro por periodo padrao e por data especifica (US21, US22).
- Simulacao de novos pedidos com alerta sonoro, foco, mensagem e destaque (US23).
- Prioridade por SLA e ordenacao por tempo no estado (US24).
- Instancia unica da aplicacao (US25).

**Fora:**
- Persistencia real (SQL/H2); os dados sao em memoria.
- Autenticacao real (assume-se Atendente/Administrador).
- Integracao com gateway de pagamento (apenas registro da forma).

## 4. Regras de negocio

- **BR12 (ciclo de vida):** situacoes Novo, Em preparo, Pronto para entrega, Em
  rota de entrega, Entregue e Cancelado. Transicoes: Novo -> Em preparo somente
  apos pagamento; Em preparo -> Pronto para entrega; Pronto -> Em rota de
  entrega; Em rota -> Entregue; qualquer estado nao cancelado -> Cancelado;
  Cancelado nao retorna.
- **BR13 (cancelamento e credito):** cancelamento em qualquer pedido nao
  cancelado, inclusive Entregue; sem pagamento nao gera credito; com pagamento
  gera credito igual ao valor do pedido; pedido Cancelado permite "Criar novo
  pedido" (novo numero, nova data/hora, situacao Novo, sem dados de pagamento).
- **US23 (pagamento):** a acao de um pedido Novo conduz ao fluxo de pagamento
  (selecao da forma); ao confirmar, o pedido passa a Em preparo.

## 5. Requisitos funcionais

### US21 - Acompanhar pedidos (`@US21-001`..`018`)
Colunas com contagem e tela maximizada; card com numero, horario, cliente,
endereco, valor, forma de pagamento (quando registrada) e status; busca por
numero/cliente (Enter ou botao); periodo padrao; transicoes e acoes por estado;
cancelamento; criar novo a partir de Cancelado; ocultar "Executar acao" quando
nao houver acao; selecao com um clique; navegacao por Tab; visualizar detalhes.

### US22 - Filtrar por periodo e data (`@US22-001`..`004`)
Filtros padrao (Hoje, Ultimos 7 dias, Ultimos 30 dias, Todos) e data especifica
(datepicker exibido somente nessa opcao); a data selecionada define tambem a data
de novos pedidos (simulados e criados a partir de Cancelado).

### US23 - Simular novos pedidos (`@US23-001`..`005`)
Intervalo aleatorio configuravel; de 1 a N pedidos por ciclo; ao receber: salvar,
atualizar painel, selecionar/focar, alerta sonoro, mensagem por cardinalidade e
destaque piscante por tempo configuravel.

### US24 - Prioridade, SLA e ordenacao (`@US24-001`..`007`)
SLA por estado (Novo, Em preparo, Pronto, Em rota); label vermelho "Prioridade"
removido ao mudar de estado; ~5% dos Novo do seeder ja priorizados; ordenacao por
tempo no estado (mais antigo no topo da raia).

### US25 - Instancia unica (`@US25-001`)
Singleton thread-safe + trava de arquivo impedem a segunda abertura.

## 6. Requisitos nao funcionais

- Java 21 (`maven.compiler.release=21`), Maven, encoding UTF-8, **sem Spring**.
- Arquitetura **MVP Passive View**; a View e passiva e o Presenter coordena.
- Padroes: **State** (ciclo de vida), **Strategy** (filtro de periodo, som,
  ordenacao), **Chain of Responsibility adaptado** (`Handler` + `Service`),
  **Observer** (atualizacoes de tela), **Adapter** (`LeitorEnv` sobre dotenv),
  **Singleton thread-safe** (instancia unica).
- Restricoes de codigo: sem lambda, Stream, `enum`, `instanceof`, `Runnable`;
  sem atributos estaticos (excecao: o Singleton); excecoes unchecked;
  `Objects.requireNonNull`; classes no singular; interfaces com prefixo `I`.
- Configuracao por `.env` (encapsulado em `LeitorEnv`).
- Persistencia em memoria atras de `IPedidoRepository`.
- Look and Feel do sistema; tela maximizada; foco/teclado (Tab).

## 7. Configuracao (`.env`)

`SEED_QUANTIDADE`, `SIMULACAO_INTERVALO_MINIMO_SEGUNDOS`,
`SIMULACAO_INTERVALO_MAXIMO_SEGUNDOS`, `SIMULACAO_MENSAGEM_SEGUNDOS`,
`SIMULACAO_PEDIDOS_POR_CICLO`, `SLA_NOVO_MINUTOS`, `SLA_EM_PREPARO_MINUTOS`,
`SLA_ENTREGA_BAIRRO_MINUTOS`, `SLA_MONITOR_INTERVALO_SEGUNDOS`.
Se ausente, valem os defaults do codigo (`.env.example` documenta).

## 8. Metricas de sucesso

- Uma execucao popula as 6 colunas e os 4 periodos padrao retornam conjuntos
  distintos.
- Pedidos que ultrapassam o SLA ganham o label "Prioridade" e sobem na raia.
- Novos pedidos simulados sao notificados (som, mensagem e destaque) sem travar
  a interface.

## 9. Riscos e limitacoes

- Sem persistencia: os dados se perdem ao fechar (aceito no POC).
- A media de entrega por bairro **inicia** em `SLA_ENTREGA_BAIRRO_MINUTOS`; existe
  `ITempoEntregaService.registrarEntrega(...)`, mas **nada o chama ainda** (ajuste
  automatico nao esta ativo).
- `SomToolkitStrategy` e uma alternativa de Strategy; a composicao usa
  `SomTomStrategy`.

## 10. Rastreabilidade

| Historia | Feature | Principais classes |
|---|---|---|
| US21 | us21-acompanhar-pedidos | `PainelPedidoPresenter`, `Pedido`, `Pedido*State`, `IPedidoRepository`, `IPainelPedidoView` |
| US22 | us22-filtro-periodo-data | `IFiltroPeriodo`/`Filtro*`, `IFabricaFiltroPeriodo`, `IFonteDataPedido` |
| US23 | us23-simulacao-novos-pedidos | `SimuladorNovoPedido`, `PainelPedidoNovoPedidoObserver`, `NotificacaoNovoPedidoService`, `RegraHandler`+`*Handler`, `IEstrategiaSom` |
| US24 | us24-prioridade-sla-ordenacao | `MonitorSlaService`, `RegraSlaHandler`+`*Handler`, `AgendadorMonitorSla`, `PrioridadeObserver`, `ITempoEntregaService`, `IOrdenacaoPedidos`, `PedidoSeeder` |
| US25 | us25-instancia-unica | `InstanciaUnicaAplicacao`, `ITravaInstanciaUnica`, `AplicacaoPainelPedido` |
