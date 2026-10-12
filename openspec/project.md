# Painel de Acompanhamento de Pedidos - Componente Reutilizavel

## Contexto

Biblioteca Java (Swing) que apresenta o Painel de Acompanhamento de Pedidos de um
delivery, organizado em seis colunas de situacao (Novo, Em preparo, Pronto para
entrega, Em rota de entrega, Entregue e Cancelado), com busca por numero do
pedido ou nome do cliente, filtro por periodo e acoes de transicao condicionadas
as regras de negocio BR12 e BR13.

O projeto deixa de ser uma aplicacao autonoma e passa a ser um **componente
reutilizavel**: e consumido como dependencia Maven por qualquer sistema de
delivery, que implementa as portas de dados e executa o painel. Nao ha
simulacao, geracao de pedidos, seed nem dados mockados.

## Arquitetura

- Padrao de apresentacao: **MVP (Model-View-Presenter), variante Passive View**.
- A View e abstraida por `IPainelPedidoView` e materializada por
  `PainelPedidoPanel` (`JPanel` embedavel) e `PainelPedidoView` (`JFrame`
  invólucro). A apresentacao nao depende de uma janela.
- A `PainelPedidoPresenter` coordena: configura os listeners, le a View, aciona
  o Model/Repository e instrui a View sobre como atualizar.
- O Model nao possui classe concreta: e a classe abstrata `PedidoAbstract`, que
  carrega os dados essenciais do painel e a maquina de estados (GoF State). O
  sistema consumidor a estende e implementa `criarCopia`. As acoes de visualizar
  pedido e confirmar pagamento sao listeners obrigatorios do cliente.
- A porta de dados e `IPedidoRepository`, implementada pelo consumidor.
- Novos pedidos entram por empurrao: o consumidor chama
  `PainelPedidoFacade.receberNovosPedidos(List<PedidoAbstract>)` (GoF Observer).
- A fachada `PainelPedidoFacade` e o ponto de entrada do componente: compoe a View, a
  Presenter, o repositorio, as estrategias e os agendadores, e executa o painel.
- Interface grafica em Swing; modulo de exemplo demonstra a integracao.

## Modulos Maven

- `painel-pedidos-parent`: agregador (packaging `pom`), Java 21 (bytecode
  `--release 21`), enforcer `[21,)` (Maven pode rodar em JDK 21 ou superior).
- `painel-pedidos`: a biblioteca. maven-shade embute as dependencias; o jar
  publicado contem tudo (dotenv incluso).
- `exemplo`: consumidor de demonstracao (depende de `painel-pedidos`), com
  repositorio em memoria, dados de exemplo estaticos e `painel_pedidos.env`.

## Material de referencia (indexado)

- `references/Model-View-Presenter.md` - documento de referencia do padrao MVP
  (Potel, 1996; Fowler, 2006; Vreca, 2023; Zhang e Luo, 2010; Alles et al.,
  2006), incluindo a variante Passive View.

## Ciclo de vida (GoF State)

- `model/PedidoState` (abstrata, package-private) mantem `protected final Pedido pedido` e
  operacoes default que lancam `RuntimeException`. O State mora com o contexto,
  no pacote `model`, para o contrato nao expor tipo interno.
- Estados concretos `PedidoNovoState`, `PedidoEmPreparoState`,
  `PedidoProntoParaEntregaState`, `PedidoEmRotaDeEntregaState`,
  `PedidoEntregueState`, `PedidoCanceladoState` sobrescrevem as operacoes
  permitidas e transicionam com `pedido.setEstado(new PedidoProximoState(pedido))`.
- Cada estado valida pre-condicoes no proprio construtor; se nao atendidas,
  lanca excecao e a transicao nao ocorre. `PedidoCanceladoState` calcula o
  credito no construtor.
- `Pedido` e o contexto: `estado = new PedidoNovoState(this)`, delega as
  operacoes e expoe `getEstado()`/`setEstado(PedidoState)`.

## Ingestao de novos pedidos (empurrao, substitui a simulacao)

- O consumidor **empurra** os novos pedidos: chama
  `PainelPedidoFacade.receberNovosPedidos(List<PedidoAbstract>)` quando chega um
  pedido no sistema dele. Aceita um ou varios.
- GoF Observer: o consumidor e o sujeito/fonte do evento e o componente e o
  observador (`NovoPedidoObserver`), que dispara a cadeia de notificacao
  (`NotificacaoNovoPedidoService` + `RegraHandler`).
- Nenhum pedido e gerado, sorteado, semeado nem lido automaticamente pelo
  componente.

## Configuracao (`painel_pedidos.env`)

O arquivo e **obrigatorio**; as quatro chaves de SLA por estado tambem.
Chaves: `SLA_NOVO_MINUTOS`, `SLA_EM_PREPARO_MINUTOS`,
`SLA_PRONTO_PARA_ENTREGA_MINUTOS`, `SLA_EM_ROTA_DE_ENTREGA_MINUTOS`,
`SLA_MONITOR_INTERVALO_SEGUNDOS`, `NOTIFICACAO_DURACAO_SEGUNDOS`, `SOM_ATIVO`,
`TITULO_JANELA`. Lidas por
`LeitorConfiguracaoPainelPedido` (Adapter sobre dotenv). Ausentes, valem os
defaults do codigo (documentados em `painel_pedidos.env.example`).

## Instancia unica (do componente)

- O componente **possui** a instancia unica; o consumidor nao configura trava,
  caminho nem singleton.
- `PainelPedidoFacade.iniciar()` adquire uma trava de arquivo
  (`TravaInstanciaUnicaArquivo`, via `FileLock`) antes de criar a janela e
  devolve `boolean` (`false` = nada foi aberto); `encerrar()` libera a trava.
- A trava e a fonte unica da verdade (nao ha flag "ja aberto" em memoria); o
  arquivo nunca e apagado.

## Filtro de periodo e data

- `IFiltroPeriodo` (+ `FiltroHoje`, `FiltroSemana`, `FiltroMes`,
  `FiltroCompleto`, `FiltroDataEspecifica`) e `IFabricaFiltroPeriodo`
  (`FabricaFiltroPeriodo`).
- Data do novo pedido: `IFonteDataPedido` (`FonteDataPedido`), atualizada pela
  Presenter; quando "Data especifica" esta ativa, o pedido e criado na data
  escolhida, senao na data atual. O relogio e injetado (`IRelogioService`).

## Tecnologia

- Java 21 (`maven.compiler.release` = 21), Maven 3.9.x, encoding UTF-8.
- Multi-modulo; `maven-shade-plugin` para o jar com dependencias.
- Testes: JUnit Jupiter + Hamcrest (Surefire); sem Failsafe por enquanto.
- Pacote raiz `br.projeto`; dominio em `br.projeto.painelpedidos.model`.

## Fonte da verdade

- Visao geral: `openspec/README.md`; produto: `openspec/prd.md`.
- `openspec/specs/**` e os `.feature` sao a fonte da verdade do comportamento.
- Design tecnico: `openspec/specs/**/design.md`.
- Historias: US21 (Acompanhar), US22 (Filtro), US24 (Prioridade/SLA/ordenacao),
  US25 (Instancia unica), US26 (Ingestao sem simulacao) e US27 (Componente
  reutilizavel).
- Mudancas concluidas em `openspec/changes/archive/`; a mudanca deste componente
  em `openspec/changes/archive/2026-10-11-componente-reutilizavel/`.
