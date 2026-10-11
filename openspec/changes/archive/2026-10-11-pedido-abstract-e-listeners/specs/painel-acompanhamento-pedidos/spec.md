# Spec delta - Pedido abstrato e acoes configuraveis do cliente

## MODIFIED Requirements

### Requirement: Identificacao minima do card
O card continua exibindo numero, horario, cliente, endereco, valor, forma de
pagamento e status - agora lidos de `PedidoAbstract`, que o cliente estende.

#### Scenario: Card a partir do modelo abstrato
- **WHEN** o painel monta um card
- **THEN** le os dados essenciais de `PedidoAbstract`
- **AND** nao existe classe concreta `Pedido` no componente

### Requirement: Visualizacao dos detalhes do pedido
A visualizacao passa a ser responsabilidade do cliente, via listener
obrigatorio; o componente nao abre a janela de detalhes.

#### Scenario: Sem listener de visualizacao
- **WHEN** o atendente aciona "Visualizar pedido" sem listener configurado
- **THEN** uma `IllegalStateException` e lancada

#### Scenario: Com listener de visualizacao
- **WHEN** o cliente configura `IVisualizarPedidoListener` e o atendente aciona
  "Visualizar pedido"
- **THEN** o listener recebe o `PedidoAbstract` selecionado
- **AND** o cliente exibe a sua propria tela do pedido

### Requirement: Pagamento obrigatorio antes de Em preparo
O registro do pagamento passa a ser feito pelo cliente, via listener
obrigatorio; o componente nao abre a escolha de forma de pagamento.

#### Scenario: Sem listener de pagamento
- **WHEN** a acao de um pedido Novo e acionada sem listener configurado
- **THEN** uma `IllegalStateException` e lancada

#### Scenario: Com listener de pagamento
- **WHEN** o cliente configura `IConfirmarPagamentoListener` e a acao de um
  pedido Novo e acionada
- **THEN** o listener recebe o `PedidoAbstract`
- **AND** o cliente registra o pagamento no seu proprio fluxo

### Requirement: Compatibilidade do botao Executar acao
Mantido; o botao "Fechar" encerra apenas o painel.

## ADDED Requirements

### Requirement: Modelo de pedido abstrato
O componente nao possui pedido concreto; expoe `PedidoAbstract`, que carrega os
dados essenciais do painel e a maquina de estados (GoF State), e o cliente a
estende.

#### Scenario: Cliente estende PedidoAbstract
- **WHEN** o cliente cria o seu pedido concreto
- **THEN** ele estende `PedidoAbstract` e implementa `criarCopia`
- **AND** o painel opera sobre `PedidoAbstract`

### Requirement: Acoes de visualizar pedido e confirmar pagamento configuraveis
Ambas sao listeners obrigatorios; ausentes, `PainelPedido.iniciar()` lanca
excecao e a acao correspondente tambem.

#### Scenario: Iniciar sem listeners obrigatorios
- **WHEN** `iniciar()` e chamado sem os dois listeners
- **THEN** uma `IllegalStateException` e lancada antes de criar a janela

#### Scenario: Iniciar com os listeners
- **WHEN** os dois listeners sao configurados
- **THEN** o painel inicia e as acoes delegam ao cliente

### Requirement: Fechar encerra apenas o painel
O botao "Fechar" encerra a janela do painel; o componente nao encerra a
aplicacao.

#### Scenario: Fechar o painel
- **WHEN** o atendente aciona "Fechar"
- **THEN** apenas a janela do painel e encerrada
- **AND** a aplicacao cliente permanece em execucao

### Requirement: Nomes no singular e sufixo de papel
Classes, classes abstratas e interfaces usam nome no singular; classes de
servico usam o sufixo `Service`.

#### Scenario: Revisao de nomes
- **WHEN** o componente e revisado
- **THEN** nao ha nome no plural em classe/abstrata/interface
- **AND** servicos usam o sufixo `Service`
