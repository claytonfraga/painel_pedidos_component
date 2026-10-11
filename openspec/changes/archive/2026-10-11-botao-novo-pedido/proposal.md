# Botao "Novo Pedido" com listener do cliente

## Por que

O atendente precisa inserir pedidos rapidamente no painel (para testar/demonstrar),
reusando os dados ja existentes. Hoje nao ha como criar pedidos pela tela: os
dados vem do consumidor (seed/origem) ou por empurrao.

## O que muda

- Novo botao **"Novo Pedido"** no rodape, **a esquerda de "Visualizar pedido"**.
- Novo **listener obrigatorio** `ICriarNovoPedidoListener` (no mesmo estilo de
  `IVisualizarPedidoListener` e `IConfirmarPagamentoListener`), com
  `List<PedidoAbstract> criarNovoPedido()`.
- O **Presenter** comanda: ao acionar o botao, chama o listener do cliente,
  **salva** os pedidos devolvidos no `IPedidoRepository` e **atualiza o painel**.
- O **projeto de exemplo** implementa o listener: pergunta a quantidade (1 a 10,
  **limite fixo no codigo do listener**, nao no `.env`) e cria **copias** de
  pedidos do seeder com **novo numero e a data atual**.

## Impacto

- O consumidor passa a implementar um terceiro listener obrigatorio.
- A criacao dos pedidos e do cliente (dados, quantos, numeros); o componente so
  invoca e exibe.

## Alternativas consideradas

- **Botao so no exemplo**: nao seria reutilizavel por outros consumidores.
- **Componente gerar os pedidos**: violaria "sem simulacao/geracao no componente".
