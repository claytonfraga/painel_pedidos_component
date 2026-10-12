package br.projeto.painelpedidos.internal.sla;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Prioridade por SLA (US24): o limite vem de um mapa por situacao, e um SLA novo
 * (nova entrada) passa a valer sem alterar a classe (OCP).
 *
 * @author clayton
 */
public final class RegraSlaEstadoHandlerTest {

    @Test
    public void priorizaPedidoNovoAcimaDoSlaDaSituacao() {
        // given: um pedido Novo e um limite de 5 minutos para a situacao Novo
        PedidoAbstract pedido = new PedidoDeTeste("1", LocalDateTime.now(), "Ana", "Rua X, 1 - Centro", 10.0);
        ContextoSla contexto = criarContexto(pedido);
        RegraSlaEstadoHandler regra = new RegraSlaEstadoHandler(criarSlas("Novo", Duration.ofMinutes(5)));

        // when: a regra e avaliada e executada
        boolean aplica = regra.seAplica(contexto);
        regra.executar(contexto);

        // then: ela se aplica e o pedido e priorizado
        assertThat(aplica, is(true));
        assertThat(pedido.isPrioridade(), is(true));
        assertThat(contexto.getPedidosPriorizados(), hasSize(1));
    }

    @Test
    public void naoAplicaQuandoASituacaoNaoTemSlaConfigurado() {
        // given: um pedido Novo e um limite configurado apenas para Em preparo
        PedidoAbstract pedido = new PedidoDeTeste("1", LocalDateTime.now(), "Ana", "Rua X, 1 - Centro", 10.0);
        ContextoSla contexto = criarContexto(pedido);
        RegraSlaEstadoHandler regra = new RegraSlaEstadoHandler(criarSlas("Em preparo", Duration.ofMinutes(20)));

        // when: a regra e avaliada
        boolean aplica = regra.seAplica(contexto);

        // then: ela nao se aplica, pois a situacao nao tem SLA
        assertThat(aplica, is(false));
    }

    @Test
    public void aceitaSituacaoNovaSemAlterarAClasse() {
        // given: um pedido Entregue e um limite de 1 minuto para a situacao Entregue
        PedidoAbstract pedido = new PedidoDeTeste("1", LocalDateTime.now(), "Ana", "Rua X, 1 - Centro", 10.0);
        pedido.registrarPagamento("Pix - chave CPF");
        pedido.marcarProntoParaEntrega();
        pedido.sairParaEntrega();
        pedido.marcarEntregue();
        ContextoSla contexto = criarContexto(pedido);
        RegraSlaEstadoHandler regra = new RegraSlaEstadoHandler(criarSlas("Entregue", Duration.ofMinutes(1)));

        // when: a regra e avaliada e executada (nova entrada no mapa, sem nova classe)
        boolean aplica = regra.seAplica(contexto);
        regra.executar(contexto);

        // then: o novo SLA passa a valer e o pedido e priorizado
        assertThat(aplica, is(true));
        assertThat(pedido.isPrioridade(), is(true));
    }

    private Map<String, Duration> criarSlas(String situacao, Duration limite) {
        Map<String, Duration> slas = new LinkedHashMap<>();
        slas.put(situacao, limite);
        return slas;
    }

    private ContextoSla criarContexto(PedidoAbstract pedido) {
        List<PedidoAbstract> pedidos = new ArrayList<>();
        pedidos.add(pedido);
        LocalDateTime agora = pedido.getDataHoraMudancaEstado().plusMinutes(10);
        return new ContextoSla(pedidos, agora);
    }
}
