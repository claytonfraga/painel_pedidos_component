package br.projeto.painelpedidos.internal.notificacao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import br.projeto.painelpedidos.som.SomFake;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Cadeia de notificacao de novos pedidos (US26): o contexto carrega apenas os
 * pedidos; cada handler recebe o que precisa pelo construtor (as acoes do painel
 * ou a estrategia de som) e o aciona diretamente (Lei de Demeter). Verifica o
 * comportamento de cada regra e a cadeia inteira.
 *
 * @author clayton
 */
public final class NotificacaoNovoPedidoHandlersTest {

    private static final LocalDateTime REFERENCIA = LocalDateTime.of(2026, 1, 10, 12, 0);

    @Test
    public void contextoCarregaApenasOsPedidosInformados() {
        // given: um lote com um novo pedido
        List<PedidoAbstract> pedidos = umPedido();

        // when: monta-se o contexto
        ContextoNotificacaoNovoPedido contexto = new ContextoNotificacaoNovoPedido(pedidos);

        // then: ele expoe exatamente os pedidos, sem carregar as acoes
        assertThat(contexto.getPedidos(), hasSize(1));
        assertThat(contexto.getPedidos().get(0).getIdentificador(), is("1"));
    }

    @Test
    public void atualizarPainelHandlerAcionaAtualizarPainel() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        AtualizarPainelHandler handler = new AtualizarPainelHandler(acoes);

        // when: a regra e executada
        handler.executar(new ContextoNotificacaoNovoPedido(umPedido()));

