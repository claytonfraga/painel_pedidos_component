package br.projeto.painelpedidos.internal.notificacao;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class MensagemUmPedidoHandler implements RegraHandler {

    private final IAcaoNotificacaoNovoPedido acoes;

    public MensagemUmPedidoHandler(IAcaoNotificacaoNovoPedido acoes) {
        this.acoes = Objects.requireNonNull(acoes, "Informe as acoes de notificacao do painel");
    }

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return contexto.getPedidos().size() == 1;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        PedidoAbstract pedido = contexto.getPedidos().get(0);
        acoes.exibirMensagem("Novo Pedido #" + pedido.getIdentificador());
    }
}
