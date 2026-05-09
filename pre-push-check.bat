@echo off
REM Pre-push security verification script for Windows
REM Execute este script ANTES de fazer push ao GitHub

setlocal enabledelayedexpansion
cls

echo.
echo 🔐 TransactionGuard - Pre-Push Security Check (Windows)
echo ======================================================
echo.

set ERRORS=0

REM Check 1: .env files
echo 1️⃣  Verificando arquivos .env...
if exist ".env" (
    echo ❌ ERRO: .env existe! Remova imediatamente!
    echo    Faça: del .env
    set ERRORS=1
    goto error
) else (
    echo ✅ .env não existe
)

REM Check 2: .gitignore exists
echo.
echo 2️⃣  Verificando .gitignore...
if not exist ".gitignore" (
    echo ❌ ERRO: .gitignore não existe!
    set ERRORS=1
    goto error
) else (
    echo ✅ .gitignore existe
)

REM Check 3: Verify key patterns in gitignore
findstr /R "^target/$" .gitignore >nul
if errorlevel 1 (
    echo ❌ ERRO: target/ não está em .gitignore!
    set ERRORS=1
    goto error
) else (
    echo ✅ target/ está em .gitignore
)

findstr /R "^\.env" .gitignore >nul
if errorlevel 1 (
    echo ⚠️  AVISO: .env não está em .gitignore
) else (
    echo ✅ .env está em .gitignore
)

REM Check 4: Verify structure
echo.
echo 3️⃣  Verificando estrutura do projeto...
if not exist "src\main\java" (
    echo ❌ ERRO: src\main\java não existe!
    set ERRORS=1
    goto error
) else (
    echo ✅ src\main\java existe
)

if not exist "src\test\java" (
    echo ❌ ERRO: src\test\java não existe!
    set ERRORS=1
    goto error
) else (
    echo ✅ src\test\java existe
)

REM Check 5: Key files exist
echo.
echo 4️⃣  Verificando arquivos essenciais...
if not exist "pom.xml" (
    echo ❌ ERRO: pom.xml não existe!
    set ERRORS=1
    goto error
) else (
    echo ✅ pom.xml existe
)

if not exist "README.md" (
    echo ❌ ERRO: README.md não existe!
    set ERRORS=1
    goto error
) else (
    echo ✅ README.md existe
)

if not exist ".env.example" (
    echo ⚠️  AVISO: .env.example não existe
) else (
    echo ✅ .env.example existe
)

REM Check 6: No plaintext secrets
echo.
echo 5️⃣  Verificando por credentials hardcoded...
findstr /I /R "password.*=" pom.xml >nul
if not errorlevel 1 (
    echo ⚠️  AVISO: Possível "password" em pom.xml (verificar manualmente)
) else (
    echo ✅ Nenhuma credential aparente em pom.xml
)

REM Final result
echo.
echo ======================================================
echo ✅ VERIFICAÇÃO COMPLETA - SEGURO PARA PUSH!
echo ======================================================
echo.
echo Próximos passos:
echo   1. git status          (verificar arquivos)
echo   2. git add .           (stagear arquivos)
echo   3. git commit -m "..." (criar commit)
echo   4. git push origin main (fazer push)
echo.
goto end

:error
echo.
echo ======================================================
echo ❌ VERIFICAÇÃO FALHOU - NÃO FAÇA PUSH!
echo ======================================================
echo.
pause
exit /b 1

:end
pause
