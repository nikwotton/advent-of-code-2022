package day05

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "143"
    val step2SampleExpected = "123"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val rules = input.readLines().filter { it.contains('|') }.map { it.split("|").map { it.toInt() } }
    val updates = input.readLines().filter { it.contains(",") }.map { it.split(",").map { it.toInt() } }
    return updates.filter { list ->
        val relevantRules = rules.filter { it.all { it in list } }
        relevantRules.all {
            list.indexOf(it[0]) < list.indexOf(it[1])
        }
    }.map {
        require(it.size % 2 == 1)
        it[it.size / 2]
    }.sum().toString()
}

fun runStep2(input: File): String {
    val rules = input.readLines().filter { it.contains('|') }.map { it.split("|").map { it.toInt() } }
    val updates = input.readLines().filter { it.contains(",") }.map { it.split(",").map { it.toInt() } }
    return updates.filter { list ->
        val relevantRules = rules.filter { it.all { it in list } }
        relevantRules.any {
            list.indexOf(it[0]) >= list.indexOf(it[1])
        }
    }
        .map { list ->
            list.sortedWith { o1, o2 ->
                val rule = rules.first { o1 in it && o2 in it }
                if (o1 == rule[0])
                    -1
                else
                    1
            }
        }
        .map {
            require(it.size % 2 == 1)
            it[it.size / 2]
        }.sum().toString()
}
