# Guia de Configuração

## Ambiente de Desenvolvimento

### Instalação de Dependências

1. **Java Development Kit (JDK)**
   - Versão mínima: Java 17
   - Download: https://www.oracle.com/java/technologies/javase-downloads.html

2. **Apache Maven**
   - Versão mínima: Maven 3.8
   - Download: https://maven.apache.org/download.cgi

3. **Docker (opcional)**
   - Download: https://www.docker.com/products/docker-desktop

### Configuração Inicial

```bash
# Clonar o repositório
git clone <repository-url>
cd transaction-guard

# Instalar dependências
mvn install

# Compilar o projeto
mvn clean compile
```

### Rodando Testes

```bash
# Testes unitários
mvn test

# Todos os testes (incluindo integração)
mvn verify
```

## Docker

### Build da imagem

```bash
docker build -f docker/Dockerfile -t transaction-guard:latest .
```

### Executar com Docker Compose

```bash
docker-compose up --build
```
