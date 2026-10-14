package br.projeto.painelpedidos.internal.notificacao;

import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class AtualizarPainelHandler implements RegraHandler {

    private final IAcaoNotificacaoNovoPedido acoes;

    public AtualizarPainelHandler(IAcaoNotificacaoNovoPedido acoes) {
        this.acoes = Objects.requireNonNull(acoes, "Informe as acoes de notificacao do painel");
    }

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return true;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        acoes.atualizarPainel();
    }
}
