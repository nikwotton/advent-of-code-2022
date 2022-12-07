package day07

import day07.FileSystemThing.Dir
import java.io.File

const val workingDir = "src/day07"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

fun runStep1(input: File): String = input.readLines().toFileStructure().sumOfSmall()

fun List<String>.toFileStructure(): Dir {
    val fileSystem = Dir(null, "/", mutableListOf())
    var currentDir: Dir? = fileSystem
    forEach {
        when {
            it.startsWith("$") -> {
                if (it == "$ cd /") currentDir = fileSystem
                else if (it == "$ cd ..") currentDir = currentDir?.parent
                else if (it.startsWith("$ cd ")) {
                    val dirName = it.removePrefix("$ cd ")
                    val childDir = currentDir?.children?.filterIsInstance<Dir>()?.firstOrNull { it.name == dirName } ?: Dir(currentDir, dirName, mutableListOf()).also { currentDir!!.children.add(it) }
                    currentDir = childDir
                } else if (it == "$ ls") Unit
                else TODO("Need to handle $it")
            }
            it.startsWith("dir ") -> Unit
            it.split(" ")[0].toIntOrNull() != null -> {
                val (size, name) = it.split(" ")
                currentDir!!.children.add(FileSystemThing.File(currentDir!!, size.toInt(), name))
            }
            else -> TODO("Need to handle: $it")
        }
    }
    return fileSystem
}

fun Dir.flattenToDir(): List<Dir> =
    children.filterIsInstance<Dir>().flatMap { it.flattenToDir() } + this

fun Dir.sumOfSmall(): String =
    flattenToDir().map { it.size() }.filter { it <= 100_000 }.sum().toString()

fun FileSystemThing.size(): Int = when (this) {
    is Dir -> children.sumOf { it.size() }
    is FileSystemThing.File -> this.size
}

sealed class FileSystemThing {
    abstract val parent: Dir?
    data class File(override val parent: Dir, val size: Int, val name: String) : FileSystemThing()
    data class Dir(override val parent: Dir?, val name: String, val children: MutableList<FileSystemThing>) : FileSystemThing()
}

fun runStep2(input: File): String = input.readLines().toFileStructure().smallestBigEnough()

fun Dir.smallestBigEnough(): String {
    val totalSize = 70_000_000
    val needed = 30_000_000
    val sizes = flattenToDir().map { it.size() }
    val usedSpace = size()
    val minToDelete = needed - (totalSize - usedSpace)
    return sizes.filter { it > minToDelete }.min().toString()
}
