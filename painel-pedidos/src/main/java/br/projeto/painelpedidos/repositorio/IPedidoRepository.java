package br.projeto.painelpedidos.repositorio;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface IPedidoRepository {

    void salvar(PedidoAbstract pedido);

    List<PedidoAbstract> buscarTodos();

    String gerarProximoIdentificador();
}
