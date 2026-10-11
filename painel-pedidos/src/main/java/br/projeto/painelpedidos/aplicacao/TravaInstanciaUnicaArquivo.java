package br.projeto.painelpedidos.aplicacao;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Trava de instancia unica sobre um arquivo, exclusiva entre processos via
 * {@link java.nio.channels.FileLock}. O arquivo NUNCA e apagado - apaga-lo e o
 * que quebra a exclusao; o sistema operacional libera a trava quando o processo
 * termina, entao um arquivo remanescente nao bloqueia a proxima abertura.
 *
 * @author clayton
 */
public final class TravaInstanciaUnicaArquivo implements ITravaInstanciaUnica {

    private final Path caminhoArquivo;
    private FileChannel canal;

    public TravaInstanciaUnicaArquivo(Path caminhoArquivo) {
        this.caminhoArquivo = Objects.requireNonNull(caminhoArquivo, "caminhoArquivo");
    }

    @Override
    public boolean adquirir() {
        if (canal != null) {
            return false;
        }
        FileChannel canalAberto = abrirCanal();
        if (!tentarTravar(canalAberto)) {
            fecharCanal(canalAberto);
            return false;
        }
        this.canal = canalAberto;
        return true;
    }

    @Override
    public void liberar() {
        if (canal == null) {
            return;
        }
        FileChannel canalAtual = canal;
        this.canal = null;
        fecharCanal(canalAtual);
    }

    private FileChannel abrirCanal() {
        try {
            return FileChannel.open(
                    caminhoArquivo, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException excecao) {
            throw new IllegalStateException(
                    "Nao foi possivel abrir o arquivo da trava de instancia unica", excecao);
        }
    }

    private boolean tentarTravar(FileChannel canalAberto) {
        try {
            return canalAberto.tryLock() != null;
        } catch (OverlappingFileLockException excecao) {
            return false;
        } catch (IOException excecao) {
            throw new IllegalStateException(
                    "Nao foi possivel adquirir a trava de instancia unica", excecao);
        }
    }

    private void fecharCanal(FileChannel canalAberto) {
        try {
            canalAberto.close();
        } catch (IOException excecao) {
            throw new IllegalStateException(
                    "Nao foi possivel liberar a trava de instancia unica", excecao);
        }
    }
}
