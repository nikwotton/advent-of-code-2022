package day11

import day11.Thing.GALAXY
import day11.Thing.SPACE
import java.io.File
import kotlin.math.abs

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == "374") { "Failed sample in step 1, got $step1Sample" }
    println("Step 1 answer: ${runStep1(input1)}")
//    val step2Sample = runStep2(sample)
//    require(step2Sample == "TODO(step2)") { "Failed sample in step 2, got $step2Sample" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val initial = input.readLines().map {
        it.map {
            when (it) {
                '.' -> SPACE
                '#' -> GALAXY
                else -> TODO("Should never hit here")
            }
        }
    }
    val emptyRowIndexes = initial.mapIndexed { index, things -> index to things.all { it == SPACE } }.filter { it.second }.map { it.first }
    val emptyColumnIndexes = initial.first().indices.map { index -> index to initial.map { it[index] }.all { it == SPACE } }.filter { it.second }.map { it.first }
    val modifiable = initial.map { it.toMutableList() }.toMutableList()
    emptyRowIndexes.reversed().forEach { index -> modifiable.add(index, Array(initial.first().size) { SPACE }.toMutableList()) }
    emptyColumnIndexes.reversed().forEach { index -> modifiable.forEach { row -> row.add(index, SPACE) } }
    val expanded = modifiable.map { it.toList() }.toList()
    val galaxies = expanded.mapIndexed { y, things -> things.mapIndexed { x, thing -> if (thing == GALAXY) x to y else null }.filterNotNull() }.flatten()
    return galaxies.mapIndexed { index, (x, y) ->
        val rest = galaxies.drop(index + 1)
        rest.sumOf { (nextX, nextY) ->
            abs(nextX - x) + abs(nextY - y)
        }
    }.sum().toString()
}

enum class Thing {
    GALAXY,
    SPACE
}

fun runStep2(input: File): String {
    val initial = input.readLines().map {
        it.map {
            when (it) {
                '.' -> SPACE
                '#' -> GALAXY
                else -> TODO("Should never hit here")
            }
        }
    }
    val emptyRowIndexes = initial.mapIndexed { index, things -> index to things.all { it == SPACE } }.filter { it.second }.map { it.first }
    val emptyColumnIndexes = initial.first().indices.map { index -> index to initial.map { it[index] }.all { it == SPACE } }.filter { it.second }.map { it.first }
    val galaxies = initial.mapIndexed { y, things -> things.mapIndexed { x, thing -> if (thing == GALAXY) x to y else null }.filterNotNull() }.flatten()
    return galaxies.mapIndexed { index, (x, y) ->
        val rest = galaxies.drop(index + 1)
        rest.sumOf { (nextX, nextY) ->
            val emptyRows = emptyRowIndexes.count {
                (it in y..nextY) || (it in nextY..y)
            }
            val emptyColumns = emptyColumnIndexes.count {
                (it in x..nextX) || (it in nextX..x)
            }
            abs(nextX.toLong() - x.toLong()) + abs(nextY.toLong() - y.toLong()) + ((emptyRows.toLong() + emptyColumns.toLong()) * 999_999.toLong())
        }
    }.sum().toString()
}
// 177_077_874 - too low
// 635832237682 - long vs int :facepalm:
