# Instancia unica do painel dentro do componente

> Plan from this document. Each slice below carries its own shape - copy it, do not re-derive it.
> Status: confirmed by clayton, 2026-10-11

## Situation

- Project: in active construction - componente biblioteca (MVP Passive View), nao
  publicado; o unico consumidor e o modulo `exemplo`.
- Decision: committed by clayton, 2026-10-11, nesta sessao - o componente possui a
  instancia unica e a aplica sozinho em `iniciar()`. A decisao anterior ("o
  consumidor decide usar") fica revogada.
- In flight: a capacidade ja existe em `aplicacao/`, mas a ligacao esta no
  consumidor e a feature `us25` foi apagada enquanto a requirement segue em
  `spec.md` sem teste - a fatia Garantia a restaura. Fica fora o app hospedeiro
  que queira varias janelas do painel (Boundary).
- At stake: muda a API publica (o retorno de `iniciar()`) e o comportamento
  padrao de toda abertura. Reversivel numa tarde enquanto nao ha consumidor
  externo - e nao ha nenhum hoje.

## Problem

Construcao.

- **O que esta peca foi prometida a tornar possivel:** reusar o painel com
  integracao minima, sem copiar o repositorio. Hoje todo consumidor que queira
  instancia unica reescreve o caminho da trava, escolhe a trava e trata a recusa:
  o `exemplo` gasta uma string magica, um `Paths.get`, um `getInstance()` e um
  `if` - decisao que nao e dele.
- **O que trava sem isso:** a requirement `Painel aberto uma unica vez (opcional)`
  (`spec.md:129`, `@US25-001`) nao tem feature nem teste, e o codigo que a
  implementa tem defeito de ciclo de vida: `PainelPedidoFacade.encerrar` nao
  libera a trava e `InstanciaUnicaAplicacao` nunca volta atras. Fechar e reabrir
  no mesmo processo sempre responde "ja esta aberto" - o primeiro consumidor que
  fizer isso bate nisso.
- **Por que agora e nao depois:** e a ultima correcao antes de a API ficar
  estavel; depois disso o retorno de `iniciar()` vira contrato.
- **O numero:** consumidores externos hoje = 0, e nenhum comprometido. Nao e
  achado sobre o problema, e achado sobre a instrumentacao. O custo medido e o
  boilerplate do `exemplo` e o defeito de reabertura acima.

## Success

- Worked if: o `exemplo` (e qualquer consumidor) abre o painel sem tocar em
  trava, caminho ou singleton; a segunda abertura nao abre janela; fechar e
  reabrir no mesmo processo abre de novo.
- Going wrong: a segunda abertura abre janela, ou o fechar-e-reabrir passa a ser
  recusado - o sinal e a trava sobrevivendo ao encerramento.
- Review: apos o build completo - clayton.

## Boundary

In: a instancia unica dentro do componente; `iniciar()` adquire a trava e
sinaliza a recusa; `encerrar()` libera; trava padrao de arquivo fornecida pelo
componente; remocao do involucro de execucao.

Out: a mensagem ao usuario - decidida pelo consumidor (o `exemplo` imprime),
porque uma biblioteca nao abre dialogo no startup do hospedeiro. Varias janelas
do painel no mesmo processo - a decisao tomada e uma instancia por maquina.

Unchanged: BR12/BR13, filtros, ordenacao, SLA, notificacao, empurrao
`receberNovosPedidos`, maximizacao, os tres listeners obrigatorios,
`IPedidoRepository` e a configuracao por `painel_pedidos.env`.

## Prior art

- Trava por arquivo (`FileLock`) e a forma que se repete para instancia unica de
  aplicacao de desktop - adotada, e o que a classe ja faz.
- A falha que todos relatam: trava obsoleta deixada no disco apos encerramento
  sujo. O SO libera o `FileLock` quando o processo morre (verificado nesta
  sessao), entao a fatia Trava decide **nao** apagar o arquivo - apagar e o que
  quebra a exclusao.
- O que fazem e nao fazemos: registro global por usuario/sessao do sistema -
  nao compartilhamos a condicao (um processo, uma maquina, um usuario).

## Shape

O componente possui a trava de instancia unica: `PainelPedidoFacade.iniciar()`
adquire a trava antes de abrir qualquer janela e, nao conseguindo, retorna
`false` sem abrir nada; `encerrar()` libera. A porta de mao unica e o retorno de
`iniciar()` (deixa de ser `void`): depois de publicado, todo chamador depende
dele, e troca-lo custa corrigir cada consumidor. A alternativa mais pesada -
manter o involucro `IExecucaoAplicacao`/`InstanciaUnicaAplicacao` e apenas mover
a ligacao - so vence se o componente voltar a ser dono do processo inteiro, o
que nao e o caso.

## Key decisions

1. **O componente possui a instancia unica; o consumidor nao configura nada.**
   A trava, o caminho e a guarda vivem no componente; o consumidor nao instancia
   trava nem `getInstance()`.
2. **A recusa acontece antes de criar janela, e `iniciar()` a sinaliza
   retornando `boolean`.** `false` = nao abriu; nao ha efeito colateral parcial
   (nada de janela, SLA ou observer iniciados).
3. **A trava e a fonte unica da verdade: a fachada nao guarda "ja aberto" em
   memoria.** `encerrar()` libera a trava, e reabrir no mesmo processo funciona
   porque nao ha flag obsoleto - era exatamente o defeito do antigo singleton, e
   e o que permite a suite rodar sem vazar a trava entre testes.
4. **A trava padrao e um arquivo em `java.io.tmpdir` e o arquivo nunca e
   apagado.** Verificado: fechar o canal libera a trava com o arquivo ainda no
   disco, e o SO libera quando o processo morre. Apagar o arquivo e a falha que
   o Prior art manda evitar.
5. **"Ja travado" nao e erro: e recusa.** `tryLock()` que devolve `null` e
   `OverlappingFileLockException` (segunda trava na mesma JVM) mapeiam para
   `false`, nunca para excecao.
6. **`IExecucaoAplicacao` e `InstanciaUnicaAplicacao` deixam de existir.** O
   involucro de execucao nao tem mais quem o invoque, e o singleton de JVM era o
   defeito (nunca resetava); a fachada passa a ser o ponto de garantia e some o
   unico campo estatico do projeto.
7. **A mensagem ao usuario e do consumidor.** O componente sinaliza a recusa; o
   `exemplo` decide imprimir. Uma biblioteca nao fala com o usuario final nem
   abre dialogo no startup do hospedeiro.

## Work

| Slice | Delivers | Status |
|---|---|---|
| [Trava](#trava) | `ITravaInstanciaUnica` ganha `liberar()`; a trava de arquivo trata "ja travado" como recusa | clear |
| [Estado unico](#estado-unico) | `PainelPedidoFacade.iniciar()` adquire e sinaliza; `encerrar()` libera | clear |
| [Exemplo](#exemplo) | `main` do exemplo sem trava, caminho nem singleton | clear |
| [Garantia](#garantia) | requirement, feature e testes da recusa e da reabertura | clear |

Ordem: Trava -> Estado unico -> Exemplo -> Garantia.

Ja tratado pelo codigo existente: os tres listeners obrigatorios e a ordem
`validar -> abrir` seguem como estao; o botao "Fechar" continua chamando
`encerrar()`.

Derivavel do repositorio, deixado ao plano: nomes de pacotes/classes/metodos, o
literal do nome do arquivo de trava, e as regras de excecao unchecked com
`Objects.requireNonNull` - como os registros existentes fazem.

### Trava

**Delivers** a porta e a trava de arquivo com liberacao e recusa. **Status:
clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Arquivo livre | trava adquirida | `true` |
| Ja travado no mesmo processo | recusado, sem excecao | `false` |
| Ja travado em outro processo | recusado | `false` |
| Liberada | arquivo permanece no disco, trava livre | nova aquisicao `true` |

### Estado unico

**Delivers** a aplicacao automatica da instancia unica pela fachada. **Status:
clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Primeira abertura | trava adquirida; janela criada; SLA e observer iniciados | painel aberto (`true`) |
| Segunda abertura | trava nao adquirida; nada aberto | `false` |
| Fechar | janela fechada; SLA parado; trava liberada | painel fechado |
| Reabrir apos fechar, mesmo processo | trava readquirida; janela criada | painel aberto (`true`) |
| Iniciar sem os tres listeners | excecao antes de qualquer trava ou janela | `IllegalStateException` |

Alternatives considered: o componente exibir a mensagem de recusa - vence se o
painel for sempre a aplicacao inteira, nunca embutido (Key decision 7).

### Exemplo

**Delivers** o `main` do consumidor sem a mecanica de instancia unica. **Status:
clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Primeira execucao | painel abre | demonstracao |
| Segunda execucao | nada abre; o consumidor informa | "ja esta aberto" (do consumidor) |

### Garantia

**Delivers** requirement, feature e testes. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Segunda abertura | recusada; garantida por teste com a trava real retida | build verde |
| `encerrar()` libera | reaquisicao apos liberar; garantida no nivel da trava | build verde |
| Involucro removido | `IExecucaoAplicacao`/`InstanciaUnicaAplicacao` ausentes | build verde |

O build roda headless (sem X): a garantia da recusa para antes de criar a
janela, e a reaquisicao e garantida no nivel da trava, que e a fonte unica da
verdade (Key decision 3) - nao ha caminho de teste que abra janela.

## Sources

- `openspec/specs/painel-acompanhamento-pedidos/spec.md` - requirement "Painel
  aberto uma unica vez (opcional)" e `@US25-001`.
- `openspec/changes/archive/2026-10-11-componente-reutilizavel/design.md` - o
  pacote `aplicacao` e os limites de entrada/saida do componente.
- Comportamento de `FileLock` verificado nesta sessao (mesma JVM, liberacao por
  fechamento de canal e por morte do processo).
