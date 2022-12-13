package day13

import day13.Packet.List
import day13.Packet.Number
import java.io.File
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

const val workingDir = "src/day13"

fun main() {
    val sample = File("$workingDir/sample.txt")
    val input1 = File("$workingDir/input_1.txt")
    println("Step 1a: ${runStep1(sample)}")
    println("Step 1b: ${runStep1(input1)}")
    println("Step 2a: ${runStep2(sample)}")
    println("Step 2b: ${runStep2(input1)}")
}

sealed class Packet {
    data class List(val contents: MutableList<Packet>) : Packet()
    data class Number(val num: Int) : Packet()
}
typealias PacketPair = Pair<Packet, Packet>

operator fun Packet.compareTo(other: Packet): Int {
    if (this is Number && other is Number) return this.num.compareTo(other.num)
    if (this is List && other is List) {
        (0 until maxOf(this.contents.size, other.contents.size)).forEach {
            try {
                if (this.contents[it] < other.contents[it]) return -1
                if (this.contents[it] > other.contents[it]) return 1
            } catch (e: IndexOutOfBoundsException) {
                if(this.contents.size < other.contents.size) return -1
                if(this.contents.size > other.contents.size) return 1
            }
        }
        return 0
    }
    if (this is Number) return List(arrayListOf(this)).compareTo(other)
    if (other is Number) return this.compareTo(List(arrayListOf(other)))
    TODO()
}

val json by lazy { Json }

fun JsonArray.toPacket(): Packet = List(map {
    try {
        it.jsonArray.toPacket()
    } catch (e: IllegalArgumentException) {
        Number(it.jsonPrimitive.int)
    }
}.toMutableList())

fun String.toPacket(): Packet = json.decodeFromString<JsonArray>(this).toPacket()

fun runStep1(input: File): String {
    val pairs = ArrayList<PacketPair>()
    input.readText().split("\n\n").forEach {
        val (p1, p2) = it.split("\n")
        pairs.add(Pair(p1.toPacket(), p2.toPacket()))
    }

    return pairs.mapIndexed { index, pair -> if (pair.first < pair.second) index+1 else -1 }.filter { it != -1 }.sum().toString()
}

fun runStep2(input: File): String {
    val decoderPackets = listOf("[[2]]", "[[6]]").map { it.toPacket() }
    return input.readLines()
        .filter { it.isNotBlank() }
        .map { it.toPacket() }
        .let { it + decoderPackets }
        .sortedWith { o1, o2 -> o1.compareTo(o2) }
        .let {  list ->
            decoderPackets.map { list.indexOf(it)+1 }.fold(1) { acc, a, -> acc * a }
        }.toString()
}
