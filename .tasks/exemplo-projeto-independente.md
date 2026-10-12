# Exemplo como projeto independente consumindo o JitPack

> Build this with **tlc-implement**.
> Every criterion below becomes a check with a proof, referenced by its number. Nothing under
> `Unresolved` gets settled while building.

## Intent

Hoje o repositório é um **reator** único: a raiz declara `<modules>` e é o `parent` que carrega
`dependencyManagement` e `pluginManagement`; o exemplo depende de `br.projeto:painel-pedidos` na versão
do próprio reator (`${project.version}`). Consequência prática: o exemplo bebe do **código-fonte**, o
caminho publicado nunca é exercitado, e a distribuição do componente não é validada por ninguém -
o consumidor que existe é o único que **não** passa pelo artefato.

O que passa a valer: **dois projetos independentes**. `painel-pedidos` é o componente e se basta
sozinho; `exemplo` é um consumidor que depende do **artefato publicado no JitPack**, na última versão
gerada. O que se ganha é o teste que faltava: se a publicação quebrar, o **build do exemplo quebra** -
antes ninguém notava.

9 criterios em 4 slices · 5 portas de mao unica · 1 aberta, que bloqueia.

## Criteria

### Projeto do componente

1. Always, `cd painel-pedidos && mvn -U clean verify` conclui verde, sem depender de nenhum diretório
   fora de `painel-pedidos/`.
2. Always, `painel-pedidos/pom.xml` não contém `<parent>` e declara `groupId`, `artifactId` e `version`
   próprios (`br.projeto:painel-pedidos`).
3. Always, as versões de dependência e plugin usadas por `painel-pedidos` estão em propriedades dentro
   de `painel-pedidos/pom.xml` - a centralização passa a ser por projeto.

### Projeto do exemplo

4. When o exemplo é construído, then ele resolve `com.github.claytonfraga.painel_pedidos_component:painel-pedidos`
   do JitPack, com a versão vinda de uma propriedade (`painel-pedidos.version`), em versão igual a uma
   tag publicada do repositório.
5. Given a versão publicada existente no JitPack, when `cd exemplo && mvn -U clean verify` roda, then
   conclui verde sem nenhum módulo do componente no mesmo reator.
6. Always, `exemplo/pom.xml` não contém `<parent>` nem a dependência `br.projeto:painel-pedidos`.

### Fim do reator

7. Always, a raiz do repositório não declara `<modules>`; `pom.xml` de agregação deixa de existir como
   caminho de build.
8. Always, nenhum `pom.xml` do repositório referencia `painel-pedidos-parent`.

### Fluxo de CI/CD

9. When uma tag `v*` é empurrada, then o componente é verificado e publicado, e a propriedade
   `painel-pedidos.version` do exemplo é atualizada para a versão publicada, num commit automático.

## Out of scope

- Protocolo de descoberta de versão em tempo de build (ranges dinâmicos do Maven) - trocaria um número
  explícito por resolução implícita, contra a regra de build determinístico.
- Publicar o exemplo como artefato - ele é demonstração, não biblioteca.
- Trocar o JitPack por outro repositório (Maven Central, GitHub Packages) - decisão de distribuição
  fora desta tarefa.

## Observable

| Surface | Decision | Landing |
| --- | --- | --- |
| API do consumidor (`exemplo/pom.xml`) | response shape | 4 - coordenada publicada, versão por propriedade |
| API do consumidor (`exemplo/pom.xml`) | error shape with codes | 5 - versão ausente no JitPack falha com mensagem do Maven |
| command `mvn verify` (raiz) | exit codes | 7 - deixa de ser o gate; o gate é por projeto |
| command `mvn verify` (componente) | output format | 1 |
| command `mvn verify` (exemplo) | output format | 5 |
| command `release` (tag `v*`) | what it prints when it fails halfway | 9 - publica e então atualiza a propriedade |
| document `README.md` | what the reader does next | 7, 9 - build por projeto e versão da dependência |
| document `AGENTS.md` | structure | 1, 5, 7 - a regra "na raiz compila os tres modulos" cai |
| collection `poms do repositorio` | grouping criterion | 2, 6, 8 - projetos independentes, sem parent |
| collection `poms do repositorio` | the exception that does not fit | n/a - todos seguem a mesma regra |

