package day14

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "12"
//    val step2SampleExpected = "TODO(step2)"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample, 11, 7)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1, 101, 103)}")
//    val step2Sample = runStep2(sample, 11, 7)
//    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1, 101, 103)}")
}

data class Position(val x: Int, val y: Int, val vX: Int, val vY: Int)

fun runStep1(input: File, maxWidth: Int, maxHeight: Int): String {
    val robots = input.readLines().map {
        val (p, v) = it.removePrefix("p=").split(" v=")
        val (x, y) = p.split(",").map { it.toInt() }
        val (vX, vY) = v.split(",").map { it.toInt() }
        Position(x, y, vX, vY)
    }
    val maxSeconds = 100
    val endRobots = robots.map {
        // travel
        it.copy(x = (it.x + (it.vX * maxSeconds)) % maxWidth, y = (it.y + (it.vY * maxSeconds)) % maxHeight)
    }.map {
        // normalize
        it.copy(x = if (it.x >= 0) it.x else maxWidth + it.x, y = if (it.y >= 0) it.y else maxHeight + it.y)
    }
    val quadrants = endRobots.fold(listOf(0, 0, 0, 0), { acc, next ->
        if (next.x < maxWidth / 2) {
            if (next.y < maxHeight / 2) {
                listOf(acc[0] + 1, acc[1], acc[2], acc[3])
            } else if (next.y > maxHeight / 2) {
                listOf(acc[0], acc[1], acc[2] + 1, acc[3])
            } else acc
        } else if (next.x > maxWidth / 2) {
            if (next.y < maxHeight / 2) {
                listOf(acc[0], acc[1] + 1, acc[2], acc[3])
            } else if (next.y > maxHeight / 2) {
                listOf(acc[0], acc[1], acc[2], acc[3] + 1)
            } else acc
        } else acc
    })
    return quadrants.fold(1, { acc, next -> acc * next }).toString()
}

fun runStep2(input: File, maxWidth: Int, maxHeight: Int): String {
    val robots = input.readLines().map {
        val (p, v) = it.removePrefix("p=").split(" v=")
        val (x, y) = p.split(",").map { it.toInt() }
        val (vX, vY) = v.split(",").map { it.toInt() }
        Position(x, y, vX, vY)
    }
    (0..100_000).forEach { maxSeconds ->
        val endRobots = robots.map {
            // travel
            it.copy(x = (it.x + (it.vX * maxSeconds)) % maxWidth, y = (it.y + (it.vY * maxSeconds)) % maxHeight)
        }.map {
            // normalize
            it.copy(x = if (it.x >= 0) it.x else maxWidth + it.x, y = if (it.y >= 0) it.y else maxHeight + it.y)
        }
        val map = (0..maxHeight).map { y ->
            (0..maxWidth).map { x ->
                val count = endRobots.count { it.x == x && it.y == y }
                if (count == 0) ' ' else '.'
            }
        }
        if (map.count { it.joinToString("").contains("...............................") } >= 2) {
            map.forEach { println(it.joinToString("")) }
            return maxSeconds.toString()
        }
    }
    return "-1"
}
// Not 1363