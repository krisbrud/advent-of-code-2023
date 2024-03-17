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
        val mid = springs.take(group).drop(1)
        firstIsDamaged or (springs.first().isUnknown() and mid.any { it.isDamaged() } and springs[group].isOperational())
    } else {
        true
        // false
    }
}

fun isRestImpossible(springs: String, groups: List<Int>): Boolean {
    val spaces = groups.size - 1
    return springs.length < (spaces + groups.sum())
}

fun possibleArrangements(springs: String, groups: List<Int>): Long {
    if (springs.isEmpty()) {
        return if (groups.isEmpty()) 1L else 0L
    }
    if (groups.isEmpty()) {
        return if (springs.none { it.isDamaged() }) 1L else 0L
    }
    if (isRestImpossible(springs, groups)) return 0L

    val firstGroup = groups.first()
    val canPlaceInFirstPosition = canPlaceSpringInFirstPosition(springs, firstGroup)
    val mustPlaceInFirstPosition = mustPlaceSpringInFirstPosition(springs, firstGroup)
    return if (canPlaceInFirstPosition) {
        memoizedPossibleArrangements(springs.drop(firstGroup + 1), groups.drop(1)) + // Arrangements if we place spring here
            if (!mustPlaceInFirstPosition) {
               memoizedPossibleArrangements(springs.drop(1), groups)
            } else {
                0L
            } // Arrangements if we don't place spring here
    } else if (mustPlaceInFirstPosition) {
        0L
    } else {
        memoizedPossibleArrangements(springs.drop(1), groups) // Arrangements if we don't place spring here
    }
}

val cache = mutableMapOf<String, Long>()
fun memoizedPossibleArrangements(springs: String, groups: List<Int>): Long {
    val key = "$springs;${groups.joinToString(",")}"
    return cache.getOrPut(key) { possibleArrangements(springs, groups) }
}


fun String.arrangements(): Long {
    val (springs, groupsString) = trim().split(" ")
    val groups = groupsString.split(",").map { it.trim().toInt() }
    return memoizedPossibleArrangements(springs, groups)
}

fun String.unfoldedArrangements(): Long {
    val (springs, groupsString) = trim().split(" ")
    val groups = groupsString.split(",").map { it.trim().toInt() }
    return possibleArrangements("$springs?$springs?$springs?$springs?$springs",
        groups + groups + groups + groups + groups
    )
}

fun main() {
    fun part1(input: List<String>): Long {
        val possibleArrangementSum = input.sumOf { it.arrangements() }

        return possibleArrangementSum
    }

    fun part2(input: List<String>): Long {
        val arrangements = input.withIndex().map {
//            println("Index: ${it.index}")
            it.value.unfoldedArrangements().toLong()
        }

        return arrangements.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("day12/Day12")
//    val input2 = readInput("day12/Day12_modified")
    part1(input).println()
    part2(input).println()
//    part2(input2).println()
}
