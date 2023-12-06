package day06

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    require(runStep1(sample) == "288") { "Failed sample in step 1, got ${runStep1(sample)}" }
    println("Step 1 answer: ${runStep1(input1)}")
    require(runStep2(sample) == "71503") { "Failed sample in step 2, got ${runStep2(sample)}" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String = input.readLines().map { it.shrinkSpaces().split(" ").drop(1) }.let {
    val times = it.first().map { it.toInt() }
    val distances = it[1].map { it.toInt() }
    times.indices.map { index ->
        val time = times[index]
        val distance = distances[index]
        (0..time).map { holding ->
            (time - holding) * holding > distance
        }.count { it }
    }.fold(1) { acc, i -> acc * i }
}.toString()

fun String.shrinkSpaces(): String = fold("") { acc, char -> if (acc.endsWith(" ") && char == ' ') acc else "$acc$char" }

fun runStep2(input: File): String =
    input.readLines().map { it.shrinkSpaces().replace(" ", "").split(":").drop(1) }.let {
        val times = it.first().map { it.toLong() }
        val distances = it[1].map { it.toLong() }
        times.indices.map { index ->
            val time = times[index]
            val distance = distances[index]
            (0..time).map { holding ->
                (time - holding) * holding > distance
            }.count { it }
        }.fold(1) { acc, i -> acc * i }
    }.toString()
