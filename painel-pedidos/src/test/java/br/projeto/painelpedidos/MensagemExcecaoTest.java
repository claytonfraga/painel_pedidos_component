package br.projeto.painelpedidos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Regra das mensagens de excecao: toda mensagem orienta quem vai corrigir,
 * dizendo o que fazer. Varre o codigo de producao do componente - a verificacao
 * de nulo comeca com "Informe " e nenhuma excecao usa um identificador solto
 * (ex.: "repositorio") como mensagem.
 *
 * @author clayton
 */
public final class MensagemExcecaoTest {

    private static final Pattern VERIFICACAO_DE_NULO =
            Pattern.compile("Objects\\.requireNonNull\\((.*?)\\);", Pattern.DOTALL);
    private static final Pattern MENSAGEM_ORIENTADORA =
            Pattern.compile(",\\s*\"Informe ");
    private static final Pattern MENSAGEM_SEM_ORIENTACAO =
            Pattern.compile("throw new [A-Za-z]+Exception\\(\"[A-Za-z_$][A-Za-z0-9_$]*\"");

    @Test
    public void todaVerificacaoDeNuloOrientaInformandoOQueFalta() throws IOException {
        // given: o codigo de producao do componente
        String codigo = lerCodigoDeProducao();

        // when: procura-se cada verificacao de nulo
        int quantidade = 0;
        Matcher verificacoes = VERIFICACAO_DE_NULO.matcher(codigo);
        while (verificacoes.find()) {
            quantidade++;
            // then: a mensagem comeca com "Informe " e descreve o item que falta
            assertThat("mensagem de nulo sem orientacao: " + verificacoes.group(1),
                    MENSAGEM_ORIENTADORA.matcher(verificacoes.group(1)).find(), is(true));
        }
        assertThat(quantidade, greaterThan(0));
    }

    @Test
    public void nenhumaExcecaoUsaIdentificadorSoltoComoMensagem() throws IOException {
        // given: o codigo de producao do componente
        String codigo = lerCodigoDeProducao();

        // when: procura-se excecoes levantadas com um identificador solto

        // then: nenhuma existe
        assertThat(MENSAGEM_SEM_ORIENTACAO.matcher(codigo).find(), is(false));
    }

    private String lerCodigoDeProducao() throws IOException {
        return new FontesDeProducao().codigo();
    }
}
