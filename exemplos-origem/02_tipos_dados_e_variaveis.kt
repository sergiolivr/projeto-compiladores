/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 02_tipos_dados_e_variaveis.kt
 * Tópicos abordados:
 *  - Tipos de dados primitivos e referências: Int, Long, Float, Double, Boolean, Char, String
 *  - Variáveis: 'val' (imutável / somente leitura) vs 'var' (mutável / reatribuível)
 *  - Escopo e tempo de vida:
 *      * Top-level (escopo global do arquivo / programa)
 *      * Escopo de classe / membro (tempo de vida ligado à instância)
 *      * Escopo de bloco / local (tempo de vida restrito ao escopo léxico)
 *  - Null Safety (Sistema de tipos nulos e não-nulos):
 *      * Tipos anuláveis com '?'
 *      * Operador de chamada segura '?.'
 *      * Operador Elvis '?:'
 *      * Operador de asserção de não-nulo '!!'
 * 
 * Contexto aplicado:
 *  Sistema de métricas, telemetria e flags de compilação do Transpilador.
 */

// ============================================================================
// Escopo Global (Top-Level): Existe durante todo o tempo de vida do programa
// ============================================================================
const val VERSAO_TRANSPILADOR: String = "1.0.0-rc2" // Constante em tempo de compilação
var contadorGlobalDeTokens: Long = 0L             // Variável global mutável

class ModuloCompilacao(val nomeModulo: String) {
    // Escopo de Membro / Classe: Tempo de vida ligado ao ciclo do objeto
    var otimizacoesAtivas: Boolean = true
    val tempoInicioMs: Long = System.currentTimeMillis()

    fun processar() {
        // Escopo Local: Tempo de vida inicia e termina na execução deste método
        val limiarOtimizacao: Int = 100
        var iteracoesLocais: Int = 0

        while (iteracoesLocais < 3) {
            iteracoesLocais++
            contadorGlobalDeTokens += 250 // Atualizando estado global
        }

        println("  [Membro] Módulo '$nomeModulo' processou $iteracoesLocais blocos (limiar: $limiarOtimizacao).")
    }
}

fun demonstrarNullSafety() {
    println("--- Demonstração de Null Safety (Segurança contra Nulos) ---")

    // Variável que NÃO pode ser nula (garantia estática do compilador)
    val autorTranspilador: String = "Equipe Compiladores 2026.2"
    // autorTranspilador = null // Erro de compilação imediato!

    // Variável que PODE ser nula (sufixo '?')
    val diretorioSaidaPersonalizado: String? = null
    val flagOpcional: String? = "O3_EXTREMO"

    println("Autor: $autorTranspilador (tamanho: ${autorTranspilador.length})")

    // 1. Chamada Segura (Safe Call '?.')
    // Se for nulo, a expressão inteira resulta em null sem lançar NullPointerException
    val tamanhoDiretorio: Int? = diretorioSaidaPersonalizado?.length
    println("Tamanho do diretório opcional (safe call): $tamanhoDiretorio")

    // 2. Operador Elvis ('?:')
    // Fornece um valor alternativo caso o lado esquerdo seja nulo
    val pastaDestinoFinal: String = diretorioSaidaPersonalizado ?: "/var/build/transpiler_default"
    println("Diretório de destino resolvido via Elvis: $pastaDestinoFinal")

    // 3. Smart Cast com checagem de nulo
    if (flagOpcional != null) {
        // O compilador sabe que aqui flagOpcional não é nulo (smart cast de String? para String)
        println("Flag configurada (smart cast): ${flagOpcional.lowercase()}")
    }

    // 4. Operador '!!' (Not-null assertion)
    // Converte explicitamente T? para T, mas lança NullPointerException se for nulo
    val flagSegura: String = flagOpcional!!
    println("Flag confirmada como não-nula: $flagSegura\n")
}

fun main() {
    println("==================================================")
    println("      TIPOS DE DADOS, VARIÁVEIS E NULL SAFETY     ")
    println("==================================================\n")

    // --- Tipos de Dados Básicos ---
    val inteiro32: Int = 42
    val inteiro64: Long = 9876543210L
    val pontoFlutuanteSimples: Float = 3.14159f
    val pontoFlutuanteDuplo: Double = 2.718281828459
    val booleano: Boolean = true
    val caractere: Char = 'K'
    val texto: String = "Kotlin Compilador"

    println(">>> TIPOS PRIMITIVOS E BÁSICOS:")
    println("  Int (32 bits):     $inteiro32")
    println("  Long (64 bits):    $inteiro64")
    println("  Float (32 bits):   $pontoFlutuanteSimples")
    println("  Double (64 bits):  $pontoFlutuanteDuplo")
    println("  Boolean:           $booleano")
    println("  Char:              '$caractere'")
    println("  String:            \"$texto\"\n")

    // --- Diferença entre val e var ---
    println(">>> IMUTABILIDADE ('val') vs MUTABILIDADE ('var'):")
    val taxaAmortizacao: Double = 0.15 // Não pode ser reatribuída
    // taxaAmortizacao = 0.20 // Erro de compilação: Val cannot be reassigned

    var contagemPassos: Int = 10
    println("  Passos iniciais (var): $contagemPassos")
    contagemPassos += 5
    println("  Passos após alteração (var): $contagemPassos\n")

    // --- Escopos e Ciclo de Vida ---
    println(">>> ESCOPOS E TEMPO DE VIDA:")
    println("  Versão Global (top-level): $VERSAO_TRANSPILADOR")
    val modulo = ModuloCompilacao("Front-end Lexer")
    modulo.processar()
    println("  Tokens globais acumulados: $contadorGlobalDeTokens\n")

    // --- Null Safety ---
    demonstrarNullSafety()

    println("==================================================")
    println("Conclusão: O sistema de tipos estático do Kotlin elimina")
    println("erros de ponteiro nulo (o 'erro de um bilhão de dólares').")
    println("==================================================")
}
