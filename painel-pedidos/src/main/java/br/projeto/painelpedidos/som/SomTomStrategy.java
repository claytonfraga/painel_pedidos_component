package br.projeto.painelpedidos.som;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author clayton
 */
public final class SomTomStrategy implements IEstrategiaSom {

    private final int frequencia;
    private final int duracaoMilissegundos;

    public SomTomStrategy(int frequencia, int duracaoMilissegundos) {
        if (frequencia <= 0) {
            throw new IllegalArgumentException("Frequencia deve ser positiva");
        }
        if (duracaoMilissegundos <= 0) {
            throw new IllegalArgumentException("Duracao deve ser positiva");
        }
        this.frequencia = frequencia;
        this.duracaoMilissegundos = duracaoMilissegundos;
    }

    @Override
    public void tocar() {
        float taxaAmostragem = 44100f;
        int quantidadeAmostras = (int) (taxaAmostragem * duracaoMilissegundos / 1000);
        byte[] buffer = new byte[quantidadeAmostras * 2];
        for (int i = 0; i < quantidadeAmostras; i++) {
            double angulo = 2.0 * Math.PI * frequencia * i / taxaAmostragem;
            short amostra = (short) (Math.sin(angulo) * Short.MAX_VALUE * 0.6);
            buffer[2 * i] = (byte) (amostra & 0xff);
            buffer[2 * i + 1] = (byte) ((amostra >> 8) & 0xff);
        }

        AudioFormat formato = new AudioFormat(taxaAmostragem, 16, 1, true, false);
        SourceDataLine linha = null;
        try {
            linha = AudioSystem.getSourceDataLine(formato);
            linha.open(formato);
            linha.start();
            linha.write(buffer, 0, buffer.length);
            linha.drain();
        } catch (LineUnavailableException excecao) {
            throw new IllegalStateException("Nao foi possivel reproduzir o som", excecao);
        } finally {
            if (linha != null) {
                linha.stop();
                linha.close();
            }
        }
    }
}
