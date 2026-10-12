package br.projeto.painelpedidos.model;

import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
final class PedidoEntregueState extends PedidoState {

    PedidoEntregueState(PedidoAbstract pedido) {
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
    public void cancelar(LocalDateTime quando) {
        pedido.setEstado(new PedidoCanceladoState(pedido), quando);
    }
}
