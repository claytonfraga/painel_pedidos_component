# OpenSpec - Painel de Acompanhamento de Pedidos

Este diretorio segue a estrutura do OpenSpec: `project.md`, `AGENTS.md`,
`specs/` (comportamento atual) e `changes/` (mudancas propostas e arquivadas).
Cada assunto tem **um unico lar**; os demais documentos apontam para ele por link,
sem repetir o conteudo (inclusive os `.feature`).

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
│       ├── design.md                 # DESIGN: padroes e diagramas de classe UML
│       ├── state-machines.md         # maquinas de estado UML (dinamica)
│       └── us21..us25*.feature       # CENARIOS (fonte testavel)
└── changes/
    └── archive/2026-10-06-painel-pedidos-mvp/
        ├── proposal.md  ├── tasks.md  ├── design.md  └── specs/…/spec.md
```

## Ordem de leitura

| Ordem | Documento | Papel | Papel em relacao ao todo |
|---|---|---|---|
| 1 | [`project.md`](./project.md) | Contexto do repositorio | Onde o projeto esta e o que ja existe |
| 2 | [`prd.md`](./prd.md) | Produto | Visao, personas, escopo, RNF, metricas, riscos |
| 3 | [`specs/.../spec.md`](./specs/painel-acompanhamento-pedidos/spec.md) | Normativo | Requisitos e regras de negocio (o "o que") |
| 4 | [`specs/.../*.feature`](./specs/painel-acompanhamento-pedidos/us21-acompanhar-pedidos.feature) | Testavel | Cenarios Gherkin (fonte da verdade do comportamento) |
| 5 | [`specs/.../design.md`](./specs/painel-acompanhamento-pedidos/design.md) | Tecnico | Como: padroes de projeto e diagramas de classe UML |
| 6 | [`specs/.../state-machines.md`](./specs/painel-acompanhamento-pedidos/state-machines.md) | Tecnico | Como (dinamica): maquinas de estado UML |
| 7 | [`AGENTS.md`](./AGENTS.md) | Regras | Convencoes obrigatorias de codigo e processo |

## Fluxo de artefatos (OpenSpec)

`proposal -> specs -> design -> tasks -> implement`: a proposta diz **por que**, a
especificacao diz **o que**, o design diz **como**, as tarefas dizem **os passos**
e a implementacao **executa**. Ver a change arquivada em
[`changes/archive/2026-10-06-painel-pedidos-mvp`](./changes/archive/2026-10-06-painel-pedidos-mvp/proposal.md).

## Mapa DRY (onde cada assunto vive)

| Assunto | Unico lar | Referenciado por |
|---|---|---|
| Contexto e tecnologia | `project.md` | `prd.md`, `design.md` |
| Visao de produto, escopo, RNF, metricas, riscos | `prd.md` | `spec.md` (escopo), `design.md` (RNF tecnicos) |
| Requisitos e regras de negocio (BR12, BR13, US23) | `spec.md` | `prd.md`, `design.md`, features |
| Cenarios de comportamento (Gherkin) | `*.feature` | `spec.md` (por tag), `design.md` (por link) |
| Padroes de projeto e diagramas de classe UML | `design.md` | `state-machines.md`, `spec.md` |
| Maquinas de estado UML | `state-machines.md` | `design.md` (por link) |
| Decisoes tecnicas da mudanca entregue | `changes/archive/.../design.md` | `design.md` |
| Regras e convencoes de codigo | `AGENTS.md` | todos |

## Historias de usuario (fonte testavel)

| Historia | Feature | Cenarios |
|---|---|---|
| US21 - Acompanhar pedidos | [`us21-acompanhar-pedidos.feature`](./specs/painel-acompanhamento-pedidos/us21-acompanhar-pedidos.feature) | `@US21-001`..`018` |
| US22 - Filtrar por periodo e data | [`us22-filtro-periodo-data.feature`](./specs/painel-acompanhamento-pedidos/us22-filtro-periodo-data.feature) | `@US22-001`..`004` |
| US23 - Simular novos pedidos | [`us23-simulacao-novos-pedidos.feature`](./specs/painel-acompanhamento-pedidos/us23-simulacao-novos-pedidos.feature) | `@US23-001`..`005` |
| US24 - Prioridade, SLA e ordenacao | [`us24-prioridade-sla-ordenacao.feature`](./specs/painel-acompanhamento-pedidos/us24-prioridade-sla-ordenacao.feature) | `@US24-001`..`007` |
| US25 - Instancia unica | [`us25-instancia-unica.feature`](./specs/painel-acompanhamento-pedidos/us25-instancia-unica.feature) | `@US25-001` |

## Convencoes

- Identificadores em portugues sem acentos; classes no singular; interfaces com
  prefixo `I`; padroes com sufixo (`State`, `Handler`, `Service`, `Strategy`,
  `Observer`, `Repository`).
- Referencias bibliograficas (ABNT) ficam em
  [`specs/.../design.md#referencias`](./specs/painel-acompanhamento-pedidos/design.md).
