package br.projeto.painelpedidos.aplicacao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.Test;

/**
 * Empacotamento: o jar gerado contem as dependencias, e a copia embutida da
 * dotenv esta relocada - assim o consumidor pode usar dotenv por conta propria
 * sem conflito de classes.
 *
 * @author clayton
 */
public final class ArtefatoEmpacotadoIT {

    private static final String DOTENV_RELOCADA =
            "br/projeto/painelpedidos/internal/dotenv/Dotenv.class";
    private static final String DOTENV_ORIGINAL =
            "io/github/cdimascio/dotenv/Dotenv.class";

    @Test
    public void dependenciaEmbutidaEstaRelocada() throws IOException {
        // given: o jar do componente produzido pelo package
        Path artefato = localizarArtefato();

        // when: listam-se as entradas do jar
        List<String> entradas = listarEntradas(artefato);

        // then: a dotenv esta no pacote relocado e nao no original
        assertThat(entradas.contains(DOTENV_RELOCADA), is(true));
        assertThat(entradas.contains(DOTENV_ORIGINAL), is(false));
    }

    private Path localizarArtefato() throws IOException {
        Path diretorio = Paths.get("target");
        List<Path> artefatos = new ArrayList<>();
        try (java.nio.file.DirectoryStream<Path> entradas = java.nio.file.Files.newDirectoryStream(diretorio)) {
            for (Path entrada : entradas) {
                String nome = entrada.getFileName().toString();
                boolean jarGerado = nome.endsWith(".jar")
                        && !nome.endsWith("-sources.jar")
                        && !nome.startsWith("original-");
                if (jarGerado) {
                    artefatos.add(entrada);
                }
            }
        }
        if (artefatos.isEmpty()) {
            throw new IllegalStateException(
                    "Informe um jar gerado em target; rode o package antes deste teste");
        }
        return artefatos.get(0);
    }

    private List<String> listarEntradas(Path artefato) throws IOException {
        List<String> entradas = new ArrayList<>();
        try (ZipFile zip = new ZipFile(artefato.toFile())) {
            Enumeration<? extends ZipEntry> itens = zip.entries();
            while (itens.hasMoreElements()) {
                entradas.add(itens.nextElement().getName());
            }
        }
        return entradas;
    }
}
