# Proposta - Fronteira de reuso na especificacao

## Por que

A fronteira de reuso foi implementada, mas a especificacao ficou atras: `spec.md`,
`us25` e `nao-funcional.feature` ainda descrevem a instancia unica por componente,
nao citam a identidade da aplicacao, o caminho de embutir, o contrato publico nem
a relocacao da dependencia embutida. Um leitor que planeje a partir da spec
construiria a fronteira errada.

## O que muda

- `spec.md`: a requirement de instancia unica passa a ser **por aplicacao**
  (identidade obrigatoria, arquivo derivado dela); a de configuracao inclui a
  chave da identidade; a de componente reutilizavel passa a enumerar o
  **contrato publico**, o **caminho de embutir** e a **relocacao**.
- `us25-instancia-unica.feature`: cenario de dois sistemas na mesma maquina.
- `nao-funcional.feature`: cenarios da identidade obrigatoria, do contrato
  enumerado e da dependencia relocada.
- Arquivadas as changes `2026-10-11-instancia-unica-no-componente` e
  `2026-10-11-mensagens-orientadoras`, ja implementadas e verdes.

## Impacto

- Nenhuma mudanca de comportamento: a spec passa a refletir o codigo.
- O gate `ContratoPublicoTest` e citado como a prova do contrato.

## Alternativas consideradas

- **Deixar a spec como esta e so registrar no `AGENTS.md`**: o `AGENTS.md` guarda
  as regras de codigo; o comportamento do sistema vive em `spec.md` e nos
  `.feature`, que e de onde o proximo trabalho e planejado.
