/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 01_hello_world_paradigmas.kt
 * Tópicos abordados:
 *  - Estrutura básica de um programa Kotlin (ponto de entrada 'fun main')
 *  - Hello World idiomático
 *  - Demonstração prática dos paradigmas suportados pela linguagem:
 *      1. Paradigma Imperativo (variáveis, loops, comandos de fluxo)
 *      2. Paradigma Orientado a Objetos (classes, atributos, métodos, encapsulamento)
 *      3. Paradigma Funcional (expressões lambdas, funções de alta ordem, imutabilidade)
 * 
 * Contexto aplicado:
 *  Módulo de inicialização do Transpilador, registrando as etapas do pipeline
 *  de compilação através de diferentes estilos de programação.
 */

// ============================================================================
// 1. Paradigma Orientado a Objetos (Classes e Métodos)
// ============================================================================
class EtapaCompilacao(val nome: String, val descricao: String) {
    var concluida: Boolean = false
        private set // Permite leitura pública, mas alteração apenas interna

    fun executar() {
        println("  [OOP] Executando etapa: $nome -> $descricao")
        concluida = true
    }

    override fun toString(): String {
        val status = if (concluida) "CONCLUÍDA" else "PENDENTE"
        return "[$status] $nome: $descricao"
    }
}

// ============================================================================
// 2. Paradigma Funcional (Funções puras e transformação de coleções)
// ============================================================================
fun formatarRelatorio(etapas: List<EtapaCompilacao>, transformador: (EtapaCompilacao) -> String): List<String> {
    // Uso de função de alta ordem (map) para transformar sem mutação de estado
    return etapas.map(transformador)
}

// ============================================================================
// Ponto de Entrada (Hello World e Execução Multi-paradigma)
// ============================================================================
fun main() {
    println("==================================================")
    println("         HELLO WORLD - KOTLIN MULTIPARADIGMA      ")
    println("         Transpilador Kotlin -> C (Compiladores)  ")
    println("==================================================\n")

    // --- Hello World Clássico ---
    println("Olá, Mundo! Inicializando o transpilador Kotlin 2026.2...\n")

    // --- Paradigma Imperativo ---
    println(">>> 1. ESTILO IMPERATIVO (Controle de fluxo explícito e mutabilidade):")
    var contadorEtapas = 0
    val totalEsperado = 3
    while (contadorEtapas < totalEsperado) {
        contadorEtapas++
        println("  [Imperativo] Preparando subsistema $contadorEtapas de $totalEsperado...")
    }
    println("  [Imperativo] Todos os subsistemas imperativos foram configurados.\n")

    // --- Paradigma Orientado a Objetos ---
    println(">>> 2. ESTILO ORIENTADO A OBJETOS (Objetos com estado e comportamento):")
    val etapaLexica = EtapaCompilacao("Análise Léxica", "Conversão de caracteres em tokens")
    val etapaSintatica = EtapaCompilacao("Análise Sintática", "Construção da Árvore Sintática Abstrata (AST)")
    val etapaGeracao = EtapaCompilacao("Geração de Código", "Emissão do código C equivalente")

    val listaEtapas = listOf(etapaLexica, etapaSintatica, etapaGeracao)

    // Executando métodos dos objetos
    etapaLexica.executar()
    etapaSintatica.executar()
    etapaGeracao.executar()
    println()

    // --- Paradigma Funcional ---
    println(">>> 3. ESTILO FUNCIONAL (Imutabilidade, Lambdas e Alta Ordem):")
    // Usando lambdas para filtrar e mapear dados declarativamente
    val etapasFinalizadas = listaEtapas.filter { it.concluida }
    
    // Função de alta ordem personalizada
    val relatorioFormatado = formatarRelatorio(etapasFinalizadas) { etapa ->
        "  [Funcional] >> ${etapa.nome.uppercase()} pronta para o pipeline"
    }

    relatorioFormatado.forEach { linha -> println(linha) }

    println("\n==================================================")
    println("Conclusão: Kotlin permite unir os paradigmas de forma natural e limpa.")
    println("==================================================")
}
