# painel-pedidos

Componente reutilizável do **Painel de Acompanhamento de Pedidos** para sistemas
de delivery (biblioteca Swing, Java 21). Este README explica como usá-lo a partir
de outra aplicação.

O componente **não** possui pedido concreto, banco de dados, tela de pedido nem
tela de pagamento: tudo isso é do sistema cliente. O componente depende de portas
que você implementa.

## 1. Dependência

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.SEU-USUARIO</groupId>
    <artifactId>painel_pedidos_component</artifactId>
    <version>v1.0.3</version>
</dependency>
```

O jar já contém as dependências (maven-shade). Este projeto é independente: para
uso local, rode `mvn -U clean install` dentro de `painel-pedidos/` e depois
dependa de `br.projeto:painel-pedidos:1.0.0`.

## 2. Estenda `PedidoAbstract`

O componente **não tem classe de pedido concreta**. Você estende
`br.projeto.painelpedidos.model.PedidoAbstract` e implementa `criarCopia`
(usada ao "Criar novo pedido" a partir de um cancelado). `PedidoAbstract` já
traz a máquina de estados e os dados essenciais do painel.

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

## 3. Implemente a porta de dados

```java
public class MeuRepositorio implements IPedidoRepository {

    @Override
    public void salvar(PedidoAbstract pedido) { /* persistir */ }

    @Override
    public List<PedidoAbstract> buscarTodos() { /* listar */ }

    @Override
    public String gerarProximoIdentificador() { /* próximo número */ }
}
```

## 4. Implemente as ações do cliente (obrigatórias)

As ações de **visualizar pedido**, **confirmar pagamento** e **novo pedido** são
listeners obrigatórios: sem eles, `iniciar()` lança `IllegalStateException` e a
ação correspondente também. As telas de pedido e de pagamento são suas; a criação
de novos pedidos também (`ICriarNovoPedidoListener` devolve a lista a inserir).

```java
public class MinhaTelaDePedido implements IVisualizarPedidoListener {
    @Override
    public void visualizarPedido(PedidoAbstract pedido) {
        // abrir a SUA tela de pedido, lendo os SEUS dados
    }
}

public class MeuPagamento implements IConfirmarPagamentoListener {
    @Override
    public void confirmarPagamento(PedidoAbstract pedido) {
        // abrir o SEU fluxo de pagamento e, ao confirmar:
        pedido.registrarPagamento("Pix - chave CPF");
    }
}

public class MeuCriadorDePedido implements ICriarNovoPedidoListener {
    @Override
    public List<PedidoAbstract> criarNovoPedido() {
        // o botão "Novo Pedido" chama isto; devolva os pedidos a inserir
        return listaDeNovosPedidos;
    }
}
```

## 5. Coloque novos pedidos no painel (empurrão)

Não há simulação nem leitura automática. Quando um novo pedido chega no seu
sistema, **empurre** para o painel — e ele se atualiza sozinho. Aceita
`List<PedidoAbstract>`, inclusive um só pedido:

```java
painelPedido.receberNovosPedidos(List.of(meuPedido));
painelPedido.receberNovosPedidos(listaDeNovosPedidos);
```

Ao receber, o componente salva no seu repositório, atualiza o painel,
seleciona/foca o primeiro, emite alerta sonoro, mostra a mensagem por
cardinalidade e destaca os cards.

## 6. Configure `painel_pedidos.env`

No diretório de trabalho da sua aplicação (veja
[`../painel_pedidos.env.example`](../painel_pedidos.env.example)):

```properties
SLA_NOVO_MINUTOS=5
SLA_EM_PREPARO_MINUTOS=20
SLA_PRONTO_PARA_ENTREGA_MINUTOS=10
SLA_EM_ROTA_DE_ENTREGA_MINUTOS=10
SLA_MONITOR_INTERVALO_SEGUNDOS=30
NOTIFICACAO_DURACAO_SEGUNDOS=10
SOM_ATIVO=true
TITULO_JANELA=Painel de Acompanhamento de Pedidos
IDENTIFICADOR_APLICACAO=meu-sistema-de-delivery
```

O arquivo é **obrigatório**: sem ele, o componente lança exceção. As quatro
chaves de SLA por estado (`SLA_NOVO_MINUTOS`, `SLA_EM_PREPARO_MINUTOS`,
`SLA_PRONTO_PARA_ENTREGA_MINUTOS`, `SLA_EM_ROTA_DE_ENTREGA_MINUTOS`) também são
obrigatórias — o SLA é fixo por estado, em minutos, sem média por bairro. A
identidade da aplicação (`IDENTIFICADOR_APLICACAO`) também é obrigatória:
distingue este painel dos demais na mesma máquina, e o arquivo de controle da
instância única deriva dela. As demais chaves usam defaults seguros.

## 7. Inicie o painel

O Look and Feel **não** é definido pelo componente: defina-o na sua aplicação
antes de iniciar (ex.: Look and Feel do sistema/Windows).

```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

