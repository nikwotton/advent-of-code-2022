package day16

import java.io.File
import java.util.*

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "11048"
    val step2SampleExpected = "64"
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
    val blocked: Boolean
)

fun isValidMove(
    x: Int,
    y: Int,
    direction: Int,
    maze: List<List<Position>>,
    visited: Array<Array<Array<Int>>>,
    distance: Int
): Boolean {
    return x >= 0 && y >= 0 && y < maze.size && x < maze[0].size && !maze[y][x].blocked && direction in listOf(
        0,
        1,
        2,
        3
    ) && distance < visited[y][x][direction]
}

fun bfsMaze(map: List<List<Position>>, start: Triple<Int, Int, Int>, end: Pair<Int, Int>): Int {
    val cols = map.size
    val rows = map[0].size
    require(cols == rows)

    val visited = Array(rows) { Array(cols) { Array(4) { Int.MAX_VALUE } } }
    val queue: Queue<Pair<Triple<Int, Int, Int>, Int>> = LinkedList()

    queue.add(start to 0) // Start point with distance 0
    visited[start.second][start.first][start.third] = 0
    var lowest = Int.MAX_VALUE

    while (queue.isNotEmpty()) {
        val (current, distance) = queue.poll()

        if ((current.first to current.second) == end) {
            if (distance < lowest) lowest = distance
            continue
        } else if (distance > lowest) {
            continue
        }

        listOf(
            listOf(current.first, current.second, (current.third - 1 + 4) % 4, distance + 1000),
            listOf(current.first, current.second, (current.third + 1) % 4, distance + 1000),
            when (current.third) {
                0 -> listOf(current.first, current.second - 1, current.third, distance + 1)
                1 -> listOf(current.first + 1, current.second, current.third, distance + 1)
                2 -> listOf(current.first, current.second + 1, current.third, distance + 1)
                3 -> listOf(current.first - 1, current.second, current.third, distance + 1)
                else -> TODO()
            }
        ).forEach { (x, y, dir, dist) ->
            if (isValidMove(x, y, dir, map, visited, distance)) {
                visited[y][x][dir] = distance
                queue.add(Triple(x, y, dir) to dist)
            }
        }
    }
    return lowest
}

fun runStep1(input: File): String {
    var starting: Triple<Int, Int, Int> = Triple(-1, -1, -1)
    var goal: Pair<Int, Int> = -1 to -1
    val map = input.readLines().mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            when (c) {
                '#' -> Position(x, y, true)
                '.' -> Position(x, y, false)
                'S' -> {
                    starting = Triple(x, y, 1)
                    Position(x, y, false)
                }

                'E' -> {
                    goal = x to y
                    Position(x, y, false)
                }

                else -> TODO()
            }
        }
    }
    // 1 point for moving, 1000 points for turning 90 degrees
    return bfsMaze(map, starting, goal).toString()
}

fun isValidMovePart2(
    x: Int,
    y: Int,
    direction: Int,
    maze: List<List<Position>>,
    visited: Array<Array<Array<Int>>>,
    distance: Int
): Boolean {
    return x >= 0 && y >= 0 && y < maze.size && x < maze[0].size && !maze[y][x].blocked && direction in listOf(
        0,
        1,
        2,
        3
    ) && distance <= visited[y][x][direction]
}

fun bfsMazeStep2(map: List<List<Position>>, start: Triple<Int, Int, Int>, end: Pair<Int, Int>): Int {
    val cols = map.size
    val rows = map[0].size
    require(cols == rows)

    val visited = Array(rows) { Array(cols) { Array(4) { Int.MAX_VALUE } } }
    val queue: Queue<Pair<Triple<Int, Int, Int>, Pair<Int, Set<Pair<Int, Int>>>>> = LinkedList()

    queue.add(start to (0 to setOf((start.first to start.second), end))) // Start point with distance 0
    visited[start.second][start.first][start.third] = 0
    var lowest = Int.MAX_VALUE
    val seens = HashMap<Int, Set<Pair<Int, Int>>>()

    while (queue.isNotEmpty()) {
        val (current, distanceAndPoints) = queue.poll()
        val (distance, pointsSeen) = distanceAndPoints

        if ((current.first to current.second) == end) {
            if (distance < lowest) lowest = distance
            seens[distance] = seens.getOrDefault(distance, emptySet()) + pointsSeen
            continue
        } else if (distance > lowest) {
            continue
        }

        listOf(
            listOf(current.first, current.second, (current.third - 1 + 4) % 4, distance + 1000),
            listOf(current.first, current.second, (current.third + 1) % 4, distance + 1000),
            when (current.third) {
                0 -> listOf(current.first, current.second - 1, current.third, distance + 1)
                1 -> listOf(current.first + 1, current.second, current.third, distance + 1)
                2 -> listOf(current.first, current.second + 1, current.third, distance + 1)
                3 -> listOf(current.first - 1, current.second, current.third, distance + 1)
                else -> TODO()
            }
        ).forEach { (x, y, dir, dist) ->
            if (isValidMovePart2(x, y, dir, map, visited, distance)) {
                visited[y][x][dir] = distance
                queue.add(Triple(x, y, dir) to (dist to pointsSeen + (x to y)))
            }
        }
    }
    return seens[lowest]!!.size
}

fun runStep2(input: File): String {
    var starting: Triple<Int, Int, Int> = Triple(-1, -1, -1)
    var goal: Pair<Int, Int> = -1 to -1
    val map = input.readLines().mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            when (c) {
                '#' -> Position(x, y, true)
                '.' -> Position(x, y, false)
                'S' -> {
                    starting = Triple(x, y, 1)
                    Position(x, y, false)
                }

                'E' -> {
                    goal = x to y
                    Position(x, y, false)
                }

                else -> TODO()
            }
        }
    }
    // 1 point for moving, 1000 points for turning 90 degrees
    return bfsMazeStep2(map, starting, goal).toString()
}
