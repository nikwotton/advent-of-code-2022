package day02

import java.io.File

const val workingDir = "src/day02"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String =
    input.readLines().filterNot { it.isBlank() }.map { it.split(" ") }.sumOf { (opponent, me) ->
        val chosenScore = when (me) {
            "X" -> 1 // Rock
            "Y" -> 2 // Paper
            "Z" -> 3 // Scissor
            else -> TODO()
        }
        val won = when (me) {
            "X" -> when (opponent) {
                "A" -> 3
                "B" -> 0
                "C" -> 6
                else -> TODO()
            }

            "Y" -> when (opponent) {
                "A" -> 6
                "B" -> 3
                "C" -> 0
                else -> TODO()
            }

            "Z" -> when (opponent) {
                "A" -> 0
                "B" -> 6
                "C" -> 3
                else -> TODO()
            }

            else -> TODO()
        }
        chosenScore + won
    }.toString()

fun runStep2(input: File): String =
    input.readLines().filterNot { it.isBlank() }.map { it.split(" ") }.sumOf { (opponent, me) ->
        val won = when (me) {
            "X" -> when (opponent) {
                "A" -> 3
                "B" -> 1
                "C" -> 2
                else -> TODO()
            }

            "Y" -> when (opponent) {
                "A" -> 4
                "B" -> 5
                "C" -> 6
                else -> TODO()
            }

            "Z" -> when (opponent) {
                "A" -> 8
                "B" -> 9
                "C" -> 7
                else -> TODO()
            }

            else -> TODO()
        }
        won
    }.toString()
