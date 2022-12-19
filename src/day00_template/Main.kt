package day00_template

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
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
