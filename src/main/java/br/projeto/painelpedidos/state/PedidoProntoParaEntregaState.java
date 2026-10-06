package br.projeto.painelpedidos.state;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class PedidoProntoParaEntregaState extends PedidoState {

    public PedidoProntoParaEntregaState(Pedido pedido) {
        super(pedido);
        if (!pedido.isPagamentoRegistrado()) {
            throw new RuntimeException("Nao e possivel marcar como pronto para entrega sem pagamento registrado");
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
