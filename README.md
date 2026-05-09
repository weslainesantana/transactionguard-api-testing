# TransactionGuard

TransactionGuard é um projeto backend focado em automação de testes e qualidade de software para APIs financeiras.

O projeto simula operações de transação financeira e foi desenvolvido para demonstrar boas práticas de testes automatizados, cobertura de código, integração contínua e organização de projetos backend com Java e Spring Boot.

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Maven
- JUnit 5
- Mockito
- Rest Assured
- Jacoco
- Allure Reports
- Docker
- GitHub Actions

## Estrutura do Projeto

```
src/
├── main/java/com/transactionguard/
│   ├── domain/
│   ├── dto/
│   ├── exception/
│   ├── repository/
│   └── service/
│
└── test/
	├── api/
	├── integration/
	└── unit/
```

## Funcionalidades

- Simulação de transferências financeiras
- Regras de validação de transações
- Tratamento de exceções
- Camada de serviço para operações financeiras
- Estrutura de testes organizada por tipo

## Testes Automatizados

O projeto possui:

- Testes unitários
- Testes de integração
- Testes de API
- Validação de exceptions
- Cobertura de código com Jacoco
- Relatórios Allure

## Cobertura de Código

O projeto utiliza Jacoco para geração de relatórios de cobertura.

Executar cobertura:

```bash
mvn clean test jacoco:report
```

Relatório gerado em:

```
target/site/jacoco/index.html
```

## Executando os Testes

Executar testes:

```bash
mvn test
```

Executar verificação completa:

```bash
mvn verify
```

Gerar relatório Allure:

```bash
mvn allure:report
```

## Docker

O projeto possui configuração Docker para ambiente de execução e testes.

Arquivos disponíveis:

- docker/Dockerfile
- docker-compose.yml

## CI/CD

O pipeline automatizado com GitHub Actions executa:

- Build da aplicação
- Execução dos testes
- Verificação de cobertura
- Geração de artefatos

Workflow disponível em:

.github/workflows/build.yml


