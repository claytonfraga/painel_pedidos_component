package br.projeto.painelpedidos.simulacao;

import br.projeto.painelpedidos.model.Pedido;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface ObservadorNovoPedido {

    void receberNovosPedidos(List<Pedido> pedidos);
}
