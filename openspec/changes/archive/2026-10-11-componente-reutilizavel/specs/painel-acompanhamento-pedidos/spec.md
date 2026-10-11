# Spec delta - Componente reutilizavel de Painel de Acompanhamento de Pedidos

## REMOVED Requirements

### Requirement: Simulacao de novos pedidos
Removida. Nao ha geracao, sorteio ou semeacao de pedidos no componente.
Cenarios: `@US23-001` a `@US23-005` deixam de existir.

#### Scenario: Nenhuma classe de simulacao permanece
- **WHEN** a aplicacao procura classes do pacote `simulacao`
- **THEN** nenhuma existe
- **AND** nenhum dado e gerado em tempo de execucao

### Requirement: Configuracao por `.env`
Removida. A configuracao passa a ser `painel_pedidos.env`, lida por
`LeitorConfiguracaoPainelPedidos`.

## MODIFIED Requirements

### Requirement: Atualizacoes de tela por Observer
A notificacao de novos pedidos continua usando o CoR adaptado, mas o sujeito
que a dispara passa a ser a leitura periodica (`LeitorNovoPedidoService`,
GoF Observer) ou o empurrao por `receberNovosPedidos(...)`, nunca um simulador.

#### Scenario: Pedido novo notificado sem simulacao
- **WHEN** a fonte de novos pedidos devolve um pedido ainda nao notificado
- **THEN** a cadeia de notificacao executa (salvar, atualizar, focar, alerta
  sonoro, mensagem e destaque)
- **AND** o pedido e persistido na origem de dados do consumidor

## ADDED Requirements

### Requirement: Componente reutilizavel como dependencia Maven
O componente deve ser empacotado como biblioteca (jar com dependencias) e
incorporado por outro projeto como dependencia, sem copiar o repositorio.

#### Scenario: Consumidor executa o painel
- **WHEN** um projeto adiciona o jar, implementa `IPedidoRepository` e chama
  `PainelPedidos.iniciar()`
- **THEN** o painel e exibido sobre os dados do consumidor
- **AND** nao ha `main` no componente

### Requirement: Ingestao de novos pedidos por leitura periodica ou empurrao
Novos pedidos entram por `IFonteNovoPedido`, lida a cada
`LEITURA_NOVO_PEDIDO_INTERVALO_MS` enquanto `LEITURA_NOVO_PEDIDO_ATIVA=true`, ou
por `receberNovosPedidos(...)`.

#### Scenario: Leitura periodica entrega apenas pedidos novos
- **WHEN** a fonte devolve pedidos ja notificados
- **THEN** nada e notificado novamente
- **WHEN** a fonte devolve um identificador novo
- **THEN** apenas ele e notificado

### Requirement: Configuracao por `painel_pedidos.env`
`LeitorConfiguracaoPainelPedidos` le `SLA_NOVO_MINUTOS`,
`SLA_EM_PREPARO_MINUTOS`, `SLA_ENTREGA_BAIRRO_MINUTOS`,
`SLA_MONITOR_INTERVALO_SEGUNDOS`, `NOTIFICACAO_DURACAO_SEGUNDOS`,
`LEITURA_NOVO_PEDIDO_ATIVA`, `LEITURA_NOVO_PEDIDO_INTERVALO_MS`, `SOM_ATIVO` e
`TITULO_JANELA`, com defaults seguros quando ausentes.

#### Scenario: Arquivo ausente mantem o componente executavel
- **WHEN** `painel_pedidos.env` nao existe
- **THEN** os defaults sao usados
- **AND** o componente permanece executavel

### Requirement: View embedavel e print headless
`PainelPedidoPanel` e um `JPanel` que implementa `IPainelPedidoView`;
`PainelPedidoView` apenas hospeda o painel. O painel pode ser renderizado sem
um servidor grafico.

#### Scenario: Print gerado sem X
- **WHEN** o teste headless monta o painel com dados de exemplo e o pinta
- **THEN** `docs/painel-pedidos.png` e produzido com conteudo nas seis colunas

### Requirement: Preservacao das regras de negocio
BR12 (ciclo de vida), BR13 (cancelamento e credito), filtros, ordenacao, SLA e
instancia unica permanecem como estao.

### Requirement: Look and Feel definido pela aplicacao cliente
O componente nao define Look and Feel; a aplicacao cliente e quem o define.

#### Scenario: Componente nao altera o Look and Feel
- **WHEN** o painel e iniciado
- **THEN** o componente nao chama `UIManager.setLookAndFeel`
- **AND** o Look and Feel permanece o definido pela aplicacao cliente

### Requirement: Botao Fechar encerra a janela
O botao "Fechar" do painel encerra a janela criada pela fachada.

#### Scenario: Fechar o painel
- **WHEN** o atendente aciona "Fechar"
- **THEN** a janela do painel e encerrada
