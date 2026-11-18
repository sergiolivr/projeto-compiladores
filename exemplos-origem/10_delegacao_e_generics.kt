/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 10_delegacao_e_generics.kt
 * Tópicos abordados:
 *  - Delegação de Comportamento (Class Delegation via palavra-chave 'by'):
 *      * Favorece Composição sobre Herança sem código 'boilerplate'
 *  - Propriedades Delegadas (Property Delegation):
 *      * 'by lazy' (inicialização postergada e thread-safe sob demanda)
 *      * 'by Delegates.observable' (notificação automática de alterações)
 *  - Programação Genérica (Generics):
 *      * Funções genéricas: 'fun <T> primeiro(itens: List<T>): T'
 *      * Classes genéricas: estruturas que operam com segurança de tipos sobre qualquer tipo T
 *      * Restrições de tipos (Type Bounds: <T : Comparable<T>>)
 * 
 * Contexto aplicado:
 *  Pilha Genérica de Escopos Léxicos do Transpilador combinada a um serviço
 *  de auditoria (Log) delegado por composição.
 */

import kotlin.properties.Delegates

// ============================================================================
// 1. Delegação de Comportamento de Classes (Class Delegation com 'by')
// ============================================================================
interface ServicoAuditoria {
    fun registrarEvento(mensagem: String)
    fun totalLogs(): Int
}

class AuditoriaConsole(val prefixo: String) : ServicoAuditoria {
    private var contador = 0

    override fun registrarEvento(mensagem: String) {
        contador++
        println("  [$prefixo] #$contador: $mensagem")
    }

    override fun totalLogs(): Int = contador
}

// A classe Compilador implementa ServicoAuditoria DELEGANDO toda a implementação
// para o objeto 'auditoria' passado via injeção de dependência!
class CompiladorEscopos(auditoria: ServicoAuditoria) : ServicoAuditoria by auditoria {
    // Propriedade delegada com 'by lazy': calculada apenas na primeira vez que for acessada!
    val configuracaoPesada: String by lazy {
        println("    [Lazy] Inicializando configuração complexa de regras de sintaxe...")
        "Configuração_AST_V2_Carregada"
    }

    // Propriedade delegada com 'by Delegates.observable': intercepta leituras e gravações
    var nivelOtimizacao: Int by Delegates.observable(1) { prop, antigo, novo ->
        registrarEvento("Nível de '${prop.name}' alterado de $antigo para $novo.")
    }
}

// ============================================================================
// 2. Programação Genérica (Generics)
// ============================================================================

// Função genérica que opera sobre qualquer tipo T
fun <T> obterPrimeiroElemento(itens: List<T>): T {
    if (itens.isEmpty()) throw NoSuchElementException("Lista vazia!")
    return itens.first()
}

// Função genérica com restrição de tipo (Upper Bound: T deve implementar Comparable)
fun <T : Comparable<T>> encontrarMaiorElemento(a: T, b: T): T {
    return if (a > b) a else b
}

// Classe genérica: Pilha com segurança de tipos (Type-safe Stack)
class PilhaGenerica<T> {
    private val elementos = mutableListOf<T>()

    fun empilhar(item: T) {
        elementos.add(item)
    }

    fun desempilhar(): T {
        if (elementos.isEmpty()) throw EmptyStackException()
        return elementos.removeAt(elementos.size - 1)
    }

    fun topo(): T? = elementos.lastOrNull()
    val tamanho: Int get() = elementos.size
    fun estaVazia(): Boolean = elementos.isEmpty()
}

class EmptyStackException : RuntimeException("A pilha genérica está vazia!")

fun main() {
    println("==================================================")
    println("      DELEGAÇÃO DE COMPORTAMENTO E GENERICS       ")
    println("==================================================\n")

    // --- 1. Delegação de Classes (Class Delegation) ---
    println(">>> 1. DELEGAÇÃO DE CLASSES POR COMPOSIÇÃO ('by'):")
    val logger = AuditoriaConsole("LOGGER-COMPILADOR")
    val compilador = CompiladorEscopos(logger)

    // O compilador chama métodos de ServicoAuditoria diretamente graças ao delegate!
    compilador.registrarEvento("Iniciando varredura léxica.")
    compilador.registrarEvento("Tabela de tipos resolvida com êxito.")
    println("  Total de logs delegados registrados: ${compilador.totalLogs()}\n")

    // --- 2. Propriedades Delegadas: Lazy e Observable ---
    println(">>> 2. PROPRIEDADES DELEGADAS (LAZY E OBSERVABLE):")
    println("  Antes de acessar configuracaoPesada...")
    println("  Acessando agora: ${compilador.configuracaoPesada}")
    println("  Acessando novamente (já em cache): ${compilador.configuracaoPesada}\n")

    compilador.nivelOtimizacao = 2
    compilador.nivelOtimizacao = 3
    println()

    // --- 3. Funções Genéricas ---
    println(">>> 3. FUNÇÕES GENÉRICAS (<T>):")
    val listaStrings = listOf("Token_IF", "Token_ELSE", "Token_RETURN")
    val listaInteiros = listOf(10, 20, 30, 40)

    val primeiroStr: String = obterPrimeiroElemento(listaStrings)
    val primeiroInt: Int = obterPrimeiroElemento(listaInteiros)

    println("  Primeiro de String: $primeiroStr")
    println("  Primeiro de Int:    $primeiroInt")

    val maiorStr = encontrarMaiorElemento("Beta", "Alpha")
    val maiorNum = encontrarMaiorElemento(100, 450)
    println("  Maior String (alfabética): $maiorStr")
    println("  Maior Número:              $maiorNum\n")

    // --- 4. Classe Genérica: Pilha de Escopos ---
    println(">>> 4. CLASSE GENÉRICA: PILHA DE ESCOPOS (PilhaGenerica<T>):")
    val pilhaEscopos = PilhaGenerica<String>()
    pilhaEscopos.empilhar("EscopoGlobal")
    pilhaEscopos.empilhar("FuncaoMain")
    pilhaEscopos.empilhar("BlocoCondicionalIf")

    println("  Topo atual da pilha: ${pilhaEscopos.topo()}")
    println("  Desempilhando:       ${pilhaEscopos.desempilhar()}")
    println("  Novo topo:           ${pilhaEscopos.topo()} (Tamanho: ${pilhaEscopos.tamanho})")

    println("\n==================================================")
    println("Conclusão: Delegação e Generics fornecem reuso máximo de código")
    println("com segurança estática de tipos e código livre de duplicação.")
    println("==================================================")
}
