package day10

import day10.Thing.ANIMAL
import day10.Thing.GROUND
import day10.Thing.HORIZONTAL
import day10.Thing.NORTH_EAST
import day10.Thing.NORTH_WEST
import day10.Thing.OUTSIDE_PIPE
import day10.Thing.SOUTH_EAST
import day10.Thing.SOUTH_WEST
import day10.Thing.VERTICAL
import java.io.File
import kotlin.math.min

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == "8") { "Failed sample in step 1, got $step1Sample" }
    println("Step 1 answer: ${runStep1(input1)}")
//    val step2Sample = runStep2(sample)
//    require(step2Sample == "TODO(step2)") { "Failed sample in step 2, got $step2Sample" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(inputFile: File): String {
    val input = inputFile.readLines().map { it.map { it.toThing() } }
    val state = input.mapIndexed { y, things ->
        things.mapIndexed { x, thing ->
            when (thing) {
                ANIMAL -> thing
                is GROUND -> thing
                is HORIZONTAL -> thing.apply { left = if (x == 0) null else input[y][x - 1]; right = if (x == things.lastIndex) null else input[y][x + 1] }
                is NORTH_EAST -> thing.apply { above = if (y == 0) null else input[y - 1][x]; right = if (x == things.lastIndex) null else input[y][x + 1] }
                is NORTH_WEST -> thing.apply { above = if (y == 0) null else input[y - 1][x]; left = if (x == 0) null else input[y][x - 1] }
                is SOUTH_EAST -> thing.apply { right = if (x == things.lastIndex) null else input[y][x + 1]; below = if (y == input.lastIndex) null else input[y + 1][x] }
                is SOUTH_WEST -> thing.apply { left = if (x == 0) null else input[y][x - 1]; below = if (y == input.lastIndex) null else input[y + 1][x] }
                is VERTICAL -> thing.apply { above = if (y == 0) null else input[y - 1][x]; below = if (y == input.lastIndex) null else input[y + 1][x] }
                OUTSIDE_PIPE -> TODO()
            }
        }
    }
    val starters = state.flatten().filter {
        when (it) {
            ANIMAL -> false
            is GROUND -> false
            is HORIZONTAL -> it.left == ANIMAL || it.right == ANIMAL
            is NORTH_EAST -> it.above == ANIMAL || it.right == ANIMAL
            is NORTH_WEST -> it.above == ANIMAL || it.left == ANIMAL
            is SOUTH_EAST -> it.below == ANIMAL || it.right == ANIMAL
            is SOUTH_WEST -> it.below == ANIMAL || it.left == ANIMAL
            is VERTICAL -> it.above == ANIMAL || it.below == ANIMAL
            OUTSIDE_PIPE -> TODO()
        }
    }
    require(starters.size == 2)
    val found = ArrayList<Long>()
    found.add(ANIMAL.id)
    val current = starters.toMutableList()
    while (current.none { it.id in found }) {
        found.addAll(current.map { it.id })
        val temp = current.toList()
        current.clear()
        current.addAll(temp.map {
            when (it) {
                ANIMAL -> TODO()
                is GROUND -> TODO()
                is HORIZONTAL -> if (it.left!!.id in found) it.right else it.left
                is NORTH_EAST -> if (it.above!!.id in found) it.right else it.above
                is NORTH_WEST -> if (it.above!!.id in found) it.left else it.above
                is SOUTH_EAST -> if (it.below!!.id in found) it.right else it.below
                is SOUTH_WEST -> if (it.below!!.id in found) it.left else it.below
                is VERTICAL -> if (it.above!!.id in found) it.below else it.above
                OUTSIDE_PIPE -> TODO()
            }
        }.map { it!! })
    }
    return ((found.size - 1) / 2).toString()
}

var currentId = 0L

sealed class Thing {
    open val id: Long = currentId++

    data class VERTICAL(var above: Thing?, var below: Thing?) : Thing()
    data class HORIZONTAL(var left: Thing?, var right: Thing?) : Thing()
    data class NORTH_EAST(var above: Thing?, var right: Thing?) : Thing()
    data class NORTH_WEST(var above: Thing?, var left: Thing?) : Thing()
    data class SOUTH_WEST(var below: Thing?, var left: Thing?) : Thing()
    data class SOUTH_EAST(var below: Thing?, var right: Thing?) : Thing()
    data class GROUND(override val id: Long) : Thing()
    data object ANIMAL : Thing()
    data object OUTSIDE_PIPE : Thing()
}

