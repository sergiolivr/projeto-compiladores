/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 06_subprogramas_e_passagem_parametros.kt
 * Tópicos abordados:
 *  - Subprogramas (Funções):
 *      * Declaração com 'fun', tipos explícitos vs inferidos
 *      * Funções de expressão única (Single-expression functions com '=')
 *      * Parâmetros com valores padrão (Default arguments)
 *      * Chamadas com argumentos nomeados (Named arguments)
 *  - Modelo de Passagem de Parâmetros em Kotlin:
 *      * Todos os parâmetros em Kotlin são passados estritamente POR VALOR.
 *      * Para tipos primitivos, o valor do dado é copiado.
 *      * Para objetos, o VALOR DA REFERÊNCIA é copiado (aponta para o mesmo objeto na memória heap).
 *      * Imutabilidade de parâmetros: em Kotlin, parâmetros de função são SEMPRE 'val' implícitos
 *        (não é permitido reatribuir o parâmetro dentro do corpo da função).
 * 
 * Contexto aplicado:
 *  Configurador e Formatador de Código (Pretty Printer) do Transpilador:
 *  demonstração da manipulação de buffers de código e opções de emissão.
 */

// Classe mutável para demonstrar o modelo de referência
class BufferDeCodigo(var conteudo: String = "") {
    fun anexarLinha(linha: String) {
        conteudo += "$linha\n"
    }

    override fun toString(): String = conteudo.trimEnd()
}

// ============================================================================
// 1. Funções de Expressão Única
// ============================================================================
// Sintaxe enxuta: tipo de retorno inferido ou explícito com '='
fun calcularIndentacao(nivel: Int, tamanhoEspaco: Int = 4): Int = nivel * tamanhoEspaco

fun gerarEspacos(quantidade: Int): String = " ".repeat(quantidade)

// ============================================================================
// 2. Parâmetros com Valores Padrão e Argumentos Nomeados
// ============================================================================
fun formatarDeclaracaoVariavel(
    nome: String,
    tipo: String,
    valorInicial: String? = null,
    ehConstante: Boolean = false,
    alinhamentoColunas: Int = 0
): String {
    val prefixo = if (ehConstante) "const val" else "val"
    val inicializacao = if (valorInicial != null) " = $valorInicial" else ""
    val declaracao = "$prefixo $nome: $tipo$inicializacao;"
    return declaracao.padEnd(alinhamentoColunas)
}

// ============================================================================
// 3. Demonstração do Modelo de Passagem de Parâmetros
// ============================================================================
fun tentarModificarPrimitivo(mutavel: Int) {
    // mutavel = mutavel + 1 // ERRO DE COMPILAÇÃO: 'Val cannot be reassigned'. Parâmetros são sempre 'val'!
    val novoValorLocal = mutavel + 1
    println("    Dentro da função: valor local calculado = $novoValorLocal")
}

fun modificarObjetoPorReferencia(buffer: BufferDeCodigo) {
    // buffer = BufferDeCodigo() // ERRO DE COMPILAÇÃO: não podemos alterar a referência do parâmetro!
    
    // Porém, o estado INTERNO do objeto apontado pela referência pode ser alterado:
    buffer.anexarLinha("  // Linha injetada pela função modificarObjetoPorReferencia")
    buffer.anexarLinha("  int resultado = 42;")
}

fun main() {
    println("==================================================")
    println("     SUBPROGRAMAS E PASSAGEM DE PARÂMETROS        ")
    println("==================================================\n")

    // --- 1. Funções de Expressão Única ---
    println(">>> 1. FUNÇÕES DE EXPRESSÃO ÚNICA (SINGLE-EXPRESSION):")
    val espacosNivel2 = calcularIndentacao(2)
    println("  Indentação para nível 2 (padrão 4 espaços): $espacosNivel2 espaços")
    val espacosCustom = calcularIndentacao(nivel = 3, tamanhoEspaco = 2)
    println("  Indentação para nível 3 (customizado 2 espaços): $espacosCustom espaços\n")

    // --- 2. Parâmetros Padrão e Argumentos Nomeados ---
    println(">>> 2. VALORES PADRÃO E ARGUMENTOS NOMEADOS:")
    
    // Chamada posicional utilizando valores padrão
    val decl1 = formatarDeclaracaoVariavel("idade", "Int", "25")
    println("  Chamada posicional: $decl1")

    // Chamada com argumentos nomeados em qualquer ordem!
    val decl2 = formatarDeclaracaoVariavel(
        valorInicial = "\"Sucesso\"",
        tipo = "String",
        ehConstante = true,
        nome = "MENSAGEM_PADRAO"
    )
    println("  Chamada nomeada fora de ordem: $decl2\n")

    // --- 3. Modelo de Passagem de Parâmetros (Por Valor de Dados vs Referências) ---
    println(">>> 3. MODELO DE PASSAGEM DE PARÂMETROS:")
    
    println("  A) Tipos Primitivos (Passagem por valor da cópia do dado):")
    val contadorOriginal = 10
    println("    Antes da chamada: contadorOriginal = $contadorOriginal")
    tentarModificarPrimitivo(contadorOriginal)
    println("    Após a chamada:   contadorOriginal = $contadorOriginal (permanece inalterado!)\n")

    println("  B) Objetos (Passagem por cópia do valor da referência):")
    val bufferPrograma = BufferDeCodigo()
    bufferPrograma.anexarLinha("// Cabeçalho do programa em C gerado")
    
    println("    Conteúdo do buffer ANTES da função:")
    println("    -----------------------------------")
    println(bufferPrograma.toString())
    
    modificarObjetoPorReferencia(bufferPrograma)
    
    println("    Conteúdo do buffer DEPOIS da função:")
    println("    -----------------------------------")
    println(bufferPrograma.toString())

    println("\n==================================================")
    println("Conclusão: Em Kotlin os parâmetros são sempre 'val'.")
    println("Não há ponteiros manuais, nem passagem por referência nua.")
    println("==================================================")
}
