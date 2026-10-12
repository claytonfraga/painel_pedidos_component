package br.projeto.painelpedidos.aplicacao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido;
import br.projeto.painelpedidos.listener.ConfirmarPagamentoListenerFake;
import br.projeto.painelpedidos.listener.CriarNovoPedidoListenerFake;
import br.projeto.painelpedidos.listener.VisualizarPedidoListenerFake;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import br.projeto.painelpedidos.repositorio.PedidoRepositoryFake;
import br.projeto.painelpedidos.som.SomFake;
import br.projeto.painelpedidos.tempo.RelogioServiceSistema;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Integracao (US26): a fachada + repositorio + cadeia de notificacao, sem janela
 * (headless). O empurrao de novos pedidos persiste e dispara a notificacao.
 *
 * @author clayton
 */
public final class PainelPedidoIntegracaoIT {

    @Test
    public void novosPedidosEmpurradosChegamAoRepositorioEAcionamANotificacao() {
        // given: fachada com repositorio, som espiao e os listeners obrigatorios
        PedidoRepositoryFake repositorio = new PedidoRepositoryFake();
        SomFake som = new SomFake();
        PainelPedidoFacade painelPedido = new PainelPedidoFacade(
                repositorio, criarConfiguracao(), som, new RelogioServiceSistema());
        painelPedido.setVisualizarPedidoListener(new VisualizarPedidoListenerFake());
        painelPedido.setConfirmarPagamentoListener(new ConfirmarPagamentoListenerFake());
        painelPedido.setCriarNovoPedidoListener(new CriarNovoPedidoListenerFake(repositorio, 0));

        // when: o sistema integrado empurra um novo pedido
        List<PedidoAbstract> novos = new ArrayList<>();
        novos.add(new PedidoDeTeste("1", LocalDateTime.now(), "Ana", "Rua X, 1 - Centro", 10.0));
        painelPedido.receberNovosPedidos(novos);

        // then: o pedido e persistido e a notificacao passa pela cadeia (som acionado)
        assertThat(repositorio.buscarTodos(), hasSize(1));
        assertThat(som.isTocado(), is(true));
    }

    private ConfiguracaoPainelPedido criarConfiguracao() {
        return new ConfiguracaoDeTeste().criar("painel-de-integracao", true);
    }
}
