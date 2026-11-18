/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 11_tratamento_excecoes_e_recursos.kt
 * Tópicos abordados:
 *  - Tratamento de Exceções em Kotlin:
 *      * Todas as exceções em Kotlin são NÃO-VERIFICADAS (Unchecked Exceptions)
 *        (não existe a obrigatoriedade da cláusula 'throws' como em Java)
 *      * 'try / catch / finally' funcionando como UMA EXPRESSÃO que produz valor
 *      * Criação de exceções personalizadas para o domínio do compilador
 *  - Gerenciamento Automático de Recursos com a função '.use { }':
 *      * Equivalente idiomático ao 'try-with-resources' de Java
 *      * Garante o fechamento automático e seguro de fluxos (Closeable / AutoCloseable)
 *        mesmo diante de erros ou exceções imprevistas.
 * 
 * Contexto aplicado:
 *  Parser e Leitor de Arquivos de Código-Fonte do Transpilador,
 *  capturando erros sintáticos e garantindo a liberação de recursos de I/O.
 */

import java.io.BufferedReader
import java.io.StringReader

// ============================================================================
// Exceções Personalizadas do Compilador
// ============================================================================
open class ErroCompilacaoException(mensagem: String, val linha: Int, val coluna: Int) :
    RuntimeException("Erro na linha $linha, coluna $coluna: $mensagem")

class ErroSintaticoException(mensagem: String, linha: Int, coluna: Int) :
    ErroCompilacaoException(mensagem, linha, coluna)

// ============================================================================
// Recurso Simulado que implementa AutoCloseable
// ============================================================================
class FluxoCodigoFonte(val nomeArquivo: String) : AutoCloseable {
    private var aberto = true

    fun lerLinha(): String {
        if (!aberto) throw IllegalStateException("Fluxo já encerrado!")
        return "val resultado = 100 / divisor"
    }

    override fun close() {
        aberto = false
        println("  [AutoCloseable] Recurso '$nomeArquivo' liberado e fechado com segurança.")
    }
}

// ============================================================================
// Demonstração das Exceções e Expressão Try
// ============================================================================
fun avaliarConstanteNumerica(literalTexto: String): Int {
    // 'try' como EXPRESSÃO: atribui o resultado diretamente à variável
    val valorResultado = try {
        literalTexto.toInt()
    } catch (e: NumberFormatException) {
        println("    [Catch] Falha ao converter '$literalTexto': usando valor fallback padrão (0).")
        0 // Valor retornado pelo bloco catch
    } finally {
        println("    [Finally] Bloco de finalização executado.")
    }

    return valorResultado
}

fun validarInstrucao(instrucao: String, numeroLinha: Int) {
    if (!instrucao.endsWith(";")) {
        throw ErroSintaticoException("Ponto e vírgula ';' ausente no final da instrução.", numeroLinha, instrucao.length)
    }
}

fun main() {
    println("==================================================")
    println("     TRATAMENTO DE EXCEÇÕES E RECURSOS (.use)     ")
    println("==================================================\n")

    // --- 1. 'try' como Expressão de Retorno ---
    println(">>> 1. 'TRY' COMO EXPRESSÃO COM VALOR DE RETORNO:")
    val numValido = avaliarConstanteNumerica("1024")
    println("  Resultado 1: $numValido\n")

    val numInvalido = avaliarConstanteNumerica("abc_token")
    println("  Resultado 2: $numInvalido\n")

    // --- 2. Exceções Personalizadas do Compilador ---
    println(">>> 2. CAPTURA DE EXCEÇÃO PERSONALIZADA (ERRO SINTÁTICO):")
    val linhaInvalida = "val x: Int = 42"
    try {
        println("  Validando: '$linhaInvalida'")
        validarInstrucao(linhaInvalida, numeroLinha = 15)
    } catch (e: ErroSintaticoException) {
        println("  [FALHA SINTÁTICA CAPTURADA]")
        println("    Mensagem: ${e.message}")
        println("    Localização: Linha ${e.linha}, Coluna ${e.coluna}")
    }
    println()

    // --- 3. Fechamento Automático de Recursos com '.use' ---
    println(">>> 3. GERENCIAMENTO AUTOMÁTICO DE RECURSOS COM '.use':")
    
    // Leitura segura com leitor de memória
    val codigoSimulado = """
        // Programa simples
        int a = 10;
        int b = 20;
    """.trimIndent()

    BufferedReader(StringReader(codigoSimulado)).use { leitor ->
        println("  Lendo linhas protegidas por .use:")
        var linha: String? = leitor.readLine()
        while (linha != null) {
            println("    > $linha")
            linha = leitor.readLine()
        }
    } // O BufferedReader é automaticamente fechado aqui!
    println()

    // Demonstração com recurso customizado AutoCloseable
    println("  Executando com recurso AutoCloseable:")
    FluxoCodigoFonte("modulo_principal.kt").use { fluxo ->
        println("    Operando sobre o fluxo: ${fluxo.lerLinha()}")
        println("    Fim da leitura do bloco...")
    } // O método close() é invocado deterministicamente aqui!

    println("\n==================================================")
    println("Conclusão: A ausência de exceções checadas e o bloco .use")
    println("reduzem a verbosidade sem abrir mão de segurança e liberação de recursos.")
    println("==================================================")
}
