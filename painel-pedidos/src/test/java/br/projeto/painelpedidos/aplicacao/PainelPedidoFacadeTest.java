package br.projeto.painelpedidos.aplicacao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido;
import br.projeto.painelpedidos.listener.ConfirmarPagamentoListenerFake;
import br.projeto.painelpedidos.listener.CriarNovoPedidoListenerFake;
import br.projeto.painelpedidos.listener.VisualizarPedidoListenerFake;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import br.projeto.painelpedidos.repositorio.PedidoRepositoryFake;
import br.projeto.painelpedidos.som.SomSilenciosoStrategy;
import br.projeto.painelpedidos.tempo.RelogioServiceSistema;
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
    public void segundaAberturaComATravaPadraoRetidaERecusadaSemAbrirJanela() {
        // given: a trava padrao do componente ja retida por outra execucao
        ITravaInstanciaUnica trava = new TravaInstanciaUnicaArquivo(caminhoTravaPadraoDoComponente());
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
        // espelha o caminho padrao do componente; se ele mudar, este teste falha.
        return Paths.get(System.getProperty("java.io.tmpdir"), "painel_pedidos.lock");
    }

    private ConfiguracaoPainelPedido criarConfiguracao() {
        Map<String, Duration> slaPorSituacao = new LinkedHashMap<>();
        slaPorSituacao.put("Novo", Duration.ofMinutes(5));
        slaPorSituacao.put("Em preparo", Duration.ofMinutes(20));
        slaPorSituacao.put("Pronto para entrega", Duration.ofMinutes(10));
        slaPorSituacao.put("Em rota de entrega", Duration.ofMinutes(10));
        return new ConfiguracaoPainelPedido(
                slaPorSituacao,
                Duration.ofSeconds(30),
                10,
                false,
                "Teste");
    }
}
