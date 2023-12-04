package day04

import java.io.File
import java.lang.Math.pow

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    require(runStep1(sample) == "13") { "Failed sample, got ${runStep1(sample)}" }
    println("Step 1 answer: ${runStep1(input1)}")
    require(runStep2(sample) == "30") { "Failed sample, got ${runStep2(sample)}" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String = input.readLines()
    .asSequence()
    .filter { it.isNotBlank() }
    .map { it.split(": ")[1] }
    .map { it.replace("  ", " ").trim() }
    .map { it.split(" | ").let { Pair(it[0], it[1]) } }
    .sumOf { (winning, yours) ->
        val winningNumbers = winning.split(" ").map { it.toInt() }
        val yourNumbers = yours.split(" ").map { it.toInt() }
        yourNumbers.filter { it in winningNumbers }.size.let { if (it == 0) 0.0 else pow(2.0, (it - 1).toDouble()) }
    }.toInt().toString()

fun runStep2(input: File): String {
    val indexToCount = HashMap<Int, Int>()
    input.readLines()
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.split(": ")[1] }
        .map { it.replace("  ", " ").trim() }
        .map { it.split(" | ").let { Pair(it[0], it[1]) } }
        .mapIndexed { index, (winning, yours) ->
            indexToCount[index] = (indexToCount[index] ?: 0) + 1
            val winningNumbers = winning.split(" ").map { it.toInt() }
            val yourNumbers = yours.split(" ").map { it.toInt() }
            val score = yourNumbers.filter { it in winningNumbers }.size
            repeat(score) {
                indexToCount[index + it + 1] = (indexToCount[index + it + 1] ?: 0) + indexToCount[index]!!
            }
        }
        .toList()
    return indexToCount.values.sum().toString()
}
