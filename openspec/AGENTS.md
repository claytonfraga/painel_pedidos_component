# AGENTS.md - Regras do projeto

Este diretorio `openspec/` e a fonte da verdade do comportamento do sistema.
Leia `project.md` e `specs/**/spec.md` antes de alterar qualquer codigo.

## Fluxo obrigatorio

1. Antes de codar, confirme a mudanca contra `specs/**/spec.md` e os `.feature`.
2. Ao alterar comportamento, atualize primeiro o `.feature` (Gherkin) e o
   `spec.md` da capacidade e, em seguida, o codigo.
3. Ao terminar qualquer alteracao, rode `mvn -U clean verify`.
   - Use `mvn -U -DskipTests clean verify` apenas quando a mudanca for
     estritamente documentacao/formatacao.
4. Este projeto NAO possui suite de testes automatizados: nao criar testes,
   dependencias de teste (JUnit/Hamcrest) nem o diretorio `src/test`.

## Regras de codigo (obrigatorias em todo o codigo, inclusive Presenter e Repository)

- NAO use lambda.
- NAO use Stream API (nem `Comparator.comparing`, nem method references).
- NAO use `enum`.
- NAO use `instanceof`.
- NAO use `implements Runnable` (nao visto na disciplina); a aplicacao inicia
  diretamente no ponto de entrada, sem `SwingUtilities.invokeLater`.
- NAO use `java.math.BigDecimal`; use `double` para valores monetarios.
- NAO use `java.util.Optional`; use retorno direto (valor ou ausencia explicita).
- NAO use atributos estaticos de forma alguma, incluindo constantes
  `public static final`. Nao ha estado estatico nem constantes estaticas; use
  metodos de instancia e literais internos. UNICA EXCECAO: o padrao Singleton
  thread-safe, que usa um unico `private static volatile` como ponto de criacao
  unica (ver secao Singleton).
- Existe APENAS UMA classe com `main` no projeto (o ponto de entrada). Nunca
  criar um segundo `main`.
- Listeners DEVEM ser configurados com classes anonimas no proprio ponto de uso,
  ex.: `getBtnSalvar().addActionListener(new ActionListener() { @Override public
  void actionPerformed(ActionEvent e) { ... } });`. NAO criar arquivos/classes de
  listener; NAO usar lambda.
- NAO criar classes internas/anonimas para outros fins alem de listeners.
- Use sempre excecoes unchecked (`IllegalArgumentException`, `RuntimeException`);
  nao declarar nem lancar excecoes checked.
- Use `Objects.requireNonNull` para tratar nulidade de parametros e atributos.
- Todo arquivo `.java` DEVE conter o comentario de autoria no padrao NetBeans,
  antes da declaracao do tipo, com o autor `clayton`:
  `/**` / ` *` / ` * @author clayton` / ` */`.
- NAO use comentarios como substituto de logica; sempre entregue codigo completo
  e compilavel.
- Identificadores em portugues do Brasil sem acentos nem caracteres especiais.
- Sufixos de padrao em ingles quando aplicavel: `Service`, `Repository`,
  `State`, `Decorator`, `Builder`.
- `Model` (dados de dominio) nao recebe sufixo de padrao nem de arquitetura.
- Sem `HashMap`/`HashSet` onde a ordem importa; use estruturas ordenadas.
- Visibilidade minima: prefira package-private a `public`.
- Trate excecoes em fronteiras; nao engula excecoes.

## Build (Maven)

- Basta `maven.compiler.release=21` para compilar em Java 21.
- NAO adicionar `maven-enforcer-plugin` nem fixar a versao do JDK: o Maven pode
  rodar sobre um JDK mais novo (ex.: NetBeans com JDK 25) usando `release 21`.
- NAO adicionar dependencias de teste nem o diretorio `src/test`.

## Seed e persistencia

- O repositorio NAO cria pedidos; ele recebe a lista pronta no construtor e
  apenas persiste.
- O seed fica em uma classe dedicada `PedidoSeeder`, deterministica, alimentada
  por uma quantidade e uma data de referencia injetadas (sem `Random` e sem
  relogio do sistema).
- A quantidade do seed vem do arquivo `.env` na raiz do projeto
  (`SEED_QUANTIDADE`), lido por `configuracao/LeitorEnv` (Adapter que encapsula o
  dotenv); a leitura e fronteira, feita no ponto de entrada.
- O seed conduz as transicoes do dominio; nunca define a situacao por string.
- Persistencia em memoria atras de `IPedidoRepository`. So trocar por banco
  (ex.: H2, sem Spring Boot) se houver exigencia de entrega, e nesse caso troca-se
  apenas a implementacao da interface.

