package br.projeto.painelpedidos.internal.notificacao;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class MensagemUmPedidoHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return contexto.getPedidos().size() == 1;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        PedidoAbstract pedido = contexto.getPedidos().get(0);
        contexto.getAcoes().exibirMensagem("Novo Pedido #" + pedido.getIdentificador());
    }
}