ConfiguracaoPainelPedido configuracao = new LeitorConfiguracaoPainelPedido(
        ".", "painel_pedidos.env").ler();

PainelPedidoFacade painelPedido = new PainelPedidoFacade(
        meuRepositorio, configuracao, new SomTomStrategy(880, 200), new RelogioServiceSistema());
painelPedido.setVisualizarPedidoListener(new MinhaTelaDePedido());
painelPedido.setConfirmarPagamentoListener(new MeuPagamento());
painelPedido.setCriarNovoPedidoListener(new MeuCriadorDePedido());
painelPedido.iniciar();
```

`iniciar()` devolve `boolean`: devolve `false` quando já existe um painel aberto
(instância única é responsabilidade do componente).

O botão **Fechar** encerra apenas a janela do painel; o componente não encerra a
sua aplicação.

### Embutir em uma aplicação Swing existente

Use `PainelPedidoView` (`JPanel`) com um `PainelPedidoPresenter`; a janela é
criada pela fachada (`JFrame`).

### Instância única (do componente)

O componente cuida disso sozinho: `iniciar()` adquire uma trava de arquivo antes
de criar a janela e devolve `false` quando já há um painel aberto, sem abrir nada;
`encerrar()` libera a trava. Não há trava, caminho nem singleton para o consumidor
configurar.

## Convenções

- **Contrato público:** só estes tipos são API —
  `aplicacao.PainelPedidoFacade`, `model.PedidoAbstract`,
  `repositorio.IPedidoRepository`, `configuracao.ConfiguracaoPainelPedido`,
  `configuracao.LeitorConfiguracaoPainelPedido`,
  `listener.IVisualizarPedidoListener`, `listener.IConfirmarPagamentoListener`,
  `listener.ICriarNovoPedidoListener`, `som.IEstrategiaSom`,
  `som.SomTomStrategy`, `tempo.IRelogioService`, `tempo.RelogioServiceSistema`,
  `view.IPainelPedidoView`, `view.PainelPedidoView`. Todo o resto vive sob
  `br.projeto.painelpedidos.internal.*` e pode mudar entre versões.
- Para embutir o painel, use `PainelPedidoFacade.getPainelPedido()` e
  `iniciarEmbutido()` — montar `PainelPedidoPresenter` à mão não é suportado.
- Nomes de classe, classe abstrata e interface no **singular**; classes de
  serviço com sufixo `Service` (ex.: `AgendadorMonitorSlaService`).
- `PedidoAbstract` **não** define `toString`.
- Look and Feel é definido pela aplicação cliente (o componente não o define).
- O botão **Fechar** encerra apenas o painel, nunca a aplicação.
- O painel inicia sempre **maximizado** (comando da Presenter para a View
  passiva).
- O arquivo `painel_pedidos.env` é **obrigatório**.

## Mapa do componente

| Porta / tipo | Papel |
|---|---|
| `PedidoAbstract` | Modelo abstrato (você estende) + GoF State |
| `IPedidoRepository` | Origem de dados (você implementa) |
| `IVisualizarPedidoListener` | Tela de pedido do cliente (obrigatório) |
| `IConfirmarPagamentoListener` | Fluxo de pagamento do cliente (obrigatório) |
| `ICriarNovoPedidoListener` | Criação de novos pedidos pelo cliente (obrigatório) |
| `IEstrategiaSom` / `IRelogioService` | Comportamento com implementação default |
| `PainelPedidoFacade` | Fachada: compõe, valida e inicia |
| `PainelPedidoView` | View: `JPanel` embutível (par `.java` + `.form`) |

O guia de uso do repositório está em [`../README.md`](../README.md).
