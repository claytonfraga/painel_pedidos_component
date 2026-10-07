# language: pt
# Fontes: src/main/java/br/projeto/painelpedidos/sla; src/main/java/br/projeto/painelpedidos/tempo; src/main/java/br/projeto/painelpedidos/ordenacao; src/main/java/br/projeto/painelpedidos/seeder/PedidoSeeder.java
@us24 @sla @prioridade @ordenacao
Funcionalidade: Priorizar e ordenar pedidos por tempo no estado

  Como um atendente
  Eu quero ver destacados e no topo os pedidos que estão há mais tempo no estado
  Para atender primeiro o que está atrasado

  @US24-001
  Cenário: Priorizar pedido Novo que ultrapassa o tempo
    Dado que exista um pedido Novo há mais de SLA_NOVO_MINUTOS sem mudança de estado
    Quando o monitor de SLA executar
    Então o pedido deve receber o label vermelho "Prioridade"

  @US24-002
  Cenário: Priorizar pedido Em preparo que ultrapassa o tempo
    Dado que exista um pedido Em preparo há mais de SLA_EM_PREPARO_MINUTOS sem mudança de estado
    Quando o monitor de SLA executar
    Então o pedido deve receber o label vermelho "Prioridade"

  @US24-003
  Cenário: Priorizar pedido Pronto ou Em rota acima da média do bairro
    Dado que exista um pedido Pronto para entrega ou Em rota de entrega
    E que o tempo no estado ultrapassou a média de entrega do bairro (inicia em SLA_ENTREGA_BAIRRO_MINUTOS)
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

  @US24-006
  Cenário: Iniciar parte dos pedidos Novo do seeder já priorizados
    Dado que o seeder gera pedidos na situação Novo
    Quando o painel abrir
    Então cerca de 5% desses pedidos Novo devem já estar marcados como Prioridade

  @US24-007
  Cenário: Ordenar os cards por prioridade e posicao na raia
    Dado que existam vários pedidos na mesma raia
    Quando o painel for atualizado
    Então os pedidos com Prioridade devem aparecer acima dos demais em qualquer raia
    E na raia Novo o pedido mais recente deve aparecer no topo
    E nas demais raias o pedido há mais tempo no estado deve aparecer no topo
