package day15

import day15.Type.*
import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "10092"
    val step2SampleExpected = "9021"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

enum class Type {
    WALL, BOX, ROBOT, SPACE
}

data class Position(
    val x: Int,
    val y: Int,
    var type: Type,
    var up: Position? = null,
    var down: Position? = null,
    var left: Position? = null,
    var right: Position? = null
)

fun drawMap(map: List<List<Position>>) {
    map.forEach {
        println(it.map {
            when (it.type) {
                WALL -> "#"
                BOX -> "O"
                ROBOT -> "@"
                SPACE -> "."
            }
        }.joinToString(""))
    }
}

fun drawMap2(map: List<List<Position2>>) {
    map.forEach {
        println(it.map {
            when (it.type) {
                Type2.WALL -> "#"
                Type2.BOX_LEFT -> "["
                Type2.BOX_RIGHT -> "]"
                Type2.ROBOT -> "@"
                Type2.SPACE -> "."
            }
        }.joinToString(""))
    }
}

fun runStep1(input: File): String {
    val map = input.readLines().filter { it.contains("#") || it.contains(".") || it.contains("O") || it.contains("@") }
        .mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Position(
                    x, y, when (c) {
                        '#' -> WALL
                        'O' -> BOX
                        '@' -> ROBOT
                        '.' -> SPACE
                        else -> TODO("Should not be hit")
                    }
                )
            }
        }
    map.forEachIndexed { y, positions ->
        positions.forEachIndexed { x, position ->
            position.up = map.getOrNull(y - 1)?.getOrNull(x)
            position.down = map.getOrNull(y + 1)?.getOrNull(x)
            position.left = map.getOrNull(y)?.getOrNull(x - 1)
            position.right = map.getOrNull(y)?.getOrNull(x + 1)
        }
    }
    val directions =
        input.readLines().filter { it.contains('<') || it.contains('>') || it.contains('^') || it.contains('v') }
            .fold("", { acc, next -> acc + next })

    fun Position.canMove(direction: Position.() -> Position): Boolean {
        val next = direction()
        if (next.type == SPACE) return true
        if (next.type == WALL) return false
        if (next.type == BOX) return next.canMove(direction)
        TODO("Should not be hit")
    }

    fun getNextSpace(p: Position, direction: Position.() -> Position): Position = when (p.type) {
        WALL -> TODO()
        BOX -> getNextSpace(p.direction(), direction)
        ROBOT -> getNextSpace(p.direction(), direction)
        SPACE -> p
    }

    fun Position.move(direction: Position.() -> Position) {
        type = SPACE
        when (direction().type) {
            WALL -> TODO()
            BOX -> {
                direction().type = ROBOT
                getNextSpace(direction(), direction).type = BOX
            }

            ROBOT -> TODO()
            SPACE -> {
                direction().type = ROBOT
            }
        }
    }
//    println("Initial State:")
//    drawMap(map)
    directions.forEach {
//        println()
//        println("Move $it:")
        val robot = map.flatten().first { it.type == ROBOT }
        when (it) {
            '<' -> if (robot.canMove { left!! }) robot.move({ left!! })
            '>' -> if (robot.canMove { right!! }) robot.move({ right!! })
            '^' -> if (robot.canMove { up!! }) robot.move({ up!! })
            'v' -> if (robot.canMove { down!! }) robot.move({ down!! })
            else -> TODO("Should not be hit")
        }
//        drawMap(map)
    }
    return map.flatten().filter { it.type == BOX }.map { (it.y * 100) + it.x }.sum().toString()
}

enum class Type2 {
    WALL, BOX_LEFT, BOX_RIGHT, ROBOT, SPACE
}

data class Position2(
    var x: Int,
    var y: Int,
    var type: Type2,
    var up: Position2? = null,
    var down: Position2? = null,
    var left: Position2? = null,
    var right: Position2? = null
)

