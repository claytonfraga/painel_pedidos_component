package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.model.Pedido;
import java.time.Duration;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class RegraSlaEmPreparoHandler implements RegraSlaHandler {

    private final Duration limite;

    public RegraSlaEmPreparoHandler(Duration limite) {
        this.limite = Objects.requireNonNull(limite, "limite");
    }

    @Override
    public boolean seAplica(ContextoSla contexto) {
        for (Pedido pedido : contexto.getPedidos()) {
            if (pedido.estaEmPreparo()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void executar(ContextoSla contexto) {
        for (Pedido pedido : contexto.getPedidos()) {
            if (!pedido.estaEmPreparo() || pedido.isPrioridade()) {
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
