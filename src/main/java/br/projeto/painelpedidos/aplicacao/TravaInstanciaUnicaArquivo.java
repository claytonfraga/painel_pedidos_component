package br.projeto.painelpedidos.aplicacao;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class TravaInstanciaUnicaArquivo implements ITravaInstanciaUnica {

    private final Path caminhoArquivo;
    private FileChannel canal;
    private FileLock trava;

    public TravaInstanciaUnicaArquivo(Path caminhoArquivo) {
        this.caminhoArquivo = Objects.requireNonNull(caminhoArquivo, "caminhoArquivo");
    }

    @Override
    public boolean adquirir() {
        try {
            this.canal = FileChannel.open(
                    caminhoArquivo, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            this.trava = canal.tryLock();
            return trava != null;
        } catch (IOException excecao) {
            throw new IllegalStateException(
                    "Nao foi possivel adquirir a trava de instancia unica", excecao);
        }
    }
}
