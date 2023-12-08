package day00_template

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == "TODO(step1)") { "Failed sample in step 1, got $step1Sample" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == "TODO(step2)") { "Failed sample in step 2, got $step2Sample" }
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
