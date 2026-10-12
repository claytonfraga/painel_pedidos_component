package br.projeto.painelpedidos.internal.state;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public abstract class PedidoState {

    protected final PedidoAbstract pedido;

    public PedidoState(PedidoAbstract pedido) {
        this.pedido = Objects.requireNonNull(pedido, "Informe um pedido valido");
    }

    public abstract String getNome();

    public boolean ehNovo() {
        return false;
    }

    public boolean ehEmPreparo() {
        return false;
    }

    public boolean ehProntoParaEntrega() {
        return false;
    }

    public boolean ehEmRotaDeEntrega() {
        return false;
    }

    public boolean ehEntregue() {
        return false;
    }

    public boolean ehCancelado() {
        return false;
    }

    public void registrarPagamento(String formaPagamento) {
        throw new RuntimeException("Nao e possivel registrar pagamento de um pedido " + getNome()
                + "; registre o pagamento em um pedido Novo");
    }

    public void marcarProntoParaEntrega() {
        throw new RuntimeException("Nao e possivel marcar como pronto para entrega um pedido " + getNome()
                + "; marque como pronto para entrega apenas um pedido Em preparo");
    }

    public void sairParaEntrega() {
        throw new RuntimeException("Nao e possivel sair para entrega um pedido " + getNome()
                + "; saia para entrega apenas um pedido Pronto para entrega");
    }

    public void marcarEntregue() {
        throw new RuntimeException("Nao e possivel marcar como entregue um pedido " + getNome()
                + "; marque como entregue apenas um pedido Em rota de entrega");
    }

    public void cancelar() {
        throw new RuntimeException("Nao e possivel cancelar um pedido " + getNome()
                + "; o pedido ja esta cancelado e nao volta a nenhuma situacao anterior");
    }
}
