package day08

import println
import readInput
import kotlin.math.max

data class Node(
    val id: String,
    val left: String,
    val right: String,
) {
    companion object {
        // AAA = (BBB, CCC)
        fun fromLine(line: String): Node {
            val id = line.slice(0..2)
            val left = line.slice(7..9)
            val right = line.slice(12..14)
            return Node(id = id, left = left, right = right)
        }
    }

    fun isGhostGoal(): Boolean = id[2] == 'Z'
}

class Graph private constructor(private val nodes: Map<String, Node>) {
    fun nodeOfId(id: String): Node = nodes[id]!!

    companion object {
        fun fromNodeList(nodeList: List<Node>): Graph = Graph(nodeList.associateBy { it.id })
    }
}

fun Int.firstFactor(): Int? = when {
    (this <= 1) -> null
//    (this == 1) -> 1
    else -> (2..this).firstOrNull { (this % it) == 0 }
}

fun Int.factorize(): List<Int> {
    val factors = mutableListOf<Int>()
    var rest = this
    while ( rest.firstFactor() != rest && rest.firstFactor() != null) {
        val firstFactor = rest.firstFactor()!!
        rest /= firstFactor
        factors.add(firstFactor)
    }
    factors.add(rest)
    return factors.toList()
}

// factor is key, count is value
fun Int.factorsWithCount(): Map<Int, Int> = factorize().groupingBy { it }.eachCount().toMap()

fun main() {
    val start = "AAA"
    val goal = "ZZZ"

    fun part1(input: List<String>): Int {
        val instructions = input.first().trim()

        val nodes = input.drop(2).map(Node::fromLine)
        val graph = Graph.fromNodeList(nodes)

        var i = 0
        var current = graph.nodeOfId(start)

        while (current.id != goal) {
            val instruction = instructions[i % instructions.length]
            val nextId = when (instruction) {
                'L' -> current.left
                'R' -> current.right
                else -> throw RuntimeException("uh oh: $instruction")
            }
            current = graph.nodeOfId(nextId)
            i++
//            println("i: $i")
        }

        return i
    }

    fun List<Node>.startNodes(): List<Node> = filter { it.id[2] == 'A' }

    fun numIterationsNeeded(startNode: Node, graph: Graph, instructions: String): Int {
        println(startNode)
        var i = 0
        var current = startNode

        while (!current.isGhostGoal()) {
            val nextId = when (val instruction = instructions[i % instructions.length]) {
                'L' -> current.left
                'R' -> current.right
                else -> throw RuntimeException("uh oh: $instruction")
            }
            current = graph.nodeOfId(nextId)
            i++
        }

        return i
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first().trim()

        val nodes = input.drop(2).map(Node::fromLine)
        val graph = Graph.fromNodeList(nodes)

        var i = 0L
        val startNodes = nodes.startNodes()
        val stepsPerNode = startNodes.withIndex().map {
            println("Calculating steps for index ${it.index}")
            numIterationsNeeded(it.value, graph, instructions)
        }
        println("steps per node: $stepsPerNode")
        val stepFactorsCounts = stepsPerNode.map { it.factorsWithCount() }
        val minStepPrimeFactors = stepFactorsCounts.map { stepFactorsCounts: Map<Int, Int> ->
            stepFactorsCounts.toList()
        }.flatten().fold(mutableMapOf<Int, Int>()) { acc, (factor, occurences) ->
            val currentMaxOccurences = acc[factor]
            if (currentMaxOccurences == null) {
                acc[factor] = occurences
            } else {
                acc[factor] = max(currentMaxOccurences, occurences)
            }
            acc
        }.toList()
        println("step factors counts: $stepFactorsCounts")
        println("min step prime factors: $stepFactorsCounts")

        return minStepPrimeFactors.fold(1) { acc, (factor, occurrences) -> acc * factor * occurrences }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("day08/Day08_test1")
    val testInput2 = readInput("day08/Day08_test2")
    val testInput3 = readInput("day08/Day08_test3")
    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    check(part2(testInput3) == 6L)

    val input = readInput("day08/Day08")
    part1(input).println()
    part2(input).println()
}
