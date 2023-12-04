package day00_template

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    require(runStep1(sample) == "TODO(step1)") { "Failed sample, got ${runStep1(sample)}" }
    println("Step 1 answer: ${runStep1(input1)}")
    require(runStep2(sample) == "TODO(step2)") { "Failed sample, got ${runStep2(sample)}" }
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
