package day07

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == "6440") { "Failed sample in step 1, got $step1Sample" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == "5905") { "Failed sample in step 2, got $step2Sample" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String =
    input.readLines().map { it.split(" ") }.sortedBy { (hand, bid) ->
        val type = when {
            hand.countCharacters().size == 1 -> 7 // 5 of a kind
            hand.countCharacters().values.contains(4) -> 6 // 4 of a kind
            hand.countCharacters().values.let { it.contains(3) && it.contains(2) } -> 5 // full house
            hand.countCharacters().values.contains(3) -> 4 // 3 of a kind
            hand.countCharacters().values.filter { it == 2 }.size == 2 -> 3 // 2 pairs
            hand.countCharacters().values.filter { it == 2 }.size == 1 -> 2 // 1 pair
            else -> 1 // high card
        }
        val value = hand.map {
            when (it) {
                '2' -> 0
                '3' -> 1
                '4' -> 2
                '5' -> 3
                '6' -> 4
                '7' -> 5
                '8' -> 6
                '9' -> 7
                'T' -> 8
                'J' -> 9
                'Q' -> 'A'
                'K' -> 'B'
                'A' -> 'C'
                else -> TODO("foobar - $it")
            }
        }.joinToString(" ")
        type.toString() + value
    }.mapIndexed { index, (_, bid) -> (index + 1) * bid.toInt() }.sum().toString()

fun String.countCharacters(): Map<Char, Int> {
    val charCountMap = mutableMapOf<Char, Int>()

    for (char in this) {
        charCountMap[char] = charCountMap.getOrDefault(char, 0) + 1
    }

    return charCountMap
}

fun runStep2(input: File): String =
    input.readLines().map { it.split(" ") }.sortedBy { (hand, bid) ->
        fun String.value(): Int = when {
            countCharacters().size == 1 -> 7 // 5 of a kind
            countCharacters().values.contains(4) -> 6 // 4 of a kind
            countCharacters().values.let { it.contains(3) && it.contains(2) } -> 5 // full house
            countCharacters().values.contains(3) -> 4 // 3 of a kind
            countCharacters().values.filter { it == 2 }.size == 2 -> 3 // 2 pairs
            countCharacters().values.filter { it == 2 }.size == 1 -> 2 // 1 pair
            else -> 1 // high card
        }
        val options = "23456789TQKA"
        val possibleHands = ArrayDeque<String>()
        possibleHands.add(hand)
        while(possibleHands.first().contains('J')) {
            val next = possibleHands.removeFirst()
            possibleHands.addAll(options.map { next.replaceFirst('J', it) })
        }
        val bestValue = possibleHands.map { it.value() }.max()
        val value = hand.map {
            when (it) {
                'J' -> 0
                '2' -> 1
                '3' -> 2
                '4' -> 3
                '5' -> 4
                '6' -> 5
                '7' -> 6
                '8' -> 7
                '9' -> 8
                'T' -> 9
                'Q' -> 'A'
                'K' -> 'B'
                'A' -> 'C'
                else -> TODO("foobar - $it")
            }
        }.joinToString(" ")
        bestValue.toString() + value
    }.mapIndexed { index, (_, bid) -> (index + 1) * bid.toInt() }.sum().toString()
