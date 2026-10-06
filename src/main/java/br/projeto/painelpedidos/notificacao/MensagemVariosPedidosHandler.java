package br.projeto.painelpedidos.notificacao;

/**
 *
 * @author clayton
 */
public class MensagemVariosPedidosHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return contexto.getPedidos().size() > 1;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        contexto.getAcoes().exibirMensagem(contexto.getPedidos().size() + " novos pedidos");
    }
}
