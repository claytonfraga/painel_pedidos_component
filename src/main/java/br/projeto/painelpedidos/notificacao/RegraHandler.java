package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public interface RegraHandler {

    boolean seAplica(ContextoNotificacaoNovoPedido contexto);

    void executar(ContextoNotificacaoNovoPedido contexto);
}
