/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 04_estruturas_repeticao_e_desvios.kt
 * Tópicos abordados:
 *  - Comandos de repetição:
 *      * 'for' com faixas (ranges: '..', 'until', 'downTo', 'step')
 *      * 'for' com iteração sobre coleções e desestruturação de índices ('withIndex()')
 *      * 'while' (iteração pré-testada)
 *      * 'do-while' (iteração pós-testada, executando pelo menos uma vez)
 *  - Desvios incondicionais de fluxo:
 *      * 'break' (interrupção imediata de laço)
 *      * 'continue' (salto para a próxima iteração)
 *      * Desvios rotulados ('break@rotulo' e 'continue@rotulo') para controle em loops aninhados
 * 
 * Contexto aplicado:
 *  Varredor de matriz bidimensional de código-fonte (Linha x Coluna) para
 *  eliminação de espaços e processamento de comentários em bloco.
 */

fun main() {
    println("==================================================")
    println("     ESTRUTURAS DE REPETIÇÃO E DESVIOS DE FLUXO   ")
    println("==================================================")\n

    // --- 1. Laço FOR com Intervalos (Ranges) e Progressões ---
    println(">>> 1. LAÇO FOR COM INTERVALOS (RANGES, UNTIL, DOWNTO, STEP):")
    
    print("  Faixa inclusiva (1..5): ")
    for (i in 1..5) {
        print("$i ")
    }
    println()

    print("  Faixa com limite aberto (0 until 5): ")
    for (i in 0 until 5) {
        print("$i ")
    }
    println()

    print("  Regressão com passo alternado (10 downTo 2 step 2): ")
    for (i in 10 downTo 2 step 2) {
        print("$i ")
    }
    println("\n")

    // --- 2. Iteração sobre Coleção com Índices ---
    println(">>> 2. FOR COM ITERAÇÃO SOBRE COLEÇÕES E ÍNDICES (withIndex):")
    val fasesCompilador = listOf("Lexer", "Parser", "Semantic", "Optimizer", "CodeGen")
    for ((indice, fase) in fasesCompilador.withIndex()) {
        println("  Fase [$indice]: $fase")
    }
    println()

    // --- 3. Laços WHILE e DO-WHILE ---
    println(">>> 3. LAÇOS WHILE E DO-WHILE:")
    var bufferBytesRestantes = 12
    println("  [while] Processando buffer de entrada:")
    while (bufferBytesRestantes > 0) {
        bufferBytesRestantes -= 4
        println("    Consumidos 4 bytes. Restantes no buffer: $bufferBytesRestantes")
    }

    var tentativasReconexao = 0
    println("  [do-while] Garantindo pelo menos uma checagem de integridade:")
    do {
        tentativasReconexao++
        println("    Tentativa de sincronização #$tentativasReconexao executada.")
    } while (tentativasReconexao < 1)
    println()

    // --- 4. Desvios Simples: BREAK e CONTINUE ---
    println(">>> 4. DESVIOS COM BREAK E CONTINUE:")
    val linhasCodigo = listOf(
        "val x = 10",
        "// Comentário simples de linha",
        "val y = 20",
        "/* Início de bloco */",
        "// Outro comentário",
        "return x + y",
        "val zInatingivel = 99"
    )

    println("  Filtrando e processando linhas:")
    for (linha in linhasCodigo) {
        // 'continue': pula linhas que são comentários simples
        if (linha.trim().startsWith("//")) {
            println("    [PULADO - CONTINUE] Linha de comentário ignorada.")
            continue
        }

        // 'break': interrompe a análise ao encontrar retorno principal
        if (linha.contains("return")) {
            println("    [FINALIZADO - BREAK] Comando de saída encontrado: '$linha'")
            break
        }

        println("    Processando instrução: '$linha'")
    }
    println()

    // --- 5. Desvios Rotulados (Labeled Jumps) em Loops Aninhados ---
    println(">>> 5. DESVIOS ROTULADOS (LABELED BREAK & CONTINUE):")
    // Matriz representando caracteres em linhas e colunas de código
    val matrizCaracteres = arrayOf(
        charArrayOf('v', 'a', 'l', ' ', 'a'),
        charArrayOf('/', '*', 'X', '*', '/'), // Linha com comentário bloco
        charArrayOf('v', 'a', 'r', ' ', 'b'),
        charArrayOf('e', 'n', 'd', '!', '!')
    )

    println("  Buscando e saltando estruturas via rótulo 'varreduraLinhas@':")
    varreduraLinhas@ for ((numLinha, linhaChars) in matrizCaracteres.withIndex()) {
        for ((numCol, c) in linhaChars.withIndex()) {
            // Se encontrar a combinação de comentário de bloco, pula a linha inteira para o próximo registro
            if (c == '/' && numCol + 1 < linhaChars.size && linhaChars[numCol + 1] == '*') {
                println("    -> Linha $numLinha descartada (comentário em bloco detectado).")
                continue@varreduraLinhas // Salta diretamente para a próxima iteração do laço externo!
            }

            // Se encontrar caractere de parada '!', interrompe totalmente ambos os laços
            if (c == '!') {
                println("    -> Parada crítica acionada na linha $numLinha, coluna $numCol. Abortando matriz.")
                break@varreduraLinhas // Interrompe totalmente o laço externo!
            }
        }
        println("    Linha $numLinha analisada com sucesso: ${String(linhaChars)}")
    }

    println("\n==================================================")
    println("Conclusão: Rótulos (labels) evitam variáveis booleanas de controle")
    println("e tornam a navegação em laços aninhados explícita e limpa.")
    println("==================================================")
}
