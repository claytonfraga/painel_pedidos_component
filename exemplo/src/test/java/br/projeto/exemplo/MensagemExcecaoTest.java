package br.projeto.exemplo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Regra das mensagens de excecao no modulo consumidor: toda mensagem orienta
 * quem vai corrigir. Varre o codigo de producao do exemplo - a verificacao de
 * nulo comeca com "Informe " e nenhuma excecao usa um identificador solto.
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
        // given: o codigo de producao do exemplo
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
        // given: o codigo de producao do exemplo
        String codigo = lerCodigoDeProducao();

        // when: procura-se excecoes levantadas com um identificador solto

        // then: nenhuma existe
        assertThat(MENSAGEM_SEM_ORIENTACAO.matcher(codigo).find(), is(false));
    }

    private String lerCodigoDeProducao() throws IOException {
        List<Path> arquivos = new ArrayList<>();
        coletarArquivosJava(Paths.get("src/main/java"), arquivos);
        StringBuilder codigo = new StringBuilder();
        for (Path arquivo : arquivos) {
            codigo.append(Files.readString(arquivo, StandardCharsets.UTF_8)).append('\n');
        }
        return codigo.toString();
    }

    private void coletarArquivosJava(Path diretorio, List<Path> destino) throws IOException {
        try (DirectoryStream<Path> entradas = Files.newDirectoryStream(diretorio)) {
            for (Path entrada : entradas) {
                if (Files.isDirectory(entrada)) {
                    coletarArquivosJava(entrada, destino);
                    continue;
                }
                if (entrada.toString().endsWith(".java")) {
                    destino.add(entrada);
                }
            }
        }
    }
}
