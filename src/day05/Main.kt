package day05

import java.io.File
import java.util.ArrayList
import java.util.Stack

const val workingDir = "src/day05"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String {
    val stacks = mutableListOf<Stack<Char>>()
    input.readLines().takeWhile { !it.trim().first().isDigit() }.reversed().forEach {
        it.replace("[", "").replace("]", "").replace("    ", "_").replace(" ", "").forEachIndexed { index, c ->
            if (c != '_') {
                while (stacks.getOrNull(index) == null) stacks.add(Stack())
                stacks[index].push(c)
            }
        }
    }
    input.readLines().filter { it.startsWith("move ") }.forEach {
        val count = it.removePrefix("move ").split(" ")[0].toInt()
        val from = it.split(" from ")[1].split(" to ")[0].toInt()
        val to = it.split(" to ")[1].toInt()
        repeat(count) {
            val moving = stacks[from - 1].pop()
            stacks[to - 1].push(moving)
        }
    }
    return stacks.map { it.peek() }.joinToString("")
}

fun runStep2(input: File): String {
    val stacks = mutableListOf<Stack<Char>>()
    input.readLines().takeWhile { !it.trim().first().isDigit() }.reversed().forEach {
        it.replace("[", "").replace("]", "").replace("    ", "_").replace(" ", "").forEachIndexed { index, c ->
            if (c != '_') {
                while (stacks.getOrNull(index) == null) stacks.add(Stack())
                stacks[index].push(c)
            }
        }
    }
    input.readLines().filter { it.startsWith("move ") }.forEach {
        val count = it.removePrefix("move ").split(" ")[0].toInt()
        val from = it.split(" from ")[1].split(" to ")[0].toInt()
        val to = it.split(" to ")[1].toInt()
        val moving = Stack<Char>()
        repeat(count) {
            moving.add(stacks[from - 1].pop())
        }
        moving.reversed().forEach {
            stacks[to - 1].push(it)
        }
    }
    return stacks.map { it.peek() }.joinToString("")
}
