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
    val list1 = input.readLines().map { it.split("   ")[0].toInt() }.sorted()
    val list2 = input.readLines().map { it.split("   ")[1].toInt() }.sorted()
    println(list1.indices.map { Math.abs(list1[it] - list2[it]) }.sum())
}

fun runStep2(input: File) {
    val list1 = input.readLines().map { it.split("   ")[0].toInt() }
    val list2 = input.readLines().map { it.split("   ")[1].toInt() }.let { l ->
        l.toSet().map { it1 -> it1 to l.count{ it == it1 } }.toMap()
    }
    println(list1.map { it * (list2[it] ?: 0) }.sum())
}
