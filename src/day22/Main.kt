package day22

import day22.Direction.DOWN
import day22.Direction.LEFT
import day22.Direction.RIGHT
import day22.Direction.UP
import day22.Instruction.Move
import day22.Instruction.TURN_LEFT
import day22.Instruction.TURN_RIGHT
import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 6032
    println("Step 1b: ${runStep1(input1)}") // 196134
    println("Step 2a: ${runStep2(sample)}") // 5031
    println("Step 2b: ${runStep2(input1)}") // 146011
}

data class Point(val x: Int, val y: Int, val walkable: Boolean)
sealed class Instruction {
    data class Move(val distance: Int) : Instruction()
    object TURN_RIGHT : Instruction()
    object TURN_LEFT : Instruction()
}

sealed class Direction {
    object LEFT : Direction()
    object RIGHT : Direction()
    object UP : Direction()
    object DOWN : Direction()
}

tailrec fun String.toInstructions(acc: List<Instruction> = emptyList()): List<Instruction> {
    if (isEmpty()) return acc
    return if (first().isDigit()) {
        val num = takeWhile { it.isDigit() }.toInt()
        removePrefix(num.toString()).toInstructions(acc + Move(num))
    } else {
        val d = first()
        when (d) {
            'R' -> removePrefix(d.toString()).toInstructions(acc + TURN_RIGHT)
            'L' -> removePrefix(d.toString()).toInstructions(acc + TURN_LEFT)
            else -> TODO()
        }
    }
}

fun runStep1(input: File): String {
    val map = ArrayList<Point>()
    input.readLines().dropLast(2).forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            when (c) {
                ' ' -> Unit
                '.' -> map.add(Point(x+1, y + 1, true))
                '#' -> map.add(Point(x+1, y + 1, false))
            }
        }
    }
    val instructions = input.readLines().last().toInstructions()
    var currentPosition = map.filter { it.y == 1 }.minBy { it.x }
    var currentDirection: Direction = RIGHT
    instructions.forEach {
        when (it) {
            is Move -> {
                repeat(it.distance) {
                    currentPosition = when (currentDirection) {
                        DOWN -> {
                            val next = map.firstOrNull { it.x == currentPosition.x && it.y == currentPosition.y + 1 }
                                ?: map.first { it.x == currentPosition.x && it.y == map.filter { it.x == currentPosition.x }.minOf { it.y } }
                            if (next.walkable) next
                            else currentPosition
                        }
                        LEFT -> {
                            val next = map.firstOrNull { it.x == currentPosition.x - 1 && it.y == currentPosition.y }
                                ?: map.first { it.x == map.filter { it.y == currentPosition.y }.maxOf { it.x } && it.y == currentPosition.y }
                            if (next.walkable) next
                            else currentPosition
                        }
                        RIGHT -> {
                            val next = map.firstOrNull { it.x == currentPosition.x + 1 && it.y == currentPosition.y }
                                ?: map.first { it.x == map.filter { it.y == currentPosition.y }.minOf { it.x } && it.y == currentPosition.y }
                            if (next.walkable) next
                            else currentPosition
                        }
                        UP -> {
                            val next = map.firstOrNull { it.x == currentPosition.x && it.y == currentPosition.y - 1 }
                                ?: map.first { it.x == currentPosition.x && it.y == map.filter { it.x == currentPosition.x }.maxOf { it.y } }
                            if (next.walkable) next
                            else currentPosition
                        }
                    }
                }
            }
            TURN_LEFT -> currentDirection = when (currentDirection) {
                DOWN -> RIGHT
                LEFT -> DOWN
                RIGHT -> UP
                UP -> LEFT
            }
            TURN_RIGHT -> currentDirection = when (currentDirection) {
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
                UP -> RIGHT
            }
        }
    }
    return ((currentPosition.y * 1000) + (currentPosition.x * 4) + when(currentDirection) {
        LEFT -> 2
        RIGHT -> 0
        UP -> 3
        DOWN -> 1
    }).toString()
}

