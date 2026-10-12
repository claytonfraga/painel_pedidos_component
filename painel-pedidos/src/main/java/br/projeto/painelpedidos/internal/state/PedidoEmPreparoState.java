package br.projeto.painelpedidos.internal.state;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class PedidoEmPreparoState extends PedidoState {

    public PedidoEmPreparoState(PedidoAbstract pedido) {
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
    public void marcarProntoParaEntrega() {
        pedido.setEstado(new PedidoProntoParaEntregaState(pedido));
    }

    @Override
    public void cancelar() {
        pedido.setEstado(new PedidoCanceladoState(pedido));
    }
}
