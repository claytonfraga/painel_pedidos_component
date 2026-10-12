# language: pt
# Fontes: pom.xml; painel-pedidos/pom.xml; exemplo/pom.xml; README.md; openspec/AGENTS.md
@nao-funcional
Funcionalidade: Requisitos não funcionais do componente

  # ----- Distribuição e configuração -----

  @US27-001
  Cenário: Empacotar o componente com as dependências
    Dado o build do projeto
    Então deve ser gerado um único artefato (jar) do componente
    E esse artefato deve conter as dependências do componente

  @US27-002
  Cenário: Configuração por painel_pedidos.env
    Dado um arquivo painel_pedidos.env com as quatro chaves de SLA por estado
    Então a configuração deve ser carregada a partir dele
    E na ausência do arquivo ou de uma chave de SLA uma exceção deve ser lançada

  @US27-003
  Cenário: Projeto de exemplo consumindo o componente
    Dado o módulo de exemplo
    Quando ele for executado
    Então o painel deve abrir sobre os seus dados

  @US27-004
  Cenário: View embutível
    Dado o painel como um componente Swing (JPanel)
    Então ele deve poder ser embutido em outra aplicação Swing
    E a janela deve ser apenas um invólucro

  @US27-005
  Cenário: Look and Feel definido pela aplicação cliente
    Dado o componente iniciado
    Então o componente não deve alterar o Look and Feel
    E a aplicação cliente é quem o define (ex.: do sistema/Windows)

  # ----- Plataforma e build -----

  @NF-001
  Cenário: Plataforma e build
    Então o componente deve compilar como Java 21
    E o build deve ser feito com Maven, sem Spring

  # ----- Restrições de projeto -----

  @NF-002
  Cenário: Sem simulação nem leitura automática
    Dado o componente iniciado
    Então nenhum pedido deve ser gerado, sorteado, semeado nem lido automaticamente

  @NF-003
  Cenário: Janela sempre maximizada
    Dado o painel iniciado
    Então a janela deve abrir maximizada

  @NF-004
  Cenário: Mensagens de exceção orientam quem vai corrigir
    Dado uma falha de validação, de nulo ou de estado
    Quando a exceção for lançada
    Então a mensagem deve dizer o que fazer para resolver
    E não deve ser apenas o nome do parâmetro

  @US25-001
  Cenário: Aplicação aberta uma única vez
    Dado que o painel já esteja aberto
    Quando houver uma segunda tentativa de abertura
    Então uma nova instância não deve ser iniciada
