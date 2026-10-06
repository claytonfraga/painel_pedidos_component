package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public class SalvarPedidosChainOfResponsibility implements RegraChainOfResponsibility {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return true;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().salvarPedidos(contexto.getPedidos());
    }
}
