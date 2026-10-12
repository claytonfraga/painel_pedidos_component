# Spec Delta - Painel de Acompanhamento de Pedidos

Change: `2026-10-06-painel-pedidos-mvp` (arquivada). Requisitos adicionados na
capacidade `painel-acompanhamento-pedidos`. A especificacao normativa consolidada
esta em `openspec/specs/painel-acompanhamento-pedidos/spec.md` e a fonte testavel
nos `*.feature` da mesma pasta.

## ADDED Requirements

### Requirement: Painel em colunas por situacao
Cenarios: `@US21-001`, `@US21-002`.

### Requirement: Busca e filtros de periodo/data
Cenarios: `@US21-003`, `@US21-004`, `@US22-001`, `@US22-002`.

### Requirement: Transicoes do ciclo de vida (BR12)
Cenarios: `@US21-005` a `@US21-009`.

### Requirement: Cancelamento e credito (BR13)
Cenarios: `@US21-010` a `@US21-014`.

### Requirement: Compatibilidade do botao Executar acao
Cenarios: `@US21-015`.

### Requirement: Selecao e navegacao por teclado
Cenarios: `@US21-016`, `@US21-017`.

### Requirement: Simulacao e notificacao de novos pedidos (CoR adaptado + Observer)
Cenarios: `@US23-001` a `@US23-005`, `@US22-003`, `@US22-004`.

### Requirement: Prioridade por SLA e ordenacao por tempo no estado
Cenarios: `@US24-001` a `@US24-007`.

### Requirement: Aplicacao aberta uma unica vez (Singleton + trava)
Cenarios: `@US25-001`.
