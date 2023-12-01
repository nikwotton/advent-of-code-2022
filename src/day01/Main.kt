package day01

import java.io.File

const val workingDir = "src/day01"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    val sample2 = File("$workingDir/sample_2.txt")
    runStep1(sample)
    runStep1(input1)
    runStep2(sample2)
    runStep2(input1)
}

fun runStep1(input: File) =
    input.readLines().map { it.filter { it.isDigit() } }.map { it.first().toString() + it.last() }.sumOf { it.toInt() }.let { println(it) }

fun runStep2(input: File) {
    input.readLines()
        .map {
            it.replace("one", "on1ne")
                .replace("two", "tw2wo")
                .replace("three", "thre3hree")
                .replace("four", "fou4our")
                .replace("five", "fiv5ive")
                .replace("six", "si6ix")
                .replace("seven", "seve7even")
                .replace("eight", "eigh8ight")
                .replace("nine", "nin9ine")
        }
        .map { it.filter { it.isDigit() } }
        .map { it.first().toString() + it.last() }
        .sumOf { it.toInt() }
        .let { println(it) }

}
