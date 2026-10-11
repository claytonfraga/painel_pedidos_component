package br.projeto.painelpedidos.ingestao.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.notificacao.ContextoNotificacaoNovoPedido;
import br.projeto.painelpedidos.notificacao.IAcaoNotificacaoNovoPedido;
import br.projeto.painelpedidos.notificacao.IServicoNotificacaoNovoPedido;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class PainelPedidoNovoPedidoObserver implements NovoPedidoObserver {

    private final IAcaoNotificacaoNovoPedido acoesNotificacao;
    private final IServicoNotificacaoNovoPedido servicoNotificacao;

    public PainelPedidoNovoPedidoObserver(
            IAcaoNotificacaoNovoPedido acoesNotificacao,
            IServicoNotificacaoNovoPedido servicoNotificacao) {

        this.acoesNotificacao = Objects.requireNonNull(acoesNotificacao, "acoesNotificacao");
        this.servicoNotificacao = Objects.requireNonNull(servicoNotificacao, "servicoNotificacao");
    }

    @Override
    public void receberNovosPedidos(List<PedidoAbstract> pedidos) {
        ContextoNotificacaoNovoPedido contexto = new ContextoNotificacaoNovoPedido(pedidos, acoesNotificacao);
        servicoNotificacao.processar(contexto);
    }
}
