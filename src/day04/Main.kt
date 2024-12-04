package day04

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "18"
    val step2SampleExpected = "9"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    fun List<String>.christmas(): Int = sumOf {
        it.windowed(4).filter { it == "XMAS" || it == "SAMX" }.count()
    }
    val horizontalCount = input.readLines().christmas()
    val verticalCount = input.readLines().map { it.toList() }.transpose().map { it.joinToString("") }.christmas()
    val diagonalsCount1 = input.readLines().map { it.toList() }.diagonalFlip().map { it.joinToString("") }.christmas()
    val diagonalsCount2 = input.readLines().map { it.toList() }.rotate90Counterclockwise().diagonalFlip().map { it.joinToString("") }.christmas()

    return (horizontalCount + verticalCount + diagonalsCount1 + diagonalsCount2).toString()
}

inline fun <reified T> List<List<T>>.transpose() = Array(get(0).size) { col ->
    Array(size) { row ->
        get(row)[col]
    }.toList()
}.toList()

fun <T> List<List<T>>.diagonalFlip(): List<List<T>> {
    val rows = size
    val cols = get(0).size
    val diagonals = Array(rows + cols - 1) { mutableListOf<T>() }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            diagonals[i + j].add(get(i)[j])
        }
    }

    return diagonals.toList()
}

inline fun <reified T> List<List<T>>.rotate90Counterclockwise(): List<List<T>> {
    val rows = size
    val cols = get(0).size
    val rotated = Array(cols) { Array(rows) { get(0)[0] } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            rotated[cols - j - 1][i] = get(i)[j]
        }
    }

    return rotated.map { it.toList() }
}

fun runStep2(input: File): String {
    val inp = input.readLines().map { it.toList() }
    return inp.indices.drop(1).dropLast(1).sumOf { y ->
        inp[y].indices.drop(1).dropLast(1).map { x ->
            val center = inp[y][x]
            val a = inp[y-1][x-1] // top left
            val b = inp[y+1][x-1] // top right
            val c = inp[y-1][x+1] // bottom left
            val d = inp[y+1][x+1] // bottom right
            fun good(e: Char, f: Char): Boolean {
                if (e == 'S' && f == 'M') return true
                if (e == 'M' && f == 'S') return true
                return false
            }
            if (center == 'A' && (good(a, d) && good(b, c))) 1 else 0
        }.sum()
    }.toString()
}
