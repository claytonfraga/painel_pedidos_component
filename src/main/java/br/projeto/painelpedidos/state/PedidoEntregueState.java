package br.projeto.painelpedidos.state;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class PedidoEntregueState extends PedidoState {

    public PedidoEntregueState(Pedido pedido) {
        super(pedido);
        if (!pedido.isPagamentoRegistrado()) {
            throw new RuntimeException("Nao e possivel entregar um pedido sem pagamento registrado");
        }
    }

    @Override
    public String getNome() {
        return "Entregue";
    }

    @Override
    public boolean ehEntregue() {
        return true;
    }

    @Override
    public void cancelar() {
        pedido.setEstado(new PedidoCanceladoState(pedido));
    }
}
