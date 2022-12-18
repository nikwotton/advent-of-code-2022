package day18

import java.io.File
import kotlin.math.abs

const val workingDir = "src/day18"

data class Position(val x: Int, val y: Int, val z: Int) {
    val immediatelyNextTo: List<Position> by lazy { listOf(
        Position(x-1, y, z),
        Position(x+1, y, z),
        Position(x, y-1, z),
        Position(x, y+1, z),
        Position(x, y, z-1),
        Position(x, y, z+1),
    ) }
}
typealias Air = Position
typealias Rock = Position
fun File.toRocks(): List<Rock> = readLines().map { it.split(",").map { it.toInt() } }.map { (x, y, z) -> Position(x, y, z) }

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 64
    println("Step 1b: ${runStep1(input1)}") // 3494
    println("Step 2a: ${runStep2(sample)}") // 58
    println("Step 2b: ${runStep2(input1)}") // 2062
}

fun runStep1(input: File): String {
    val rocks: List<Rock> = input.toRocks()
    val totalSides = rocks.size * 6
    require(rocks.size == rocks.toSet().size) { "Found duplicate: ${rocks - rocks.toSet()}" }
    val sharedSides = rocks.sumOf { rock -> rocks.map { it in rock.immediatelyNextTo }.count { it } }
    return (totalSides - sharedSides).toString()
}

fun runStep2(input: File): String {
    val rocks: List<Rock> = input.toRocks()
    val totalSides = rocks.size * 6
    require(rocks.size == rocks.toSet().size) { "Found duplicate: ${rocks - rocks.toSet()}" }

    val minX = rocks.minOf { it.x } - 1
    val maxX = rocks.maxOf { it.x } + 1
    val minY = rocks.minOf { it.y } - 1
    val maxY = rocks.maxOf { it.y } + 1
    val minZ = rocks.minOf { it.z } - 1
    val maxZ = rocks.maxOf { it.z } + 1
    val airs: List<Air> = (minX..maxX).flatMap { x ->
        (minY..maxY).flatMap { y ->
            (minZ..maxZ).map { z ->
                Position(x, y, z)
            }
        }
    }.filter { it !in rocks }

    fun Position.isBorderPosition() =
        x in listOf(minX, maxX) || y in listOf(minY, maxY) || z in listOf(minZ, maxZ)

    tailrec fun canEscape(possibilities: List<Position>, seen: List<Position> = listOf()): Boolean {
        if (possibilities.isEmpty()) return false
        val checking = possibilities.first()
        return if (checking in rocks) canEscape(possibilities - checking, seen + checking)
        else if (checking.isBorderPosition()) true
        else canEscape(possibilities + checking.immediatelyNextTo.filter { it !in possibilities && it !in seen } - checking, seen + checking)
    }
    fun Air.canEscape() = canEscape(listOf(this))

    val enclosedAirs = airs.filter { !it.canEscape() }

    val sharedSides = rocks.sumOf { rock -> (rocks + enclosedAirs).map { it in rock.immediatelyNextTo }.count { it } }

    return (totalSides - sharedSides).toString()
}
