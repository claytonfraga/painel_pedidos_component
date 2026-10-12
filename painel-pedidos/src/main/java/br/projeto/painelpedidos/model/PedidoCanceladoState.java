package br.projeto.painelpedidos.model;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
final class PedidoCanceladoState extends PedidoState {

    PedidoCanceladoState(PedidoAbstract pedido) {
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