fun runStep2(input: File): String {
    val map = input.readLines().filter { it.contains("#") || it.contains(".") || it.contains("O") || it.contains("@") }
        .mapIndexed { y, s ->
            s.flatMapIndexed { x, c ->
                listOf(
                    Position2(
                        x, y, when (c) {
                            '#' -> Type2.WALL
                            'O' -> Type2.BOX_LEFT
                            '@' -> Type2.ROBOT
                            '.' -> Type2.SPACE
                            else -> TODO("Should not be hit")
                        }
                    ), Position2(
                        x, y, when (c) {
                            '#' -> Type2.WALL
                            'O' -> Type2.BOX_RIGHT
                            '@' -> Type2.SPACE
                            '.' -> Type2.SPACE
                            else -> TODO("Should not be hit")
                        }
                    )
                )
            }
        }
    map.forEachIndexed { y, position2s -> position2s.forEachIndexed { x, position2 ->
        position2.x = x
        position2.y = y
    } }
    map.forEachIndexed { y, positions ->
        positions.forEachIndexed { x, position ->
            position.up = map.getOrNull(y - 1)?.getOrNull(x)
            position.down = map.getOrNull(y + 1)?.getOrNull(x)
            position.left = map.getOrNull(y)?.getOrNull(x - 1)
            position.right = map.getOrNull(y)?.getOrNull(x + 1)
        }
    }
    val directions =
        input.readLines().filter { it.contains('<') || it.contains('>') || it.contains('^') || it.contains('v') }
            .fold("", { acc, next -> acc + next })

    fun Position2.canMove(direction: Position2.() -> Position2): Boolean {
        val next = direction()
        val vertical = next == up || next == down
        return when (next.type) {
            Type2.WALL -> false
            Type2.BOX_LEFT -> if (vertical) {
                next.canMove(direction) && next.right!!.canMove(direction)
            } else {
                next.canMove(direction)
            }

            Type2.BOX_RIGHT -> if (vertical) {
                next.canMove(direction) && next.left!!.canMove(direction)
            } else {
                next.canMove(direction)
            }

            Type2.ROBOT -> TODO()
            Type2.SPACE -> true
        }
    }

//    println("Initial State:")
//    drawMap2(map)
    directions.forEach {
        if (map.any {
                it.windowed(2).any {
                    it.first().type == Type2.SPACE && it[1].type == Type2.BOX_RIGHT ||
                    it.first().type == Type2.BOX_LEFT && it[1].type == Type2.SPACE
                }
            }) System.exit(1)
//        println()
//        println("Move $it:")
        val robot = map.flatten().first { it.type == Type2.ROBOT }
        when (it) {
            '<' -> if (robot.canMove { left!! }) {
                robot.type = Type2.SPACE
                when (robot.left!!.type) {
                    Type2.WALL -> TODO()
                    Type2.BOX_LEFT -> TODO()
                    Type2.BOX_RIGHT -> {
                        fun handleNext(p: Position2) {
                            when (p.type) {
                                Type2.WALL -> TODO()
                                Type2.BOX_LEFT -> {
                                    p.type = Type2.BOX_RIGHT
                                    handleNext(p.left!!)
                                }

                                Type2.BOX_RIGHT -> {
                                    p.type = Type2.BOX_LEFT
                                    handleNext(p.left!!)
                                }

                                Type2.ROBOT -> TODO()
                                Type2.SPACE -> {
                                    p.type = Type2.BOX_LEFT
                                }
                            }
                        }
                        robot.left!!.type = Type2.ROBOT
                        handleNext(robot.left!!.left!!)
                    }

                    Type2.ROBOT -> TODO()
                    Type2.SPACE -> {
                        robot.left!!.type = Type2.ROBOT
                    }
                }
            }

            '>' -> if (robot.canMove { right!! }) {
                robot.type = Type2.SPACE
                when (robot.right!!.type) {
                    Type2.WALL -> TODO()
                    Type2.BOX_RIGHT -> TODO()
                    Type2.BOX_LEFT -> {
                        fun handleNext(p: Position2) {
                            when (p.type) {
                                Type2.WALL -> TODO()
                                Type2.BOX_RIGHT -> {
                                    p.type = Type2.BOX_LEFT
                                    handleNext(p.right!!)
                                }

                                Type2.BOX_LEFT -> {
                                    p.type = Type2.BOX_RIGHT
                                    handleNext(p.right!!)
                                }

                                Type2.ROBOT -> TODO()
                                Type2.SPACE -> {
                                    p.type = Type2.BOX_RIGHT
                                }
                            }
                        }
                        robot.right!!.type = Type2.ROBOT
                        handleNext(robot.right!!.right!!)
                    }

                    Type2.ROBOT -> TODO()
                    Type2.SPACE -> {
                        robot.right!!.type = Type2.ROBOT
                    }
                }
            }

            '^' -> if (robot.canMove { up!! }) {
//                println("${robot.x},${robot.y}")
                robot.type = Type2.SPACE
                val handled = ArrayList<Pair<Int, Int>>()
                when (robot.up!!.type) {
                    Type2.WALL -> TODO()
                    Type2.BOX_RIGHT -> {
                        val queue = ArrayDeque<Pair<Position2, Type2>>()
                        fun handleNext() {
                            if (queue.isEmpty()) return
                            val next = queue.removeFirst()
                            val p = next.first
                            val prev = next.second
                            if ((p.x to p.y) in handled) {
                                handleNext()
                                return
                            } else handled.add(p.x to p.y)
                            when (p.type) {
                                Type2.WALL -> TODO()
                                Type2.BOX_RIGHT -> {
                                    p.type = prev
                                    queue.add(p.up!! to Type2.BOX_RIGHT)
                                    queue.add(p.left!!.up!! to Type2.BOX_LEFT)
                                    handleNext()
                                    queue.add(p.left!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.BOX_LEFT -> {
                                    p.type = prev
                                    queue.add(p.up!! to Type2.BOX_LEFT)
                                    queue.add(p.right!!.up!! to Type2.BOX_RIGHT)
                                    handleNext()
                                    queue.add(p.right!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.ROBOT -> TODO()
                                Type2.SPACE -> {
                                    p.type = prev
                                    handleNext()
                                }
                            }
                        }
                        robot.up!!.type = Type2.ROBOT
                        robot.up!!.left!!.type = Type2.SPACE
                        queue.add(robot.up!!.up!! to Type2.BOX_RIGHT)
                        queue.add(robot.up!!.left!!.up!! to Type2.BOX_LEFT)
                        handleNext()
                    }

                    Type2.BOX_LEFT -> {
                        val queue = ArrayDeque<Pair<Position2, Type2>>()
                        fun handleNext() {
                            if (queue.isEmpty()) return
                            val next = queue.removeFirst()
                            val p = next.first
                            val prev = next.second
                            if ((p.x to p.y) in handled) {
                                handleNext()
                                return
                            } else handled.add(p.x to p.y)
                            when (p.type) {
                                Type2.WALL -> TODO()
                                Type2.BOX_RIGHT -> {
                                    p.type = prev
                                    queue.add(p.up!! to Type2.BOX_RIGHT)
                                    queue.add(p.left!!.up!! to Type2.BOX_LEFT)
                                    handleNext()
                                    queue.add(p.left!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.BOX_LEFT -> {
                                    p.type = prev
                                    queue.add(p.up!! to Type2.BOX_LEFT)
                                    queue.add(p.right!!.up!! to Type2.BOX_RIGHT)
                                    handleNext()
                                    queue.add(p.right!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.ROBOT -> TODO()
                                Type2.SPACE -> {
                                    p.type = prev
                                    handleNext()
                                }
                            }
                        }
                        robot.up!!.type = Type2.ROBOT
                        robot.up!!.right!!.type = Type2.SPACE
                        queue.add(robot.up!!.up!! to Type2.BOX_LEFT)
                        queue.add(robot.up!!.right!!.up!! to Type2.BOX_RIGHT)
                        handleNext()
                    }

                    Type2.ROBOT -> TODO()
                    Type2.SPACE -> {
                        robot.up!!.type = Type2.ROBOT
                    }
                }
            }

            'v' -> if (robot.canMove { down!! }) {
                robot.type = Type2.SPACE
                val handled = ArrayList<Pair<Int, Int>>()
                when (robot.down!!.type) {
                    Type2.WALL -> TODO()
                    Type2.BOX_RIGHT -> {
                        val queue = ArrayDeque<Pair<Position2, Type2>>()
                        fun handleNext() {
                            if (queue.isEmpty()) return
                            val next = queue.removeFirst()
                            val p = next.first
                            val prev = next.second
                            if ((p.x to p.y) in handled) {
                                handleNext()
                                return
                            } else handled.add(p.x to p.y)
                            when (p.type) {
                                Type2.WALL -> TODO()
                                Type2.BOX_RIGHT -> {
                                    p.type = prev
                                    queue.add(p.down!! to Type2.BOX_RIGHT)
                                    queue.add(p.left!!.down!! to Type2.BOX_LEFT)
                                    handleNext()
                                    queue.add(p.left!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.BOX_LEFT -> {
                                    p.type = prev
                                    queue.add(p.down!! to Type2.BOX_LEFT)
                                    queue.add(p.right!!.down!! to Type2.BOX_RIGHT)
                                    handleNext()
                                    queue.add(p.right!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.ROBOT -> TODO()
                                Type2.SPACE -> {
                                    p.type = prev
                                    handleNext()
                                }
                            }
                        }
                        robot.down!!.type = Type2.ROBOT
                        robot.down!!.left!!.type = Type2.SPACE
                        queue.add(robot.down!!.down!! to Type2.BOX_RIGHT)
                        queue.add(robot.down!!.left!!.down!! to Type2.BOX_LEFT)
                        handleNext()
                    }

                    Type2.BOX_LEFT -> {
                        val queue = ArrayDeque<Pair<Position2, Type2>>()
                        fun handleNext() {
                            if (queue.isEmpty()) return
                            val next = queue.removeFirst()
                            val p = next.first
                            val prev = next.second
                            if ((p.x to p.y) in handled) {
                                handleNext()
                                return
                            } else handled.add(p.x to p.y)
                            when (p.type) {
                                Type2.WALL -> TODO()
                                Type2.BOX_RIGHT -> {
                                    p.type = prev
                                    queue.add(p.down!! to Type2.BOX_RIGHT)
                                    queue.add(p.left!!.down!! to Type2.BOX_LEFT)
                                    handleNext()
                                    queue.add(p.left!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.BOX_LEFT -> {
                                    p.type = prev
                                    queue.add(p.down!! to Type2.BOX_LEFT)
                                    queue.add(p.right!!.down!! to Type2.BOX_RIGHT)
                                    handleNext()
                                    queue.add(p.right!! to Type2.SPACE)
                                    handleNext()
                                }

                                Type2.ROBOT -> TODO()
                                Type2.SPACE -> {
                                    p.type = prev
                                    handleNext()
                                }
                            }
                        }
                        robot.down!!.type = Type2.ROBOT
                        robot.down!!.right!!.type = Type2.SPACE
                        queue.add(robot.down!!.down!! to Type2.BOX_LEFT)
                        queue.add(robot.down!!.right!!.down!! to Type2.BOX_RIGHT)
                        handleNext()
                    }

                    Type2.ROBOT -> TODO()
                    Type2.SPACE -> {
                        robot.down!!.type = Type2.ROBOT
                    }
                }
            }

            else -> TODO("Should not be hit")
        }
//        drawMap2(map)
    }
    return map.flatten().filter { it.type == Type2.BOX_LEFT }.map { (it.y * 100) + it.x }.sum().toString()
}
