package br.projeto.painelpedidos.internal.notificacao;

/**
 *
 * @author clayton
 */
public final class MensagemMultiploPedidoHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return contexto.getPedidos().size() > 1;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().exibirMensagem(contexto.getPedidos().size() + " novos pedidos");
    }
}
