package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public class DestaquePedidosChainOfResponsibility implements RegraChainOfResponsibility {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return !contexto.getPedidos().isEmpty();
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().destacarPedidos(contexto.getPedidos());
    }
}
