# OpenSpec - Painel de Acompanhamento de Pedidos (MVP Passive View)

Este diretorio e a fonte da verdade do comportamento do sistema. Estrutura:

```
openspec/
├── README.md                 # este indice
├── project.md                # contexto, arquitetura, tecnologia, dominio
├── AGENTS.md                 # regras obrigatorias para agentes/pessoas
├── prd.md                    # PRD (produto: o que e por que)
├── references/               # material de referencia indexado (MVP, etc.)
├── specs/
│   └── painel-acompanhamento-pedidos/
│       ├── spec.md           # especificacao normativa da capacidade
│       ├── design.md         # design tecnico (como)
│       └── us21..us25*.feature  # cenarios Gherkin (fonte testavel)
└── changes/
    └── archive/
        └── 2026-10-06-painel-pedidos-mvp/   # change concluida
```

## Ordem de leitura

1. `prd.md` - o produto e o escopo.
2. `specs/painel-acompanhamento-pedidos/spec.md` - comportamento normativo.
3. `specs/painel-acompanhamento-pedidos/*.feature` - cenarios testaveis (Gherkin).
4. `specs/painel-acompanhamento-pedidos/design.md` - design tecnico.
5. `AGENTS.md` - regras e convencoes obrigatorias.

## Historias de usuario

| Historia | Feature | Cenarios |
|---|---|---|
| US21 - Acompanhar pedidos | `us21-acompanhar-pedidos.feature` | `@US21-001`..`018` |
| US22 - Filtrar por periodo e data | `us22-filtro-periodo-data.feature` | `@US22-001`..`004` |
| US23 - Simular novos pedidos | `us23-simulacao-novos-pedidos.feature` | `@US23-001`..`005` |
| US24 - Prioridade, SLA e ordenacao | `us24-prioridade-sla-ordenacao.feature` | `@US24-001`..`007` |
| US25 - Instancia unica | `us25-instancia-unica.feature` | `@US25-001` |

## Convencoes

- `.feature` e a fonte da verdade testavel; `spec.md`/`prd.md` sao normativos.
- Identificadores em portugues sem acentos; classes no singular; interfaces com
  prefixo `I`; padroes com sufixo (`State`, `Handler`, `Service`, `Strategy`,
  `Observer`, `Repository`).
- Decisoes e restricoes em `AGENTS.md`.
