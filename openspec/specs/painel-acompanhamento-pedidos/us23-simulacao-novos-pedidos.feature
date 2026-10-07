# language: pt
# Fontes: src/main/java/br/projeto/painelpedidos/simulacao; src/main/java/br/projeto/painelpedidos/notificacao; src/main/java/br/projeto/painelpedidos/som
@us23 @simulacao @notificacao
Funcionalidade: Simular recebimento de novos pedidos

  Como um atendente
  Eu quero ser notificado quando novos pedidos forem simulados
  Para saber, em tempo real, que um cliente fez um pedido

  @US23-001
  Cenário: Simular novo pedido enquanto a aplicação está aberta
    Dado que a aplicação esteja aberta
    Quando transcorrer o intervalo aleatório configurado (mínimo de 30 segundos)
    Então um novo pedido deve ser criado na situação Novo
    E um alerta sonoro deve ser emitido
    E o foco deve ir para o card do novo pedido
    E o label de destaque deve exibir "Novo Pedido #numero"

  @US23-002
  Cenário: Notificar lote de novos pedidos
    Dado que o simulador crie de 1 a N pedidos (SIMULACAO_PEDIDOS_POR_CICLO) no mesmo ciclo
    Quando os novos pedidos forem recebidos
    Então todos devem ser salvos e exibidos
    E o label de destaque deve exibir "Novo Pedido #id" (um) ou "N novos pedidos" (vários)

  @US23-003
  Cenário: Destacar com efeito acende-apaga os cards novos
    Dado que um ou mais pedidos novos chegaram
    Quando o destaque for iniciado
    Então os cards dos novos pedidos devem piscar por SIMULACAO_MENSAGEM_SEGUNDOS
    E ao final o realce deve ser removido e o label de destaque deve desaparecer

  @US23-004
  Cenário: Emitir alerta sonoro pela estratégia configurada
    Dado que chegaram novos pedidos
    Quando o alerta for acionado
    Então o som deve ser emitido pela estratégia IEstrategiaSom (Strategy) configurada
