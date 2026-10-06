# Indice de referencias

Material de referencia que fundamenta as decisoes arquiteturais deste projeto.
As regras derivadas destes textos estao em `openspec/AGENTS.md` e nas restricoes
de `openspec/specs/**/spec.md`.

| Referencia | Arquivo | Uso no projeto |
|---|---|---|
| **Model-View-Presenter** | [`Model-View-Presenter.md`](./Model-View-Presenter.md) | Padrao de apresentacao adotado: MVP na variante Passive View, com View abstraida por interface, Presenter coordenando o caso de uso e composicao no ponto de entrada. |

## Model-View-Presenter

O documento descreve o MVP, a diferenca em relacao ao MVC, a variante Passive
View, classes de servico opcionais e um exemplo de login com `ILoginView`,
`LoginPresenter`, `AutenticacaoService` e `AplicacaoPrincipal`. Referencias
citadas: Potel (1996), Krasner e Pope (1988), Fowler (2006), Martin (2003),
Syromiatnikov e Weyns (2014), Vreca (2023), Zhang e Luo (2010), Alles et al.
(2006).

### Regras extraidas

- A View e passiva: coleta entradas, dispara eventos e projeta saidas; nao decide
  o fluxo nem conhece o Model.
- O Presenter configura listeners, le a View, aciona o Model/servico e define a
  resposta visual.
- A View e acessada por uma abstracao (`ILoginView` no exemplo; `IPainelPedidoView`
  neste projeto), e a implementacao concreta e injetada na composicao
  (`AplicacaoPrincipal`).
- Classes de servico isolam regras de negocio fora do Presenter.
- A saida de mensagens pode ficar no Presenter com `JOptionPane` (coerente com a
  passividade da View) ou ser abstraida em `ExibidorMensagem` para testabilidade.
