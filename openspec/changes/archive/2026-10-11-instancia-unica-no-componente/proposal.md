# Proposta - Instancia unica dentro do componente

## Por que

A instancia unica ja existe no componente, mas a **ligacao** esta no consumidor:
o sistema integrado precisa escolher o caminho da trava, construir a trava e
chamar `InstanciaUnicaAplicacao.getInstance().iniciar(...)`. Sao decisoes que nao
pertencem a ele - o `exemplo` gasta uma string magica, um `Paths.get`, um
`getInstance()` e um `if` para uma responsabilidade do componente.

Alem disso, o codigo tem defeito de ciclo de vida: `PainelPedidoFacade.encerrar`
nao libera a trava e `InstanciaUnicaAplicacao` nunca volta atras. Fechar e
reabrir o painel no mesmo processo sempre responde "ja esta aberto".

## O que muda

- O componente **possui** a instancia unica e a aplica **sozinho** em
  `PainelPedidoFacade.iniciar()`.
- `iniciar()` passa a devolver `boolean`: `false` = a trava nao foi adquirida e
  **nada** foi aberto (nem janela, nem SLA, nem observer).
- A trava padrao e fornecida pelo componente (arquivo em `java.io.tmpdir`); o
  consumidor nao instancia trava, caminho nem singleton.
- `encerrar()` **libera** a trava, permitindo reabrir no mesmo processo.
- "Ja travado" vira recusa (`false`), nunca excecao.
- Removidos `IExecucaoAplicacao` e `InstanciaUnicaAplicacao`: o involucro de
  execucao fica sem quem o invoque e o singleton de JVM era o defeito. Some o
  unico campo estatico do projeto.
- O `main` do exemplo deixa de montar trava, caminho e singleton.

## Impacto

- Todo consumidor que chame `iniciar()` pode ignorar o retorno (compativel) ou
  usar o `boolean` para informar o usuario; a **mensagem** e do consumidor.
- Uma instancia por maquina: duas janelas do painel deixam de ser possiveis. O
  arquivo de trava nunca e apagado - apaga-lo e o que quebraria a exclusao.

## Alternativas consideradas

- **O componente exibir a propria mensagem de recusa**: vence se o painel for
  sempre a aplicacao inteira, nunca embutido; uma biblioteca nao deve abrir
  dialogo no startup do hospedeiro.
- **Manter o involucro e apenas mover a ligacao**: preserva o unico `static` do
  projeto e um nivel de indirecao sem uso; nao corrige o defeito de reabertura.