        // then: o painel foi atualizado uma vez
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(umPedido())), is(true));
        assertThat(acoes.getAtualizacoes(), is(1));
    }

    @Test
    public void alertaSonoroHandlerAcionaAEstrategiaDeSom() {
        // given: o handler com a estrategia de som injetada
        SomFake som = new SomFake();
        AlertaSonoroHandler handler = new AlertaSonoroHandler(som);

        // when: a regra e executada
        handler.executar(new ContextoNotificacaoNovoPedido(umPedido()));

        // then: o alerta sonoro foi acionado uma vez
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(umPedido())), is(true));
        assertThat(som.getQuantidadeToques(), is(1));
    }

    @Test
    public void salvarPedidoHandlerPersisteOsPedidosDoContexto() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        SalvarPedidoHandler handler = new SalvarPedidoHandler(acoes);
        List<PedidoAbstract> pedidos = umPedido();

        // when: a regra e executada
        handler.executar(new ContextoNotificacaoNovoPedido(pedidos));

        // then: os pedidos do contexto foram salvos
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(pedidos)), is(true));
        assertThat(acoes.getSalvos(), hasSize(1));
        assertThat(acoes.getSalvos().get(0).getIdentificador(), is("1"));
    }

    @Test
    public void selecionarFocarHandlerFocaOPrimeiroPedido() {
        // given: o handler com as acoes injetadas e um lote com dois pedidos
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        SelecionarFocarHandler handler = new SelecionarFocarHandler(acoes);
        List<PedidoAbstract> pedidos = doisPedidos();

        // when: a regra e executada
        handler.executar(new ContextoNotificacaoNovoPedido(pedidos));

        // then: o primeiro pedido do lote foi o focado
        assertThat(acoes.getFocado(), sameInstance(pedidos.get(0)));
    }

    @Test
    public void selecionarFocarHandlerNaoSeAplicaSemPedidos() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        SelecionarFocarHandler handler = new SelecionarFocarHandler(acoes);

        // when/then: sem pedidos a regra nao se aplica
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(new ArrayList<PedidoAbstract>())), is(false));
    }

    @Test
    public void mensagemUmPedidoHandlerSeAplicaApenasComUmPedido() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        MensagemUmPedidoHandler handler = new MensagemUmPedidoHandler(acoes);

        // when/then: so com exatamente um pedido a regra se aplica
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(umPedido())), is(true));
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(doisPedidos())), is(false));
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(new ArrayList<PedidoAbstract>())), is(false));
    }

    @Test
    public void mensagemUmPedidoHandlerExibeOIdentificador() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        MensagemUmPedidoHandler handler = new MensagemUmPedidoHandler(acoes);

        // when: a regra e executada
        handler.executar(new ContextoNotificacaoNovoPedido(umPedido()));

        // then: a mensagem cita o identificador do unico pedido
        assertThat(acoes.getMensagens(), contains("Novo Pedido #1"));
    }

    @Test
    public void mensagemMultiploPedidoHandlerSeAplicaApartirDeDois() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        MensagemMultiploPedidoHandler handler = new MensagemMultiploPedidoHandler(acoes);

        // when/then: so a partir de dois pedidos a regra se aplica
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(doisPedidos())), is(true));
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(umPedido())), is(false));
    }

    @Test
    public void mensagemMultiploPedidoHandlerExibeAQuantidade() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        MensagemMultiploPedidoHandler handler = new MensagemMultiploPedidoHandler(acoes);

        // when: a regra e executada para dois pedidos
        handler.executar(new ContextoNotificacaoNovoPedido(doisPedidos()));

        // then: a mensagem informa a quantidade
        assertThat(acoes.getMensagens(), contains("2 novos pedidos"));
    }

    @Test
    public void destaquePedidoHandlerDestacaOsPedidos() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        DestaquePedidoHandler handler = new DestaquePedidoHandler(acoes);
        List<PedidoAbstract> pedidos = doisPedidos();

        // when: a regra e executada
        handler.executar(new ContextoNotificacaoNovoPedido(pedidos));

        // then: os pedidos do lote foram destacados
        assertThat(acoes.getDestacados(), hasSize(2));
    }

    @Test
    public void destaquePedidoHandlerNaoSeAplicaSemPedidos() {
        // given: o handler com as acoes injetadas
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        DestaquePedidoHandler handler = new DestaquePedidoHandler(acoes);

        // when/then: sem pedidos a regra nao se aplica
        assertThat(handler.seAplica(new ContextoNotificacaoNovoPedido(new ArrayList<PedidoAbstract>())), is(false));
    }

    @Test
    public void handlersDoPainelExigemAcoesNoConstrutor() {
        // given: os handlers que agem pelo painel, sem as acoes (nulo)

        // when/then: cada construcao nula falha orientando o que informar
        afirmarObrigatorio(new Executable() {
            @Override
            public void execute() {
                new AtualizarPainelHandler(null);
            }
        }, "Informe as acoes de notificacao do painel");
        afirmarObrigatorio(new Executable() {
            @Override
            public void execute() {
                new SalvarPedidoHandler(null);
            }
        }, "Informe as acoes de notificacao do painel");
        afirmarObrigatorio(new Executable() {
            @Override
            public void execute() {
                new SelecionarFocarHandler(null);
            }
        }, "Informe as acoes de notificacao do painel");
        afirmarObrigatorio(new Executable() {
            @Override
            public void execute() {
                new MensagemUmPedidoHandler(null);
            }
        }, "Informe as acoes de notificacao do painel");
        afirmarObrigatorio(new Executable() {
            @Override
            public void execute() {
                new MensagemMultiploPedidoHandler(null);
            }
        }, "Informe as acoes de notificacao do painel");
        afirmarObrigatorio(new Executable() {
            @Override
            public void execute() {
                new DestaquePedidoHandler(null);
            }
        }, "Informe as acoes de notificacao do painel");
    }

    @Test
    public void alertaSonoroHandlerExigeEstrategiaNoConstrutor() {
        // given: o handler de som sem a estrategia (nula)

        // when/then: a construcao nula falha orientando informar a estrategia
        afirmarObrigatorio(new Executable() {
            @Override
            public void execute() {
                new AlertaSonoroHandler(null);
            }
        }, "Informe uma estrategia de som valida");
    }

    @Test
    public void cadeiaCompletaAplicaTodasAsRegrasParaUmPedido() {
        // given: a cadeia montada como na fachada (som ligado), com as acoes e o som
        AcoesNotificacaoFake acoes = new AcoesNotificacaoFake();
        SomFake som = new SomFake();
        List<RegraHandler> regras = new ArrayList<>();
        regras.add(new SalvarPedidoHandler(acoes));
        regras.add(new AtualizarPainelHandler(acoes));
        regras.add(new SelecionarFocarHandler(acoes));
        regras.add(new AlertaSonoroHandler(som));
        regras.add(new MensagemUmPedidoHandler(acoes));
        regras.add(new MensagemMultiploPedidoHandler(acoes));
        regras.add(new DestaquePedidoHandler(acoes));
        NotificacaoNovoPedidoService servico = new NotificacaoNovoPedidoService(regras);
        List<PedidoAbstract> pedidos = umPedido();

        // when: um novo pedido atravessa a cadeia
        servico.processar(new ContextoNotificacaoNovoPedido(pedidos));

        // then: salva, atualiza, foca, alerta, exibe e destaca - uma vez cada
        assertThat(acoes.getSalvos(), hasSize(1));
        assertThat(acoes.getAtualizacoes(), is(1));
        assertThat(acoes.getFocado(), sameInstance(pedidos.get(0)));
        assertThat(som.getQuantidadeToques(), is(1));
        assertThat(acoes.getMensagens(), contains("Novo Pedido #1"));
        assertThat(acoes.getDestacados(), hasSize(1));
    }

    private void afirmarObrigatorio(Executable construcao, String mensagemEsperada) {
        // given: uma construcao de handler sem a dependencia obrigatoria
        // when: ela e executada
        // then: falha orientando informar o que falta
        NullPointerException excecao = assertThrows(NullPointerException.class, construcao);
        assertThat(excecao.getMessage(), is(mensagemEsperada));
    }

    private List<PedidoAbstract> umPedido() {
        List<PedidoAbstract> pedidos = new ArrayList<>();
        pedidos.add(criarPedido("1"));
        return pedidos;
    }

    private List<PedidoAbstract> doisPedidos() {
        List<PedidoAbstract> pedidos = new ArrayList<>();
        pedidos.add(criarPedido("1"));
        pedidos.add(criarPedido("2"));
        return pedidos;
    }

    private PedidoAbstract criarPedido(String identificador) {
        return new PedidoDeTeste(
                identificador, REFERENCIA, "Ana Souza", "Rua das Flores, 120 - Centro", 42.90);
    }
}
