package day12

import day11.part2.isEmptySpace
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
        springs[group].let { it.isEmptySpace() or it.isUnknown() }
}

// possibleArrangements(springs, groups) equals
// if arrangements are not possible:
// return 0
// if zero groups left
// return 1 if no operational springs left, 0 otherwise
/// Assume there is at least one group left below
// if it is possible to have an arrangement with the remaining springs and groups:
// return
// possibleArrangements(springs without first group, groups without first group)
// PLUS
// possibleArrangements(springs without first spring, groups)

fun possibleArrangements(springs: String, groups: List<Int>): Int {
    if (springs.isEmpty()) {
        return if (groups.isEmpty()) 1 else 0
    }
    if (groups.isEmpty()) {
        return if (springs.none { it.isDamaged() }) 1 else 0
    }

    val firstGroup = groups.first()
    val restContainsMandatory = springs.any { it.isDamaged() }
    val canPlaceInFirstPosition = canPlaceSpringInFirstPosition(springs, firstGroup)
    return if (canPlaceInFirstPosition && groups.size > 1) {
        possibleArrangements(springs.drop(firstGroup + 1), groups.drop(1)) + // Arrangements if we place spring here
            possibleArrangements(springs.drop(1), groups) // Arrangements if we don't place spring here
//    } else if (groups.size == 1) {
//        if (restContainsMandatory) {
//            val firstDamagedIndex = springs.indexOfFirst { it.isDamaged() }
//            val lastDamagedIndex = springs.indexOfLast { it.isDamaged() }
//
//            val indexDiff = lastDamagedIndex - firstDamagedIndex
//            if (indexDiff >= firstGroup) {
//                0
//            } else {
//                springs
//                    .withIndex()
//                    .windowed(firstGroup)
//                    .filter { indexedValues -> indexedValues.any { it.index == firstDamagedIndex} && indexedValues.any{it.index == lastDamagedIndex} }
//                    .map { indexedValues -> indexedValues.map(IndexedValue<Char>::value) }
//                    .count { windowedSprings -> canPlaceSpringInFirstPosition(windowedSprings.toString(), firstGroup) }
//            }
//        } else {
//            0
//        }
   } else {
        possibleArrangements(springs.drop(1), groups) // Arrangements if we don't place spring here
    }
}

fun String.arrangements(): Int {
    val (springs, groupsString) = trim().split(" ")
    val groups = groupsString.split(",").map { it.trim().toInt() }
    return possibleArrangements(springs, groups)
}

fun main() {
    fun part1(input: List<String>): Int {
        val possibleArrangementSum = input.sumOf { it.arrangements() }

        return possibleArrangementSum
    }

    fun part2(input: List<String>): Int {

        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 21)
//    check(part2(testInput) == 2)

    val input = readInput("day12/Day12")
    part1(input).println()
    part2(input).println()
}
