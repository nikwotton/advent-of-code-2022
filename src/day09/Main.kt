package day09

import java.io.File

const val workingDir = "src/day09"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val sample2 = File("$workingDir/sample2.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 13
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}") // 1
    println("Step 2a: ${runStep2(sample2)}") // 36
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val rows = mutableListOf(mutableListOf(true))
    var headX = 0
    var headY = 0
    var tailX = 0
    var tailY = 0
    input.readLines().forEach {
        val (direction, distance) = it.split(" ")
        repeat(distance.toInt()) {
            when (direction) {
                "R" -> {
                    // handle head
                    val row = rows[headY]
                    if (headX + 1 == row.size)
                        rows.forEach { it.add(false) }
                    headX++

                    // handle tail
                    if ((headX - tailX) == 2) {
                        tailY = headY
                        tailX++
                        row[tailX] = true
                    }
                }

                "U" -> {
                    // handle head
                    if (headY == 0) {
                        rows.add(0, rows[0].map { false }.toMutableList())
                        tailY++
                    } else {
                        headY--
                    }

                    // handle tail
                    if (tailY - headY == 2) {
                        tailX = headX
                        tailY--
                        rows[tailY][tailX] = true
                    }
                }
                "D" -> {
                    // handle head
                    if (headY + 1 == rows.size) {
                        rows.add(rows[0].map { false }.toMutableList())
                        headY++
                    } else {
                        headY++
                    }

                    // handle tail
                    if (headY - tailY == 2) {
                        tailX = headX
                        tailY++
                        rows[tailY][tailX] = true
                    }
                }
                "L" -> {
                    // handle head
                    val row = rows[headY]
                    if (headX == 0) {
                        rows.forEach { it.add(0, false) }
                        tailX++
                    } else {
                        headX--
                    }

                    // handle tail
                    if (tailX - headX == 2) {
                        tailY = headY
                        tailX--
                        row[tailX] = true
                    }
                }

                else -> TODO()
            }
        }
    }
    return rows.sumOf { it.count { it } }.toString()
}

data class Position(val x: Int, val y: Int)

class Node(private val followedBy: Node?, val setTrue: (Int, Int)->Unit) {
    fun moveRight() {
        x++
        handleTail()
    }
    private fun moveUpRight() {
        x++
        y--
        handleTail()
    }
    private fun handleTail() {
        if (followedBy != null) {
            val dx = x - followedBy.x
            val dy = y - followedBy.y
            val delta = Pair(dx, dy)
            if (dx !in listOf(-1, 0, 1) || dy !in listOf(-1, 0, 1))
                when (delta) {
                    Pair(2, 0) -> followedBy.moveRight()
                    Pair(2, -1) -> followedBy.moveUpRight()
                    Pair(2, -2) -> followedBy.moveUpRight()
                    Pair(2, 1) -> followedBy.moveDownRight()
                    Pair(2, 2) -> followedBy.moveDownRight()
                    Pair(-2, -1) -> followedBy.moveUpLeft()
                    Pair(-2, 0) -> followedBy.moveLeft()
                    Pair(-2, -2) -> followedBy.moveUpLeft()
                    Pair(-2, 1) -> followedBy.moveDownLeft()
                    Pair(-2, 2) -> followedBy.moveDownLeft()
                    Pair(1, -2) -> followedBy.moveUpRight()
                    Pair(0, -2) -> followedBy.moveUp()
                    Pair(-1, -2) -> followedBy.moveUpLeft()
                    Pair(-1, 2) -> followedBy.moveDownLeft()
                    Pair(0, 2) -> followedBy.moveDown()
                    Pair(1, 2) -> followedBy.moveDownRight()
                    else -> TODO("Need to handle $delta")
                }
        } else setTrue(x, y)
    }
    private fun moveUpLeft() {
        x--
        y--
        handleTail()
    }
    private fun moveDownRight() {
        y++
        x++
        handleTail()
    }
    private fun moveDownLeft() {
        y++
        x--
        handleTail()
    }
    fun moveLeft() {
        x--
        handleTail()
    }
    fun moveUp() {
        y--
        handleTail()
    }
    fun moveDown() {
        y++
        handleTail()
    }
    private var x: Int = 0
    private var y: Int = 0
}

fun runStep2(input: File): String {
    val seenPositions = mutableSetOf(Position(0, 0))
    val setTrue = {x: Int, y: Int -> Unit.also {seenPositions.add(Position(x, y))}}
    val head = Node(Node(Node(Node(Node(Node(Node(Node(Node(Node(null, setTrue), setTrue), setTrue), setTrue), setTrue), setTrue), setTrue), setTrue), setTrue), setTrue)
    input.readLines().forEach {
        val (direction, distance) = it.split(" ")
        repeat(distance.toInt()) {
            when (direction) {
                "R" -> head.moveRight()
                "U" -> head.moveUp()
                "D" -> head.moveDown()
                "L" -> head.moveLeft()
                else -> TODO()
            }
        }
    }
    return seenPositions.count().toString()
}
