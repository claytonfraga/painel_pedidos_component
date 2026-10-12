package br.projeto.painelpedidos.model;

import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
final class PedidoEmRotaDeEntregaState extends PedidoState {

    PedidoEmRotaDeEntregaState(PedidoAbstract pedido) {
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
    public void marcarEntregue(LocalDateTime quando) {
        pedido.setEstado(new PedidoEntregueState(pedido), quando);
    }

    @Override
    public void cancelar(LocalDateTime quando) {
        pedido.setEstado(new PedidoCanceladoState(pedido), quando);
    }
}
