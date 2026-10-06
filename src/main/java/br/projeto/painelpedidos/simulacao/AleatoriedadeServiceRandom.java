package br.projeto.painelpedidos.simulacao;

import java.util.Random;

/**
 *
 * @author clayton
 */
public class AleatoriedadeServiceRandom implements IAleatoriedadeService {

    private final Random random = new Random();

    @Override
    public int sortearInteiro(int origemInclusiva, int limiteExclusivo) {
        if (limiteExclusivo <= origemInclusiva) {
            throw new IllegalArgumentException(
                    "Limite exclusivo deve ser maior que a origem inclusiva");
        }
        return origemInclusiva + random.nextInt(limiteExclusivo - origemInclusiva);
    }
}
