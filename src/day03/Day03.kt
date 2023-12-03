package day03

import println
import readInput

val NUMBERS = (0..9).map(Int::toString).toSet()
val NON_SYMBOLS = NUMBERS + "."
fun String.isSymbol() = !NON_SYMBOLS.contains(this)

data class Tile(
    val row: Int,
    val col: Int,
    val value: String,
) {
    val isSymbol = value.isSymbol()
}

fun main() {
    fun part1(input: List<String>): Int {
        val tiles = input.mapIndexed { rowIndex, line ->
            line.mapIndexed { colIndex, char ->
                Tile(row=rowIndex, col=colIndex, value = char.toString())
            }
        }
        println("Tiles: $tiles")
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
//    check(part1(testInput) == 4361)
//    check(part2(testInput) == 2286)

    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()
}
