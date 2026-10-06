package br.projeto.painelpedidos.notificacao;

import br.projeto.painelpedidos.model.Pedido;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class ContextoNotificacaoNovoPedido {

    private final List<Pedido> pedidos;
    private final IAcoesNotificacaoNovoPedido acoes;

    public ContextoNotificacaoNovoPedido(List<Pedido> pedidos, IAcoesNotificacaoNovoPedido acoes) {
        this.pedidos = Objects.requireNonNull(pedidos, "pedidos");
        this.acoes = Objects.requireNonNull(acoes, "acoes");
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public IAcoesNotificacaoNovoPedido getAcoes() {
        return acoes;
    }
}
