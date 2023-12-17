package day14

import println
import readInput

fun Char.isRoundedRock(): Boolean = this == 'O'
fun Char.isSupportBeam(): Boolean = this == '#'

fun List<String>.transpose(): List<String> = first().indices.map { colIndex ->
    indices.map { rowIndex ->
        this[rowIndex][colIndex]
    }.joinToString("")
}

fun List<String>.tiltRocksNorth(): List<String> {
    // Pseudocode:
    // - transpose
    // - split lines into lists of strings containing only rocks and empty or support beams
    // - split into char arrays, sort descending, join back to strings (doesn't matter if we sort CharArrays of only support beams)
    // - join to string
    // - transpose back
    val transposed = transpose()
    val sorted = transposed.map { line ->
        line.fold(mutableListOf(mutableListOf<Char>())) { acc, c ->
            if (c.isSupportBeam() || acc.last().all { it.isSupportBeam() }) {
                acc.add(mutableListOf(c))
            } else {
                acc.last().add(c)
            }
            acc
        }.map { list -> list.sortedDescending().joinToString("") }.joinToString("")
    }
    val result = sorted.transpose()
    return result
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
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 136)
//    check(part2(testInput) == 400)

    val input = readInput("day14/Day14")
    part1(input).println()
//    part2(input).println()
}
