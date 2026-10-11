package br.projeto.painelpedidos.aplicacao;

/**
 *
 * @author clayton
 */
public final class InstanciaUnicaAplicacao {

    private static volatile InstanciaUnicaAplicacao instancia;

    private boolean iniciada;
    private ITravaInstanciaUnica travaInstancia;

    private InstanciaUnicaAplicacao() {
    }

    public static InstanciaUnicaAplicacao getInstance() {
        InstanciaUnicaAplicacao resultado = instancia;
        if (resultado == null) {
            synchronized (InstanciaUnicaAplicacao.class) {
                resultado = instancia;
                if (resultado == null) {
                    resultado = new InstanciaUnicaAplicacao();
                    instancia = resultado;
                }
            }
        }
        return resultado;
    }

    public synchronized boolean iniciar(IExecucaoAplicacao aplicacao, ITravaInstanciaUnica trava) {
        if (iniciada) {
            return false;
        }
        if (!trava.adquirir()) {
            return false;
        }
        this.travaInstancia = trava;
        this.iniciada = true;
        aplicacao.executar();
        return true;
    }
}
