package br.projeto.painelpedidos.internal.notificacao;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class ContextoNotificacaoNovoPedido {

    private final List<PedidoAbstract> pedidos;

    public ContextoNotificacaoNovoPedido(List<PedidoAbstract> pedidos) {
        this.pedidos = Objects.requireNonNull(pedidos, "Informe uma lista de pedidos valida");
    }

    public List<PedidoAbstract> getPedidos() {
        return pedidos;
    }
}
