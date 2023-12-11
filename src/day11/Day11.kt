package day11

import println
import readInput

typealias Space = Char

fun Space.isEmptySpace() = this == '.'
fun Space.isGalaxy() = this == '#'

typealias Universe = List<String>

fun Universe.height() = size
fun Universe.width() = first().length

fun Universe.transpose(): Universe =
    (0 until width()).map { colIndex ->
        (0 until height()).map { rowIndex ->
            this[rowIndex][colIndex]
        }.joinToString("")
    }

fun Universe.emptyRowsDuplicated(): Universe = this.fold(listOf()) { acc, line ->
    if (line.all(Space::isEmptySpace)) {
        acc + line + line
    } else {
        acc + line
    }
}

fun Universe.expandEmptySpace(): Universe = emptyRowsDuplicated() // Expand rows
    .transpose().emptyRowsDuplicated().transpose() // Transpose, expand what was originally columns, transpose back

data class Coordinate(val row: Int, val col: Int)

fun Universe.isWithinBounds(coordinate: Coordinate): Boolean = (0 <= coordinate.row) && (coordinate.row < height()) &&
    (0 <= coordinate.col) && (coordinate.col < width())

fun Universe.adjacentOf(coordinate: Coordinate): Set<Coordinate> = listOf(
    Coordinate(coordinate.row - 1, coordinate.col),
    Coordinate(coordinate.row + 1, coordinate.col),
    Coordinate(coordinate.row, coordinate.col - 1),
    Coordinate(coordinate.row, coordinate.col + 1)
).filter { isWithinBounds(it) }.toSet()

fun Universe.shortestPathLength(from: Coordinate, to: Coordinate): Int {
    require(isWithinBounds(from)) { "invalid from coordinate $from" }
    require(isWithinBounds(to)) { "invalid to coordinate $to" }

    val queue = ArrayDeque<Coordinate>()
        .also { it.add(from) }
    val distances = mutableMapOf<Coordinate, Int>() // Key is coordinate, value is distance from start
        .also { it[from] = 0 }

    var distance = 0
    while (queue.isNotEmpty()) {
        distance += 1
        val node = queue.removeFirst()
        if (node == to) return distances.getValue(node)

        val adjacentNodes = adjacentOf(node)
        adjacentNodes.forEach { adjacentNode ->
            if (!distances.contains(adjacentNode)) {
                distances[adjacentNode] = distances.getValue(node) + 1
                queue.addLast(adjacentNode)
            }
        }
    }

    throw RuntimeException("Could not find path!")
}

fun main() {
    fun part1(input: List<String>): Int {
        val universe = input
        val expandedUniverse = universe.expandEmptySpace()

        val galaxyCoordinates = expandedUniverse.mapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, space ->
                Coordinate(row = rowIndex, col = colIndex).takeIf { space.isGalaxy() }
            }
        }.flatten()

        val galaxyCoordinatePairs = galaxyCoordinates.mapIndexed { i, coord1 ->
            val indices = (0 until i) //  + (i + 1 until galaxyCoordinates.size)
            galaxyCoordinates.slice(indices).map { coord2 ->
                Pair(coord1, coord2)
            }
        }.flatten().also {
//            it.size.println()
        }

        galaxyCoordinatePairs.forEach { (from, to) ->
            require(from != to)
        }

        var i = 0
        val distanceSum = galaxyCoordinatePairs.sumOf { (from, to) ->
            i += 1
            if (i % 100 == 0) i.println()
            expandedUniverse.shortestPathLength(from, to)
        }

        return distanceSum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 374)
//    check(part2(testInput) == 2)

    val input = readInput("day11/Day11")
    part1(input).println()
    part2(input).println()
}
