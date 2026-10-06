# Painel de Acompanhamento de Pedidos Specification

## Purpose

Definir o comportamento do Painel de Acompanhamento de Pedidos do delivery
(US21), permitindo que o atendente acompanhe e atualize os pedidos pelas etapas
operacionais das regras BR12 e BR13, incluindo cancelamento com credito e
criacao de novo pedido a partir de um pedido cancelado.

## Fonte da verdade

- Este documento e normativo (o que e por que).
- O arquivo Gherkin desta capability e a fonte da verdade testavel:
  [`us21-acompanhar-pedidos.feature`](./us21-acompanhar-pedidos.feature).
- Todo cenario deste `spec.md` deve existir no `.feature` e vice-versa.

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

### BR13 - Cancelamento e credito

- Cancelamento disponivel para qualquer pedido que ainda nao esteja Cancelado,
  inclusive Entregue.
- Sem pagamento registrado: nenhum credito e gerado.
- Com pagamento registrado: credito igual ao valor efetivamente pago.
- Pedido Cancelado serve apenas para consulta e permite Criar novo pedido com
  dados reutilizaveis; o novo pedido recebe novo numero, nova data e hora,
  situacao Novo e NENHUMA informacao de pagamento.

### US23 - Fluxo de pagamento

- A acao associada a um pedido Novo conduz ao fluxo de pagamento; ao finalizar,
  o pedido passa para Em preparo.

### BR12 - Implementacao do ciclo de vida (GoF State)

O ciclo de vida e implementado com GoF State no estilo do exemplo `Porta`:

- Classe abstrata `PedidoState` (pacote `br.projeto.painelpedidos.state`) com
  `protected final Pedido pedido` e operacoes default que lancam `RuntimeException`.
- Estados concretos `PedidoNovoState`, `PedidoEmPreparoState`,
  `PedidoProntoParaEntregaState`, `PedidoEmRotaDeEntregaState`,
  `PedidoEntregueState`, `PedidoCanceladoState` sobrescrevem as operacoes
  permitidas e transicionam via `pedido.setEstado(new PedidoProximoState(pedido))`.
- Cada estado concentra a logica/regras da situacao e valida pre-condicoes no
  construtor; se nao atendidas, lanca excecao e a transicao nao ocorre
  (`PedidoEmPreparoState` exige pagamento; `PedidoCanceladoState` calcula credito).
- `Pedido` e o contexto (`estado = new PedidoNovoState(this)`,
  `getEstado`/`setEstado(PedidoState)`).
- SRP: nomes/regras de situacao ficam nas classes de estado; o repositorio nao
  conhece strings de situacao. OCP/LSP/DIP: nova situacao = nova classe, estados
  substituiveis, `Pedido` depende da abstracao `PedidoState`.
- Sem `enum`, sem `instanceof` e sem strings de situacao duplicadas.
- Dados iniciais: o repositorio em memoria semeia 40 pedidos (10 originais + 30
  novos), conduzindo cada um pelas transicoes do dominio.

## Requirements

### Requirement: Painel organizado em colunas por situacao

O sistema DEVE apresentar os pedidos em seis colunas distintas: Novo, Em
preparo, Pronto para entrega, Em rota de entrega, Entregue e Cancelado.

#### Scenario: Abrir o painel de acompanhamento
- **WHEN** um Atendente ou Administrador autenticado acessa o Painel de Acompanhamento de Pedidos
- **THEN** os pedidos sao organizados nas colunas Novo, Em preparo, Pronto para entrega, Em rota de entrega, Entregue e Cancelado
- **AND** a tela inicializa maximizada

### Requirement: Identificacao minima do card

Cada card DEVE identificar, no minimo, o numero do pedido, o cliente, o
endereco de entrega, o valor e a forma de pagamento quando registrada.

#### Scenario: Apresentar informacoes do card
- **WHEN** um pedido e apresentado no painel
- **THEN** o card mostra numero, cliente, endereco de entrega e valor
- **AND** mostra a forma de pagamento quando houver pagamento registrado

### Requirement: Busca por numero do pedido ou nome do cliente

