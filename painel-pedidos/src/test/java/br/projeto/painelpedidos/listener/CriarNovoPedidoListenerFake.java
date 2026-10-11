package br.projeto.painelpedidos.listener;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fake funcional de {@link ICriarNovoPedidoListener}: comporta-se como um
 * consumidor real (como o exemplo), copiando pedidos da origem de dados com novo
 * numero e a data atual, sem depender do modulo de exemplo.
 *
 * @author clayton
 */
public final class CriarNovoPedidoListenerFake implements ICriarNovoPedidoListener {

    private final IPedidoRepository repositorio;
    private final int quantidade;

    public CriarNovoPedidoListenerFake(IPedidoRepository repositorio, int quantidade) {
        this.repositorio = Objects.requireNonNull(repositorio, "repositorio");
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade nao pode ser negativa");
        }
        this.quantidade = quantidade;
    }

    @Override
    public List<PedidoAbstract> criarNovoPedido() {
        List<PedidoAbstract> existentes = repositorio.buscarTodos();
        if (existentes.isEmpty()) {
            return new ArrayList<>();
        }

        List<PedidoAbstract> novos = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();
        for (int indice = 0; indice < quantidade; indice++) {
            PedidoAbstract base = existentes.get(indice % existentes.size());
            String novoIdentificador = repositorio.gerarProximoIdentificador();
            novos.add(base.criarCopia(novoIdentificador, agora));
        }
        return novos;
    }
}
