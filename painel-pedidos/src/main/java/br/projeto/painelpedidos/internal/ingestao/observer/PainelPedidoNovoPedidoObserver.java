package br.projeto.painelpedidos.internal.ingestao.observer;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.internal.notificacao.ContextoNotificacaoNovoPedido;
import br.projeto.painelpedidos.internal.notificacao.IServicoNotificacaoNovoPedido;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class PainelPedidoNovoPedidoObserver implements NovoPedidoObserver {

    private final IServicoNotificacaoNovoPedido servicoNotificacao;

    public PainelPedidoNovoPedidoObserver(IServicoNotificacaoNovoPedido servicoNotificacao) {
        this.servicoNotificacao = Objects.requireNonNull(
                servicoNotificacao, "Informe um servico de notificacao valido");
    }

    @Override
    public void receberNovosPedidos(List<PedidoAbstract> pedidos) {
        ContextoNotificacaoNovoPedido contexto = new ContextoNotificacaoNovoPedido(pedidos);
        servicoNotificacao.processar(contexto);
    }
}
