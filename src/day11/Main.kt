package day11

import java.io.File
import java.math.BigInteger

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "55312"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    runStep2(sample)
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String = helper(input, 25)

fun runStep2(input: File): String = helper(input, 75)

fun helper(input: File, runCount: Int): String {
    val stones = input.readLines().first().split(" ").map { it.toBigInteger() }
    // Map<Possibilities, Count>
    var counters = mapOf<BigInteger, BigInteger>()
    val newMap = HashMap<BigInteger, BigInteger>()
    stones.forEach { stone ->
        newMap[stone] = counters.getOrElse(stone, { BigInteger.ZERO }).add(BigInteger.ONE)
    }
    counters = newMap
    (0 until runCount).forEach {
        val oldCounters = counters
        val newCounters = mutableMapOf<BigInteger, BigInteger>()
        oldCounters.forEach { (stone, count) ->
            when {
                stone == BigInteger.ZERO -> newCounters[BigInteger.ONE] =
                    newCounters.getOrElse(BigInteger.ONE, { BigInteger.ZERO }).add(count)

                stone.toString().length % 2 == 0 -> {
                    val stoneString = stone.toString()
                    val key1 = stoneString.substring(0, stoneString.length / 2).toBigInteger()
                    val key2 = stoneString.substring(stoneString.length / 2, stoneString.length).toBigInteger()
                    newCounters[key1] = newCounters.getOrElse(key1, { BigInteger.ZERO }).add(count)
                    newCounters[key2] = newCounters.getOrElse(key2, { BigInteger.ZERO }).add(count)
                }

                else -> {
                    val newKey = stone.times("2024".toBigInteger())
                    newCounters[newKey] = newCounters.getOrElse(newKey, { BigInteger.ZERO }).add(count)
                }
            }
        }
        counters = newCounters
    }
    return counters.values.fold(BigInteger.ZERO) { acc, next -> acc.add(next) }.toString()
}
