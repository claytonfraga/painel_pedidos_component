package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.model.Pedido;
import java.time.Duration;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class RegraSlaNovoHandler implements RegraSlaHandler {

    private final Duration limite;

    public RegraSlaNovoHandler(Duration limite) {
        this.limite = Objects.requireNonNull(limite, "limite");
    }

    @Override
    public boolean seAplica(ContextoSla contexto) {
        for (Pedido pedido : contexto.getPedidos()) {
            if (pedido.estaNovo()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void executar(ContextoSla contexto) {
        for (Pedido pedido : contexto.getPedidos()) {
            if (!pedido.estaNovo() || pedido.isPrioridade()) {
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
