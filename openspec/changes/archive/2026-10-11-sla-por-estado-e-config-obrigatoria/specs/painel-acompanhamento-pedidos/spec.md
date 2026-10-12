# Spec delta - SLA por estado e configuracao obrigatoria

## MODIFIED Requirements

### Requirement: Prioridade por SLA
Cada estado com SLA tem um limite fixo, em minutos, lido do `painel_pedidos.env`:
Novo (`SLA_NOVO_MINUTOS`), Em preparo (`SLA_EM_PREPARO_MINUTOS`), Pronto para
entrega (`SLA_PRONTO_PARA_ENTREGA_MINUTOS`) e Em rota de entrega
(`SLA_EM_ROTA_DE_ENTREGA_MINUTOS`). Entregue e Cancelado nao tem SLA. A media por
bairro deixa de existir.

#### Scenario: Priorizar pelo SLA do estado
- **WHEN** um pedido ultrapassa o SLA em minutos do seu estado
- **THEN** recebe o label vermelho "Prioridade"

#### Scenario: Sem media por bairro
- **WHEN** o SLA e avaliado
- **THEN** usa o valor fixo do estado
- **AND** nao existe calculo por bairro

### Requirement: Configuracao por `painel_pedidos.env`
O arquivo passa a ser **obrigatorio** no uso do componente, e as quatro chaves de
SLA por estado tambem. Ausentes, o componente lanca excecao.

#### Scenario: Arquivo ausente
- **WHEN** `painel_pedidos.env` nao existe
- **THEN** o componente lanca excecao e nao inicia

#### Scenario: Chave de SLA ausente
- **WHEN** falta uma chave de SLA por estado
- **THEN** o componente lanca excecao

## ADDED Requirements

### Requirement: Modelo abstrato sem toString
`PedidoAbstract` nao define `toString`.

#### Scenario: Sem toString
- **WHEN** `PedidoAbstract` e inspecionado
- **THEN** nao possui override de `toString`
