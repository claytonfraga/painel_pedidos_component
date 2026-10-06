package br.projeto.painelpedidos.simulacao.observer;

import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.notificacao.ContextoNotificacaoNovoPedido;
import br.projeto.painelpedidos.notificacao.IAcoesNotificacaoNovoPedido;
import br.projeto.painelpedidos.notificacao.IServicoNotificacaoNovoPedido;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class PainelPedidoNovoPedidoObserver implements NovoPedidoObserver {

    private final IAcoesNotificacaoNovoPedido acoesNotificacao;
    private final IServicoNotificacaoNovoPedido servicoNotificacao;

    public PainelPedidoNovoPedidoObserver(
            IAcoesNotificacaoNovoPedido acoesNotificacao,
            IServicoNotificacaoNovoPedido servicoNotificacao) {

        this.acoesNotificacao = Objects.requireNonNull(acoesNotificacao, "acoesNotificacao");
        this.servicoNotificacao = Objects.requireNonNull(servicoNotificacao, "servicoNotificacao");
    }

    @Override
    public void receberNovosPedidos(List<Pedido> pedidos) {
        ContextoNotificacaoNovoPedido contexto = new ContextoNotificacaoNovoPedido(pedidos, acoesNotificacao);
        servicoNotificacao.processar(contexto);
    }
}
