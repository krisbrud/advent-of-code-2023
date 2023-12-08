package day08

import println
import readInput

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
            return Node(id=id, left=left, right=right)
        }
    }
}

class Graph private constructor(private val nodes: Map<String, Node>) {
    fun nodeOfId(id: String): Node = nodes[id]!!
    companion object {
        fun fromNodeList(nodeList: List<Node>): Graph = Graph(nodeList.associateBy { it.id })
    }
}

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
            val nextId = when(instruction) {
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

    fun part2(input: List<String>): Int {

        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("day08/Day08_test1")
    val testInput2 = readInput("day08/Day08_test2")
    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
//    check(part2(testInput) == 71503)

    val input = readInput("day08/Day08")
    part1(input).println()
    part2(input).println()
}
