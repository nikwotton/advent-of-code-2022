package day07

import java.io.File
import java.math.BigInteger

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "3749"
    val step2SampleExpected = "11387"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String = input.readLines().map { line ->
    val (outputString, inputsString) = line.split(": ")
    val output = outputString.toBigInteger()
    val inputs = inputsString.split(" ").map { it.toBigInteger() }
    if (testRun(inputs, output)) output else "0".toBigInteger()
}.fold("0".toBigInteger(), { acc, a -> acc.add(a) }).toString()

fun testRun(inputs: List<BigInteger>, output: BigInteger): Boolean = if (inputs.size == 1) {
    inputs[0] == output
} else {
    testRun(arrayListOf(inputs[0] + inputs[1]) + inputs.drop(2), output) ||
            testRun(arrayListOf(inputs[0] * inputs[1]) + inputs.drop(2), output)
}

fun runStep2(input: File): String = input.readLines().map { line ->
    val (outputString, inputsString) = line.split(": ")
    val output = outputString.toBigInteger()
    val inputs = inputsString.split(" ").map { it.toBigInteger() }
    if (testRun2(inputs, output)) output else "0".toBigInteger()
}.fold("0".toBigInteger(), { acc, a -> acc.add(a) }).toString()

fun testRun2(inputs: List<BigInteger>, output: BigInteger): Boolean = if (inputs.size == 1) {
    inputs[0] == output
} else {
    testRun2(arrayListOf(inputs[0] + inputs[1]) + inputs.drop(2), output) ||
            testRun2(arrayListOf(inputs[0] * inputs[1]) + inputs.drop(2), output) ||
            testRun2(
                arrayListOf((inputs[0].toString() + inputs[1].toString()).toBigInteger()) + inputs.drop(2),
                output
            )
}
