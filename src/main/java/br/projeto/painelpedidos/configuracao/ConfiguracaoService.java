package br.projeto.painelpedidos.configuracao;

/**
 *
 * @author clayton
 */
public class ConfiguracaoService {

    private final double taxaEntregaPadrao;

    public ConfiguracaoService() {
        this.taxaEntregaPadrao = 10.0;
    }

    public double getTaxaEntregaPadrao() {
        return taxaEntregaPadrao;
    }
}