fun Char.toThing(): Thing = when (this) {
    '|' -> VERTICAL(null, null)
    '-' -> HORIZONTAL(null, null)
    'L' -> NORTH_EAST(null, null)
    'J' -> NORTH_WEST(null, null)
    '7' -> SOUTH_WEST(null, null)
    'F' -> SOUTH_EAST(null, null)
    '.' -> GROUND(currentId++)
    'S' -> ANIMAL
    else -> TODO()
}

fun runStep2(inputFile: File): String {
    val input = inputFile.readLines().map { it.map { it.toThing() } }
    val state = input.mapIndexed { y, things ->
        things.mapIndexed { x, thing ->
            when (thing) {
                ANIMAL -> thing
                is GROUND -> thing
                is HORIZONTAL -> thing.apply { left = if (x == 0) null else input[y][x - 1]; right = if (x == things.lastIndex) null else input[y][x + 1] }
                is NORTH_EAST -> thing.apply { above = if (y == 0) null else input[y - 1][x]; right = if (x == things.lastIndex) null else input[y][x + 1] }
                is NORTH_WEST -> thing.apply { above = if (y == 0) null else input[y - 1][x]; left = if (x == 0) null else input[y][x - 1] }
                is SOUTH_EAST -> thing.apply { right = if (x == things.lastIndex) null else input[y][x + 1]; below = if (y == input.lastIndex) null else input[y + 1][x] }
                is SOUTH_WEST -> thing.apply { left = if (x == 0) null else input[y][x - 1]; below = if (y == input.lastIndex) null else input[y + 1][x] }
                is VERTICAL -> thing.apply { above = if (y == 0) null else input[y - 1][x]; below = if (y == input.lastIndex) null else input[y + 1][x] }
                OUTSIDE_PIPE -> TODO()
            }
        }
    }
    val starters = state.flatten().filter {
        when (it) {
            ANIMAL -> false
            is GROUND -> false
            is HORIZONTAL -> it.left == ANIMAL || it.right == ANIMAL
            is NORTH_EAST -> it.above == ANIMAL || it.right == ANIMAL
            is NORTH_WEST -> it.above == ANIMAL || it.left == ANIMAL
            is SOUTH_EAST -> it.below == ANIMAL || it.right == ANIMAL
            is SOUTH_WEST -> it.below == ANIMAL || it.left == ANIMAL
            is VERTICAL -> it.above == ANIMAL || it.below == ANIMAL
            OUTSIDE_PIPE -> TODO()
        }
    }
    require(starters.size == 2)
    val found = ArrayList<Long>()
    found.add(ANIMAL.id)
    val current = starters.toMutableList()
    while (current.none { it.id in found }) {
        found.addAll(current.map { it.id })
        val temp = current.toList()
        current.clear()
        current.addAll(temp.map {
            when (it) {
                ANIMAL -> TODO()
                is GROUND -> TODO()
                is HORIZONTAL -> if (it.left!!.id in found) it.right else it.left
                is NORTH_EAST -> if (it.above!!.id in found) it.right else it.above
                is NORTH_WEST -> if (it.above!!.id in found) it.left else it.above
                is SOUTH_EAST -> if (it.below!!.id in found) it.right else it.below
                is SOUTH_WEST -> if (it.below!!.id in found) it.left else it.below
                is VERTICAL -> if (it.above!!.id in found) it.below else it.above
                OUTSIDE_PIPE -> TODO()
            }
        }.map { it!! })
    }
    var newState = state.map { it.map { if (it.id in found) it else GROUND(currentId++) } }
    newState = newState.mapIndexed { y, things ->
        things.mapIndexed { x, thing ->
            if ((x == 0 || y == 0 || x == things.lastIndex || y == newState.lastIndex) && thing.id !in found) {
                OUTSIDE_PIPE
            } else
                thing
        }
    }
    var changedAny = true
    while (changedAny) {
        changedAny = false
        newState = newState.mapIndexed { y, things ->
            things.mapIndexed { x, thing ->
                if (thing == OUTSIDE_PIPE) OUTSIDE_PIPE
                else if ((
                        things.getOrNull(x - 1) == OUTSIDE_PIPE ||
                            things.getOrNull(x + 1) == OUTSIDE_PIPE ||
                            newState[y - 1][x] == OUTSIDE_PIPE ||
                            newState[y + 1][x] == OUTSIDE_PIPE
                        ) && thing.id !in found) {
                    changedAny = true
                    OUTSIDE_PIPE
                } else thing
            }
        }
    }
    changedAny = true
    while (changedAny) {
        changedAny = false
        newState = newState.mapIndexed { y, things ->
            things.mapIndexed { x, thing ->
                if (thing !is GROUND) thing
                else {
                    if (newState.getOrNull(y - 1)?.getOrNull(x).let { it != null && it is GROUND && it.id != thing.id }) {
                        changedAny = true
                        GROUND(min(thing.id, newState.getOrNull(y - 1)!![x].id))
                    } else if (things.getOrNull(x - 1).let { it != null && it is GROUND && it.id != thing.id }) {
                        changedAny = true
                        GROUND(min(thing.id, things.getOrNull(x - 1)!!.id))
                    } else if (newState.getOrNull(y + 1)?.getOrNull(x).let { it != null && it is GROUND && it.id != thing.id }) {
                        changedAny = true
                        GROUND(min(thing.id, newState.getOrNull(y + 1)!![x].id))
                    } else if (things.getOrNull(x + 1).let { it != null && it is GROUND && it.id != thing.id }) {
                        changedAny = true
                        GROUND(min(thing.id, things.getOrNull(x + 1)!!.id))
                    } else {
                        thing
                    }
                }
            }
        }
    }
    changedAny = true
    while (changedAny) {
        changedAny = false
        newState = newState.mapIndexed { y, things ->
            things.mapIndexed { x, thing ->
                if (thing !is GROUND) thing
                else {
                    val possibles = things.drop(x).filter { it !is OUTSIDE_PIPE && it !is GROUND && it !is HORIZONTAL }
                    if (possibles.contains(ANIMAL)) thing
                    else {
                        // 5 possibilities - vertical, corners
                        val count = possibles.count { it is VERTICAL } +
                            possibles.windowed(2).count { (it[0] is SOUTH_EAST && it[1] is NORTH_WEST) || (it[0] is NORTH_EAST && it[1] is SOUTH_WEST) }
                        if (count % 2 == 0)
                            OUTSIDE_PIPE
                        else thing
                    }
                }
            }
        }
    }
    changedAny = true
    while (changedAny) {
        changedAny = false
        newState = newState.mapIndexed { y, things ->
            things.mapIndexed { x, thing ->
                if (thing !is GROUND) thing
                else {
                    if (newState.getOrNull(y - 1)?.getOrNull(x).let { it != null && it is OUTSIDE_PIPE }) {
                        changedAny = true
                        OUTSIDE_PIPE
                    } else if (things.getOrNull(x - 1).let { it != null && it is OUTSIDE_PIPE }) {
                        changedAny = true
                        OUTSIDE_PIPE
                    } else if (newState.getOrNull(y + 1)?.getOrNull(x).let { it != null && it is OUTSIDE_PIPE }) {
                        changedAny = true
                        OUTSIDE_PIPE
                    } else if (things.getOrNull(x + 1).let { it != null && it is OUTSIDE_PIPE }) {
                        changedAny = true
                        OUTSIDE_PIPE
                    } else {
                        thing
                    }
                }
            }
        }
    }



    println(newState.flatten().filterIsInstance<GROUND>().map { it.id }.groupBy { it }.map { it.key to it.value.size }.sortedByDescending { it.second })


    newState.forEach {
        println(it.joinToString("") {
            when (it) {
                ANIMAL -> "S"
                is GROUND -> "I"
                is HORIZONTAL -> "-"
                is NORTH_EAST -> "L"
                is NORTH_WEST -> "J"
                OUTSIDE_PIPE -> "O"
                is SOUTH_EAST -> "F"
                is SOUTH_WEST -> "7"
                is VERTICAL -> "|"
            }
        })
    }

    return newState.flatten().filterIsInstance<GROUND>().size.toString()

}
// 733 - too high
// 394 - too low
// 399 - too low
// 566 - wrong??
// 449 - FINALLY!!
