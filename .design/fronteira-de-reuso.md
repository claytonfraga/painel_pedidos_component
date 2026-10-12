# Fronteira de reuso do componente

> Plan from this document. Each slice below carries its own shape - copy it, do not re-derive it.
> Status: confirmed by clayton, 2026-10-11

## Situation

- Project: in active construction - componente biblioteca, **nao publicado**;
  consumidores externos = 0 (o unico consumidor e o modulo `exemplo`).
- Decision: committed by clayton, 2026-10-11, nesta sessao - (1) fazer a passada de
  fronteira **antes de publicar**; (2) **dois sistemas consumidores convivem na
  mesma maquina** e isso e cenario real.
- In flight: a fachada acabou de ganhar a instancia unica e as mensagens
  orientadoras, e a API **ainda nao e contrato**. Fica fora redesenho de telas,
  persistencia e publicacao no JitPack.
- At stake: **porta de mao unica**. Hoje encolher a API custa apagar `public`;
  depois do primeiro consumidor, cada tipo publico e contrato e retira-lo quebra
  quem depende.

## Problem

Construcao.

- **O que esta peca foi prometida a tornar possivel:** reusar o painel sem forkar
  (integracao minima).
- **O que trava sem ela:** a evidencia e o proprio repositorio, nao um numero de
  uso - nao ha instrumentacao e nao ha consumidor. Quem respondeu confirmou que
  dois sistemas na mesma maquina e cenario real; e o que torna o achado 3 defeito
  e nao hipotese.
- **Achados (com a origem):**
  1. **57 tipos publicos**, com a composicao inteira exposta: os 6 estados
     concretos, os 8 handlers de notificacao, `ContextoSla`, `RegraHandler`, o
     `PainelPedidoPresenter` de **7 argumentos**. Cada refatoracao interna passa a
     ser quebra de contrato.
  2. **Embutir e so nominal:** `PainelPedidoFacade.iniciar()` sempre cria a janela
     (`PainelPedidoFacade:146`), entao quem embute remonta a composicao a mao -
     Presenter de 7 argumentos + 7 handlers + 2 servicos + 2 observers. Evidencia:
     o teste do proprio consumidor faz `new PainelPedidoPresenter(...)`.
  3. **Instancia unica por componente, nao por aplicacao:** a trava e fixa em
     `${java.io.tmpdir}/painel_pedidos.lock` (`PainelPedidoFacade:177`). Dois
     sistemas diferentes na mesma maquina recusam um ao outro.
  4. **Jar gordo sem relocacao:** o shade embute a dotenv e o `pom` nao tem
     `<relocations>` - conflito de classes se o consumidor tambem usar dotenv.
- **A fronteira nao e alcancavel por visibilidade.** A fachada vive em
  `aplicacao` e importa `notificacao`, `sla`, `ingestao.observer`, `filtro`,
  `ordenacao`, `som` e `tempo`; `PedidoAbstract` (em `model`) importa `state`.
  Logo os internos nao podem virar package-private enquanto seus usuarios moram em
  outro pacote.

## Success

- Worked if: um segundo sistema, na mesma maquina, abre o painel junto com o
  primeiro; e embutir o painel nao exige importar nada fora do contrato.
- Going wrong: o segundo sistema recebe "ja esta aberto"; ou o consumidor precisa
  importar algo marcado como interno para embutir.
- Review: apos o build completo - clayton.

## Boundary

In: contrato enumerado; internos marcados como tais; um caminho de embutir pela
fachada; identidade da aplicacao na configuracao; trava por identidade; gate do
contrato.

Out: JPMS - inviavel com o jar gordo (a dotenv 3.2.0 nao tem `module-info` e o
shade exclui `module-info.class`), entao nao ha modulo para exportar/ocultar.
Publicacao no JitPack, redesenho de telas e persistencia ficam fora.

Unchanged: BR12/BR13, filtros, SLA, notificacao, ordenacao, mensagens
orientadoras, maximizacao, os tres listeners obrigatorios, `IPedidoRepository`.

## Prior art

- A convencao `...internal` para "isto nao e API" e o que se repete em bibliotecas
  Java que nao usam JPMS - adotada, porque esta fronteira precisa ser **sinalizada**,
  nao imposta pelo compilador.
- A falha que todos relatam: expor os internos primeiro e nao conseguir encolher
  depois - e exatamente o que o gate do contrato existe para impedir.
- O que fazem e nao fazemos: `exports` de JPMS para imposicao em tempo de
  compilacao - nao compartilhamos a condicao (jar gordo com dependencia nao
  modular).

## Shape

O componente passa a ter um **contrato enumerado** e todo o resto marcado como
interno por subpacote, de modo que a fronteira vire um sinal visivel em vez de 57
tipos publicos. A fachada ganha **um caminho de embutir**: ela mesma compoe o
painel e o entrega ao consumidor, em vez de o consumidor remontar vinte tipos. A
instancia unica deixa de ser por componente e passa a ser **por aplicacao**, com a
identidade vinda da configuracao. A alternativa mais pesada - impor a fronteira com
JPMS - so venceria se o artefato deixasse de ser o jar gordo, o que nao acontece.

