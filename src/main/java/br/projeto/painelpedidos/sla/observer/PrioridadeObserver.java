package br.projeto.painelpedidos.sla.observer;

import br.projeto.painelpedidos.model.Pedido;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface PrioridadeObserver {

    void prioridadesAtualizadas(List<Pedido> pedidos);
}
