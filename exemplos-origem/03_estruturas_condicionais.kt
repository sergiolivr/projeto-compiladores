/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 03_estruturas_condicionais.kt
 * Tópicos abordados:
 *  - Condicionais 'if' / 'else':
 *      * Uso como comando/instrução tradicional
 *      * Uso como expressão com retorno de valor (substituto idiomático do operador ternário)
 *  - Estrutura 'when' (Pattern Matching avançado de Kotlin):
 *      * Correspondência por valor exato e múltiplos valores (separados por vírgula)
 *      * Correspondência por intervalo numérico ('in' e '!in')
 *      * Verificação de tipos com Smart Cast ('is' e '!is')
 *      * 'when' sem argumento (funcionando como cadeia if-else-if legível)
 * 
 * Contexto aplicado:
 *  Classificador Léxico (Lexer Classifier) do Transpilador: analisa lexemas,
 *  determina a categoria do token e atribui pesos de severidade a alertas de sintaxe.
 */

// Enumeração de tipos de token léxico
enum class CategoriaToken {
    PALAVRA_CHAVE,
    IDENTIFICADOR,
    LITERAL_NUMERICO,
    LITERAL_TEXTO,
    OPERADOR,
    DELIMITADOR,
    DESCONHECIDO
}

fun classificarLexema(lexema: String): CategoriaToken {
    // Uso de 'when' como expressão que retorna um valor diretamente
    return when (lexema) {
        "fun", "val", "var", "class", "if", "else", "when", "for", "while", "return" -> {
            CategoriaToken.PALAVRA_CHAVE
        }
        "+", "-", "*", "/", "%", "=", "==", "!=" -> {
            CategoriaToken.OPERADOR
        }
        "(", ")", "{", "}", "[", "]", ";", ",", "." -> {
            CategoriaToken.DELIMITADOR
        }
        else -> {
            // 'when' sem argumento ou com condições lógicas internas
            when {
                lexema.startsWith("\"") && lexema.endsWith("\"") -> CategoriaToken.LITERAL_TEXTO
                lexema.toIntOrNull() != null || lexema.toDoubleOrNull() != null -> CategoriaToken.LITERAL_NUMERICO
                lexema.matches(Regex("[a-zA-Z_][a-zA-Z0-9_]*")) -> CategoriaToken.IDENTIFICADOR
                else -> CategoriaToken.DESCONHECIDO
            }
        }
    }
}

fun avaliarSeveridadeDiagnostico(codigoErro: Int): String {
    // Uso de 'when' com faixas / intervalos numéricos
    return when (codigoErro) {
        in 100..199 -> "INFO: Informação diagnóstica do compilador."
        in 200..299 -> "AVISO (Warning): Prática desencorajada, mas compilação prossegue."
        in 300..499 -> "ERRO SINTÁTICO: Falha na análise gramatical, abortando fase."
        !in 100..500 -> "CÓDIGO INVÁLIDO: Falha crítica interna do transpilador."
        else -> "ERRO DESCONHECIDO."
    }
}

fun processarExpressaoGenerica(objeto: Any): String {
    // Uso de 'when' com verificação de tipo (is) e Smart Cast automático
    return when (objeto) {
        is Int -> "Inteiro com valor dobrado: ${objeto * 2}"
        is String -> "String em maiúsculas (tamanho ${objeto.length}): ${objeto.uppercase()}"
        is Boolean -> if (objeto) "Booleano VERDADEIRO" else "Booleano FALSO"
        is List<*> -> "Lista com ${objeto.size} elemento(s)"
        else -> "Tipo não mapeado: ${objeto::class.simpleName}"
    }
}

fun main() {
    println("==================================================")
    println("           ESTRUTURAS CONDICIONAIS: IF & WHEN     ")
    println("==================================================\n")

    // --- 1. IF como Expressão ---
    println(">>> 1. IF / ELSE COMO EXPRESSÃO:")
    val linhasCodigo = 1250
    val nivelOtimizacao = 2

    // Atribuição direta do resultado da condição 'if/else'
    val statusCompilador = if (linhasCodigo > 1000 && nivelOtimizacao >= 2) {
        "Compilação OTIMIZADA para projetos de grande porte"
    } else {
        "Compilação PADRÃO / Rápida"
    }
    println("  Resultado do if-expressão: $statusCompilador\n")

    // --- 2. Classificação com WHEN ---
    println(">>> 2. ESTRUTURA 'WHEN' COM MULTIVALORES E CONDIÇÕES:")
    val amostrasLexicas = listOf("val", "+", "contador", "42", "\"compiladores\"", "{", "@invalido")

    for (lexema in amostrasLexicas) {
        val categoria = classificarLexema(lexema)
        println("  Lexema: %-16s -> Categoria: %s".format("'$lexema'", categoria))
    }
    println()

    // --- 3. WHEN com Intervalos (Ranges) ---
    println(">>> 3. 'WHEN' COM CHECAGEM DE INTERVALOS (IN / !IN):")
    val codigosTeste = listOf(105, 204, 301, 999)
    for (cod in codigosTeste) {
        println("  Código $cod -> ${avaliarSeveridadeDiagnostico(cod)}")
    }
    println()

    // --- 4. WHEN com Type Pattern Matching (is) e Smart Cast ---
    println(">>> 4. 'WHEN' COM VERIFICAÇÃO DE TIPOS (SMART CAST):")
    val objetosDiversos: List<Any> = listOf(50, "transpiler_c", false, listOf("token1", "token2"), 3.14)
    for (item in objetosDiversos) {
        println("  Item: $item -> ${processarExpressaoGenerica(item)}")
    }

    println("\n==================================================")
    println("Conclusão: O comando 'when' de Kotlin substitui os antigos")
    println("'switch-case' com muito mais segurança, expressividade e poder.")
    println("==================================================")
}
