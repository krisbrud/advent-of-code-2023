package day16

import println
import readInput

/*
- If the beam encounters empty space (.), it continues in the same direction.
- If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees depending on the angle of the mirror.
  For instance, a rightward-moving beam that encounters a / mirror would continue upward in the mirror's column,
  while a rightward-moving beam that encounters a \ mirror would continue downward from the mirror's column.
- If the beam encounters the pointy end of a splitter (| or -),
  the beam passes through the splitter as if the splitter were empty space.
  For instance, a rightward-moving beam that encounters a - splitter would continue in the same direction.
- If the beam encounters the flat side of a splitter (| or -),
  the beam is split into two beams going in each of the two directions the splitter's pointy ends are pointing.
  For instance, a rightward-moving beam that encounters a | splitter would split into two beams:
  one that continues upward from the splitter's column and one that continues downward from the splitter's column.
 */

fun Char.outputsTowards(fromDirection: Direction): List<Direction> = when (this) {
    '.' -> listOf(fromDirection.opposite()) // Continue in same dir (towards opposite of where it came from)
    '-' -> when (fromDirection) {
        Direction.Bottom, Direction.Top -> {
            // Flat side, return sides
            listOf(Direction.Right, Direction.Left)
        }

        Direction.Right, Direction.Left -> {
            // Towards is opposite of where it came from
            listOf(fromDirection.opposite())
        }
    }

    '|' -> when (fromDirection) {
        Direction.Right, Direction.Left -> {
            // Flat side
            listOf(Direction.Top, Direction.Bottom)
        }

        Direction.Top, Direction.Bottom -> {
            // Towards is opposite of where it came from
            listOf(fromDirection.opposite())
        }
    }

    '/' -> when (fromDirection) {
        Direction.Top -> listOf(Direction.Left)
        Direction.Left -> listOf(Direction.Top)
        Direction.Bottom -> listOf(Direction.Right)
        Direction.Right -> listOf(Direction.Bottom)
    }

    '\\' -> when (fromDirection) {
        Direction.Top -> listOf(Direction.Right)
        Direction.Right -> listOf(Direction.Top)
        Direction.Left -> listOf(Direction.Bottom)
        Direction.Bottom -> listOf(Direction.Left)
    }


    else -> throw RuntimeException("Unknown char '$this'")
}


enum class Direction {
    Bottom,
    Top,
    Left,
    Right;

    fun opposite(): Direction = when (this) {
        Bottom -> Top
        Top -> Bottom
        Left -> Right
        Right -> Left
    }
}

class ExploredFromDirections(
    var bottom: Boolean = false,
    var top: Boolean = false,
    var left: Boolean = false,
    var right: Boolean = false,
) {
    fun exploreInDirection(direction: Direction) = when (direction) {
        Direction.Bottom -> bottom = true
        Direction.Top -> top = true
        Direction.Left -> left = true
        Direction.Right -> right = true
    }

    fun isExplored(): Boolean = bottom or top or left or right
}

data class BeamToExplore(
    val row: Int,
    val col: Int,
    val towardsDirection: Direction,
)

typealias Contraption = List<String>

fun Contraption.rows(): Int = size
fun Contraption.cols(): Int = first().length

fun Contraption.beamToExploreTowardsDirectionOrNull(row: Int, col: Int, towards: Direction): BeamToExplore? = when (towards) {
    Direction.Right -> Pair(row, col + 1).takeIf { it.second < cols() }
    Direction.Left -> Pair(row, col - 1).takeIf { it.second >= 0 }
    Direction.Top -> Pair(row - 1, col).takeIf { it.first >= 0 }
    Direction.Bottom -> Pair(row + 1, col).takeIf { it.first < rows() }
}?.let { BeamToExplore(it.first, it.second, towards) }

fun main() {
    fun numEnergized(input: List<String>, firstBeam: BeamToExplore, ignoreFirstTile: Boolean = false): Int {
        // Possible solution:
        // While there are more unexplored beams:
        // Take an unexplored beam from the queue
        // Check if the beam is already in the space with the direction in question
        // If so, do not explore further
        // Otherwise, set it as explored with the direction
        // Find the beams the current beam lead to

        // After the queue is empty, return the size of the set of explored nodes
        val queue = ArrayDeque<BeamToExplore>().also {
            it.add(firstBeam)
        }

        val explored = input.map { line -> line.map { ExploredFromDirections() } }

        if (ignoreFirstTile) {
            // For part 2, the beam should not be reflected from the first tile
            val beamToExplore = queue.removeFirst()
            explored[beamToExplore.row][beamToExplore.col].exploreInDirection(beamToExplore.towardsDirection)
            queue.addLast(input.beamToExploreTowardsDirectionOrNull(
                row = beamToExplore.row,
                col = beamToExplore.col,
                towards = beamToExplore.towardsDirection,
            ) ?: throw RuntimeException("Failed in start of part 2!"))
        }

        while (queue.size > 0) {
            val beamToExplore = queue.removeFirst()

            val exploredInDirections = explored[beamToExplore.row][beamToExplore.col]
            when (beamToExplore.towardsDirection) {
                Direction.Top -> if (exploredInDirections.top) continue
                Direction.Bottom -> if (exploredInDirections.bottom) continue
                Direction.Right -> if (exploredInDirections.right) continue
                Direction.Left -> if (exploredInDirections.left) continue
            }

            exploredInDirections.exploreInDirection(beamToExplore.towardsDirection)

            val beamsToExploreLater = input[beamToExplore.row][beamToExplore.col]
                .outputsTowards(fromDirection = beamToExplore.towardsDirection.opposite())
                .mapNotNull { input.beamToExploreTowardsDirectionOrNull(beamToExplore.row, beamToExplore.col, towards = it) }
            beamsToExploreLater.forEach { queue.addLast(it) }
        }

        val exploredCount = explored.sumOf { line -> line.count { it.isExplored() } }

        return exploredCount
    }

    fun part1(input: List<String>): Int {
        return numEnergized(input, BeamToExplore(0, 0, Direction.Right)).also {
            println("part 1: $it")
        }
    }

    fun part2(input: List<String>): Int {
        val rowIndices = 0 until input.rows()
        val colIndices = 0 until input.cols()

        val topBeams = colIndices.map { BeamToExplore(0, it, Direction.Bottom) }
        val bottomBeams = colIndices.map { BeamToExplore(rowIndices.last, it, Direction.Top) }
        val leftBeams = rowIndices.map { BeamToExplore(it, 0, Direction.Right) }
        val rightBeams = rowIndices.map { BeamToExplore(it, colIndices.last, Direction.Left) }

        val edgeBeams = topBeams + bottomBeams + leftBeams + rightBeams
        val maxEnergized = edgeBeams.maxOf { numEnergized(input, it, true) }.also {
            println("Max energized $it")
        }

        return maxEnergized
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day16/Day16_test")
    check(part1(testInput) == 46)
//    check(part2(testInput) == 51)

    val input = readInput("day16/Day16")
    part1(input).println()
    part2(input).println()
}
