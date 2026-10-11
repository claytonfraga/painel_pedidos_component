# OpenSpec - Painel de Acompanhamento de Pedidos (componente)

Este diretorio segue a estrutura do OpenSpec: `project.md`, `AGENTS.md`,
`specs/` (comportamento atual) e `changes/` (mudancas propostas e arquivadas).
Cada assunto tem **um unico lar**; os demais documentos apontam para ele.

## Estrutura

```
openspec/
├── README.md                         # indice, ordem de leitura e mapa DRY
├── project.md                        # contexto do projeto
├── AGENTS.md                         # regras/convencoes obrigatorias
├── prd.md                            # produto (visao, escopo, RNF, metricas)
├── references/                       # material de referencia indexado
├── specs/
│   └── painel-acompanhamento-pedidos/
│       ├── spec.md                   # REQUISITOS (o que e por que)
│       ├── design.md                 # DESIGN: padroes e diagramas UML
│       ├── state-machines.md         # maquinas de estado UML
│       ├── nao-funcional.feature     # Requisitos nao funcionais
│       └── us21..us29*.feature       # CENARIOS de negocio (fonte testavel)
└── changes/
    └── archive/
        ├── 2026-10-06-painel-pedidos-mvp/
        ├── 2026-10-11-componente-reutilizavel/
        ├── 2026-10-11-pedido-abstract-e-listeners/
        └── 2026-10-11-sla-por-estado-e-config-obrigatoria/
```

## Ordem de leitura

| Ordem | Documento | Papel |
|---|---|---|
| 1 | [`project.md`](./project.md) | Contexto, arquitetura, modulos, tecnologia |
| 2 | [`prd.md`](./prd.md) | Produto: visao, escopo, RNF, metricas, riscos |
| 3 | [`specs/.../spec.md`](./specs/painel-acompanhamento-pedidos/spec.md) | Normativo (o "o que") |
| 4 | [`specs/.../*.feature`](./specs/painel-acompanhamento-pedidos/us21-acompanhar-pedidos.feature) | Testavel (Gherkin) |
| 5 | [`specs/.../design.md`](./specs/painel-acompanhamento-pedidos/design.md) | Tecnico (o "como") |
| 6 | [`specs/.../state-machines.md`](./specs/painel-acompanhamento-pedidos/state-machines.md) | Dinamica de estados |
| 7 | [`AGENTS.md`](./AGENTS.md) | Regras obrigatorias |

## Historias de usuario (fonte testavel)

| Historia | Feature | Cenarios |
|---|---|---|
| US21 - Acompanhar pedidos | [`us21-acompanhar-pedidos.feature`](./specs/painel-acompanhamento-pedidos/us21-acompanhar-pedidos.feature) | `@US21-001`..`018` |
| US22 - Filtrar por periodo e data | [`us22-filtro-periodo-data.feature`](./specs/painel-acompanhamento-pedidos/us22-filtro-periodo-data.feature) | `@US22-001`..`004` |
| US24 - Prioridade, SLA e ordenacao | [`us24-prioridade-sla-ordenacao.feature`](./specs/painel-acompanhamento-pedidos/us24-prioridade-sla-ordenacao.feature) | `@US24-001`..`007` |
| US26 - Ingestao de novos pedidos | [`us26-ingestao-novos-pedidos.feature`](./specs/painel-acompanhamento-pedidos/us26-ingestao-novos-pedidos.feature) | `@US26-001`..`003` |
| US28 - Acoes e modelo do sistema integrado | [`us28-acoes-do-cliente.feature`](./specs/painel-acompanhamento-pedidos/us28-acoes-do-cliente.feature) | `@US28-001`..`004` |
| US29 - Criar novos pedidos pelo painel | [`us29-botao-novo-pedido.feature`](./specs/painel-acompanhamento-pedidos/us29-botao-novo-pedido.feature) | `@US29-001`..`003` |
| Requisitos nao funcionais | [`nao-funcional.feature`](./specs/painel-acompanhamento-pedidos/nao-funcional.feature) | `@US25-001`, `@US27-001`..`005`, `@NF-001`..`003` |

## Mapa DRY (onde cada assunto vive)

| Assunto | Unico lar |
|---|---|
| Contexto, modulos e tecnologia | `project.md` |
| Visao de produto, escopo, RNF, metricas | `prd.md` |
| Requisitos e regras de negocio (BR12, BR13, US23) | `spec.md` |
| Cenarios de comportamento (Gherkin) | `*.feature` |
| Padroes de projeto e diagramas UML | `design.md` |
| Maquinas de estado UML | `state-machines.md` |
| Regras e convencoes de codigo | `AGENTS.md` |
| Decisoes das mudancas entregues | `changes/archive/**/design.md` |

## Convencoes

- Identificadores em portugues sem acentos; classes no singular; interfaces com
  prefixo `I`; padroes com sufixo (`State`, `Service`, `Repository`, `Handler`,
  `Strategy`, `Observer`).
- Referencias bibliograficas (ABNT) em `specs/.../design.md`.
