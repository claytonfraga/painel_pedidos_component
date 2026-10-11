package br.projeto.painelpedidos.listener;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class VisualizarPedidoListenerFake implements IVisualizarPedidoListener {

    private PedidoAbstract pedidoRecebido;

    @Override
    public void visualizarPedido(PedidoAbstract pedido) {
        this.pedidoRecebido = pedido;
    }

    public PedidoAbstract getPedidoRecebido() {
        return pedidoRecebido;
    }
}
