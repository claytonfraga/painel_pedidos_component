# Painel de Acompanhamento de Pedidos (MVP Passive View)

## Contexto

Aplicacao desktop Swing que apresenta o Painel de Acompanhamento de Pedidos do
delivery, organizado em seis colunas de situacao (Novo, Em preparo, Pronto para
entrega, Em rota de entrega, Entregue e Cancelado), com busca por numero do
pedido ou nome do cliente, filtro por periodo e acoes de transicao condicionadas
as regras de negocio BR12 e BR13.

## Arquitetura

- Padrao de apresentacao adotado: **MVP (Model-View-Presenter), variante Passive
  View**, conforme o material de referencia indexado em
  `openspec/references/Model-View-Presenter.md`.
- A View e abstraida por `IPainelPedidoView`; o Presenter depende dessa
  abstracao e nao conhece a implementacao concreta.
- A `PainelPedidoView` (concretizacao da interface) nao conhece o Model, apenas
  expoe componentes e permanece passiva quanto ao fluxo.
- A `PainelPedidoPresenter` coordena: configura os listeners, le a View, aciona
  o Model/Repository e instrui a View sobre como atualizar a apresentacao.
- O Model (`Pedido`) concentra estado e as regras de transicao de situacao.
- O repositorio em memoria (`PedidoEmMemoriaRepository`) implementa a interface
  `IPedidoRepository`.
- A composicao das dependencias (View concreta + servico/repositorio + Presenter)
  ocorre no ponto de entrada (`aplicacao`), como em `AplicacaoPrincipal` do
  material de referencia.
- Interface grafica em Swing.

## Material de referencia (indexado)

- `openspec/references/Model-View-Presenter.md` - documento de referencia do
  padrao MVP (Potel, 1996; Fowler, 2006; Vreca, 2023; Zhang e Luo, 2010; Alles et
  al., 2006), incluindo a variante Passive View e o exemplo de login.
- Indice em `openspec/references/README.md`.

## Adaptacoes do padrao neste projeto

- O material de referencia usa `ActionListener` anonimo no Presenter; este projeto
  segue esse padrao: listeners anonimos no ponto de uso (sem criar classes de
  listener e sem lambda).
- O material mantem `JOptionPane` no Presenter (variante principal). Este projeto
  adota essa variante; a alternativa com abstracao `ExibidorMensagem` fica
  registrada como evolucao possivel para testabilidade.
- A View concreta e criada e injetada pelo ponto de entrada (`aplicacao`), e o
  Presenter recebe a abstracao `IPainelPedidoView` por construtor.

## Dados de exemplo (seed)

- O seed vive em `br.projeto.painelpedidos.seeder.PedidoSeeder`, separado do
  repositorio (SRP). O repositorio nao cria pedidos: recebe a lista pronta.
- O `PedidoSeeder` e deterministico: recebe a quantidade e uma data de referencia
  e deriva todas as datas/valores dela, sem `Random` nem relogio do sistema.
- A quantidade vem do arquivo `.env` na raiz do projeto (chave
  `SEED_QUANTIDADE`, ex.: 1000), lida por `configuracao/LeitorEnv`, um Adapter
  que encapsula a biblioteca dotenv (`io.github.cdimascio:dotenv-java`); nenhuma
  outra classe conhece a biblioteca. O ponto de entrada injeta o valor.
- Gera os pedidos nas seis situacoes e em meses/dias do mes variados, conduzindo
  as transicoes do dominio (nunca define a situacao por string).
- Persistencia em memoria atras de `IPedidoRepository`; um banco (H2) so se for
  exigencia de entrega (ver `.design/pedido-seeder.md`).

## Simulacao de novo pedido

- Com a aplicacao aberta, um simulador cria pedidos em intervalos aleatorios
  (minimo configuravel, >= 30s), simulando clientes.
- Configuracao no `.env`: `SIMULACAO_INTERVALO_MINIMO_SEGUNDOS`,
  `SIMULACAO_INTERVALO_MAXIMO_SEGUNDOS` e `SIMULACAO_MENSAGEM_SEGUNDOS`.
- Ao criar: alerta sonoro (Strategy `IEstrategiaSom`), foco no card e label
  "Novo Pedido #<numero>" (card pequeno laranja escuro, texto branco negrito) que
  some apos o tempo configurado; os cards novos piscam por esse tempo.
