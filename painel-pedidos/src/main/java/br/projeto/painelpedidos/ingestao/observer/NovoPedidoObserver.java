package br.projeto.painelpedidos.ingestao.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface NovoPedidoObserver {

    void receberNovosPedidos(List<PedidoAbstract> pedidos);
}
