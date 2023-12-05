package day05

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    require(runStep1(sample) == "35") { "Failed sample, got ${runStep1(sample)}" }
    println("Step 1 answer: ${runStep1(input1)}")
    require(runStep2(sample) == "46") { "Failed sample, got ${runStep2(sample)}" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val seeds = input.readLines().first().removePrefix("seeds: ").split(" ").map { it.toLong() }
    val maps = input.readText().split("\n\n").drop(1).map {
        it.split("\n").filter { it.isNotBlank() }.drop(1).map { it.split(" ").map { it.toLong() } }.map { (destination, source, length) ->
            { input: Long ->
                if (input >= source && input < source + length)
                    destination + (input - source)
                else
                    null
            }
        }
    }
    return seeds.minOfOrNull {
        maps.fold(it) { acc, mapOptions ->
            mapOptions.mapNotNull { it(acc) }.let { if (it.isEmpty()) acc else it.first() }
        }
    }.toString()
}

fun runStep2(input: File): String {
    val seeds = input.readLines().first().removePrefix("seeds: ").split(" ").map { it.toLong() }.windowed(2, 2).map { LongRange(it[0], it[0] + it[1]) }.toMutableList()
    input.readText().split("\n\n").drop(1).map { mapSet ->
        val toHandle = seeds.toList().toMutableList()
        seeds.clear()
        mapSet.split("\n").filter { it.isNotBlank() }.drop(1).map { it.split(" ").map { it.toLong() } }.forEach { (destination, source, length) ->
            seeds.addAll(toHandle.toList().flatMap {
                if (it.first >= source + length || it.last < source) {
                    emptyList()
                } else if (it.first <= source && it.last >= source + length - 1) {
                    toHandle.remove(it)
                    if (it.first < source) {
                        toHandle.add(LongRange(it.first, source - 1))
                    }
                    if (it.last > source + length - 1) {
                        toHandle.add(LongRange(source + length, it.last))
                    }
                    listOf(LongRange(destination, destination + length - 1))
                } else if (it.first >= source && it.last <= source + length - 1) {
                    toHandle.remove(it)
                    listOf(LongRange(destination + (it.first - source), destination + (it.first - source) + (it.last - it.first)))
                } else if (it.first < source) {
                    toHandle.remove(it)
                    toHandle.add(LongRange(it.first, source - 1))
                    listOf(LongRange(destination, destination + length - (source - 1 - it.first)))
                } else if (it.last > source + length - 1) {
                    toHandle.remove(it)
                    val overlap = LongRange(it.first, source + length - 1)
                    toHandle.add(LongRange(source + length, it.last))
                    listOf(LongRange(destination + length - 1 - (overlap.last - overlap.first), destination + length - 1))
                } else {
                    TODO("This should never be hit")
                }
            })
        }
        seeds.addAll(toHandle)
    }
    return seeds.minBy { it.first }.first.toString()
}
