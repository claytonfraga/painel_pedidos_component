package br.projeto.painelpedidos.internal.notificacao;

import br.projeto.painelpedidos.som.IEstrategiaSom;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class AlertaSonoroHandler implements RegraHandler {

    private final IEstrategiaSom estrategiaSom;

    public AlertaSonoroHandler(IEstrategiaSom estrategiaSom) {
        this.estrategiaSom = Objects.requireNonNull(
                estrategiaSom, "Informe uma estrategia de som valida");
    }

    @Override
    public boolean seAplica(ContextoNotificacaoNovoPedido contexto) {
        return true;
    }

    @Override
    public void executar(ContextoNotificacaoNovoPedido contexto) {
        estrategiaSom.tocar();
    }
}
