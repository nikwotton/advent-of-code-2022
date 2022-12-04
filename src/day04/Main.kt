package day04

import java.io.File

const val workingDir = "src/day04"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    return input.readLines().map {
        val (a, b) = it.split(",")
        val (amin, amax) = a.split("-").map { it.toInt() }
        val (bmin, bmax) = b.split("-").map { it.toInt() }
        if ((amin <= bmin && amax >= bmax) || (bmin <= amin && bmax >= amax)) 1 else 0
    }.sum().toString()
}

fun runStep2(input: File): String {
    return input.readLines().map {
        val (a, b) = it.split(",")
        val (amin, amax) = a.split("-").map { it.toInt() }
        val (bmin, bmax) = b.split("-").map { it.toInt() }
        if (((amin..amax).intersect(bmin..bmax)).isEmpty()) 0 else 1
    }.sum().toString()
}
