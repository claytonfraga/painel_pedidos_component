package br.projeto.painelpedidos.notificacao;

import br.projeto.painelpedidos.model.Pedido;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface IAcoesNotificacaoNovoPedido {

    void salvarPedidos(List<Pedido> pedidos);

    void atualizarPainel();

    void selecionarFocar(Pedido pedido);

    void emitirAlertaSonoro();

    void exibirMensagem(String mensagem);

    void destacarPedidos(List<Pedido> pedidos);
}
