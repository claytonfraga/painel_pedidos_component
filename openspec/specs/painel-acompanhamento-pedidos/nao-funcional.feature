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
    Dado o projeto de exemplo independente, que resolve o componente publicado
    Quando ele for executado
    Então o painel deve abrir sobre os seus dados

  @US27-009
  Cenário: Dois projetos Maven independentes
    Dado o repositório
    Então não deve existir pom agregador na raiz
    E cada projeto deve ter o próprio pom.xml, sem parent compartilhado

  @US27-010
  Cenário: Exemplo consome o componente publicado
    Dado o projeto de exemplo
    Então ele deve depender do componente pelo artefato publicado, não pelo código-fonte
    E a coordenada publicada deve ser com.github.<autor>:<repositorio>:<tag>

  @US27-011
  Cenário: Autoria e versão por propriedade (template)
    Dado o pom.xml do exemplo
    Então o groupId, o artifactId e a versão do componente devem vir de propriedades
    E não deve haver coordenada de autor fixa fora dessas propriedades

  @US27-012
  Cenário: Publicação e verificação no JitPack
    Dada uma tag v* empurrada
    Quando a Release for publicada
    Então o JitPack deve construir o artefato da tag
    E um segundo workflow deve aguardar o artefato resolver
    E verificar o exemplo contra o artefato publicado

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

  @US27-006
  Cenário: Identidade da aplicação obrigatória
    Dado um painel_pedidos.env sem IDENTIFICADOR_APLICACAO
    Então uma exceção orientando informá-la deve ser lançada

  @US27-007
  Cenário: Contrato público enumerado
    Dado o componente compilado
    Então apenas os 14 tipos do contrato devem ser públicos fora de internal
    E um tipo que entre ou saia do contrato deve fazer o build falhar

  @US27-008
  Cenário: Dependência embutida relocada
    Dado o jar do componente
    Então a cópia embutida da dotenv deve estar relocada
    E o consumidor pode usar dotenv sem conflito de classes

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
