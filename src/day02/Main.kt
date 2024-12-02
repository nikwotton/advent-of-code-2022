package day02

import java.io.File
import kotlin.math.abs

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val step1SampleExpected = "2"
    val step2SampleExpected = "4"
    val input1 = File("$workingDir/input_1.txt")
    val step1Sample = runStep1(sample)
    require(step1Sample == step1SampleExpected) { "Failed sample in step 1, got $step1Sample, expected $step1SampleExpected" }
    println("Step 1 answer: ${runStep1(input1)}")
    val step2Sample = runStep2(sample)
    require(step2Sample == step2SampleExpected) { "Failed sample in step 2, got $step2Sample, expected $step2SampleExpected" }
    println("Step 2 answer: ${runStep2(input1)}")
}

fun runStep1(input: File): String = input.readLines().map {
   val foo1 = it.split(" ").map { it.toInt() }
   if (foo1.joinToString("") == foo1.sorted().joinToString("") || foo1.joinToString("") == foo1.sortedDescending().joinToString("")) {
       val foo = foo1
           .windowed(2)
           .map { (a, b) ->
           if (a == b) 1
           else if (abs(a - b) > 3) 1
           else 0
       }.sum()
       if (foo == 0) 1 else 0
   } else 0
}.sum().toString()

fun runStep2(input: File): String = input.readLines().map {
    fun helper(inp: List<Int>): Boolean =
        (inp.joinToString("") == inp.sorted().joinToString("") || inp.joinToString("") == inp.sortedDescending().joinToString("")) &&
        inp.windowed(2)
        .map { (a, b) ->
            if (a == b) 1
            else if (abs(a - b) > 3) 1
            else 0
        }.sum() == 0
    val foo1 = it.split(" ").map { it.toInt() }
        if (helper(foo1)) 1 else {
            if (foo1.indices.toList().stream().anyMatch {
                val mutable = foo1.toMutableList()
                mutable.removeAt(it)
                helper(mutable)
            }) 1 else 0
        }
}.sum().toString()
