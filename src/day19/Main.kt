package day19

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "6"
    val step2SampleExpected = "16"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val availablePatterns = input.readLines().first().split(", ")
    val desiredDesigns = input.readLines().drop(2)
    fun isPossible(pattern: String): Boolean = if (pattern in availablePatterns) true
    else availablePatterns.filter { pattern.startsWith(it) }.any { isPossible(pattern.removePrefix(it)) }
    return desiredDesigns.count { isPossible(it) }.toString()
}

fun runStep2(input: File): String {
    val availablePatterns = input.readLines().first().split(", ")
    val desiredDesigns = input.readLines().drop(2)
    val cache = HashMap<String, Long>()
    fun countPossible(pattern: String): Long = cache.getOrPut(pattern, {
        if (pattern in availablePatterns) 1 + (availablePatterns - pattern).filter { pattern.startsWith(it) }
            .sumOf { countPossible(pattern.removePrefix(it)) }
        else availablePatterns.filter { pattern.startsWith(it) }.sumOf { countPossible(pattern.removePrefix(it)) }
    })
    return desiredDesigns.sumOf { countPossible(it) }.toString()
}
