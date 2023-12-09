package day09

import println
import readInput

fun List<Int>.diff(): List<Int> = windowed(2).map { (first, second) -> second - first }

fun List<Int>.allZeroes(): Boolean = all { it == 0 }

fun List<Int>.diffUntilAllZeroes(): List<List<Int>> {
    val lists = mutableListOf<List<Int>>()
    var list = this
    while (!list.allZeroes()) {
        lists.add(list)
        list = list.diff()
    }
    lists.add(list) // All zeroes list
    return lists.toList() // Remove mutability
}

fun List<List<Int>>.predict(): Int {
    require(last().allZeroes())

    return reversed().fold(0) { prevLast, list ->
        val last = list.last()
        val prediction = prevLast + last
        prediction
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val histories = input.map { it.split(" ").map(String::trim).map(String::toInt)}
        val predictions = histories.map { history -> history.diffUntilAllZeroes().predict() }

        return predictions.sum()
    }

    fun part2(input: List<String>): Int {

        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("day09/Day09")
    part1(input).println()
    part2(input).println()
}
