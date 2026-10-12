# Spec delta - Instancia unica dentro do componente

## MODIFIED Requirements

### Requirement: Painel aberto uma unica vez
O componente possui a trava de instancia unica e a aplica sozinho ao iniciar:
`iniciar()` adquire a trava antes de criar a janela e, nao conseguindo, devolve
`false` sem abrir nada (nem janela, nem SLA, nem observer). `encerrar()` libera a
trava, de modo que fechar e reabrir no mesmo processo volta a abrir. A trava e um
arquivo fornecido pelo componente e o arquivo nunca e apagado. O consumidor nao
configura trava, caminho nem singleton; informar o usuario da recusa e do
consumidor.
Cenarios: `@US25-001` a `@US25-004`.

#### Scenario: Impedir a segunda abertura
- **WHEN** o painel ja esta aberto e ocorre uma segunda tentativa de abertura
- **THEN** nenhuma janela e aberta
- **AND** `iniciar()` devolve `false`

#### Scenario: Reabrir depois de fechar
- **WHEN** o painel e fechado (`encerrar()`) e aberto novamente
- **THEN** a trava volta a ser adquirida e a janela abre

#### Scenario: Abrir apos encerramento abrupto
- **WHEN** a execucao anterior terminou a forca deixando o arquivo de trava no
  disco
- **THEN** o painel abre normalmente (nao ha trava ativa no arquivo)

#### Scenario: Consumidor nao configura nada
- **WHEN** o consumidor inicia o painel sem informar nada sobre instancia unica
- **THEN** o componente cuida disso sozinho
