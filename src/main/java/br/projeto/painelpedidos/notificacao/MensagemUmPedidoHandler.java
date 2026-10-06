package br.projeto.painelpedidos.notificacao;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class MensagemUmPedidoHandler implements RegraHandler {

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return contexto.getPedidos().size() == 1;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        Pedido pedido = contexto.getPedidos().get(0);
        contexto.getAcoes().exibirMensagem("Novo Pedido #" + pedido.getIdentificador());
    }
}
