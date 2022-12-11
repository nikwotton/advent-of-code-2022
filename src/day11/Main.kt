package day11

import java.io.File

const val workingDir = "src/day11"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

@JvmInline
value class Item(val worryLevel: Long)

@JvmInline
value class MonkeyId(val id: Long)

data class Monkey(
    val id: MonkeyId,
    val items: MutableList<Item>,
    val operation: (old: Item) -> Item,
    val test: (Item) -> MonkeyId,
    var inspectionCounter: Long = 0
)

fun runStep1(input: File): String {
    val monkeys: MutableList<Monkey> = mutableListOf()
    input.readText().split("\n\n").forEach {
        val lines = it.split("\n")
        val monkeyId = MonkeyId(lines[0].removeSurrounding("Monkey ", ":").toLong())
        val startingItems = lines[1].removePrefix("  Starting items: ")
            .let {
                if (it.contains(","))
                    it.split(", ").map { Item(it.toLong()) }.toMutableList()
                else
                    mutableListOf(Item(it.toLong()))
            }

        val operation = lines[2].removePrefix("  Operation: new = ").split(" ").let {
            { old: Item ->
                val a = if (it[0] == "old") old else Item(it[0].toLong())
                val b = if (it[2] == "old") old else Item(it[2].toLong())
                Item(
                    when (it[1]) {
                        "+" -> a.worryLevel + b.worryLevel
                        "*" -> a.worryLevel * b.worryLevel
                        else -> TODO("Need to handle ${it[0]}")
                    }
                )
            }
        }
        val divisibleBy = lines[3].removePrefix("  Test: divisible by ").toLong()
        val ifTrue = lines[4].removePrefix("    If true: throw to monkey ").toLong()
        val ifFalse = lines[5].removePrefix("    If false: throw to monkey ").toLong()
        val test = { item: Item -> if (item.worryLevel % divisibleBy == 0L) MonkeyId(ifTrue) else MonkeyId(ifFalse) }
        monkeys.add(Monkey(monkeyId, startingItems, operation, test))
    }

    repeat(20) {
        monkeys.sortedBy { it.id.id }.forEach { monkey ->
            // inspect item
            monkey.items.forEach {
                monkey.inspectionCounter++
                val newWorryLevel = Item(monkey.operation(it).worryLevel / 3)
                val newMonkeyId = monkey.test(newWorryLevel)
                monkeys.first { it.id == newMonkeyId }.items.add(newWorryLevel)
            }
            monkey.items.clear()
        }
    }
    return monkeys.map { it.inspectionCounter }.sortedDescending().take(2).let { (a, b) -> a * b }.toString()
}

fun runStep2(input: File): String {
    val monkeys: MutableList<Monkey> = mutableListOf()
    var masterDivisor = 1L
    input.readText().split("\n\n").forEach {
        val lines = it.split("\n")
        val monkeyId = MonkeyId(lines[0].removeSurrounding("Monkey ", ":").toLong())
        val startingItems = lines[1].removePrefix("  Starting items: ")
            .let {
                if (it.contains(","))
                    it.split(", ").map { Item(it.toLong()) }.toMutableList()
                else
                    mutableListOf(Item(it.toLong()))
            }

        val operation = lines[2].removePrefix("  Operation: new = ").split(" ").let {
            { old: Item ->
                val a = if (it[0] == "old") old else Item(it[0].toLong())
                val b = if (it[2] == "old") old else Item(it[2].toLong())
                Item(
                    when (it[1]) {
                        "+" -> a.worryLevel + b.worryLevel
                        "*" -> a.worryLevel * b.worryLevel
                        else -> TODO("Need to handle ${it[0]}")
                    }
                )
            }
        }
        val divisibleBy = lines[3].removePrefix("  Test: divisible by ").toLong()
        masterDivisor *= divisibleBy
        val ifTrue = lines[4].removePrefix("    If true: throw to monkey ").toLong()
        val ifFalse = lines[5].removePrefix("    If false: throw to monkey ").toLong()
        val test = { item: Item -> if (item.worryLevel % divisibleBy == 0L) MonkeyId(ifTrue) else MonkeyId(ifFalse) }
        monkeys.add(Monkey(monkeyId, startingItems, operation, test))
    }

    repeat(10_000) { index ->
        monkeys.sortedBy { it.id.id }.forEach { monkey ->
            // inspect item
            monkey.items.forEach {
                monkey.inspectionCounter++
                val newWorryLevel = Item(monkey.operation(it).worryLevel % masterDivisor)
                val newMonkeyId = monkey.test(newWorryLevel)
                monkeys.first { it.id == newMonkeyId }.items.add(newWorryLevel)
            }
            monkey.items.clear()
        }
    }
    return monkeys.map { it.inspectionCounter }.sortedDescending().take(2).let { (a, b) -> a * b }.toString()
}
