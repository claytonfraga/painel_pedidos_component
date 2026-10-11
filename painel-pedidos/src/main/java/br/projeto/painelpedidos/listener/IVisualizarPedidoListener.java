package br.projeto.painelpedidos.listener;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 * Listener obrigatorio da aplicacao cliente para visualizar um pedido. O
 * componente nao abre a janela de detalhes: o cliente exibe a sua propria tela
 * do pedido, lendo os dados do seu proprio sistema.
 *
 * @author clayton
 */
public interface IVisualizarPedidoListener {

    void visualizarPedido(PedidoAbstract pedido);
}
