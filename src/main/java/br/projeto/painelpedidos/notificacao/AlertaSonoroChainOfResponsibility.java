package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public class AlertaSonoroChainOfResponsibility implements RegraChainOfResponsibility {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return true;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().emitirAlertaSonoro();
    }
}