## Swept

- validation: 4 - a versão do exemplo precisa existir no JitPack
- failure modes: 5 - artefato ausente falha o build do exemplo, com a mensagem do Maven
- idempotency and retry: n/a - operação de build, não de escrita
- authorization: n/a - repositório público; a resolução anônima já foi verificada
- concurrency and ordering: 9 - o componente publica antes de o exemplo apontar para a versão
- data lifecycle: n/a - nada persistido
- external-dependency failure: Unresolved 1 - o JitPack não resolve o artefato hoje (429 do Central)
- state transitions: n/a - nenhuma mudança de domínio
- observability: 1, 5 - um job de CI por projeto, verde ou vermelho isolado

## Impact

| Front | What changes |
|---|---|
| domain | nothing - nenhum termo de domínio muda |
| build | a topologia: reator com `parent` + `<modules>` vira dois projetos independentes |
| build | o gate local: `mvn -U clean verify` na raiz deixa de existir; passa a ser por projeto |
| stored data | nothing to migrate |

Disturbed: os três `pom.xml`, os três workflows (`qualidade.yml`, `release.yml`, `jitpack.yml`),
`README.md`, `painel-pedidos/README.md`, `exemplo/nbactions.xml` e a seção de build do
`openspec/AGENTS.md`.

## Decided

| Decision | Shape | Alternative rejected |
|---|---|---|
| O repositório tem dois projetos independentes | sem `parent` compartilhado, sem `<modules>`, sem pom de agregação | manter o reator - não exercita o artefato publicado |
| O exemplo consome o artefato publicado | `com.github.claytonfraga.painel_pedidos_component:painel-pedidos`, versão por propriedade | depender de `br.projeto:painel-pedidos:${project.version}` - bebe do fonte |
| A versão do exemplo é atualizada pelo release | o workflow de release ajusta `painel-pedidos.version` e commita | range dinâmico (`[1.0,)`) - build não determinístico |
| Cada projeto centraliza as próprias versões | propriedades no `pom.xml` de cada um | heredar do parent - deixa de existir |
| O gate é por projeto | `cd <projeto> && mvn -U clean verify` | gate na raiz - sem reator, não há raiz |

## Surface

| Route | In | Out | Status | Criteria |
|---|---|---|---|---|
| `painel-pedidos/` build | `pom.xml` próprio | jar com dependências | verde isolado | 1, 2, 3 |
| `exemplo/` build | `painel-pedidos.version` | classe consumidora | verde com o artefato do JitPack | 4, 5, 6 |
| release `v*` | tag | Release + commit de versão no exemplo | publica e então atualiza | 9 |

## Sources

- `pom.xml`, `painel-pedidos/pom.xml`, `exemplo/pom.xml` - a topologia atual (parent, modules, e a
  dependência de reator do exemplo).
- `.github/workflows/` - os três workflows rodam a partir da raiz hoje.
- `openspec/AGENTS.md` - a regra de build ("na raiz compila e testa os tres modulos").
- Documentacao do JitPack lida nesta sessao - coordenadas de multi-módulo
  (`com.github.USUARIO.REPOSITORIO:MODULO:VERSAO`) e "publicar é criar uma Release".

This task is the record of decision. If a linked document diverges, ask before building.

## Unresolved

| # | Kind | Question | Until answered |
|---|---|---|---|
| 1 | blocks | O artefato do JitPack resolve? Hoje não: `painel-pedidos-v1.0.1.pom` responde 404 e o log do build é `429 Too Many Requests` do Maven Central. | O criterio 5 não pode ser satisfeito: o build do exemplo não passa enquanto a versão não existir no JitPack. |
