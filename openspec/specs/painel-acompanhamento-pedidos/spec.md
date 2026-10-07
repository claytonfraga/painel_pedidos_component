# Painel de Acompanhamento de Pedidos - Especificacao

> Documento **normativo** (o que e por que). Nao repete cenarios nem design: os
> cenarios vivem nos `.feature` e as decisoes tecnicas em `design.md`. Este
> documento aponta para ambos por link, evitando repeticao conceitual.

## Purpose

Definir o comportamento do Painel de Acompanhamento de Pedidos do delivery:
acompanhar e atualizar os pedidos pelas etapas operacionais (BR12), cancelar com
credito e criar novo pedido a partir de um cancelado (BR13), filtrar por
periodo/data, receber pedidos simulados com notificacao, priorizar/ordenar pelo
tempo no estado e abrir a aplicacao uma unica vez.

## Documentos relacionados

- Contexto do projeto: [`project.md`](../../project.md).
- Produto (visao, escopo, RNF, metricas): [`prd.md`](../../prd.md).
- Cenarios (fonte testavel): `us21..us25*.feature` nesta pasta (tabela adiante).
- Design (padroes e diagramas de classe UML) e referencias ABNT:
  [`design.md`](./design.md).
- Maquinas de estado UML: [`state-machines.md`](./state-machines.md).
- Regras e convencoes de codigo: [`AGENTS.md`](../../AGENTS.md).

## Historias de usuario e cenarios

Os cenarios sao a fonte da verdade testavel e **nao** sao reproduzidos aqui.

| Historia | Arquivo `.feature` | Cenarios |
|---|---|---|
| US21 - Acompanhar pedidos | [`us21-acompanhar-pedidos.feature`](./us21-acompanhar-pedidos.feature) | `@US21-001` a `@US21-018` |
| US22 - Filtrar por periodo e data | [`us22-filtro-periodo-data.feature`](./us22-filtro-periodo-data.feature) | `@US22-001` a `@US22-004` |
| US23 - Simular novos pedidos | [`us23-simulacao-novos-pedidos.feature`](./us23-simulacao-novos-pedidos.feature) | `@US23-001` a `@US23-005` |
| US24 - Prioridade, SLA e ordenacao | [`us24-prioridade-sla-ordenacao.feature`](./us24-prioridade-sla-ordenacao.feature) | `@US24-001` a `@US24-007` |
| US25 - Instancia unica da aplicacao | [`us25-instancia-unica.feature`](./us25-instancia-unica.feature) | `@US25-001` |

## Glossario e regras de negocio

### BR12 - Ciclo de vida do pedido

Situacoes: Novo, Em preparo, Pronto para entrega, Em rota de entrega, Entregue e
Cancelado. Transicoes validas:

- Novo -> Em preparo somente apos pagamento registrado (fluxo de pagamento US23).
- Em preparo -> Pronto para entrega.
- Pronto para entrega -> Em rota de entrega.
- Em rota de entrega -> Entregue.
- Qualquer situacao diferente de Cancelado -> Cancelado.
- Cancelado nao retorna a nenhuma situacao anterior.

A modelagem dessas transicoes (GoF State) e a respectiva maquina de estado UML
estao em [`design.md`](./design.md) e [`state-machines.md`](./state-machines.md).

### BR13 - Cancelamento e credito

- Cancelamento disponivel para qualquer pedido que ainda nao esteja Cancelado,
  inclusive Entregue.
- Sem pagamento registrado: nenhum credito e gerado.
- Com pagamento registrado: credito igual ao valor efetivamente pago.
- Pedido Cancelado serve apenas para consulta e permite Criar novo pedido com
  dados reutilizaveis; o novo pedido recebe novo numero, nova data e hora,
  situacao Novo e NENHUMA informacao de pagamento (ver `@US21-014` e `@US22-004`).

### US23 - Fluxo de pagamento

- A acao associada a um pedido Novo conduz ao fluxo de pagamento; ao finalizar,
  o pedido passa para Em preparo (ver `@US21-005` e `@US21-006`).

## Requisitos

Cada requisito e enunciado uma unica vez; os cenarios ficam no `.feature`
correspondente (identificados por tag).

### Requirement: Painel organizado em colunas por situacao
Apresentar os pedidos em seis colunas distintas (Novo, Em preparo, Pronto para
entrega, Em rota de entrega, Entregue, Cancelado) e com contagem por coluna.
Cenarios: `@US21-001`. Feature: [`us21-acompanhar-pedidos.feature`](./us21-acompanhar-pedidos.feature).

### Requirement: Identificacao minima do card
Cada card identifica, no minimo, numero, horario, cliente, endereco, valor, forma
de pagamento (quando registrada) e status. Cenarios: `@US21-002`.

