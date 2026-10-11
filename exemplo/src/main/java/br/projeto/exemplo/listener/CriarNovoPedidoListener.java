package br.projeto.exemplo.listener;

import br.projeto.painelpedidos.listener.ICriarNovoPedidoListener;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;

/**
 * Criacao de novos pedidos da atividade: pergunta a quantidade (1 a 10, limite
 * fixo neste listener, nao no .env) e cria copias de pedidos existentes com novo
 * numero e a data atual (situacao Novo). O componente salva o retorno e atualiza
 * o painel.
 *
 * @author clayton
 */
public final class CriarNovoPedidoListener implements ICriarNovoPedidoListener {

    private final IPedidoRepository repositorio;
    private final int quantidadeMaxima = 10;

    public CriarNovoPedidoListener(IPedidoRepository repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio, "repositorio");
    }

    @Override
    public List<PedidoAbstract> criarNovoPedido() {
        List<PedidoAbstract> existentes = repositorio.buscarTodos();
        if (existentes.isEmpty()) {
            return new ArrayList<>();
        }

        int quantidade = escolherQuantidade();
        if (quantidade < 1) {
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

    private int escolherQuantidade() {
        List<String> opcoes = new ArrayList<>();
        for (int i = 1; i <= quantidadeMaxima; i++) {
            opcoes.add(String.valueOf(i));
        }

        Object escolha = JOptionPane.showInputDialog(
                null,
                "Quantos pedidos criar? (maximo " + quantidadeMaxima + ")",
                "Novo Pedido (tela do cliente)",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes.toArray(),
                opcoes.get(0));

        if (escolha == null) {
            return 0;
        }
        return Integer.parseInt(escolha.toString());
    }
}
