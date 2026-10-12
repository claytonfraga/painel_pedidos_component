package br.projeto.painelpedidos.internal.notificacao;

/**
 *
 * @author clayton
 */
public interface RegraHandler {

    boolean seAplica(ContextoNotificacaoNovoPedido contexto);

    void executar(ContextoNotificacaoNovoPedido contexto);
}
