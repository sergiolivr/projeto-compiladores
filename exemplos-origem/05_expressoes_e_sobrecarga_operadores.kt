/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 05_expressoes_e_sobrecarga_operadores.kt
 * Tópicos abordados:
 *  - Expressões:
 *      * Precedência de operadores (ordem de avaliação de operadores aritméticos, relacionais e lógicos)
 *      * Associatividade de operadores (da esquerda para a direita vs direita para a esquerda)
 *  - Sobrecarga de Operadores em Kotlin (Operator Overloading via convenção de nomes):
 *      * 'operator fun plus' (+)
 *      * 'operator fun minus' (-)
 *      * 'operator fun times' (*)
 *      * 'operator fun unaryMinus' (- unário)
 *      * 'operator fun compareTo' (<, <=, >, >=)
 *      * 'operator fun get' (acesso indexado com colchetes [])
 *      * 'operator fun contains' (operador 'in')
 * 
 * Contexto aplicado:
 *  Métricas de Custo Computacional de Nós da AST: modelagem de vetores de custo
 *  (tempo em ms, uso de memória em KB, nós filhos) somando, comparando e escalando recursos.
 */

// Classe que representa métricas de custo computacional de nós da AST
data class CustoNoAST(
    val ciclosCpu: Int,
    val memoriaBytes: Int,
    val descricao: String = "Custo"
) : Comparable<CustoNoAST> {

    // Sobrecarga do operador de adição (+)
    operator fun plus(outro: CustoNoAST): CustoNoAST {
        return CustoNoAST(
            ciclosCpu = this.ciclosCpu + outro.ciclosCpu,
            memoriaBytes = this.memoriaBytes + outro.memoriaBytes,
            descricao = "${this.descricao} + ${outro.descricao}"
        )
    }

    // Sobrecarga do operador de multiplicação por escalar (*)
    operator fun times(fatorOtimizacao: Int): CustoNoAST {
        return CustoNoAST(
            ciclosCpu = this.ciclosCpu * fatorOtimizacao,
            memoriaBytes = this.memoriaBytes * fatorOtimizacao,
            descricao = "(${this.descricao} * $fatorOtimizacao)"
        )
    }

    // Sobrecarga do operador menos unário (-a)
    operator fun unaryMinus(): CustoNoAST {
        return CustoNoAST(
            ciclosCpu = -this.ciclosCpu,
            memoriaBytes = -this.memoriaBytes,
            descricao = "-(${this.descricao})"
        )
    }

    // Sobrecarga de operadores de comparação (<, <=, >, >=)
    override fun compareTo(other: CustoNoAST): Int {
        // Compara com base no total ponderado de custo
        val pesoEste = this.ciclosCpu * 2 + this.memoriaBytes
        val pesoOutro = other.ciclosCpu * 2 + other.memoriaBytes
        return pesoEste.compareTo(pesoOutro)
    }

    // Sobrecarga do operador de indexação [] (leitura de campos por índice 0 ou 1)
    operator fun get(indice: Int): Int {
        return when (indice) {
            0 -> ciclosCpu
            1 -> memoriaBytes
            else -> throw IndexOutOfBoundsException("Índice de métrica inválido: $indice (use 0 para CPU, 1 para Memória)")
        }
    }
}

// Coleção customizada que implementa o operador 'contains' ('in')
class TabelaSimbolosReservados(private val palavras: List<String>) {
    // Sobrecarga do operador 'in'
    operator fun contains(palavra: String): Boolean {
        return palavras.contains(palavra.lowercase())
    }
}

fun main() {
    println("==================================================")
    println("     EXPRESSÕES E SOBRECARGA DE OPERADORES        ")
    println("==================================================\n")

    // --- 1. Precedência e Associatividade ---
    println(">>> 1. PRECEDÊNCIA E ASSOCIATIVIDADE DE OPERADORES:")
    val a = 10
    val b = 5
    val c = 2

    // Precedência aritmética: multiplicação (*) avaliada antes da soma (+)
    val exp1 = a + b * c // 10 + (5 * 2) = 20, NÃO (10 + 5) * 2 = 30
    println("  Precedência: a + b * c = $a + $b * $c = $exp1")

    // Associatividade: operadores de mesma precedência (+ e -) avaliados da esquerda para a direita
    val exp2 = a - b + c // (10 - 5) + 2 = 7, NÃO 10 - (5 + 2) = 3
    println("  Associatividade (esq -> dir): a - b + c = $a - $b + $c = $exp2")

    // Precedência lógica e relacional: relacionais (> , <) precedem lógicos (&&, ||)
    val expLogica = a > b && b > c || a == 0 // ((10 > 5) && (5 > 2)) || (10 == 0) -> true && true || false -> true
    println("  Expressão lógica complexa: $expLogica\n")

    // --- 2. Sobrecarga de Operadores Aritméticos ---
    println(">>> 2. SOBRECARGA DE OPERADORES ARITMÉTICOS (+, *, - unário):")
    val custoNoLiteral = CustoNoAST(ciclosCpu = 2, memoriaBytes = 8, descricao = "LiteralNum")
    val custoNoIdentificador = CustoNoAST(ciclosCpu = 4, memoriaBytes = 16, descricao = "Identificador")

    // Uso do operador '+' sobrecarregado
    val custoTotalNos = custoNoLiteral + custoNoIdentificador
    println("  Custo somado (+): $custoTotalNos")

    // Uso do operador '*' sobrecarregado
    val custoExpandido = custoTotalNos * 3
    println("  Custo multiplicado (* 3): $custoExpandido")

    // Uso do operador '-' unário sobrecarregado
    val diferencaEconomia = -custoNoLiteral
    println("  Crédito de economia (- unário): $diferencaEconomia\n")

    // --- 3. Operadores Relacionais e Comparação (<, >, <=, >=) ---
    println(">>> 3. SOBRECARGA DE COMPARAÇÃO (compareTo -> <, >, ==):")
    println("  No Literal ($custoNoLiteral) < No Identificador ($custoNoIdentificador)? ${custoNoLiteral < custoNoIdentificador}")
    println("  No Expandido ($custoExpandido) > No Total ($custoTotalNos)? ${custoExpandido > custoTotalNos}\n")

    // --- 4. Operador de Indexação [] (get) e Pertinência (contains / in) ---
    println(">>> 4. ACESSO INDEXADO ([]) E OPERADOR IN (contains):")
    println("  Acessando métrica via colchetes custoTotalNos[0] (CPU): ${custoTotalNos[0]} ciclos")
    println("  Acessando métrica via colchetes custoTotalNos[1] (Memória): ${custoTotalNos[1]} bytes")

    val tabela = TabelaSimbolosReservados(listOf("fun", "val", "var", "transpile", "emit"))
    println("  A palavra 'transpile' está na tabela ('in')? ${"transpile" in tabela}")
    println("  A palavra 'goto' está na tabela ('in')? ${"goto" in tabela}")

    println("\n==================================================")
    println("Conclusão: Em Kotlin a sobrecarga é segura e explícita,")
    println("permitindo criar DSLs matemáticas e semânticas legíveis.")
    println("==================================================")
}
