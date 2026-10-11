# Painel de Acompanhamento de Pedidos - Especificacao

> Documento **normativo** (o que e por que). Nao repete cenarios nem design: os
> cenarios vivem nos `.feature` e as decisoes tecnicas em `design.md`.

## Purpose

Definir o comportamento do componente reutilizavel do Painel de Acompanhamento
de Pedidos: acompanhar e atualizar os pedidos pelas etapas operacionais (BR12),
cancelar com credito e criar novo pedido a partir de um cancelado (BR13),
filtrar por periodo/data, receber pedidos do sistema consumidor sem simulacao
(US26), priorizar/ordenar pelo tempo no estado (US24), oferecer execucao unica
opcional (US25) e ser distribuido como dependencia Maven (US27).

## Documentos relacionados

- Contexto do projeto: [`project.md`](../../project.md).
- Produto (visao, escopo, RNF, metricas): [`prd.md`](../../prd.md).
- Cenarios (fonte testavel): `us21`, `us22`, `us24`, `us25`, `us26`, `us27`
  nesta pasta.
- Design (padroes e diagramas UML) e referencias ABNT: [`design.md`](./design.md).
- Maquinas de estado UML: [`state-machines.md`](./state-machines.md).
- Regras e convencoes de codigo: [`AGENTS.md`](../../AGENTS.md).

## Historias de usuario e cenarios

Os cenarios sao a fonte da verdade testavel e nao sao reproduzidos aqui.

| Historia | Arquivo `.feature` | Cenarios |
|---|---|---|
| US21 - Acompanhar pedidos | [`us21-acompanhar-pedidos.feature`](./us21-acompanhar-pedidos.feature) | `@US21-001` a `@US21-018` |
| US22 - Filtrar por periodo e data | [`us22-filtro-periodo-data.feature`](./us22-filtro-periodo-data.feature) | `@US22-001` a `@US22-004` |
| US24 - Prioridade, SLA e ordenacao | [`us24-prioridade-sla-ordenacao.feature`](./us24-prioridade-sla-ordenacao.feature) | `@US24-001` a `@US24-007` |
| US26 - Ingestao de novos pedidos sem simulacao | [`us26-ingestao-novos-pedidos.feature`](./us26-ingestao-novos-pedidos.feature) | `@US26-001` a `@US26-003` |
| Requisitos nao funcionais | [`nao-funcional.feature`](./nao-funcional.feature) | `@US25-001`, `@US27-001` a `@US27-005`, `@NF-001` a `@NF-003` |
| US28 - Acoes do cliente e modelo abstrato | [`us28-acoes-do-cliente.feature`](./us28-acoes-do-cliente.feature) | `@US28-001` a `@US28-004` |
| US29 - Criar novos pedidos pelo painel | [`us29-botao-novo-pedido.feature`](./us29-botao-novo-pedido.feature) | `@US29-001` a `@US29-003` |

## Glossario e regras de negocio

### BR12 - Ciclo de vida do pedido

Situacoes: Novo, Em preparo, Pronto para entrega, Em rota de entrega, Entregue e
Cancelado. Novo -> Em preparo apos pagamento; Em preparo -> Pronto para entrega;
Pronto -> Em rota de entrega; Em rota -> Entregue; qualquer situacao diferente de
Cancelado -> Cancelado; Cancelado nao retorna. Modelagem GoF State em
[`design.md`](./design.md) e [`state-machines.md`](./state-machines.md).

### BR13 - Cancelamento e credito

- Cancelamento para qualquer pedido nao Cancelado, inclusive Entregue.
- Sem pagamento: nenhum credito. Com pagamento: credito igual ao valor pago.
- Pedido Cancelado permite Criar novo pedido (novo numero, nova data/hora,
  situacao Novo, sem pagamento).

### US23 - Fluxo de pagamento

A acao de um pedido Novo conduz ao fluxo de pagamento; ao finalizar, passa a
Em preparo. O fluxo e do cliente, disparado por `IConfirmarPagamentoListener`.

## Requisitos

### Requirement: Painel organizado em colunas por situacao
Seis colunas e contagem por coluna. Cenarios: `@US21-001`.

### Requirement: Identificacao minima do card
Numero, horario, cliente, endereco, valor, forma de pagamento (quando registrada)
e status. Cenarios: `@US21-002`.

### Requirement: Busca por numero ou cliente
Respeitando o periodo. Cenarios: `@US21-003`.

### Requirement: Filtro por periodo padrao e por data especifica
Hoje, Ultimos 7 dias, Ultimos 30 dias, Todos e Data especifica (datepicker
exibido apenas nessa opcao). Cenarios: `@US21-004`, `@US22-001`, `@US22-002`.

### Requirement: Pagamento obrigatorio antes de Em preparo
Novo sem pagamento nao avanca diretamente; a acao dispara
`IConfirmarPagamentoListener` (obrigatorio) e o cliente conduz o seu fluxo.
Cenarios: `@US21-005`, `@US21-006`, `@US28-002`.

### Requirement: Transicoes operacionais
Em preparo -> Pronto -> Em rota -> Entregue. Cenarios: `@US21-007`..`@US21-009`.

### Requirement: Cancelamento conforme BR13
Cenarios: `@US21-010`..`@US21-012`.

### Requirement: Pedido Cancelado nao retorna e permite criar novo
Cenarios: `@US21-013`, `@US21-014`.

