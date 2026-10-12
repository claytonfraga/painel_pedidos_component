# language: pt
# Fontes: painel-pedidos/src/main/java/br/projeto/painelpedidos/filtro; painel-pedidos/src/main/java/br/projeto/painelpedidos/tempo/FonteDataPedido.java; painel-pedidos/src/main/java/br/projeto/painelpedidos/presenter/PainelPedidoPresenter.java
@us22 @filtro @periodo
Funcionalidade: Filtrar pedidos por período e data específica

  Como um atendente
  Eu quero filtrar os pedidos por período padrão ou por uma data específica
  Para visualizar apenas o intervalo efetivamente desejado

  @US22-001
  Cenário: Exibir o datepicker apenas quando necessário
    Dado que eu esteja no painel
    Então o campo de data deve ficar oculto
    Quando eu selecionar "Data específica" no período
    Então o campo de data (datepicker) deve ser exibido
    E ao selecionar outro período o campo de data deve ser ocultado

  @US22-002
  Cenário: Filtrar pedidos por data específica
    Dado que "Data específica" esteja selecionada e uma data esteja escolhida no datepicker
    Quando eu clicar em Atualizar painel
    Então devem permanecer visíveis apenas os pedidos daquela data
    E a busca por número/cliente deve ser respeitada

  @US22-003
  Cenário: Novo pedido recebido na data específica
    Dado que "Data específica" esteja selecionada com uma data
    Quando um novo pedido for recebido (leitura periódica ou empurrão)
    Então o novo pedido deve ser criado com a data selecionada

  @US22-004
  Cenário: Criar novo pedido a partir de Cancelado usa a data do período
    Dado um pedido Cancelado e o período selecionado (data atual ou data específica)
    Quando eu acionar Criar novo pedido
    Então o novo pedido deve ser criado com a data do período selecionado
