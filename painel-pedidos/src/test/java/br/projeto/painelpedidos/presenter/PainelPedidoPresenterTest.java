package br.projeto.painelpedidos.presenter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.projeto.painelpedidos.filtro.FabricaFiltroPeriodo;
import br.projeto.painelpedidos.listener.ConfirmarPagamentoListenerFake;
import br.projeto.painelpedidos.listener.CriarNovoPedidoListenerFake;
import br.projeto.painelpedidos.listener.VisualizarPedidoListenerFake;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import br.projeto.painelpedidos.ordenacao.OrdenacaoPedidoPorPrioridadeETempoNoEstado;
import br.projeto.painelpedidos.repositorio.PedidoRepositoryFake;
import br.projeto.painelpedidos.som.SomSilenciosoStrategy;
import br.projeto.painelpedidos.tempo.FonteDataPedido;
import br.projeto.painelpedidos.tempo.RelogioServiceSistema;
import br.projeto.painelpedidos.view.IPainelPedidoView;
import br.projeto.painelpedidos.view.PainelPedidoPanel;
import br.projeto.painelpedidos.view.PainelPedidoPanelEspiao;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Acoes do cliente na Presenter (US28/US29): visualizar, confirmar pagamento e
 * criar novo pedido sao listeners obrigatorios; a maximizacao e comandada pela
 * Presenter.
 *
 * @author clayton
 */
public final class PainelPedidoPresenterTest {

    private static final LocalDateTime REFERENCIA = LocalDateTime.of(2026, 1, 10, 12, 0);

    @Test
    public void visualizarPedidoSemListenerLancaExcecao() {
        // given: uma Presenter sem o listener de visualizacao
        PainelPedidoPresenter presenter = criarPresenter(new PedidoRepositoryFake());
        PedidoAbstract pedido = criarPedido();

        // when: aciona-se a visualizacao
        Executable visualizar = new Executable() {
            @Override
            public void execute() {
                presenter.visualizarPedido(pedido);
            }
        };

        // then: uma excecao e lancada
        assertThrows(IllegalStateException.class, visualizar);
    }

    @Test
    public void visualizarPedidoComListenerAcionaOCliente() {
        // given: uma Presenter com o listener de visualizacao
        PainelPedidoPresenter presenter = criarPresenter(new PedidoRepositoryFake());
        VisualizarPedidoListenerFake listener = new VisualizarPedidoListenerFake();
        presenter.setVisualizarPedidoListener(listener);
        PedidoAbstract pedido = criarPedido();

        // when: aciona-se a visualizacao
        presenter.visualizarPedido(pedido);

        // then: o cliente recebe exatamente o pedido selecionado
        assertThat(listener.getPedidoRecebido(), sameInstance(pedido));
    }

    @Test
    public void confirmarPagamentoSemListenerLancaExcecao() {
        // given: uma Presenter sem o listener de pagamento
        PainelPedidoPresenter presenter = criarPresenter(new PedidoRepositoryFake());
        PedidoAbstract pedido = criarPedido();

        // when: aciona-se o pagamento
        Executable confirmar = new Executable() {
            @Override
            public void execute() {
                presenter.confirmarPagamento(pedido);
            }
        };

        // then: uma excecao e lancada
        assertThrows(IllegalStateException.class, confirmar);
    }

    @Test
    public void confirmarPagamentoComListenerRegistraEPersiste() {
        // given: uma Presenter com o listener de pagamento
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();
        PainelPedidoPresenter presenter = criarPresenter(repositorio);
        ConfirmarPagamentoListenerFake listener = new ConfirmarPagamentoListenerFake();
        presenter.setConfirmarPagamentoListener(listener);
        PedidoAbstract pedido = criarPedido();

        // when: aciona-se o pagamento
        presenter.confirmarPagamento(pedido);

        // then: o cliente recebe o pedido, ele vai para Em preparo e e persistido
        assertThat(listener.getPedidoRecebido(), sameInstance(pedido));
        assertThat(pedido.estaEmPreparo(), is(true));
        assertThat(repositorio.buscarTodos(), hasSize(1));
    }

    @Test
    public void criarNovoPedidoSemListenerLancaExcecao() {
        // given: uma Presenter sem o listener de novo pedido
        PainelPedidoPresenter presenter = criarPresenter(new PedidoRepositoryFake());

        // when: aciona-se "Novo Pedido"
        Executable criar = new Executable() {
            @Override
            public void execute() {
                presenter.criarNovoPedido();
            }
        };

        // then: uma excecao e lancada
        assertThrows(IllegalStateException.class, criar);
    }

    @Test
    public void criarNovoPedidoComListenerPersisteOsPedidos() {
        // given: uma origem de dados com um pedido e um listener funcional que copia 1
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();
        repositorio.salvar(criarPedido(repositorio.gerarProximoIdentificador()));
        PainelPedidoPresenter presenter = criarPresenter(repositorio);
        presenter.setCriarNovoPedidoListener(new CriarNovoPedidoListenerFake(repositorio, 1));

        // when: aciona-se "Novo Pedido"
        presenter.criarNovoPedido();

        // then: a origem de dados passa a ter o original + a copia
        assertThat(repositorio.buscarTodos(), hasSize(2));
    }

    @Test
    public void iniciarComandaAMaximizacaoDaView() {
        // given: uma Presenter ligada a uma View espia
        PainelPedidoPanelEspiao painel = new PainelPedidoPanelEspiao();
        PainelPedidoPresenter presenter = criarPresenter(painel, new PedidoRepositoryFake());

        // when: a Presenter inicia
        presenter.iniciar();

        // then: a Presenter comandou a maximizacao da View
        assertThat(painel.isMaximizada(), is(true));
    }

    private PainelPedidoPresenter criarPresenter(PedidoRepositoryFake repositorio) {
        return criarPresenter(new PainelPedidoPanel(), repositorio);
    }

    private PainelPedidoPresenter criarPresenter(IPainelPedidoView painel, PedidoRepositoryFake repositorio) {
        return new PainelPedidoPresenter(
                painel,
                repositorio,
                new FonteDataPedido(new RelogioServiceSistema()),
                new FabricaFiltroPeriodo(),
                new SomSilenciosoStrategy(),
                new OrdenacaoPedidoPorPrioridadeETempoNoEstado(),
                10);
    }

    private PedidoAbstract criarPedido() {
        return criarPedido("1");
    }

    private PedidoAbstract criarPedido(String identificador) {
        return new PedidoDeTeste(identificador, REFERENCIA, "Ana Souza", "Rua das Flores, 120 - Centro", 42.90);
    }
}