O sistema DEVE permitir busca por numero do pedido ou nome do cliente e DEVE
manter visiveis apenas os pedidos compativeis com a busca e com o periodo.

#### Scenario: Filtrar pedidos por numero ou cliente
- **WHEN** o atendente informa um numero de pedido ou nome de cliente no campo de busca e atualiza o painel
- **THEN** permanecem visiveis os pedidos compativeis com a busca e com o periodo selecionado

### Requirement: Filtro por periodo e acao Atualizar painel

O sistema DEVE oferecer selecao de periodo e a acao Atualizar painel, recarregando
os pedidos pertencentes ao periodo selecionado.

#### Scenario: Atualizar o periodo do painel
- **WHEN** o atendente seleciona um periodo diferente e clica em Atualizar painel
- **THEN** o painel recarrega os pedidos pertencentes ao periodo selecionado

### Requirement: Pagamento obrigatorio antes de Em preparo

Um pedido Novo sem pagamento NAO PODE ser promovido diretamente para Em preparo;
a acao relacionada ao pagamento DEVE conduzir ao fluxo de pagamento.

#### Scenario: Impedir avanco de pedido Novo sem pagamento
- **WHEN** o atendente tenta conduzir para preparacao um pedido Novo sem pagamento registrado
- **THEN** o pedido nao e promovido diretamente para Em preparo
- **AND** a operacao e direcionada ao fluxo de pagamento

#### Scenario: Promover pedido pago para Em preparo
- **WHEN** o pagamento de um pedido Novo e finalizado conforme a US23
- **THEN** o pedido passa para a situacao Em preparo
- **AND** o card e apresentado na coluna Em preparo apos a atualizacao do painel

### Requirement: Transicoes operacionais do ciclo de vida

O sistema DEVE permitir avancar Em preparo para Pronto para entrega, Pronto para
entrega para Em rota de entrega e Em rota de entrega para Entregue.

#### Scenario: Marcar pedido como Pronto para entrega
- **WHEN** o atendente aciona Pronto para entrega em um pedido Em preparo
- **THEN** a situacao muda para Pronto para entrega
- **AND** o pedido passa para a coluna correspondente

#### Scenario: Colocar pedido em rota de entrega
- **WHEN** o atendente aciona Sair para entrega em um pedido Pronto para entrega
- **THEN** a situacao muda para Em rota de entrega
- **AND** o pedido passa para a coluna correspondente

#### Scenario: Marcar pedido como Entregue
- **WHEN** o atendente aciona Entregue em um pedido Em rota de entrega
- **THEN** a situacao muda para Entregue
- **AND** o pedido passa para a coluna correspondente

### Requirement: Cancelamento conforme BR13

O cancelamento DEVE estar disponivel para qualquer pedido que ainda nao esteja
Cancelado, inclusive Entregue, aplicando o credito quando houver pagamento.

#### Scenario: Cancelar pedido sem pagamento
- **WHEN** o atendente solicita o cancelamento de um pedido nao cancelado e sem pagamento registrado e confirma
- **THEN** a situacao muda para Cancelado
- **AND** nenhum credito e gerado para o cliente

#### Scenario: Cancelar pedido com pagamento
- **WHEN** o atendente solicita o cancelamento de um pedido nao cancelado com pagamento registrado e confirma
- **THEN** a situacao muda para Cancelado
- **AND** e registrado credito para o cliente no valor efetivamente pago

#### Scenario: Cancelar pedido Entregue
- **WHEN** o atendente solicita o cancelamento de um pedido Entregue e confirma
- **THEN** a situacao muda para Cancelado
- **AND** e aplicado o tratamento de credito previsto na BR13 quando houver pagamento registrado

### Requirement: Pedido Cancelado nao retorna ao fluxo

A coluna Cancelado DEVE manter os pedidos apenas para consulta, sem acao de
restaurar ou retornar a uma situacao anterior.

#### Scenario: Impedir recuperacao de pedido Cancelado
- **WHEN** o atendente seleciona um pedido Cancelado
- **THEN** nao existe acao para restaurar ou retornar o pedido a uma situacao anterior

