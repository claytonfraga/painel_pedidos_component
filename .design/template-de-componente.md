# Repositório como template: cada autor publica o seu componente

> Plan from this document. Each slice below carries its own shape - copy it, do not re-derive it.
> Status: confirmed by clayton, 2026-10-12

## Situation

- Project: in active construction (disciplina Projeto de Sistemas de Software / UFES).
- Decision: open - a proposta ("nova tag no JitPack, cada usuário com a sua conta") foi trazida como solução; o problema foi recuperado nesta descoberta.
- In flight: a reestruturação para dois projetos independentes (`painel-pedidos/` + `exemplo/`, sem reator) está pronta na árvore de trabalho; o modelo de distribuição por JitPack e os workflows de release/qualidade partem dela. Fica fora: a escolha do fornecedor de distribuição (JitPack hoje).
- At stake: expensive - muda o que o README ensina e as coordenadas que o `exemplo` fixa; reverter depois de outros publicarem custa coordenação.

## Problem

O problema é de **ausência**, com dois entregáveis distintos escondidos na mesma proposta.

Quem não consegue fazer isto hoje: quem precisa do componente e não é o autor. O que fazem em vez disso: leem o README e tentam consumir as coordenadas de `com.github.claytonfraga` - um artefato de outra pessoa, sobre o qual não têm controle. Nada quebra quando o artefato sob uma conta alheia não resolve (é o caso hoje: HTTP 429 no build do JitPack); o consumidor simplesmente não consegue construir, e é o autor quem descobre.

A medição que moveu a decisão: o README fixa **um** `groupId` (`com.github.claytonfraga.painel_pedidos_component`), e o `exemplo/pom.xml` fixa a mesma coordenada. Um repositório que se propõe a ser adotado por várias pessoas não pode gravar a autoria de uma. Não há número de uso a medir - o entregável é acadêmico, não um produto com usuários; o substituto observável é o README e as coordenadas que ele manda colar.

## Success

- Worked if: clonar o repositório e publicar o próprio artefato exige mudar **um** ponto (uma propriedade de autoria) e seguir **uma** seção do README; nenhum outro arquivo cita uma conta de autor.
- Going wrong: o `exemplo/pom.xml` ainda resolve - ou o README ainda manda colar - coordenadas de uma conta específica fora da propriedade de autoria.
- Review: na próxima leitura do README por outra pessoa - quem adotar o repositório diz se conseguiu publicar sem editar mais de um lugar.

## Boundary

In: parametrizar a autoria no `exemplo`; ensinar a publicar o próprio componente.

Out: trocar o fornecedor de distribuição (JitPack → Maven Central/GitHub Packages) - decisão separada, mais pesada. Publicar o `exemplo` como artefato - ele é demonstração. Resolver o 429 do Maven Central - externo e transitório; aparece aqui apenas como o que hoje impede provar o exemplo ponta a ponta.

Unchanged: o contrato público de 14 tipos do componente; o `groupId`/`artifactId` de `painel-pedidos`; a máquina de estados; os workflows de release e qualidade, exceto a autoria que eles registram.

## Prior art

- **Consumir por propriedade, não por literal** - o que toda biblioteca Java publicada faz (versão em `<properties>`); tomamos: a versão do componente já é propriedade no `exemplo`, falta o `groupId`.
- **A falha que todos relatam ao distribuir por JitPack** - builds que morrem por rate-limit do Maven Central e artefatos que não resolvem na primeira tag; mitigação é o workflow de release com retentativa e a alternativa `install:install-file`, não uma tag nova.
- **Template vs biblioteca** - projetos que se adotam por fork publicam documentação de "publique o seu", com placeholder de coordenada; é o que fazemos. A condição que não compartilhamos: não somos uma biblioteca de terceiros com consumidores anônimos.

## Shape

O repositório passa a ser um **template**: dois projetos independentes, e a autoria do componente consumido pelo `exemplo` vira um par de propriedades (`painel-pedidos.groupId`, `painel-pedidos.version`) com o valor do autor de referência como default. O README ganha uma seção "publique o seu componente" que nomeia o único ponto a mudar e o caminho de release até o JitPack. A porta: fixar a autoria em mais de um lugar - caro de reverter depois que terceiros publicarem, e por isso parametrizado desde já. A alternativa mais pesada é publicar o componente num repositório público de verdade (Maven Central), e ela só ganharia se houvesse consumidores anônimos além da disciplina - não há.

## Key decisions

1. **A autoria é parâmetro, nunca literal.** O `groupId` e a versão do componente vivem em propriedades no `exemplo/pom.xml`; nenhum outro lugar do repositório cita uma conta de autor fora dessas propriedades.
2. **O modelo é template, não biblioteca de terceiros.** O README ensina a publicar o próprio componente e a apontar o exemplo para ele; não instrui a consumir o artefato de uma conta específica como fim.
3. **O `exemplo` usa o artefato publicado uma vez, como prova que compila.** Ele resolve pelo JitPack (não pelo código-fonte), com a versão publicada pelo autor de referência como default - é o teste vivo de que a publicação funciona.
4. **A distribuição continua no JitPack.** Rejeitado Maven Central agora: setup e credenciais incompatíveis com o porte da atividade; a revisão fica registrada em Boundary.

## Work

| Slice | Delivers | Status |
|---|---|---|
| [Autoria parametrizada](#autoria-parametrizada) | o `exemplo` resolve o componente por propriedades, sem conta fixa fora delas | clear |
| [Publicação própria](#publicacao-propria) | README ensina a publicar o próprio componente e onde apontar o exemplo | clear |

Order: Autoria parametrizada → Publicação própria.

Derivable from the repository, left to the plan: o nome das propriedades segue a convenção existente (`<artifact>.version`); a versão publicada é atualizada pelo workflow de release.

### Autoria parametrizada

**Delivers** que o `exemplo` dependa do componente por `painel-pedidos.groupId` + `painel-pedidos.version`, com o autor de referência como default e nenhuma coordenada fixa fora dessas propriedades. **Status: clear.**

| State | What should happen | Caller sees |
|---|---|---|
| Clonado por outro autor | trocar as duas propriedades resolve o próprio artefato | build verde sem editar nenhum outro arquivo |
| Default (autor de referência) | resolve o artefato publicado do autor de referência | build verde pelo JitPack |
| Artefato da versão ausente no JitPack | a resolução falha claramente | erro do Maven apontando a coordenada |

Alternatives considered: manter o `groupId` literal e pedir edição no `pom.xml` - rejeitada; a propriedade é o único ponto.

### Publicação própria

**Delivers** uma seção no README que nomeia o ponto único de autoria e o caminho tag → Release → JitPack → dependência. **Status: clear.**

| State | What should happen |
|---|---|
| Autor novo | publica a própria tag e troca as propriedades; o exemplo passa a resolver o artefato dele |
| Artefato não resolve na primeira tentativa | a seção diz que o JitPack constrói no primeiro pedido e que o rate-limit do Central é transitório |

Alternatives considered: documentar `install:install-file` como caminho principal - rejeitada como principal (o objetivo é publicar); permanece como saída de emergência quando o JitPack não constrói.

## Sources

- `README.md`, `painel-pedidos/README.md` - coordenadas hoje fixas em uma conta.
- `exemplo/pom.xml` - dependência do componente; alvo da parametrização.
- `.github/workflows/release.yml`, `jitpack.yml` - o caminho de publicação por tag.
- Log do build do JitPack para `v1.0.1` - o 429 do Maven Central que hoje impede provar o exemplo ponta a ponta.
