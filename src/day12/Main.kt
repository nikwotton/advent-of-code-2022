package day12

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "140"
    val step2SampleExpected = "80"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, instead of $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, instead of $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

data class Plot(
    val x: Int,
    val y: Int,
    val type: Char,
    var up: Plot? = null,
    var down: Plot? = null,
    var left: Plot? = null,
    var right: Plot? = null
)

fun runStep1(input: File): String {
    val map = input.readLines().mapIndexed { y, s -> s.mapIndexed { x, c -> Plot(x, y, c) } }
    map.flatten().forEach { plot ->
        plot.up = map.getOrNull(plot.y - 1)?.getOrNull(plot.x)?.let { if (it.type == plot.type) it else null }
        plot.down = map.getOrNull(plot.y + 1)?.getOrNull(plot.x)?.let { if (it.type == plot.type) it else null }
        plot.left = map.getOrNull(plot.y)?.getOrNull(plot.x - 1)?.let { if (it.type == plot.type) it else null }
        plot.right = map.getOrNull(plot.y)?.getOrNull(plot.x + 1)?.let { if (it.type == plot.type) it else null }
    }
    val counted = mutableListOf<Plot>()
    val plots = arrayListOf<List<Plot>>()
    map.flatten().forEach { plot ->
        if (plot in counted) return@forEach
        val thisPlot = arrayListOf<Plot>()
        fun helper(p: Plot) {
            if (p in thisPlot) return
            thisPlot.add(p)
            p.up?.let { helper(it) }
            p.down?.let { helper(it) }
            p.left?.let { helper(it) }
            p.right?.let { helper(it) }
        }
        helper(plot)
        plots.add(thisPlot)
        counted.addAll(thisPlot)
    }
    return plots.map { plotList ->
        val area = plotList.size
        val perimeter = plotList.map {
            var counter = 0
            if (it.up?.type != it.type) counter++
            if (it.down?.type != it.type) counter++
            if (it.left?.type != it.type) counter++
            if (it.right?.type != it.type) counter++
            counter
        }.sum()
        area * perimeter
    }.sum().toString()
}

fun runStep2(input: File): String {
    val map = input.readLines().mapIndexed { y, s -> s.mapIndexed { x, c -> Plot(x, y, c) } }
    map.flatten().forEach { plot ->
        plot.up = map.getOrNull(plot.y - 1)?.getOrNull(plot.x)?.let { if (it.type == plot.type) it else null }
        plot.down = map.getOrNull(plot.y + 1)?.getOrNull(plot.x)?.let { if (it.type == plot.type) it else null }
        plot.left = map.getOrNull(plot.y)?.getOrNull(plot.x - 1)?.let { if (it.type == plot.type) it else null }
        plot.right = map.getOrNull(plot.y)?.getOrNull(plot.x + 1)?.let { if (it.type == plot.type) it else null }
    }
    val counted = mutableListOf<Plot>()
    val plots = arrayListOf<List<Plot>>()
    map.flatten().forEach { plot ->
        if (plot in counted) return@forEach
        val thisPlot = arrayListOf<Plot>()
        fun helper(p: Plot) {
            if (p in thisPlot) return
            thisPlot.add(p)
            p.up?.let { helper(it) }
            p.down?.let { helper(it) }
            p.left?.let { helper(it) }
            p.right?.let { helper(it) }
        }
        helper(plot)
        plots.add(thisPlot)
        counted.addAll(thisPlot)
    }
    return plots.sortedBy { it[0].type }.map { plotList ->
        val area = plotList.size
        val perimeter = plotList.map {
            var counter = 0.0
            if (it.up?.type != it.type) {
                counter++
                if (it.left?.type == it.type && it.left?.up?.type != it.type) {
                    counter -= .5
                }
                if (it.right?.type == it.type && it.right?.up?.type != it.type) {
                    counter -= .5
                }
            }
            if (it.down?.type != it.type) {
                counter++
                if (it.left?.type == it.type && it.left?.down?.type != it.type) {
                    counter -= .5
                }
                if (it.right?.type == it.type && it.right?.down?.type != it.type) {
                    counter -= .5
                }
            }
            if (it.left?.type != it.type) {
                counter++
                if (it.up?.type == it.type && it.up?.left?.type != it.type) {
                    counter -= .5
                }
                if (it.down?.type == it.type && it.down?.left?.type != it.type) {
                    counter -= .5
                }
            }
            if (it.right?.type != it.type) {
                counter++
                if (it.up?.type == it.type && it.up?.right?.type != it.type) {
                    counter -= .5
                }
                if (it.down?.type == it.type && it.down?.right?.type != it.type) {
                    counter -= .5
                }
            }
            counter
        }.sum()
        area * perimeter
    }.sum().toInt().toString()
}
