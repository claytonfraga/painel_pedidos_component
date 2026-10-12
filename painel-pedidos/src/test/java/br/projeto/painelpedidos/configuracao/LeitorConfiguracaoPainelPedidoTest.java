package br.projeto.painelpedidos.configuracao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;

/**
 * Configuracao obrigatoria por painel_pedidos.env.
 *
 * @author clayton
 */
public final class LeitorConfiguracaoPainelPedidoTest {

    @TempDir
    Path diretorioTemporario;

    @Test
    public void leAsChavesObrigatoriasDoArquivo() throws IOException {
        // given: um painel_pedidos.env com as quatro chaves de SLA
        escreverConfiguracao(
                "SLA_NOVO_MINUTOS=7\n"
                + "SLA_EM_PREPARO_MINUTOS=15\n"
                + "SLA_PRONTO_PARA_ENTREGA_MINUTOS=12\n"
                + "SLA_EM_ROTA_DE_ENTREGA_MINUTOS=9\n"
                + "SLA_MONITOR_INTERVALO_SEGUNDOS=45\n"
                + "NOTIFICACAO_DURACAO_SEGUNDOS=8\n"
                + "SOM_ATIVO=false\n"
                + "TITULO_JANELA=Meu Painel\n"
                + "IDENTIFICADOR_APLICACAO=painel-de-teste\n");

        // when: a configuracao e lida
        ConfiguracaoPainelPedido configuracao = criarLeitor().ler();

        // then: os valores refletem o arquivo
        assertThat(configuracao.getSlaPorSituacao().get("Novo"), is(Duration.ofMinutes(7)));
        assertThat(configuracao.getSlaPorSituacao().get("Em preparo"), is(Duration.ofMinutes(15)));
        assertThat(configuracao.getSlaPorSituacao().get("Pronto para entrega"), is(Duration.ofMinutes(12)));
        assertThat(configuracao.getSlaPorSituacao().get("Em rota de entrega"), is(Duration.ofMinutes(9)));
        assertThat(configuracao.getSlaMonitorIntervalo(), is(Duration.ofSeconds(45)));
        assertThat(configuracao.getDuracaoNotificacaoSegundos(), is(8));
        assertThat(configuracao.isSomAtivo(), is(false));
        assertThat(configuracao.getTituloJanela(), is("Meu Painel"));
        assertThat(configuracao.getIdentificadorAplicacao(), is("painel-de-teste"));
    }

    @Test
    public void identificadorDaAplicacaoAusenteLancaExcecao() throws IOException {
        // given: um painel_pedidos.env com as quatro chaves de SLA, sem a identidade
        escreverConfiguracao(
                "SLA_NOVO_MINUTOS=5\n"
                + "SLA_EM_PREPARO_MINUTOS=20\n"
                + "SLA_PRONTO_PARA_ENTREGA_MINUTOS=10\n"
                + "SLA_EM_ROTA_DE_ENTREGA_MINUTOS=10\n");

        // when: tenta-se ler a configuracao
        Executable ler = new Executable() {
            @Override
            public void execute() {
                criarLeitor().ler();
            }
        };

        // then: uma excecao orientando informar a identidade e lancada
        IllegalStateException excecao = assertThrows(IllegalStateException.class, ler);
        assertThat(excecao.getMessage().contains("IDENTIFICADOR_APLICACAO"), is(true));
    }

    @Test
    public void arquivoAusenteLancaExcecao() {
        // given: nenhum arquivo painel_pedidos.env

        // when: tenta-se ler a configuracao
        Executable ler = new Executable() {
            @Override
            public void execute() {
                criarLeitor().ler();
            }
        };

        // then: uma excecao e lancada (arquivo obrigatorio)
        assertThrows(IllegalStateException.class, ler);
    }

    @Test
    public void chaveDeSlaAusenteLancaExcecao() throws IOException {
        // given: um painel_pedidos.env sem a chave SLA_EM_ROTA_DE_ENTREGA_MINUTOS
        escreverConfiguracao(
                "SLA_NOVO_MINUTOS=5\n"
                + "SLA_EM_PREPARO_MINUTOS=20\n"
                + "SLA_PRONTO_PARA_ENTREGA_MINUTOS=10\n");

        // when: tenta-se ler a configuracao
        Executable ler = new Executable() {
            @Override
            public void execute() {
                criarLeitor().ler();
            }
        };

        // then: uma excecao e lancada (chave de SLA obrigatoria)
        assertThrows(IllegalStateException.class, ler);
    }

    private LeitorConfiguracaoPainelPedido criarLeitor() {
        return new LeitorConfiguracaoPainelPedido(
                diretorioTemporario.toString(), "painel_pedidos.env");
    }

    private void escreverConfiguracao(String conteudo) throws IOException {
        Files.writeString(diretorioTemporario.resolve("painel_pedidos.env"),
                conteudo, StandardCharsets.UTF_8);
    }
}