## Fonte da verdade de comportamento

- Todo comportamento descrito em `specs/**/spec.md` deve ter cenario
  correspondente no arquivo `.feature` da mesma pasta.
- O arquivo `.feature` e a fonte da verdade testavel; o `spec.md` e a fonte da
  verdade normativa. Ambos devem permanecer sincronizados.

## Padrao arquitetural obrigatorio: MVP Passive View

- Padrao adotado conforme o material de referencia indexado em
  `references/README.md` e `references/Model-View-Presenter.md` (Potel, 1996;
  Fowler, 2006; Vreca, 2023; Zhang e Luo, 2010; Alles et al., 2006). Em caso de
  duvida sobre responsabilidades, prevalece a referencia.
- **Model**: dados/estado e regras de dominio; sem dependencia de Swing, logging
  ou persistencia.
- **View**: passiva; apenas captura entrada, dispara eventos e projeta saida. Nao
  conhece o Model, nao decide o fluxo e nao configura listeners.
- **Presenter**: coordena o caso de uso; configura listeners, le a View, aciona
  Model/servicos, interpreta resultados e instrui a View sobre como atualizar.
- A View e abstraida por uma interface `I<Nome>View` que o Presenter conhece; a
  implementacao concreta e injetada pelo ponto de entrada (`aplicacao`). O
  Presenter nao instancia a View.
- Classes de servico sao opcionais e isolam regras de aplicacao/dominio.
- `JOptionPane` pode permanecer no Presenter (coerente com a passividade da View);
  para ampliar testabilidade, a saida de mensagens pode ser extraida para uma
  abstracao `ExibidorMensagem` com implementacao concreta via `JOptionPane`.
- Adaptacao local: os listeners sao configurados no Presenter como classes
  anonimas no ponto de uso (sem lambda), sem criar arquivos de listener.

## Padrao Singleton (instancia unica de aplicacao)

- A aplicacao DEVE abrir apenas uma vez: uma execucao por JVM e uma instancia
  por processo, garantido por `InstanciaUnicaAplicacao` (Singleton thread-safe).
- Implementacao: `private static volatile` + `getInstance()` com double-checked
  locking e construtor privado. Este `static` e a UNICA excecao a regra de "sem
  atributos estaticos".
- `iniciar(IExecucaoAplicacao, ITravaInstanciaUnica)` e `synchronized`: impede
  reexecucao na mesma JVM (flag `iniciada`) e segunda instancia entre processos
  (via `FileLock` em `TravaInstanciaUnicaArquivo`).
- DIP/OCP: o Singleton depende de `IExecucaoAplicacao` e `ITravaInstanciaUnica`;
  novas execucoes/travas entram sem alterar o Singleton.
- SRP: o Singleton so garante instancia/execucao unica; a composicao das
  dependencias fica em `AplicacaoPainelPedido`.

## Configuracao (.env) e dependencia dotenv

- O dotenv (`io.github.cdimascio:dotenv-java`, escopo `compile`) e encapsulado
  APENAS por `configuracao/LeitorEnv` (padrao Adapter). Nenhuma outra classe do
  projeto conhece a biblioteca, nem indiretamente (Lei de Demeter): os clientes
  usam somente `LeitorEnv.lerInteiro(chave, padrao)`.
- Justificativa da dependencia: padronizar a leitura do `.env` sem expor a
  biblioteca ao restante do codigo. Alternativa rejeitada: parse manual do
  arquivo (ja existia) - preterida por manter um leitor unico e testavel.
- Ao adicionar dependencia, declarar o motivo, o escopo e a alternativa
  rejeitada neste arquivo.

## Prioridade por SLA e Observer

- SLA por estado: Novo `SLA_NOVO_MINUTOS` (5), Em preparo `SLA_EM_PREPARO_MINUTOS`
  (20), Pronto para entrega e Em rota de entrega pela media de entrega por bairro
  (`SLA_ENTREGA_BAIRRO_MINUTOS`, inicia em 10 e e ajustada a cada entrega);
  Entregue e Cancelado nao tem SLA.
- Mecanismo: listener periodico
  (`AgendadorMonitorSla`) opera o CoR adaptado (`MonitorSlaService` +
  `RegraSla*Handler`), marcando `pedido.marcarPrioridade()`.
- A prioridade some quando o pedido muda de estado (`setEstado` limpa a
  prioridade e atualiza `dataHoraMudancaEstado`).
