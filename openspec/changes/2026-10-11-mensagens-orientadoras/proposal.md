# Proposta - Mensagens de excecao orientadoras

## Por que

As mensagens de excecao do projeto sao, em muitos pontos, apenas o nome do
parametro (`Objects.requireNonNull(repositorio, "repositorio")`) ou um substantivo
solto. Quem recebe a falha fica sem saber o que fazer para corrigir.

## O que muda

- Regra registrada em `openspec/AGENTS.md`: **toda** mensagem de excecao orienta
  quem vai corrigir.
- Verificacao de nulo: comeca com `Informe ` e nomeia especificamente o item -
  `"Informe um repositorio de pedidos valido"`.
- Verificacao `if` que levanta: comeca pelo verbo da acao (`Informe`, `Configure`,
  `Escolha`, `Use`).
- Operacao invalida no estado (GoF State): diz onde a operacao e valida - ex.:
  `"... ; registre o pagamento em um pedido Novo"`.
- Reescreve todas as ~80 mensagens do componente e do exemplo.

## Impacto

- Nenhuma mudanca de comportamento; apenas o texto das mensagens.
- `MensagemExcecaoTest` (nos dois modulos) varre o codigo de producao e falha se
  uma verificacao de nulo deixar de comecar com `Informe ` ou se uma excecao for
  levantada com um identificador solto.

## Alternativas consideradas

- **Convencao apenas na revisao, sem teste**: nao protege contra regressao de
  codigo gerado; a varredura e o mecanismo de garantia.
- **Exigir `valido` no fim de toda mensagem**: a concordancia de genero quebra
  ("uma forma de pagamento valida"); a regra exige o verbo de orientacao, nao o
  sufixo.
