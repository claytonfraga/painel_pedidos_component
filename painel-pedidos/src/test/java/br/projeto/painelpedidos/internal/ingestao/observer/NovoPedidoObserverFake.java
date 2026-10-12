package br.projeto.painelpedidos.internal.ingestao.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.ArrayList;
import java.util.List;

/**
 * Fake de {@link NovoPedidoObserver} para os testes: registra os pedidos que
 * recebeu, para assercao de roteamento.
 *
 * @author clayton
 */
public final class NovoPedidoObserverFake implements NovoPedidoObserver {

    private final List<PedidoAbstract> recebidos = new ArrayList<>();

    @Override
    public void receberNovosPedidos(List<PedidoAbstract> pedidos) {
        recebidos.addAll(pedidos);
    }

    public List<PedidoAbstract> getRecebidos() {
        return new ArrayList<>(recebidos);
    }
}
