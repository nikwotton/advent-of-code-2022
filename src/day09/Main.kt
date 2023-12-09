package day09

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == "114") { "Failed sample in step 1, got $step1Sample" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == "2") { "Failed sample in step 2, got $step2Sample" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String = input.readLines().sumOf {
    val starting = it.split(" ").map { it.toLong() }
    fun List<Long>.nextThing(): Long = if (this.all { it == 0L }) 0L
    else this.windowed(2).map { it[1] - it[0] }.let { it.last() + it.nextThing() }.toLong()
    starting.last() + starting.nextThing()
}.toString()

fun runStep2(input: File): String = input.readLines().sumOf {
    val starting = it.split(" ").map { it.toLong() }
    fun List<Long>.prevThing(): Long = if (this.all { it == 0L }) 0L
    else this.windowed(2).map { it[1] - it[0] }.let { it.first() - it.prevThing() }.toLong()
    starting.first() - starting.prevThing()
}.toString()
