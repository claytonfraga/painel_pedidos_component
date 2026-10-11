package br.projeto.painelpedidos.listener;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 * Listener obrigatorio da aplicacao cliente para confirmar o pagamento de um
 * pedido. O componente nao abre a escolha de forma de pagamento: o cliente
 * conduz o seu proprio fluxo e registra o pagamento no pedido.
 *
 * @author clayton
 */
public interface IConfirmarPagamentoListener {

    void confirmarPagamento(PedidoAbstract pedido);
}
