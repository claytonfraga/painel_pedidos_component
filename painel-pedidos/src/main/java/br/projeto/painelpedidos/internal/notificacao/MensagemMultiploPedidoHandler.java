package br.projeto.painelpedidos.internal.notificacao;

import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class MensagemMultiploPedidoHandler implements RegraHandler {

    private final IAcaoNotificacaoNovoPedido acoes;

    public MensagemMultiploPedidoHandler(IAcaoNotificacaoNovoPedido acoes) {
        this.acoes = Objects.requireNonNull(acoes, "Informe as acoes de notificacao do painel");
    }

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return contexto.getPedidos().size() > 1;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        acoes.exibirMensagem(contexto.getPedidos().size() + " novos pedidos");
    }
}