### Requirement: Selecao e navegacao por teclado
Cenarios: `@US21-016`, `@US21-017`.

### Requirement: Visualizacao dos detalhes do pedido
A visualizacao e do cliente, via `IVisualizarPedidoListener` (obrigatorio); o
componente nao abre a janela de detalhes. Cenarios: `@US21-018`, `@US28-001`.

### Requirement: Compatibilidade do botao Executar acao
Invisivel quando nao ha acao ou selecao. Cenarios: `@US21-015`.

### Requirement: Ingestao de novos pedidos por empurrao
O sistema consumidor empurra os novos pedidos via
`receberNovosPedidos(List<PedidoAbstract>)` (GoF Observer: o consumidor e o
sujeito/fonte do evento e o componente e o observador). Nao ha leitura
automatica nem nenhum pedido gerado pelo componente. Cenarios: `@US26-001` a
`@US26-003`.

### Requirement: Novo pedido na data selecionada
Com Data especifica, o novo pedido usa aquela data (`IFonteDataPedido`).
Cenarios: `@US22-003`, `@US22-004`.

### Requirement: Prioridade por SLA
Cada estado com SLA tem um limite fixo, em minutos, lido do `painel_pedidos.env`:
Novo (`SLA_NOVO_MINUTOS`), Em preparo (`SLA_EM_PREPARO_MINUTOS`), Pronto para
entrega (`SLA_PRONTO_PARA_ENTREGA_MINUTOS`) e Em rota de entrega
(`SLA_EM_ROTA_DE_ENTREGA_MINUTOS`); Entregue e Cancelado sem SLA; label vermelho
"Prioridade" removido ao mudar de estado; sem media por bairro. Cenarios:
`@US24-001` a `@US24-006`.

### Requirement: Configuracao obrigatoria por `painel_pedidos.env`
O arquivo e obrigatorio no uso do componente, e as quatro chaves de SLA por
estado tambem; ausentes, `LeitorConfiguracaoPainelPedido` lanca excecao.
Cenarios: `@US27-002`.

### Requirement: Ordenacao dos cards (prioridade e fila FIFO)
Prioridade primeiro; dentro da raia, `dataHoraMudancaEstado` ascendente.
Cenarios: `@US24-007`.

### Requirement: Painel aberto uma unica vez (opcional)
Uma execucao por JVM e uma instancia por processo. Cenarios: `@US25-001`.

### Requirement: Atualizacoes de tela por Observer
Novos pedidos (empurrados pelo consumidor) e prioridade usam Observer. Cenarios:
`@US26-001` (efeito observavel).

### Requirement: Componente reutilizavel como dependencia Maven
Jar com dependencias; configuracao por `painel_pedidos.env`; modulo de exemplo;
View embedavel. Cenarios: `@US27-001` a `@US27-005`.

### Requirement: Modelo de pedido abstrato
O componente nao possui pedido concreto; expoe `PedidoAbstract`, com os dados
essenciais do painel e a maquina de estados (GoF State), que o cliente estende e
cuja `criarCopia` implementa. Cenarios: `@US28-003`.

### Requirement: Acoes de visualizar pedido e confirmar pagamento
Sao listeners obrigatorios do cliente; ausentes, `PainelPedidoFacade.iniciar()` lanca
excecao e a acao tambem. O componente nao abre as janelas do cliente. Cenarios:
`@US28-001`, `@US28-002`.

### Requirement: Botao "Novo Pedido" com listener do cliente
O painel expoe o botao **"Novo Pedido"** a esquerda de "Visualizar pedido", que
exige o listener obrigatorio `ICriarNovoPedidoListener` (devolve
`List<PedidoAbstract>`); o componente salva o retorno e atualiza o painel, sem
criar pedidos por conta propria. Cenarios: `@US29-001` a `@US29-003`.

### Requirement: Fechar encerra apenas o painel
O botao "Fechar" encerra a janela do painel; o componente nao encerra a
aplicacao. Cenarios: `@US28-004`.

### Requirement: Nomes no singular e sufixo de papel
Classes, classes abstratas e interfaces usam nome no singular; classes de
servico usam o sufixo `Service`. Cenarios: `@US28-004`.

### Requirement: Painel inicia sempre maximizado
O painel inicia maximizado. A maximizacao e comandada pela Presenter; a View
apenas executa o comando, permanecendo passiva. Cenarios: `@NF-003`.

## Diretrizes visuais

- Borda do card (normal): cinza, RGB (176, 181, 189).
- Borda do card (selecionado): azul de foco, RGB (37, 99, 235), 2 px.
- Fundo do card selecionado: cor do titulo da raia (Novo 232,239,247; Em preparo
  218,236,252; Pronto 255,242,196; Em rota 252,228,209; Entregue 222,242,222;
  Cancelado 249,221,221).
- Fundo da raia: cinza muito claro (248, 250, 252).
- Look and Feel definido pela aplicacao cliente (o componente nao o define);
  tela maximizada.

## Restricoes de implementacao

As restricoes obrigatorias (MVP Passive View, limites de linguagem,
nomenclatura, excecoes unchecked, instancia unica, etc.) ficam em
[`AGENTS.md`](../../AGENTS.md).

## Referencias

Referencias bibliograficas (ABNT): ver [`design.md`](./design.md).
