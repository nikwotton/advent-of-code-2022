package day08

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val sample2 = File("$workingDir/sample2.txt")
    val sample3 = File("$workingDir/sample3.txt")
    val input1 = File("$workingDir/input_1.txt")
//    val step1Sample = runStep1(sample)
//    val step1Sample2 = runStep1(sample2)
//    require(step1Sample == "2") { "Failed sample in step 1, got $step1Sample" }
//    require(step1Sample2 == "6") { "Failed sample2 in step 1, got $step1Sample2" }
//    println("Step 1 answer: ${runStep1(input1)}")
//    val step2Sample = runStep2(sample)
//    val step2Sample2 = runStep2(sample2)
//    val step2Sample3 = runStep2(sample3)
//    require(step2Sample == "2") { "Failed sample in step 2, got $step2Sample" }
//    require(step2Sample2 == "6") { "Failed sample2 in step 2, got $step2Sample2" }
//    require(step2Sample3 == "6") { "Failed sample3 in step 2, got $step2Sample3" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val steps = input.readLines().first()
    val map = input
        .readLines()
        .drop(2)
        .map { it.split(" = ") }
        .map { it.first() to it[1].removeSurrounding("(", ")").split(", ").let { Pair(it[0], it[1]) } }
        .toMap()
    var index = 0
    var current = "AAA"
    while (current != "ZZZ") {
        current = map[current]!!.let { if (steps[index % steps.length] == 'L') it.first else if (steps[index % steps.length] == 'R') it.second else TODO() }
        index++
    }

    return index.toString()
}

fun runStep2(input: File): String {
    val steps = input.readLines().first()
    val map = input
        .readLines()
        .drop(2)
        .map { it.split(" = ") }
        .map { it.first() to it[1].removeSurrounding("(", ")").split(", ").let { Pair(it[0], it[1]) } }
        .toMap()
    var index: Long = 0
    var currents = map.keys.filter { it.endsWith("A") }
//    val startWith = 1
    fun makeAttempt() {
        currents = currents.map {
            map[it]!!.let { if (steps[(index % steps.length.toLong()).toInt()] == 'L') it.first else if (steps[(index % steps.length.toLong()).toInt()] == 'R') it.second else TODO() }
        }
        index++
    }
//    while (currents.any { !it.endsWith("Z") } && index < startWith) {
//        makeAttempt()
//    }
    val workingOn = 0
    while (!currents[workingOn].endsWith("Z")) {
        makeAttempt()
    }
    println(index)
    (0..1000).map {
        makeAttempt()
        while (!currents[workingOn].endsWith("Z")) {
            makeAttempt()
        }
        index
    }.let { println(it.windowed(2).map { it[1] - it[0] }.groupBy { it }.map { it.key to it.value.count() }.toMap()) }

//    while (currents.any { !it.endsWith("Z") }) {
//        makeAttempt(21883)
//    }
    // A1, A2.. - B1, B2..
    // 21_883 - 21_883 - 1_015_482 == 13019
    // 13_019 - 13_019
    // 19_667 - 19_667
    // 16_343 - 16_343
    // 18_559 - 18_559
    // 14_681 - 14_681
    // B1 + A1x == B2 + A2y
    // (B1 - B2) == A2y - A1x
    // 13019 = 13019y - 21883x
    // y = 79x/47 + 1
    // 13019 = (13019 * 79x)/47 + 1 - 21883x
    // lowest common denominator of all of them - 15299095336639



    return index.toString()
}