### Requirement: Criar novo pedido a partir de Cancelado (BR13)

A coluna Cancelado DEVE disponibilizar a acao Criar novo pedido, que inicia um
novo pedido com os dados reutilizaveis e sem informacao de pagamento.

#### Scenario: Criar novo pedido a partir de pedido Cancelado
- **WHEN** o atendente aciona Criar novo pedido em um pedido Cancelado
- **THEN** e aberto um novo pedido com os dados reutilizaveis copiados do pedido cancelado
- **AND** o novo pedido possui novo numero, nova data e hora e situacao Novo
- **AND** a nova data e hora segue o periodo selecionado (data atual, ou a data especifica escolhida) via `IFonteDataPedido`
- **AND** nenhuma informacao de pagamento e copiada

### Requirement: Selecao do card e navegacao por teclado

O sistema DEVE permitir selecionar um card com um unico clique do mouse em
qualquer area do card (exceto os botoes de acao) e DEVE permitir navegar por
teclado com a tecla Tab entre os pedidos e entre os botoes.

#### Scenario: Selecionar card com um unico clique
- **WHEN** o atendente clica uma unica vez em qualquer area do card que nao seja um botao
- **THEN** o card e selecionado e recebe o destaque visual

#### Scenario: Navegar entre pedidos e botoes com Tab
- **WHEN** o atendente pressiona a tecla Tab
- **THEN** o foco percorre os pedidos (cards) e os botoes
- **AND** o pedido que recebe o foco e selecionado e destacado

### Requirement: Compatibilidade do botao Executar acao

O botao Executar acao DEVE ficar invisivel quando o pedido selecionado nao
possuir acao compativel (por exemplo, Entregue) e visivel apenas quando houver
acao aplicavel. Sem pedido selecionado, tambem permanece invisivel.

#### Scenario: Ocultar acao inexistente
- **WHEN** um pedido sem acao compativel (ex.: Entregue) e selecionado
- **THEN** o botao Executar acao nao e exibido

#### Scenario: Exibir acao aplicavel
- **WHEN** um pedido com acao compativel (Novo, Em preparo, Pronto para entrega, Em rota de entrega ou Cancelado) e selecionado
- **THEN** o botao Executar acao e exibido com o texto da acao correspondente

### Requirement: Simulacao de novo pedido

Com a aplicacao aberta, o sistema DEVE, em intervalos aleatorios com minimo
configuravel (pelo menos 30 segundos), criar de 1 ate N pedidos por ciclo
(`SIMULACAO_PEDIDOS_POR_CICLO`), simulando clientes. Configuracao no `.env`:
`SEED_QUANTIDADE`, `SIMULACAO_INTERVALO_MINIMO_SEGUNDOS`,
`SIMULACAO_INTERVALO_MAXIMO_SEGUNDOS`, `SIMULACAO_MENSAGEM_SEGUNDOS`,
`SIMULACAO_PEDIDOS_POR_CICLO`.

#### Scenario: Novo pedido simulado
- **WHEN** o intervalo aleatorio configurado transcorre com a aplicacao aberta
- **THEN** de 1 a N pedidos sao criados na situacao Novo e salvos
- **AND** um alerta sonoro e emitido e o foco vai para o card do novo pedido

### Requirement: Notificacao de novos pedidos (Chain of Responsibility adaptado)

Ao receber um ou mais pedidos novos, um `NotificacaoNovoPedidoService` processa
regras `RegraHandler` (cada uma com `seAplica`/`executar`, sem
referencia a outra regra; o Service coordena a ordem) para: salvar, atualizar o
painel, selecionar/focar o card, alerta sonoro, mensagem e destaque.

#### Scenario: Lote de novos pedidos
- **WHEN** o simulador cria de 1 a N pedidos no mesmo ciclo
- **THEN** todos sao salvos e exibidos; o alerta sonoro e emitido
- **AND** o label (card pequeno laranja escuro, texto branco em negrito) mostra "Novo Pedido #id" (um) ou "N novos pedidos" (varios)
- **AND** o label desaparece apos `SIMULACAO_MENSAGEM_SEGUNDOS`

