package br.projeto.exemplo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import br.projeto.exemplo.repositorio.PedidoEmMemoriaRepository;
import br.projeto.exemplo.seeder.PedidoSeeder;
import br.projeto.painelpedidos.aplicacao.PainelPedidoFacade;
import br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido;
import br.projeto.painelpedidos.configuracao.LeitorConfiguracaoPainelPedido;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.som.SomTomStrategy;
import br.projeto.painelpedidos.tempo.RelogioServiceSistema;
import br.projeto.painelpedidos.view.PainelPedidoView;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/**
 * Integracao: gera, de forma headless (sem X), o print do painel usado na
 * documentacao. O painel vem montado pela fachada, pelo caminho de embutir.
 *
 * @author clayton
 */
public final class PainelPedidoImagemTest {

    private static final int LARGURA = 1380;
    private static final int ALTURA = 980;

    @Test
    public void geraImagemDoPainelParaODocumentacao() throws Exception {
        // given: dados de exemplo e o painel montado pela fachada (sem janela)
        LocalDateTime referencia = LocalDateTime.now();
        List<PedidoAbstract> pedidos = new PedidoSeeder(referencia).gerar();
        IPedidoRepository repositorio = new PedidoEmMemoriaRepository(pedidos);
        ConfiguracaoPainelPedido configuracao = new LeitorConfiguracaoPainelPedido(
                ".", "painel_pedidos.env").ler();

        PainelPedidoFacade painelPedido = new PainelPedidoFacade(
                repositorio, configuracao, new SomTomStrategy(880, 200), new RelogioServiceSistema());
        PainelPedidoView painel = painelPedido.getPainelPedido();

        painel.getCmbPeriodo().setSelectedIndex(3);
        painel.getBtnAtualizarPainel().doClick();

        painel.setSize(LARGURA, ALTURA);
        painel.addNotify();
        painel.validate();

        // when: o painel e pintado em uma imagem (headless)
        BufferedImage imagem = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);
        Graphics2D grafico = imagem.createGraphics();
        painel.paint(grafico);
        grafico.dispose();

        File destino = new File("../docs/painel-pedidos.png");
        destino.getParentFile().mkdirs();
        ImageIO.write(imagem, "png", destino);

        // then: a imagem e gerada com conteudo (tamanho e pixels escuros)
        assertThat(destino.exists(), is(true));
        assertThat((int) destino.length(), greaterThan(10000));
        assertThat(quantidadeDePixelsEscuros(imagem), greaterThan(500));
    }

    private int quantidadeDePixelsEscuros(BufferedImage imagem) {
        int quantidade = 0;
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                int rgb = imagem.getRGB(x, y);
                int vermelho = (rgb >> 16) & 0xFF;
                int verde = (rgb >> 8) & 0xFF;
                int azul = rgb & 0xFF;
                if (vermelho < 120 && verde < 120 && azul < 120) {
                    quantidade++;
                }
            }
        }
        return quantidade;
    }
}
