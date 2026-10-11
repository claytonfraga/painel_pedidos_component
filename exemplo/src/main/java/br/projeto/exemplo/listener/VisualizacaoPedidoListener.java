package br.projeto.exemplo.listener;

import br.projeto.painelpedidos.listener.IVisualizarPedidoListener;
import br.projeto.painelpedidos.model.PedidoAbstract;
import javax.swing.JOptionPane;

/**
 * Visualizacao de pedido da atividade: exibe a tela do proprio consumidor
 * (aqui, um dialogo simples). Num sistema real, abriria a tela de pedido do
 * cliente.
 *
 * @author clayton
 */
public final class VisualizacaoPedidoListener implements IVisualizarPedidoListener {

    @Override
    public void visualizarPedido(PedidoAbstract pedido) {
        StringBuilder texto = new StringBuilder();
        texto.append("Pedido: ").append(pedido.getIdentificador()).append('\n');
        texto.append("Cliente: ").append(pedido.getNomeCliente()).append('\n');
        texto.append("Endereco: ").append(pedido.getEnderecoEntrega()).append('\n');
        texto.append("Situacao: ").append(pedido.getSituacao()).append('\n');
        texto.append("Valor: ").append(pedido.getValorTotal());

        JOptionPane.showMessageDialog(
                null, texto.toString(), "Pedido (tela do cliente)", JOptionPane.INFORMATION_MESSAGE);
    }
}
