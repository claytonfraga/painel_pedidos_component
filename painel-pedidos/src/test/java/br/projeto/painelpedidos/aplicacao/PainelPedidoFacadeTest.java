package br.projeto.painelpedidos.aplicacao;
import br.projeto.painelpedidos.internal.aplicacao.TravaInstanciaUnicaArquivo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido;
import br.projeto.painelpedidos.listener.ConfirmarPagamentoListenerFake;
import br.projeto.painelpedidos.listener.CriarNovoPedidoListenerFake;
import br.projeto.painelpedidos.listener.VisualizarPedidoListenerFake;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import br.projeto.painelpedidos.repositorio.PedidoRepositoryFake;
import br.projeto.painelpedidos.internal.som.SomSilenciosoStrategy;
import br.projeto.painelpedidos.tempo.RelogioServiceSistema;
import br.projeto.painelpedidos.view.PainelPedidoPanel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Fachada (US26/US28/US29): pode ser montada sem janela (headless); o empurrao
 * persiste os pedidos na origem de dados do consumidor; e os listeners
 * obrigatorios sao exigidos ao iniciar.
 *
 * @author clayton
 */
public final class PainelPedidoFacadeTest {

    private static final String IDENTIDADE_DE_TESTE = "painel-de-teste";

    @Test
    public void receberNovosPedidosPersisteNaOrigemDeDadosMasNaoIniciaJanela() {
        // given: uma fachada montada sem janela
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();
        PainelPedidoFacade painelPedido = criarPainel(repositorio);

        // when: o sistema integrado empurra novos pedidos
        List<PedidoAbstract> novos = new ArrayList<>();
        novos.add(criarPedido("1"));
        painelPedido.receberNovosPedidos(novos);

        // then: os pedidos sao persistidos na origem de dados
        assertThat(repositorio.buscarTodos(), hasSize(1));
        assertThat(repositorio.buscarTodos().get(0).getIdentificador(), is("1"));
    }

    @Test
    public void painelIniciaVazioQuandoNaoHaPedidos() {
        // given: uma fachada montada sem pedidos
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();

        // when: nenhum pedido e empurrado
        criarPainel(repositorio);

        // then: a origem de dados permanece vazia
        assertThat(repositorio.buscarTodos(), hasSize(0));
        assertThat(repositorio.buscarTodos(), is(empty()));
    }

    @Test
    public void iniciarSemListenersObrigatoriosLancaExcecao() {
        // given: uma fachada sem os listeners obrigatorios
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();
        PainelPedidoFacade painelPedido = criarPainel(repositorio);

        // when: tenta-se iniciar o painel
        Executable iniciar = new Executable() {
            @Override
            public void execute() {
                painelPedido.iniciar();
            }
        };

        // then: uma excecao e lancada antes de criar a janela
        assertThrows(IllegalStateException.class, iniciar);
    }

    @Test
    public void caminhoDeEmbutirAdquireATravaEEncerrarALibera() {
        // given: uma fachada com os listeners obrigatorios e a trava livre
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();
        PainelPedidoFacade painelPedido = criarPainelComListeners(repositorio);

        // when: inicia-se pelo caminho de embutir e em seguida encerra-se
        boolean iniciado = painelPedido.iniciarEmbutido();
        painelPedido.encerrar();

        // then: a abertura foi concedida e a trava voltou a ficar livre
        assertThat(iniciado, is(true));
        TravaInstanciaUnicaArquivo trava = new TravaInstanciaUnicaArquivo(caminhoTravaPadraoDoComponente());
        assertThat(trava.adquirir(), is(true));
        trava.liberar();
    }

    @Test
    public void caminhoDeEmbutirEntregaOPainelMontadoSemCriarJanela() {
        // given: uma fachada sobre uma origem com um pedido de hoje
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();
        repositorio.salvar(new PedidoDeTeste(
                "1", LocalDateTime.now(), "Ana Souza", "Rua das Flores, 120 - Centro", 42.90));
        PainelPedidoFacade painelPedido = criarPainel(repositorio);

        // when: o consumidor pede o painel para embutir

        // then: o painel vem montado, com o pedido na raia, e sem janela
        PainelPedidoPanel painel = painelPedido.getPainelPedido();
        assertThat(painel, is(notNullValue()));
        assertThat(painel.getPnlPedidosNovo().getComponentCount(), greaterThan(0));
        assertThat(painel.getLblTituloNovo().getText(), is("Novo (1)"));
    }

