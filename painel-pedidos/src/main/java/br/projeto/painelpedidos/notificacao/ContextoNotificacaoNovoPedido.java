package br.projeto.painelpedidos.notificacao;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class ContextoNotificacaoNovoPedido {

    private final List<PedidoAbstract> pedidos;
    private final IAcaoNotificacaoNovoPedido acoes;

    public ContextoNotificacaoNovoPedido(List<PedidoAbstract> pedidos, IAcaoNotificacaoNovoPedido acoes) {
        this.pedidos = Objects.requireNonNull(pedidos, "pedidos");
        this.acoes = Objects.requireNonNull(acoes, "acoes");
    }

    public List<PedidoAbstract> getPedidos() {
        return pedidos;
    }

    public IAcaoNotificacaoNovoPedido getAcoes() {
        return acoes;
    }
}
