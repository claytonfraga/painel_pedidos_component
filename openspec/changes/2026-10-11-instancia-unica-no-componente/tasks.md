# Tarefas - Instancia unica dentro do componente

## 1. Especificacao

- [x] Change `2026-10-11-instancia-unica-no-componente`.
- [x] Feature `us25-instancia-unica.feature` (restaurada, em linguagem de negocio).
- [x] Requirement em `spec.md` (deixa de ser "opcional") e `AGENTS.md`.
- [x] `project.md`, `prd.md`, `state-machines.md`, `design.md` e READMEs.

## 2. Trava

- [x] `ITravaInstanciaUnica.liberar()`.
- [x] `TravaInstanciaUnicaArquivo`: guardar o canal; `liberar()` fecha o canal
      (o arquivo permanece); `tryLock()==null` e `OverlappingFileLockException`
      resultam em `false`, sem excecao.

## 3. Estado unico na fachada

- [x] `PainelPedidoFacade`: trava padrao propria; `iniciar()` adquire antes de
      abrir e devolve `boolean`; `encerrar()` libera.
- [x] Remover `IExecucaoAplicacao` e `InstanciaUnicaAplicacao`; a fachada deixa de
      implementar `IExecucaoAplicacao`.

## 4. Exemplo

- [x] `AplicacaoPainelPedido.main`: sem trava, caminho nem singleton; usa o
      retorno de `iniciar()`.

## 5. Testes e build

- [x] `TravaInstanciaUnicaArquivoTest`: segunda aquisicao `false`; apos `liberar()`
      `true`; arquivo permanece.
- [x] `PainelPedidoFacadeTest`: segunda abertura recusada (`false`, sem excecao) com
      a trava real retida.
- [x] `EstruturaComponenteTest`: `IExecucaoAplicacao`/`InstanciaUnicaAplicacao`
      ausentes.
- [x] `mvn -U clean verify` verde e sincronizacao.
