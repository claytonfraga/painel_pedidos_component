# language: pt
# Fontes: painel-pedidos/src/main/java/br/projeto/painelpedidos/model; painel-pedidos/src/main/java/br/projeto/painelpedidos/listener; painel-pedidos/src/main/java/br/projeto/painelpedidos/presenter
@us28 @modelo @cliente
Funcionalidade: Acoes e modelo do sistema integrado

  Como um sistema de delivery que incorpora o painel
  Eu quero que as telas de pedido e de pagamento sejam minhas
  Para controlar os dados e o fluxo do meu proprio sistema

  @US28-001
  Cenário: Visualizar pedido é do sistema integrado
    Dado que o sistema integrado informe como exibir um pedido
    Quando o atendente acionar "Visualizar pedido"
    Então o sistema integrado deve exibir a sua própria tela do pedido
    E sem essa configuração uma exceção deve ser lançada

  @US28-002
  Cenário: Confirmar pagamento é do sistema integrado
    Dado que o sistema integrado informe como pagar um pedido
    Quando o atendente acionar a ação de um pedido Novo
    Então o sistema integrado deve conduzir o seu próprio fluxo de pagamento
    E sem essa configuração uma exceção deve ser lançada

  @US28-003
  Cenário: Modelo de pedido estendido pelo sistema integrado
    Dado que o componente não possui pedido concreto
    Então o sistema integrado deve estender o modelo de pedido do componente
    E o painel opera apenas sobre esse modelo

  @US28-004
  Cenário: Fechar apenas o painel
    Dado o painel aberto
    Quando o atendente acionar "Fechar"
    Então apenas a janela do painel é encerrada
    E a aplicação cliente permanece em execução
