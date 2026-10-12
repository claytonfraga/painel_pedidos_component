package br.projeto.painelpedidos.view;

/**
 * Espiao de {@link PainelPedidoView} para os testes: registra se a Presenter
 * comandou a maximizacao da View.
 *
 * @author clayton
 */
public final class PainelPedidoViewEspiao extends PainelPedidoView {

    private boolean maximizada;

    @Override
    public void maximizar() {
        this.maximizada = true;
    }

    public boolean isMaximizada() {
        return maximizada;
    }
}
