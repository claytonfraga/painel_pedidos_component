package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public final class SalvarPedidoHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return true;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().salvarPedidos(contexto.getPedidos());
    }
}
