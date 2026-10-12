package br.projeto.painelpedidos.internal.state;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class PedidoEntregueState extends PedidoState {

    public PedidoEntregueState(PedidoAbstract pedido) {
        super(pedido);
        if (!pedido.isPagamentoRegistrado()) {
            throw new RuntimeException(
                    "Nao e possivel entregar um pedido sem pagamento registrado; registre o pagamento antes");
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
