# Painel de Acompanhamento de Pedidos

Este projeto é uma **atividade da disciplina Projeto de Sistemas de Software**
(UFES) e é, ele mesmo, um exercício do que a ementa cobra: *"padrões de projeto,
princípio de reuso, construção e uso de componentes de software e frameworks"*.

O que se entrega é um componente **Java Swing** reutilizável para sistemas de
delivery. Apresenta os pedidos em seis colunas de situação — **Novo**, **Em
preparo**, **Pronto para entrega**, **Em rota de entrega**, **Entregue** e
**Cancelado** — com busca, filtro por período, ações de transição (BR12),
cancelamento com crédito (BR13), prioridade por SLA e ordenação em fila (FIFO).

O componente é consumido como **dependência Maven**: o seu sistema de delivery
implementa um pequeno conjunto de **portas** (interfaces) e o painel chama de
volta as **suas** telas. Não há simulação, geração de pedidos, seed nem dados
mockados no componente — os dados e as telas são sempre seus.

## O que você precisa fazer e saber

**Fazer** — quatro passos, detalhados na seção 2:

1. adicionar o componente como dependência (seção 1);
2. estender `PedidoAbstract` e implementar `IPedidoRepository` — o modelo e a
   origem de dados são seus;
3. implementar os **três listeners obrigatórios**: visualizar pedido, confirmar
   pagamento e criar novo pedido;
4. criar o `painel_pedidos.env` com as quatro chaves de SLA e o
   `IDENTIFICADOR_APLICACAO` (seção 3).

**Saber** — o que costuma surpreender quem integra:

- **Os dados e as telas são do seu sistema.** O componente não gera, não semeia e
  não guarda pedidos: ele exibe o que você entregar.
- **A ingestão é por empurrão.** Você chama `receberNovosPedidos(...)`; não há
  leitura automática, e o componente **não deduplica** (seção 2.6).
- **O ciclo de vida é fixo** (BR12/BR13): seis situações e as transições da seção
  5 — não há como criar uma situação nova sem mexer no componente.
- **O SLA é por estado, em minutos**, vem do `.env`, e marca "Prioridade" quando o
  pedido *ultrapassa* o limite (seção 5).
- **O contrato público tem 14 tipos**; o resto vive sob `internal` e pode mudar
  entre versões (seção 3, "Contrato público").
- **Há um consumidor completo para copiar**: o módulo [`exemplo`](exemplo/) faz
  tudo isso e roda (seção 6).

## Requisitos

- Java 21 (alvo do bytecode `--release 21`; o build pode rodar em JDK 21 ou
  superior, ex.: 24).
- Maven 3.9+.

## 1. Adicione a dependência

