# language: pt
# Fontes: src/main/java/br/projeto/painelpedidos/presenter/PainelPedidoPresenter.java; src/main/java/br/projeto/painelpedidos/model/Pedido.java; src/main/java/br/projeto/painelpedidos/state
@us21 @painel @mvp
Funcionalidade: Acompanhar pedidos

  Como um atendente
  Eu quero acompanhar e atualizar os pedidos do delivery
  Para conduzir cada pedido pelas etapas operacionais e identificar pedidos cancelados

  @US21-001
  Cenário: Abrir o painel de acompanhamento
    Dado que eu esteja autenticado como Atendente ou Administrador
    Quando eu acessar o Painel de Acompanhamento de Pedidos
    Então os pedidos devem ser organizados nas colunas Novo, Em preparo, Pronto para entrega, Em rota de entrega, Entregue e Cancelado
    E a tela deve inicializar maximizada
    E o título de cada coluna deve exibir a contagem de pedidos

  @US21-002
  Cenário: Apresentar as informações mínimas do card
    Dado um pedido exibido no painel
    Então o card deve mostrar o número do pedido, o horário, o cliente, o endereço de entrega e o valor
    E deve mostrar a forma de pagamento quando houver pagamento registrado
    E deve mostrar o status "Aguardando pagamento", "Pagamento confirmado" ou "Pedido cancelado"

  @US21-003
  Cenário: Buscar pedidos por número ou cliente
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu informar um número de pedido ou nome de cliente no campo de busca e confirmar (Enter)
    Ou clicar em Atualizar painel
    Então devem permanecer visíveis os pedidos cujo identificador ou nome do cliente contenha o texto informado
    E o filtro de período selecionado deve ser respeitado

  @US21-004
  Cenário: Filtrar por período padrão
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu selecionar um período (Hoje, Últimos 7 dias, Últimos 30 dias ou Todos) e clicar em Atualizar painel
    Então o painel deve recarregar os pedidos pertencentes ao período selecionado

  @US21-005
  Cenário: Impedir avanço de pedido Novo sem pagamento
    Dado que exista um pedido na situação Novo sem pagamento registrado
    Quando eu acionar a ação principal desse pedido
    Então deve ser aberto o diálogo "Registrar pagamento" para seleção da forma de pagamento
    E o pedido não deve ser promovido diretamente para Em preparo

  @US21-006
  Cenário: Promover pedido pago para Em preparo
    Dado que exista um pedido Novo
    Quando eu selecionar uma forma de pagamento no diálogo de pagamento
    Então o pedido deve passar para a situação Em preparo
    E deve aparecer na coluna Em preparo após a atualização do painel

  @US21-007
  Cenário: Marcar pedido como Pronto para entrega
    Dado que exista um pedido na situação Em preparo
    Quando eu acionar a ação principal desse pedido
    Então a situação deve ser alterada para Pronto para entrega

  @US21-008
  Cenário: Colocar pedido em rota de entrega
    Dado que exista um pedido na situação Pronto para entrega
    Quando eu acionar a ação principal desse pedido
    Então a situação deve ser alterada para Em rota de entrega

  @US21-009
  Cenário: Marcar pedido como Entregue
    Dado que exista um pedido na situação Em rota de entrega
    Quando eu acionar a ação principal desse pedido
    Então a situação deve ser alterada para Entregue

  @US21-010
  Cenário: Cancelar pedido sem pagamento
    Dado que exista um pedido não cancelado e sem pagamento registrado
    Quando eu acionar Cancelar e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E nenhum crédito deve ser gerado para o cliente

  @US21-011
  Cenário: Cancelar pedido com pagamento
    Dado que exista um pedido não cancelado com pagamento registrado
    Quando eu acionar Cancelar e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E deve ser registrado crédito para o cliente no valor total do pedido

  @US21-012
  Cenário: Cancelar pedido Entregue
    Dado que exista um pedido na situação Entregue
    Quando eu acionar Cancelar e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E deve ser registrado crédito quando houver pagamento registrado

  @US21-013
  Cenário: Impedir recuperação de pedido Cancelado
    Dado que exista um pedido na situação Cancelado
    Quando eu selecionar esse pedido
    Então não deve existir ação para restaurar ou retornar o pedido a uma situação anterior
    E a ação principal disponível deve ser "Criar novo pedido"

  @US21-014
  Cenário: Criar novo pedido a partir de pedido Cancelado
    Dado que exista um pedido na situação Cancelado
    Quando eu acionar Criar novo pedido
    Então deve ser criado um novo pedido com os dados reutilizáveis copiados do pedido cancelado
    E o novo pedido deve possuir novo número, nova data e hora e situação Novo
    E nenhuma informação de pagamento deve ser copiada

  @US21-015
  Cenário: Ocultar ação inexistente para o pedido selecionado
    Dado que eu selecione um pedido sem ação compatível (Entregue)
    Então o botão Executar ação não deve ser exibido
    E ao selecionar um pedido com ação compatível o botão Executar ação deve ser exibido com o texto da ação

  @US21-016
  Cenário: Selecionar card com um único clique
    Dado que exista um pedido exibido no painel
    Quando eu clicar uma única vez em qualquer área do card que não seja um botão
    Então o card deve ser selecionado e destacado
    E a barra inferior deve exibir o pedido selecionado e habilitar Visualizar pedido

  @US21-017
  Cenário: Navegar entre pedidos e botões com Tab
    Dado que eu esteja no painel
    Quando eu pressionar a tecla Tab
    Então o foco deve percorrer os cards dos pedidos e os botões
    E o card que receber o foco deve ser selecionado

  @US21-018
  Cenário: Visualizar os detalhes do pedido selecionado
    Dado um pedido selecionado
    Quando eu acionar Visualizar pedido
    Então deve ser exibido um diálogo com número, cliente, endereço, data e hora, situação, valor e forma de pagamento
    E deve exibir a data e hora do pagamento quando houver pagamento registrado
