package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public interface RegraChainOfResponsibility {

    boolean seAplica(ContextoNotificacaoNovoPedido contexto);

    void executar(ContextoNotificacaoNovoPedido contexto);
}