O repositório pode ser publicado no [JitPack](https://jitpack.io). Substitua
`<usuario>` pelo usuário do GitHub e `<tag>` pela versão publicada:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.claytonfraga.painel_pedidos_component</groupId>
    <artifactId>painel-pedidos</artifactId>
    <version><tag></version>
</dependency>
```

O jar de `painel-pedidos` **já contém todas as dependências** (é empacotado com
`maven-shade-plugin`), então esta é a única dependência necessária.

**Uso local** (antes de publicar): a partir da raiz do projeto do componente,
rode `mvn -U clean install` e dependa de `br.projeto:painel-pedidos:1.0.0`.

## 2. O que o seu projeto implementa

### 2.1 Estenda `PedidoAbstract` (o modelo)

O componente **não possui pedido concreto**. Estenda
`br.projeto.painelpedidos.model.PedidoAbstract` — ela já traz os dados essenciais
do painel e a **máquina de estados** (GoF State) — e implemente `criarCopia`
(usada pelo botão "Criar novo pedido" a partir de um cancelado):

```java
public class MeuPedido extends PedidoAbstract {

    public MeuPedido(String identificador, LocalDateTime dataHora, String nomeCliente,
            String enderecoEntrega, double valorTotal) {
        super(identificador, dataHora, nomeCliente, enderecoEntrega, valorTotal);
    }

    @Override
    public PedidoAbstract criarCopia(String novoIdentificador, LocalDateTime novaDataHora) {
        return new MeuPedido(novoIdentificador, novaDataHora,
                getNomeCliente(), getEnderecoEntrega(), getValorTotal());
    }
}
```

A `PedidoAbstract` oferece (e o painel usa):

| Grupo | Métodos |
|---|---|
| Identidade/dados | `getIdentificador`, `getDataHora`, `getNomeCliente`, `getEnderecoEntrega`, `getValorTotal`, `getFormaPagamento`, `getCreditoGerado`, `getSituacao`, `getDataHoraMudancaEstado` |
| Estado | `estaNovo`, `estaEmPreparo`, `estaProntoParaEntrega`, `estaEmRotaDeEntrega`, `estaEntregue`, `estaCancelado`, `isPagamentoRegistrado`, `isPrioridade` |
| Ações | `registrarPagamento`, `marcarProntoParaEntrega`, `sairParaEntrega`, `marcarEntregue`, `cancelar`, `copiarComoNovo` |
| Copia | `criarCopia` (**abstrata** — você implementa) |

Regras validadas pela própria máquina de estados: não avança **Novo → Em
preparo** sem pagamento; **Cancelado** não retorna; o crédito é calculado no
cancelamento (BR12/BR13).

### 2.2 Implemente `IPedidoRepository` (a origem de dados)

```java
public class MeuRepositorioDePedidos implements IPedidoRepository {
    @Override public void salvar(PedidoAbstract pedido) { /* inserir/atualizar */ }
    @Override public List<PedidoAbstract> buscarTodos() { /* listar por período */ }
    @Override public String gerarProximoIdentificador() { /* próximo número */ }
}
```

### 2.3 Implemente `IVisualizarPedidoListener` (**obrigatório**)

O componente **não** abre a tela de detalhes: o botão "Visualizar pedido" chama o
seu listener, e a tela é sua, lendo os seus dados.

```java
public class MinhaTelaDePedido implements IVisualizarPedidoListener {
    @Override
    public void visualizarPedido(PedidoAbstract pedido) {
        // abrir a SUA tela de pedido
    }
}
```

### 2.4 Implemente `IConfirmarPagamentoListener` (**obrigatório**)

O componente **não** escolhe a forma de pagamento: a ação de um pedido **Novo**
chama o seu listener, e o fluxo é seu. Ao confirmar, registre o pagamento no
próprio pedido.

```java
public class MeuPagamento implements IConfirmarPagamentoListener {
    @Override
    public void confirmarPagamento(PedidoAbstract pedido) {
        // abrir o SEU fluxo de pagamento e, ao confirmar:
        pedido.registrarPagamento("Pix - chave CPF");
    }
}
```

> **Sem os três listeners (visualizar, pagamento e novo pedido), `iniciar()`
> lança `IllegalStateException`.** Eles são a forma de o componente devolver ao
> cliente as telas e ações que são do cliente.

### 2.5 Implemente `ICriarNovoPedidoListener` (**obrigatório**)

O botão **"Novo Pedido"** (à esquerda de "Visualizar pedido") exige este
listener. Ele **devolve** os pedidos a inserir; o componente os salva e atualiza
o painel — o componente **não** cria pedidos.

```java
public class MeuCriadorDePedido implements ICriarNovoPedidoListener {
    @Override
    public List<PedidoAbstract> criarNovoPedido() {
        // você decide quais pedidos e quantos; devolva a lista
        return listaDeNovosPedidos;
    }
}
```

### 2.6 Coloque novos pedidos no painel (empurrão)

Não há simulação nem leitura automática. Quando um novo pedido chega no **seu**
sistema, **você empurra** para o painel — e ele se atualiza sozinho. Aceita
`List<PedidoAbstract>`, inclusive um só pedido:

```java
// um pedido
painelPedido.receberNovosPedidos(List.of(meuPedido));

// ou vários de uma vez
painelPedido.receberNovosPedidos(listaDeNovosPedidos);
```

Ao receber, o componente: salva no seu `IPedidoRepository`, **atualiza o
painel**, seleciona/foca o primeiro card, emite o alerta sonoro, mostra a
mensagem ("Novo Pedido #N" para um, "N novos pedidos" para vários) e destaca os
cards por `NOTIFICACAO_DURACAO_SEGUNDOS`.

### 2.7 (Opcional) Som e relógio

- `IEstrategiaSom` — como tocar o alerta. Default: `SomTomStrategy(frequencia,
  duracaoMs)`. Desligue com `SOM_ATIVO=false` (usa `SomSilenciosoStrategy`).
- `IRelogioService` — de onde vem "agora". Default: `RelogioServiceSistema`.

Ambos têm implementação default; só informe se quiser trocar.

## 3. Configuração `painel_pedidos.env` (**obrigatória**)

No diretório de trabalho da sua aplicação, crie `painel_pedidos.env` (veja
[`painel_pedidos.env.example`](painel_pedidos.env.example)):

```properties
# SLA fixo por estado, em minutos (OBRIGATÓRIO)
SLA_NOVO_MINUTOS=5
SLA_EM_PREPARO_MINUTOS=20
SLA_PRONTO_PARA_ENTREGA_MINUTOS=10
SLA_EM_ROTA_DE_ENTREGA_MINUTOS=10

# Monitor de SLA e notificação
SLA_MONITOR_INTERVALO_SEGUNDOS=30
NOTIFICACAO_DURACAO_SEGUNDOS=10

# Som e título
SOM_ATIVO=true
TITULO_JANELA=Painel de Acompanhamento de Pedidos

# Identidade da aplicação (OBRIGATÓRIO): distingue este painel dos demais
# na mesma máquina; o arquivo de controle da instância única deriva dela
IDENTIFICADOR_APLICACAO=meu-sistema-de-delivery
```

- O **arquivo** é obrigatório: ausente, `LeitorConfiguracaoPainelPedido` lança
  exceção.
- As **quatro chaves de SLA por estado** também são obrigatórias, e a
  **identidade da aplicação** (`IDENTIFICADOR_APLICACAO`) também.
- As demais chaves usam defaults seguros.

Não quer o arquivo no diretório de trabalho? Aponte o diretório no construtor:
`new LeitorConfiguracaoPainelPedido(diretorio, "painel_pedidos.env").ler()`.

### Contrato público

São API apenas estes tipos: `PainelPedidoFacade`, `PedidoAbstract`,
`IPedidoRepository`, `ConfiguracaoPainelPedido`,
`LeitorConfiguracaoPainelPedido`, os três listeners, `IEstrategiaSom`,
`SomTomStrategy`, `IRelogioService`, `RelogioServiceSistema`,
`IPainelPedidoView` e `PainelPedidoView`. Todo o resto vive sob
`br.projeto.painelpedidos.internal.*` e pode mudar entre versões — a lista é
fixada por teste (`ContratoPublicoTest`), que falha nomeando o tipo que entrar
ou sair.

## 4. Executando

### 4.1 Fachada `PainelPedidoFacade`

O componente **não** define Look and Feel (é decisão da sua aplicação). As duas
ações do cliente são obrigatórias.

```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

ConfiguracaoPainelPedido configuracao = new LeitorConfiguracaoPainelPedido(
        ".", "painel_pedidos.env").ler();

PainelPedidoFacade painelPedido = new PainelPedidoFacade(
        meuRepositorio, configuracao, new SomTomStrategy(880, 200), new RelogioServiceSistema());
painelPedido.setVisualizarPedidoListener(minhaTelaDePedido);   // obrigatório
painelPedido.setConfirmarPagamentoListener(meuPagamento);      // obrigatório
painelPedido.setCriarNovoPedidoListener(meuCriadorDePedido);   // obrigatório

if (!painelPedido.iniciar()) {
    System.out.println("O painel ja esta aberto.");
}
```

O construtor da fachada é **único** e exige repositório, configuração, estratégia
de som e relógio (use os defaults `SomTomStrategy` e `RelogioServiceSistema`).

Métodos da fachada: `iniciar()` (devolve `boolean`),
`receberNovosPedidos(List<PedidoAbstract>)`, `encerrar()`,
`setVisualizarPedidoListener(...)`, `setConfirmarPagamentoListener(...)`,
`setCriarNovoPedidoListener(...)`.

### 4.2 Instância única (do componente)

O componente cuida disso sozinho: `iniciar()` adquire uma trava de arquivo antes
de criar a janela e devolve `false` quando já há um painel aberto, **sem abrir
nada**. `encerrar()` libera a trava, então fechar e reabrir volta a funcionar. O
consumidor não configura trava, caminho nem singleton; a mensagem ao usuário é
sua (o componente apenas sinaliza).

### 4.3 Embutir em uma aplicação Swing existente

Use `PainelPedidoView` (`JPanel`, implementa `IPainelPedidoView`) com um
`PainelPedidoPresenter`. A janela é criada pela fachada (`JFrame`).

## 5. Comportamento do painel

### Ciclo de vida (BR12)

`Novo → Em preparo → Pronto para entrega → Em rota de entrega → Entregue`;
qualquer estado não cancelado pode ir para `Cancelado`; `Cancelado` não retorna.
Cada card tem a ação do estado e um botão "Cancelar" (exceto em `Cancelado`).

### Cancelamento e crédito (BR13)

Cancelamento em qualquer pedido não cancelado (inclusive `Entregue`). Sem
pagamento, nenhum crédito; com pagamento, crédito igual ao valor pago. Um pedido
`Cancelado` permite "Criar novo pedido" (novo número, nova data/hora, situação
`Novo`, sem pagamento).

### Prioridade por SLA

Cada estado com SLA tem um limite fixo em minutos (tabela acima). Pedidos que
ultrapassam ganham o rótulo vermelho **"Prioridade"** (removido ao mudar de
estado) e sobem na raia. Entregue e Cancelado não têm SLA.

### Ingestão e notificação

Novos pedidos entram por **empurrão**: o seu sistema chama
`receberNovosPedidos(List<PedidoAbstract>)`. Ao receber, o componente salva no
seu repositório, atualiza o painel, seleciona/foca o primeiro, emite alerta
sonoro, exibe a mensagem por cardinalidade ("Novo Pedido #N" ou "N novos
pedidos") e destaca os cards por `NOTIFICACAO_DURACAO_SEGUNDOS`.

### Janela

O painel **inicia sempre maximizado** (comando da Presenter para a View passiva).
O botão **Fechar** encerra **apenas o painel** e para os monitores internos; o
componente **nunca** encerra a sua aplicação.

## 6. Exemplo completo

```java
public class MeuMain {

    public static void main(String[] argumentos) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        ConfiguracaoPainelPedido configuracao = new LeitorConfiguracaoPainelPedido(
                ".", "painel_pedidos.env").ler();
        IPedidoRepository repositorio = new MeuRepositorioDePedidos();

        PainelPedidoFacade painelPedido = new PainelPedidoFacade(
                repositorio, configuracao, new SomTomStrategy(880, 200), new RelogioServiceSistema());
        painelPedido.setVisualizarPedidoListener(new MinhaTelaDePedido());
        painelPedido.setConfirmarPagamentoListener(new MeuPagamento());
        painelPedido.setCriarNovoPedidoListener(new MeuCriadorDePedido());

        if (!painelPedido.iniciar()) {
            System.out.println("O painel ja esta aberto.");
        }
    }
}
```

Há um consumidor de referência no módulo [`exemplo`](exemplo/) (repositório em
memória, seeder determinístico e `painel_pedidos.env`):

```bash
mvn -U clean verify
java -jar exemplo/target/exemplo-painel-pedidos-1.0.0.jar
```

## 7. Módulos, build e testes

| Módulo | Papel |
|---|---|
| `painel-pedidos-parent` | Agregador Maven |
| `painel-pedidos` | O componente (biblioteca, jar com todas as dependências) |
| `exemplo` | Consumidor de demonstração |

Rode a partir da **raiz** (o `pom.xml` agregador constrói os três módulos):

```bash
mvn -U clean verify
mvn -U clean install   # instala parent + painel-pedidos no repositório local
```

Para construir o módulo `exemplo` **isolado** (ex.: abrindo só ele no NetBeans),
o `painel-pedidos` precisa estar instalado (`mvn -U clean install` na raiz) ou
publicado no JitPack. Mais simples: abra/construa o projeto pela raiz.

A suíte (JUnit Jupiter + Hamcrest) cobre o ciclo de vida, a configuração, a
ingestão (empurrão), a fachada, os listeners e a maximização, além de garantir a
ausência de simulação/dados mockados e da leitura automática de novos pedidos.

## 8. Solução de problemas (Windows)

- **`clean` falha com "Failed to delete ...\target\..."**: algum processo segura
  a pasta. Feche o NetBeans e instâncias do painel em execução, pause o OneDrive
  (se o projeto estiver em `Downloads`) e rode o build a partir da raiz. Também
  pode apagar manualmente: `rmdir /s /q target` (dentro do módulo).
- **`ClassNotFoundException` ao rodar pelo NetBeans**: a configuração de "Run"
  guardou um nome de classe antigo. Rode o arquivo com a classe `main` correta ou
  use `java -jar` (o `Main-Class` do jar está correto).
- **A janela não abre / erro de arena headless em testes**: o painel exige um
  ambiente gráfico para abrir a janela; os testes do componente são headless.

## 9. Documentação

A especificação completa (requisitos, cenários Gherkin, design, regras e
decisões) está em [`openspec/`](openspec/README.md). Um guia mais enxuto, focado
em uso, está em [`painel-pedidos/README.md`](painel-pedidos/README.md).
