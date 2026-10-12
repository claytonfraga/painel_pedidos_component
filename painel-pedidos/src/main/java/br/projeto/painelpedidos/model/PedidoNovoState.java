package br.projeto.painelpedidos.model;

import br.projeto.painelpedidos.model.PedidoAbstract;

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
    public void registrarPagamento(String formaPagamento) {
        pedido.aplicarPagamento(formaPagamento);
        pedido.setEstado(new PedidoEmPreparoState(pedido));
    }

    @Override
    public void cancelar() {
        pedido.setEstado(new PedidoCanceladoState(pedido));
    }
}
