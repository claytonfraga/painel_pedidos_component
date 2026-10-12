# Spec delta - Fronteira de reuso

## MODIFIED Requirements

### Requirement: Painel aberto uma unica vez por aplicacao
O componente possui a trava de instancia unica e a aplica sozinho ao iniciar:
`iniciar()` e `iniciarEmbutido()` adquirem a trava antes de criar a janela e, nao
conseguindo, devolvem `false` sem abrir nada. `encerrar()` libera a trava, de modo
que fechar e reabrir no mesmo processo volta a abrir. A **identidade da aplicacao**
(`IDENTIFICADOR_APLICACAO`, obrigatoria) e o que a trava isola: dois sistemas
diferentes na mesma maquina convivem, e o mesmo sistema recusa a segunda abertura.
O arquivo de controle deriva da identidade, e nunca e apagado.
Cenarios: `@US25-001` a `@US25-005`.

#### Scenario: Dois sistemas diferentes na mesma maquina
- **WHEN** o painel de um sistema esta aberto e outro sistema, com identidade
  diferente, abre o painel
- **THEN** a segunda abertura e concedida

#### Scenario: Identidade da aplicacao ausente
- **WHEN** o `painel_pedidos.env` nao informa `IDENTIFICADOR_APLICACAO`
- **THEN** uma excecao orientando informa-la e lancada

### Requirement: Componente reutilizavel como dependencia Maven
Jar com dependencias - a dotenv embutida e relocada, para o consumidor poder usar
dotenv por conta propria sem conflito de classes; configuracao por
`painel_pedidos.env`; modulo de exemplo; View embedavel. O **contrato publico** e
enumerado (14 tipos) e todo o resto vive sob `br.projeto.painelpedidos.internal`,
fixado pelo `ContratoPublicoTest`; embutir o painel usa `getPainelPedido()` e
`iniciarEmbutido()`. Cenarios: `@US27-001` a `@US27-008`.
