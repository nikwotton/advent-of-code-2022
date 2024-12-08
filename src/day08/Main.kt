package day08

import java.io.File
import kotlin.math.abs

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "14"
    val step2SampleExpected = "34"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

data class Position(val x: Int, val y: Int, val c: Char)

fun runStep1(input: File): String {
    val map = input.readLines().mapIndexed { y, s -> s.mapIndexed { x, c -> Position(x, y, c) } }
    val goals = mutableSetOf<Position>()
    val frequencyPoints = map.flatten().filter { it.c != '.' }
    frequencyPoints.forEach { p ->
        frequencyPoints.filter { it.c == p.c }.forEach { p2 ->
            val dx = abs(p.x - p2.x)
            val dy = abs(p.y - p2.y)
            if (p.x > p2.x && p.y > p2.y) {
                goals.add(Position(p.x + dx, p.y + dy, p.c))
                goals.add(Position(p2.x - dx, p2.y - dy, p.c))
            } else if (p.x > p2.x) {
                goals.add(Position(p.x + dx, p.y - dy, p.c))
                goals.add(Position(p2.x - dx, p2.y + dy, p.c))
            } else if (p.y > p2.y) {
                goals.add(Position(p.x - dx, p.y + dy, p.c))
                goals.add(Position(p2.x+  dx, p2.y - dy, p.c))
            } else {
                goals.add(Position(p.x - dx, p.y - dy, p.c))
                goals.add(Position(p2.x + dx, p2.y + dy, p.c))
            }
        }
    }
    return goals.filter { it.x >= 0 && it.x < map[0].size && it.y >= 0 && it.y < map.size }
        .filter { it !in frequencyPoints }
        .map { it.copy(c = '.') }
        .toSet()
        .count()
        .toString()
}

fun runStep2(input: File): String {
    val map = input.readLines().mapIndexed { y, s -> s.mapIndexed { x, c -> Position(x, y, c) } }
    val goals = mutableSetOf<Position>()
    val frequencyPoints = map.flatten().filter { it.c != '.' }
    frequencyPoints.forEach { p ->
        frequencyPoints.filter { it.c == p.c }.forEach { p2 ->
            val dx = abs(p.x - p2.x)
            val dy = abs(p.y - p2.y)
            (0..50).forEach { mult ->
                if (p.x > p2.x && p.y > p2.y) {
                    goals.add(Position(p.x + (dx * mult), p.y + (dy * mult), p.c))
                    goals.add(Position(p2.x - (dx * mult), p2.y - (dy * mult), p.c))
                } else if (p.x > p2.x) {
                    goals.add(Position(p.x + (dx * mult), p.y - (dy * mult), p.c))
                    goals.add(Position(p2.x - (dx * mult), p2.y + (dy * mult), p.c))
                } else if (p.y > p2.y) {
                    goals.add(Position(p.x - (dx * mult), p.y + (dy * mult), p.c))
                    goals.add(Position(p2.x + (dx * mult), p2.y - (dy * mult), p.c))
                } else {
                    goals.add(Position(p.x - (dx * mult), p.y - (dy * mult), p.c))
                    goals.add(Position(p2.x + (dx * mult), p2.y + (dy * mult), p.c))
                }
            }
        }
    }
    return goals.filter { it.x >= 0 && it.x < map[0].size && it.y >= 0 && it.y < map.size }
        .map { it.copy(c = '.') }
        .toSet()
        .count()
        .toString()
}
