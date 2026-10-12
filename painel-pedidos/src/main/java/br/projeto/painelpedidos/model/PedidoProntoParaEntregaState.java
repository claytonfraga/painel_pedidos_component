package br.projeto.painelpedidos.model;

import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
final class PedidoProntoParaEntregaState extends PedidoState {

    PedidoProntoParaEntregaState(PedidoAbstract pedido) {
        super(pedido);
        if (!pedido.isPagamentoRegistrado()) {
            throw new RuntimeException(
                    "Nao e possivel marcar como pronto para entrega sem pagamento registrado; "
                    + "registre o pagamento antes");
        }
    }

    @Override
    public String getNome() {
        return "Pronto para entrega";
    }

    @Override
    public boolean ehProntoParaEntrega() {
        return true;
    }

    @Override
    public void sairParaEntrega(LocalDateTime quando) {
        pedido.setEstado(new PedidoEmRotaDeEntregaState(pedido), quando);
    }

    @Override
    public void cancelar(LocalDateTime quando) {
        pedido.setEstado(new PedidoCanceladoState(pedido), quando);
    }
}