### Requirement: Busca por numero ou cliente
Buscar por numero do pedido ou nome do cliente, respeitando o periodo.
Cenarios: `@US21-003`.

### Requirement: Filtro por periodo padrao e por data especifica
Oferecer Hoje, Ultimos 7 dias, Ultimos 30 dias, Todos e Data especifica (com
datepicker exibido apenas nessa opcao). Cenarios: `@US21-004`, `@US22-001`,
`@US22-002`.

### Requirement: Pagamento obrigatorio antes de Em preparo
Pedido Novo sem pagamento nao avanca diretamente; a acao conduz ao fluxo de
pagamento (US23). Cenarios: `@US21-005`, `@US21-006`.

### Requirement: Transicoes operacionais
Avancar Em preparo -> Pronto para entrega -> Em rota de entrega -> Entregue.
Cenarios: `@US21-007`, `@US21-008`, `@US21-009`.

### Requirement: Cancelamento conforme BR13
Cancelamento em qualquer pedido nao cancelado (inclusive Entregue), com credito
quando houver pagamento. Cenarios: `@US21-010`, `@US21-011`, `@US21-012`.

### Requirement: Pedido Cancelado nao retorna e permite criar novo
Coluna Cancelado so para consulta; acao Criar novo pedido com dados reutilizaveis
e sem pagamento. Cenarios: `@US21-013`, `@US21-014`.

### Requirement: Selecao e navegacao por teclado
Selecionar com um clique (area do card, fora dos botoes) e navegar por Tab entre
pedidos e botoes. Cenarios: `@US21-016`, `@US21-017`.

### Requirement: Visualizacao dos detalhes do pedido
Dialogo com numero, cliente, endereco, data/hora, situacao, valor e forma de
pagamento (e data/hora do pagamento quando houver). Cenarios: `@US21-018`.

### Requirement: Compatibilidade do botao Executar acao
Botao invisivel quando nao ha acao compativel (ex.: Entregue) ou sem selecao.
Cenarios: `@US21-015`.

### Requirement: Simulacao de novos pedidos
Com a aplicacao aberta, criar de 1 a N pedidos por ciclo em intervalos aleatorios
(minimo configuravel), simulando clientes, com alerta sonoro, foco, mensagem e
destaque. Cenarios: `@US23-001` a `@US23-005`.

### Requirement: Novo pedido na data selecionada
Com Data especifica selecionada, o novo pedido usa aquela data (`IFonteDataPedido`);
com filtros padrao, a data atual. Cenarios: `@US22-003`, `@US22-004`.

### Requirement: Prioridade por SLA
Tempo maximo por estado (Novo, Em preparo, Pronto, Em rota); Entregue e Cancelado
sem SLA; marca o pedido com label vermelho "Prioridade", removido ao mudar de
estado. Cenarios: `@US24-001` a `@US24-006`.

### Requirement: Ordenacao dos cards (prioridade e fila FIFO)
A raia e uma **fila**: o pedido ha mais tempo no estado (que chegou primeiro) fica
no topo, para ser atendido primeiro, respeitada a prioridade. Ordem: prioridade
primeiro; em seguida, `dataHoraMudancaEstado` ascendente (FIFO), via
`IOrdenacaoPedidos`. Cenarios: `@US24-007`.

### Requirement: Aplicacao aberta uma unica vez
Uma execucao por JVM e uma instancia por processo. Cenarios: `@US25-001`.

### Requirement: Atualizacoes de tela por Observer
Atualizacoes decorrentes de eventos (novos pedidos e prioridade) usam o padrao
Observer. Detalhe e diagramas: [`design.md`](./design.md). Cenarios:
`@US23-002`, `@US24-001` (efeito observavel na tela).

## Diretrizes visuais

- Borda do card (normal): cinza mais escuro, RGB (176, 181, 189).
- Borda do card (selecionado): azul de foco marcante, RGB (37, 99, 235), 2 px.
- Fundo do card selecionado: a cor de fundo do titulo da raia (Novo 232,239,247;
  Em preparo 218,236,252; Pronto para entrega 255,242,196; Em rota de entrega
  252,228,209; Entregue 222,242,222; Cancelado 249,221,221).
- Fundo da raia: cinza muito claro (248, 250, 252).
- Look and Feel do sistema (`UIManager.getSystemLookAndFeelClassName()`); tela
  maximizada.

## Restricoes de implementacao

As restricoes obrigatorias (arquitetura MVP Passive View, limites de linguagem,
nomenclatura, excecoes unchecked, instancia unica, etc.) ficam em
[`AGENTS.md`](../../AGENTS.md) e sao citadas aqui por link, sem repeticao.

## Referencias

Referencias bibliograficas no padrao ABNT: ver
[`design.md`](./design.md) (secao Referencias).
