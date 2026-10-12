package br.projeto.painelpedidos.internal.som;

import br.projeto.painelpedidos.som.IEstrategiaSom;

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
