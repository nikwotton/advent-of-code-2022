package day12

import java.io.File

const val workingDir = "src/day12"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

data class Position(val x: Int, val y: Int)

operator fun List<List<Char>>.get(position: Position): Char = this[position.y][position.x]

fun runStep1(input: File): String {
    val rows = mutableListOf<MutableList<Char>>()
    input.readLines().forEach {
        rows.add(it.toCharArray().toMutableList())
    }
    val goalRow = rows.indexOfFirst { it.contains('E') }
    val goalPosition = Position(rows[goalRow].indexOfFirst { it == 'E' }, goalRow)
    val queue = ArrayDeque<Pair<Int, Position>>()
    val seenPositions = ArrayList<Position>()
    fun tryPosition(currentPosition: Position, newPosition: Position): Boolean {
        return try {
            val current = rows[currentPosition]
            val next = rows[newPosition]
            if (current == 'S' && next !in listOf('a', 'b')) return false
            if (current == 'S' && next in listOf('a', 'b')) return true
            if (next == 'E' && current !in listOf('z', 'y')) return false
            if (next == 'E' && current in listOf('z', 'y')) return true
            return (next - (current+1)) <= 0
        } catch (e: IndexOutOfBoundsException) {
            false
        }
    }
    tailrec fun tryStep(currentCounter: Int, currentPosition: Position): Int {
        if (currentPosition == goalPosition) return currentCounter
        seenPositions.add(currentPosition)
        val canGoLeft = tryPosition(currentPosition, currentPosition.copy(currentPosition.x-1, currentPosition.y))
        val canGoRight = tryPosition(currentPosition, currentPosition.copy(currentPosition.x+1, currentPosition.y))
        val canGoUp = tryPosition(currentPosition, currentPosition.copy(currentPosition.x, currentPosition.y-1))
        val canGoDown = tryPosition(currentPosition, currentPosition.copy(currentPosition.x, currentPosition.y+1))
        if(canGoLeft)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x-1, currentPosition.y)))
        if(canGoRight)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x+1, currentPosition.y)))
        if(canGoUp)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x, currentPosition.y-1)))
        if(canGoDown)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x, currentPosition.y+1)))
        queue.removeAll { it.second in seenPositions }
        val (a, b) = queue.removeFirst()
        return tryStep(a, b)
    }
    val currentRow = rows.indexOfFirst { it.contains('S') }
    val currentPosition = Position(rows[currentRow].indexOfFirst { it == 'S' }, currentRow)
    return tryStep(0, currentPosition).toString()
}

fun runStep2(input: File): String {
    val rows = mutableListOf<MutableList<Char>>()
    input.readLines().forEach {
        rows.add(it.toCharArray().toMutableList())
    }
    val goalRow = rows.indexOfFirst { it.contains('E') }
    val goalPosition = Position(rows[goalRow].indexOfFirst { it == 'E' }, goalRow)
    val queue = ArrayDeque<Pair<Int, Position>>()
    val seenPositions = ArrayList<Position>()
    fun tryPosition(currentPosition: Position, newPosition: Position): Boolean {
        return try {
            val current = rows[currentPosition]
            val next = rows[newPosition]
            if (current == 'S' && next !in listOf('a', 'b')) return false
            if (current == 'S' && next in listOf('a', 'b')) return true
            if (next == 'E' && current !in listOf('z', 'y')) return false
            if (next == 'E' && current in listOf('z', 'y')) return true
            return (next - (current+1)) <= 0
        } catch (e: IndexOutOfBoundsException) {
            false
        }
    }
    var currentBest = 457
    tailrec fun tryStep(currentCounter: Int, currentPosition: Position): Int {
        if (currentPosition == goalPosition) return currentCounter
        if (currentCounter >= currentBest) return Int.MAX_VALUE
        seenPositions.add(currentPosition)
        val canGoLeft = tryPosition(currentPosition, currentPosition.copy(currentPosition.x-1, currentPosition.y))
        val canGoRight = tryPosition(currentPosition, currentPosition.copy(currentPosition.x+1, currentPosition.y))
        val canGoUp = tryPosition(currentPosition, currentPosition.copy(currentPosition.x, currentPosition.y-1))
        val canGoDown = tryPosition(currentPosition, currentPosition.copy(currentPosition.x, currentPosition.y+1))
        if(canGoLeft)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x-1, currentPosition.y)))
        if(canGoRight)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x+1, currentPosition.y)))
        if(canGoUp)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x, currentPosition.y-1)))
        if(canGoDown)queue.add(Pair(currentCounter+1, currentPosition.copy(currentPosition.x, currentPosition.y+1)))
        queue.removeAll { it.second in seenPositions }
        if (queue.isEmpty()) return Int.MAX_VALUE
        val (a, b) = queue.removeFirst()
        return tryStep(a, b)
    }

    return rows
        .mapIndexed { index, chars -> Pair(index, chars) }
        .filter { it.second.contains('a') || it.second.contains('S') }
        .flatMap { it.second.mapIndexed { index, c -> Pair(c, Position(index, it.first)) } }
        .filter { it.first in listOf('a', 'S')  }
        .map { it.second }
        .map {
            seenPositions.clear()
            queue.clear()
            val step = tryStep(0, it)
            if (step < currentBest) {
                currentBest = step
                println("Found a new best: $currentBest")
            }
            step
        }
        .min()
        .toString()
}
