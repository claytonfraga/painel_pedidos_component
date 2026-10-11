package br.projeto.painelpedidos.som;

/**
 * Estrategia de som que nao emite audio; usada quando o som esta desativado.
 *
 * @author clayton
 */
public final class SomSilenciosoStrategy implements IEstrategiaSom {

    @Override
    public void tocar() {
    }
}
