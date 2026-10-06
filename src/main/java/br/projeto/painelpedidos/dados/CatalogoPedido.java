package br.projeto.painelpedidos.dados;

import java.util.List;

/**
 *
 * @author clayton
 */
public class CatalogoPedido {

    private final List<String> nomes;
    private final List<String> sobrenomes;
    private final List<String> logradouros;
    private final List<String> bairros;
    private final List<String> formasPagamento;

    public CatalogoPedido() {
        this.nomes = List.of(
                "Ana", "Bruno", "Carla", "Daniel", "Eduardo", "Fernanda", "Gabriel", "Helena",
                "Igor", "Julia", "Karina", "Lucas", "Marina", "Nicolas", "Olivia", "Paulo",
                "Rafael", "Sabrina", "Thiago", "Vanessa", "Wagner", "Yasmin", "Amanda", "Beatriz",
                "Caio", "Debora", "Everton", "Fabiana", "Guilherme", "Hugo", "Isabela", "Jonas",
                "Kelly", "Leandro", "Marcos", "Natalia", "Otavio", "Patricia", "Renata", "Simone");
        this.sobrenomes = List.of(
                "Souza", "Lima", "Rocha", "Almeida", "Mendes", "Costa", "Ribeiro", "Nunes",
                "Gomes", "Martins", "Barros", "Ramos", "Teixeira", "Fernandes", "Correia",
                "Pacheco", "Cardoso", "Oliveira", "Farias", "Batista");
        this.logradouros = List.of(
                "das Flores", "da Paz", "das Palmeiras", "Bela Vista", "do Comercio",
                "Sao Miguel", "das Acacias", "das Rosas", "Nova", "do Sol",
                "Alberto", "Bernardes", "Caetano", "Duarte", "Eurico",
                "Faria", "Gusmao", "Heloisa", "Iris", "Jonas",
                "Kleber", "Lima", "Moura", "Nogueira", "Oliveira",
                "Paiva", "Queiroz", "Ribeiro", "Sousa", "Torres");
        this.bairros = List.of("Centro", "Bela Vista", "Jardim", "Vila do Sul", "Sao Miguel");
        this.formasPagamento = List.of("Pix - chave CPF", "Pix - chave e-mail", "Cartão de crédito");
    }

    public List<String> getNomes() {
        return nomes;
    }

    public List<String> getSobrenomes() {
        return sobrenomes;
    }

    public List<String> getLogradouros() {
        return logradouros;
    }

    public List<String> getBairros() {
        return bairros;
    }

    public List<String> getFormasPagamento() {
        return formasPagamento;
    }
}
