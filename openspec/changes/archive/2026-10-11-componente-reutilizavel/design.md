# Componente reutilizavel de Painel de Acompanhamento de Pedidos

> Design da mudanca. Plano a partir deste documento: cada fatia carrega sua
> propria forma.
> Status: confirmed by clayton, 2026-10-11

## Situation

- Projeto: POC academico concluido (MVP Passive View), nao um produto em uso;
  13 commits, autor unico, sem consumidores.
- Decisao: **ja comprometida** por clayton - o projeto deve virar componente
  reutilizavel. Nao ha gate de veredito; o trabalho comeca na forma.
- Em voo: a estrutura OpenSpec e o precedente onde esta mudanca vive; as regras
  de codigo (sem lambda/Stream/enum/instanceof/`static`, um unico `main`) sao
  restricao; o GoF State (ciclo de vida), o CoR adaptado e o Observer sao
  precedente a preservar.
- O que esta em jogo: muda a fronteira do artefato (aplicacao -> biblioteca) e
  sua API publica. Como **nao existe consumidor hoje**, a API ainda nao e
  contrato, o que torna barato escolher a forma mais leve agora.

## Problem

Ausencia/construcao.

- **Quem nao consegue:** um desenvolvedor de sistema de delivery que queira
  embutir o painel hoje.
- **Substituto (evidencia):** faz fork/clone do repositorio e corta simulacao,
  seed e `main` na mao - exatamente o custo que se quer eliminar - ou reescreve
  o painel.
- **O que fica impossivel sem isso:** reusar o painel sem forkar; nao existe
  API para injetar dados nem para receber "novo pedido" (o simulador e a unica
  fonte).
- **O numero que mudaria a decisao:** quantos consumidores existem/estao
  comprometidos hoje. Resposta: zero conhecido. Isso nao e achado sobre o
  problema, e achado sobre a instrumentacao.

### Cheaper than building

Um flag para desligar seed/simulacao seria mais barato, mas nao resolve: sem
porta de ingestao o painel fica sem fonte de "novo pedido". Nao ha compra (e um
painel Swing sob medida). Construir a fronteira e necessario.

### Fronteira desta rodada

- **Responde:** fronteira/API do componente, ingestao de pedidos, configuracao,
  empacotamento (jar com dependencias), isolamento da View, projeto de exemplo,
  o que sai (simulacao, dados mockados, seed, `.env`).
- **Nao responde:** integracao de um consumidor real (nao existe) nem
  persistencia real/autenticacao.

## Success

- **Worked if:** um outro projeto adiciona o jar como dependencia, implementa a
  porta de dados e executa o painel sem copiar o repositorio; o projeto de
  exemplo faz isso e roda.
- **Sinal cedo:** `mvn -U clean verify` verde com os testes de garantia; o jar
  gordo e gerado contendo as dependencias; o print do painel e produzido pelo
  teste headless.
- **Review:** apos o primeiro build completo - clayton.

## Boundary

- **In:** componente biblioteca (jar com dependencias), fachada `PainelPedidos`,
  portas `IPedidoRepository` e `IFonteNovoPedido`, leitura periodica
  (GoF Observer), configuracao por `painel_pedidos.env`, View embedavel, projeto
  de exemplo, README em portugues, testes.
- **Out:** persistencia real (SQL/H2), autenticacao, integracao com gateway de
  pagamento, publicacao no JitPack (a cargo do mantenedor).
- **Inalterado:** BR12/BR13 (ciclo de vida GoF State), filtros, ordenacao por
  prioridade/FIFO, SLA, notificacao (CoR), instancia unica, diretrizes visuais.

## Shape

O componente e uma **biblioteca** empacotada como jar que contem todas as suas
dependencias (maven-shade). O consumidor adiciona a dependencia, implementa
`IPedidoRepository` (sua origem de dados) e, se quiser leitura periodica,
`IFonteNovoPedido`; instancia a fachada `PainelPedidos` e chama `iniciar()`. A
ingestao de novos pedidos deixa de ser simulada: e uma leitura a cada N
milissegundos (GoF Observer, sujeito `LeitorNovoPedidoService`) ou um empurrao
por `receberNovosPedidos(...)`, ambos parametrizados em `painel_pedidos.env`. A
porta de mao unica e o contrato de dados (`IPedidoRepository`): trocar por banco
custa uma nova implementacao e nada mais. A alternativa mais pesada - portas de
comando por acao, com o sistema do consumidor como fonte da verdade - so vence
se o painel deixar de ser o escritor do estado do pedido, o que nao e o caso
hoje.

## Key decisions

1. **Componente e biblioteca, nao aplicacao.** Nao ha `main` no componente; o
   ponto de entrada e a fachada `PainelPedidos`, e o `main` fica no projeto de
   exemplo (consumidor).
2. **Jar gordo com dependencias.** Todas as dependencias ficam no `pom.xml` e
   sao embutidas no jar via maven-shade, para uso como dependencia unica no
   JitPack.
3. **Fim da simulacao e dos dados mockados.** Nenhuma classe gera, sorteia ou
   semeia pedidos; `seeder`, `dados`, `simulacao` e `configuracao/LeitorEnv`
   deixam de existir (garantido por teste).
