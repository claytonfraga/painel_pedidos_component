package br.projeto.painelpedidos.model;

import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
final class PedidoNovoState extends PedidoState {

    PedidoNovoState(PedidoAbstract pedido) {
        super(pedido);
    }

    @Override
    public String getNome() {
        return "Novo";
    }

    @Override
    public boolean ehNovo() {
        return true;
    }

    @Override
    public void registrarPagamento(String formaPagamento, LocalDateTime quando) {
        pedido.aplicarPagamento(formaPagamento, quando);
        pedido.setEstado(new PedidoEmPreparoState(pedido), quando);
    }

    @Override
    public void cancelar(LocalDateTime quando) {
        pedido.setEstado(new PedidoCanceladoState(pedido), quando);
    }
}
