# language: pt
# Fontes: src/main/java/br/projeto/painelpedidos/filtro; src/main/java/br/projeto/painelpedidos/simulacao/FonteDataPedido.java; src/main/java/br/projeto/painelpedidos/presenter/PainelPedidoPresenter.java
@us22 @filtro @periodo
Funcionalidade: Filtrar pedidos por período e data específica

  Como um atendente
  Eu quero filtrar os pedidos por período padrão ou por uma data específica
  Para visualizar apenas o intervalo efetivamente desejado

  @US22-001
  Cenário: Filtrar pedidos por data específica
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu selecionar "Data específica"
    Então o datepicker deve ser exibido
    Quando eu selecionar a data no datepicker e atualizar o painel
    Então devem permanecer visíveis apenas os pedidos daquela data

  @US22-002
  Cenário: Criar novo pedido simulado na data selecionada
    Dado que "Data específica" esteja selecionada com uma data
    Quando o simulador criar um novo pedido
    Então o novo pedido deve ser criado com a data selecionada

  @US22-003
  Cenário: Criar novo pedido a partir de Cancelado usa a data do período
    Dado um pedido Cancelado e um período selecionado
    Quando eu acionar Criar novo pedido
    Então o novo pedido deve ser criado com a data do período selecionado (data atual ou data específica)
