package br.projeto.painelpedidos.som;

/**
 * Fake de {@link IEstrategiaSom} para os testes: funciona como Strategy do
 * consumidor, contando quantas vezes o alerta sonoro foi acionado (o audio real
 * nao e reproduzivel em teste).
 *
 * @author clayton
 */
public final class SomFake implements IEstrategiaSom {

    private int quantidadeToques;

    @Override
    public void tocar() {
        this.quantidadeToques++;
    }

    public int getQuantidadeToques() {
        return quantidadeToques;
    }

    public boolean isTocado() {
        return quantidadeToques > 0;
    }
}
