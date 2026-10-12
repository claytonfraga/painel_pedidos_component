package br.projeto.painelpedidos.internal.notificacao;

/**
 *
 * @author clayton
 */
public final class AlertaSonoroHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return true;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().emitirAlertaSonoro();
    }
}
