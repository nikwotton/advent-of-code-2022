package day13

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == "405") { "Failed sample in step 1, got $step1Sample" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == "400") { "Failed sample in step 2, got $step2Sample" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String = input.readText().trim().split("\n\n").map { str ->
    (str.columnCount() + (100 * str.rowCount()))
}.sum().toString()

fun String.columnCount(): Int {
    val rows = split("\n")
    return (0 until rows.first().length).sumOf { index ->
        if (rows.all {
                val left = it.take(index)
                val right = it.removePrefix(left).reversed()
                if (left.length > right.length) {
                    left.takeLast(right.length) == right
                } else if (left.length < right.length) {
                    left == right.takeLast(left.length)
                } else {
                    left == right
                }
            }) {
            index
        } else {
            0
        }
    }
}

fun String.rowCount(): Int {
    val rows = split("\n")
    return rows.indices.sumOf { index ->
        if (rows.first().indices.all { columnIndex ->
                val above = rows.map { it[columnIndex] }.joinToString("").take(index)
                val below = rows.map { it[columnIndex] }.joinToString("").removePrefix(above).reversed()
                if (above.isBlank() || below.isBlank()) {
                    false
                } else if (above.length > below.length) {
                    above.takeLast(below.length) == below
                } else if (above.length < below.length) {
                    above == below.takeLast(above.length)
                } else {
                    above == below
                }
            }) {
            index
        } else {
            0
        }
    }
}

fun runStep2(input: File): String = input.readText().trim().split("\n\n").map { str ->
    ((str.columnCountWithSmudge() + (100 * str.rowCountWithSmudge())))
}.sum().toString()

fun String.columnCountWithSmudge(): Int {
    val rows = split("\n")
    return (0 until rows.first().length).sumOf { index ->
        var usedSmudge = false
        if (rows.all {
                val left = it.take(index)
                val right = it.removePrefix(left).reversed()
                if (left.length > right.length) {
                    val str1 = left.takeLast(right.length)
                    val str2 = right
                    if (str1 == str2) {
                        true
                    } else if (!usedSmudge && str1.indices.map { str1[it] != str2[it] }.count { it } == 1) {
                        usedSmudge = true
                        true
                    } else {
                        false
                    }
                } else if (left.length < right.length) {
                    val str1 = left
                    val str2 = right.takeLast(left.length)
                    if (str1 == str2) {
                        true
                    } else if (!usedSmudge && str1.indices.map { str1[it] != str2[it] }.count { it } == 1) {
                        usedSmudge = true
                        true
                    } else {
                        false
                    }
                } else {
                    val str1 = left
                    val str2 = right
                    if (str1 == str2) {
                        true
                    } else if (!usedSmudge && str1.indices.map { str1[it] != str2[it] }.count { it } == 1) {
                        usedSmudge = true
                        true
                    } else {
                        false
                    }
                }
            }) {
            if (usedSmudge)
                index
            else
                0
        } else {
            0
        }
    }
}

fun String.rowCountWithSmudge(): Int {
    val rows = split("\n")
    return rows.indices.sumOf { index ->
        var usedSmudge = false
        if (rows.first().indices.all { columnIndex ->
                val above = rows.map { it[columnIndex] }.joinToString("").take(index)
                val below = rows.map { it[columnIndex] }.joinToString("").removePrefix(above).reversed()
                if (above.isBlank() || below.isBlank()) {
                    false
                } else if (above.length > below.length) {
                    val str1 = above.takeLast(below.length)
                    val str2 = below
                    if (str1 == str2) {
                        true
                    } else if (!usedSmudge && str1.indices.map { str1[it] != str2[it] }.count { it } == 1) {
                        usedSmudge = true
                        true
                    } else {
                        false
                    }
                } else if (above.length < below.length) {
                    val str1 = above
                    val str2 = below.takeLast(above.length)
                    if (str1 == str2) {
                        true
                    } else if (!usedSmudge && str1.indices.map { str1[it] != str2[it] }.count { it } == 1) {
                        usedSmudge = true
                        true
                    } else {
                        false
                    }
                } else {
                    val str1 = above
                    val str2 = below
                    if (str1 == str2) {
                        true
                    } else if (!usedSmudge && str1.indices.map { str1[it] != str2[it] }.count { it } == 1) {
                        usedSmudge = true
                        true
                    } else {
                        false
                    }
                }
            }) {
            if (usedSmudge)
                index
            else
                0
        } else {
            0
        }
    }
}
