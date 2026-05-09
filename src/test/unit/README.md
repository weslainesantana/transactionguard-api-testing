# Testes Unitários

## 📌 Propósito

Testes unitários validam componentes isolados sem dependências externas.

## 📂 Estrutura

```
unit/
├── service/
│   ├── TransactionServiceTest.java
│   ├── AccountServiceTest.java
│   └── AuthenticationServiceTest.java
├── repository/
│   └── (não testa aqui - veja integration/)
└── util/
    └── ValidationUtilTest.java
```

## ✅ Características

- ✅ Rápidos (~1-5ms por teste)
- ✅ Determinísticos
- ✅ Sem I/O (BD, HTTP, etc)
- ✅ Usa Mockito para dependências
- ✅ Testa lógica isolada

## 🎯 Cobertura Alvo

- **Services**: 80%+
- **Utils**: 85%+
- **Domain**: 90%+ (quando possível)

## 📝 Exemplo

```java
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository repository;
    
    @InjectMocks
    private TransactionService service;
    
    @Test
    void testValidTransfer() {
        // Arrange
        when(repository.save(any())).thenReturn(transaction);
        
        // Act
        var result = service.transfer(request);
        
        // Assert
        assertThat(result).isNotNull();
    }
}
```

## 🚀 Como Executar

```bash
# Todos os testes unitários
mvn test

# Teste específico
mvn test -Dtest=TransactionServiceTest

# Com nome de método
mvn test -Dtest=TransactionServiceTest#testValidTransfer
```

## 📊 Gerar Cobertura

```bash
mvn clean test
# Abrir: target/site/jacoco/index.html
```
