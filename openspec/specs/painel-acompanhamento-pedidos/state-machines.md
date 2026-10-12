# Maquinas de estado (UML) por elemento

Diagramas de maquina de estados finita (UML) para cada elemento do sistema que
usa o padrao **State** ou que possui estados (mesmo sem usar o padrao hoje).
Fonte: codigo implementado (72 classes). Diagramas em Mermaid `stateDiagram-v2`.

Documentos relacionados: requisitos em [`spec.md`](./spec.md); padroes de projeto
e diagramas de classe UML, com as referencias ABNT, em [`design.md`](./design.md);
cenarios em `us21..us25*.feature` nesta pasta.

Indice:
1. `Pedido` (usa GoF State)
2. Status derivado do card (`Pedido` + pagamento)
3. `TravaInstanciaUnicaArquivo` (estados implicitos; nao usa State)
4. `PainelPedidoPresenter` - estados de interface (selecao, destaque, botao, label)

---

## 1. `Pedido` (GoF State)

Elemento: `model.PedidoAbstract` (contexto) + `model.PedidoState` e concretos
`PedidoNovoState`, `PedidoEmPreparoState`, `PedidoProntoParaEntregaState`,
`PedidoEmRotaDeEntregaState`, `PedidoEntregueState`, `PedidoCanceladoState`.

```mermaid
stateDiagram-v2
    [*] --> Novo
    Novo --> EmPreparo : registrarPagamento(forma)
    Novo --> Cancelado : cancelar
    EmPreparo --> ProntoParaEntrega : marcarProntoParaEntrega
    EmPreparo --> Cancelado : cancelar
    ProntoParaEntrega --> EmRotaDeEntrega : sairParaEntrega
    ProntoParaEntrega --> Cancelado : cancelar
    EmRotaDeEntrega --> Entregue : marcarEntregue
    EmRotaDeEntrega --> Cancelado : cancelar
    Entregue --> Cancelado : cancelar
    Cancelado --> [*]
    note right of Cancelado
      Terminal: nao retorna a
      nenhuma situacao anterior
    end note
```

**Descricao dos estados**

| Estado | Classe | Operacoes permitidas | Observacao |
|---|---|---|---|
| Novo | `PedidoNovoState` | `registrarPagamento`, `cancelar` | Sem pagamento ainda. |
| Em preparo | `PedidoEmPreparoState` | `marcarProntoParaEntrega`, `cancelar` | Exige pagamento (construtor). |
| Pronto para entrega | `PedidoProntoParaEntregaState` | `sairParaEntrega`, `cancelar` | Exige pagamento. |
| Em rota de entrega | `PedidoEmRotaDeEntregaState` | `marcarEntregue`, `cancelar` | Exige pagamento. |
| Entregue | `PedidoEntregueState` | `cancelar` | Exige pagamento. |
| Cancelado | `PedidoCanceladoState` | (nenhuma) | Calcula o credito no construtor. |

**Transicoes (guardas e efeitos)**
- `Novo --registrarPagamento(forma)--> Em preparo`: aplica a forma e a data/hora
  do pagamento.
- `Em preparo --> Pronto para entrega --> Em rota de entrega --> Entregue`:
  avancos operacionais.
- Qualquer estado nao Cancelado `--cancelar--> Cancelado` (BR13); se houver
  pagamento, gera credito igual ao valor do pedido.
- Pre-condicao de entrada (no construtor do estado alvo): `Em preparo`,
  `Pronto para entrega`, `Em rota de entrega` e `Entregue` exigem
  `isPagamentoRegistrado()`; se falhar, lanca excecao e **a transicao nao
  ocorre**.
- Operacoes nao previstas para o estado lancam `RuntimeException` (default em
  `PedidoState`) e nao transitam.
- `setEstado(...)` (efeito de toda transicao): limpa `prioridade` e atualiza
  `dataHoraMudancaEstado`.

**Fora da maquina de estados**
- `copiarComoNovo()` so e permitido em `Cancelado` e cria um **novo** `Pedido`
  na situacao `Novo` (novo objeto, nao uma transicao do mesmo pedido).
- `prioridade` e uma **flag ortogonal** (nao e estado): marcada pelo SLA e
  limpa em `setEstado`.

---

## 2. Status derivado do card (`Pedido` + pagamento)

