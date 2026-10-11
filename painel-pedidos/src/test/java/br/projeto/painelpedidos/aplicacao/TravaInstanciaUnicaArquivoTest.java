package br.projeto.painelpedidos.aplicacao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * Trava de instancia unica (US25): a segunda aquisicao sobre o mesmo arquivo e
 * recusada sem excecao; liberar permite readquirir e o arquivo permanece no
 * disco (apaga-lo e o que quebraria a exclusao).
 *
 * @author clayton
 */
public final class TravaInstanciaUnicaArquivoTest {

    @Test
    public void segundaAquisicaoSobreOMesmoArquivoERecusada() {
        // given: uma trava ja adquirida sobre um arquivo
        Path caminho = caminhoDeTeste("segunda");
        TravaInstanciaUnicaArquivo primeira = new TravaInstanciaUnicaArquivo(caminho);
        TravaInstanciaUnicaArquivo segunda = new TravaInstanciaUnicaArquivo(caminho);
        assertThat(primeira.adquirir(), is(true));

        // when: uma segunda trava tenta adquirir o mesmo arquivo
        boolean adquirida = segunda.adquirir();

        // then: a segunda e recusada, sem excecao
        assertThat(adquirida, is(false));

        primeira.liberar();
    }

    @Test
    public void liberarPermiteReadquirirEoArquivoPermanece() throws IOException {
        // given: uma trava adquirida e ja liberada
        Path caminho = caminhoDeTeste("liberar");
        TravaInstanciaUnicaArquivo trava = new TravaInstanciaUnicaArquivo(caminho);
        assertThat(trava.adquirir(), is(true));
        trava.liberar();

        // when: tenta-se adquirir novamente (mesma instancia e arquivo)
        boolean readquirida = trava.adquirir();

        // then: a trava e concedida e o arquivo continua no disco
        assertThat(readquirida, is(true));
        assertThat(Files.exists(caminho), is(true));

        trava.liberar();
    }

    @Test
    public void liberarSemTerAdquiridoNaoAtrapalhaANovaAquisicao() {
        // given: uma trava que nunca foi adquirida
        TravaInstanciaUnicaArquivo trava = new TravaInstanciaUnicaArquivo(caminhoDeTeste("sem-aquisicao"));

        // when: libera-se sem ter adquirido
        trava.liberar();

        // then: o estado permanece consistente e a aquisicao funciona
        assertThat(trava.adquirir(), is(true));

        trava.liberar();
    }

    private Path caminhoDeTeste(String nome) {
        return Paths.get(System.getProperty("java.io.tmpdir"), "painel_pedidos_teste_" + nome + ".lock");
    }
}
