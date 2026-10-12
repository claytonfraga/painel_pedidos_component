# language: pt
# Fontes: spec.md (Requirement: Painel aberto uma unica vez)
@us25 @instancia-unica
Funcionalidade: Abrir o painel uma unica vez

  Como um operador
  Eu quero que o painel abra apenas uma vez por maquina
  Para evitar duas janelas concorrentes do mesmo painel

  @US25-001
  Cenário: Impedir a segunda abertura
    Dado que o painel já esteja aberto
    Quando ocorrer uma segunda tentativa de abertura
    Então nenhuma nova janela deve ser aberta
    E o consumidor é avisado de que o painel já está aberto

  @US25-002
  Cenário: Reabrir depois de fechar
    Dado que o painel tenha sido aberto e depois fechado
    Quando o painel for aberto novamente
    Então a janela deve abrir normalmente

  @US25-003
  Cenário: Abrir depois de um encerramento abrupto
    Dado que a execução anterior tenha terminado à força
    E que o arquivo de controle tenha ficado no disco
    Quando o painel for aberto
    Então a janela deve abrir normalmente

  @US25-004
  Cenário: O consumidor não configura nada
    Dado que o sistema consumidor inicie o painel
    Quando ele não informar nada sobre instância única
    Então o próprio painel cuida disso

  @US25-005
  Cenário: Dois sistemas diferentes na mesma máquina
    Dado o painel de um sistema aberto
    Quando outro sistema, com identidade diferente, abrir o painel
    Então a segunda abertura deve ser concedida