4. **Ingestao por porta, nao por geracao.** `IFonteNovoPedido` e lida a cada N
   ms (`LEITURA_NOVO_PEDIDO_INTERVALO_MS`) por um sujeito GoF Observer; o
   empurrao (`receberNovosPedidos`) tambem e suportado pelo mesmo observador.
5. **Configuracao em `painel_pedidos.env`.** Leitura encapsulada por
   `LeitorConfiguracaoPainelPedidos` (Adapter sobre dotenv); nao ha mais `.env`
   nem `SEED_QUANTIDADE`/`SIMULACAO_*`.
6. **View isolada da janela.** `PainelPedidoPanel` e um `JPanel` embedavel que
   implementa `IPainelPedidoView`; `PainelPedidoView` e apenas um `JFrame`
   invólucro. Isso permite embutir o painel e renderiza-lo headless (print).
7. **Relogio injetado.** `IRelogioService` e injetado (inclusive no
   `AgendadorMonitorSla`) para nao depender do relogio do sistema em regra de
   negocio.
8. **Multi-modulo Maven, Java 21.** Parent agregador + `painel-pedidos`
   (biblioteca) + `exemplo` (consumidor); enforcer exige JDK `[21,)`.
9. **Look and Feel e da aplicacao cliente.** O componente NAO chama
   `UIManager.setLookAndFeel`; quem define o Look and Feel (ex.: do
   sistema/Windows) e a aplicacao cliente, antes de iniciar o painel.
10. **Fechar e da fachada.** O botao "Fechar" encerra a janela pela fachada
   (`PainelPedidos`), que e quem a cria; a View permanece passiva e nao fecha
   janelas.

## Work

| Fatia | Entrega quando pronta | Status |
|---|---|---|
| [EstruturaMaven](#estruturamaven) | Parent + modulos `painel-pedidos` e `exemplo`, Java 21, enforcer. | clear |
| [Sanitizacao](#sanitizacao) | Sem simulacao, seed, dados mockados ou `LeitorEnv`. | clear |
| [Configuracao](#configuracao) | `ConfiguracaoPainelPedidos` + `LeitorConfiguracaoPainelPedidos` lendo `painel_pedidos.env`. | clear |
| [Ingestao](#ingestao) | `IFonteNovoPedido` + `LeitorNovoPedidoService` (Observer) + empurrao. | clear |
| [Fachada](#fachada) | `PainelPedidos` compoe e executa o componente sobre as portas. | clear |
| [View embedavel](#view-embedavel) | `PainelPedidoPanel` + `PainelPedidoView` + Presenter sem janela. | clear |
| [Empacotamento e exemplo](#empacotamento-e-exemplo) | Jar gordo; modulo `exemplo` consumindo o componente. | clear |
| [Garantia por testes](#garantia-por-testes) | Testes JUnit/Hamcrest cobrindo dominio, config, ingestao, estrutura e print. | clear |

Ordem: EstruturaMaven -> Sanitizacao -> Configuracao -> Ingestao -> Fachada ->
View embedavel -> Empacotamento e exemplo -> Garantia por testes.

Ja tratado pelo codigo existente: BR12/BR13, filtros, ordenacao, SLA,
notificacao, instancia unica e diretrizes visuais seguem como estao.

Derivavel do repositorio, deixado ao plano: nomes de pacotes, classes e
metodos, e as regras de excecao unchecked/`Objects.requireNonNull` - como os
registros existentes fazem.

### EstruturaMaven

**Entrega** o parent e os dois modulos. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Sem modulos | Projeto raiz nao compila | - |
| Modulos definidos | `mvn -U clean verify` compila os tres com Java 21 | build verde |

### Sanitizacao

**Entrega** a remocao de tudo que simula ou mocka. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Simulacao presente | Classes em `simulacao` | removidas |
| Dados mockados | `seeder`/`dados` | removidos |
| Config antiga | `configuracao/LeitorEnv`/`.env` | removida |

### Configuracao

**Entrega** a configuracao por arquivo. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Arquivo `painel_pedidos.env` presente | Chaves lidas | valores aplicados |
| Arquivo ausente | Defaults seguros | componente executavel |

### Ingestao

**Entrega** a leitura periodica e o empurrao. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Pedidos novos na fonte | Notificados uma vez (dedup por identificador) | som/mensagem/destaque |
| Identificador ja notificado | Ignorado | sem notificacao repetida |
| Sem pedidos | Nada acontece | painel inalterado |

### Fachada

**Entrega** `PainelPedidos`. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Construida | Painel e Presenter montados (headless) | sem janela |
| `iniciar()` | Janela exibida, SLA e leitura iniciados | painel aberto |

### View embedavel

**Entrega** o painel e a janela. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| Painel obtido | `JPanel` com filtros e seis colunas | painel embedavel |
| Janela construida | `JFrame` hospedando o painel | janela maximizada |

### Empacotamento e exemplo

**Entrega** o jar gordo e o modulo de exemplo. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| `mvn package` | Jar com as dependencias | um unico jar |
| Exemplo executado | Painel aberto sobre dados de exemplo | demonstracao |

### Garantia por testes

**Entrega** a suite de garantia. **Status: clear.**

| Estado | O que deve acontecer | Consumidor ve |
|---|---|---|
| `mvn verify` | Testes verdes | garantia dos requisitos |
| Print | Teste headless gera `docs/painel-pedidos.png` | imagem no README |
