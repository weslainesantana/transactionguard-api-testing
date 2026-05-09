# Mantendo e Melhorando Cobertura de Testes

## 🎯 Objetivo

Manter cobertura de testes acima das metas estabelecidas e melhorar continuamente.

## 📊 Metas de Cobertura

```
Services:        80%+  ████████░░
Controllers:     70%+  ███████░░░
Domain/Rules:    90%+  █████████░
Repositories:    75%+  ███████░░░
General:         70%+  ███████░░░
```

## 🔍 Analisando Cobertura

### Gerar Relatório Jacoco

```bash
# Rodar testes e gerar cobertura
mvn clean test

# Abrir relatório
# Windows
start target\site\jacoco\index.html

# macOS
open target/site/jacoco/index.html

# Linux
xdg-open target/site/jacoco/index.html
```

### Entender o Relatório

**Verde (100%)**: Todas as linhas cobertas
**Amarelo (Parcial)**: Algumas linhas não cobertas
**Vermelho (<50%)**: Maioria não coberta

### Identificar Gaps

1. Abrir relatório Jacoco
2. Clicar em pacote (ex: `service`)
3. Clicar em classe que está abaixo da meta
4. Ver linhas não cobertas (marcadas em vermelho)

## 📈 Estratégia por Tipo

### 1️⃣ Services (Alvo: 80%)

**Cobertas:**
- ✅ Fluxo feliz
- ✅ Validações
- ✅ Exceptions comuns

**Pode ignorar:**
- ❌ Logs (não críticos)
- ❌ Getters/Setters simples
- ❌ Config beans

**Exemplo:**
```java
@Test
void testTransferValidAmount() { }

@Test
void testTransferInvalidAmount() { }

@Test
void testTransferInsufficientFunds() { }

@Test
void testTransferSameAccount() { }

@Test
void testTransferSuccess() { }
// = ~80% com 5 testes focados
```

### 2️⃣ Controllers (Alvo: 70%)

**Cobertas:**
- ✅ HTTP 200/201
- ✅ HTTP 400 (validação)
- ✅ HTTP 404
- ✅ Headers importantes

**Pode ignorar:**
- ❌ HTTP 500 (erros raros)
- ❌ Logging
- ❌ Documentação Swagger

**Exemplo:**
```java
@Test
void testTransferSuccess() { }

@Test
void testTransferBadRequest() { }

@Test
void testTransferNotFound() { }
// = ~70% com 3 testes
```

### 3️⃣ Domain/Rules (Alvo: 90%)

**Cobertas:**
- ✅ Todas as regras críticas
- ✅ Validações obrigatórias
- ✅ Transições de estado

**Exemplo:**
```java
class Transaction {
    public void approve() {
        if (status != PENDING) throw new InvalidStateException();
        status = APPROVED;
    }
}

@Test
void testApproveFromPending() { }

@Test
void testApproveFail_InvalidState() { }
// = 100% - pois é crítico
```

### 4️⃣ Repositories (Alvo: 75%)

**Cobertas:**
- ✅ Save, Update, Delete
- ✅ Queries customizadas
- ✅ Find by ID

**Pode ignorar:**
- ❌ Métodos herdados (count, exists)
- ❌ Paging (padrão Spring)

**Exemplo:**
```java
@Test
void testFindByAccountId() { }

@Test
void testSaveTransaction() { }

@Test
void testUpdateTransaction() { }
// = ~75% com 3 testes
```

## ⚡ Aumentando Cobertura Rapidamente

### 1. Identificar Classes Críticas

```bash
# Gerar relatório
mvn clean test

# Verificar cada pacote:
# src/main/java/com/transactionguard/service/ → 75%
# src/main/java/com/transactionguard/controller/ → 60%
# src/main/java/com/transactionguard/domain/ → 85%
```

### 2. Focar em Gaps

Exemplo: Controller em 60%, alvo 70%

**Linhas não cobertas:**
- L42: Error handling
- L58: Header setter
- L71: Logging

**Adicionar testes:**
```java
@Test
void testErrorHandling() { }

@Test
void testResponseHeaders() { }
// Pronto! 70%
```

### 3. Rodar Incrementalmente

```bash
# Testes + cobertura
mvn clean test

# Verificar resultado
# Se aumentou → bom!
# Se não subiu → testes não testam código novo
```

## 🚨 Evitando Queda de Cobertura

### Checklist Pré-Commit

- [ ] Novo código tem testes?
- [ ] Cobertura aumentou ou manteve?
- [ ] `mvn verify` passa?
- [ ] Jacoco report OK?

### Git Hook Automático

```bash
# .git/hooks/pre-commit

#!/bin/bash
mvn clean test
if [ $? -ne 0 ]; then
  echo "❌ Testes falharam!"
  exit 1
fi

# Verificar cobertura
COVERAGE=$(grep -oP '(?<=line coverage: )\d+\.' target/site/jacoco/index.html | head -1)
if (( $(echo "$COVERAGE < 70" | bc -l) )); then
  echo "❌ Cobertura abaixo de 70%!"
  exit 1
fi

echo "✅ Testes e cobertura OK"
```

## 📈 Melhorando Continuamente

### Meta Trimestral

| Trimestre | Services | Controllers | Domain | Geral |
|-----------|----------|-------------|--------|-------|
| Q1 2024 | 75% | 65% | 85% | 70% |
| Q2 2024 | 78% | 68% | 88% | 72% |
| Q3 2024 | 80% | 70% | 90% | 74% |
| Q4 2024 | 82% | 72% | 92% | 76% |

### Ações Mensais

1. **Primeira semana**: Analisar relatório Jacoco
2. **Segunda semana**: Identificar gaps principais
3. **Terceira semana**: Adicionar testes
4. **Quarta semana**: Revisar e consolidar

## 🔗 Integrando com CI/CD

### GitHub Actions

```yaml
- name: Verify Coverage
  run: mvn verify

- name: Report Coverage
  uses: codecov/codecov-action@v3
  with:
    files: ./target/site/jacoco/jacoco.xml
```

**Falha se:**
- Services < 80%
- Controllers < 70%
- Domain < 90%
- Repositories < 75%
- Geral < 70%

## 💡 Dicas Finais

### Testes Bons vs Ruins

**❌ Ruim:**
```java
@Test
void testTransfer() {
    // Um teste gigante que testa tudo
    service.transfer(...);
    service.verify(...);
    service.approve(...);
    // Se falha, qual é o problema?
}
```

**✅ Bom:**
```java
@Test
void testTransferWithValidAmount() { }

@Test
void testTransferWithInvalidAmount() { }

@Test
void testTransferApproval() { }
// Cada teste testa uma coisa
```

### Evitar Testes Frágeis

```java
// ❌ Frágil (dependente de ordem)
@Test
void testAfterTransfer() {
    repository.save(transaction); // depende de testes anteriores!
}

// ✅ Resiliente (independente)
@Test
void testTransaction() {
    transaction = new Transaction(...);
    repository.save(transaction);
    assertThat(repository.findById(id)).isPresent();
}
```

## 📞 Referências

- [Jacoco Guide](https://www.jacoco.org/jacoco/trunk/doc/index.html)
- [JUnit 5 Best Practices](https://junit.org/junit5/docs/current/user-guide/)
- [Code Coverage in CI/CD](https://codecov.io/docs/)

---

**Lembre-se:** Cobertura de testes é sobre **qualidade**, não quantidade!
Uma cobertura bem planejada vale mais que 100%.
