package day10

import java.io.File

const val workingDir = "src/day10"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 13140
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: \n${runStep2(sample)}")
    println("Step 2b: \n${runStep2(input1)}") // RGZEHURK
}

fun runStep1(input: File): String {
    var cycle = 1
    var x = 1
    var sum = 0
    fun checkHandle() {
        if (cycle == 20 || (cycle - 20) % 40 == 0) {
            sum += cycle * x
        }
    }
    input.readLines().forEach {
        when {
            it == "noop" -> {
                cycle++
                checkHandle()
            }

            it.startsWith("addx") -> {
                cycle++
                checkHandle()
                cycle++
                x += it.split(" ")[1].toInt()
                checkHandle()
            }

            else -> TODO()
        }
    }
    return sum.toString()
}

fun runStep2(input: File): String {
    val screen = mutableListOf<MutableList<Char>>(mutableListOf())
    var cycle = 1
    var x = 2
    fun checkHandle() {
        var currentRow = screen.last()
        if (currentRow.size == 40) {
            screen.add(mutableListOf())
            currentRow = screen.last()
        }
        if (cycle % 40 in listOf(x%40, (x-1)%40, (x+1)%40))
            currentRow.add('#')
        else
            currentRow.add('.')
    }
    input.readLines().forEach {
        when {
            it == "noop" -> {
                checkHandle()
                cycle++
            }

            it.startsWith("addx") -> {
                checkHandle()
                cycle++
                checkHandle()
                x += it.split(" ")[1].toInt()
                cycle++
            }

            else -> TODO()
        }
    }
    return screen.joinToString("\n") { it.joinToString("") }
}
