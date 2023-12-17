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
        val seenStates = mutableSetOf<String>()
        val totalIterations = 1000000000L

        var tmp = input
        var i = 0
        var firstCycle: String? = null
        var firstStateRepetitionIteration: Long? = null
        var secondStateRepetitionIteration: Long? = null

        // Dirty (but decently performant) solution - find the first and second time the cycle repeats (i.e. the second and third time they occur)
        // and use some simple modulo math to iterate just the needed iterations within the cycle after the second
        // occurence for the result to be equivalent to doing all the iterations
        while (i < 10000) {
            val stringState = tmp.joinToString("")
            if (seenStates.contains(stringState)) {
                if (firstCycle == null) {
                    firstCycle = stringState
                    firstStateRepetitionIteration = i.toLong()
                    println("First cycle at iteration $i")
                } else if (stringState == firstCycle) {
                    println("Further hit of cycle at iteration $i")
                    secondStateRepetitionIteration = i.toLong()
                    break
                }
            }
            seenStates.add(stringState)
            tmp = tmp.cycle()
            i += 1
        }
        val cycleLength = secondStateRepetitionIteration!! - firstStateRepetitionIteration!!
        val remainingIterations = (totalIterations - i) % cycleLength
        for (j in 0 until remainingIterations.toInt()) {
            tmp = tmp.cycle()
        }

        val load = tmp.calculateLoad()

        return load
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("day14/Day14")
    part1(input).println()
    part2(input).println()
}
