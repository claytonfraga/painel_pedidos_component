package br.projeto.painelpedidos.ingestao.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author clayton
 */
public final class NovoPedidoObserverFake implements NovoPedidoObserver {

    private final List<List<PedidoAbstract>> lotes;

    public NovoPedidoObserverFake() {
        this.lotes = new ArrayList<>();
    }

    @Override
    public void receberNovosPedidos(List<PedidoAbstract> pedidos) {
        lotes.add(new ArrayList<>(pedidos));
    }

    public int quantidadeChamadas() {
        return lotes.size();
    }

    public List<PedidoAbstract> ultimoLote() {
        return lotes.get(lotes.size() - 1);
    }
}
