package day06

import java.io.File

const val workingDir = "src/day06"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    var recentChars = listOf<Char>()
    input.readText().forEachIndexed { index, c ->
        if (c in recentChars) {
            recentChars = recentChars.dropWhile { it != c }
            recentChars = recentChars.drop(1)
            recentChars = recentChars + c
        } else {
            recentChars = recentChars + c
            if (recentChars.size == 4) return (index+1).toString()
        }
    }
    TODO()
}

fun runStep2(input: File): String {
    var recentChars = listOf<Char>()
    input.readText().forEachIndexed { index, c ->
        if (c in recentChars) {
            recentChars = recentChars.dropWhile { it != c }
            recentChars = recentChars.drop(1)
            recentChars = recentChars + c
        } else {
            recentChars = recentChars + c
            if (recentChars.size == 14) return (index+1).toString()
        }
    }
    TODO()
}
