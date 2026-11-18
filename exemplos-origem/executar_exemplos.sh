#!/usr/bin/env bash
# ==============================================================================
# Script: executar_exemplos.sh
# Disciplina: Compiladores - Semestre 2026.2
# Descrição: Script para compilar e executar os códigos de exemplo em Kotlin.
# ==============================================================================

set -e

DIR_ATUAL="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DIR_BUILD="$DIR_ATUAL/build_temp"

# Verificar se kotlinc e java estão instalados
if ! command -v kotlinc &> /dev/null; then
    echo "======================================================================"
    echo "ERRO: O compilador Kotlin ('kotlinc') não foi encontrado no PATH."
    echo "Consulte o README.md desta pasta para instruções de instalação."
    echo "======================================================================"
    exit 1
fi

if ! command -v java &> /dev/null; then
    echo "======================================================================"
    echo "ERRO: A JVM ('java') não foi encontrada no PATH."
    echo "======================================================================"
    exit 1
fi

mkdir -p "$DIR_BUILD"

executar_arquivo() {
    local arquivo="$1"
    local nome_base="$(basename "$arquivo" .kt)"
    local jar_saida="$DIR_BUILD/${nome_base}.jar"

    echo ""
    echo "======================================================================"
    echo "  Compilando e Executando: $(basename "$arquivo")"
    echo "======================================================================"
    
    kotlinc "$arquivo" -include-runtime -d "$jar_saida"
    java -jar "$jar_saida"
}

if [ -n "$1" ]; then
    # Executar arquivo específico
    if [ -f "$DIR_ATUAL/$1" ]; then
        executar_arquivo "$DIR_ATUAL/$1"
    elif [ -f "$1" ]; then
        executar_arquivo "$1"
    else
        echo "Arquivo não encontrado: $1"
        exit 1
    fi
else
    # Executar todos os exemplos em ordem numérica
    echo "Iniciando compilação e execução de todos os 14 exemplos..."
    for arq in "$DIR_ATUAL"/[0-9][0-9]_*.kt; do
        if [ -f "$arq" ]; then
            executar_arquivo "$arq"
        fi
    done
fi

echo ""
echo "======================================================================"
echo "Execução concluída com sucesso!"
echo "Arquivos temporários armazenados em: $DIR_BUILD"
echo "======================================================================"
