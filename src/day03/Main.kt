package day03

import java.io.File

const val workingDir = "src/day03"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    return input.readLines().sumOf {
        val itemSize = it.length / 2
        val first = it.take(itemSize)
        val second = it.takeLast(itemSize)
        val shared = first.first { it in second }
        if (shared.isUpperCase()) shared.toInt() - 'A'.toInt() + 27
        else shared.toInt() - 'a'.toInt() + 1
    }.toString()
}

fun runStep2(input: File): String {
    return input.readLines().chunked(3).sumOf { (a, b, c) ->
        val shared = a.first { it in b && it in c }
        if (shared.isUpperCase()) shared.toInt() - 'A'.toInt() + 27
        else shared.toInt() - 'a'.toInt() + 1
    }.toString()
}
