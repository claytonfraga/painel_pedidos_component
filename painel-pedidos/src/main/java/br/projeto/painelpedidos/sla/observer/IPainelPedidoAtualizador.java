package br.projeto.painelpedidos.sla.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface IPainelPedidoAtualizador {

    void atualizarPedidos(List<PedidoAbstract> pedidos);
}
