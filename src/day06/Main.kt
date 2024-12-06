package day06

import day06.Direction.*
import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "41"
    val step2SampleExpected = "6"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val map = input.readLines().map { it.toCharArray() }
    val startingY = map.indexOfFirst { it.contains('^') }
    val startingX = map[startingY].indexOf('^')
    val seenPoints = mutableSetOf<Position>()
    var currentPosition = Position(startingX, startingY)
    var currentDirection = UP
    while (currentPosition.x >= 0 && currentPosition.y >= 0 && currentPosition.x < map[0].size && currentPosition.y < map.size) {
        seenPoints.add(currentPosition)
        val nextPosition = when (currentDirection) {
            UP -> currentPosition.copy(y = currentPosition.y - 1)
            DOWN -> currentPosition.copy(y = currentPosition.y + 1)
            LEFT -> currentPosition.copy(x = currentPosition.x - 1)
            RIGHT -> currentPosition.copy(x = currentPosition.x + 1)
        }
        if (nextPosition.x < 0 || nextPosition.y < 0 || nextPosition.x >= map[0].size || nextPosition.y >= map.size) {
            break
        }
        val nextThing = map[nextPosition.y][nextPosition.x]
        if (nextThing == '#') {
            currentDirection = when (currentDirection) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        } else {
            currentPosition = nextPosition
        }
    }
    return seenPoints.size.toString()
}

data class Position(val x: Int, val y: Int)
enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun canExit(map: List<List<Char>>): Boolean {
    val maxLoop = 6000
    val startingY = map.indexOfFirst { it.contains('^') }
    val startingX = map[startingY].indexOf('^')
    val seenPoints = mutableSetOf<Position>()
    var currentPosition = Position(startingX, startingY)
    var currentDirection = UP
    var i = 0
    while (currentPosition.x >= 0 && currentPosition.y >= 0 && currentPosition.x < map[0].size && currentPosition.y < map.size) {
        if (i++ > maxLoop) {
            return false
        }
        seenPoints.add(currentPosition)
        val nextPosition = when (currentDirection) {
            UP -> currentPosition.copy(y = currentPosition.y - 1)
            DOWN -> currentPosition.copy(y = currentPosition.y + 1)
            LEFT -> currentPosition.copy(x = currentPosition.x - 1)
            RIGHT -> currentPosition.copy(x = currentPosition.x + 1)
        }
        if (nextPosition.x < 0 || nextPosition.y < 0 || nextPosition.x >= map[0].size || nextPosition.y >= map.size) {
            return true
        }
        val nextThing = map[nextPosition.y][nextPosition.x]
        if (nextThing == '#') {
            currentDirection = when (currentDirection) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        } else {
            currentPosition = nextPosition
        }
    }
    TODO()
}

fun runStep2(input: File): String {
    val map = input.readLines().map { it.toCharArray() }
    return map.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            !canExit(map.mapIndexed { innerY, s ->
                s.mapIndexed { innerX, c ->
                    if (innerY == y && innerX == x && c == '.') '#' else c
                }
            })
        }
    }.flatten().count { it }.toString()
}
