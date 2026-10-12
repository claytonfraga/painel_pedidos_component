package br.projeto.painelpedidos.state;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class PedidoEmRotaDeEntregaState extends PedidoState {

    public PedidoEmRotaDeEntregaState(PedidoAbstract pedido) {
        super(pedido);
        if (!pedido.isPagamentoRegistrado()) {
            throw new RuntimeException(
                    "Nao e possivel sair para entrega sem pagamento registrado; registre o pagamento antes");
        }
    }

    @Override
    public String getNome() {
        return "Em rota de entrega";
    }

    @Override
    public boolean ehEmRotaDeEntrega() {
        return true;
    }

    @Override
    public void marcarEntregue() {
        pedido.setEstado(new PedidoEntregueState(pedido));
    }

    @Override
    public void cancelar() {
        pedido.setEstado(new PedidoCanceladoState(pedido));
    }
}
