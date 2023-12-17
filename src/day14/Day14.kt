package day14

import println
import readInput
import kotlin.time.measureTime

fun Char.isRoundedRock(): Boolean = this == 'O'
fun Char.isSupportBeam(): Boolean = this == '#'

fun List<String>.transpose(): List<String> = first().indices.map { colIndex ->
    indices.map { rowIndex ->
        this[rowIndex][colIndex]
    }.joinToString("")
}

fun List<String>.rotateRight(): List<String> = transpose().map(String::reversed)
fun List<String>.rotateLeft(): List<String> = map(String::reversed).transpose()

fun List<String>.tiltRocksNorth(): List<String> {
    // Pseudocode:
    // - transpose
    // - split lines into lists of strings containing only rocks and empty or support beams
    // - split into char arrays, sort descending, join back to strings (doesn't matter if we sort CharArrays of only support beams)
    // - join to string
    // - transpose back
    return transpose().map { line ->
        line.fold(mutableListOf(mutableListOf<Char>())) { acc, c ->
            if (c.isSupportBeam() || acc.last().all { it.isSupportBeam() }) {
                acc.add(mutableListOf(c))
            } else {
                acc.last().add(c)
            }
            acc
        }.map { list -> list.sortedDescending().joinToString("") }.joinToString("")
    }.transpose()
}

fun List<String>.tiltRocksLeft(): List<String> {
    // Pseudocode:
    // - split lines into lists of strings containing only rocks and empty or support beams
    // - split into char arrays, sort descending, join back to strings (doesn't matter if we sort CharArrays of only support beams)
    // - join to string
    return map { line ->
        line.fold(mutableListOf(mutableListOf<Char>())) { acc, c ->
            if (c.isSupportBeam() || acc.last().all { it.isSupportBeam() }) {
                acc.add(mutableListOf(c))
            } else {
                acc.last().add(c)
            }
            acc
        }.map { list -> list.sortedDescending().joinToString("") }.joinToString("")
    }
}

fun List<String>.cycle(): List<String> {
    return rotateLeft().tiltRocksLeft() // North
        .rotateRight().tiltRocksLeft() // West
        .rotateRight().tiltRocksLeft() // South
        .rotateRight().tiltRocksLeft() // East
        .rotateRight().rotateRight()
}

fun List<String>.calculateLoad(): Int = withIndex().sumOf { indexedLine ->
    val numRoundedRocksInLine = indexedLine.value.count { it.isRoundedRock() }
    val load = numRoundedRocksInLine * (size - indexedLine.index)
    load
}

fun main() {
    fun part1(input: List<String>): Int {
        val tiltedNorth = input.tiltRocksNorth()
        val load = tiltedNorth.calculateLoad()
        return load
    }

    fun part2(input: List<String>): Int {
        val testIterations = 1000
        val duration = measureTime {
            var tmp = input
            var i = 0

            while (i < testIterations) {
                tmp = tmp.cycle()
                i += 1
            }
        }
        println("Duration: $duration")
        val durationPerCycle = duration / testIterations
        println("Duration per cycle: $durationPerCycle")

        val totalIterations = 1000000000
        val estimatedTotalDuration = durationPerCycle * 1000000000
        println("Estimated total duration: $estimatedTotalDuration")

        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
//    check(part1(testInput) == 136)
//    check(part2(testInput) == 400)

    val input = readInput("day14/Day14")
    part1(input).println()
    part2(input).println()
}
