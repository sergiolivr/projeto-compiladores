/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 09_orientacao_objetos_e_classes.kt
 * Tópicos abordados:
 *  - Paradigma Orientado a Objetos em Kotlin:
 *      * Declaração de classes com construtor primário e inicialização 'init'
 *      * Construtores secundários ('constructor(...)')
 *      * Propriedades com campos de apoio ('field') e getters/setters customizados
 *      * Modificadores de visibilidade ('public', 'private', 'protected')
 *      * Herança: 'open class' (classes são finais por padrão) e métodos 'override'
 *      * Interfaces com métodos abstratos e implementações padrão
 *      * Classes de Dados ('data class') com geração automática de equals, hashCode e copy
 *      * Objetos Singleton ('object') e Objetos Complementares ('companion object')
 * 
 * Contexto aplicado:
 *  Hierarquia de Nós da Árvore Sintática Abstrata (AST - Abstract Syntax Tree)
 *  do Transpilador Kotlin -> C.
 */

// Interface que define o comportamento de qualquer nó da AST
interface NoAST {
    val linhaFonte: Int
    fun transcompilarParaC(): String

    // Método com implementação padrão (default method)
    fun imprimirArvore(nivel: Int = 0) {
        val indentacao = "  ".repeat(nivel)
        println("$indentacao* [Linha $linhaFonte] ${this::class.simpleName}: ${transcompilarParaC()}")
    }
}

// Classe base aberta para herança (open)
open class NoExpressao(
    override val linhaFonte: Int,
    protected val tipoRetorno: String
) : NoAST {
    open fun obterTipo(): String = tipoRetorno

    override fun transcompilarParaC(): String {
        return "/* expressao generica ($tipoRetorno) */"
    }
}

// Subclasse herdando de NoExpressao: Nó de Literal Numérico
class NoLiteral(
    linhaFonte: Int,
    val valor: Double
) : NoExpressao(linhaFonte, "double") {

    // Construtor secundário para valores inteiros
    constructor(linhaFonte: Int, valorInt: Int) : this(linhaFonte, valorInt.toDouble())

    override fun transcompilarParaC(): String {
        return if (valor % 1.0 == 0.0) "${valor.toLong()}" else "$valor"
    }
}

// Subclasse de Operação Binária (ex: a + b)
class NoOperacaoBinaria(
    linhaFonte: Int,
    val esquerda: NoExpressao,
    val operador: String,
    val direita: NoExpressao
) : NoExpressao(linhaFonte, "double") {

    override fun transcompilarParaC(): String {
        return "(${esquerda.transcompilarParaC()} $operador ${direita.transcompilarParaC()})"
    }

    override fun imprimirArvore(nivel: Int) {
        val indentacao = "  ".repeat(nivel)
        println("$indentacao+ Operador: '$operador' (Linha $linhaFonte)")
        esquerda.imprimirArvore(nivel + 1)
        direita.imprimirArvore(nivel + 1)
    }
}

// Data Class para representar nós de metadados
data class MetadadosAST(
    val versaoGramatica: String,
    val totalNos: Int,
    val otimizado: Boolean
)

// Objeto Singleton: Gerador / Fábrica Central de Nós (Design Pattern Singleton)
object FabricaDeNosAST {
    private var contadorNosCriados: Int = 0

    fun criarLiteral(linha: Int, valor: Int): NoLiteral {
        contadorNosCriados++
        return NoLiteral(linha, valor)
    }

    fun criarSoma(linha: Int, esq: NoExpressao, dir: NoExpressao): NoOperacaoBinaria {
        contadorNosCriados++
        return NoOperacaoBinaria(linha, esq, "+", dir)
    }

    fun totalCriados(): Int = contadorNosCriados
}

fun main() {
    println("==================================================")
    println("      ORIENTAÇÃO A OBJETOS, HERANÇA E OBJETOS     ")
    println("==================================================\n")

    // --- 1. Instanciação e Construtores (Primário e Secundário) ---
    println(">>> 1. CONSTRUTORES E INSTANCIAÇÃO POLIMÓRFICA:")
    val literal1 = FabricaDeNosAST.criarLiteral(1, 10)
    val literal2 = FabricaDeNosAST.criarLiteral(1, 20)
    val literalFlutuante = NoLiteral(linhaFonte = 2, valor = 3.1415)

    println("  Literal 1: ${literal1.transcompilarParaC()}")
    println("  Literal 2: ${literal2.transcompilarParaC()}")
    println("  Literal Flutuante: ${literalFlutuante.transcompilarParaC()}\n")

    // --- 2. Herança, Polimorfismo e Sobrescrita (Override) ---
    println(">>> 2. HIERARQUIA POLIMÓRFICA DA AST:")
    val expressaoSoma = FabricaDeNosAST.criarSoma(linha = 3, esq = literal1, dir = literal2)
    val expressaoComposta = NoOperacaoBinaria(
        linhaFonte = 4,
        esquerda = expressaoSoma,
        operador = "*",
        direita = literalFlutuante
    )

    println("  Código C gerado pelo método transcompilarParaC():")
    println("  ------------------------------------------------")
    println("  printf(\"%f\\n\", ${expressaoComposta.transcompilarParaC()});\n")

    println("  Estrutura da Árvore (chamada polimórfica com indentação):")
    expressaoComposta.imprimirArvore()
    println()

    // --- 3. Data Classes (Imutabilidade com copy e toString) ---
    println(">>> 3. DATA CLASSES:")
    val metaOriginal = MetadadosAST(versaoGramatica = "Kotlin-2.0-Grammar", totalNos = 5, otimizado = false)
    // O método 'copy()' permite criar uma cópia com modificações seletivas
    val metaAtualizado = metaOriginal.copy(otimizado = true)

    println("  Metadados Originais:  $metaOriginal")
    println("  Metadados Atualizados: $metaAtualizado")
    println("  São iguais? ${metaOriginal == metaAtualizado}\n")

    // --- 4. Objeto Singleton (object) ---
    println(">>> 4. PADRÃO SINGLETON COM 'object':")
    println("  Total de nós gerados pela fábrica única: ${FabricaDeNosAST.totalCriados()}")

    println("\n==================================================")
    println("Conclusão: O modelo de orientação a objetos de Kotlin é limpo,")
    println("seguro por padrão (classes fechadas por padrão) e expressivo.")
    println("==================================================")
}
