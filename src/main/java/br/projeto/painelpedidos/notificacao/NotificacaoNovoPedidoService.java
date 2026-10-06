package br.projeto.painelpedidos.notificacao;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class NotificacaoNovoPedidoService implements IServicoNotificacaoNovoPedido {

    private final List<RegraHandler> regras;

    public NotificacaoNovoPedidoService(List<RegraHandler> regras) {
        this.regras = Objects.requireNonNull(regras, "regras");
    }

    @Override
    public void processar(ContextoNotificacaoNovoPedido contexto) {
        for (RegraHandler regra : regras) {
            if (regra.seAplica(contexto)) {
                regra.executar(contexto);
            }
        }
    }
}
