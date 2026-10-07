# Change: Painel de Acompanhamento de Pedidos (MVP Passive View)

## Why

Entregar um POC desktop do Painel de Acompanhamento de Pedidos do delivery com
arquitetura MVP Passive View, ciclo de vida por estados, filtros por periodo/data,
simulacao de novos pedidos, prioridade por SLA e ordenacao por tempo no estado,
seguindo SOLID (DIP/SRP/OCP) e as convencoes do projeto.

## What Changes

- Painel em 6 colunas de situacao com acoes por estado (BR12) e cancelamento com
  credito (BR13).
- Filtros de periodo padrao e por data especifica (datepicker).
- Seeder deterministico com quantidade via `.env` (encapsulado em `LeitorEnv`).
- Simulacao de novos pedidos (1..N por ciclo) com alerta sonoro, mensagem e
  destaque, usando Chain of Responsibility adaptado e Observer.
- Prioridade por SLA por estado e ordenacao dos cards por tempo no estado.
- Instancia unica da aplicacao (Singleton thread-safe + trava de arquivo).
- Documentacao: PRD, especificacao normativa, design e features Gherkin.

## Impact

- Capacidade nova: `specs/painel-acompanhamento-pedidos`.
- Dependencia nova: `io.github.cdimascio:dotenv-java` (encapsulada por
  `LeitorEnv`), escopo `compile`.
- Sem Spring; persistencia em memoria atras de `IPedidoRepository`.
- Restricoes: sem lambda/Stream/enum/instanceof/Runnable; sem atributos estaticos
  (excecao: Singleton); excecoes unchecked.
