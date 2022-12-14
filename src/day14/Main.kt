package day14

import java.io.File
import kotlin.random.Random
import kotlin.random.Random.Default

const val workingDir = "src/day14"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 24
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}") // 93
    println("Step 2b: ${runStep2(input1)}")
}

data class Position(val x: Int, val y: Int)
typealias Path = List<Position>

fun runStep1(input: File): String {
    val paths: List<Path> = input.readLines().map {
        it.split(" -> ").map { it.split(",").let { Position(it[0].toInt(), it[1].toInt()) } }
    }
    val blockingPoints = paths.flatMap { it.windowed(2).flatMap { (a, b) ->
        if (a.x == b.x) {
            (minOf(a.y, b.y)..maxOf(a.y, b.y)).map { Position(a.x, it) }
        } else if (a.y == b.y) {
            (minOf(a.x, b.x)..maxOf(a.x, b.x)).map { Position(it, a.y) }
        } else TODO()
    } }.toMutableList()
    fun lowerSand(sandPosition: Position): Position {
        if (!blockingPoints.contains(Position(sandPosition.x, sandPosition.y+1)))
            return Position(sandPosition.x, sandPosition.y+1)
        if (!blockingPoints.contains(Position(sandPosition.x-1, sandPosition.y+1)))
            return Position(sandPosition.x-1, sandPosition.y+1)
        if (!blockingPoints.contains(Position(sandPosition.x+1, sandPosition.y+1)))
            return Position(sandPosition.x+1, sandPosition.y+1)
        return sandPosition
    }
    val doneAt = blockingPoints.maxOf { it.y } + 1
    fun dropSand(): Boolean {
        var sandPosition = Position(500, 0)
        while(true) {
            val newSandPosition = lowerSand(sandPosition)
            if (newSandPosition == sandPosition) {
                blockingPoints.add(newSandPosition)
                return true
            }
            if (newSandPosition.y >= doneAt) return false
            sandPosition = newSandPosition
        }
    }
    var ret = 0
    while (dropSand()) ret++
    return ret.toString()
}

fun runStep2(input: File): String {
    val paths: List<Path> = input.readLines().map {
        it.split(" -> ").map { it.split(",").let { Position(it[0].toInt(), it[1].toInt()) } }
    }
    val blockingPoints = paths.flatMap { it.windowed(2).flatMap { (a, b) ->
        if (a.x == b.x) {
            (minOf(a.y, b.y)..maxOf(a.y, b.y)).map { Position(a.x, it) }
        } else if (a.y == b.y) {
            (minOf(a.x, b.x)..maxOf(a.x, b.x)).map { Position(it, a.y) }
        } else TODO()
    } }.toMutableList()
    fun lowerSand(sandPosition: Position): Position {
        if (!blockingPoints.contains(Position(sandPosition.x, sandPosition.y+1)))
            return Position(sandPosition.x, sandPosition.y+1)
        if (!blockingPoints.contains(Position(sandPosition.x-1, sandPosition.y+1)))
            return Position(sandPosition.x-1, sandPosition.y+1)
        if (!blockingPoints.contains(Position(sandPosition.x+1, sandPosition.y+1)))
            return Position(sandPosition.x+1, sandPosition.y+1)
        return sandPosition
    }
    val floor = blockingPoints.maxOf { it.y } + 2
    fun dropSand(): Boolean {
        var sandPosition = Position(500, 0)
        while(true) {
            val newSandPosition = lowerSand(sandPosition)
            if (newSandPosition == sandPosition || newSandPosition.y >= floor-1) {
                if (blockingPoints.contains(newSandPosition)) return false
                blockingPoints.add(newSandPosition)
                if (Random.nextInt(100) == 42)
                    blockingPoints.removeAll {
                        blockingPoints.contains(Position(it.x, it.y - 1)) &&
                                blockingPoints.contains(Position(it.x - 1, it.y - 1)) &&
                                blockingPoints.contains(Position(it.x + 1, it.y - 1))
                    }
                return true
            }
            if (newSandPosition == Position(500, 0)) return false
            sandPosition = newSandPosition
        }
    }
    var ret = 0
    while (dropSand()) {
        if (ret%100==0)
        println("At ret $ret with points ${blockingPoints.size} minx: ${blockingPoints.minOf { it.x }} maxX: ${blockingPoints.maxOf { it.x }} minY: ${blockingPoints.minOf{it.y}} maxY: ${blockingPoints.maxOf{it.y}}")
        ret++
    }
    return ret.toString()
}
