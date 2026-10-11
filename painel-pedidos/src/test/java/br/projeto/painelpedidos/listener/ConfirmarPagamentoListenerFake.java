package br.projeto.painelpedidos.listener;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class ConfirmarPagamentoListenerFake implements IConfirmarPagamentoListener {

    private PedidoAbstract pedidoRecebido;

    @Override
    public void confirmarPagamento(PedidoAbstract pedido) {
        this.pedidoRecebido = pedido;
        pedido.registrarPagamento("Pix - chave CPF");
    }

    public PedidoAbstract getPedidoRecebido() {
        return pedidoRecebido;
    }
}
