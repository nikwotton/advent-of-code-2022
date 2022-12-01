package day01

import java.io.File

const val workingDir = "src/day01"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    runStep1(sample)
    runStep1(input1)
    runStep2(sample)
    runStep2(input1)
}

fun runStep1(input: File) {
    val elves = mutableListOf<Int>()
    var currentElf = 0
    input.readLines().forEach {
        if (it.isEmpty()) {
            elves.add(currentElf)
            currentElf = 0
        } else {
            currentElf += it.toInt()
        }
    }
    elves.add(currentElf)
    println("Max is: ${elves.max()}")
}

fun runStep2(input: File) {
    val elves = mutableListOf<Int>()
    var currentElf = 0
    input.readLines().forEach {
        if (it.isEmpty()) {
            elves.add(currentElf)
            currentElf = 0
        } else {
            currentElf += it.toInt()
        }
    }
    elves.add(currentElf)
    println("Max is: ${elves.sortedDescending().take(3).sum()}")
}
