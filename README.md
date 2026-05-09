# TransactionGuard

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Uma API backend financeira desenvolvida para demonstrar boas práticas de desenvolvimento e automação de testes.

## 📋 Sobre o Projeto

TransactionGuard é um projeto que simula operações bancárias básicas, incluindo autenticação JWT, consulta de saldo, transferências e histórico de transações.

Além da implementação da API, o foco principal está na **qualidade do software**, utilizando testes automatizados, integração contínua, relatórios de cobertura e ambiente containerizado com Docker.

## 🛠️ Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot** - Framework web
- **PostgreSQL** - Banco de dados
- **Flyway** - Controle de versão de banco de dados
- **JWT** - Autenticação segura
- **Docker** - Containerização
- **JUnit 5** - Framework de testes
- **Rest Assured** - Testes de API
- **Jacoco** - Relatórios de cobertura
- **Allure Reports** - Relatórios visuais
- **GitHub Actions** - CI/CD

## ✨ Funcionalidades

- ✅ Cadastro de usuários
- ✅ Login com JWT
- ✅ Consulta de saldo
- ✅ Transferências entre contas
- ✅ Histórico de transações

## 📦 Estrutura do Projeto

```
transaction-guard/
├── src/
│   ├── main/          # Código-fonte principal
│   └── test/
│       ├── unit/          # Testes unitários
│       ├── integration/    # Testes de integração
│       └── api/            # Testes de API
├── docker/            # Configurações Docker
├── docs/              # Documentação
├── .github/workflows/ # CI/CD workflows
├── docker-compose.yml # Orquestração de containers
├── README.md          # Este arquivo
└── pom.xml            # Configuração Maven
```

## 🧪 Qualidade e Testes

O projeto possui uma estratégia abrangente de testes:

- **Testes Unitários** - Validação de componentes isolados
- **Testes de Integração** - Validação entre componentes
- **Testes de API** - Validação dos endpoints REST
- **Relatórios de Cobertura** - Jacoco para análise de cobertura
- **Pipeline CI** - GitHub Actions para integração contínua
- **Relatórios Allure** - Visualização detalhada de testes

### 📊 Metas de Cobertura de Código

| Camada | Meta de Cobertura | Status |
|--------|-------------------|--------|
| Services | 80%+ | ✅ Configurado |
| Controllers | 70%+ | ✅ Configurado |
| Domain/Regras Críticas | 90%+ | ✅ Configurado |
| Repositories | 75%+ | ✅ Configurado |
| **Cobertura Geral** | **70%+** | ✅ Configurado |

### 🔍 Como Visualizar a Cobertura

```bash
# Gerar relatório Jacoco
mvn clean test

# Abrir relatório no navegador
open target/site/jacoco/index.html
```

### 📈 Relatórios Allure

```bash
# Gerar relatório Allure
mvn allure:report

# Servir relatório localmente
mvn allure:serve
```

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker (opcional)
- PostgreSQL (se rodar localmente)

### Localmente

```bash
# Instalar dependências
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

### Com Docker

```bash
# Construir e executar com Docker Compose
docker-compose up --build
```

### Rodando Testes

```bash
# Testes unitários
mvn test

# Todos os testes (incluindo integração)
mvn verify

# Gerar relatório Allure
mvn allure:report
```

## 🎯 Objetivo

Este projeto visa demonstrar conhecimentos em:

- 💻 Backend Java
- 🔌 APIs REST
- 🤖 Automação de testes
- 🔄 Integração contínua
- ✅ Qualidade de software
- 📚 Boas práticas de desenvolvimento

## 📄 Documentação

- [Guia de Configuração](docs/SETUP.md) - Setup inicial do projeto
- [Estratégia de Testes](docs/TESTING.md) - Estrutura e tipos de testes
- [Cobertura de Código](docs/COVERAGE.md) - Metas e manutenção de cobertura
- [Segurança](docs/SECURITY.md) - Proteção de dados sensíveis

## 📝 Contribuindo

Para contribuir, crie uma branch e envie um pull request.

## 📄 Licença

MIT
