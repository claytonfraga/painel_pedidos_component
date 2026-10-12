package br.projeto.painelpedidos.internal.state;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class PedidoCanceladoState extends PedidoState {

    public PedidoCanceladoState(PedidoAbstract pedido) {
        super(pedido);
        pedido.confirmarCancelamento();
    }

    @Override
    public String getNome() {
        return "Cancelado";
    }

    @Override
    public boolean ehCancelado() {
        return true;
    }
}
