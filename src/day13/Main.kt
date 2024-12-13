package day13

import java.io.File
import kotlin.math.abs
import kotlin.math.round

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "480"
    val step2SampleExpected = "875318608908"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val aCost = 3
    val bCost = 1
    val maxPresses = 100
    return input.readLines().filter { it.isNotBlank() }.windowed(3, 3).map {
        val (aX, aY) = it[0].removePrefix("Button A: X+").split(", Y+").map { it.toInt() }
        val (bX, bY) = it[1].removePrefix("Button B: X+").split(", Y+").map { it.toInt() }
        val (pX, pY) = it[2].removePrefix("Prize: X=").split(", Y=").map { it.toInt() }
        var lowest = Int.MAX_VALUE
        (0..maxPresses).forEach { aPressCount ->
            (0..maxPresses).forEach { bPressCount ->
                val x = (aX * aPressCount) + (bX * bPressCount)
                val y = (aY * aPressCount) + (bY * bPressCount)
                if (x == pX && y == pY) {
                    val cost = (aPressCount * aCost) + (bPressCount * bCost)
                    if (cost < lowest) lowest = cost
                }
            }
        }
        if (lowest == Int.MAX_VALUE) 0 else lowest
    }.sum().toString()
}

fun inverse(input: List<List<Int>>): List<List<Double>> {
    val a = input[0][0]
    val b = input[0][1]
    val c = input[1][0]
    val d = input[1][1]
    val determinant = (a * d) - (b * c)
    return listOf(listOf(d, -1 * b), listOf(-1 * c, a)).map { it.map { it.toDouble() / determinant.toDouble() } }
}

operator fun List<List<Double>>.times(other: List<Double>): List<Double> {
    val a = this[0][0]
    val b = this[0][1]
    val c = this[1][0]
    val d = this[1][1]
    val x = other[0]
    val y = other[1]
    return listOf((a * x) + (b * y), (c * x) + (d * y))
}

fun runStep2(input: File): String = input.readLines().filter { it.isNotBlank() }.windowed(3, 3).map {
    val (aX, aY) = it[0].removePrefix("Button A: X+").split(", Y+").map { it.toInt() }
    val (bX, bY) = it[1].removePrefix("Button B: X+").split(", Y+").map { it.toInt() }
    val (pX, pY) = it[2].removePrefix("Prize: X=").split(", Y=").map { it.toDouble() + 10_000_000_000_000L }
    val mat = listOf(listOf(aX, bX), listOf(aY, bY))
    val matInv = inverse(mat)
    val res = matInv * listOf(pX, pY)
    val (rX, rY) = res
    if (abs(rX - round(rX)) < 0.001 && abs(rY - round(rY)) < 0.001) {
        (3 * round(rX)) + round(rY)
    } else 0.0
}.sum().toLong().toString()
