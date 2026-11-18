/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 13_tratamento_eventos_e_reatividade.kt
 * Tópicos abordados:
 *  - Tratamento de Eventos em Kotlin:
 *      * Modelos de eventos dependentes de plataforma:
 *          - Android: Listeners e Callbacks funcionais (SAM conversion)
 *          - Servidor/Geral: Streams, Barramentos de Eventos (Event Bus)
 *          - Desktop: Handlers e padrão Observer
 *      * Implementação do Padrão Observer com funções de alta ordem
 *      * Eventos fortemente tipados com Classes Seladas ('sealed class')
 * 
 * Contexto aplicado:
 *  Monitor de Eventos de Ciclo de Vida do Transpilador:
 *  notificação de início de compilação, término de módulo e emissão de alertas.
 */

// ============================================================================
// 1. Hierarquia de Eventos com Sealed Class (Eventos fortemente tipados)
// ============================================================================
sealed class EventoCompilacao {
    data class InicioCompilacao(val versao: String, val timestamp: Long = System.currentTimeMillis()) : EventoCompilacao()
    data class ModuloConcluido(val nomeModulo: String, val duracaoMs: Long) : EventoCompilacao()
    data class AlertaEmitido(val mensagem: String, val linha: Int) : EventoCompilacao()
    data class CompilacaoFinalizada(val totalErros: Int, val sucesso: Boolean) : EventoCompilacao()
}

// Tipo funcional (alias) para ouvintes de eventos
typealias OuvinteEvento = (EventoCompilacao) -> Unit

// ============================================================================
// 2. Barramento de Eventos (Event Bus / Observer Pattern)
// ============================================================================
class BarramentoDeEventosCompilador {
    private val ouvintes = mutableListOf<OuvinteEvento>()

    fun registrarOuvinte(ouvinte: OuvinteEvento) {
        ouvintes.add(ouvinte)
    }

    fun dispararEvento(evento: EventoCompilacao) {
        // Notifica todos os ouvintes registrados
        ouvintes.forEach { ouvinte -> ouvinte(evento) }
    }
}

// ============================================================================
// 3. Callback Tradicional (Estilo Android Listener com SAM interface)
// ============================================================================
fun interface ListenerProgresso {
    fun onProgresso(porcentagem: Int, mensagem: String)
}

class MotorTranspilacao(private val listenerProgresso: ListenerProgresso) {
    fun executar() {
        listenerProgresso.onProgresso(25, "Carregando arquivos de cabeçalho...")
        listenerProgresso.onProgresso(60, "Otimizando árvores sintáticas...")
        listenerProgresso.onProgresso(100, "Código C gerado com sucesso!")
    }
}

fun main() {
    println("==================================================")
    println("      TRATAMENTO DE EVENTOS E PADRÃO OBSERVER     ")
    println("=================================================="\n")

    // --- 1. Barramento de Eventos Desacoplado ---
    println(">>> 1. BARRAMENTO DE EVENTOS COM SEALED CLASSES:")
    val barramento = BarramentoDeEventosCompilador()

    // Ouvinte 1: Registrador de Log em Console
    barramento.registrarOuvinte { evento ->
        when (evento) {
            is EventoCompilacao.InicioCompilacao -> {
                println("  [LOG-CONSOLE] Início da compilação na versão ${evento.versao}")
            }
            is EventoCompilacao.ModuloConcluido -> {
                println("  [LOG-CONSOLE] Módulo '${evento.nomeModulo}' concluído em ${evento.duracaoMs}ms")
            }
            is EventoCompilacao.AlertaEmitido -> {
                println("  [LOG-CONSOLE] AVISO na linha ${evento.linha}: ${evento.mensagem}")
            }
            is EventoCompilacao.CompilacaoFinalizada -> {
                val resultado = if (evento.sucesso) "SUCESSO" else "FALHA"
                println("  [LOG-CONSOLE] Finalizado com $resultado (${evento.totalErros} erros).")
            }
        }
    }

    // Ouvinte 2: Coletor de Métricas para Telemetria
    var totalAlertas = 0
    barramento.registrarOuvinte { evento ->
        if (evento is EventoCompilacao.AlertaEmitido) {
            totalAlertas++
            println("    [METRICS-AGENT] Total de alertas acumulados: $totalAlertas")
        }
    }

    // Disparando sequência de eventos
    barramento.dispararEvento(EventoCompilacao.InicioCompilacao("2.0.26"))
    barramento.dispararEvento(EventoCompilacao.ModuloConcluido("AnaliseLexica", 45))
    barramento.dispararEvento(EventoCompilacao.AlertaEmitido("Variável declarada e nunca utilizada", linha = 24))
    barramento.dispararEvento(EventoCompilacao.ModuloConcluido("GeradorCodigoC", 112))
    barramento.dispararEvento(EventoCompilacao.CompilacaoFinalizada(totalErros = 0, sucesso = true))
    println()

    // --- 2. Callbacks com Interfaces Funcionais (SAM) ---
    println(">>> 2. CALLBACKS COM INTERFACES FUNCIONAIS (ESTILO ANDROID / DESKTOP):")
    // Em Kotlin, uma interface funcional com um único método abstrato pode ser instanciada como lambda!
    val motor = MotorTranspilacao { porcentagem, status ->
        println("  [Listener UI] Progresso: $porcentagem% - $status")
    }
    motor.executar()

    println("\n==================================================")
    println("Conclusão: O modelo de eventos em Kotlin varia conforme")
    println("o ambiente, mas aproveita a concisão de lambdas e interfaces funcionais.")
    println("==================================================")
}
