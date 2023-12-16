package day13

import println
import readInput

fun List<String>.transpose(): List<String> = first().indices.map { colIndex ->
    indices.map { rowIndex ->
        this[rowIndex][colIndex]
    }.joinToString("")
}

fun List<String>.reflectionOrNull(): Int? {
    val height = size
    val reflectAfterIndices = 0 until height - 1

    return reflectAfterIndices.firstOrNull { i ->
        val upIndices = i downTo 0
        val downIndices = i+1 until height
        println("height: $height")
        println("$upIndices, $downIndices")
        upIndices.zip(downIndices).all { (up, down) ->
            this[up] == this[down]
        }
    }
}

fun List<String>.reflectionValue(): Int {
    val cols = transpose().reflectionOrNull()
    val rows = reflectionOrNull()

    return rows?.let { (it + 1) * 100 } ?: cols?.let { it + 1 } ?: throw RuntimeException("No reflections found")
}


fun main() {
    fun part1(input: List<String>): Int {
        val patterns = input.joinToString("\n").split("\n\n").map { it.split("\n") }
        val reflectionValues = patterns.map { it.reflectionValue() }.also { it.println() }
        return reflectionValues.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 405)
//    check(part2(testInput) == 525152)

    val input = readInput("day13/Day13")
    part1(input).println()
    part2(input).println()
}
