package day17
//
//import println
//import readInput
//import java.util.*
//
//enum class Direction {
//    Up,
//    Down,
//    Left,
//    Right;
//
//    fun opposite(): Direction = when (this) {
//        Up -> Down
//        Down -> Up
//        Left -> Right
//        Right -> Left
//    }
//
//    fun sides(): List<Direction> = when (this) {
//        Up -> listOf(Left, Right)
//        Down -> listOf(Left, Right)
//        Left -> listOf(Up, Down)
//        Right -> listOf(Up, Down)
//    }
//}
//
//data class Coordinate(val row: Int, val col: Int)
//
//data class Node(
//    val coordinate: Coordinate,
//    // Direction the previous step took and how many consecutive steps so far
//    val consecutiveSteps: Int,
//    val direction: Direction,
//) {
////    fun key(): String = "$coordinate;$consecutiveSteps;$direction"
//}
//
//typealias Board = List<String>
//
//fun Board.rows(): Int = size
//fun Board.cols(): Int = first().length
//
//fun Board.isCoordinateInbounds(coordinate: Coordinate): Boolean {
//    return 0 <= coordinate.row && coordinate.row < rows() && 0 <= coordinate.col && coordinate.col < cols()
//}
//
//fun Board.inboundsCoordinateInDirectionOrNull(coordinate: Coordinate, direction: Direction): Coordinate? = when (direction) {
//    Direction.Up -> coordinate.copy(row = coordinate.row - 1)
//    Direction.Down -> coordinate.copy(row = coordinate.row + 1)
//    Direction.Left -> coordinate.copy(col = coordinate.col - 1)
//    Direction.Right -> coordinate.copy(col = coordinate.col + 1)
//}.takeIf(::isCoordinateInbounds)
//
//fun Board.adjacent(node: Node): List<Node> = if (node.consecutiveSteps < 3) {
//    node.direction.sides() + node.direction
//} else {
//    node.direction.sides()
//}.mapNotNull { direction ->
//    inboundsCoordinateInDirectionOrNull(node.coordinate, direction)?.let { coordinate ->
//        direction to coordinate
//    }
//}.map { (direction, coord) ->
//    val prev = node
//    Node(coordinate = coord, consecutiveSteps = prev.consecutiveSteps + 1, direction)
//}
//
//fun Board.isGoalNode(node: Node): Boolean = (node.coordinate.row == (rows() - 1)) && (node.coordinate.col == (cols() - 1))
//
//
//fun Board.costAtCoordinate(coordinate: Coordinate) = this[coordinate.row][coordinate.col].digitToInt()
//
//data class NodeWithCost(
//    val node: Node,
//    val cost: Int,
//)
//
//
//fun main() {
//    fun shortestPathLength(board: Board): Int {
//        val queue = PriorityQueue<NodeWithCost>(compareBy { -it.cost })
//
//        val costs = mutableMapOf<Node, Int>()
//
//        while (queue.isNotEmpty()) {
//            val nodeWithCost = queue.remove()
//            val node = nodeWithCost.node
//            val cost = nodeWithCost.cost
//
//            board.adjacent(node).forEach { adjacent ->
//                val adjCost = cost + board.costAtCoordinate(adjacent)
//                val candidate =
//                    if (adjCost < costs.getOrDefault(adja))
//            }
//        }
//
//
//        return 0
//    }
//
//    fun part1(input: List<String>): Int {
//        val shortestLength = shortestPathLength(input).also {
//            println("Shortest path length $it")
//        }
//        return shortestLength
//    }
//
//    fun part2(input: List<String>): Int {
//        return input.size
//    }
//
//    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("day17/Day17_test")
//    check(part1(testInput) == 102)
////    check(part2(testInput) == 51)
//
//    val input = readInput("day17/Day17")
//    part1(input).println()
//    part2(input).println()
//}
