# Estratégia de Testes e Cobertura

## 📋 Visão Geral

TransactionGuard segue uma estratégia robusta de testes com foco em qualidade e cobertura de código. O projeto utiliza:

- **JUnit 5** - Framework de testes
- **Mockito** - Mocks e stubs
- **Rest Assured** - Testes de API
- **TestContainers** - Ambientes de teste isolados
- **Jacoco** - Medição de cobertura de código
- **Allure** - Relatórios de testes

## 🏗️ Estrutura de Testes

```
src/test/
├── unit/              # Testes unitários
├── integration/       # Testes de integração
└── api/               # Testes de API REST
```

### Testes Unitários (`unit/`)

Testam componentes isolados sem dependências externas:

```java
// Exemplo: TransactionServiceTest.java
@Test
void testTransferSuccess() {
    // Arrange
    TransactionRequest request = new TransactionRequest(...);
    
    // Act
    TransactionResponse response = transactionService.transfer(request);
    
    // Assert
    assertThat(response.isSuccess()).isTrue();
}
```

### Testes de Integração (`integration/`)

Testam a interação entre componentes com banco de dados real (TestContainers):

```java
// Exemplo: TransactionRepositoryIntegrationTest.java
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class TransactionRepositoryIntegrationTest {
    
    @Test
    void testFindByAccountId() {
        // Testa interação com PostgreSQL
    }
}
```

### Testes de API (`api/`)

Testam endpoints REST com Rest Assured:

```java
// Exemplo: TransactionControllerApiTest.java
@Test
void testTransferEndpoint() {
    given()
        .contentType(ContentType.JSON)
        .body(transferRequest)
    .when()
        .post("/api/transactions/transfer")
    .then()
        .statusCode(200)
        .body("success", equalTo(true));
}
```

## 📊 Metas de Cobertura

### Configuração Jacoco

O projeto está configurado com as seguintes metas:

| Componente | Camada | Cobertura |
|-----------|--------|-----------|
| **Domain** | Regras de Negócio | 90%+ |
| **Services** | Lógica de Aplicação | 80%+ |
| **Repositories** | Acesso a Dados | 75%+ |
| **Controllers** | Endpoints REST | 70%+ |
| **Geral** | Projeto Todo | 70%+ |

### Por quê dessas metas?

- **90% (Domain)**: Regras de negócio são críticas - devem ter máxima cobertura
- **80% (Services)**: Lógica de aplicação merece cobertura alta
- **75% (Repositories)**: Queries SQL são testadas pela integração com BD
- **70% (Controllers)**: Endpoints REST focam em fluxo, não em lógica pura
- **70% (Geral)**: Assegura qualidade global sem perfecionismo

## 🔍 Executando Testes

### Testes Unitários

```bash
# Rodar apenas testes unitários
mvn test -Dtest=*Test

# Com padrão específico
mvn test -Dtest=TransactionServiceTest
```

### Testes de Integração

```bash
# Rodar testes de integração
mvn verify -Dtest=*IntegrationTest
```

### Todos os Testes

```bash
# Rodar todos os testes (unit + integration + API)
mvn clean verify
```

## 📈 Gerando Relatórios

### Jacoco (Cobertura de Código)

```bash
# Gerar relatório
mvn clean test

# Abrir no navegador
# Windows
start target\site\jacoco\index.html

# macOS
open target/site/jacoco/index.html

# Linux
xdg-open target/site/jacoco/index.html
```

**Localização**: `target/site/jacoco/index.html`

O relatório mostra:
- Cobertura por classe
- Cobertura por pacote
- Linhas cobertas vs não cobertas
- Branches cobertos

### Allure (Relatórios Visuais)

```bash
# Gerar relatório Allure
mvn allure:report

# Servir relatório (abre no navegador automaticamente)
mvn allure:serve
```

**Localização**: `target/site/allure-maven-plugin/`

O relatório mostra:
- Panorama dos testes
- Histórico de execução
- Testes falhados
- Timeline de execução

## 🔄 Pipeline CI/CD

O GitHub Actions executa automaticamente:

```yaml
1. Checkout do código
2. Setup do Java 21
3. Build do projeto (mvn clean install)
4. Testes com Jacoco (mvn verify)
5. Upload de cobertura (Codecov)
6. Geração de relatório Allure
7. Armazenamento de artefatos
```

### Verificação de Cobertura

A build **falha** se:
- Services < 80%
- Controllers < 70%
- Domain < 90%
- Repositories < 75%
- Geral < 70%

## ✅ Checklist de Qualidade

Antes de fazer commit:

- [ ] Todos os testes passam: `mvn clean verify`
- [ ] Cobertura atende às metas: `mvn test`
- [ ] Sem erros de compilação
- [ ] Jacoco report gerado com sucesso
- [ ] Novos testes incluídos para novas features

## 📚 Boas Práticas

### Nomenclatura de Testes

```java
// ✅ Bom
testTransferSuccessWithValidAccount()
testTransferFailsWithInsufficientFunds()
testFindTransactionByIdReturnNull()

// ❌ Evitar
test1()
testIt()
transactionTest()
```

### AAA Pattern (Arrange, Act, Assert)

```java
@Test
void testTransfer() {
    // ARRANGE - Setup inicial
    Account source = createAccount(1000);
    Account target = createAccount(0);
    
    // ACT - Executa ação
    transactionService.transfer(source, target, 100);
    
    // ASSERT - Valida resultado
    assertThat(source.getBalance()).isEqualTo(900);
    assertThat(target.getBalance()).isEqualTo(100);
}
```

### Evitar Testes Acoplados

```java
// ❌ Evitar
@Test
void testCompleteFlow() {
    createUser(); // testa criação
    login();      // testa login
    transfer();   // testa transferência
    // Muito acoplado, difícil de debugar
}

// ✅ Fazer
@Test
void testCreateUser() { /* apenas criação */ }

@Test
void testLogin() { /* apenas login */ }

@Test
void testTransfer() { /* apenas transferência */ }
```

## 🐛 Troubleshooting

### Jacoco não gera relatório

```bash
# Certifique-se de que os testes rodaram
mvn clean test

# Se persistir, force a geração
mvn jacoco:report
```

### Testes falhando no CI mas passando localmente

Verifique:
- Java version match: `java -version`
- Variáveis de ambiente: `echo $JAVA_HOME`
- Dependências Maven: `mvn dependency:tree`

### TestContainers com PostgreSQL

Certifique-se que Docker está rodando:
```bash
docker ps
```

Se Docker não está disponível, os testes de integração serão skipped automaticamente.

## 📞 Referências

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Rest Assured](https://rest-assured.io/)
- [Jacoco Coverage Report](https://www.jacoco.org/jacoco/trunk/doc/)
- [Allure Report](https://docs.qameta.io/allure/)
- [TestContainers](https://www.testcontainers.org/)
