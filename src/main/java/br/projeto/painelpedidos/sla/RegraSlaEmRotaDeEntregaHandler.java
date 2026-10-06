package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.model.Pedido;
import java.time.Duration;

/**
 *
 * @author clayton
 */
public class RegraSlaEmRotaDeEntregaHandler implements RegraSlaHandler {

    @Override
    public boolean seAplica(ContextoSla contexto) {
        for (Pedido pedido : contexto.getPedidos()) {
            if (pedido.estaEmRotaDeEntrega()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void executar(ContextoSla contexto) {
        for (Pedido pedido : contexto.getPedidos()) {
            if (!pedido.estaEmRotaDeEntrega() || pedido.isPrioridade()) {
                continue;
            }
            Duration limite = contexto.getTempoEntregaService()
                    .tempoMedioEntrega(pedido.getBairroEntrega());
            Duration decorrido = Duration.between(pedido.getDataHoraMudancaEstado(), contexto.getAgora());
            if (decorrido.compareTo(limite) > 0) {
                pedido.marcarPrioridade();
                contexto.registrarPriorizado(pedido);
            }
        }
    }
}
