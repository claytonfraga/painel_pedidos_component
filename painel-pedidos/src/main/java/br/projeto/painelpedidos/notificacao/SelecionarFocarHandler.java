package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public final class SelecionarFocarHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return !contexto.getPedidos().isEmpty();
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().selecionarFocar(contexto.getPedidos().get(0));
    }
}
