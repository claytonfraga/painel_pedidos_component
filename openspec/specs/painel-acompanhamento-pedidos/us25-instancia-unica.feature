# language: pt
# Fontes: src/main/java/br/projeto/painelpedidos/aplicacao
@us25 @singleton @instancia-unica
Funcionalidade: Executar a aplicação uma única vez

  Como um operador
  Eu quero que a aplicação abra apenas uma vez
  Para evitar duas instâncias concorrentes

  @US25-001
  Cenário: Impedir segunda abertura da aplicação
    Dado que a aplicação já esteja aberta
    Quando ocorrer uma segunda tentativa de abertura
    Então uma nova instância não deve ser iniciada
