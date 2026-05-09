# Testes de API

## 📌 Propósito

Testes de API validam endpoints REST com servidor real.

## 📂 Estrutura

```
api/
├── auth/
│   └── AuthenticationControllerApiTest.java
├── transaction/
│   └── TransactionControllerApiTest.java
├── account/
│   └── AccountControllerApiTest.java
└── fixtures/
    └── TestDataFixtures.java
```

## ✅ Características

- ✅ Testa endpoints REST
- ✅ Valida status HTTP
- ✅ Valida payloads JSON
- ✅ Headers e autenticação
- ✅ Usa Rest Assured

## 🛠️ Rest Assured

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TransactionControllerApiTest {
    
    @LocalServerPort
    private int port;
    
    @Test
    void testTransferEndpoint() {
        given()
            .contentType(JSON)
            .body(request)
        .when()
            .post("/api/transactions/transfer")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));
    }
}
```

## 🎯 Cobertura Alvo

- **Controllers**: 70%+
- **Endpoints**: 100% de caminho feliz
- **Error handling**: Casos principais

## 🚀 Como Executar

```bash
# Todos os testes de API
mvn test -Dtest=*ApiTest

# Teste específico
mvn test -Dtest=TransactionControllerApiTest

# Verbose
mvn test -Dtest=*ApiTest -X
```

## 📊 Validações Comuns

### Status HTTP

```java
.statusCode(200)
.statusCode(400)
.statusCode(401)
.statusCode(404)
.statusCode(500)
```

### JSON Response

```java
.body("id", notNullValue())
.body("name", equalTo("John"))
.body("balance", greaterThan(0))
.body("transactions", hasSize(3))
```

### Headers

```java
.header("Content-Type", containsString("application/json"))
.header("Authorization", notNullValue())
```

### Timing

```java
.time(lessThan(1000L))  // < 1s
```

## 🔐 Autenticação

```java
@Test
void testAuthenticatedEndpoint() {
    given()
        .header("Authorization", "Bearer " + token)
        .contentType(JSON)
    .when()
        .get("/api/account/balance")
    .then()
        .statusCode(200);
}
```

## 📋 Checklist de Testes

Para cada endpoint, teste:

- ✅ Sucesso (200/201)
- ✅ Validação (400)
- ✅ Não autenticado (401)
- ✅ Não autorizado (403)
- ✅ Não encontrado (404)
- ✅ Conflito (409)
- ✅ Erro servidor (500)

## 💡 Boas Práticas

### Use Fixtures

```java
@BeforeEach
void setUp() {
    user = TestDataFixtures.createUser();
    account = TestDataFixtures.createAccount(user);
    token = TestDataFixtures.generateToken(user);
}
```

### Organize por Endpoint

```
api/
├── transaction/
│   ├── TransactionTransferTest.java
│   ├── TransactionHistoryTest.java
│   └── TransactionGetTest.java
└── account/
    ├── AccountBalanceTest.java
    └── AccountDetailsTest.java
```

### Teste Fluxos Realistas

```java
@Test
@DisplayName("Complete transfer flow")
void testCompleteTransferFlow() {
    // 1. Criar usuário
    var user = createUser();
    
    // 2. Login
    var token = authenticate(user);
    
    // 3. Transferir
    var response = transfer(token, request);
    
    // 4. Validar histórico
    var history = getTransactionHistory(token);
    assertThat(history).contains(response);
}
```

## 📝 Exemplo Completo

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class TransactionApiTest {
    
    @LocalServerPort
    private int port;
    
    @Test
    void testTransferFlow() {
        var request = new TransferRequest(1L, 2L, BigDecimal.TEN);
        
        given()
            .baseUri("http://localhost:" + port)
            .contentType(JSON)
            .body(request)
        .when()
            .post("/api/transactions/transfer")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("status", equalTo("SUCCESS"));
    }
}
```

## 🔗 Referências

- [Rest Assured Docs](https://rest-assured.io/)
- [Hamcrest Matchers](http://hamcrest.org/JavaHamcrest/javadoc/1.3/)
- [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
