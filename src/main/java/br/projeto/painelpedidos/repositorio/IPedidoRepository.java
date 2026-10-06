package br.projeto.painelpedidos.repositorio;

import br.projeto.painelpedidos.model.Pedido;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface IPedidoRepository {

    void salvar(Pedido pedido);

    List<Pedido> buscarTodos();

    String gerarProximoIdentificador();
}
