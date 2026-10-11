# language: pt
# Fontes: painel-pedidos/src/main/java/br/projeto/painelpedidos/listener; painel-pedidos/src/main/java/br/projeto/painelpedidos/presenter; painel-pedidos/src/main/java/br/projeto/painelpedidos/view
@us29 @novo-pedido @cliente
Funcionalidade: Criar novos pedidos pelo painel

  Como um atendente
  Eu quero criar novos pedidos pelo painel
  Para inserir pedidos rapidamente

  @US29-001
  Cenário: Botão Novo Pedido exige a configuração do sistema integrado
    Dado o painel de acompanhamento
    Então deve haver um botão "Novo Pedido" à esquerda de "Visualizar pedido"
    E sem essa configuração uma exceção deve ser lançada

  @US29-002
  Cenário: O sistema integrado cria os novos pedidos
    Dado que o sistema integrado informe como criar novos pedidos
    Quando o atendente acionar "Novo Pedido"
    Então os pedidos informados devem ser exibidos no painel
    E devem ser registrados na origem de dados do sistema

  @US29-003
  Cenário: Cópias de pedidos existentes no exemplo
    Dado o sistema integrado de exemplo
    Quando o atendente escolher a quantidade (1 a 10)
    Então cópias de pedidos existentes devem ser criadas com novo número e a data atual, na situação Novo
    E o limite máximo de 10 fica no código de exemplo, não no .env
