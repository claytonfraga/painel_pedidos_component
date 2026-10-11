package br.projeto.painelpedidos.sla.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class PainelPedidoPrioridadeObserver implements PrioridadeObserver {

    private final IPainelPedidoAtualizador painelPedidoAtualizador;

    public PainelPedidoPrioridadeObserver(IPainelPedidoAtualizador painelPedidoAtualizador) {
        this.painelPedidoAtualizador = Objects.requireNonNull(
                painelPedidoAtualizador, "painelPedidoAtualizador");
    }

    @Override
    public void prioridadesAtualizadas(List<PedidoAbstract> pedidos) {
        painelPedidoAtualizador.atualizarPedidos(pedidos);
    }
}
