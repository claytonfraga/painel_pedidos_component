# RFC - Estruturais do componente

> Status: resolved - 2026-10-11. As quatro questoes foram decididas pelo autor e
> implementadas (ver "Decisoes"). O texto original de cada questao fica abaixo
> como registro do que foi pesado.
> Origem: varredura registrada em `openspec/AGENTS.md` (secao "Excecoes aceitas").

## Decisoes

- **Q1 - SRP do presenter: resolvido por extracao.** Saíram do presenter a
  formatacao (`FormatacaoDePedido`: identificador, valor, horario, forma de
  pagamento) e a aparencia por estado (`ApresentacaoDoPedido`: texto da acao, cor
  da raia, rotulo e cor do status, credito). Ficaram a montagem do cartao e a
  selecao/destaque: sao layout e interacao, coesos com o papel do presenter, e
  verificaveis so pelo caminho de pintura. Presenter: 943 -> 862 linhas, com os
  dois tipos novos cobertos por teste headless.
- **Q2 - contrato expondo `PedidoState`: resolvido por placement.** O State foi
  para o pacote `model`, junto do contexto, e virou package-private;
  `setEstado` virou package-private e o contrato para de expor tipo interno. O
  contrato segue com 14 tipos e a dependencia passou a apontar para o estavel.
- **Q3 - branching por estado: resolvido no escopo da aparencia.** As quatro
  cadeias de aparencia saíram para `ApresentacaoDoPedido`. Ficaram a particao por
  raia (sao as 6 colunas fixas da View) e o roteamento da acao (e transicao de
  estado e depende dos colaboradores do presenter) - ambos registrados com a
  razao, nao escondidos.
- **Q4 - fabrica acoplada ao indice do combo: resolvido por fonte unica.** A
  fabrica passou a expor `rotulos(hoje)`, e a tela monta o combo a partir dela:
  a lista e o mapeamento indice -> periodo vivem na mesma classe e nao podem
  divergir. Coberto por `FabricaFiltroPeriodoTest`.

## Por que este documento existe

A varredura achou quatro pontos em que o codigo viola um principio sem que a
correcao seja obvia: cada um tem **mais de uma resposta defensavel** e nenhuma e
decidida pelo repositorio. Decidir qualquer um deles em conversa produziria um
artefato que parece decidido - o RFC existe para que isso nao aconteca.

## Q1 - Como dividir `PainelPedidoPresenter` (943 linhas, 55 metodos)?

O proximo maior e `PainelPedidoView` com 380/29, e e construcao de UI. O
presenter acumula montagem de card, formatacao (moeda/hora/data), selecao e
destaque com 2 timers, notificacao, acoes de estado, cancelamento, criacao de
pedido, filtros e busca.

| Opcao | O que muda | Condicao para vencer |
|---|---|---|
| Extrair a formatacao (moeda, hora, data, forma de pagamento) para um tipo coeso | tira ~10 metodos e e testavel headless hoje, sem tocar na View | sempre - e o pedaco de menor risco |
| Extrair a montagem do card para um tipo proprio | concentra a construcao de componente num lugar | se mudancas de card forem frequentes |
| Extrair as acoes de estado (texto, cor, proxima acao) para polimorfismo | resolve tambem a Q3 | se estados novos entrarem |
| Nao dividir | mantem 943 linhas | se o presenter parar de crescer |

Trava: nada hoje. O custo de esperar e que toda mudanca de card, de acao ou de
formatacao cai no mesmo arquivo.

## Q2 - O contrato expondo `PedidoState`

`model.PedidoAbstract` e tipo de contrato, mas **importa e expoe**
`internal.state.PedidoState` em `public void setEstado(PedidoState)`: o estavel
depende do volatil, e o consumidor nao usa esse metodo publico sem tocar em
`internal`.

| Opcao | O que muda | Condicao para vencer |
|---|---|---|
| Mover o State para junto do contexto (`model`) | `setEstado` vira package-private e o contrato para de expor o interno | se a regra de mapeamento de pacotes (`state` separado) ceder |
| Promover `PedidoState` a contrato (15o tipo) | a maquina de estados vira API publica | se o consumidor precisar compor estados proprios |
| Manter e documentar | nada muda no codigo | se nenhum consumidor precisar do metodo |

Trava: o contrato publico enumerado (14 tipos) fica incompleto enquanto isto nao
for respondido - a lista documenta uma API que ainda vaza um tipo interno.

## Q3 - Branching por estado no presenter

Quatro cadeias `estaX()` decidem raia, texto de acao, cor e rotulo
(`atualizarPainel`, `obterTextoAcaoPrincipal`, `corFundoTituloRaia`,
`criarRotuloStatus`). E branching de tipo em runtime - o equivalente ao
`instanceof` que o projeto proibe.

| Opcao | O que muda | Condicao para vencer |
|---|---|---|
| Apresentacao por estado polimorfica (um tipo por estado com rotulo, cor e texto de acao) | remove as quatro cadeias; a View continua com 6 raias fixas | se estados novos entrarem |
| Manter | as raias sao 6 colunas fixas na View; alguem precisa mapear estado para raia | se a raia continuar sendo o unico criterio |

Trava: nada hoje. O custo de esperar e que cada estado novo exige editar quatro
metodos no mesmo arquivo.

## Q4 - `FabricaFiltroPeriodo` indexada pela ordem do combo

Seis ramos `indice == 0..4`, e o indice vem de
`painelPedidoView.getCmbPeriodo().getSelectedIndex()`: a fabrica depende da ordem
em que a View adicionou os itens.

| Opcao | O que muda | Condicao para vencer |
|---|---|---|
| A View fornece um valor de tipo de periodo em vez do indice | desacopla a fabrica da ordem do combo | se periodos novos entrarem (a regra proibe `enum`, entao e um tipo) |
| Manter | funciona para os cinco periodos atuais | se a lista de periodos estabilizar |

Trava: nada hoje.

## Quando isto para

Nao e um spike: cada questao tem resposta por decisao, nao por experimento. O RFC
esta respondido quando cada Q tiver uma opcao escolhida (ou "manter") registrada
aqui e, se houver codigo, virado change propria.
