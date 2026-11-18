/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 07_funcoes_alta_ordem_e_lambdas.kt
 * Tópicos abordados:
 *  - Funções de Ordem Superior (Higher-Order Functions):
 *      * Funções que recebem outras funções como parâmetros
 *      * Funções que retornam funções
 *  - Expressões Lambda:
 *      * Sintaxe literal: { param1, param2 -> corpo }
 *      * Parâmetro implícito 'it' para lambdas com um único argumento
 *      * Convenção Trailing Lambda (lambda como último parâmetro colocado fora dos parênteses)
 *  - Operações Funcionais da Biblioteca Padrão:
 *      * 'filter' (seleção por predicado booleano)
 *      * 'map' (transformação de elementos)
 *      * 'fold' e 'reduce' (agregação cumulativa)
 *      * 'groupBy' (agrupamento categórico)
 * 
 * Contexto aplicado:
 *  Pipeline de Otimização e Análise de Instruções da Representação Intermediária (IR)
 *  do Transpilador.
 */

// Representação de uma instrução na Representação Intermediária (IR)
data class InstrucaoIR(
    val id: Int,
    val comando: String,
    val operando1: String,
    val operando2: String? = null,
    val ehMorta: Boolean = false, // Instrução inalcançável ou sem efeito
    val custoCiclos: Int = 1
)

// Função de alta ordem personalizada: recebe uma lista e uma função de filtro/transformação
fun <T, R> executarPassoOtimizacao(
    nomePasso: String,
    dados: List<T>,
    transformador: (T) -> R
): List<R> {
    println("  [Pipeline] Executando passo '$nomePasso' sobre ${dados.size} itens...")
    return dados.map(transformador)
}

// Função de alta ordem que RETORNA uma função (Closure / Fábrica de predicados)
fun criarFiltroPorCustoMinimo(custoMinimo: Int): (InstrucaoIR) -> Boolean {
    // Retorna uma lambda que captura 'custoMinimo' do escopo externo
    return { instrucao -> instrucao.custoCiclos >= custoMinimo }
}

fun main() {
    println("==================================================")
    println("      FUNÇÕES LAMBDA E DE ORDEM SUPERIOR (HOF)    ")
    println("==================================================\n")

    // Lista de instruções geradas na etapa intermediária
    val instrucoes = listOf(
        InstrucaoIR(1, "LOAD", "R1", "x", ehMorta = false, custoCiclos = 2),
        InstrucaoIR(2, "ADD", "R1", "5", ehMorta = false, custoCiclos = 1),
        InstrucaoIR(3, "NOP", "0", null, ehMorta = true, custoCiclos = 0), // Dead code
        InstrucaoIR(4, "STORE", "y", "R1", ehMorta = false, custoCiclos = 3),
        InstrucaoIR(5, "LOAD", "R2", "y", ehMorta = true, custoCiclos = 2), // Dead code
        InstrucaoIR(6, "MUL", "R1", "R2", ehMorta = false, custoCiclos = 4)
    )

    // --- 1. Operações Funcionais: Filter, Map e Trailing Lambda ---
    println(">>> 1. PIPELINE DE TRANSFORMAÇÃO: FILTER E MAP")
    
    // Filtragem de código morto usando trailing lambda e parâmetro implícito 'it'
    val instrucoesVivas = instrucoes.filter { !it.ehMorta }
    println("  Instruções vivas restantes: ${instrucoesVivas.size}")

    // Mapeamento para formato textual de assembly / C
    val codigoLinear = instrucoesVivas.map { inst ->
        val ops = if (inst.operando2 != null) "${inst.operando1}, ${inst.operando2}" else inst.operando1
        "${inst.comando} $ops"
    }
    codigoLinear.forEach { linha -> println("    ASM: $linha") }
    println()

    // --- 2. Agregação com REDUCE e FOLD ---
    println(">>> 2. AGREGAÇÃO CUMULATIVA COM FOLD E REDUCE:")
    
    // fold: calcula a soma total de ciclos partindo de um valor inicial (0)
    val totalCiclosCpu = instrucoesVivas.fold(0) { acumulador, inst ->
        acumulador + inst.custoCiclos
    }
    println("  Custo total acumulado das instruções vivas: $totalCiclosCpu ciclos de CPU\n")

    // --- 3. Agrupamento Categórico com GROUPBY ---
    println(">>> 3. AGRUPAMENTO COM GROUPBY:")
    val instrucoesPorComando = instrucoes.groupBy { it.comando }
    instrucoesPorComando.forEach { (comando, lista) ->
        println("  Comando '$comando' aparece ${lista.size} vez(es)")
    }
    println()

    // --- 4. Função de Alta Ordem Personalizada ---
    println(">>> 4. FUNÇÃO DE ALTA ORDEM PERSONALIZADA COM TRAILING LAMBDA:")
    val relatorioOtimizado = executarPassoOtimizacao("Renomeação de Registradores", instrucoesVivas) { inst ->
        "No. ${inst.id}: ${inst.comando} (Custo: ${inst.custoCiclos})"
    }
    relatorioOtimizado.forEach { println("    $it") }
    println()

    // --- 5. Função que Retorna Função (Fábrica de Predicados) ---
    println(">>> 5. RETORNANDO FUNÇÕES A PARTIR DE FUNÇÕES (CLOSURES):")
    val filtroInstrucoesPesadas = criarFiltroPorCustoMinimo(3)
    val instrucoesCriticas = instrucoesVivas.filter(filtroInstrucoesPesadas)
    println("  Instruções críticas (custo >= 3 ciclos):")
    instrucoesCriticas.forEach { println("    -> ${it.comando} com custo ${it.custoCiclos}") }

    println("\n==================================================")
    println("Conclusão: O suporte nativo a lambdas permite processar")
    println("estruturas do compilador com pipelines expressivos e puros.")
    println("==================================================")
}
