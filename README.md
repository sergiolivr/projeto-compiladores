# Repositório da Disciplina de Compiladores — Semestre 2026.2

> **Projeto de um Transpilador de Código-Fonte** 
> **Linguagem de Origem:** Kotlin 


---

## Visão Geral do Projeto

Este repositório faz parte dos trabalhos práticos da disciplina de **Compiladores** do período letivo **2026.2**. O objetivo central do projeto consiste na concepção e implementação de um **Transpilador (*Source-to-Source Compiler*)**, cuja linguagem de origem (fonte) é **Kotlin**.

O transpilador tem por finalidade analisar programas escritos em Kotlin, submetê-los às fases canônicas de compilação (análise léxica, análise sintática, análise semântica e representação intermediária) e emitir código equivalente de destino em uma linguagem de mais baixo nível, com foco em previsibilidade e eficiência.

---


## Estrutura de Pastas do Repositório

```text
projeto/
├── README.md                      # Documentação principal do projeto e repositório
├── exemplos-origem/               # Códigos-fonte de exemplo na linguagem Kotlin
│   ├── README.md                  # Instruções de instalação do kotlinc, compilação e execução
│   ├── executar_exemplos.sh       # Script bash para compilar e rodar todos os exemplos
│   ├── 01_hello_world_paradigmas.kt
│   ├── 02_tipos_dados_e_variaveis.kt
│   ├── 03_estruturas_condicionais.kt
│   ├── 04_estruturas_repeticao_e_desvios.kt
│   ├── 05_expressoes_e_sobrecarga_operadores.kt
│   ├── 06_subprogramas_e_passagem_parametros.kt
│   ├── 07_funcoes_alta_ordem_e_lambdas.kt
│   ├── 08_colecoes_e_estruturas_dados.kt
│   ├── 09_orientacao_objetos_e_classes.kt
│   ├── 10_delegacao_e_generics.kt
│   ├── 11_tratamento_excecoes_e_recursos.kt
│   ├── 12_concorrencia_e_sincronizacao.kt
│   ├── 13_tratamento_eventos_e_reatividade.kt
│   └── 14_metaprogramacao_extensoes_dsl.kt
│
└── exemplos_origem -> exemplos-origem  # Link simbólico para compatibilidade de nomenclatura
```

---

## Cobertura dos Tópicos do Seminário nos Códigos de Exemplo

Foram implementados **14 arquivos de código-fonte** abrangendo integralmente todos os assuntos abordados nos slides do seminário. Cada arquivo explora os recursos em um cenário realista de engenharia de software e compiladores:

| # | Arquivo Fonte | Tópico do Seminário | Aplicação no Domínio de Compiladores |
| :-: | :--- | :--- | :--- |
| **01** | `01_hello_world_paradigmas.kt` | Introdução e Paradigmas | Execução multi-estilo (imperativo, orientado a objetos e funcional) do pipeline de inicialização do transpilador. |
| **02** | `02_tipos_dados_e_variaveis.kt` | Tipos Primitivos, Escopos e Null Safety | Módulo de telemetria e flags de compilação usando tipos estáticos e segurança de tipos anuláveis (`?`, `?:`). |
| **03** | `03_estruturas_condicionais.kt` | Comandos Condicionais (`if` e `when`) | Classificador Léxico (Lexer) categorizando palavras-chave e tokens com *smart casting* e faixas. |
| **04** | `04_estruturas_repeticao_e_desvios.kt` | Comandos de Repetição e Desvios | Varredura de matriz de código-fonte eliminando comentários com *labeled jumps* (`break@label`, `continue@label`). |
| **05** | `05_expressoes_e_sobrecarga_operadores.kt` | Expressões e Sobrecarga de Operadores | Modelagem de custos de nós da AST com sobrecarga de operadores (`+`, `*`, `- unário`, `<`, `>`, `[]`, `in`). |
| **06** | `06_subprogramas_e_passagem_parametros.kt` | Funções e Modelo de Parâmetros | Pretty-printer com argumentos nomeados/padrão e demonstração de passagem estrita por valor de dados e referências. |
| **07** | `07_funcoes_alta_ordem_e_lambdas.kt` | Lambdas e Funções de Ordem Superior | Otimizador de Código Intermediário (IR) com pipelines de transformação funcional (`filter`, `map`, `fold`). |
| **08** | `08_colecoes_e_estruturas_dados.kt` | Coleções e Gerenciamento de Memória | Tabela de Símbolos usando Listas, Sets e Maps; gerenciamento de ciclo de vida com Garbage Collector vs ponteiros. |
| **09** | `09_orientacao_objetos_e_classes.kt` | Classes, Herança e Polimorfismo | Hierarquia de nós polimórficos da Árvore Sintática Abstrata (AST) e fábrica Singleton com `object`. |
| **10** | `10_delegacao_e_generics.kt` | Delegação (`by`) e Generics (`<T>`) | Pilha genérica de escopos léxicos combinada com serviço de auditoria por composição delegada (`by`) e `by lazy`. |
| **11** | `11_tratamento_excecoes_e_recursos.kt` | Exceções e Gerenciamento de Recursos | Recuperação de falhas sintáticas com `try` como expressão e fechamento determinístico de arquivos com `.use { }`. |
| **12** | `12_concorrencia_e_sincronizacao.kt` | Concorrência e Sincronização | Compilador multithread com contadores atômicos (`AtomicInteger`), exclusão mútua (`withLock`) e fila produtora-consumidora. |
| **13** | `13_tratamento_eventos_e_reatividade.kt` | Tratamento de Eventos e Observer | Barramento de Eventos desacoplado para notificações do ciclo de vida da compilação com *sealed classes*. |
| **14** | `14_metaprogramacao_extensoes_dsl.kt` | Extensões, Infix e DSLs Type-Safe | Criação de uma Domain-Specific Language nativa em Kotlin para especificação de gramáticas livres de contexto e BNF. |

---

## Guia Rápido de Execução

Para instruções completas de instalação do compilador Kotlin (`kotlinc`) em diversas plataformas (Linux, macOS, Windows) e detalhes de compilação manual, consulte o arquivo dedicado:
 **[exemplos-origem/README.md](exemplos-origem/README.md)**

### Execução rápida via script automatizado:
```bash
cd exemplos-origem
chmod +x executar_exemplos.sh
./executar_exemplos.sh
```

---

## Critérios de Avaliação Atendidos

* **Cobertura dos tópicos:** Todos os conceitos apresentados nas três apresentações (slides 1 a 11) estão rigorosamente representados e demonstrados.
* **Organização e Modularidade:** Cada tema foi isolado em seu próprio arquivo fonte independente, nomeado com convenção numérica sequencial (`01` a `14`), facilitando o estudo progressivo.
* **Legibilidade e Comentários:** Cabeçalhos completos com metadados da disciplina, explicações conceituais detalhadas e formatação limpa no padrão idiomático do Kotlin.
* **Criatividade e Aplicabilidade:** Todos os exemplos utilizam problemas reais do desenvolvimento de compiladores (Tabela de Símbolos, AST, Otimização de IR, Lexer, DSLs Gramaticais e Concorrência).
* **Quantidade de Arquivos:** 14 arquivos de código-fonte (atendendo à faixa solicitada entre 10 e 20 arquivos).

---

**Disciplina:** Compiladores  
**Semestre Letivo:** 2026.2  
**Linguagem de Origem:** Kotlin (JetBrains)
