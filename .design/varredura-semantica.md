# Varredura do codigo (mecanica e semantica)

> Status: record - 2026-10-11. Resultado da varredura pedida. Mecanica corrigida.
> Semantica: **S1, S2 e S5 corrigidos**; **S3 aberto** (exige tipo de situacao).

## Corrigido depois da varredura

- **S1 - resolvido no estilo do State (skill `state-pattern-java`).** As operacoes
  de transicao passaram a receber o `quando` e `setEstado(estado, quando)` grava
  em `dataHoraMudancaEstado`; o Model **nao** le mais o relogio do sistema.
  Quem dispara informa a hora (o consumidor no listener com o proprio relogio; o
  presenter com o `IRelogioService` injetado). O `PedidoSeeder` passa a data
  derivada da referencia, entao os dados da demonstracao ficam coerentes com o
  SLA. `RegraSlaEstadoHandlerTest` foi reescrito com relogio **fixo** e provou
  discriminar: revertendo `setEstado` para `LocalDateTime.now()`, ele falha.
- **S2 - resolvido.** `IRelogioService` passou a ser injetado no presenter (8o
  argumento) e responde pelo "hoje" do filtro, pelos rotulos do combo e pelo
  hoje/ontem da formatacao (`FormatacaoDePedido.horario(dataHora, hoje)`).
- **S5 - resolvido.** `volatile` no canal da trava.
- **S4 - registrado** no `AGENTS.md`: o componente nao deduplica; o consumidor
  empurra so o que e novo.

### S3 aberto - e agora com o achado da skill

A skill de State e explicita: "**status strings duplicated across
services/repositories**" e um anti-pattern. As situacoes hoje aparecem como
strings em tres lugares: `getNome()` nos estados, o mapa de SLA e as chaves do
`painel_pedidos.env`. O conserto alinhado e um **tipo `Situacao`** (sem `enum`)
com o conjunto fechado, usado pelos estados e pelo mapa - o que tambem faz a
configuracao errada falhar ao construir, em vez de silenciar. E uma mudanca de
contrato (15o tipo) e por isso ficou como decisao.

## Mecanica

| Verificacao | Resultado |
|---|---|
| Imports ociosos | **8**, todos em testes (sobra dos refactors): `Duration`, `LinkedHashMap`, `Map` em `PainelPedidoFacadeTest` e `PainelPedidoIntegracaoIT`; `DirectoryStream`, `Paths` em `ContratoPublicoTest` - **corrigidos** |
| Campos privados nao usados | nenhum |
| Metodos privados nao usados | nenhum |
| Referencias orfas (simbolo movido/removido) | nenhuma |
| `System.out`/`err` em producao | nenhum |
| `catch` que engole, `printStackTrace`, TODO/FIXME | nenhum |
| `==` comparando String | nenhum |
| `new Thread` / `synchronized` | nenhum (o unico agendamento e o Timer na EDT) |
| Visibilidade | os 5 `Filtro*` eram publicos sem uso externo - **package-private** |
| Metodos longos | `criarCartaoPedido` **117**, `atualizarPainel` 58, `executarAcaoPrincipal` 40, `configurarListeners` 39; `initComponents` 159 (gerado pelo NetBeans) |

## Semantica - achados

### S1 (critico) Duas fontes de tempo na mesma regra de SLA

`model.PedidoAbstract:75` e `:120` carimbam `dataHoraPagamento` e
`dataHoraMudancaEstado` com `LocalDateTime.now()`; o SLA mede
`Duration.between(pedido.getDataHoraMudancaEstado(), contexto.getAgora())` e o
`agora` vem do relogio **injetado** (`AgendadorMonitorSlaService` ->
`relogioService.agora()`). Sob relogio injetado (teste, fuso diferente, hora
forjada) o `decorrido` fica errado: negativo (nunca prioriza) ou enorme (prioriza
sempre). Viola a regra do projeto de nao depender do relogio do sistema em regra
de negocio.

**O teste nao pega:** `RegraSlaEstadoHandlerTest.criarContexto` deriva
`agora = pedido.getDataHoraMudancaEstado().plusMinutes(10)` - os dois relogios
coincidem por construcao.

