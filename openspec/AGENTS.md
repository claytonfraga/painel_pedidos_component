# AGENTS.md - Regras do projeto

Este diretorio `openspec/` e a fonte da verdade do comportamento do sistema.
Leia `project.md` e `specs/**/spec.md` antes de alterar qualquer codigo.

## Fluxo obrigatorio

1. Antes de codar, confirme a mudanca contra `specs/**/spec.md` e os `.feature`.
2. Ao alterar comportamento, atualize primeiro o `.feature` (Gherkin) e o
   `spec.md` da capacidade e, em seguida, o codigo.
3. Ao terminar qualquer alteracao, rode `mvn -U clean verify` na raiz.
   - Use `mvn -U -DskipTests clean verify` apenas quando a mudanca for
     estritamente documentacao/formatacao.
   - O build exige JDK 21 ou superior (enforcer `[21,)`); o codigo e compilado
     com `--release 21` (bytecode Java 21), podendo o Maven rodar em JDK mais
     novo (ex.: 24).
4. O projeto possui suite de testes automatizados (JUnit Jupiter + Hamcrest)
   em `painel-pedidos/src/test` e `exemplo/src/test`. Testes novos garantem os
   requisitos da mudanca; nomes em portugues sem caracteres especiais.
   - Os testes sao o **mecanismo de protecao contra falhas geradas no codigo por
     IA**: existem para **garantir os requisitos** da spec/`.feature`, nao para
     cobrir linhas. Cada requisito deve ter teste correspondente; o teste deve
     **falhar** se o requisito for violado; e o codigo (inclusive gerado por IA)
     so e aceito com a suite inteira verde.
   - Os fakes de teste sao **funcionais** (comportam-se como um consumidor real,
     como o exemplo), ficam em `src/test` e os testes do componente **NAO**
     dependem do modulo `exemplo`.
   - Unitarios com Surefire (`*Test`) e integracao com Failsafe (`*IT`).
   - TODO teste (unitario e de integracao) usa o padrao **given/when/then** com
     comentarios explicitos (`// given`, `// when`, `// then`), baseado nas
     especificacoes e nos `.feature`.
   - NAO usar Mockito; use fakes escritos a mao (em `src/test`).
   - Gate de qualidade: so prosseguir com **todos** os testes verdes em
     `mvn -U clean verify`.

## Estrutura (multi-modulo Maven)

- `painel-pedidos-parent`: agregador (packaging `pom`).
- `painel-pedidos`: o componente (biblioteca). Utiliza `maven-shade-plugin` para
  produzir um jar que contem todas as dependencias.
- `exemplo`: projeto consumidor de demonstracao (depende de `painel-pedidos`).
- Java 21 (`maven.compiler.release=21`); encoding UTF-8.

## Regras de codigo (obrigatorias em todo o codigo, inclusive Presenter e Repository)

- NAO use lambda.
- NAO use Stream API (nem `Comparator.comparing`, nem method references).
- `for` com contador SOMENTE quando o contador for necessario: o indice e usado
  no corpo (posicao, `buffer[2 * i]`), o laco percorre uma contagem e nao uma
  colecao, ou o proprio valor do contador e o conteudo. Ao percorrer uma colecao
  sem precisar do indice, use o **for-each** (`for (Tipo item : colecao)`), sem
  lambda e sem Stream.
- NAO use `enum`.
- NAO use `instanceof`.
- NAO use `implements Runnable`; sem `SwingUtilities.invokeLater`.
- NAO use `java.math.BigDecimal`; use `double` para valores monetarios.
- NAO use `java.util.Optional`; use retorno direto.
- NAO use atributos estaticos, incluindo constantes `public static final`. O
  projeto nao tem nenhum campo estatico.
- Listeners com classes anonimas no ponto de uso; nao criar arquivos de listener.
- NAO criar classes internas/anonimas para outros fins alem de listeners (em
  testes, classes anonimas de `Executable` para `assertThrows` sao permitidas).
- Excecoes unchecked (`IllegalArgumentException`, `RuntimeException`); nao lancar
  checked.
- `Objects.requireNonNull` para nulidade de parametros e atributos.
- Todo arquivo `.java` DEVE conter o comentario de autoria no padrao NetBeans,
  antes da declaracao do tipo, com o autor `clayton`.
- Identificadores em portugues do Brasil sem acentos nem caracteres especiais.
- Sufixos de padrao em ingles: `Service`, `Repository`, `State`, `Handler`,
  `Strategy`, `Observer`, `Facade` (ex.: `PainelPedidoFacade`).
- `Model` (dados de dominio) nao recebe sufixo de padrao nem de arquitetura.
- Interfaces com prefixo `I`; classes no singular.
- Sem `HashMap`/`HashSet` onde a ordem importa; use estruturas ordenadas.
- Visibilidade minima: prefira package-private a `public`.
- Cada classe tem NO MAXIMO **1 construtor** (sem sobrecarga de construtores).
- Classes concretas que NAO sao pontos de extensao sao **`final`**. Excecoes:
  classes abstratas (`PedidoAbstract`, `PedidoState`) e as Views
  (`PainelPedidoPanel`, `PainelPedidoView`), que permanecem estendiveis.
- Trate excecoes em fronteiras; nao engula excecoes.

## Mensagens de excecao (orientar quem vai corrigir)

- TODA mensagem de excecao - inclusive as de `Objects.requireNonNull` - diz **o
  que fazer** para resolver, no imperativo, nomeando **especificamente** o que
  falta. Nunca o nome do parametro nem um substantivo solto.
- Verificacao de nulo: a mensagem COMECA com `Informe ` e descreve o item
  especifico - ex.: `Objects.requireNonNull(pedidoRepository, "Informe um
  repositorio de pedidos valido")`, e nao `"pedidoRepository"`.
- Verificacao `if` que levanta excecao: a mensagem comeca pelo verbo da acao -
  `Informe ...`, `Configure ...`, `Escolha ...`, `Use ...`.
- Operacao invalida no estado (GoF State): a mensagem diz **onde** a operacao e
  valida - ex.: `"Nao e possivel registrar pagamento de um pedido Cancelado;
  registre o pagamento em um pedido Novo."`.
- Sem acentos nem caracteres especiais (regra geral do projeto).
- Garantido por `MensagemExcecaoTest` (varredura do codigo de producao dos dois
  modulos): nenhuma verificacao de nulo deixa de comecar com `Informe `, e
  nenhuma excecao e levantada com um identificador solto como mensagem.

## Contrato publico e pacotes internos

- O contrato publico do componente e EXATAMENTE: `aplicacao.PainelPedidoFacade`,
  `model.PedidoAbstract`, `repositorio.IPedidoRepository`,
  `configuracao.ConfiguracaoPainelPedido`,
  `configuracao.LeitorConfiguracaoPainelPedido`,
  `listener.IVisualizarPedidoListener`, `listener.IConfirmarPagamentoListener`,
  `listener.ICriarNovoPedidoListener`, `som.IEstrategiaSom`,
  `som.SomTomStrategy`, `tempo.IRelogioService`, `tempo.RelogioServiceSistema`,
  `view.IPainelPedidoView`, `view.PainelPedidoPanel`.
- Todo o resto vive sob `br.projeto.painelpedidos.internal.*`: estado, filtro,
  notificacao, SLA, ordenacao, ingestao, presenter, a janela invólucro, a trava
  de instancia unica e a fonte de data.
- `ContratoPublicoTest` varre o codigo de producao e falha nomeando o tipo que
  entrou ou saiu do contrato. Nao acrescente tipo publico fora do contrato sem
  decidir que ele e API - a fronteira nao e imposta pelo compilador, e o gate.
- Embutir o painel usa `getPainelPedido()` e `iniciarEmbutido()`; montar o
  `PainelPedidoPresenter` a mao NAO e caminho suportado.

## Excecoes aceitas a SOLID, DRY e Demeter

Varredura feita em 2026-10-11. Estas NAO sao violacoes a corrigir - sao o preco
de padroes que o proprio projeto manda, e cada uma tem a sua razao:

- **LSP - `PedidoState` lanca nas operacoes default.** E o estilo de State
  mandado aqui: o contrato da base declara que a operacao pode ser invalida no
  estado. Nao "consertar" transformando as operacoes em no-op silencioso.
- **Lei de Demeter - `view.getX().metodo()` no presenter.** E o Passive View por
  getters, adotado aqui. Trocar por metodos de intencao na View e redesenho de
  arquitetura, nao limpeza.
- **DRY - `MensagemExcecaoTest` repetido nos dois modulos.** Deliberado: o teste
  do componente NAO pode depender do `exemplo`, e nao ha test-jar compartilhado.
- **DIP - `AgendadorMonitorSlaService` usa `javax.swing.Timer`.** O callback roda
  na EDT, que e o que torna a atualizacao da tela segura; `java.util.Timer`
  exigiria `SwingUtilities.invokeLater`, proibido aqui, e extrair uma porta
  recriaria a abstracao de implementacao unica que ja foi removida.

