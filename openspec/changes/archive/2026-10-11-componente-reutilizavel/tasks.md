# Tarefas - Componente reutilizavel de Painel de Acompanhamento de Pedidos

## 1. Estrutura Maven

- [x] Criar parent agregador (`painel-pedidos-parent`) com modulos
      `painel-pedidos` e `exemplo`, Java 21 e enforcer `[21,)`.
- [x] Mover `src/main/java` para `painel-pedidos/src/main/java`.
- [x] Configurar maven-shade no componente (jar com dependencias).

## 2. Sanitizacao

- [x] Remover `simulacao/*` (simulador, gerador, aleatoriedade, configuracao).
- [x] Remover `seeder/PedidoSeeder` e `dados/CatalogoPedido`.
- [x] Remover `configuracao/LeitorEnv` e os arquivos `.env`/`.env.example`.
- [x] Mover relogio/fonte de data para `tempo` e observers para `ingestao`.

## 3. Configuracao

- [x] Criar `ConfiguracaoPainelPedidos` (value object).
- [x] Criar `LeitorConfiguracaoPainelPedidos` (Adapter dotenv) lendo
      `painel_pedidos.env`.
- [x] Criar `painel_pedidos.env.example`.

## 4. Ingestao

- [x] Criar `IFonteNovoPedido`.
- [x] Criar `LeitorNovoPedidoService` (GoF Observer) com dedup por
      identificador e intervalo configuravel.

## 5. Fachada

- [x] Criar `PainelPedidos` (`iniciar`, `encerrar`, `receberNovosPedidos`,
      `atualizar`, `usarFonteNovoPedido`).
- [x] Injetar `IRelogioService` no `AgendadorMonitorSla`.

## 6. View embedavel

- [x] Extrair `PainelPedidoPanel` (`JPanel`) de `PainelPedidoView`.
- [x] Tornar `IPainelPedidoView` independente de `JFrame`
      (`getPainelPrincipal`/`fechar`).
- [x] Ajustar `PainelPedidoPresenter` para nao exibir janela.

## 7. Empacotamento e exemplo

- [x] Modulo `exemplo` com `main`, repositorio em memoria, seeder de dados de
      exemplo (`PedidoSeeder`) e fonte de novos pedidos em memoria.
- [x] `painel_pedidos.env` do exemplo.
- [x] Jar gordo do componente e jar executavel do exemplo.

## 8. Garantia por testes

- [x] `PedidoTest` (BR12/BR13).
- [x] `LeitorConfiguracaoPainelPedidosTest` (config).
- [x] `LeitorNovoPedidoServiceTest` (ingestao/dedup).
- [x] `PainelPedidosTest` (fachada headless + empurrao).
- [x] `EstruturaComponenteTest` (ausencia de simulacao/mock).
- [x] `PainelPedidoImagemTest` (print do painel em `docs/painel-pedidos.png`).

## 9. Documentacao

- [x] README em portugues com print, JitPack e `painel_pedidos.env`.
- [x] Atualizar `openspec/` (project, prd, spec, design, AGENTS, features).
- [x] Remover o Look and Feel do componente (responsabilidade do cliente).
- [x] Ligar o botao "Fechar" a fachada (encerra a janela).
- [x] `mvn -U clean verify` verde.
