package br.projeto.painelpedidos.listener;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;

/**
 * Listener obrigatorio da aplicacao cliente para criar novos pedidos a partir do
 * painel (botao "Novo Pedido"). O componente nao cria pedidos: o cliente devolve
 * a lista de pedidos a inserir e o componente os salva e atualiza o painel.
 *
 * @author clayton
 */
public interface ICriarNovoPedidoListener {

    List<PedidoAbstract> criarNovoPedido();
}
