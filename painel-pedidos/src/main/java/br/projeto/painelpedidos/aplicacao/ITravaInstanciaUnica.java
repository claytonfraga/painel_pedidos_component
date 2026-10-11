package br.projeto.painelpedidos.aplicacao;

/**
 * Trava de instancia unica: garante que apenas uma janela do painel exista por
 * maquina.
 *
 * @author clayton
 */
public interface ITravaInstanciaUnica {

    boolean adquirir();

    void liberar();
}
