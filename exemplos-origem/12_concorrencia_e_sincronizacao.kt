/**
 * Disciplina: Compiladores - Semestre 2026.2
 * Projeto: Transpilador com Linguagem de Origem Kotlin
 * Pasta: exemplos-origem
 * 
 * Arquivo: 12_concorrencia_e_sincronizacao.kt
 * Tópicos abordados:
 *  - Concorrência e Paralelismo em Kotlin/JVM:
 *      * Threads de execução paralela vs Corrotinas (suspensão sem bloqueio)
 *      * O problema da Condição de Corrida (Race Condition) em estado compartilhado
 *  - Controle e Sincronização:
 *      * Operações atômicas com 'AtomicInteger' (modificações livres de lock via hardware CAS)
 *      * Exclusão mútua com travas reentrantes ('ReentrantLock' / 'withLock')
 *      * Comunicação segura via Canais / Filas Produtor-Consumidor ('LinkedBlockingQueue')
 * 
 * Contexto aplicado:
 *  Compilador Concorrente: Múltiplas threads processando arquivos-fonte em paralelo,
 *  alimentando uma fila de instruções e atualizando métricas de compilação de forma atômica.
 */

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

// Mensagem transmitida pelo canal produtor-consumidor
data class MensagemCompilacao(val nomeArquivo: String, val linhasCompiladas: Int)

fun main() {
    println("==================================================")
    println("      CONCORRÊNCIA, SINCRONIZAÇÃO E CONTROLE      ")
    println("==================================================")\n

    // --- 1. Operações Atômicas (AtomicInteger) ---
    println(">>> 1. ESTADO COMPARTILHADO COM OPERAÇÕES ATÔMICAS:")
    val contadorInstrucoes = AtomicInteger(0)
    val threads = mutableListOf<Thread>()

    // Disparando 5 threads para incrementar simultaneamente
    for (i in 1..5) {
        val t = thread(start = true, name = "Worker-$i") {
            for (j in 1..100) {
                // Operação atômica thread-safe (sem necessidade de lock pesado)
                contadorInstrucoes.incrementAndGet()
            }
        }
        threads.add(t)
    }

    // Aguardando término das threads (join)
    threads.forEach { it.join() }
    println("  Total de instruções contadas atomicamente (esperado 500): ${contadorInstrucoes.get()}\n")

    // --- 2. Exclusão Mútua com ReentrantLock (withLock idiomático) ---
    println(">>> 2. EXCLUSÃO MÚTUA COM MUTEX / REENTRANTLOCK:")
    val travaExclusao = ReentrantLock()
    val registroErrosCompartilhado = mutableListOf<String>()

    val threadsLock = (1..3).map { workerId ->
        thread(start = true) {
            // Em Kotlin, 'lock.withLock { }' garante acquire e release automático via try-finally
            travaExclusao.withLock {
                registroErrosCompartilhado.add("Alerta registrado com segurança pelo Worker-$workerId.")
            }
        }
    }
    threadsLock.forEach { it.join() }

    registroErrosCompartilhado.forEach { println("    $it") }
    println()

    // --- 3. Canais de Mensagens: Padrão Produtor-Consumidor ---
    println(">>> 3. COMUNICAÇÃO BASEADA EM MENSAGENS (CANAIS / PRODUTOR-CONSUMIDOR):")
    // Em corrotinas usa-se 'Channel<T>()'. Na JVM padrão, LinkedBlockingQueue simula canais com segurança
    val canalCompilacao = LinkedBlockingQueue<MensagemCompilacao>()
    val arquivosFonte = listOf("Lexer.kt", "Parser.kt", "AstNode.kt", "CodeGen.kt")

    // Thread Produtora
    val threadProdutora = thread(start = true, name = "Produtor-Scanner") {
        for (arq in arquivosFonte) {
            Thread.sleep(30) // Simula tempo de leitura e parsing
            val msg = MensagemCompilacao(arq, (50..200).random())
            canalCompilacao.put(msg) // Envia para o canal
            println("  [Produtor] Enviou: $arq (${msg.linhasCompiladas} linhas)")
        }
    }

    // Thread Consumidora
    val threadConsumidora = thread(start = true, name = "Consumidor-Emissor") {
        var processados = 0
        while (processados < arquivosFonte.size) {
            val msg = canalCompilacao.poll(500, TimeUnit.MILLISECONDS)
            if (msg != null) {
                println("    [Consumidor] Recebeu e transpilou: ${msg.nomeArquivo}")
                processados++
            }
        }
    }

    threadProdutora.join()
    threadConsumidora.join()

    println("\n==================================================")
    println("Conclusão: Seja através de Atomics, Locks ou Canais,")
    println("Kotlin oferece ferramentas robustas para evitar condições de corrida.")
    println("==================================================")
}