### Requirement: Destaque piscante dos novos pedidos

Os cards dos pedidos recem-criados DEVEM piscar (acende/apaga) por
`SIMULACAO_MENSAGEM_SEGUNDOS`, alternando o realce, para chamar a atencao do
atendente; ao final, o realce e removido e a selecao corrente e reaplicada.

#### Scenario: Efeito acende-apaga nos cards novos
- **WHEN** um ou mais pedidos novos chegam
- **THEN** seus cards piscam por `SIMULACAO_MENSAGEM_SEGUNDOS`
- **AND** ao termino voltam ao estado normal (reaplicando a selecao)

### Requirement: Alerta sonoro configuravel (Strategy)

O alerta sonoro DEVE usar a abstracao `IEstrategiaSom` (Strategy), com
implementacoes `SomTomStrategy` (tom audivel gerado) e `SomToolkitStrategy`
(`Toolkit.beep`), permitindo trocar o mecanismo sem alterar o Presenter (DIP).

#### Scenario: Emitir alerta sonoro
- **WHEN** chegam novos pedidos
- **THEN** o presente toca o alerta pela `IEstrategiaSom` configurada

### Requirement: Aplicacao aberta uma unica vez

A aplicacao DEVE poder ser aberta apenas uma vez: uma execucao por JVM e uma
instancia por processo, garantido pelo Singleton thread-safe
`InstanciaUnicaAplicacao`, sem permitir uma segunda abertura.

#### Scenario: Impedir segunda abertura
- **WHEN** a aplicacao ja esta aberta e ocorre uma segunda tentativa de abertura
- **THEN** uma nova instancia nao e iniciada

### Requirement: Periodo com filtros padrao ou data especifica

O filtro de periodo DEVE oferecer os filtros padrao (Hoje, Ultimos 7 dias,
Ultimos 30 dias, Todos) e a opcao de selecionar uma data especifica por um
datepicker `spnData` (`JSpinner` com `SpinnerDateModel`, `dd/MM/yyyy`). O
datepicker DEVE ser exibido somente quando "Data especifica" estiver
selecionada, para nao confundir a interface. A selecao e resolvida por uma
abstracao `IFiltroPeriodo`, criada por `IFabricaFiltroPeriodo` (OCP/DIP).

#### Scenario: Filtrar por data especifica
- **WHEN** o atendente escolhe "Data específica", seleciona a data no datepicker e atualiza o painel
- **THEN** permanecem visiveis apenas os pedidos daquela data
- **AND** o datepicker aparece somente quando "Data específica" esta selecionada

### Requirement: Novo pedido simulado na data selecionada

Quando uma data especifica esta selecionada no periodo, todo novo pedido criado
pelo mecanismo de simulacao DEVE ser criado naquela data, via `IFonteDataPedido`
(`FonteDataPedido`); com os filtros padrao, o novo pedido e criado na data atual.

#### Scenario: Novo pedido na data selecionada
- **WHEN** "Data específica" esta selecionada com uma data e o simulador cria um novo pedido
- **THEN** o novo pedido e criado com a data selecionada
- **AND** com os filtros padrao, o novo pedido e criado na data atual

### Requirement: Prioridade por SLA (Chain of Responsibility adaptado)

Cada pedido tem um tempo maximo na situacao (SLA): Novo 5 min
(`SLA_NOVO_MINUTOS`), Em preparo 20 min (`SLA_EM_PREPARO_MINUTOS`), Pronto para
entrega e Em rota de entrega usam a media de entrega por bairro (inicia em
`SLA_ENTREGA_BAIRRO_MINUTOS`, ajustada a cada entrega); Entregue e Cancelado nao
tem SLA. Um listener periodico (`AgendadorMonitorSla`) opera regras
`RegraSla*Handler` (CoR adaptado, orquestrado por `MonitorSlaService`) e marca o
pedido com label vermelho "Prioridade", que some quando o pedido muda de estado.

