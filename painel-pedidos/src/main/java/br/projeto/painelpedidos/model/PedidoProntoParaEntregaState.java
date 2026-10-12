package br.projeto.painelpedidos.model;

import br.projeto.painelpedidos.model.PedidoAbstract;

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
    public void sairParaEntrega() {
        pedido.setEstado(new PedidoEmRotaDeEntregaState(pedido));
    }

    @Override
    public void cancelar() {
        pedido.setEstado(new PedidoCanceladoState(pedido));
    }
}
