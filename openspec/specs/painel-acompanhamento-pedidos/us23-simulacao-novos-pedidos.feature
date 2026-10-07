# language: pt
# Fontes: src/main/java/br/projeto/painelpedidos/simulacao; src/main/java/br/projeto/painelpedidos/notificacao; src/main/java/br/projeto/painelpedidos/som
@us23 @simulacao @notificacao
Funcionalidade: Simular recebimento de novos pedidos

  Como um atendente
  Eu quero ser notificado quando novos pedidos forem simulados
  Para saber, em tempo real, que um cliente fez um pedido

  @US23-001
  Cenário: Simular um novo pedido enquanto a aplicação está aberta
    Dado que a aplicação esteja aberta
    Quando transcorrer o intervalo aleatório configurado (mínimo SIMULACAO_INTERVALO_MINIMO_SEGUNDOS)
    Então de 1 a N pedidos (SIMULACAO_PEDIDOS_POR_CICLO) devem ser criados na situação Novo
    E os pedidos devem ser salvos e exibidos no painel

  @US23-002
  Cenário: Notificar e destacar os novos pedidos
    Dado que um ou mais pedidos novos foram recebidos
    Então um alerta sonoro deve ser emitido pela estratégia IEstrategiaSom
    E o foco deve ir para o card do primeiro novo pedido
    E os cards dos novos pedidos devem piscar por SIMULACAO_MENSAGEM_SEGUNDOS

  @US23-003
  Cenário: Mensagem por quantidade de pedidos novos
    Dado um único pedido novo
    Então o label de destaque deve exibir "Novo Pedido #número"
    Dado mais de um pedido novo no mesmo ciclo
    Então o label de destaque deve exibir a quantidade seguida de "novos pedidos"

  @US23-004
  Cenário: Sumir com o label e o destaque após o tempo configurado
    Dado que o label de destaque foi exibido e os cards estão piscando
    Quando transcorrer SIMULACAO_MENSAGEM_SEGUNDOS
    Então o label deve desaparecer
    E o realce dos cards deve ser removido, reaplicando a seleção corrente

  @US23-005
  Cenário: Executar a notificação via Chain of Responsibility adaptado
    Dado um lote de novos pedidos
    Quando a notificação for processada
    Então as regras (Handlers) aplicáveis devem ser executadas na ordem definida pelo NotificacaoNovoPedidoService
    E as atualizações de tela devem ocorrer via Observer
