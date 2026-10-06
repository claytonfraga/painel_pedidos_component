package br.projeto.painelpedidos.simulacao;

/**
 *
 * @author clayton
 */
public class ConfiguracaoSimulacao {

    private final int intervaloMinimoSegundos;
    private final int intervaloMaximoSegundos;
    private final int duracaoNotificacaoSegundos;
    private final int pedidosPorCiclo;

    public ConfiguracaoSimulacao(
            int intervaloMinimoSegundos,
            int intervaloMaximoSegundos,
            int duracaoNotificacaoSegundos,
            int pedidosPorCiclo) {

        if (intervaloMinimoSegundos < 1) {
            throw new IllegalArgumentException("Intervalo minimo deve ser de pelo menos 1 segundo");
        }
        if (intervaloMaximoSegundos < intervaloMinimoSegundos) {
            throw new IllegalArgumentException("Intervalo maximo deve ser maior ou igual ao minimo");
        }
        if (duracaoNotificacaoSegundos < 1) {
            throw new IllegalArgumentException("Duracao da notificacao deve ser de pelo menos 1 segundo");
        }
        if (pedidosPorCiclo < 1) {
            throw new IllegalArgumentException("Pedidos por ciclo deve ser de pelo menos 1");
        }

        this.intervaloMinimoSegundos = intervaloMinimoSegundos;
        this.intervaloMaximoSegundos = intervaloMaximoSegundos;
        this.duracaoNotificacaoSegundos = duracaoNotificacaoSegundos;
        this.pedidosPorCiclo = pedidosPorCiclo;
    }

    public int getIntervaloMinimoSegundos() {
        return intervaloMinimoSegundos;
    }

    public int getIntervaloMaximoSegundos() {
        return intervaloMaximoSegundos;
    }

    public int getDuracaoNotificacaoSegundos() {
        return duracaoNotificacaoSegundos;
    }

    public int getPedidosPorCiclo() {
        return pedidosPorCiclo;
    }
}
