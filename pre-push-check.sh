#!/bin/bash
# Pre-push security verification script
# Execute este script ANTES de fazer push ao GitHub

echo "🔐 TransactionGuard - Pre-Push Security Check"
echo "================================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check 1: .env files
echo "1️⃣  Verificando arquivos .env..."
if [ -f ".env" ]; then
    echo -e "${RED}❌ ERRO: .env existe! Remova imediatamente!${NC}"
    echo "   Faça: rm .env"
    exit 1
else
    echo -e "${GREEN}✅ .env não existe${NC}"
fi

# Check 2: .env.local
if [ -f ".env.local" ]; then
    echo -e "${RED}⚠️  AVISO: .env.local existe (será ignorado por .gitignore)${NC}"
else
    echo -e "${GREEN}✅ .env.local não existe${NC}"
fi

# Check 3: Senhas hardcoded
echo ""
echo "2️⃣  Procurando por senhas hardcoded..."
if grep -r "password" src/main --include="*.properties" --include="*.yml" 2>/dev/null; then
    echo -e "${RED}❌ ERRO: Encontradas palavras-chave 'password' em properties!${NC}"
    exit 1
else
    echo -e "${GREEN}✅ Nenhuma senha em properties/yml${NC}"
fi

# Check 4: Tokens
echo ""
echo "3️⃣  Procurando por tokens hardcoded..."
if grep -r "Bearer " src/main --include="*.java" | grep -v "Bearer\s\${" 2>/dev/null; then
    echo -e "${RED}❌ ERRO: Encontrados tokens Bearer hardcoded!${NC}"
    exit 1
else
    echo -e "${GREEN}✅ Nenhum token Bearer hardcoded${NC}"
fi

# Check 5: .gitignore contains target/
echo ""
echo "4️⃣  Verificando .gitignore..."
if grep -q "^target/" .gitignore; then
    echo -e "${GREEN}✅ target/ está em .gitignore${NC}"
else
    echo -e "${RED}❌ ERRO: target/ não está em .gitignore!${NC}"
    exit 1
fi

if grep -q "^\.env" .gitignore; then
    echo -e "${GREEN}✅ .env está em .gitignore${NC}"
else
    echo -e "${RED}❌ ERRO: .env não está em .gitignore!${NC}"
    exit 1
fi

if grep -q "^secrets/" .gitignore; then
    echo -e "${GREEN}✅ secrets/ está em .gitignore${NC}"
else
    echo -e "${YELLOW}⚠️  AVISO: secrets/ não está em .gitignore${NC}"
fi

# Check 6: No secrets/ directory with files
echo ""
echo "5️⃣  Verificando pasta secrets/..."
if [ -d "secrets" ] && [ ! -z "$(ls -A secrets 2>/dev/null)" ]; then
    echo -e "${YELLOW}⚠️  AVISO: Pasta secrets/ contém arquivos${NC}"
    echo "   Certifique-se de que está em .gitignore"
else
    echo -e "${GREEN}✅ Pasta secrets/ vazia ou não existe${NC}"
fi

# Check 7: No credentials directory
echo ""
echo "6️⃣  Verificando pasta credentials/..."
if [ -d "credentials" ] && [ ! -z "$(ls -A credentials 2>/dev/null)" ]; then
    echo -e "${RED}❌ ERRO: Pasta credentials/ contém arquivos!${NC}"
    exit 1
else
    echo -e "${GREEN}✅ Pasta credentials/ vazia ou não existe${NC}"
fi

# Check 8: Check git status
echo ""
echo "7️⃣  Verificando git status..."
if git status --short 2>/dev/null | grep -E "target/|\.env|secrets/|\.idea/" | grep -v "^?" > /dev/null; then
    echo -e "${RED}❌ ERRO: Arquivos protegidos aparecendo em git status!${NC}"
    echo "   Verifique seu .gitignore"
    exit 1
else
    echo -e "${GREEN}✅ Arquivos protegidos não aparecerão no commit${NC}"
fi

# Check 9: src/main and src/test exist
echo ""
echo "8️⃣  Verificando estrutura do projeto..."
if [ -d "src/main/java" ] && [ -d "src/test/java" ]; then
    echo -e "${GREEN}✅ Estrutura de código OK${NC}"
else
    echo -e "${RED}❌ ERRO: Estrutura de código incompleta!${NC}"
    exit 1
fi

# Check 10: .env.example exists
if [ -f ".env.example" ]; then
    echo -e "${GREEN}✅ .env.example exists${NC}"
else
    echo -e "${YELLOW}⚠️  AVISO: .env.example não existe${NC}"
fi

# Final result
echo ""
echo "================================================"
echo -e "${GREEN}✅ VERIFICAÇÃO COMPLETA - SEGURO PARA PUSH!${NC}"
echo "================================================"
echo ""
echo "Próximos passos:"
echo "  1. git status          (verificar arquivos)"
echo "  2. git add .           (stagear arquivos)"
echo "  3. git commit -m '...' (criar commit)"
echo "  4. git push origin main (fazer push)"
echo ""
