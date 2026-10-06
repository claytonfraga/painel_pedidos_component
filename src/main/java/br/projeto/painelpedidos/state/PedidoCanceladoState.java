package br.projeto.painelpedidos.state;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class PedidoCanceladoState extends PedidoState {

    public PedidoCanceladoState(Pedido pedido) {
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
