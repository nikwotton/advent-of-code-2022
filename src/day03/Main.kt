package day03

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    assert(runStep1(sample) == "4361")
    println("Step 1 answer: ${runStep1(input1)}")
    assert(runStep2(sample) == "467835")
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val inputLines = input.readLines()
    return inputLines.mapIndexed { index, s ->
        val regex = "\\d+".toRegex()
        val matches = regex.findAll(s)
        val numbers = matches.map { Pair(it.range, it.value.toInt()) }.toList()
        numbers.sumOf { num -> if (checkNumber(num.first, num.second, s, inputLines.getOrNull(index - 1), inputLines.getOrNull(index + 1))) num.second else 0 }
    }.sumOf { it }.toString()
}

fun checkNumber(indexRange: IntRange, num: Int, s: String, above: String?, below: String?): Boolean {
    val startingIndex = (indexRange.first - 1).let { if (it < 0) 0 else it }
    val endIndex = (indexRange.last + 1).let { if (it > s.length - 1) s.length - 1 else it }
    if (s[startingIndex] != '.' && s[startingIndex] != num.toString()[0]) return true
    if (s[endIndex] != '.' && s[endIndex] != num.toString().last()) return true
    if (above != null && above.substring(startingIndex, endIndex + 1).any { !it.isDigit() && it != '.' }) return true
    if (below != null && below.substring(startingIndex, endIndex + 1).any { !it.isDigit() && it != '.' }) return true
    return false
}


fun runStep2(input: File): String {
    val inputLines = input.readLines()
    val partNumbers = inputLines.mapIndexed { index, s ->
        val regex = "\\d+".toRegex()
        index to regex.findAll(s).map { it.range }
    }.toMap()

    return inputLines.mapIndexed { index, s ->
        val regex = "\\*".toRegex()
        val matches = regex.findAll(s)
        matches.sumOf { result ->
            val adjacent = ArrayList<Int>()
            if (result.range.first - 1 in partNumbers[index]!!.map { it.last }) adjacent.add(partNumbers[index]!!.first { it.last == result.range.first - 1 }.let { s.substring(it.first, it.last + 1).toInt() })
            if (result.range.last + 1 in partNumbers[index]!!.map { it.first }) adjacent.add(partNumbers[index]!!.first { it.first == result.range.last + 1 }.let { s.substring(it.first, it.last + 1).toInt() })
            adjacent.addAll((partNumbers[index - 1] ?: emptySequence()).filter { it.contains(result.range.first - 1) || it.contains(result.range.first) || it.contains(result.range.first + 1) }.map { inputLines[index-1].substring(it.first, it.last + 1).toInt() })
            adjacent.addAll((partNumbers[index + 1] ?: emptySequence()).filter { it.contains(result.range.first - 1) || it.contains(result.range.first) || it.contains(result.range.first + 1) }.map { inputLines[index+1].substring(it.first, it.last + 1).toInt() })
            if (adjacent.size == 2) {
                adjacent.first() * adjacent[1]
            } else 0
        }
    }.sum().toString()
}
