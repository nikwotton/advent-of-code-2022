package day03

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val sample2 = File("$workingDir/sample2.txt")
    val step1SampleExpected = "161"
    val step2SampleExpected = "48"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample2)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    var counter = 0
    var index = 0
    val inp = input.readText()
    var m1 = ""
    var m2 = ""
    var ontoM2 = false
    fun reset() {
        index = 0
        m1 = ""
        m2 = ""
        ontoM2 = false
    }
    inp.indices.forEach {
        val char = inp[it]
        when(index) {
            0 -> if (char == 'm') index++
            1 -> if (char == 'u') index++ else reset()
            2 -> if (char == 'l') index++ else reset()
            3 -> if (char == '(') index++ else reset()
            else -> if (char.isDigit()) {
                if (ontoM2) {
                    if (m2.length < 3) m2 += char else reset()
                } else {
                    if (m1.length < 3) m1 += char else reset()
                }
            } else {
                if (ontoM2) {
                    if (char == ')') {
                        counter += (m1.toInt() * m2.toInt())
                        reset()
                    } else reset()
                } else {
                    if (char == ',') ontoM2 = true else reset()
                }
            }
        }
    }
    return counter.toString()
}

fun runStep2(input: File): String {
    var counter = 0
    var index = 0
    val inp = input.readText()
    var m1 = ""
    var m2 = ""
    var ontoM2 = false
    var doMul = true
    var doStr = ""
    fun reset() {
        index = 0
        m1 = ""
        m2 = ""
        ontoM2 = false
        doStr = ""
    }
    inp.indices.forEach {
        val char = inp[it]
        when(index) {
            0 -> if (char == 'm') index++ else if (char == 'd') {
                doStr = "d"
                index++
            }
            1 -> if (char == 'u') index++ else if (char == 'o' && doStr == "d") {
                doStr = "do"
                index++
            } else reset()
            2 -> if (char == 'l') index++ else if (char == '(' && doStr == "do") {
                doStr = "do("
                index++
            } else if (char == 'n' && doStr == "do") {
                doStr = "don"
                index++
            } else reset()
            3 -> if (char == '(') index++ else if (char == ')' && doStr == "do(") {
                doMul = true
                reset()
            } else if (char == '\'' && doStr == "don") {
                doStr = "don'"
                index++
            } else reset()
            else -> if (char.isDigit()) {
                if (ontoM2) {
                    if (m2.length < 3) m2 += char else reset()
                } else {
                    if (m1.length < 3) m1 += char else reset()
                }
            } else {
                if (doStr == "don'" && char == 't') {
                    doStr = "don't"
                    index++
                } else if (doStr == "don't" && char == '(') {
                    doStr = "don't("
                    index++
                } else if (doStr == "don't(" && char == ')') {
                    doMul = false
                    reset()
                }
                else if (ontoM2) {
                    if (char == ')' && doMul) {
                        counter += (m1.toInt() * m2.toInt())
                        reset()
                    } else reset()
                } else {
                    if (char == ',') ontoM2 = true else reset()
                }
            }
        }
    }
    return counter.toString()
}
