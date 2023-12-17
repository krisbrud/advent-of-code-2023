package day13

import println
import readInput

fun List<String>.transpose(): List<String> = first().indices.map { colIndex ->
    indices.map { rowIndex ->
        this[rowIndex][colIndex]
    }.joinToString("")
}

fun Char.smudge(): Char = when (this) {
    '#' -> '.'
    '.' -> '#'
    else -> throw RuntimeException("Unknown char $this")
}

fun List<String>.smudged(): List<List<String>> = indices.flatMap { rowIndex ->
    first().indices.map { colIndex ->
        rowIndex to colIndex
    }
}.map { (i, j) ->
    val mutable = this.toMutableList().map { it.toCharArray() }
    mutable[i][j] = mutable[i][j].smudge()
    mutable.map(CharArray::concatToString).toList()
}

fun List<String>.reflectionOrNull(): Int? {
    val height = size
    val reflectAfterIndices = 0 until height - 1

    return reflectAfterIndices.firstOrNull { i ->
        val upIndices = i downTo 0
        val downIndices = i + 1 until height
//        println("height: $height")
//        println("$upIndices, $downIndices")
        upIndices.zip(downIndices).all { (up, down) ->
            this[up] == this[down]
        }
    }
}

fun List<String>.reflectionValue(): Int? {
    val cols = transpose().reflectionOrNull()
    val rows = reflectionOrNull()

    return rows?.let { (it + 1) * 100 } ?: cols?.let { it + 1 }
}


fun List<String>.allReflections(): List<Int> {
    val height = size
    val reflectAfterIndices = 0 until height - 1

    return reflectAfterIndices.filter { i ->
        val upIndices = i downTo 0
        val downIndices = i + 1 until height
//        println("height: $height")
//        println("$upIndices, $downIndices")
        upIndices.zip(downIndices).all { (up, down) ->
            this[up] == this[down]
        }
    }
}


fun List<String>.smudgedReflectionValue(originalValue: Int): Int? {
    val cols = transpose().allReflections().map { it + 1 }.firstOrNull { it != originalValue }
    val rows = allReflections().map { (it + 1) * 100 }.firstOrNull { it != originalValue }

    return rows ?: cols
}

fun main() {

    fun List<String>.parsePatterns(): List<List<String>> = this.joinToString("\n").split("\n\n").map { it.split("\n") }

    fun part1(input: List<String>): Int {
        val patterns = input.parsePatterns()
        val reflectionValues = patterns.mapNotNull { it.reflectionValue() }.also { it.println() }
        return reflectionValues.sum()
    }

    fun part2(input: List<String>): Int {
        val patterns = input.parsePatterns()
        val smudgedReflectionValues = patterns.map { pattern ->
            val originalValue = pattern.reflectionValue()!!
            val smudgedPatterns = pattern.smudged()
            val smudgedValues = smudgedPatterns.map { smudgedPattern ->
                smudgedPattern.smudgedReflectionValue(originalValue)
            }
//            if (smudgedValues.all { it == null }) {
//                println("Original value: $originalValue")
//                println("Original pattern:\n $pattern")
//                smudgedPatterns.forEachIndexed { i, p ->
//                    println("Smudged pattern $i:\n $p")
//                }
//                smudgedValues.forEachIndexed{ i, v ->
//                    println("Smudged value $i:\n $v")
//                }
//            }
            smudgedValues.firstNotNullOf { it }
        }

        return smudgedReflectionValues.sum().also { println("Smudged sum $it") }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
//    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("day13/Day13")
//    part1(input).println()
    part2(input).println()
}
