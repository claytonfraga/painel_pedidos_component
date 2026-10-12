package br.projeto.painelpedidos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Estrutura do componente (requisitos nao funcionais): sem simulacao, dados
 * mockados, seed ou leitura automatica; o modelo e abstrato e sem toString.
 *
 * @author clayton
 */
public final class EstruturaComponenteTest {

    @Test
    public void naoHaClassesDeSimulacao() {
        // given: o componente compilado

        // when: procura-se pelas classes de simulacao

        // then: nenhuma delas existe no classpath
        afirmarClasseAusente("br.projeto.painelpedidos.simulacao.SimuladorNovoPedido");
        afirmarClasseAusente("br.projeto.painelpedidos.simulacao.ISimuladorNovoPedido");
        afirmarClasseAusente("br.projeto.painelpedidos.simulacao.GeradorPedidoSimulado");
        afirmarClasseAusente("br.projeto.painelpedidos.simulacao.IGeradorPedido");
        afirmarClasseAusente("br.projeto.painelpedidos.simulacao.ConfiguracaoSimulacao");
        afirmarClasseAusente("br.projeto.painelpedidos.simulacao.AleatoriedadeServiceRandom");
    }

    @Test
    public void naoHaDadosMockadosNemSeed() {
        // given: o componente compilado

        // when: procura-se pelo seeder e pelo catalogo mockado

        // then: nenhum deles existe no classpath
        afirmarClasseAusente("br.projeto.painelpedidos.seeder.PedidoSeeder");
        afirmarClasseAusente("br.projeto.painelpedidos.dados.CatalogoPedido");
    }

    @Test
    public void configuracaoDeixaDeDependerDeLeitorEnv() {
        // given: o componente compilado

        // when: procura-se pelo antigo leitor de .env

        // then: ele nao existe mais (a config e painel_pedidos.env)
        afirmarClasseAusente("br.projeto.painelpedidos.configuracao.LeitorEnv");
    }

    @Test
    public void naoHaLeituraAutomaticaDeNovosPedidos() {
        // given: o componente compilado

        // when: procura-se pela porta e pelo leitor de novos pedidos

        // then: nenhum deles existe (a ingestao e por empurrao)
        afirmarClasseAusente("br.projeto.painelpedidos.ingestao.IFonteNovoPedido");
        afirmarClasseAusente("br.projeto.painelpedidos.ingestao.LeitorNovoPedidoService");
    }

    @Test
    public void naoHaPedidoConcretoNoComponente() {
        // given: o componente compilado

        // when: procura-se por uma classe concreta Pedido

        // then: ela nao existe (o cliente estende PedidoAbstract)
        afirmarClasseAusente("br.projeto.painelpedidos.model.Pedido");
    }

    @Test
    public void instanciaUnicaEPossuidaPeloComponente() {
        // given: o componente compilado

        // when: procura-se pelo involucro de execucao e pelo singleton

        // then: nenhum deles existe (a fachada aplica a instancia unica)
        afirmarClasseAusente("br.projeto.painelpedidos.aplicacao.IExecucaoAplicacao");
        afirmarClasseAusente("br.projeto.painelpedidos.aplicacao.InstanciaUnicaAplicacao");
        afirmarClasseAusente("br.projeto.painelpedidos.aplicacao.ITravaInstanciaUnica");
    }

    @Test
    public void modeloDePedidoEAbstrato() throws ClassNotFoundException {
        // given: o componente compilado

        // when: carrega-se o modelo de pedido

        // then: ele e uma classe abstrata
        Class<?> tipo = Class.forName("br.projeto.painelpedidos.model.PedidoAbstract");
        assertThat(Modifier.isAbstract(tipo.getModifiers()), is(true));
    }

    @Test
    public void pedidoAbstratoNaoDefineToString() throws ClassNotFoundException, NoSuchMethodException {
        // given: o componente compilado

        // when: inspeciona-se o toString do modelo

        // then: ele e o de Object (o modelo nao define toString)
        Class<?> tipo = Class.forName("br.projeto.painelpedidos.model.PedidoAbstract");
        String declarante = tipo.getMethod("toString").getDeclaringClass().getName();
        assertThat(declarante, is("java.lang.Object"));
    }

    private void afirmarClasseAusente(String nomeClasse) {
        // given: um nome de classe que nao deve existir
        // when: tenta-se carrega-la
        // then: ClassNotFoundException
        assertThrows(ClassNotFoundException.class, new Executable() {
            @Override
            public void execute() throws ClassNotFoundException {
                Class.forName(nomeClasse);
            }
        });
    }
}
