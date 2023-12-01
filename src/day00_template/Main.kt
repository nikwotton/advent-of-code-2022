package day00_template

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    assert(runStep1(sample) == TODO())
    println("Step 1 answer: ${runStep1(input1)}")
    assert(runStep2(sample) == TODO())
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    input.readLines().forEach {
        TODO()
    }
    return TODO()
}

fun runStep2(input: File): String {
    input.readLines().forEach {
        TODO()
    }
    return TODO()
}
