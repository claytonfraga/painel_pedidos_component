# Tarefas - Mensagens de excecao orientadoras

## 1. Especificacao

- [x] Regra em `openspec/AGENTS.md` ("Mensagens de excecao").
- [x] Requirement nao funcional em `spec.md` e cenario `@NF-004`.
- [x] Change `2026-10-11-mensagens-orientadoras`.

## 2. Componente

- [x] Toda verificacao de nulo comeca com `Informe ` e nomeia o item.
- [x] Guards (`if`) comecam pelo verbo da acao.
- [x] Operacao invalida no estado diz onde e valida.

## 3. Exemplo

- [x] Mesmas regras nas mensagens do modulo consumidor.

## 4. Testes e build

- [x] `MensagemExcecaoTest` no componente e no exemplo (varredura do producao).
- [x] Teste comportamental da mensagem da fachada (nulo) e do estado.
- [x] `mvn -U clean verify` verde e sincronizacao.