    @Test
    public void iniciarEmbutidoComATravaRetidaERecusado() {
        // given: a trava da identidade ja retida por outra execucao
        TravaInstanciaUnicaArquivo trava = new TravaInstanciaUnicaArquivo(caminhoTravaPadraoDoComponente());
        assertThat(trava.adquirir(), is(true));
        PainelPedidoFacade painelPedido = criarPainelComListeners(new PedidoRepositoryFake());

        try {
            // when: tenta-se iniciar pelo caminho de embutir
            boolean iniciado = painelPedido.iniciarEmbutido();

            // then: a recusa e sinalizada, sem iniciar nada
            assertThat(iniciado, is(false));
        } finally {
            trava.liberar();
        }
    }

    @Test
    public void repositorioNuloOrientaInformarUmRepositorioDePedidos() {
        // given: os demais colaboradores validos
        ConfiguracaoPainelPedido configuracao = criarConfiguracao();

        // when: constroi-se a fachada sem repositorio
        Executable construir = new Executable() {
            @Override
            public void execute() {
                new PainelPedidoFacade(
                        null, configuracao, new SomSilenciosoStrategy(), new RelogioServiceSistema());
            }
        };

        // then: a mensagem diz o que o consumidor deve informar
        NullPointerException excecao = assertThrows(NullPointerException.class, construir);
        assertThat(excecao.getMessage(), is("Informe um repositorio de pedidos valido"));
    }

    @Test
    public void segundaAberturaComATravaPadraoRetidaERecusadaSemAbrirJanela() {
        // given: a trava padrao do componente ja retida por outra execucao
        TravaInstanciaUnicaArquivo trava = new TravaInstanciaUnicaArquivo(caminhoTravaPadraoDoComponente());
        assertThat(trava.adquirir(), is(true));
        PainelPedidoFacade painelPedido = criarPainelComListeners(new PedidoRepositoryFake());

        try {
            // when: tenta-se abrir o painel
            boolean iniciada = painelPedido.iniciar();

            // then: nada e aberto e a recusa e sinalizada
            assertThat(iniciada, is(false));
        } finally {
            trava.liberar();
        }
    }

    private PedidoAbstract criarPedido(String identificador) {
        return new PedidoDeTeste(identificador, LocalDateTime.of(2026, 1, 10, 12, 0),
                "Ana Souza", "Rua das Flores, 120 - Centro", 42.90);
    }

    private PainelPedidoFacade criarPainel(PedidoRepositoryFake repositorio) {
        return new PainelPedidoFacade(repositorio, criarConfiguracao(),
                new SomSilenciosoStrategy(), new RelogioServiceSistema());
    }

    private PainelPedidoFacade criarPainelComListeners(PedidoRepositoryFake repositorio) {
        PainelPedidoFacade painelPedido = criarPainel(repositorio);
        painelPedido.setVisualizarPedidoListener(new VisualizarPedidoListenerFake());
        painelPedido.setConfirmarPagamentoListener(new ConfirmarPagamentoListenerFake());
        painelPedido.setCriarNovoPedidoListener(new CriarNovoPedidoListenerFake(repositorio, 0));
        return painelPedido;
    }

    private Path caminhoTravaPadraoDoComponente() {
        // espelha o caminho derivado da identidade; se ele mudar, este teste falha.
        return Paths.get(System.getProperty("java.io.tmpdir"), "painel_pedidos-" + IDENTIDADE_DE_TESTE + ".lock");
    }

    private ConfiguracaoPainelPedido criarConfiguracao() {
        return new ConfiguracaoDeTeste().criar(IDENTIDADE_DE_TESTE, false);
    }
}
