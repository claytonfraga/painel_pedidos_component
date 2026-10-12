package br.projeto.painelpedidos.internal.sla;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * Regra de SLA generica: aplica o limite fixo (em minutos) da situacao de cada
 * pedido, conforme o mapa configurado.
 *
 * <p>Um SLA novo por estado nao exige nova classe: basta uma entrada no mapa de
 * configuracao, mantendo o principio aberto/fechado (OCP).</p>
 *
 * @author clayton
 */
public final class RegraSlaEstadoHandler implements RegraSlaHandler {

    private final Map<String, Duration> slaPorSituacao;

    public RegraSlaEstadoHandler(Map<String, Duration> slaPorSituacao) {
        this.slaPorSituacao = Objects.requireNonNull(
                slaPorSituacao, "Informe um mapa de SLA por situacao valido");
    }

    @Override
    public boolean seAplica(ContextoSla contexto) {
        for (PedidoAbstract pedido : contexto.getPedidos()) {
            if (slaPorSituacao.containsKey(pedido.getSituacao())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void executar(ContextoSla contexto) {
        for (PedidoAbstract pedido : contexto.getPedidos()) {
            Duration limite = slaPorSituacao.get(pedido.getSituacao());
            if (limite == null || pedido.isPrioridade()) {
                continue;
            }
            Duration decorrido = Duration.between(pedido.getDataHoraMudancaEstado(), contexto.getAgora());
            if (decorrido.compareTo(limite) > 0) {
                pedido.marcarPrioridade();
                contexto.registrarPriorizado(pedido);
            }
        }
    }
}
