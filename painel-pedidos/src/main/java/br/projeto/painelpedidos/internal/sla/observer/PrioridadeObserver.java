package br.projeto.painelpedidos.internal.sla.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface PrioridadeObserver {

    void prioridadesAtualizadas(List<PedidoAbstract> pedidos);
}
