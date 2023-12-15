package day11.part2

import println
import readInput
import kotlin.math.abs

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

fun Universe.emptyRowsBetween(from: Coordinate, to: Coordinate): Long = when {
    from.row == to.row -> 0
    from.row < to.row -> this.slice(from.row.toInt() + 1 until to.row.toInt()).count { row -> row.all { it.isEmptySpace() } }.toLong()
    else -> emptyRowsBetween(from=to, to=from)
}

fun Universe.emptyColsBetween(from: Coordinate, to: Coordinate) = transpose().emptyRowsBetween(from=from.transpose(), to=to.transpose())


data class Coordinate(val row: Long, val col: Long) {
    operator fun minus(other: Coordinate) = Coordinate(row = row - other.row, col = col - other.col)
    operator fun plus(other: Coordinate) = Coordinate(row = row + other.row, col = col + other.col)

    fun abs() = Coordinate(row=abs(row), col=abs(col))

    fun manhattanDistanceFromOrigin(): Long {
        return abs(row) + abs(col)
    }
}
fun Coordinate.transpose() = Coordinate(row=col, col=row)

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
    fun part2(input: List<String>, multiplier: Long): Long {
        val universe = input

        val galaxyCoordinates = universe.mapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, space ->
                Coordinate(row = rowIndex.toLong(), col = colIndex.toLong()).takeIf { space.isGalaxy() }
            }
        }.flatten()

        val galaxyCoordinatePairs = galaxyCoordinates.mapIndexed { i, coord1 ->
            val indices = (0 until i) //  + (i + 1 until galaxyCoordinates.size)
            galaxyCoordinates.slice(indices).map { coord2 ->
                Pair(coord1, coord2)
            }
        }.flatten()

        galaxyCoordinatePairs.forEach { (from, to) ->
            require(from != to)
        }

//        var i = 0
        val distanceSum = galaxyCoordinatePairs.sumOf { (from, to) ->
//            i += 1
//            if (i % 100 == 0) i.println()
            
            val emptyRowsBetween = universe.emptyRowsBetween(from, to)
            val emptyColsBetween = universe.emptyColsBetween(from, to)

            val expandedRowsBetween = emptyRowsBetween * multiplier - emptyRowsBetween
            val expandedColsBetween = emptyColsBetween * multiplier - emptyColsBetween

            val betweenCoordinate = Coordinate(row = expandedRowsBetween, col = expandedColsBetween)
            val diff = betweenCoordinate + (to - from).abs()
            val distance = diff.manhattanDistanceFromOrigin()
            distance
        }

        return distanceSum
    }



    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/part2/Day11_test")
    check(part2(testInput, 10L) == 1030L)
    check(part2(testInput, 100L) == 8410L)

    val input = readInput("day11/part2/Day11")
    part2(input, 1_000_000L).println()
}