Nao e um estado proprio armazenado; e derivado de `Pedido` para o rotulo do card
(`PainelPedidoPresenter.criarRotuloStatus`). Mapeamento:

| Condicao | Rotulo |
|---|---|
| `pedido.estaCancelado()` | "Pedido cancelado" |
| `pedido.isPagamentoRegistrado()` (e nao cancelado) | "Pagamento confirmado" |
| caso contrario | "Aguardando pagamento" |

```mermaid
stateDiagram-v2
    [*] --> AguardandoPagamento
    AguardandoPagamento --> PagamentoConfirmado : registrarPagamento
    AguardandoPagamento --> PedidoCancelado : cancelar
    PagamentoConfirmado --> PedidoCancelado : cancelar
    PedidoCancelado --> [*]
```

Rotulo adicional ortogonal: "Prioridade" (vermelho) quando `pedido.isPrioridade()`
(SLA); removido ao mudar de estado.

---

## 3. `TravaInstanciaUnicaArquivo` (nao usa State)

Elemento: `aplicacao.TravaInstanciaUnicaArquivo` (detalhe interno do componente,
via `FileLock`), possuida por `aplicacao.PainelPedidoFacade` (adquire ao iniciar,
libera ao encerrar).

```mermaid
stateDiagram-v2
    [*] --> Livre
    Livre --> Travada : adquirir() [tryLock != null]
    Livre --> Livre : adquirir() [tryLock == null ou OverlappingFileLockException]
    Travada --> Travada : adquirir() [ja travada nesta instancia -> false]
    Travada --> Livre : liberar() [fecha o canal; o arquivo permanece]
    Livre --> [*]
```

- **Livre**: nenhum processo detem a trava; a abertura do painel e concedida.
- **Travada**: esta instancia detem a trava de arquivo; a abertura e recusada
  (`iniciar()` devolve `false`) e **nada** e aberto (nem janela, nem SLA, nem
  observer).
- `liberar()` fecha o canal - a trava cede - e **nao apaga o arquivo**: apaga-lo e
  o que quebraria a exclusao. Fechar e reabrir no mesmo processo volta a abrir
  porque a fachada nao guarda flag "ja aberto" em memoria: a trava e a fonte
  unica da verdade.
- Nao usa o padrao State (resultado de `tryLock`/`close`).

---

## 4. `PainelPedidoPresenter` - estados de interface (nao usa State)

Elemento: `presenter.PainelPedidoPresenter`. Varios estados de interface
controlados por campos/Timers (nao pelo padrao State).

### 4.1 Selecao de pedido

```mermaid
stateDiagram-v2
    [*] --> NenhumSelecionado
    NenhumSelecionado --> PedidoSelecionado : clique em card / foco (Tab) / acao
    PedidoSelecionado --> PedidoSelecionado : selecionar outro pedido
    PedidoSelecionado --> NenhumSelecionado : limparSelecao (apos atualizar sem o pedido)
```

### 4.2 Destaque piscante dos novos pedidos (`destaqueAceso`)

```mermaid
stateDiagram-v2
    [*] --> Apagado
    Apagado --> Aceso : destacarPedidos(pedidos)
    Aceso --> Apagado : tick (500ms)
    Apagado --> Aceso : tick (500ms)
    Aceso --> Apagado : fim (NOTIFICACAO_DURACAO_SEGUNDOS)
    Apagado --> [*] : fim
```

### 4.3 Botao "Executar acao" (visivel/habilitado)

```mermaid
stateDiagram-v2
    [*] --> Invisivel
    Invisivel --> VisivelHabilitado : selecionar pedido com acao (Novo/Em preparo/Pronto/Em rota/Cancelado)
    VisivelHabilitado --> Invisivel : selecionar Entregue ou limpar selecao
```

### 4.4 Label de notificacao ("Novo Pedido #id" / "N novos pedidos")

```mermaid
stateDiagram-v2
    [*] --> Oculto
    Oculto --> Visivel : exibirMensagem(texto)
    Visivel --> Visivel : exibirMensagem(texto) (reinicia o tempo)
    Visivel --> Oculto : fim (NOTIFICACAO_DURACAO_SEGUNDOS)
```

- Nenhum desses usa o padrao State (campos booleanos/Timers). Sao estados de
  apresentacao coordenados pela Presenter.
