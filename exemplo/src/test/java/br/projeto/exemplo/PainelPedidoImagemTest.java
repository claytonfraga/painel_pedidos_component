package br.projeto.exemplo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import br.projeto.exemplo.seeder.PedidoSeeder;
import br.projeto.exemplo.repositorio.PedidoEmMemoriaRepository;
import br.projeto.painelpedidos.filtro.FabricaFiltroPeriodo;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.ordenacao.OrdenacaoPedidoPorPrioridadeETempoNoEstado;
import br.projeto.painelpedidos.presenter.PainelPedidoPresenter;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.som.SomSilenciosoStrategy;
import br.projeto.painelpedidos.tempo.FonteDataPedido;
import br.projeto.painelpedidos.tempo.IFonteDataPedido;
import br.projeto.painelpedidos.tempo.RelogioServiceSistema;
import br.projeto.painelpedidos.view.PainelPedidoPanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/**
 * Integracao: gera, de forma headless (sem X), o print do painel usado na
 * documentacao.
 *
 * @author clayton
 */
public final class PainelPedidoImagemTest {

    private static final int LARGURA = 1380;
    private static final int ALTURA = 980;

    @Test
    public void geraImagemDoPainelParaODocumentacao() throws Exception {
        // given: dados de exemplo e o painel montado com todos os estados
        LocalDateTime referencia = LocalDateTime.now();
        List<PedidoAbstract> pedidos = new PedidoSeeder(referencia).gerar();
        IPedidoRepository repositorio = new PedidoEmMemoriaRepository(pedidos);

        PainelPedidoPanel painel = new PainelPedidoPanel();
        IFonteDataPedido fonteDataPedido = new FonteDataPedido(new RelogioServiceSistema());
        PainelPedidoPresenter presenter = new PainelPedidoPresenter(
                painel,
                repositorio,
                fonteDataPedido,
                new FabricaFiltroPeriodo(),
                new SomSilenciosoStrategy(),
                new OrdenacaoPedidoPorPrioridadeETempoNoEstado(),
                10);
        painel.getCmbPeriodo().setSelectedIndex(3);
        presenter.atualizarPainel();

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
