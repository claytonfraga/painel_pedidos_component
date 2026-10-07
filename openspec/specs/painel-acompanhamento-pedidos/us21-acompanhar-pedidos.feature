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

  @US21-002
  Cenário: Filtrar pedidos por número ou cliente
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu informar um número de pedido ou nome de cliente no campo de busca
    E atualizar o painel
    Então devem permanecer visíveis os pedidos compatíveis com a busca e com o período selecionado

  @US21-003
  Cenário: Atualizar o período do painel
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu selecionar um período diferente
    E clicar em Atualizar painel
    Então o painel deve recarregar os pedidos pertencentes ao período selecionado

  @US21-004
  Cenário: Impedir avanço de pedido Novo sem pagamento
    Dado que exista um pedido na situação Novo sem pagamento registrado
    Quando eu tentar conduzi-lo para a preparação
    Então o pedido não deve ser promovido diretamente para Em preparo
    E o sistema deve direcionar a operação para o fluxo de pagamento

  @US21-005
  Cenário: Promover pedido pago para Em preparo
    Dado que exista um pedido Novo
    Quando o pagamento desse pedido for finalizado conforme a US23
    Então o pedido deve passar para a situação Em preparo
    E o card deve ser apresentado na coluna Em preparo após a atualização do painel

  @US21-006
  Cenário: Marcar pedido como Pronto para entrega
    Dado que exista um pedido na situação Em preparo
    Quando eu acionar Pronto para entrega
    Então a situação deve ser alterada para Pronto para entrega
    E o pedido deve passar para a coluna correspondente

  @US21-007
  Cenário: Colocar pedido em rota de entrega
    Dado que exista um pedido na situação Pronto para entrega
    Quando eu acionar Sair para entrega
    Então a situação deve ser alterada para Em rota de entrega
    E o pedido deve passar para a coluna correspondente

  @US21-008
  Cenário: Marcar pedido como Entregue
    Dado que exista um pedido na situação Em rota de entrega
    Quando eu acionar Entregue
    Então a situação deve ser alterada para Entregue
    E o pedido deve passar para a coluna correspondente

  @US21-009
  Cenário: Cancelar pedido sem pagamento
    Dado que exista um pedido não cancelado e sem pagamento registrado
    Quando eu solicitar o cancelamento e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E nenhum crédito deve ser gerado para o cliente

  @US21-010
  Cenário: Cancelar pedido com pagamento
    Dado que exista um pedido não cancelado com pagamento registrado
    Quando eu solicitar o cancelamento e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E deve ser registrado crédito para o cliente no valor efetivamente pago

  @US21-011
  Cenário: Cancelar pedido Entregue
    Dado que exista um pedido na situação Entregue
    Quando eu solicitar o cancelamento e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E deve ser aplicado o tratamento de crédito previsto na BR13 quando houver pagamento registrado

  @US21-012
  Cenário: Impedir recuperação de pedido Cancelado
    Dado que exista um pedido na situação Cancelado
    Quando eu selecionar esse pedido
    Então não deve existir ação para restaurar ou retornar o pedido a uma situação anterior

  @US21-013
  Cenário: Criar novo pedido a partir de pedido Cancelado
    Dado que exista um pedido na situação Cancelado
    Quando eu acionar Criar novo pedido
    Então deve ser aberto um novo pedido com os dados reutilizáveis copiados do pedido cancelado
    E o novo pedido deve possuir novo número, nova data e hora e situação Novo
    E nenhuma informação de pagamento deve ser copiada

  @US21-014
  Cenário: Ocultar ação inexistente para o pedido selecionado
    Dado que exista um pedido selecionado sem ação compatível (por exemplo, Entregue)
    Quando eu selecionar esse pedido
    Então o botão Executar ação não deve ser exibido
    E para um pedido com ação compatível o botão Executar ação deve ser exibido com o texto da ação

  @US21-015
  Cenário: Selecionar card com um único clique
    Dado que exista um pedido exibido no Painel de Acompanhamento de Pedidos
    Quando eu clicar uma única vez em qualquer área do card que não seja um botão
    Então o card deve ser selecionado e receber o destaque visual

  @US21-016
  Cenário: Navegar entre pedidos e botões com Tab
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu pressionar a tecla Tab
    Então o foco deve percorrer os pedidos (cards) e os botões
    E o pedido que receber o foco deve ser selecionado e destacado
