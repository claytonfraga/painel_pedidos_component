package br.projeto.exemplo.listener;

import br.projeto.painelpedidos.listener.IConfirmarPagamentoListener;
import br.projeto.painelpedidos.model.PedidoAbstract;
import javax.swing.JOptionPane;

/**
 * Confirmacao de pagamento da atividade: conduz o fluxo de pagamento do proprio
 * consumidor (aqui, um dialogo simples) e registra o pagamento no pedido. Num
 * sistema real, abriria a tela de pagamento do cliente.
 *
 * @author clayton
 */
public final class ConfirmacaoPagamentoListener implements IConfirmarPagamentoListener {

    @Override
    public void confirmarPagamento(PedidoAbstract pedido) {
        Object[] formasPagamento = {
            "Pix - chave CPF",
            "Pix - chave e-mail",
            "Cartão de crédito"
        };

        Object formaPagamentoSelecionada = JOptionPane.showInputDialog(
                null,
                "Selecione a forma de pagamento",
                "Registrar pagamento (tela do cliente)",
                JOptionPane.PLAIN_MESSAGE,
                null,
                formasPagamento,
                formasPagamento[0]);

        if (formaPagamentoSelecionada == null) {
            return;
        }

        pedido.registrarPagamento(formaPagamentoSelecionada.toString());
    }
}
