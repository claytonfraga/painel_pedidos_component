package br.projeto.painelpedidos.model;

import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
final class PedidoEmPreparoState extends PedidoState {

    PedidoEmPreparoState(PedidoAbstract pedido) {
        super(pedido);
        if (!pedido.isPagamentoRegistrado()) {
            throw new RuntimeException(
                    "Nao e possivel entrar em preparo sem pagamento registrado; registre o pagamento antes");
        }
    }

    @Override
    public String getNome() {
        return "Em preparo";
    }

    @Override
    public boolean ehEmPreparo() {
        return true;
    }

    @Override
    public void marcarProntoParaEntrega(LocalDateTime quando) {
        pedido.setEstado(new PedidoProntoParaEntregaState(pedido), quando);
    }

    @Override
    public void cancelar(LocalDateTime quando) {
        pedido.setEstado(new PedidoCanceladoState(pedido), quando);
    }
}
