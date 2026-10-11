# Spec delta - Painel maximizado comandado pela Presenter

## ADDED Requirements

### Requirement: Painel inicia sempre maximizado
O painel inicia maximizado. A maximizacao e comandada pela Presenter; a View
apenas executa o comando, permanecendo passiva.

#### Scenario: Janela inicia maximizada
- **WHEN** o painel e iniciado
- **THEN** a Presenter instrui a View a maximizar
- **AND** a janela inicia maximizada

#### Scenario: View nao se maximiza sozinha
- **WHEN** a View e construida sem comando da Presenter
- **THEN** ela nao altera o estado da janela
