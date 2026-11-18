/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 08_colecoes_e_estruturas_dados.kt
 * Tópicos abordados:
 *  - Coleções em Kotlin:
 *      * Arrays nativos (tamanho fixo, 'arrayOf', 'IntArray')
 *      * Listas de somente leitura ('listOf') vs Listas mutáveis ('mutableListOf')
 *      * Conjuntos ('setOf' e 'mutableSetOf') para garantia de unicidade de elementos
 *      * Mapas / Dicionários ('mapOf' e 'mutableMapOf') para pares chave-valor
 *  - Ponteiros e Referências:
 *      * Kotlin não possui ponteiros manuais (sem aritmética de ponteiros, desreferenciação '*' ou '&')
 *      * Todas as variáveis que guardam instâncias de classes contêm referências gerenciadas
 *      * Gerenciamento de memória automático (Garbage Collection pela JVM)
 * 
 * Contexto aplicado:
 *  Tabela de Símbolos (Symbol Table) do Transpilador:
 *  mapeamento de identificadores, tipos, endereços virtuais e validação de escopos.
 */

// Registro de uma entrada de símbolo
data class Simbolo(
    val identificador: String,
    val tipo: String,
    val enderecoVirtual: Int,
    val ehConstante: Boolean
)

// Estrutura que representa a Tabela de Símbolos usando coleções idiomáticas
class TabelaDeSimbolos(val nomeEscopo: String) {
    // Conjunto mutável para garantir identificadores únicos no mesmo escopo
    private val identificadoresDeclarados: MutableSet<String> = mutableSetOf()

    // Mapa mutável para associar o identificador aos metadados do símbolo
    private val mapaSimbolos: MutableMap<String, Simbolo> = mutableMapOf()

    // Lista mutável para registrar a ordem de inserção dos símbolos (para alocação de stack)
    private val ordemDeclaracao: MutableList<Simbolo> = mutableListOf()

    fun declararSimbolo(identificador: String, tipo: String, ehConstante: Boolean): Boolean {
        // Verificação rápida de duplicidade no Set O(1)
        if (identificador in identificadoresDeclarados) {
            println("  [AVISO SEMÂNTICO] Redeclaração inválida: identificador '$identificador' já existe no escopo '$nomeEscopo'.")
            return false
        }

        val endereco = mapaSimbolos.size * 4 // Simulação de alocação de 4 bytes por palavra
        val novoSimbolo = Simbolo(identificador, tipo, endereco, ehConstante)

        identificadoresDeclarados.add(identificador)
        mapaSimbolos[identificador] = novoSimbolo
        ordemDeclaracao.add(novoSimbolo)

        println("  [OK] Símbolo declarado: '$identificador' : $tipo (Endereço: 0x${endereco.toString(16).padStart(4, '0')})")
        return true
    }

    fun buscarSimbolo(identificador: String): Simbolo? {
        return mapaSimbolos[identificador]
    }

    // Retorna uma Lista de somente leitura (imutabilidade defensiva)
    fun listarEmOrdem(): List<Simbolo> = ordemDeclaracao.toList()
}

fun demonstrarReferenciasVsPonteiros() {
    println("--- Demonstração: Referências vs Ponteiros Manuais ---")

    // Instanciação de dois objetos no heap
    val s1 = Simbolo("taxaJuros", "Double", 0, false)
    
    // s2 recebe uma cópia da referência de s1 (ambos apontam para o mesmo objeto)
    val s2 = s1

    println("s1 referencia objeto: $s1")
    println("s2 referencia objeto: $s2")
    println("s1 e s2 apontam para o mesmo objeto (referential equality '==='): ${s1 === s2}")

    // Em C/C++, a memória precisaria de 'free(s1)'.
    // Em Kotlin, quando as variáveis saem de escopo, o Garbage Collector da JVM
    // coleta a memória automaticamente, sem riscos de vazamento (memory leak) de dangling pointers!
    println("Gerenciamento de memória: Coleta automática de lixo (GC) ativa.\n")
}

fun main() {
    println("==================================================")
    println("      COLEÇÕES, ESTRUTURAS DE DADOS E REFERÊNCIAS ")
    println("==================================================\n")

    // --- 1. Arrays Nativos ---
    println(">>> 1. ARRAYS:")
    val tiposNativos = arrayOf("Int", "Long", "Float", "Double", "Boolean", "String")
    println("  Array de tipos suportados (${tiposNativos.size} tipos):")
    tiposNativos.forEachIndexed { i, tipo -> println("    [$i] -> $tipo") }
    println()

    // --- 2. Listas, Sets e Mapas na Tabela de Símbolos ---
    println(">>> 2. TABELA DE SÍMBOLOS COM SETS, MAPS E LISTAS:")
    val tabelaGlobal = TabelaDeSimbolos("Escopo_Global_Main")

    tabelaGlobal.declararSimbolo("limiteIteracoes", "Int", ehConstante = true)
    tabelaGlobal.declararSimbolo("nomeUsuario", "String", ehConstante = false)
    tabelaGlobal.declararSimbolo("saldoConta", "Double", ehConstante = false)

    // Tentativa de duplicata (rejeitada pelo Set)
    tabelaGlobal.declararSimbolo("nomeUsuario", "String", ehConstante = false)
    println()

    // Busca no Map
    val consulta = tabelaGlobal.buscarSimbolo("saldoConta")
    println("  Consulta por 'saldoConta': $consulta\n")

    // Exibição da Lista ordenada
    println("  Símbolos ordenados para geração de quadro de pilha (Stack Frame):")
    for (simbolo in tabelaGlobal.listarEmOrdem()) {
        println("    -> Endereço 0x${simbolo.enderecoVirtual.toString(16)}: ${simbolo.identificador} (${simbolo.tipo})")
    }
    println()

    // --- 3. Referências e Gerenciamento de Memória ---
    demonstrarReferenciasVsPonteiros()

    println("==================================================")
    println("Conclusão: O uso de coleções mutáveis e imutáveis")
    println("garante integridade sem os riscos de ponteiros crus.")
    println("==================================================")
}
