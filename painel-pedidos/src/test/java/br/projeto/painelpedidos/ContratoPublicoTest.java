package br.projeto.painelpedidos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Contrato publico do componente: os tipos publicos fora dos pacotes `internal`
 * sao exatamente a API anunciada. Um tipo que entre ou saia da lista aparece na
 * mensagem da falha, que e o que impede a fronteira de crescer em silencio.
 *
 * @author clayton
 */
public final class ContratoPublicoTest {

    private static final String PACOTE_INTERNO = ".internal.";

    private static final List<String> CONTRATO = Arrays.asList(
            "br.projeto.painelpedidos.aplicacao.PainelPedidoFacade",
            "br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido",
            "br.projeto.painelpedidos.configuracao.LeitorConfiguracaoPainelPedido",
            "br.projeto.painelpedidos.listener.IConfirmarPagamentoListener",
            "br.projeto.painelpedidos.listener.ICriarNovoPedidoListener",
            "br.projeto.painelpedidos.listener.IVisualizarPedidoListener",
            "br.projeto.painelpedidos.model.PedidoAbstract",
            "br.projeto.painelpedidos.repositorio.IPedidoRepository",
            "br.projeto.painelpedidos.som.IEstrategiaSom",
            "br.projeto.painelpedidos.som.SomTomStrategy",
            "br.projeto.painelpedidos.tempo.IRelogioService",
            "br.projeto.painelpedidos.tempo.RelogioServiceSistema",
            "br.projeto.painelpedidos.view.IPainelPedidoView",
            "br.projeto.painelpedidos.view.PainelPedidoPanel");

    @Test
    public void oContratoPublicoForaDosPacotesInternosEExatamenteOListado() throws IOException {
        // given: o codigo de producao do componente
        List<Path> arquivos = new FontesDeProducao().arquivosJava();

        // when: coletam-se os tipos publicos que nao estao sob `internal`
        List<String> publicos = new ArrayList<>();
        for (Path arquivo : arquivos) {
            String tipo = lerTipoPublicoForaDoInternal(arquivo);
            if (tipo != null) {
                publicos.add(tipo);
            }
        }

        // then: nada sobrou fora do contrato, e nada do contrato sumiu
        List<String> foraDoContrato = diferenca(publicos, CONTRATO);
        assertThat("tipo publico fora do contrato: " + foraDoContrato, foraDoContrato, is(empty()));

        List<String> ausentes = diferenca(CONTRATO, publicos);
        assertThat("tipo do contrato ausente: " + ausentes, ausentes, is(empty()));
    }

    private String lerTipoPublicoForaDoInternal(Path arquivo) throws IOException {
        String codigo = Files.readString(arquivo, StandardCharsets.UTF_8);
        String[] linhas = codigo.split("\n");
        String pacote = null;
        for (String linha : linhas) {
            String texto = linha.trim();
            if (texto.startsWith("package ")) {
                pacote = texto.substring("package ".length()).replace(";", "").trim();
                continue;
            }
            if (pacote == null || !texto.startsWith("public ")) {
                continue;
            }
            if (texto.contains("class ") || texto.contains("interface ")) {
                if (pacote.contains(PACOTE_INTERNO)) {
                    return null;
                }
                return pacote + "." + nomeDoTipo(texto);
            }
        }
        return null;
    }

    private String nomeDoTipo(String declaracao) {
        String texto = declaracao.replace("{", "").trim();
        String[] partes = texto.split(" ");
        for (int indice = 0; indice < partes.length - 1; indice++) {
            if (partes[indice].equals("class") || partes[indice].equals("interface")) {
                return partes[indice + 1];
            }
        }
        return texto;
    }

    private List<String> diferenca(List<String> primeira, List<String> segunda) {
        List<String> resultado = new ArrayList<>();
        for (String item : primeira) {
            if (!segunda.contains(item)) {
                resultado.add(item);
            }
        }
        return resultado;
    }
}