## Key decisions

1. **O contrato e enumerado e o resto e interno.** Os internos vao para um
   subpacote `internal`, que e a marca de "nao e API"; o contrato e a lista do que
   permanece fora dele. A fronteira e sinal, nao imposicao - ver Key decision 6.
2. **Embutir reusa a composicao da fachada.** Existe um caminho suportado em que o
   componente entrega o painel montado; montar Presenter e servicos a mao deixa de
   ser suportado, e nenhum tipo necessario a embutir fica fora do contrato.
3. **A instancia unica e por aplicacao, com identidade obrigatoria na
   configuracao.** A identidade e o que separa dois sistemas na mesma maquina;
   sem ela, dois sistemas voltam a colidir. Arquivo de trava derivado da
   identidade, num diretorio temporario do sistema.
4. **A janela so e criada quando o componente e o dono dela.** Quem embute recebe
   um painel e o coloca no proprio conteiner; quem usa a fachada continua ganhando
   a janela. E o que separa "embutir" de "executar".
5. **A trava e liberada no encerramento e o arquivo nunca e apagado** - ja valido
   hoje e mantido, porque a reabertura no mesmo processo depende disso.
6. **Nao usar JPMS: a fronteira e convencao mais gate.** O jar gordo com uma
   dependencia nao modular nao pode ser um modulo confiavel, entao a eliminacao de
   JPMS e decisao, nao esquecimento; o gate (Key decision 7) e o que substitui o
   compilador.
7. **O contrato e fixado por teste de allow-list.** Um teste compara o conjunto de
   tipos publicos com uma lista explicita e falha quando um tipo entra ou sai fora
   da lista - e o unico jeito de impedir a fronteira de crescer em silencio.

## Work

| Slice | Delivers | Status |
|---|---|---|
| [Contrato](#contrato) | contrato enumerado; internos sob `internal` | clear |
| [Embutir](#embutir) | caminho suportado de embutir pela fachada | clear |
| [Identidade da aplicacao](#identidade-da-aplicacao) | identidade obrigatoria na config e trava por identidade | clear |
| [Gate do contrato](#gate-do-contrato) | teste de allow-list do contrato | clear |

Ordem: Contrato -> Embutir -> Identidade da aplicacao -> Gate do contrato.

Ja tratado pelo codigo existente: a liberacao da trava e o "ja travado como
recusa" (Key decision 5) seguem como estao; os tres listeners e a janela
maximizada nao mudam.

Derivavel do repositorio, deixado ao plano: nomes de pacotes e classes internas, o
literal do subpacote `internal`, o separador do nome do arquivo de trava, e as
regras de excecao e de mensagens orientadoras - como os registros existentes fazem.

### Contrato

**Delivers** a fronteira sinalizada: contrato fora, internos dentro de `internal`.
**Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Tipo de contrato | Permanece publico e fora de `internal` | importavel |
| Tipo interno | Passa para `internal` | nao e API (fora do contrato) |
| Consumidor importa interno | Compila, mas esta fora do contrato | risco assumido e documentado |

### Embutir

**Delivers** o caminho suportado para colocar o painel no conteiner do consumidor.
**Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Consumidor quer embutir | Recebe o painel montado pela fachada | `JPanel` pronto, sem janela |
| Consumidor quer executar | Fachada cria e exibe a janela | janela maximizada |
| Embutido, sem a identidade | Nada e aberto | recusa sinalizada |

### Identidade da aplicacao

**Delivers** a instancia unica por aplicacao. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Primeiro sistema abre | Trava da identidade A adquirida | painel aberto |
| Segundo sistema, identidade B, mesma maquina | Trava de B adquirida | painel aberto |
| Segundo sistema, identidade A | Trava de A retida | recusa sinalizada |
| Encerrar | Trava liberada; arquivo permanece | reabre no mesmo processo |

### Gate do contrato

**Delivers** a fixacao do contrato. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Contrato inalterado | Conjunto publico igual a lista | build verde |
| Tipo publico novo ou removido | Diferenca entre conjunto e lista | build falha apontando o tipo |

## Sources

- `openspec/AGENTS.md` - visibilidade minima, configuracao obrigatoria e regra de
  instancia unica (trava por arquivo, liberada no encerramento).
- `openspec/specs/painel-acompanhamento-pedidos/design.md` - Figura 7 (a fachada
  possui a trava) e a fronteira da biblioteca.
- `openspec/changes/archive/2026-10-11-componente-reutilizavel/design.md` - a
  fronteira do artefato (aplicacao -> biblioteca) e "sem consumidor a API ainda
  nao e contrato".
- Verificado nesta sessao: a dotenv 3.2.0 nao tem `module-info`; os imports
  cross-package da fachada e de `PedidoAbstract`.
