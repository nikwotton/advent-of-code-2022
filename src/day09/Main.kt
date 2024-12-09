package day09

import java.io.File
import java.math.BigInteger

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "1928"
    val step2SampleExpected = "2858"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val startingArray = mutableListOf<Int>()
    var isBlock = true
    var index = 0
    input.readLines().first().forEach {
        if (isBlock) {
            (0 until it.digitToInt()).forEach { startingArray.add(index) }
            isBlock = false
        } else {
            (0 until it.digitToInt()).forEach { startingArray.add(-1) }
            isBlock = true
            index++
        }
    }
    while (startingArray.last() == -1) {
        startingArray.removeLast()
    }
    while (startingArray.contains(-1)) {
        val last = startingArray.removeLast()
        val i = startingArray.indexOfFirst { it == -1 }
        startingArray.removeAt(i)
        startingArray.add(i, last)
        while (startingArray.last() == -1) {
            startingArray.removeLast()
        }
    }
    return startingArray.mapIndexed { i, c -> i.toBigInteger() * c.toBigInteger() }
        .fold(BigInteger.ZERO) { acc, i ->
            acc.plus(i)
        }.toString()
}

fun runStep2(input: File): String {
    val startingArray = mutableListOf<Int>()
    var isBlock = true
    var index = 0
    input.readLines().first().forEach {
        if (isBlock) {
            (0 until it.digitToInt()).forEach { startingArray.add(index) }
            isBlock = false
        } else {
            (0 until it.digitToInt()).forEach { startingArray.add(-1) }
            isBlock = true
            index++
        }
    }
    (startingArray.last() downTo 0).forEach { last ->
        val size = startingArray.count { it == last }
        var foundSoFar = 0
        var index = 0
        var done = false
        startingArray.forEachIndexed { idx, i ->
            if (!done) {
                if (i != -1) {
                    foundSoFar = 0
                } else {
                    if (foundSoFar == 0) index = idx
                    foundSoFar++
                    if (foundSoFar == size) {
                        done = true
                    }
                }
            }
        }
        if (done) {
            (index until index + size).forEach { i ->
                startingArray.removeAt(i)
                startingArray.add(i, last)
                val lastLast = startingArray.lastIndexOf(last)
                startingArray.removeAt(lastLast)
                startingArray.add(lastLast, -1)
            }
        }
    }

    return startingArray.mapIndexed { i, c ->
        if (c == -1)
            BigInteger.ZERO
        else
            i.toBigInteger() * c.toBigInteger()
    }
        .fold(BigInteger.ZERO) { acc, i ->
            acc.plus(i)
        }.toString()
}