#### Scenario: Pedido Novo excede 5 minutos
- **WHEN** um pedido Novo fica mais de 5 minutos sem mudar de estado
- **THEN** ele recebe o label vermelho "Prioridade"
- **AND** o label some quando o pedido passa para outro estado

### Requirement: Atualizacoes de tela por Observer (GoF)

As atualizacoes de tela decorrentes de eventos (novos pedidos e mudanca de
prioridade por SLA) DEVEM usar o padrao Observer: a abstracao e as classes
concretas de observador usam o sufixo `Observer`, em subpacote do elemento
observado.

#### Scenario: Atualizar tela ao priorizar
- **WHEN** o monitor marca pedidos como prioridade
- **THEN** os observadores notificam e a tela e atualizada

### Requirement: Ordenacao dos cards por tempo no estado

Dentro de cada raia, os cards DEVEM ser ordenados pelo tempo no estado atual
(quem esta ha mais tempo no estado aparece no topo), usando a abstracao
`IOrdenacaoPedidos` (Strategy/DIP); a implementacao `OrdenacaoPorTempoNoEstado`
compara por `dataHoraMudancaEstado` ascendente.

#### Scenario: Pedido mais antigo no estado no topo
- **WHEN** o painel e atualizado
- **THEN** em cada raia o pedido ha mais tempo no estado fica no topo da raia

## Diretrizes visuais

- Borda do card (normal): cinza mais escuro, RGB (176, 181, 189).
- Borda do card (selecionado): azul de foco mais marcante sem exagero, RGB (37, 99, 235), com espessura de 2 px.
- Fundo do card selecionado: a mesma cor de fundo do titulo da raia (Novo 232,239,247; Em preparo 218,236,252; Pronto para entrega 255,242,196; Em rota de entrega 252,228,209; Entregue 222,242,222; Cancelado 249,221,221).
- Fundo da raia: cinza muito claro (248, 250, 252).
- Look and Feel: o do sistema (`UIManager.getSystemLookAndFeelClassName()`), isto e, o
  look and feel do Windows quando executado no Windows.

## Restricoes de implementacao

- Padrao obrigatorio: MVP na variante Passive View, conforme
  [`openspec/references/Model-View-Presenter.md`](../../references/Model-View-Presenter.md)
  (indexado em [`openspec/references/README.md`](../../references/README.md)).
- A View e abstraida por `IPainelPedidoView`; o Presenter depende da abstracao e
  nao instancia a View concreta (composicao no ponto de entrada).
- A View e a Presenter sao Passive View: a View nao conhece o Model.
- Nao usar lambda, Stream API, `enum`, `instanceof` nem atributos estaticos.
- Nao usar `implements Runnable`; a aplicacao inicia diretamente no ponto de entrada.
- Nao usar `BigDecimal` (usar `double` para valores monetarios) nem `Optional`.
- Nao criar testes automatizados nem dependencias/diretorio de teste.
- Listeners devem ser classes anonimas no ponto de uso (no Presenter); nao criar
  classes/arquivos de listener nem usar lambda.
- Usar sempre excecoes unchecked (`IllegalArgumentException`/`RuntimeException`) e
  `Objects.requireNonNull` para nulidade.
- Todo arquivo `.java` deve conter o comentario de autoria no padrao NetBeans
  (`@author clayton`) antes da declaracao do tipo.
- Nomes de classes sempre no singular.
- Toda interface (abstracao) Java deve comecar com `I` (ex.: `IPedidoRepository`).
- A View contem apenas a criacao/configuracao de seus elementos: sem listeners,
  sem `main` e com getters apenas para os elementos manipulados pela Presenter.
- A View nao expoe setters publicos (setter apenas interno/privado e quando
  estritamente necessario).
- A View segue o padrao NetBeans (`initComponents`/bloco de variaveis + `.form`
  sincronizado) e os componentes Swing (campos e variaveis locais, na View e no
  Presenter) usam SEMPRE os prefixos por tipo (`btn`, `cmb`, `lbl`, `pnl`, `scr`,
  `txt`, ... conforme `openspec/AGENTS.md`).
- A tela sempre inicializa maximizada.
