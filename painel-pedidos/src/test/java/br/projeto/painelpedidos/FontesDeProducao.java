package br.projeto.painelpedidos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Leitura das fontes de producao do componente para os testes estruturais, para
 * a recursao de diretorios nao ser repetida em cada gate.
 *
 * @author clayton
 */
public final class FontesDeProducao {

    private final Path raiz = Paths.get("src/main/java");

    public List<Path> arquivosJava() throws IOException {
        List<Path> arquivos = new ArrayList<>();
        coletar(raiz, arquivos);
        return arquivos;
    }

    public String codigo() throws IOException {
        StringBuilder codigo = new StringBuilder();
        for (Path arquivo : arquivosJava()) {
            codigo.append(Files.readString(arquivo, StandardCharsets.UTF_8)).append('\n');
        }
        return codigo.toString();
    }

    private void coletar(Path diretorio, List<Path> destino) throws IOException {
        try (DirectoryStream<Path> entradas = Files.newDirectoryStream(diretorio)) {
            for (Path entrada : entradas) {
                if (Files.isDirectory(entrada)) {
                    coletar(entrada, destino);
                    continue;
                }
                if (entrada.toString().endsWith(".java")) {
                    destino.add(entrada);
                }
            }
        }
    }
}
