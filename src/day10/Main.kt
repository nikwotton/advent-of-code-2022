package day10

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "36"
    val step2SampleExpected = "81"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

data class Position(
    val x: Int,
    val y: Int,
    val height: Int,
    var up: Position? = null,
    var down: Position? = null,
    var left: Position? = null,
    var right: Position? = null
)

fun runStep1(input: File): String {
    val map = input.readLines().mapIndexed { y, s -> s.mapIndexed { x, c -> Position(x, y, c.digitToInt()) } }
    map.forEachIndexed { y, positions ->
        positions.forEachIndexed { x, position ->
            position.up = map.getOrNull(y - 1)?.getOrNull(x)
            position.down = map.getOrNull(y + 1)?.getOrNull(x)
            position.left = map.getOrNull(y)?.getOrNull(x - 1)
            position.right = map.getOrNull(y)?.getOrNull(x + 1)
        }
    }
    fun helper(p: Position): List<Position> =
        if (p.height == 9)
            listOf(p)
        else
            listOfNotNull(p.up, p.down, p.left, p.right).filter { it.height == p.height + 1 }
                .map { helper(it) }.flatten()
    return map.flatten().filter { it.height == 0 }.map {
        helper(it).map { "${it.x},${it.y}" }.toSet()
    }.flatten().count().toString()
}

fun runStep2(input: File): String {
    val map = input.readLines().mapIndexed { y, s -> s.mapIndexed { x, c -> Position(x, y, c.digitToInt()) } }
    map.forEachIndexed { y, positions ->
        positions.forEachIndexed { x, position ->
            position.up = map.getOrNull(y - 1)?.getOrNull(x)
            position.down = map.getOrNull(y + 1)?.getOrNull(x)
            position.left = map.getOrNull(y)?.getOrNull(x - 1)
            position.right = map.getOrNull(y)?.getOrNull(x + 1)
        }
    }
    fun helper(p: Position): List<Position> =
        if (p.height == 9)
            listOf(p)
        else
            listOfNotNull(p.up, p.down, p.left, p.right).filter { it.height == p.height + 1 }
                .map { helper(it) }.flatten()
    return map.flatten().filter { it.height == 0 }.map {
        helper(it)
    }.flatten().count().toString()
}