O que sobra como trabalho real esta em `.design/rfc-estruturais-do-componente.md`
(SRP do presenter, contrato expondo tipo interno, branching por estado e a
fabrica de periodo acoplada ao indice do combo).

## Sem simulacao, sem dados mockados

- O componente NAO gera, sorteia, semeia nem embute pedidos. Nao ha `seeder`,
  `dados`/`CatalogoPedido`, `simulacao` nem `LeitorEnv`.
- Novos pedidos entram por **empurrao**: o consumidor chama
  `PainelPedidoFacade.receberNovosPedidos(List<PedidoAbstract>)` (GoF Observer: o
  consumidor e o sujeito/fonte do evento e o componente e o observador). Nao ha
  leitura automatica.
- O componente **NAO deduplica**: quem decide o que e novo e o consumidor, entao
  ele empurra apenas o que ainda nao foi notificado.
- O teste `EstruturaComponenteTest` garante a ausencia dessas classes.

## Configuracao (`painel_pedidos.env`)

- Lida APENAS por `configuracao/LeitorConfiguracaoPainelPedido` (Adapter). Nenhuma
  outra classe conhece a biblioteca dotenv.
- O arquivo e OBRIGATORIO no uso do componente, e as quatro chaves de SLA por
  estado tambem. Ausentes, lanca excecao (`IllegalStateException`).
- Chaves de SLA por estado (minutos): `SLA_NOVO_MINUTOS`,
  `SLA_EM_PREPARO_MINUTOS`, `SLA_PRONTO_PARA_ENTREGA_MINUTOS`,
  `SLA_EM_ROTA_DE_ENTREGA_MINUTOS`.
- Chave obrigatoria: `IDENTIFICADOR_APLICACAO` (identidade da aplicacao;
  distingue este painel dos demais na mesma maquina).
- As chaves de SLA sao as **situacoes do ciclo** (`Novo`, `Em preparo`,
  `Pronto para entrega`, `Em rota de entrega`): uma chave que nao casa com
  nenhuma delas faz o SLA **nao se aplicar**, sem aviso - por isso use o
  `LeitorConfiguracaoPainelPedido`, e os testes `PedidoTest` e
  `LeitorConfiguracaoPainelPedidoTest` fixam esse contrato.
- Demais chaves (defaults): `SLA_MONITOR_INTERVALO_SEGUNDOS`,
  `NOTIFICACAO_DURACAO_SEGUNDOS`, `SOM_ATIVO`, `TITULO_JANELA`.
- `painel_pedidos.env.example` documenta.
- Dependencia `dotenv-java` (escopo `compile`), encapsulada pelo Adapter.

## Portas do componente

- `IPedidoRepository` (dados): `salvar`, `buscarTodos`, `gerarProximoIdentificador`.
  Implementada pelo consumidor (ex.: `exemplo/.../PedidoEmMemoriaRepository`).
- `IEstrategiaSom`, `IRelogioService`, `IFonteDataPedido`: portas de
  comportamento com implementacoes default.

## Modelo abstrato (PedidoAbstract)

- O componente NAO possui pedido concreto. O modelo e a classe abstrata
  `model.PedidoAbstract`, que carrega os dados essenciais do painel e a maquina
  de estados (GoF State).
- O sistema consumidor estende `PedidoAbstract` e implementa
  `criarCopia(String, LocalDateTime)` com o seu tipo concreto.
- `IPedidoRepository`, notificacao, SLA, ordenacao e a
  Presenter operam sobre `PedidoAbstract`.
- `EstruturaComponenteTest` garante a ausencia de `model.Pedido`.

## Acoes do cliente (listeners obrigatorios)

- "Visualizar pedido" e "Confirmar pagamento" sao listeners obrigatorios do
  cliente: `IVisualizarPedidoListener` e `IConfirmarPagamentoListener`.
- "Novo Pedido" tambem: `ICriarNovoPedidoListener`, cujo `criarNovoPedido()`
  devolve `List<PedidoAbstract>`; o componente salva o retorno e atualiza o
  painel (o componente nao cria pedidos). O botao fica a esquerda de
  "Visualizar pedido".
- Sem eles, `PainelPedidoFacade.iniciar()` lanca `IllegalStateException` e a acao
  correspondente tambem. O componente NAO abre a tela de pedido nem a de
  pagamento; quem as exibe e o cliente.
- O botao "Fechar" encerra apenas a janela do painel; o componente nao encerra a
  aplicacao.

## Nomes: singular e sufixo de papel