- Atualizacoes de tela decorrentes de eventos usam Observer (GoF): abstracao e
  concretas com sufixo `Observer`, em subpacote do elemento observado.
- Ordenacao em fila (FIFO): `IOrdenacaoPedidos` /
  `OrdenacaoPedidoPorPrioridadeETempoNoEstado`: prioridade primeiro; em seguida,
  quem esta ha mais tempo no estado (que chegou primeiro) no topo, para ser
  atendido primeiro. A prioridade e por estado: `setEstado` limpa a prioridade e
  atualiza o timestamp.

## Simulacao, notificacao e sons

- Simulacao: cria de 1 a N pedidos por ciclo (`SIMULACAO_PEDIDOS_POR_CICLO` no
  `.env`), em intervalo aleatorio (`SIMULACAO_INTERVALO_MINIMO/MAXIMO_SEGUNDOS`).
- Notificacao usa o **Chain of Responsibility adaptado**: abstracao
  `RegraHandler` (`seAplica`/`executar`), concretas
  `Regra...Handler`, sem referencia entre si; o
  `NotificacaoNovoPedidoService` guarda a colecao (`List`) e `processar(...)`
  aciona as regras aplicaveis na ordem. O Presenter implementa
  `IAcoesNotificacaoNovoPedido` (as acoes de UI) e o contexto
  `ContextoNotificacaoNovoPedido` carrega pedidos + acoes.
- Som usa **Strategy**: `IEstrategiaSom`, implementada por `SomTomStrategy` (tom
  audivel gerado); o Presenter depende da abstracao.
- O label de notificacao e um card pequeno laranja escuro (RGB 194,88,0) com
  texto branco em negrito; os cards novos piscam por `SIMULACAO_MENSAGEM_SEGUNDOS`.

## Filtro de periodo e data do novo pedido

- O filtro de periodo e uma abstracao `IFiltroPeriodo` (Hoje, ultimos 7, ultimos
  30, todos, data especifica), criada por `IFabricaFiltroPeriodo`. Novos filtros
  = novas classes (OCP); o Presenter depende da abstracao (DIP).
- O campo de data e um `JFormattedTextField` (`txtFmtData`, prefixo `txtFmt`); a
  data e lida/validada no ponto de uso (`dd/MM/yyyy`), com excecoes unchecked.
- A data do novo pedido simulado vem de `IFonteDataPedido` (`FonteDataPedido`),
  atualizada pelo Presenter conforme o periodo selecionado: data selecionada
  quando em "Data especifica"; data atual caso contrario. O gerador depende de
  `IFonteDataPedido`, nao de relogio/`Random` diretamente.
- "Criar novo pedido" a partir de um Cancelado usa a MESMA `IFonteDataPedido`,
  sincronizada com o periodo selecionado no momento da acao (nunca
  `LocalDateTime.now()` direto no Presenter).

## Ciclo de vida do pedido (GoF State no estilo Porta)

- O estado do pedido usa GoF State no MESMO estilo do exemplo `Porta`:
  - Classe abstrata `PedidoState` (pacote `br.projeto.painelpedidos.state`) com
    `protected final Pedido pedido` e construtor `PedidoState(Pedido pedido)`.
  - Operacoes default na classe abstrata lancam `RuntimeException`.
  - Cada estado concreto TEM o prefixo `Pedido` e o sufixo `State`:
    `PedidoNovoState`, `PedidoEmPreparoState`, `PedidoProntoParaEntregaState`,
    `PedidoEmRotaDeEntregaState`, `PedidoEntregueState`, `PedidoCanceladoState`.
  - Cada estado concreto sobrescreve apenas as operacoes permitidas e transiciona
    via `pedido.setEstado(new PedidoProximoState(pedido))`.
- LOGICA NO CONSTRUTOR: cada estado concreto trata no construtor as regras do que
  pode existir naquele estado. Se a pre-condicao nao for atendida, o construtor
  lanca excecao e a transicao NAO acontece (ex.: `PedidoEmPreparoState` exige
  pagamento registrado; `PedidoCanceladoState` calcula o credito no construtor).
- O estado concentra as regras de cada situacao (o que pode acontecer com o
  pedido em cada estado), nao sendo um mero marcador de nome.
- `Pedido` e o contexto: inicializa `estado = new PedidoNovoState(this)`, delega
  as operacoes ao estado e expoe `getEstado()`/`setEstado(PedidoState)`.
- SRP: nomes e regras de situacao ficam nas classes de estado; o Repository NAO
  conhece strings de situacao, apenas cria/persiste pedidos exercitando as
  transicoes do dominio.
