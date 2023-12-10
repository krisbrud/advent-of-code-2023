package day10.part2

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
    DownRight,
    VirtualGround;  // Virtual tile that is between the real tiles. Used to find out if a tile is reachable from the outside

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

data class Coordinate(val row: Int, val col: Int) {
    fun isVirtual(): Boolean {
        val isBetweenCols = row % 2 != 0
        val isBetweenRows = col % 2 != 0
        return isBetweenRows || isBetweenCols
    }
}

data class Node(
    val coordinate: Coordinate,
    val tile: Tile,
//    val isVirtual: Boolean,
    var prev: Node? = null,
) {
    fun visited(): Boolean = prev != null

    fun visit(from: Node) {
        if (prev != null) return
        prev = from
    }

    fun isStart(): Boolean = tile == Tile.Start
}

fun Int.average(other: Int): Int = (this + other) / 2

class Graph(
    val cols: Int,
    val rows: Int,
    val nodes: Map<Coordinate, Node>,
) {
    val edges: Map<Coordinate, List<Coordinate>> by lazy {
        nodes.toList().associate { (coordinate, node) ->
            val connectedNodes = node.connectedNodes()
            (coordinate to connectedNodes.map(Node::coordinate))
        }

    }

    /**
     * Get the adjacent coordinates within bounds without regards to if they are connected or not.
     */
    private fun Coordinate.adjacent(): List<Pair<Direction, Coordinate>> {
        val maybeUp = (row - 2).takeIf { it >= 0 }?.let { Pair(Direction.Up, Coordinate(it, col)) }
        val maybeDown = (row + 2).takeIf { it <= rows }?.let { Pair(Direction.Down, Coordinate(it, col)) }
        val maybeLeft = (col - 2).takeIf { it >= 0 }?.let { Pair(Direction.Left, Coordinate(row, it)) }
        val maybeRight = (col + 2).takeIf { it <= cols }?.let { Pair(Direction.Right, Coordinate(row, it)) }
        return listOfNotNull(maybeUp, maybeDown, maybeLeft, maybeRight)
    }

    private fun Coordinate.virtualAdjacent(): List<Pair<Direction, Coordinate>> {
        val maybeUp = (row - 1).takeIf { it >= -1 }?.let { Pair(Direction.Up, Coordinate(it, col)) }
        val maybeDown = (row + 1).takeIf { it <= rows + 1 }?.let { Pair(Direction.Down, Coordinate(it, col)) }
        val maybeLeft = (col - 1).takeIf { it >= -1 }?.let { Pair(Direction.Left, Coordinate(row, it)) }
        val maybeRight = (col + 1).takeIf { it <= cols + 1 }?.let { Pair(Direction.Right, Coordinate(row, it)) }
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

    private fun virtualEdgeCoordinates(): Set<Coordinate> {
        // Note: Virtual edge coordinates have values that are the _average_ of the source and destination of real edges
        return edges.toList()
            .map { (source, destinations) ->
                destinations.map { destination -> Pair(source, destination) }
            }.flatten().map { (source, destination) ->
                Coordinate(
                    row = source.row.average(destination.row),
                    col = source.col.average(destination.col),
                )
            }.toSet()
    }

    private fun allVirtualCoordinates(): Set<Coordinate> {
        return (-1..(rows + 1)).zip(-1..(cols + 1)).map { (row, col) ->
            Coordinate(row = row, col = col)
        }.filter(Coordinate::isVirtual).toSet()
    }

    /**
     * Make an augmented graph by creating virtual space between tiles.
     */
    fun augmented(): Graph {
        val allVirtualCoords = allVirtualCoordinates()
        val virtualEdgeCoords = virtualEdgeCoordinates()
        val nonEdgeVirtualCoords = allVirtualCoords - virtualEdgeCoords

//        val virtualEdgeRoot = Node(Coordinate(-5,-5), Tile.VirtualGround, isVirtual = true)
        val virtualEdgeRoot = Node(Coordinate(-5, -5), Tile.VirtualGround)
//        virtualEdgeRoot.prev = virtualEdgeRoot // Not completely sure if this is needed

        val virtualEdgeNodes = virtualEdgeCoords.map { Node(Coordinate(row = it.row, col = it.col), Tile.VirtualGround) }.also {
            it.forEach { node ->
                // By setting the virtual edge root as prev, this node will not be explored when we do a second BFS
                // pass from outside the loop
                node.prev = virtualEdgeRoot
            }
        }.associateBy(Node::coordinate)
        val virtualNonEdgeNodes = nonEdgeVirtualCoords.map {
            Node(Coordinate(row = it.row, col = it.col), Tile.VirtualGround)
        }.associateBy(Node::coordinate)

        return Graph(rows = rows, cols = cols, nodes = (nodes + virtualEdgeNodes + virtualNonEdgeNodes))
    }

    fun traverse() {
        val startNode = nodes.values.first { it.tile == Tile.Start }.also {
            it.prev = it // Mark it as visited
        }

        // Traverse the graph
        val nodesToExplore = ArrayDeque(listOf(startNode))
        while (nodesToExplore.size > 0) {
            val node = nodesToExplore.removeFirst()
            val nodeEdges = edges[node.coordinate]
            nodeEdges?.forEach {
                val edgeDestination = nodes[it]!!
                if (!edgeDestination.visited() && !edgeDestination.isStart()) // First node has self as previous
                {
                    edgeDestination.visit(from = node)
                    nodesToExplore.addLast(edgeDestination)
                }
            }
        }
    }

//    fun virtualTraverse() {
//        val startNode = nodes.find
//
//        // Traverse the graph
//        val nodesToExplore = ArrayDeque(listOf(startNode))
//        while (nodesToExplore.size > 0) {
//            val node = nodesToExplore.removeFirst()
//            val nodeEdges = edges[node.coordinate]
//            nodeEdges?.forEach {
//                val edgeDestination = nodes[it]!!
//                if (!edgeDestination.visited() && !edgeDestination.isStart()) // First node has self as previous
//                {
//                    edgeDestination.visit(from = node)
//                    nodesToExplore.addLast(edgeDestination)
//                }
//            }
//        }
//    }

    companion object {
        fun parse(input: List<String>): Graph {
            val cols = input.first().length * 2
            val rows = input.size * 2

            val nodes = input.mapIndexed { rowIndex, line ->
                line.mapIndexed { colIndex, char ->
                    val coordinate = Coordinate(row = rowIndex * 2, col = colIndex * 2)
                    val tile = Tile.fromChar(char)
                    Node(coordinate, tile, false)
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
    fun part2(input: List<String>): Int {
        val graph = Graph.parse(input)
        graph.traverse() // This gives a non-null prev-value to all non-virtual nodes in the loop


        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("day10/part2/Day10_test1")
    val testInput2 = readInput("day10/part2/Day10_test2")
    check(part2(testInput1) == 2)
    check(part2(testInput2) == 2)

    val input = readInput("day10/part2/Day10")
    part2(input).println()
}