- Classes, classes abstratas e interfaces usam nome no SINGULAR.
- Classes de servico usam o sufixo `Service` (ex.: `AgendadorMonitorSlaService`).

## Padrao arquitetural obrigatorio: MVP Passive View

- **Model**: dados/estado e regras de dominio; sem dependencia de Swing.
- **View**: passiva; `IPainelPedidoView` e materializada por
  `PainelPedidoPanel` (um `JPanel` embedavel) e, opcionalmente, por
  `PainelPedidoView` (um `JFrame` invólucro). A View nao configura listeners e
  nao possui `main`; expoe apenas getters dos elementos usados pela Presenter.
- **Presenter**: coordena o caso de uso; configura listeners, le a View, aciona
  Model/servicos e instrui a View. NAO exibe nem fecha a janela (isso e da
  fachada `PainelPedidoFacade`); a Presenter usa `getPainelPrincipal()` e comanda
  `maximizar()` (a View permanece passiva).
- **Fachada** `PainelPedidoFacade`: composicao das dependencias e execucao.
- A View e abstrata por `IPainelPedidoView`; a implementacao e injetada pela
  fachada. A Presenter nao instancia a View.

## Nomenclatura de componentes Swing

Prefixos por tipo, em campos e variaveis locais: `btn`, `chk`, `cmb`, `dtp`,
`lbl`, `mnu`, `pnl`, `scr`, `spn`, `tab`, `tbl`, `txt`, `txtFmt`, `opt`, etc.
A View nao possui setters publicos.

## Ciclo de vida do pedido (GoF State no estilo Porta)

- `model/PedidoState` (abstrata, package-private) com `protected final Pedido pedido`,
  operacoes default que lancam `RuntimeException`.
- O State mora no pacote `model`, junto do contexto, e NAO e publico: assim o
  contrato nao expoe tipo interno e o estavel nao depende do detalhe
  (`setEstado` e package-private, chamado pelos estados).
- Estados concretos com prefixo `Pedido` e sufixo `State`, transicionando via
  `pedido.setEstado(new PedidoProximoState(pedido), quando)`.
- O instante da transicao vem de FORA: as operacoes recebem `quando` e `setEstado`
  grava em `dataHoraMudancaEstado`. O Model NUNCA le o relogio do sistema - quem
  dispara informa a hora (consumidor no listener, presenter com `IRelogioService`),
  e o SLA mede com esse mesmo relogio.
- Regras no construtor: pre-condicao nao atendida lanca excecao e a transicao
  nao ocorre (`PedidoCanceladoState` calcula o credito no construtor).
- `Pedido` e o contexto; sem `enum`, sem `instanceof`, sem strings de situacao.

## Prioridade por SLA, ordenacao e Observer

- SLA fixo por estado, em minutos: Novo `SLA_NOVO_MINUTOS`, Em preparo
  `SLA_EM_PREPARO_MINUTOS`, Pronto para entrega
  `SLA_PRONTO_PARA_ENTREGA_MINUTOS`, Em rota de entrega
  `SLA_EM_ROTA_DE_ENTREGA_MINUTOS`; Entregue e Cancelado sem SLA. Nao ha media
  por bairro.
- `AgendadorMonitorSlaService` opera o CoR adaptado (`MonitorSlaService` +
  `RegraSlaHandler`) usando `IRelogioService` (relogio injetado, nao o do
  sistema). Marca `pedido.marcarPrioridade()`; a prioridade some ao mudar de
  estado.
- O SLA por estado vive num unico mapa
  (`ConfiguracaoPainelPedido.getSlaPorSituacao`, situacao -> duracao) e e
  aplicado por `RegraSlaEstadoHandler` (generico): um SLA novo e uma entrada no
  mapa, sem nova classe nem construtor maior (OCP).
- Atualizacoes de tela decorrentes de eventos usam Observer (GoF), com sufixo
  `Observer`.
- Ordenacao em fila (FIFO) via `IOrdenacaoPedido`.

## Notificacao de novos pedidos

- `NotificacaoNovoPedidoService` (CoR adaptado) mantem a colecao de
  `RegraHandler` e executa as aplicaveis na ordem.
- O som usa Strategy (`IEstrategiaSom`); `SomSilenciosoStrategy` quando
  `SOM_ATIVO=false`.
- O label de notificacao e um card laranja escuro (RGB 194,88,0) com texto branco
  em negrito; os cards novos piscam por `NOTIFICACAO_DURACAO_SEGUNDOS`.

## Filtro de periodo e data do novo pedido

