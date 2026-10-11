# Tarefas - Botao "Novo Pedido"

## 1. OpenSpec

- [x] Change `2026-10-11-botao-novo-pedido`.
- [x] Feature `us29-botao-novo-pedido.feature`.
- [x] Atualizar `spec.md`, `AGENTS.md`, `project.md`, `prd.md`.

## 2. Listener e View

- [x] `listener/ICriarNovoPedidoListener` (`List<PedidoAbstract> criarNovoPedido()`).
- [x] `IPainelPedidoView.getBtnNovoPedido()`.
- [x] `PainelPedidoPanel`: botao "Novo Pedido" a esquerda de "Visualizar pedido".
- [x] `PainelPedidoView`: delegar `getBtnNovoPedido()`.

## 3. Presenter e Fachada

- [x] Presenter: campo + `setCriarNovoPedidoListener(...)`; ligar o botao
      (salvar + atualizar); sem listener lanca excecao.
- [x] `PainelPedidoFacade.setCriarNovoPedidoListener(...)` + validar em `iniciar()`.

## 4. Exemplo

- [x] `exemplo/listener/CriarNovoPedidoListener`: pergunta 1..10 (limite no
      codigo), copia pedidos do seeder via `criarCopia` (novo id + data atual).

## 5. Testes e build

- [x] Presenter: criar sem listener lanca; com listener salva/atualiza.
- [x] Fachada: `iniciar()` sem o listener lanca.
- [x] `mvn -U clean verify` verde e sincronizacao.
