Exemplos de Código-Fonte da Linguagem de Origem: Kotlin

Este diretório contém os códigos-fonte de exemplo na linguagem de origem Kotlin) desenvolvidos para a disciplina de Compiladores Cada arquivo foi projetado de forma modular, cobrindo integralmente os tópicos apresentados nos slides do seminário, com ênfase na legibilidade, comentários didáticos e aplicações práticas voltadas à engenharia de compiladores e transpiladores.

---

 Compilador Utilizado e Pré-requisitos

Para compilar e executar os códigos deste diretório, é necessário o Kotlin Compiler (`kotlinc`) e uma versão do Java Runtime Environment (JRE/JDK 11 ou superior).

* Linguagem: Kotlin (versão 1.9+ ou 2.0+)
* Compilador CLI: `kotlinc` (Kotlin Compiler para a plataforma JVM)
* Ambiente de Execução: JVM (Java Virtual Machine / OpenJDK)




Instruções completas de instalação do compilador Kotlin (`kotlinc`) em diversas plataformas


aqui está o guia completo de instalação do compilador kotlin (kotlinc) e os detalhes práticos de como funciona a compilação manual:
  ──────
  ## 1. pré-requisito fundamental

  como o compilador padrão do kotlin roda sobre a jvm e produz bytecode java, é necessário ter o java (jdk 11 ou superior) instalado.

  para verificar se o java já está instalado, execute:

    java -version
  ──────
  ## 2. como instalar o kotlinc

  ### no linux

  #### opção a: via sdkman! (recomendado oficialmente pela jetbrains)

  o sdkman gerencia versões do kotlin e java com facilidade:
    # 1. instalar o sdkman
    curl -s "https://get.sdkman.io" | bash
    source "$home/.sdkman/bin/sdkman-init.sh"

    # 2. instalar o compilador kotlin
    sdk install kotlin

    # 3. conferir a versão
    kotlinc -version

  #### opção b: via gerenciador de pacotes do sistema

  • ubuntu / debian / mint:
    sudo snap install --classic kotlin

  • arch linux / manjaro:
    sudo pacman -s kotlin
  • fedora / rhel: o método mais estável é via sdkman (opção a) ou pelo download manual do binário.
  ──────
  ### no macos
  #### opção a: via homebrew

    brew install kotlin

  #### opção b: via sdkman!
    curl -s "https://get.sdkman.io" | bash
    source "$home/.sdkman/bin/sdkman-init.sh"
    sdk install kotlin
  ──────
  ### no windows

  #### opção a: via winget (nativo do windows 10/11)

  abra o powershell ou prompt de comando e execute:

    winget install jetbrains.kotlincompiler

  #### opção b: via scoop ou chocolatey

    # via scoop:
    scoop install kotlin

    # ou via chocolatey:
    choco install kotlinc

  #### opção c: download manual (arquivo zip)
  1. baixe o pacote oficial kotlin-compiler-<versão>.zip na página de releases do github da jetbrains https://github.
  com/jetbrains/kotlin/releases/latest.
  2. extraia o conteúdo em um diretório fixo (por exemplo: c:\program files\kotlinc).
  3. adicione a pasta c:\program files\kotlinc\bin à variável de ambiente path do windows.
  4. reinicie o terminal e teste com kotlinc -version.
  ──────
  ## ⚙ 3. detalhes da compilação manual
  o kotlin oferece comandos de linha para compilação e execução direta.
  ### 1. compilação para jar autônomo (método padrão)
  para distribuir ou rodar seu programa em qualquer computador que tenha java:

    kotlinc arquivo.kt -include-runtime -d programa.jar

  • o que faz o -include-runtime?
  empacota a biblioteca padrão do kotlin (kotlin-stdlib) dentro do arquivo .jar. sem essa flag, o jar exigiria que as bibliotecas do kotlin
  estivessem configuradas separadamente no classpath.
  • o que faz o -d programa.jar?
  define o destino de saída (destination), gerando o executável.

  como executar o jar gerado:
    java -jar programa.jar
  ──────
  ### 2. compilação para arquivos de classe (.class)

  se você preferir gerar os arquivos .class soltos em uma pasta:

    # compila e joga as classes compiladas dentro da pasta bin/
    kotlinc arquivo.kt -d ./bin

    # executa informando o classpath e a classe gerada:
    # (o kotlin gera a classe principal nomeando-a como arquivokt)
    kotlin -cp ./bin arquivokt
  ──────
  ### 3. compilação de múltiplos arquivos

  se o seu projeto for composto por vários arquivos .kt:

    kotlinc arquivo1.kt arquivo2.kt modulo.kt -include-runtime -d build_completo.jar
    java -jar build_completo.jar
  ──────
  ### 4. execução direta sem gerar jar

  nas versões recentes do kotlin, você pode executar um arquivo .kt diretamente sem compilar previamente de forma explícita:

    kotlin arquivo.kt
  ──────
  │ [!note]
  │ você também encontra essas instruções salvas no arquivo exemplos-origem/readme.md.



