package br.projeto.painelpedidos.simulacao;

import br.projeto.painelpedidos.model.Pedido;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.Timer;

/**
 *
 * @author clayton
 */
public class SimuladorNovoPedido implements ISimuladorNovoPedido {

    private final ConfiguracaoSimulacao configuracao;
    private final IGeradorPedido geradorPedido;
    private final IAleatoriedadeService aleatoriedadeService;
    private final Timer timer;
    private ObservadorNovoPedido observador;

    public SimuladorNovoPedido(
            ConfiguracaoSimulacao configuracao,
            IGeradorPedido geradorPedido,
            IAleatoriedadeService aleatoriedadeService) {

        this.configuracao = Objects.requireNonNull(configuracao, "configuracao");
        this.geradorPedido = Objects.requireNonNull(geradorPedido, "geradorPedido");
        this.aleatoriedadeService = Objects.requireNonNull(aleatoriedadeService, "aleatoriedadeService");
        this.timer = new Timer(intervaloMilissegundos(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                executarCiclo();
            }
        });
        this.timer.setRepeats(false);
    }

    @Override
    public void iniciar(ObservadorNovoPedido observador) {
        this.observador = Objects.requireNonNull(observador, "observador");
        this.timer.setInitialDelay(intervaloMilissegundos());
        this.timer.start();
    }

    @Override
    public void parar() {
        this.timer.stop();
    }

    private void executarCiclo() {
        int quantidade = aleatoriedadeService.sortearInteiro(
                1, configuracao.getPedidosPorCiclo() + 1);
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            pedidos.add(geradorPedido.gerar());
        }
        if (observador != null) {
            observador.receberNovosPedidos(pedidos);
        }
        timer.setInitialDelay(intervaloMilissegundos());
        timer.restart();
    }

    private int intervaloMilissegundos() {
        int segundos = aleatoriedadeService.sortearInteiro(
                configuracao.getIntervaloMinimoSegundos(),
                configuracao.getIntervaloMaximoSegundos() + 1);
        return segundos * 1000;
    }
}
