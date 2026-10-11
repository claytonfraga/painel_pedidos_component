# language: pt
# Fontes: painel-pedidos/src/main/java/br/projeto/painelpedidos/sla; painel-pedidos/src/main/java/br/projeto/painelpedidos/tempo; painel-pedidos/src/main/java/br/projeto/painelpedidos/ordenacao
@us24 @sla @prioridade @ordenacao
Funcionalidade: Priorizar e ordenar pedidos por tempo no estado

  Como um atendente
  Eu quero ver destacados e no topo os pedidos que estão há mais tempo no estado
  Para atender primeiro o que está atrasado

  @US24-001
  Cenário: Priorizar pedido Novo que ultrapassa o tempo
    Dado que exista um pedido Novo há mais que o tempo máximo configurado para a situação, sem mudança de estado
    Quando o monitor de SLA executar
    Então o pedido deve receber o label vermelho "Prioridade"

  @US24-002
  Cenário: Priorizar pedido Em preparo que ultrapassa o tempo
    Dado que exista um pedido Em preparo há mais que o tempo máximo configurado para a situação, sem mudança de estado
    Quando o monitor de SLA executar
    Então o pedido deve receber o label vermelho "Prioridade"

  @US24-003
  Cenário: Priorizar pedido Pronto ou Em rota acima do SLA do estado
    Dado que exista um pedido Pronto para entrega ou Em rota de entrega
    E que o tempo no estado ultrapassou o tempo máximo configurado para a situação
    Quando o monitor de SLA executar
    Então o pedido deve receber o label vermelho "Prioridade"

  @US24-004
  Cenário: Não aplicar SLA a Entregue e Cancelado
    Dado um pedido Entregue ou Cancelado
    Quando o monitor de SLA executar
    Então nenhum label de Prioridade deve ser aplicado

  @US24-005
  Cenário: Remover a prioridade ao mudar de estado
    Dado um pedido marcado como Prioridade
    Quando o pedido mudar para outro estado
    Então o label vermelho "Prioridade" deve desaparecer

  @US24-007
  Cenário: Ordenar os cards por prioridade e tempo no estado (fila FIFO)
    Dado que existam vários pedidos na mesma raia
    Quando o painel for atualizado
    Então os pedidos com Prioridade devem aparecer acima dos demais
    E dentro de cada raia o pedido há mais tempo no estado (o que chegou primeiro) deve aparecer no topo
