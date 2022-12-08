package day08

import java.io.File

const val workingDir = "src/day08"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 21
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}") // 8
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val rows = mutableListOf<MutableList<Int>>()
    input.readLines().forEach {
        rows.add(it.map { it.digitToInt() }.toMutableList())
    }
    return rows.mapIndexed outer@{ y, row ->
        row.mapIndexed inner@{ x, i ->
            if (y == 0 || y == rows.size - 1) return@inner true
            if (x == 0 || x == row.size - 1) return@inner true
            if (row.take(x).none { it >= i }) return@inner true
            if (row.takeLast(row.size - x - 1).none { it >= i }) return@inner true
            if (rows.take(y).none { it[x] >= i }) return@inner true
            if (rows.takeLast(rows.size - y - 1).none { it[x] >= i }) return@inner true
            false
        }
    }.sumOf { it.count { it } }.toString()
}

fun runStep2(input: File): String {
    val rows = mutableListOf<MutableList<Int>>()
    input.readLines().forEach {
        rows.add(it.map { it.digitToInt() }.toMutableList())
    }
    return rows.mapIndexed outer@{ y, row ->
        row.mapIndexed inner@{ x, i ->
            var up = 0
            var foundUp = false
            var down = 0
            var foundDown = false
            var left = 0
            var foundLeft = false
            var right = 0
            var foundRight = false

            (y-1 downTo 0).forEach {
                if (foundUp) return@forEach
                if (rows[it][x] >= i) foundUp = true
                up++
            }
            (y+1 until rows.size).forEach {
                if (foundDown) return@forEach
                if (rows[it][x] >= i) foundDown = true
                down++
            }
            (x-1 downTo 0).forEach {
                if (foundLeft) return@forEach
                if (row[it] >= i) foundLeft = true
                left++
            }
            (x+1 until rows.size).forEach {
                if (foundRight) return@forEach
                if (row[it] >= i) foundRight = true
                right++
            }

            up * down * left * right
        }
    }.maxOf { it.max() }.toString()
}
