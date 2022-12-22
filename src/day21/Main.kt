package day21

import java.io.File

val workingDir = "src/${object {}.javaClass.`package`.name}"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}") // 152
    println("Step 1b: ${runStep1(input1)}") // 78342931359552
    println("Step 2a: ${runStep2(sample)}") // 301
    println("Step 2b: ${runStep2(input1)}") // 3296135418820
}
typealias MonkeyMap = HashMap<String, String>
fun MonkeyMap.customGet(index: String): String {
    val value = get(index)!!
    if(value.toLongOrNull() != null) return value
    val (x, op, y) = value.split(" ")
    return when (op) {
        "+" -> customGet(x).toLong() + customGet(y).toLong()
        "-" -> customGet(x).toLong() - customGet(y).toLong()
        "*" -> customGet(x).toLong() * customGet(y).toLong()
        "/" -> customGet(x).toLong() / customGet(y).toLong()
        else -> TODO()
    }.toString()
}

fun File.toMonkeys(): MonkeyMap = readLines().associate {
    val (name, op) = it.split(":").map { it.trim() }
    name to op
}.toMutableMap() as MonkeyMap

fun runStep1(input: File): String = input.toMonkeys().customGet("root")

fun runStep2(input: File): String {
    val monkeys = input.toMonkeys()
    val (v1, v2) = monkeys["root"]!!.split(" + ")
    fun removeVariables(value: String): String {
        if (value == "humn") return "x"
        if (value.toLongOrNull() != null) return value
        return if (value.contains(" ")) {
            val (x, op, y) = value.split(" ")
            "(${removeVariables(x)} $op ${removeVariables(y)})"
        } else {
            removeVariables(monkeys[value]!!)
        }
    }
    tailrec fun simplifyEquation(equation: String): String {
        var ret = equation
        Regex("\\([0-9]+ \\+ [0-9]+\\)").find(equation)?.let {
            ret = equation.replaceRange(it.range, it.value.removeSurrounding("(", ")").split(" + ").sumOf(String::toLong).toString())
        }
        Regex("\\([0-9]+ - [0-9]+\\)").find(equation)?.let {
            ret = equation.replaceRange(it.range, it.value.removeSurrounding("(", ")").split(" - ").map(String::toLong).reduce { acc, l -> acc - l }.toString())
        }
        Regex("\\([0-9]+ \\* [0-9]+\\)").find(equation)?.let {
            ret = equation.replaceRange(it.range, it.value.removeSurrounding("(", ")").split(" * ").map(String::toLong).reduce { acc, l -> acc * l }.toString())
        }
        Regex("\\([0-9]+ / [0-9]+\\)").find(equation)?.let {
            ret = equation.replaceRange(it.range, it.value.removeSurrounding("(", ")").split(" / ").map(String::toLong).reduce { acc, l -> acc / l }.toString())
        }
        return if (ret == equation) ret
        else simplifyEquation(ret)
    }

    tailrec fun findX(number: Long, equation: String): Long {
        val restOfEquationRegex = "[0-9\\(\\)\\s\\+\\-\\/\\*x]+"
        if(equation == "x") return number
        Regex("\\($restOfEquationRegex / [0-9]+\\)").matchEntire(equation)?.let {
            val divisor = equation.dropLast(1).takeLastWhile { it.isDigit() }.toLong()
            val newEquation = equation.removeSurrounding("(", " / ${divisor})")
            return findX(number * divisor, newEquation)
        }
        Regex("\\([0-9]+ \\+ $restOfEquationRegex\\)").matchEntire(equation)?.let {
            val num = equation.drop(1).takeWhile { it.isDigit() }.toLong()
            val newEquation = equation.removeSurrounding("($num + ", ")")
            return findX(number - num, newEquation)
        }
        Regex("\\([0-9]+ \\* $restOfEquationRegex\\)").matchEntire(equation)?.let {
            val num = equation.drop(1).takeWhile { it.isDigit() }.toLong()
            val newEquation = equation.removeSurrounding("($num * ", ")")
            return findX(number / num, newEquation)
        }
        Regex("\\($restOfEquationRegex - [0-9]+\\)").matchEntire(equation)?.let {
            val num = equation.dropLast(1).takeLastWhile { it.isDigit() }.toLong()
            val newEquation = equation.removeSurrounding("(", " - ${num})")
            return findX(number + num, newEquation)
        }
        Regex("\\([0-9]+ - $restOfEquationRegex\\)").matchEntire(equation)?.let {
            val num = equation.drop(1).takeWhile { it.isDigit() }.toLong()
            val newEquation = equation.removeSurrounding("($num - ", ")")
            return findX((number - num) * -1, newEquation)
        }
        Regex("\\($restOfEquationRegex \\+ [0-9]+\\)").matchEntire(equation)?.let {
            val num = equation.dropLast(1).takeLastWhile { it.isDigit() }.toLong()
            val newEquation = equation.removeSurrounding("(", " + ${num})")
            return findX(number - num, newEquation)
        }
        Regex("\\($restOfEquationRegex \\* [0-9]+\\)").matchEntire(equation)?.let {
            val num = equation.dropLast(1).takeLastWhile { it.isDigit() }.toLong()
            val newEquation = equation.removeSurrounding("(", " * ${num})")
            return findX(number / num, newEquation)
        }
        TODO("Need to handle $equation")
    }
    val simpV1 = simplifyEquation(removeVariables(v1))
    val simpV2 = simplifyEquation(removeVariables(v2))
    val num = if (simpV1.toLongOrNull() != null) simpV1.toLong() else simpV2.toLong()
    val eq = if (simpV1.toLongOrNull() != null) simpV2 else simpV1
    return findX(num, eq).toString()
}
