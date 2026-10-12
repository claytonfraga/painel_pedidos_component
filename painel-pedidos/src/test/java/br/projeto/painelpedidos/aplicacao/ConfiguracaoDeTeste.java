package br.projeto.painelpedidos.aplicacao;

import br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuracao de teste compartilhada pelos testes da fachada: evita repetir o
 * mapa de SLA e os defaults em cada classe.
 *
 * @author clayton
 */
public final class ConfiguracaoDeTeste {

    public ConfiguracaoPainelPedido criar(String identificadorAplicacao, boolean somAtivo) {
        Map<String, Duration> slaPorSituacao = new LinkedHashMap<>();
        slaPorSituacao.put("Novo", Duration.ofMinutes(5));
        slaPorSituacao.put("Em preparo", Duration.ofMinutes(20));
        slaPorSituacao.put("Pronto para entrega", Duration.ofMinutes(10));
        slaPorSituacao.put("Em rota de entrega", Duration.ofMinutes(10));
        return new ConfiguracaoPainelPedido(
                slaPorSituacao,
                Duration.ofSeconds(30),
                10,
                somAtivo,
                "Teste",
                identificadorAplicacao);
    }
}
