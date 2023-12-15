package day12

import day11.part2.isEmptySpace
import println
import readInput

typealias Spring = Char

fun Spring.isOperational(): Boolean = this == '.'
fun Spring.isDamaged(): Boolean = this == '#'
fun Spring.isUnknown(): Boolean = this == '?'

fun canPlaceSpringInFirstPosition(springs: String, group: Int): Boolean = when(springs.length) {
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

//fun possibleArrangements(springs: String, groups: List<Int>): Int {
//    // An arrangement is possible if
//    // - It is possible to place the first spring somewhere and the remaining springs somewhere
//
//    // It is _not_ any possible arrangements if
//    // - The first spring is operational and it is not part of a contiguous group of damaged or unknown springs
//    //   that end with an operational or unknown or empty spring
//    // - There exist damaged springs but no corresponding groups
//    // - There exist no springs but existing groups
//    val firstSpring = springs.firstOrNull()
//    firstSpring ?: return if (groups.isEmpty()) 1 else {
//
//    }
//    if (firstSpring.isDamaged())
//
//    // It is possible to place the first group at the first non-operational spring if
//    // there exists a group of contiguous damaged and/or unknown springs of the length of the first group
//    // OR
//
//
//    return 0
//}

fun String.arrangements(): Int {
    val (springs, groupsString) = trim().split(" ")
    val groups = groupsString.split(",").map { it.trim().toInt() }
//    return 0
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
