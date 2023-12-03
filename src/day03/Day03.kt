package day03

import println
import readInput
import kotlin.math.max
import kotlin.math.min

val NUMBERS = (0..9).map(Int::toString).toSet()
val NON_SYMBOLS = NUMBERS + "."
fun String.isDigit() = NUMBERS.contains(this)
fun String.isSymbol() = !NON_SYMBOLS.contains(this)

data class Coordinate(val row: Int, val col: Int)

data class Tile(
    val coordinate: Coordinate,
    val value: String,
) {
    val isNumber = value.isDigit()
    val isSymbol = value.isSymbol()
}

data class Number(
    val value: Int,
    val firstCoord: Coordinate,
) {
    private val length = value.toString().length
    val indices by lazy {
        val colIndices = firstCoord.col until (firstCoord.col + length)
        colIndices.map { Coordinate(firstCoord.row, it) }
    }
}

fun main() {
    val NUMBER_PATTERN = """\d+""".toRegex()
    fun part1(input: List<String>): Int {
        val numRows = input.size
        val numCols = input.first().length

        val adjacentIndicesWithinBounds = { coord: Coordinate ->
            val rowIndices = (max(coord.row - 1, 0)..min(coord.row + 1, numRows - 1)).toList()
            val colIndices = (max(coord.col - 1, 0)..min(coord.col + 1, numCols - 1)).toList()
            rowIndices.map { rowIdx ->
                colIndices.map { colIdx ->
                    Coordinate(rowIdx, colIdx)
                }
            }.flatten()
        }

        val tiles = input.mapIndexed { rowIndex, line ->
            line.mapIndexed { colIndex, char ->
                Tile(Coordinate(row = rowIndex, col = colIndex), value = char.toString())
            }
        }
        val symbolTileSet = tiles.flatten().filter(Tile::isSymbol).map(Tile::coordinate).toSet()
        val validNumbers = input.mapIndexed { rowIdx, line ->
            val allNumberMatches = NUMBER_PATTERN.findAll(line).toList().map { it.groups }.flatten().filterNotNull()
            allNumberMatches.filter { numberMatch ->
                val numberCoords = numberMatch.range.map { colIdx -> Coordinate(rowIdx, colIdx) }
                val adjacentCoords = numberCoords.map { adjacentIndicesWithinBounds(it) }.flatten()
                val hasAdjacentSymbol = adjacentCoords.any { symbolTileSet.contains(it) }
                hasAdjacentSymbol
            }
        }.flatten().map { it.value.toInt() }

        return validNumbers.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 4361)
//    check(part2(testInput) == 2286)

    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()
}