| Opcao | Custo |
|---|---|
| (a) quem dispara a transicao informa a data (`registrarPagamento(forma, quando)`, estado recebendo `quando`) | muda a assinatura do contrato; o Model para de inventar tempo |
| (b) `PedidoAbstract` recebe um relogio (porta) | muda o construtor que o consumidor estende |
| (c) manter e documentar que o componente carimba transicoes com o relogio do sistema | nenhum |

Recomendado: **(a)** - e o unico que mantem o Model sem dependencia e sem inventar
tempo, ao custo de uma assinatura.

### S2 (alto) O presenter e a formatacao ignoram o relogio injetado

`PainelPedidoPresenter:362` (`LocalDate.now()` como "hoje" no filtro), `:241`
(rotulos do combo) e `FormatacaoDePedido:38` (hoje/ontem). O periodo selecionado
passa por `IFonteDataPedido`/`IRelogioService`, mas o "hoje" do filtro nao - sob
relogio injetado, "Hoje" filtra pelo dia do **sistema**.

| Opcao | Custo |
|---|---|
| (a) injetar `IRelogioService` no presenter | construtor de 7 para 8 argumentos (construtor do componente, nao do consumidor) |
| (b) `IFonteDataPedido` responder tambem "hoje" | sobrecarrega a porta de data do pedido |

Recomendado: **(a)**.

### S3 (medio) Configuracao de SLA errada falha em silencio

`RegraSlaEstadoHandler.seAplica` verifica se **algum** pedido tem situacao
presente no mapa. Se o consumidor construir `ConfiguracaoPainelPedido` a mao com
uma chave que nao casa com as situacoes (`Novo`, `Em preparo`,
`Pronto para entrega`, `Em rota de entrega`), o SLA **nunca se aplica** e nada
avisa. O caminho suportado (`LeitorConfiguracaoPainelPedido`) usa as chaves
certas; o construtor publico aceita qualquer coisa.

| Opcao | Custo |
|---|---|
| (a) validar as situacoes conhecidas ao construir a configuracao | exige expor as situacoes conhecidas no contrato (hoje o State e package-private) |
| (b) validar em `RegraSlaEstadoHandler` e avisar no primeiro monitoramento | o aviso chega tarde e por log/tela |
| (c) documentar | nenhum |

Recomendado: **(a)**, ou (b) se expor situacoes no contrato nao valer.

### S4 (baixo, documental) "Dedup por identificador" e historia, nao comportamento

O claim vive no design **arquivado** (`2026-10-11-componente-reutilizavel`),
escrito quando a ingestao era por leitura periodica. Com empurrao, quem decide o
que e novo e o consumidor, e o componente **nao** deduplica
(`PainelPedidoNovoPedidoObserver` notifica sempre). O spec vivo **nao** promete
dedup - falta dizer de quem e a responsabilidade.
Acao: linha no `AGENTS.md`.

### S5 (baixo) Visibilidade do canal da trava entre threads - corrigido

`TravaInstanciaUnicaArquivo.canal` era escrito por `iniciar()` (thread principal)
e lido por `encerrar()` (EDT, pelo botao Fechar), sem sincronizacao; sem
`volatile` a liberacao podia nao ser vista e a trava ficaria presa ate o processo
morrer. **Corrigido** com `volatile`.

## Verificado e correto (nao e achado)

- **Ordenacao:** prioridade primeiro e, no empate, `dataHoraMudancaEstado`
  ascendente - e a fila FIFO por tempo no estado, e `List.sort` e estavel.
- **Guardas do CoR adaptado:** `seAplica` protege o `get(0)` (`size == 1`,
  `size > 1`, `!isEmpty`) - lista vazia nao causa `IndexOutOfBounds`.
- **SLA no limite:** `decorrido.compareTo(limite) > 0` prioriza o que
  **ultrapassa**, coerente com o spec (no limite exato, nao prioriza).
- **`PedidoSeeder`:** deterministico - deriva tudo de `dataReferencia`, sem
  `Random` e sem relogio do sistema.
- **`ConfiguracaoPainelPedido`:** imutavel, com mapa nao-modificavel.
- **Operacao invalida no estado:** as default do State lancam com mensagem
  orientadora; nenhum no-op silencioso.
