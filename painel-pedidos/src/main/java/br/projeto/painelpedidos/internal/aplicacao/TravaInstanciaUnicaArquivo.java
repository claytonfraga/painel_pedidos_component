package br.projeto.painelpedidos.internal.aplicacao;

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
 * <p>Detalhe interno do componente: nao e porta nem ponto de extensao - o
 * consumidor nao configura trava alguma.</p>
 *
 * @author clayton
 */
public final class TravaInstanciaUnicaArquivo {

    private final Path caminhoArquivo;
    private FileChannel canal;

    public TravaInstanciaUnicaArquivo(Path caminhoArquivo) {
        this.caminhoArquivo = Objects.requireNonNull(
                caminhoArquivo, "Informe um caminho de arquivo de controle valido");
    }

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
                    "Nao foi possivel abrir o arquivo de controle de instancia unica em "
                    + caminhoArquivo + "; verifique as permissoes de escrita", excecao);
        }
    }

    private boolean tentarTravar(FileChannel canalAberto) {
        try {
            return canalAberto.tryLock() != null;
        } catch (OverlappingFileLockException excecao) {
            return false;
        } catch (IOException excecao) {
            throw new IllegalStateException(
                    "Nao foi possivel usar a trava de instancia unica em " + caminhoArquivo
                    + "; verifique as permissoes de escrita", excecao);
        }
    }

    private void fecharCanal(FileChannel canalAberto) {
        try {
            canalAberto.close();
        } catch (IOException excecao) {
            throw new IllegalStateException(
                    "Nao foi possivel liberar a trava de instancia unica em " + caminhoArquivo
                    + "; reinicie a aplicacao para liberar", excecao);
        }
    }
}