Tabela de Arquivos e Tópicos Cobertos

Abaixo estão listados todos os 14 arquivos desenvolvidos, associando cada tema dos slides aos conceitos abordados:

| Arquivo | Tópico do Seminário | Conceitos Abordados no Código |
| :--- | :--- | :--- |
| `01_hello_world_paradigmas.kt` | Hello World & Paradigmas | Função `main`, estilos imperativo, orientado a objetos e funcional no mesmo programa. |
| `02_tipos_dados_e_variaveis.kt` | Tipos de Dados & Variáveis | Primitivos (`Int`, `Long`, `Float`, `Double`, `Boolean`, `Char`, `String`), `val` vs `var`, escopos (global, membro, local) e Null Safety (`?`, `?.`, `?:`, `!!`). |
| `03_estruturas_condicionais.kt` | Condicionais & Fluxo | `if/else` como comando e como expressão de retorno; `when` com faixas (`in`), tipos (`is`) e múltiplos valores. |
| `04_estruturas_repeticao_e_desvios.kt` | Laços & Desvios | `for` com intervalos (`..`, `until`, `downTo`, `step`), `while`, `do-while`, desvios simples e rotulados (`break@rotulo`, `continue@rotulo`). |
| `05_expressoes_e_sobrecarga_operadores.kt` | Expressões & Operadores | Precedência, associatividade e sobrecarga de operadores (`plus`, `minus`, `times`, `unaryMinus`, `compareTo`, `get`, `contains`). |
| `06_subprogramas_e_passagem_parametros.kt` | Subprogramas & Chamadas | Funções (`fun`), expressão única (`=`), parâmetros padrão, argumentos nomeados e passagem de parâmetros por valor. |
| `07_funcoes_alta_ordem_e_lambdas.kt` | Lambdas & Ordem Superior | Funções de ordem superior (HOF), trailing lambda, parâmetro implícito `it`, pipelines com `filter`, `map`, `fold` e fábricas de funções. |
| `08_colecoes_e_estruturas_dados.kt` | Coleções & Ponteiros/Ref. | Arrays, Listas (`listOf`/`mutableListOf`), Sets e Maps na Tabela de Símbolos; referências vs ausência de ponteiros crus; Garbage Collector. |
| `09_orientacao_objetos_e_classes.kt` | Paradigma Orientado a Objetos | Classes, construtores primários/secundários, herança com `open`/`override`, interfaces com default methods, `data class` e Singleton `object`. |
| `10_delegacao_e_generics.kt` | Delegação & Generics | Delegação de classes por composição (`by`), propriedades delegadas (`by lazy`, `by Delegates.observable`), funções e classes genéricas (`<T>`). |
| `11_tratamento_excecoes_e_recursos.kt` | Tratamento de Exceções | Exceções não-verificadas (unchecked), `try` como expressão com retorno de valor, exceções personalizadas e fechamento de recursos com `.use { }`. |
| `12_concorrencia_e_sincronizacao.kt` | Concorrência & Sincronização | Threads e corrotinas, estado compartilhado, operações atômicas (`AtomicInteger`), exclusão mútua (`withLock`) e canais produtores-consumidores. |
| `13_tratamento_eventos_e_reatividade.kt` | Tratamento de Eventos | Barramento de Eventos (Event Bus), sealed classes tipadas, desacoplamento Observer e interfaces funcionais SAM. |
| `14_metaprogramacao_extensoes_dsl.kt` | Extensões, Infix & DSLs | Funções e propriedades de extensão (`fun String.ext()`), notação infixa (`infix fun`), Lambdas com receptor e Type-Safe Builders. |

---

bservação de Compatibilidade: Todos os arquivos foram desenvolvidos utilizando unicamente a biblioteca padrão do Kotlin (`kotlin-stdlib`), não necessitando de dependências externas ou configurações complexas de compilação.
