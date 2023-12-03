package day02

import java.io.File
import kotlin.math.max

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    assert(runStep1(sample) == "8")
    println("Step 1 answer: ${runStep1(input1)}")
    assert(runStep2(sample) == "2286")
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    return input.readLines()
        .filter { it.isNotBlank() }
        .map { it.removePrefix("Game ") }
        .map { Pair(it.split(":")[0], it.removePrefix("${it.split(":")[0]}: ")) }
        .sumOf { (id, games) ->
            val allPossible = games.split(";").all { game ->
                var red = 0
                var green = 0
                var blue = 0
                game.replace(" ", "").split(",").forEach {
                    when {
                        it.contains("blue") -> blue = it.replace("blue", "").toInt()
                        it.contains("red") -> red = it.replace("red", "").toInt()
                        it.contains("green") -> green = it.replace("green", "").toInt()
                    }
                }
                step1Helper(red = red, green = green, blue = blue)
            }
            if (allPossible) id.toInt() else 0
        }.toString()
}

fun step1Helper(red: Int, green: Int, blue: Int): Boolean {
    if (red > 12) return false
    if (green > 13) return false
    if (blue > 14) return false
    return true
}

fun runStep2(input: File): String {
    return input.readLines()
        .filter { it.isNotBlank() }
        .map { it.removePrefix("Game ") }
        .map { it.removePrefix("${it.split(":")[0]}: ") }
        .sumOf { games ->
            games.split(";").map { game ->
                var red = 0
                var green = 0
                var blue = 0
                game.replace(" ", "").split(",").forEach {
                    when {
                        it.contains("blue") -> blue = it.replace("blue", "").toInt()
                        it.contains("red") -> red = it.replace("red", "").toInt()
                        it.contains("green") -> green = it.replace("green", "").toInt()
                    }
                }
                Triple(red, green, blue)
            }.fold(Triple(0, 0, 0)) { acc, next ->
                Triple(max(acc.first, next.first), max(acc.second, next.second), max(acc.third, next.third))
            }.let {
                it.first * it.second * it.third
            }
        }.toString()
}
