# Testes de Integração

## 📌 Propósito

Testes de integração validam a interação entre componentes com infraestrutura real (BD, cache, etc).

## 📂 Estrutura

```
integration/
├── repository/
│   ├── TransactionRepositoryIntegrationTest.java
│   ├── AccountRepositoryIntegrationTest.java
│   └── UserRepositoryIntegrationTest.java
├── service/
│   └── TransactionServiceIntegrationTest.java
└── config/
    └── TestcontainersConfiguration.java
```

## ✅ Características

- ✅ Usa TestContainers (PostgreSQL)
- ✅ Testa interação com BD real
- ✅ Mais lentos (100-500ms)
- ✅ Determinísticos com isolamento
- ✅ Sem flakiness com containers

## 🐘 TestContainers + PostgreSQL

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
class TransactionRepositoryIntegrationTest {
    
    @Autowired
    private TransactionRepository repository;
    
    @Test
    void testSaveTransaction() {
        // BD real via TestContainers
        var saved = repository.save(transaction);
        assertThat(saved.getId()).isNotNull();
    }
}
```

## 🎯 Cobertura Alvo

- **Repositories**: 75%+
- **Banco de Dados**: Queries validadas

## 🚀 Como Executar

```bash
# Todos os testes de integração
mvn verify -Dtest=*IntegrationTest

# Teste específico
mvn verify -Dtest=TransactionRepositoryIntegrationTest

# Sem testes de integração
mvn test -DskipTests=*IntegrationTest
```

## ⚙️ Requisitos

- Docker instalado e rodando
- `docker ps` deve retornar sucesso

Se Docker não está disponível:
- Testes de integração serão **skipped** automaticamente
- Testes unitários continuam rodando

## 📊 Gerar Cobertura

```bash
mvn clean verify
# Abrir: target/site/jacoco/index.html
```

## 🔄 Fluxo de BD com Flyway

Os testes usam Flyway para schema de teste:

```
db/
└── migration/
    └── V1__Initial_Schema.sql
    └── V2__Add_Indexes.sql
```

Cada teste começa com schema limpo!

## 💡 Dicas

- Prepare dados específicos em `@BeforeEach`
- Limpe dados em `@AfterEach` se necessário
- Use `@Transactional` para auto-rollback
- Compartilhe container entre testes para acelerar
