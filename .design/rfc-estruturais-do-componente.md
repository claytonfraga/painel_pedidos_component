# RFC - Estruturais do componente

> Status: open - 2026-10-11. Nada aqui e decisao: sao quatro questoes que a
> varredura de SOLID/DRY/Demeter levantou e que NAO se resolvem por leitura do
> codigo. Ninguem implementa antes de responder.
> Origem: varredura registrada em `openspec/AGENTS.md` (secao "Excecoes aceitas").

## Por que este documento existe

A varredura achou quatro pontos em que o codigo viola um principio sem que a
correcao seja obvia: cada um tem **mais de uma resposta defensavel** e nenhuma e
decidida pelo repositorio. Decidir qualquer um deles em conversa produziria um
artefato que parece decidido - o RFC existe para que isso nao aconteca.

## Q1 - Como dividir `PainelPedidoPresenter` (943 linhas, 55 metodos)?

O proximo maior e `PainelPedidoPanel` com 380/29, e e construcao de UI. O
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