- `IFiltroPeriodo` (Hoje, ultimos 7, ultimos 30, todos, data especifica) criada
  por `IFabricaFiltroPeriodo`; a Presenter depende da abstracao.
- A data do novo pedido vem de `IFonteDataPedido`, sincronizada com o periodo
  selecionado.

## Instancia unica

- O COMPONENTE possui a trava de instancia unica; o consumidor nao configura
  trava, caminho nem singleton.
- `PainelPedidoFacade.iniciar()` adquire a trava antes de criar a janela e
  devolve `boolean` (`false` = nada foi aberto). `encerrar()` libera a trava.
- A recusa NAO e obrigacao tecnica de tratar (Java permite ignorar o retorno),
  mas quem a engole deixa o app "nao fazendo nada" na segunda execucao. No
  `exemplo` ela e sinalizada com `System.out.println`, porque o exemplo roda por
  console (`exec-maven-plugin`/IDE); nao trocar por dialogo sem mudar a forma de
  lancamento.
- `System.out.println` e o canal permitido de um modulo de demonstracao (o
  `main` do `exemplo`); em classe que nao e ponto de entrada, use o dialogo do
  componente.
- A trava e a fonte unica da verdade: a fachada NAO guarda flag "ja aberto"; o
  defeito antigo era o singleton de JVM nunca resetar.
- Trava padrao: `TravaInstanciaUnicaArquivo` sobre um arquivo em
  `java.io.tmpdir` cujo nome deriva da identidade da aplicacao
  (`IDENTIFICADOR_APLICACAO`), de modo que dois sistemas diferentes na mesma
  maquina convivem; "ja travado" (`tryLock()==null` ou
  `OverlappingFileLockException`) e recusa, nunca excecao.
- A trava e detalhe interno (package-private): NAO e porta nem ponto de extensao;
  nao recrie interface para ela sem um consumidor que a implemente.
- O arquivo de trava NUNCA e apagado: apaga-lo e o que quebra a exclusao.
- A mensagem ao usuario e do consumidor (o componente apenas sinaliza).

## Build e distribuicao

- `mvn -U clean verify` na raiz compila e testa os tres modulos.
- O jar de `painel-pedidos` contem as dependencias (maven-shade), para uso como
  dependencia unica (JitPack).
- NAO versionar `target/`, `dependency-reduced-pom.xml` nem arquivos gerados.

## Convencao de nomenclatura de padroes

- `State`, `Service`, `Repository`, `Handler`, `Strategy`, `Observer` sao
  sufixos. Modelos de dados puros usam apenas o conceito de dominio (`Pedido`).
- Interfaces comecam com `I` (ex.: `IPedidoRepository`, `IPainelPedidoView`).
- Metodos que **configuram um colaborador** (porta/listener) usam o prefixo `set`
  (`setVisualizarPedidoListener`, `setConfirmarPagamentoListener`). Metodos com
  semantica de **acao** permanecem descritivos (`usarDataAtual`).

## Commits (Conventional Commits)

- SEMPRE que houver alteracoes, faca commit, inferindo a mensagem e classificando
  o tipo: `feat`, `fix`, `refactor`, `build`, `test`, `docs`, `chore`.
- Mensagem no imperativo, em portugues, com escopo quando util (ex.:
  `feat(exemplo): ...`, `docs(openspec): ...`).
- Commits pequenos e por escopo, na ordem que reconstroi a estrutura; valide com
  `mvn -U clean verify` antes de concluir a entrega.

## Sincronizacao com o Windows (obrigatoria)

- SEMPRE que copiar/espelhar qualquer arquivo para o WSL, remova TODOS os
  arquivos `desktop.ini` resultantes, recursivamente, antes de concluir.
- Ao terminar qualquer entrega, espelhe o projeto COMPLETO (`pom.xml`, modulos,
  `openspec/`, `README.md`, `painel_pedidos.env.example`, `docs/`) para
  `/mnt/c/Users/clayt/Downloads/painel_pedidos_mvp`, sobrescrevendo o destino.
- NAO sincronize `.git/`, `target/` nem pastas de ferramentas de agente/IDE
  (`.opencode/`, `.claude/`, `.cursor/`, `.windsurf/`, `.agents/`, `.gemini/`,
  `.agent/`, `.codex/`, `.github/`).
- Nunca versione `desktop.ini`, `target/` nem imagens temporarias.
- Depois de cada mudanca, remova imagens temporarias (`/tmp/opencode/`,
  `/tmp/*.png`) apos confirmar que a informacao foi extraida para codigo, specs
  e commits.
