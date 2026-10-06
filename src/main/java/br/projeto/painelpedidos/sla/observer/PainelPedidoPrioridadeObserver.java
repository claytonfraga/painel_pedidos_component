package br.projeto.painelpedidos.sla.observer;

import br.projeto.painelpedidos.model.Pedido;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class PainelPedidoPrioridadeObserver implements PrioridadeObserver {

    private final IPainelPedidoAtualizador painelPedidoAtualizador;

    public PainelPedidoPrioridadeObserver(IPainelPedidoAtualizador painelPedidoAtualizador) {
        this.painelPedidoAtualizador = Objects.requireNonNull(
                painelPedidoAtualizador, "painelPedidoAtualizador");
    }

    @Override
    public void prioridadesAtualizadas(List<Pedido> pedidos) {
        painelPedidoAtualizador.atualizarPedidos(pedidos);
    }
}
