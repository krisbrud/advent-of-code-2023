package day12

import println
import readInput

typealias Spring = Char

fun Spring.isOperational(): Boolean = this == '.'
fun Spring.isDamaged(): Boolean = this == '#'
fun Spring.isUnknown(): Boolean = this == '?'

fun canPlaceSpringInFirstPosition(springs: String, group: Int): Boolean = when (springs.length) {
    in (0..<group) -> false
    group -> springs.all { it.isDamaged() or it.isUnknown() }
    else -> springs.slice(0..<group).all { it.isDamaged() or it.isUnknown() } and
        springs[group].let { it.isOperational() or it.isUnknown() }
}

fun mustPlaceSpringInFirstPosition(springs: String, group: Int): Boolean {
    val firstIsDamaged = springs.first().isDamaged()
    return if (springs.length == group) {
        firstIsDamaged
    } else if (springs.length > group) {
        val mid = springs.take(group).drop(1).dropLast(1)
        firstIsDamaged or (springs.first().isUnknown() and mid.any { it.isDamaged() } and springs[group].isOperational())
    } else {
        true
        // false
    }
}

fun possibleArrangements(springs: String, groups: List<Int>): Int {
    if (springs.isEmpty()) {
        return if (groups.isEmpty()) 1 else 0
    }
    if (groups.isEmpty()) {
        return if (springs.none { it.isDamaged() }) 1 else 0
    }

    val firstGroup = groups.first()
    val canPlaceInFirstPosition = canPlaceSpringInFirstPosition(springs, firstGroup)
    val mustPlaceInFirstPosition = mustPlaceSpringInFirstPosition(springs, firstGroup)
    return if (canPlaceInFirstPosition) {
        possibleArrangements(springs.drop(firstGroup + 1), groups.drop(1)) + // Arrangements if we place spring here
            if (!mustPlaceInFirstPosition) {
                possibleArrangements(springs.drop(1), groups)
            } else {
                0
            } // Arrangements if we don't place spring here
    } else if (mustPlaceInFirstPosition) {
        0
    } else {
        possibleArrangements(springs.drop(1), groups) // Arrangements if we don't place spring here
    }
}


fun String.arrangements(): Int {
    val (springs, groupsString) = trim().split(" ")
    val groups = groupsString.split(",").map { it.trim().toInt() }
    return possibleArrangements(springs, groups)
}

fun String.unfoldedArrangements(): Int {
    val (springs, groupsString) = trim().split(" ")
    val groups = groupsString.split(",").map { it.trim().toInt() }
    return possibleArrangements("$springs?$springs?$springs?$springs?$springs",
        groups + groups + groups + groups + groups
    )
}

fun main() {
    fun part1(input: List<String>): Int {
        val possibleArrangementSum = input.sumOf { it.arrangements() }

        return possibleArrangementSum
    }

    fun part2(input: List<String>): Int {
        val arrangementSum = input.withIndex().sumOf {
            println("Index: ${it.index}")
            it.value.unfoldedArrangements()
        }

        return arrangementSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152)

    val input = readInput("day12/Day12")
    part1(input).println()
    part2(input).println()
}
