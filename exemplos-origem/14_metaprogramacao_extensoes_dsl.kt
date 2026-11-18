/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 14_metaprogramacao_extensoes_dsl.kt
 * Tópicos abordados:
 *  - Metaprogramação e Recursos Avançados em Kotlin:
 *      * Funções de Extensão ('Extension Functions'): adicionam novos métodos a tipos
 *        existentes sem necessidade de herança ou padrões como Decorator
 *      * Propriedades de Extensão ('Extension Properties')
 *      * Funções Infixas ('infix fun'): permitem sintaxe fluida sem pontos ou parênteses
 *      * Lambdas com Receptor ('Function literals with receiver'): base para Type-Safe Builders
 *      * Construção de DSLs (Domain Specific Languages) nativas e fortemente tipadas
 * 
 * Contexto aplicado:
 *  DSL para Especificação de Regras Gramaticais e Produções Sintáticas
 *  do Transpilador Kotlin -> C.
 */

// ============================================================================
// 1. Funções e Propriedades de Extensão
// ============================================================================

// Estendendo a classe padrão String com formatação para C
fun String.paraIdentificadorC(): String {
    return this.replace(" ", "_")
        .replace("-", "_")
        .lowercase()
}

// Propriedade de extensão calculada
val String.ehPalavraChaveKotlin: Boolean
    get() = this in setOf("fun", "val", "var", "class", "if", "when", "return")

// Função infixa: invocável sem pontos: '"token" produz "tipo"'
infix fun String.produz(tipo: String): ParGramatical = ParGramatical(this, tipo)

data class ParGramatical(val simboloNaoTerminal: String, val producao: String)

// ============================================================================
// 2. Construção de uma DSL Type-Safe (Domain-Specific Language)
// ============================================================================

class RegraGramatical(val nome: String) {
    private val producoes = mutableListOf<String>()

    fun opcao(producao: String) {
        producoes.add(producao)
    }

    override fun toString(): String {
        return "$nome ::= " + producoes.joinToString(" | ")
    }
}

// Construtor de bloco da DSL usando Lambdas com Receptor: 'RegraGramatical.() -> Unit'
class GramaticaBuilder(val nomeLinguagem: String) {
    private val regras = mutableListOf<RegraGramatical>()

    // Função que recebe uma lambda com receptor
    fun regra(nome: String, configurador: RegraGramatical.() -> Unit) {
        val novaRegra = RegraGramatical(nome)
        novaRegra.configurador() // Executa no contexto do objeto RegraGramatical
        regras.add(novaRegra)
    }

    fun exibirGramatica() {
        println("  Gramática Formal da Linguagem '$nomeLinguagem':")
        regras.forEach { r -> println("    $r") }
    }
}

// Ponto de entrada da DSL (Type-safe builder)
fun gramatica(linguagem: String, init: GramaticaBuilder.() -> Unit): GramaticaBuilder {
    val builder = GramaticaBuilder(linguagem)
    builder.init()
    return builder
}

fun main() {
    println("==================================================")
    println("    EXTENSÕES, INFIX E DSLs (METAPROGRAMAÇÃO)     ")
    println("==================================================\n")

    // --- 1. Funções e Propriedades de Extensão ---
    println(">>> 1. FUNÇÕES E PROPRIEDADES DE EXTENSÃO:")
    val nomeVariavelSujo = "meu-identificador teste"
    println("  Nome original:           \"$nomeVariavelSujo\"")
    println("  Extensão paraIdentificadorC: \"${nomeVariavelSujo.paraIdentificadorC()}\"")

    val token1 = "val"
    val token2 = "minhaVariavel"
    println("  '$token1' é palavra-chave? ${token1.ehPalavraChaveKotlin}")
    println("  '$token2' é palavra-chave? ${token2.ehPalavraChaveKotlin}\n")

    // --- 2. Funções Infixas ---
    println(">>> 2. NOTAÇÃO INFIXA ('infix fun'):")
    val regraExpressao = "Expressao" produz "Termo ('+' Termo)*"
    println("  Regra BNF gerada via infix: ${regraExpressao.simboloNaoTerminal} -> ${regraExpressao.producao}\n")

    // --- 3. DSL Type-Safe com Lambdas com Receptor ---
    println(">>> 3. DSL TYPE-SAFE PARA CONSTRUÇÃO DE GRAMÁTICA:")

    // Bloco que parece uma linguagem dedicada declarativa, mas é 100% Kotlin válido e tipado!
    val spec = gramatica("Subconjunto-Kotlin") {
        regra("Declaracao") {
            opcao("'val' IDENTIFICADOR ':' TIPO '=' Expressao")
            opcao("'var' IDENTIFICADOR ':' TIPO '=' Expressao")
        }

        regra("Expressao") {
            opcao("Expressao '+' Termo")
            opcao("Expressao '-' Termo")
            opcao("Termo")
        }

        regra("Termo") {
            opcao("Termo '*' Fator")
            opcao("Fator")
        }

        regra("Fator") {
            opcao("NUMERO")
            opcao("IDENTIFICADOR")
            opcao("'(' Expressao ')'")
        }
    }

    spec.exibirGramatica()

    println("\n==================================================")
    println("Conclusão: O suporte a DSLs e extensões permite que")
    println("o Kotlin modele especificações de compiladores de forma única.")
    println("==================================================")
}
