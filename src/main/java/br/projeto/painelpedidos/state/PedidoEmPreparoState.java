package br.projeto.painelpedidos.state;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class PedidoEmPreparoState extends PedidoState {

    public PedidoEmPreparoState(Pedido pedido) {
        super(pedido);
        if (!pedido.isPagamentoRegistrado()) {
            throw new RuntimeException("Nao e possivel entrar em preparo sem pagamento registrado");
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
