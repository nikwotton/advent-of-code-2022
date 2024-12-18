package day18

import java.io.File
import java.util.*

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "22"
    val step2SampleExpected = "6,1"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

data class Position(val x: Int, val y: Int, val blocked: Boolean)

fun isValidMove(x: Int, y: Int, maze: List<List<Position>>, visited: Array<BooleanArray>): Boolean {
    return x >= 0 && y >= 0 && y < maze.size && x < maze[0].size && !maze[y][x].blocked && !visited[y][x]
}

fun bfsMaze(map: List<List<Position>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    val cols = map.size
    val rows = map[0].size

    val directions = listOf(
        1 to 0,   // Right
        -1 to 0,  // Left
        0 to 1,   // Down
        0 to -1   // Up
    )

    val visited = Array(rows) { BooleanArray(cols) }
    val queue: Queue<Pair<Pair<Int, Int>, Int>> = LinkedList()

    queue.add(start to 0) // Start point with distance 0
    visited[start.second][start.first] = true

    while (queue.isNotEmpty()) {
        val (current, distance) = queue.poll()

        if (current == end) return distance // Reached the destination

        for (dir in directions) {
            val newX = current.first + dir.first
            val newY = current.second + dir.second

            if (isValidMove(newX, newY, map, visited)) {
                visited[newY][newX] = true
                queue.add((newX to newY) to distance + 1)
            }
        }
    }
    return -1 // Return -1 if no path is found
}

fun runStep1(input: File): String {
    val inp = input.readLines()
    val (maxWidth,maxHeight) = inp[0].split(",").map { it.toInt() }
    val numBytes = inp[1].toInt()
    val startingAt = 0 to 0
    val goal = maxWidth to maxHeight
    val bytes = inp.drop(2).map { it.split(",").map { it.toInt() } }.take(numBytes)
    val map = (0..maxHeight).map { y -> (0..maxWidth).map { x -> Position(x, y, bytes.contains(listOf(x, y))) } }
    return bfsMaze(map, startingAt, goal).toString()
}

fun runStep2(input: File): String {
    val inp = input.readLines()
    val (maxWidth,maxHeight) = inp[0].split(",").map { it.toInt() }
    val numBytes = inp[1].toInt()
    val startingAt = 0 to 0
    val goal = maxWidth to maxHeight
    (0..4000).forEach { i ->
        val bytes = inp.drop(2).map { it.split(",").map { it.toInt() } }.take(numBytes + i)
        val map = (0..maxHeight).map { y -> (0..maxWidth).map { x -> Position(x, y, bytes.contains(listOf(x, y))) } }
        if (bfsMaze(map, startingAt, goal) == -1) return inp.drop(1).drop(numBytes + i).take(1).joinToString("")
    }
    TODO()
}
