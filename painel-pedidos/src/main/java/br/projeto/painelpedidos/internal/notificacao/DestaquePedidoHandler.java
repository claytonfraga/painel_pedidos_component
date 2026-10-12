package br.projeto.painelpedidos.internal.notificacao;

/**
 *
 * @author clayton
 */
public final class DestaquePedidoHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return !contexto.getPedidos().isEmpty();
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().destacarPedidos(contexto.getPedidos());
    }
}