- OCP/LSP/DIP: nova situacao = nova classe; estados substituiveis; `Pedido`
  depende da abstracao `PedidoState`. Sem `enum`, sem `instanceof` e sem strings
  de situacao duplicadas.
- Ao semear pedidos no Repository, conduza o pedido pelas transicoes
  (`registrarPagamento`, `marcarProntoParaEntrega`, `sairParaEntrega`,
  `marcarEntregue`, `cancelar`) em vez de informar a situacao por string.

## View (padrao NetBeans) e nomenclatura de componentes Swing

- A View contem apenas codigo para criar/configurar seus elementos visuais.
- A View NAO configura listeners e NAO possui `main`.
- A View expoe getters apenas para os elementos que serao manipulados pela
  Presenter respectiva.
- A View NAO possui setters publicos; metodo `set` so existe quando estritamente
  necessario e como algo interno/privado.
- Seguir o padrao NetBeans: blocos `//GEN-BEGIN:initComponents` /
  `//GEN-END:initComponents` e `//GEN-BEGIN:variables` /
  `//GEN-END:variables`, com o `.form` mantido sincronizado com os nomes dos
  campos.
- Nomes de componentes Swing DEVEM seguir os prefixos por tipo abaixo, SEMPRE,
  em campos e variaveis locais, tanto na View quanto no Presenter.

| Componente Swing | Prefixo adotado | Exemplo |
|---|---|---|
| `JButton` | `btn` | `btnSalvar` |
| `JCheckBox` | `chk` | `chkAtivo` |
| `JComboBox` | `cmb` | `cmbCategoria` |
| `JDatePicker` | `dtp` | `dtpDataNascimento` |
| `JDesktopPane` | `dsk` | `dskPrincipal` |
| `JFormattedTextField` | `txtFmt` | `txtFmtCpf` |
| `JLabel` | `lbl` | `lblNome` |
| `JMenu` | `mnu` | `mnuCadastro` |
| `JMenuBar` | `mnb` | `mnbPrincipal` |
| `JMenuItem` | `mit` | `mitSair` |
| `JOptionPane` | `opt` | `optConfirmacao` |
| `JPanel` | `pnl` | `pnlDados` |
| `JPopupMenu` | `pmn` | `pmnOpcoes` |
| `JProgressBar` | `prg` | `prgProgresso` |
| `JRadioButton` | `rad` | `radAdministrador` |
| `JScrollPane` | `scr` | `scrProdutos` |
| `JSpinner` | `spn` | `spnData` |
| `JSplitPane` | `spt` | `sptPrincipal` |
| `JTabbedPane` | `tab` | `tabCadastro` |
| `JTable` | `tbl` | `tblProdutos` |
| `JTextField` | `txt` | `txtNome` |
| `JToggleButton` | `tgl` | `tglAtivo` |
| `JPasswordField` | `txt` | `txtSenha` |


## Regra de sincronizacao com o Windows (obrigatoria)

- SEMPRE que copiar/espelhar qualquer arquivo para o WSL (`/mnt/c/...`), remova
  TODOS os arquivos `desktop.ini` resultantes, recursivamente, antes de concluir.
  Vale para qualquer copia, nao apenas o espelhamento completo.
- Ao terminar qualquer entrega, espelhe o projeto COMPLETO
  (`pom.xml`, `src/`, `openspec/`, `README`, scripts) para:
  `/mnt/c/Users/clayt/Downloads/PainelPedidos_MVP_PassiveView`
  sobrescrevendo o conteudo de destino.
- Remova tambem os `desktop.ini` sob `_PainelPedidos_MVP_PassiveView` e
  `_PainelPedidos_MVP_PassiveView_Revisado`.
- Nunca versione `desktop.ini`, `target/` nem imagens temporarias.
- Depois de cada mudanca/requisito, remova imagens temporarias usadas
  (`/tmp/opencode/`, `/tmp/*.png`) apos confirmar que a informacao foi extraida
  para codigo, specs e commits.

## Convencao de nomenclatura de padroes

- `State`, `Decorator`, `Builder`, `Service`, `Repository` sao sufixos.
- Modelos de dados puros usam apenas o conceito de dominio (ex.: `Pedido`).
- Nomes de classes sempre no SINGULAR (ex.: `PainelPedidoPresenter`, nao
  `PainelPedidosPresenter`; `IPainelPedidoView`; `AplicacaoPainelPedido`).
- Toda interface (abstracao) Java DEVE comecar com `I` (ex.: `IPedidoRepository`,
  `IPainelPedidoView`).
