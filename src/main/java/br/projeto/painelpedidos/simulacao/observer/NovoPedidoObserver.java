package br.projeto.painelpedidos.simulacao.observer;

import br.projeto.painelpedidos.model.Pedido;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface NovoPedidoObserver {

    void receberNovosPedidos(List<Pedido> pedidos);
}
