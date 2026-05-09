# Guia de Segurança

## 🔐 Informações Sensíveis

Este projeto é um **demo de testes** e não contém dados reais sensíveis. 

Quando implementar a API em produção, siga este checklist:

## ❌ NUNCA Faça Commit De:

```
❌ Senhas de banco de dados
❌ Chaves JWT privadas
❌ Chaves de API (AWS, Google, etc)
❌ Tokens de acesso
❌ Credenciais de email SMTP
❌ Informações de contas bancárias
❌ PII (Personally Identifiable Information)
```

## ✅ Sempre Configure:

### 1. Arquivo `.env.example`
```bash
# Commit este arquivo COM VALORES FAKE
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/transactionguard
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=changeme
JWT_SECRET=minimum-32-characters-secret-key
JWT_EXPIRATION=86400000
```

### 2. Arquivo `.env.local` (adicionar ao .gitignore)
```bash
# NÃO COMMIT - Use valores REAIS apenas localmente
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/transactionguard
SPRING_DATASOURCE_USERNAME=prod_admin
SPRING_DATASOURCE_PASSWORD=actual_secure_password_!@#$%
JWT_SECRET=actual_very_long_secret_key_with_special_chars_!@#$%
JWT_EXPIRATION=86400000
```

### 3. Application.properties (adicionar ao .gitignore)
```properties
# NÃO COMMIT
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
```

## 🔑 Boas Práticas

### Variáveis de Ambiente
```bash
# Use sempre variáveis de ambiente em produção
export SPRING_DATASOURCE_PASSWORD="senha_segura"
java -jar app.jar
```

### Docker Secrets (Swarm/Kubernetes)
```yaml
# docker-compose.yml
version: '3.8'
services:
  app:
    environment:
      DB_PASSWORD_FILE: /run/secrets/db_password
    secrets:
      - db_password

secrets:
  db_password:
    file: ./secrets/db_password.txt  # Não fazer commit!
```

### CI/CD (GitHub Actions)
```yaml
# .github/workflows/deploy.yml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Deploy
        env:
          DB_PASSWORD: ${{ secrets.DB_PASSWORD }}  # GitHub Secrets
          JWT_SECRET: ${{ secrets.JWT_SECRET }}
        run: ./deploy.sh
```

## 🚨 Se Vazar Credenciais

1. **Adicione ao `.gitignore`** se ainda não estiver
2. **Reverta o commit** com:
   ```bash
   git reset --soft HEAD~1
   git checkout -- arquivo_sensivel.txt
   git commit -m "Remove sensitive file"
   ```
3. **Rotacione credenciais** imediatamente
4. **Use `git-filter-branch`** para remover do histórico (se já foi commitado)

## 📋 Checklist Segurança

- [ ] `.env.local` está em `.gitignore`
- [ ] `.env.*.local` está em `.gitignore`
- [ ] `application-*.properties` (exceto exemplo) estão ignorados
- [ ] Sem chaves/certificados commitados
- [ ] `.git-crypt` ou `git-secrets` instalado (opcional)
- [ ] Branch protection ativado (pull requests obrigatórios)
- [ ] Code review antes de merge

## 🔗 Referências

- [OWASP: Secrets Management](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [GitHub: Encrypted Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [12Factor App: Config](https://12factor.net/config)
- [AWS: Secrets Manager](https://aws.amazon.com/secrets-manager/)
