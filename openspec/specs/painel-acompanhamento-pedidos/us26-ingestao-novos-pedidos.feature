# language: pt
# Fontes: painel-pedidos/src/main/java/br/projeto/painelpedidos/ingestao/observer; painel-pedidos/src/main/java/br/projeto/painelpedidos/notificacao; painel-pedidos/src/main/java/br/projeto/painelpedidos/aplicacao
@us26 @ingestao @notificacao
Funcionalidade: Receber novos pedidos sem simulacao

  Como um sistema de delivery que incorpora o painel
  Eu quero entregar novos pedidos ao painel
  Para que o atendente seja notificado em tempo real

  @US26-001
  Cenário: Chegada de novos pedidos pelo sistema integrado
    Dado que o sistema integrado informe novos pedidos ao painel
    Então o painel deve se atualizar com os novos pedidos
    E os novos pedidos devem ser registrados na origem de dados do sistema
    E o atendente deve ser notificado (som, mensagem e destaque)

  @US26-002
  Cenário: Um único pedido ou vários
    Dado que cheguem um ou vários pedidos de uma vez
    Então a mensagem deve indicar "Novo Pedido #N" para um ou "N novos pedidos" para vários
    E os cards recebidos devem ser destacados
