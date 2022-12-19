package day15

import java.io.File
import kotlin.math.abs

const val workingDir = "src/day15"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample, 10)}") // 26
    println("Step 1b: ${runStep1(input1, 2000000)}") // 5147333
    println("Step 2a: ${runStep2(sample, 20)}") // 56000011
    println("Step 2b: ${runStep2(input1, 4_000_000)}") // 13734006908372
}

data class Position(val x: Int, val y: Int) {
    fun distanceTo(other: Position) = abs(x - other.x) + abs(y - other.y)
    val tuningFrequency: Long by lazy { x.toLong() * 4_000_000L + y }
}

data class Sensor(val position: Position, val nearestBeacon: Beacon) {
    private val distanceToBeacon by lazy { position.distanceTo(nearestBeacon) }

    fun emptySpotsAtRow(y: Int): List<IntRange> {
        val dX = distanceToBeacon - abs(position.y - y)
        if (dX < 0) return emptyList()
        val minX = position.x - dX
        val maxX = position.x + dX
        return listOf(minX..maxX)
            .flatMap {
                if (position.y == y && position.x in it) {
                    when (position.x) {
                        it.first -> listOf((it.first + 1..it.last))
                        it.last -> listOf((it.first until it.last))
                        else -> listOf((it.first until position.x), (position.x + 1..it.last))
                    }
                } else listOf(it)
            }.flatMap {
                if (nearestBeacon.y == y && nearestBeacon.x in it) {
                    when (nearestBeacon.x) {
                        it.first -> listOf((it.first + 1..it.last))
                        it.last -> listOf((it.first until it.last))
                        else -> listOf((it.first until nearestBeacon.x), (nearestBeacon.x + 1..it.last))
                    }
                } else listOf(it)
            }
    }
}
typealias Beacon = Position

fun String.remove(str: String) = replace(str, "")
fun String.remove(vararg strs: String) = strs.fold(this) { acc, s -> acc.remove(s) }
fun File.toSensors() = readLines().map {
    val (p1, p2) = it
        .remove("Sensor at x=", ": closest beacon is at ", " ", "y=")
        .split("x=")
        .map { it.split(",").map { it.toInt() } }
        .map { (x, y) -> Position(x, y) }
    Sensor(p1, p2)
}

fun List<IntRange>.findHoles(min: Int, max: Int): List<Int> {
    val ret = ArrayList<Int>()
    if (minOf { it.first } > min) ret.addAll(min until minOf { it.first })
    if (maxOf { it.last } < max) ret.addAll(maxOf { it.last } + 1..max)
    var current = min
    filter { it.last >= min && it.first <= max }.sortedBy { it.first }.forEach {
        if (it.first <= current && it.last >= current) current = it.last + 1
        else if (it.last <= current) Unit
        else {
            ret.addAll(current until it.first)
            current = it.last + 1
        }
    }
    return ret
}

val IntRange.size: Int
    get() = (last - first) + 1

fun runStep1(input: File, rowNum: Int): String {
    val sensors = input.toSensors()
    val beacons = sensors.map { it.nearestBeacon }
    val takenSpots = sensors.map { it.position } + beacons
    val emptySpotsAtRow = input.toSensors().flatMap { it.emptySpotsAtRow(rowNum) }
    val taken = takenSpots.filter { it.y == rowNum }.map { it.x }
    return ((emptySpotsAtRow.minOf { it.first }..emptySpotsAtRow.maxOf { it.last }) - taken.toSet()).size.toString()
}

fun runStep2(input: File, maxCoord: Int): String {
    val sensors = input.toSensors()
    val beacons = sensors.map { it.nearestBeacon }
    val takenSpots = sensors.map { it.position } + beacons
    (0..maxCoord).forEach { y ->
        val taken = takenSpots.filter { it.y == y }.map { it.x }
        val foo = sensors.flatMap { it.emptySpotsAtRow(y) }.findHoles(0, maxCoord).filter { it !in taken }
        if (foo.isNotEmpty()) return Position(foo.first(), y).tuningFrequency.toString()
    }
    TODO()
}
