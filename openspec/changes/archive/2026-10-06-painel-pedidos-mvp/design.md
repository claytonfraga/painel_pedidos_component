# Pedido Seeder

> Plan from this document. Each slice below carries its own shape - copy it, do not re-derive it.
> Status: confirmed by clayton, 2026-10-06

## Situation

- Project: in active construction.
- Decision: open, now committed by clayton - keep persistence in memory, extract the seed.
- In flight: the State lifecycle (`PedidoState`/`Pedido*State`) and the project code rules are the precedent; task decomposition stays out.
- At stake: reverted in an afternoon - a POC, no data to migrate.

## Problem

Construction. The panel's period filter (Hoje / últimos 7 / últimos 30 / Todos) is under-exercised because the seed is almost entirely today/yesterday, and nobody can hand-write the 100+ orders needed to populate all six columns across several months. What stalls without this: demonstrating the filter and the columns with variety. Why now: `IPedidoRepository` is the only persistence seam, so moving the seed is cheap. The number that would have changed the decision - "is real persistence a delivery requirement?" - is unmeasured; the maintainer said no.

## Success

- Worked if: one run populates all six columns and the four period options return visibly different, reproducible sets.
- Going wrong: the period options return identical sets, or the data changes between runs.
- Review: after the first run - clayton.

## Boundary

In: a deterministic seeder producing the order dataset, and the repository consuming it.

Out: H2/SQL persistence - no delivery requirement, adds a driver, DDL and connection/transaction lifecycle for no measured gain. Out: data surviving restarts.

Unchanged: `IPedidoRepository`'s contract, the Presenter, the View, and the State lifecycle.

## Shape

The record is `Pedido`, seeded by a dedicated `PedidoSeeder` that returns the full dataset; the repository stops creating orders and only stores the injected list. The seeder is deterministic: it derives every date and value from an injected reference date, never from `Random` or the system clock. The one-way door is the persistence seam - `IPedidoRepository` - and it stays in memory, so switching to a database later costs one new implementation and nothing else. The heavier alternative is an H2-backed repository with plain JDBC; it wins only if the delivery requires a real database or persistence across runs, which it does not.

## Key decisions

1. **No new runtime dependency; persistence stays in memory behind `IPedidoRepository`.** Reversible by swapping the implementation only; the POC keeps zero dependencies.
2. **Seeding is extracted to a dedicated `PedidoSeeder`, and the repository no longer constructs orders.** Single responsibility: the repository persists, the seeder produces data.
3. **The seeder is deterministic, fed by an injected reference date.** Same run, same data; the period filter becomes verifiable and no business rule reads the system clock or `Random`.
4. **Situations are produced by driving the domain transitions, not by setting strings.** The seeder never names a status; it calls the operations and the State rules apply.
5. **The seed quantity is configuration read from `.env` (`SEED_QUANTIDADE`), with no new dependency.** The entry point reads it (`LeitorEnv`) and injects it; an absent file falls back to the 1000 default so the app stays runnable.

## Work

| Slice | Delivers | Status |
|---|---|---|
| [PedidoSeeder](#pedidoseeder) | A deterministic `List<Pedido>` of `SEED_QUANTIDADE` orders (from `.env`, default 1000) across six states and varied months/day-of-month. | clear |
| [PedidoEmMemoriaRepository](#pedidoemmemoriarepository) | A repository seeded from an injected list, with no inline order creation. | clear |

Order: PedidoSeeder -> PedidoEmMemoriaRepository.

Already handled by existing code: none.

Derivable from the repository, left to the plan: identifiers, packages, class and field naming, the unchecked-exception and no-static rules - all as the existing records do them.

### PedidoSeeder

**Delivers** the full dataset, computed from an injected reference date. **Status: clear.**

| State | What should happen | Caller sees |
|---|---|---|
| Novo | orders exist with no payment | column Novo populated |
| Em preparo | payment registered | column Em preparo populated |
| Pronto para entrega | advanced after payment | column Pronto para entrega populated |
| Em rota de entrega | advanced after ready | column Em rota de entrega populated |
| Entregue | advanced after dispatch | column Entregue populated |
| Cancelado | cancelled, with credit when paid | column Cancelado populated, credit shown when applicable |

Dates spread across today, the last 7 days, the last 30 days and earlier months.

### PedidoEmMemoriaRepository

**Delivers** a repository that stores the seeded list and generates the next identifier from it. **Status: clear.**

| State | What should happen | Caller sees |
|---|---|---|
| Any | the injected list is returned unchanged by the read | the same orders the seeder produced |

Alternatives considered: H2-backed repository - wins if a real database or cross-run persistence is required; not today.
