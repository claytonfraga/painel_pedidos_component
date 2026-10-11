# Spec delta - Botao "Novo Pedido"

## ADDED Requirements

### Requirement: Botao "Novo Pedido" com listener do cliente
O painel expoe um botao **"Novo Pedido"** a esquerda de "Visualizar pedido". Ele
exige um listener do cliente `ICriarNovoPedidoListener`, cujo
`criarNovoPedido()` devolve uma `List<PedidoAbstract>`; sem o listener,
`iniciar()` lanca excecao e a acao tambem. O componente nao cria pedidos: ele
apenas invoca o listener, salva o retorno no `IPedidoRepository` e atualiza o
painel.

#### Scenario: Sem listener de novo pedido
- **WHEN** o painel e iniciado sem `ICriarNovoPedidoListener`
- **THEN** uma `IllegalStateException` e lancada

#### Scenario: Criar pedidos pelo botao
- **WHEN** o cliente configura `ICriarNovoPedidoListener` e o atendente aciona
  "Novo Pedido"
- **THEN** os pedidos devolvidos pelo listener sao salvos
- **AND** o painel e atualizado

#### Scenario: Copias de pedidos do seeder (exemplo)
- **WHEN** o atendente escolhe a quantidade (1 a 10) no listener de exemplo
- **THEN** copias de pedidos existentes sao criadas com novo numero e a data
  atual, iniciando na situacao Novo
- **AND** o limite maximo de 10 fica no codigo do listener de exemplo, nao no `.env`
