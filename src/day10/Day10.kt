package day10

import println
import readInput

enum class Direction {
    Up, Down, Left, Right
}

enum class Tile {
    Start,
    Ground,
    Vertical,
    Horizontal,
    UpLeft,
    UpRight,
    DownLeft,
    DownRight;

    fun hasLeft() = when (this) {
        Start, Horizontal, UpLeft, DownLeft -> true
        else -> false
    }

    fun hasRight() = when (this) {
        Start, Horizontal, UpRight, DownRight -> true
        else -> false
    }

    fun hasUp() = when (this) {
        Start, Vertical, UpLeft, UpRight -> true
        else -> false
    }

    fun hasDown() = when (this) {
        Start, Vertical, DownLeft, DownRight -> true
        else -> false
    }

    fun hasConnection(other: Tile, direction: Direction) = when (direction) {
        Direction.Up -> hasUp() && other.hasDown()
        Direction.Down -> hasDown() && other.hasUp()
        Direction.Left -> hasLeft() && other.hasRight()
        Direction.Right -> hasRight() && other.hasLeft()
    }

    companion object {
        fun fromChar(char: Char): Tile = when (char) {
            'S' -> Start
            '.' -> Ground
            '|' -> Vertical
            '-' -> Horizontal
            'J' -> UpLeft
            'L' -> UpRight
            '7' -> DownLeft
            'F' -> DownRight
            else -> throw RuntimeException("Error parsing char $char")
        }
    }
}

data class Coordinate(val row: Int, val col: Int)

data class Node(
    val coordinate: Coordinate,
    val tile: Tile,
    var prev: Node? = null,
    var stepsFromStart: Int? = null
) {
    fun visited(): Boolean = prev != null

    fun visit(from: Node) {
        if (prev != null) return

        val prevSteps = from.stepsFromStart ?: 0
        stepsFromStart = prevSteps + 1

        prev = from
    }

    fun isStart(): Boolean = tile == Tile.Start

//    fun stepsFromStart(): Int = when {
//        isStart() -> 0
//        prev != null -> 1 + prev!!.stepsFromStart()
//        else -> Int.MIN_VALUE // Since we are trying to find the node with the max steps from the start node
//    }
}

class Graph(
    val cols: Int,
    val rows: Int,
    val nodes: Map<Coordinate, Node>,
) {
    // Key is node, value is a list of its edges
    val edges: Map<Coordinate, List<Node>> by lazy {
        nodes.toList().associate { (coordinate, node) ->
            val connectedNodes = node.connectedNodes()
            (coordinate to connectedNodes)
        }

    }

    /**
     * Get the adjacent coordinates within bounds without regards to if they are connected or not.
     */
    private fun Coordinate.adjacent(): List<Pair<Direction, Coordinate>> {
        val maybeUp = (row - 1).takeIf { it >= 0 }?.let { Pair(Direction.Up, Coordinate(it, col)) }
        val maybeDown = (row + 1).takeIf { it <= rows }?.let { Pair(Direction.Down, Coordinate(it, col)) }
        val maybeLeft = (col - 1).takeIf { it >= 0 }?.let { Pair(Direction.Left, Coordinate(row, it)) }
        val maybeRight = (col + 1).takeIf { it <= cols }?.let { Pair(Direction.Right, Coordinate(row, it)) }
        return listOfNotNull(maybeUp, maybeDown, maybeLeft, maybeRight)
    }

    private fun Node.connectedNodes(): List<Node> {
        val adjacent = coordinate.adjacent()
        val connectedNodes = adjacent.filter { (direction, otherCoordinate) ->
            val otherNode = nodes[otherCoordinate]
            otherNode?.let { tile.hasConnection(it.tile, direction) } ?: false
        }.mapNotNull { nodes[it.second] }
        return connectedNodes
    }

    companion object {
        fun parse(input: List<String>): Graph {
            val cols = input.first().length
            val rows = input.size

            val nodes = input.mapIndexed { rowIndex, line ->
                line.mapIndexed { colIndex, char ->
                    val coordinate = Coordinate(row = rowIndex, col = colIndex)
                    val tile = Tile.fromChar(char)
                    Node(coordinate, tile)
                }
            }.flatten().associateBy(Node::coordinate)

            return Graph(
                nodes = nodes,
                rows = rows,
                cols = cols,
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        // TODO: Make node class that can be queried for which directions it can be connected to from
        // TODO: Make a graph out of the possible connections in the node
        // TODO: Traverse the graph using BFS
        val graph = Graph.parse(input)
        val startNode = graph.nodes.values.first { it.tile == Tile.Start }.also {
            it.prev = it // Mark it as visited
        }
        startNode.stepsFromStart = 0

        // Traverse the graph
        val nodesToExplore = ArrayDeque(listOf(startNode))
        while (nodesToExplore.size > 0) {
            val node = nodesToExplore.removeFirst()
            val nodeEdges = graph.edges[node.coordinate]
            nodeEdges?.forEach {
                if (!it.visited() && !it.isStart()) // First node has self as previous
                {
                    it.visit(from = node)
                    nodesToExplore.addLast(it)
                }
            }
        }
        val stepsFromStart = graph.nodes.maxOf {
            it.value.stepsFromStart ?: 0
        }.also { it.println() }
        return stepsFromStart
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("day10/Day10_test1")
    val testInput2 = readInput("day10/Day10_test2")
    check(part1(testInput1) == 4)
    check(part1(testInput2) == 8)
//    check(part2(testInput) == 2)

    val input = readInput("day10/Day10")
    part1(input).println()
    part2(input).println()
}