- Ate N pedidos por ciclo (`SIMULACAO_PEDIDOS_POR_CICLO`). A notificacao usa o
  Chain of Responsibility adaptado (`NotificacaoNovoPedidoService` +
  `Regra...Handler`).
- SOLID: `ISimuladorNovoPedido`/`ObservadorNovoPedido` (o Presenter e observador),
  `IGeradorPedido`, `IRelogioService` e `IAleatoriedadeService` como abstracoes;
  `CatalogoPedido` compartilha os dados entre seeder e gerador.

## Instancia unica (Singleton)

- A aplicacao abre apenas uma vez: `InstanciaUnicaAplicacao` e um Singleton
  thread-safe (double-checked locking), ponto unico de criacao/execucao.
- A composicao fica em `AplicacaoPainelPedido` (unico `main`), que implementa
  `IExecucaoAplicacao`; a trava entre processos e `ITravaInstanciaUnica`
  (`FileLock`). DIP/OCP/SRP preservados.

## Filtro de periodo e data

- Periodo: filtros padrao (Hoje, ultimos 7, ultimos 30, todos) ou data especifica
  via datepicker `spnData` (`JSpinner` com `SpinnerDateModel`), exibido somente
  quando "Data especifica" esta selecionada.
- Abstracoes: `IFiltroPeriodo` (+ `FiltroHoje`, `FiltroUltimos7Dias`,
  `FiltroUltimos30Dias`, `FiltroTodos`, `FiltroDataEspecifica`) e
  `IFabricaFiltroPeriodo` (`FabricaFiltroPeriodo`).
- Data do novo pedido simulado: `IFonteDataPedido` (`FonteDataPedido`), atualizada
  pelo Presenter; quando "Data especifica" esta ativa, o pedido e criado na data
  escolhida, senao na data atual.

## Tecnologia

- Java 21 (`maven.compiler.release` = 21), Maven 3.9.x, encoding UTF-8.
- Sem `maven-enforcer-plugin`: a compilacao usa `release 21`, permitindo que o
  Maven rode sobre JDK mais novo (ex.: NetBeans com JDK 25).
- Pacote raiz `br.projeto`; dominio em `br.projeto.painelpedidos.model`.
- Layout de camadas: `model`, `repositorio`, `presenter`, `view`,
  `aplicacao`, `configuracao`.

## Fonte da verdade

- `openspec/specs/**` e os arquivos `.feature` associados sao a fonte da verdade
  do comportamento do sistema.
- Cada `.feature` fica na pasta da capacidade correspondente e e indexado no
  `spec.md` da mesma pasta.
- A historia de usuario de referencia e a US21 (Acompanhar pedidos), com as
  regras BR12, BR13 e o fluxo de pagamento US23.

## Dominio

O `Pedido` reaproveita a base do delivery (`Cliente`, `Item`,
`CupomDescontoEntrega`, `CupomDescontoPedido`, taxa de entrega) e acrescenta as
informacoes exigidas pela tela: identificador, data e hora, endereco de entrega,
forma de pagamento, data e hora do pagamento e credito gerado.

### Ciclo de vida (GoF State)

O estado do pedido e modelado com GoF State no estilo do exemplo `Porta`:
- `br.projeto.painelpedidos.state.PedidoState` (abstrata) mantem `protected final
  Pedido pedido` e operacoes default que lancam `RuntimeException`.
- Estados concretos `PedidoNovoState`, `PedidoEmPreparoState`,
  `PedidoProntoParaEntregaState`, `PedidoEmRotaDeEntregaState`,
  `PedidoEntregueState`, `PedidoCanceladoState` sobrescrevem as operacoes
  permitidas e transicionam com `pedido.setEstado(new PedidoProximoState(pedido))`.
- Cada estado valida pre-condicoes no proprio construtor; se nao atendidas, lanca
  excecao e a transicao nao ocorre. `PedidoCanceladoState` calcula o credito no
  construtor.
- `Pedido` e o contexto: `estado = new PedidoNovoState(this)`, delega as operacoes
  e expoe `getEstado()`/`setEstado(PedidoState)`.
- A cadeia: Novo -> Em preparo (apos pagamento) -> Pronto para entrega ->
  Em rota de entrega -> Entregue; qualquer estado nao cancelado pode ir para
  Cancelado. Cancelado nao retorna.
- O repositorio em memoria semeia 40 pedidos exercitando essas transicoes, sem
  conhecer strings de situacao.
