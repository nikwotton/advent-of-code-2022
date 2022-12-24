package day23

import day23.Direction.East
import day23.Direction.North
import day23.Direction.South
import day23.Direction.West
import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 110
    println("Step 1b: ${runStep1(input1)}") // 3689
    println("Step 2a: ${runStep2(sample)}") // 20
    println("Step 2b: ${runStep2(input1)}") // 965
}

data class Position(var x: Int, var y: Int) {
    var plannedNextPosition: Position? = null
}
sealed class Direction {
    object North : Direction()
    object South : Direction()
    object East : Direction()
    object West : Direction()
}


fun runStep1(input: File): String {
    val elves = ArrayList<Position>()
    input.readLines().forEachIndexed { y, row ->
        row.forEachIndexed { x, elf ->
            if (elf == '#')
                elves.add(Position(x, y))
        }
    }
    var order = listOf(North, South, West, East)
    fun tryNorth(elf: Position): Boolean =
        if (elves.none { it in (-1..1).map { Position(elf.x + it, elf.y - 1) } }) {
            elf.plannedNextPosition = Position(elf.x, elf.y - 1)
            true
        } else false
    fun tryEast(elf: Position) =
        if (elves.none { it in (-1..1).map { Position(elf.x+1, elf.y + it) } }) {
            elf.plannedNextPosition = Position(elf.x+1, elf.y)
            true
        } else false
    fun trySouth(elf: Position) =
        if (elves.none { it in (-1..1).map { Position(elf.x+it, elf.y + 1) } }) {
            elf.plannedNextPosition = Position(elf.x, elf.y+1)
            true
        } else false
    fun tryWest(elf: Position) =
        if (elves.none { it in (-1..1).map { Position(elf.x-1, elf.y + it) } }) {
            elf.plannedNextPosition = Position(elf.x-1, elf.y)
            true
        } else false
    repeat(10) {
        elves.forEach { elf ->
            if ((-1..1).flatMap { x -> (-1..1).map { y -> Position(elf.x-x, elf.y-y) } }.filter { it != elf }.any { it in elves }) {
                var hitHere = false
                order.forEach {
                    when (it) {
                        East -> if (!hitHere && tryEast(elf)) hitHere = true
                        North -> if (!hitHere && tryNorth(elf)) hitHere = true
                        South -> if (!hitHere && trySouth(elf)) hitHere = true
                        West -> if (!hitHere && tryWest(elf)) hitHere = true
                    }
                }
            }
            if (elf.plannedNextPosition == null) {
                elf.plannedNextPosition = elf
            }
        }

        val proposedPositions = elves.map { it.plannedNextPosition }
        elves.forEach { elf ->
            if (proposedPositions.count { it == Position(elf.plannedNextPosition!!.x, elf.plannedNextPosition!!.y) } == 1) {
                elf.x = elf.plannedNextPosition!!.x
                elf.y = elf.plannedNextPosition!!.y
            }
            elf.plannedNextPosition = null
        }
        order = order.drop(1) + order.first()
    }
    val minX = elves.minOf { it.x }
    val maxX = elves.maxOf { it.x }
    val minY = elves.minOf { it.y }
    val maxY = elves.maxOf { it.y }
    return (minX..maxX).sumOf { x ->
        (minY..maxY).map { y ->
            if (elves.contains(Position(x, y))) 0 else 1
        }.sum()
    }.toString()
}

fun runStep2(input: File): String {
    val elves = ArrayList<Position>()
    input.readLines().forEachIndexed { y, row ->
        row.forEachIndexed { x, elf ->
            if (elf == '#')
                elves.add(Position(x, y))
        }
    }
    var order = listOf(North, South, West, East)
    fun tryNorth(elf: Position): Boolean =
        if (elves.none { it in (-1..1).map { Position(elf.x + it, elf.y - 1) } }) {
            elf.plannedNextPosition = Position(elf.x, elf.y - 1)
            true
        } else false
    fun tryEast(elf: Position) =
        if (elves.none { it in (-1..1).map { Position(elf.x+1, elf.y + it) } }) {
            elf.plannedNextPosition = Position(elf.x+1, elf.y)
            true
        } else false
    fun trySouth(elf: Position) =
        if (elves.none { it in (-1..1).map { Position(elf.x+it, elf.y + 1) } }) {
            elf.plannedNextPosition = Position(elf.x, elf.y+1)
            true
        } else false
    fun tryWest(elf: Position) =
        if (elves.none { it in (-1..1).map { Position(elf.x-1, elf.y + it) } }) {
            elf.plannedNextPosition = Position(elf.x-1, elf.y)
            true
        } else false
    var count = 0
    var hash = elves.joinToString(":") { "${it.x},${it.y}" }
    var prevHash = ""
    while(hash != prevHash) {
        elves.forEach { elf ->
            if ((-1..1).flatMap { x -> (-1..1).map { y -> Position(elf.x-x, elf.y-y) } }.filter { it != elf }.any { it in elves }) {
                var hitHere = false
                order.forEach {
                    when (it) {
                        East -> if (!hitHere && tryEast(elf)) hitHere = true
                        North -> if (!hitHere && tryNorth(elf)) hitHere = true
                        South -> if (!hitHere && trySouth(elf)) hitHere = true
                        West -> if (!hitHere && tryWest(elf)) hitHere = true
                    }
                }
            }
            if (elf.plannedNextPosition == null) {
                elf.plannedNextPosition = elf
            }
        }

        val proposedPositions = elves.map { it.plannedNextPosition }
        elves.forEach { elf ->
            if (proposedPositions.count { it == Position(elf.plannedNextPosition!!.x, elf.plannedNextPosition!!.y) } == 1) {
                elf.x = elf.plannedNextPosition!!.x
                elf.y = elf.plannedNextPosition!!.y
            }
            elf.plannedNextPosition = null
        }
        order = order.drop(1) + order.first()
        prevHash = hash
        hash = elves.joinToString(":") { "${it.x},${it.y}" }
        count++
        println("Currently on round: $count")
    }
    return count.toString()
}
