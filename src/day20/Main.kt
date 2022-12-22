package day20

import java.io.File
import kotlin.math.abs

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 3
    println("Step 1b: ${runStep1(input1)}") // 17490
    println("Step 2a: ${runStep2(sample)}") // 1623178306
    println("Step 2b: ${runStep2(input1)}") // 1632917375836
}

data class Node(val value: Int, val shrunkValue: Long, var prev: Node?, var next: Node?, var originalPrev: Node?, var originalNext: Node?) {
    fun printLine() {
        var c = this
        val ret = ArrayList<Long>()
        do {
            ret.add(c.shrunkValue)
            c = c.next!!
        } while (c != this)
        println(ret)
    }
}

fun runStep1(input: File): String {
    val inputNumbers = input.readLines().map { it.toInt() }.map { Node(it, -1, null, null, null, null) }
    inputNumbers.forEachIndexed { index, node ->
        when (index) {
            0 -> {
                node.next = inputNumbers[1]
                node.prev = inputNumbers.last()
            }
            inputNumbers.size-1 -> {
                node.next = inputNumbers.first()
                node.prev = inputNumbers[index-1]
            }
            else -> {
                node.next = inputNumbers[index+1]
                node.prev = inputNumbers[index-1]
            }
        }
    }
    inputNumbers.forEach {
        it.originalNext = it.next
        it.originalPrev = it.prev
    }

    var current = inputNumbers.first()
    do {
        if (current.value < 0) {
            repeat(abs(current.value)) {
                val twoBack = current.prev!!.prev!!
                val oneBack = current.prev!!
                val next = current.next!!
                twoBack.next = current
                oneBack.prev = current
                oneBack.next = next
                next.prev = oneBack
                current.prev = twoBack
                current.next = oneBack
            }
        } else if (current.value == 0) {
            Unit
        } else if (current.value > 0) {
            repeat(current.value) {
                val prev = current.prev!!
                val oneForward = current.next!!
                val twoForward = current.next!!.next!!
                prev.next = oneForward
                oneForward.prev = prev
                oneForward.next = current
                twoForward.prev = current
                current.prev = oneForward
                current.next = twoForward
            }
        } else TODO()
        current = current.originalNext!!
    } while (current != inputNumbers.first())
    tailrec fun Node.atIndex(index: Int): Node {
        if (index == 0) return this
        return next!!.atIndex(index-1)
    }

    return listOf(1000, 2000, 3000).sumOf { inputNumbers.first { it.value == 0 }.atIndex(it).value }.toString()

}

fun runStep2(input: File): String {
    val decryptionKey = 811589153L
    val lines = input.readLines().count { it.isNotBlank() } - 1
    val inputNumbers = input.readLines().map { it.toInt() }.map {
        Node(((it * decryptionKey) % lines).toInt(), it * decryptionKey, null, null, null, null)
    }
    inputNumbers.forEachIndexed { index, node ->
        when (index) {
            0 -> {
                node.next = inputNumbers[1]
                node.prev = inputNumbers.last()
            }
            inputNumbers.size-1 -> {
                node.next = inputNumbers.first()
                node.prev = inputNumbers[index-1]
            }
            else -> {
                node.next = inputNumbers[index+1]
                node.prev = inputNumbers[index-1]
            }
        }
    }
    inputNumbers.forEach {
        it.originalNext = it.next
        it.originalPrev = it.prev
    }

    repeat(10) {
        var current = inputNumbers.first()
        do {
            if (current.value < 0) {
                repeat(abs(current.value)) {
                    val twoBack = current.prev!!.prev!!
                    val oneBack = current.prev!!
                    val next = current.next!!
                    twoBack.next = current
                    oneBack.prev = current
                    oneBack.next = next
                    next.prev = oneBack
                    current.prev = twoBack
                    current.next = oneBack
                }
            } else if (current.value == 0) {
                Unit
            } else if (current.value > 0) {
                repeat(current.value) {
                    val prev = current.prev!!
                    val oneForward = current.next!!
                    val twoForward = current.next!!.next!!
                    prev.next = oneForward
                    oneForward.prev = prev
                    oneForward.next = current
                    twoForward.prev = current
                    current.prev = oneForward
                    current.next = twoForward
                }
            } else TODO()
            current = current.originalNext!!
        } while (current != inputNumbers.first())
    }
    tailrec fun Node.atIndex(index: Int): Node {
        if (index == 0) return this
        return next!!.atIndex(index-1)
    }

    return listOf(1000, 2000, 3000).sumOf {
        (inputNumbers.first { it.value == 0 }.atIndex(it).shrunkValue)
    }.toString()

}
