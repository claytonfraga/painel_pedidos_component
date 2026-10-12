# PRD - Distribuicao e CI/CD do componente

> Status: entregue e verificado (v1.0.3 publicada no JitPack e consumida pelo exemplo).
> Fontes normativas: `specs/painel-acompanhamento-pedidos/spec.md` (`@US27-009` a
> `@US27-012`) e `nao-funcional.feature`.

## 1. Visao e objetivo

O repositorio deixou de ser um reator multi-modulo e passou a ser um **template de
dois projetos independentes**: `painel-pedidos/` (o componente) e `exemplo/` (o
consumidor). Publicar e consumir o componente tem de ser um caminho **automatico,
verificavel e reproduzivel por qualquer autor**, sem depender de passos manuais na
maquina de quem publica.

## 2. Contexto e problema

- O `exemplo` consumia o componente pelo **codigo-fonte** (modulo do reator): a
  publicacao nunca era exercitada, entao uma quebra em publicar passava despercebida.
- A coordenada publicada nao era obvia: sem pom na raiz, o JitPack publica o
  repositorio como **artefato unico** (`com.github.<autor>:<repositorio>:<tag>`), e
  nao `:painel-pedidos`.
- O build do JitPack falhava por um motivo **nao obvio**: a imagem padrao dele traz
  **Maven < 3.6.3**, e o `maven-enforcer-plugin:3.5.0` exige **Maven >= 3.6.3**.
- Publicar e verificar era **circular**: um workflow que cria a Release e espera o
  JitPack trava, porque o JitPack so constroi **depois** da Release.

## 3. Escopo

**Dentro:**
- Dois projetos Maven independentes, sem pom agregador nem parent compartilhado.
- O `exemplo` consome o artefato **publicado** no JitPack (nao o codigo-fonte).
- Autoria e versao em propriedades do `exemplo/pom.xml` (`painel-pedidos.groupId`,
  `painel-pedidos.artifactId`, `painel-pedidos.version`) - o unico ponto onde uma
  conta de autor aparece.
- Publicacao por tag e verificacao do consumo, em **dois workflows**.

**Fora:**
- Publicacao no Maven Central / GitHub Packages (fica no JitPack).
- Assinatura de artefatos e SBOM.
- Cache de build do JitPack entre execucoes.

## 4. Fluxo (o workflow)

**Etapa 1 - `release.yml`** (gatilho: tag `v*`):
1. alinha a versao do componente com a tag (sem o `v`);
2. roda o gate de qualidade do componente;
3. cria/atualiza a **GitHub Release** com o jar. **Termina aqui** - nao espera o JitPack.

**Etapa 2 - `verificar-jitpack.yml`** (gatilho: conclusao do `Release`):
1. resolve a **ultima tag**;
2. **aguarda 30 s** e consulta a URL do artefato
   (`https://jitpack.io/com/github/<owner>/<repo>/<tag>/<repo>-<tag>.pom`), que
   dispara o "Get it" do JitPack;
3. repete a consulta ate o artefato resolver (HTTP 200);
4. registra a versao no `exemplo/pom.xml` e roda `cd exemplo && mvn -U clean verify`;
5. commita a versao registrada, se mudou.

**Gate continuo - `qualidade.yml`** (gatilho: push/PR em `main`): gate do componente
e, quando o artefato publicado resolve, o do exemplo.

**Config do JitPack - `jitpack.yml`**: instala um **Maven moderno** no `before_install`
(por causa do defeito (a) abaixo) e builda `painel-pedidos/`.

## 5. Decisoes e o motivo dos defeitos antigos

- **Maven antigo na imagem do JitPack.** Bloqueio real: `maven-enforcer-plugin:3.5.0`
  exige `>= 3.6.3`. Resolvido com `before_install` instalando o Maven 3.9.10; sem
  isso, o build do JitPack volta a falhar.
- **A versao no JitPack e a tag** (`v1.0.3`), nao o numero sem o `v` - a URL sem o
  `v` responde 404.
- **Coordenada publicada**: `com.github.<autor>:<repositorio>:<tag>` (artefato unico,
  sem pom na raiz). O `artifactId` e o nome do repositorio.
- **Dois workflows** para eliminar o ciclo: a verificacao e disparada pela conclusao
  do release, nunca dentro dele.
- **O `before_install` do `jitpack.yml` e load-bearing**: nao remover. A explicacao
  tecnica mora aqui e nos comentarios do proprio `jitpack.yml`; o README de consumo
  leva so o que e acionavel.

## 6. Riscos e limitacoes

- **Indice de tags do JitPack** pode atrasar em repositorio novo; por isso a
  verificacao tenta por tag e por commit.
- **Build do JitPack e compartilhado**: rate-limit do Maven Central (HTTP 429) e
  transitorio - reenviar a tag resolve.
- **Fork de template**: cada autor troca as tres propriedades e publica na propria
  conta; a coordenada de referencia nao deve ser tratada como fixa.

## 7. Metricas de sucesso

- Publicar = criar a tag; nenhum passo manual no JitPack.
- O exemplo compila contra o artefato publicado no gate e na verificacao pos-release.
- Uma quebra na publicacao aparece como falha de CI, nao como surpresa no consumidor.

## 8. Rastreabilidade

- Requisitos: `spec.md` (`@US27-009` a `@US27-012`).
- Cenarios: `nao-funcional.feature` (`@US27-009` a `@US27-012`, `@US27-003`).
- Workflows: `.github/workflows/release.yml`, `verificar-jitpack.yml`, `qualidade.yml`.
- Config: `jitpack.yml`.