fun runStep2(input: File): String {
    val map = ArrayList<Point>()
    input.readLines().dropLast(2).forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            when (c) {
                ' ' -> Unit
                '.' -> map.add(Point(x+1, y + 1, true))
                '#' -> map.add(Point(x+1, y + 1, false))
            }
        }
    }
    val instructions = input.readLines().last().toInstructions()
    var currentPosition = map.filter { it.y == 1 }.minBy { it.x }
    var currentDirection: Direction = RIGHT
    fun wrapLeft(): Point =
        if (input.name == "sample.txt") {
            when (currentPosition.y) {
                in 1..4 -> {
                    val next = map.first { it.x == 4 + currentPosition.y && it.y == 5 }
                    if (next.walkable) currentDirection = DOWN
                    next
                }
                in 5..8 -> TODO()
                in 9..12 -> TODO()
                else -> TODO()
            }
        } else {
            when (currentPosition.y) {
                in 1..50 -> {
                    val next = map.first { it.x == 1 && it.y == 151 - currentPosition.y }
                    if (next.walkable) currentDirection = RIGHT
                    next
                }
                in 51..100 -> {
                    val next = map.first { it.x == currentPosition.y-50 && it.y == 101 }
                    if (next.walkable) currentDirection = DOWN
                    next
                }
                in 101..150 -> {
                    val next = map.first { it.x == 51 && it.y == 151-currentPosition.y }
                    if (next.walkable) currentDirection = RIGHT
                    next
                }
                in 151..200 -> {
                    val next = map.first { it.x == currentPosition.y-100 && it.y == 1 }
                    if (next.walkable) currentDirection = DOWN
                    next
                }
                else -> TODO()
            }
        }
    fun wrapRight(): Point =
        if (input.name == "sample.txt") {
            when (currentPosition.y) {
                in 1..4 -> {
                    TODO()
                }
                in 5..8 -> {
                    val next = map.first { it.x == (9 - currentPosition.y) + 12 && it.y == 9 }
                    if (next.walkable) currentDirection = DOWN
                    next
                }
                in 9..12 -> TODO()
                else -> TODO()
            }
        } else {
            when (currentPosition.y) {
                in 1..50 -> {
                    val next = map.first { it.x == 100 && it.y == 151-currentPosition.y }
                    if (next.walkable) currentDirection = LEFT
                    next
                }
                in 51..100 -> {
                    val next = map.first { it.x == 50 + currentPosition.y && it.y == 50 }
                    if (next.walkable) currentDirection = UP
                    next
                }
                in 101..150 -> {
                    val next = map.first { it.x == 150 && it.y == 151-currentPosition.y }
                    if (next.walkable) currentDirection = LEFT
                    next
                }
                in 151..200 -> {
                    val next = map.first { it.x == currentPosition.y-100 && it.y == 150 }
                    if (next.walkable) currentDirection = UP
                    next
                }
                else -> TODO()
            }
        }
    fun wrapUp(): Point =
        if (input.name == "sample.txt") {
            when (currentPosition.x) {
                in 1..4 -> {
                    TODO()
                }
                in 5..8 -> {
                    val next = map.first { it.x == 9 && it.y == currentPosition.x-4 }
                    if (next.walkable) currentDirection = RIGHT
                    next
                }
                in 9..12 -> TODO()
                in 13..16 -> TODO()
                else -> TODO()
            }
        } else {
            when (currentPosition.x) {
                in 1..50 -> {
                    val next = map.first { it.x == 51 && it.y == currentPosition.x+50 }
                    if (next.walkable) currentDirection = RIGHT
                    next
                }
                in 51..100 -> {
                    val next = map.first { it.x == 1 && it.y == 100 + currentPosition.x }
                    if (next.walkable) currentDirection = RIGHT
                    next
                }
                in 101..150 -> {
                    val next = map.first { it.x == currentPosition.x-100 && it.y == 200 }
                    if (next.walkable) currentDirection = UP
                    next
                }
                else -> TODO()
            }
        }
    fun wrapDown(): Point =
        if (input.name == "sample.txt") {
            when (currentPosition.x) {
                in 1..4 -> {
                    TODO()
                }
                in 5..8 -> TODO()
                in 9..12 -> {
                    val next = map.first { it.x == (13-currentPosition.x) && it.y == 8 }
                    if (next.walkable) currentDirection = UP
                    next
                }
                in 13..16 -> TODO()
                else -> TODO()
            }
        } else {
            when (currentPosition.x) {
                in 1..50 -> {
                    val next = map.first { it.x == currentPosition.x+100 && it.y == 1 }
                    if (next.walkable) currentDirection = DOWN
                    next
                }
                in 51..100 -> {
                    val next = map.first { it.x == 50 && it.y == 100 + currentPosition.x }
                    if (next.walkable) currentDirection = LEFT
                    next
                }
                in 101..150 -> {
                    val next = map.first { it.x == 100 && it.y == currentPosition.x-50 }
                    if (next.walkable) currentDirection = LEFT
                    next
                }
                else -> TODO()
            }
        }
    instructions.forEachIndexed { index, it ->
        when (it) {
            is Move -> {
                repeat(it.distance) {
                    currentPosition = when (currentDirection) {
                        DOWN -> {
                            val next = map.firstOrNull { it.x == currentPosition.x && it.y == currentPosition.y + 1 }
                                ?: wrapDown()
                            if (next.walkable) next
                            else currentPosition
                        }
                        LEFT -> {
                            val next = map.firstOrNull { it.x == currentPosition.x - 1 && it.y == currentPosition.y }
                                ?: wrapLeft()
                            if (next.walkable) next
                            else currentPosition
                        }
                        RIGHT -> {
                            val next = map.firstOrNull { it.x == currentPosition.x + 1 && it.y == currentPosition.y }
                                ?: wrapRight()
                            if (next.walkable) next
                            else currentPosition
                        }
                        UP -> {
                            val next = map.firstOrNull { it.x == currentPosition.x && it.y == currentPosition.y - 1 }
                                ?: wrapUp()
                            if (next.walkable) next
                            else currentPosition
                        }
                    }
                }
            }
            TURN_LEFT -> currentDirection = when (currentDirection) {
                DOWN -> RIGHT
                LEFT -> DOWN
                RIGHT -> UP
                UP -> LEFT
            }
            TURN_RIGHT -> currentDirection = when (currentDirection) {
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
                UP -> RIGHT
            }
        }
    }
    return ((currentPosition.y * 1000) + (currentPosition.x * 4) + when(currentDirection) {
        LEFT -> 2
        RIGHT -> 0
        UP -> 3
        DOWN -> 1
    }).toString()
}
