# language: pt
# Fonte da verdade de comportamento: US21 - Acompanhar pedidos
# Indexado a:
#   openspec/specs/painel-acompanhamento-pedidos/spec.md
# Regras de negocio: BR12 (ciclo de vida), BR13 (cancelamento e credito), US23 (pagamento)

Funcionalidade: Acompanhar pedidos
  Como atendente
  Quero acompanhar e atualizar os pedidos do delivery
  Para conduzir cada pedido pelas etapas operacionais e identificar pedidos cancelados

  Cenário: 1 - Abrir o painel de acompanhamento
    Dado que eu esteja autenticado como Atendente ou Administrador
    Quando eu acessar o Painel de Acompanhamento de Pedidos
    Então os pedidos devem ser organizados nas colunas Novo, Em preparo, Pronto para entrega, Em rota de entrega, Entregue e Cancelado
    E a tela deve inicializar maximizada

  Cenário: 2 - Filtrar pedidos por número ou cliente
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu informar um número de pedido ou nome de cliente no campo de busca
    E atualizar o painel
    Então devem permanecer visíveis os pedidos compatíveis com a busca e com o período selecionado

  Cenário: 3 - Atualizar o período do painel
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu selecionar um período diferente
    E clicar em Atualizar painel
    Então o painel deve recarregar os pedidos pertencentes ao período selecionado

  Cenário: 4 - Impedir avanço de pedido Novo sem pagamento
    Dado que exista um pedido na situação Novo sem pagamento registrado
    Quando eu tentar conduzi-lo para a preparação
    Então o pedido não deve ser promovido diretamente para Em preparo
    E o sistema deve direcionar a operação para o fluxo de pagamento

  Cenário: 5 - Promover pedido pago para Em preparo
    Dado que exista um pedido Novo
    Quando o pagamento desse pedido for finalizado conforme a US23
    Então o pedido deve passar para a situação Em preparo
    E o card deve ser apresentado na coluna Em preparo após a atualização do painel

  Cenário: 6 - Marcar pedido como Pronto para entrega
    Dado que exista um pedido na situação Em preparo
    Quando eu acionar Pronto para entrega
    Então a situação deve ser alterada para Pronto para entrega
    E o pedido deve passar para a coluna correspondente

  Cenário: 7 - Colocar pedido em rota de entrega
    Dado que exista um pedido na situação Pronto para entrega
    Quando eu acionar Sair para entrega
    Então a situação deve ser alterada para Em rota de entrega
    E o pedido deve passar para a coluna correspondente

  Cenário: 8 - Marcar pedido como Entregue
    Dado que exista um pedido na situação Em rota de entrega
    Quando eu acionar Entregue
    Então a situação deve ser alterada para Entregue
    E o pedido deve passar para a coluna correspondente

  Cenário: 9 - Cancelar pedido sem pagamento
    Dado que exista um pedido não cancelado e sem pagamento registrado
    Quando eu solicitar o cancelamento e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E nenhum crédito deve ser gerado para o cliente

  Cenário: 10 - Cancelar pedido com pagamento
    Dado que exista um pedido não cancelado com pagamento registrado
    Quando eu solicitar o cancelamento e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E deve ser registrado crédito para o cliente no valor efetivamente pago

  Cenário: 11 - Cancelar pedido Entregue
    Dado que exista um pedido na situação Entregue
    Quando eu solicitar o cancelamento e confirmar a operação
    Então a situação deve ser alterada para Cancelado
    E deve ser aplicado o tratamento de crédito previsto na BR13 quando houver pagamento registrado

  Cenário: 12 - Impedir recuperação de pedido Cancelado
    Dado que exista um pedido na situação Cancelado
    Quando eu selecionar esse pedido
    Então não deve existir ação para restaurar ou retornar o pedido a uma situação anterior

  Cenário: 13 - Criar novo pedido a partir de pedido Cancelado
    Dado que exista um pedido na situação Cancelado
    Quando eu acionar Criar novo pedido
    Então deve ser aberto um novo pedido com os dados reutilizáveis copiados do pedido cancelado
    E o novo pedido deve possuir novo número, nova data e hora e situação Novo
    E a nova data e hora deve seguir o período selecionado (data atual ou data específica escolhida)
    E nenhuma informação de pagamento deve ser copiada

  Cenário: 14 - Selecionar card com um unico clique
    Dado que exista um pedido exibido no Painel de Acompanhamento de Pedidos
    Quando eu clicar uma unica vez em qualquer area do card que nao seja um botao
    Então o card deve ser selecionado e receber o destaque visual

  Cenário: 15 - Navegar entre pedidos e botoes com Tab
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu pressionar a tecla Tab
    Então o foco deve percorrer os pedidos (cards) e os botoes
    E o pedido que receber o foco deve ser selecionado e destacado

  Cenário: 16 - Ocultar acao inexistente para o pedido selecionado
    Dado que exista um pedido selecionado sem ação compatível (por exemplo, Entregue)
    Quando eu selecionar esse pedido
    Então o botão Executar ação não deve ser exibido
    E para um pedido com ação compatível o botão Executar ação deve ser exibido com o texto da ação

  Cenário: 17 - Simular novo pedido enquanto a aplicacao esta aberta
    Dado que a aplicação esteja aberta
    Quando transcorrer o intervalo aleatório configurado (mínimo de 30 segundos)
    Então um novo pedido deve ser criado na situação Novo
    E um beep deve ser emitido
    E o foco deve ir para o card do novo pedido
    E o label "Novo Pedido #numero" deve ser exibido
    E após o tempo configurado o label deve desaparecer

  Cenário: 18 - Impedir segunda abertura da aplicacao
    Dado que a aplicação já esteja aberta
    Quando ocorrer uma segunda tentativa de abertura
    Então uma nova instância não deve ser iniciada

  Cenário: 19 - Filtrar pedidos por data especifica
    Dado que eu esteja no Painel de Acompanhamento de Pedidos
    Quando eu selecionar "Data específica"
    Então o datepicker deve ser exibido
    Quando eu selecionar a data no datepicker e atualizar o painel
    Então devem permanecer visíveis apenas os pedidos daquela data

  Cenário: 20 - Criar novo pedido simulado na data selecionada
    Dado que "Data específica" esteja selecionada com uma data
    Quando o simulador criar um novo pedido
    Então o novo pedido deve ser criado com a data selecionada

  Cenário: 21 - Notificar lote de novos pedidos
    Dado que o simulador crie de 1 a N pedidos (SIMULACAO_PEDIDOS_POR_CICLO) no mesmo ciclo
    Quando os novos pedidos forem recebidos
    Então todos devem ser salvos e exibidos
    E um alerta sonoro deve ser emitido
    E o label laranja de destaque deve mostrar "Novo Pedido #id" ou "N novos pedidos"
    E os cards dos novos pedidos devem piscar por SIMULACAO_MENSAGEM_SEGUNDOS
    E após esse tempo o label deve desaparecer e o realce dos cards ser removido
