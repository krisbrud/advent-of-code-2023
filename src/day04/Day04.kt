package day04

import println
import readInput
import java.math.BigInteger

data class ScratchCard(val id: Int, val lookedFor: List<Int>, val drawn: List<Int>) {
    val numWinningNumbers: Int by lazy { drawn.count { lookedFor.contains(it) } }
    val points: Int by lazy { calculatePoints(numWinningNumbers) }

    fun calculatePoints(winningNumbers: Int): Int = if (winningNumbers >= 1) {
        BigInteger.valueOf(2).pow(winningNumbers - 1).toInt()
    } else {
        0
    }

    val wonCopyIds: List<Int> = if (numWinningNumbers >= 1) {
        (id + 1..id + numWinningNumbers).toList()
    } else emptyList()

    companion object {
        private val pattern = """Card (.+): (.+) \| (.+)""".toRegex()

        fun String.parseAsNumberList(): List<Int> = split(" ")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map(String::toInt)

        fun parseLine(line: String): ScratchCard {
            val matchGroups = pattern.findAll(line).map { it.groupValues }.first()

            val id = matchGroups[1].parseAsNumberList().first()
            val winningNumbers = matchGroups[2].parseAsNumberList()
            val drawnNumbers = matchGroups[3].parseAsNumberList()

            return ScratchCard(
                id = id,
                lookedFor = winningNumbers,
                drawn = drawnNumbers,
            )
        }
    }
}

fun ScratchCard.wonCards(scratchCardLookup: (Int) -> ScratchCard?): List<ScratchCard> {
    val wonDirectly = wonCopyIds
        .mapNotNull { scratchCardLookup(it) }
    return wonDirectly + wonDirectly.flatMap { it.wonCards(scratchCardLookup) }
}

fun ScratchCard.contribution(scratchCardLookup: (Int) -> ScratchCard?): Int {
    val won = wonCards(scratchCardLookup)
    // Return count of cards plus one since this card also contributes
    return won.size + 1
}

fun main() {
    fun part1(input: List<String>): Int {
        val scratchCards = input.map(ScratchCard::parseLine)
        return scratchCards.sumOf(ScratchCard::points)
    }

    fun part2(input: List<String>): Int {
        val scratchCards = input.map(ScratchCard::parseLine)

        // Define a map that associates each scratch card with its id
        val scratchCardMap = scratchCards.associateBy(ScratchCard::id)

        // Use brute force solution instead of memoization/starting at the end
        val contributions = scratchCards.map { it.contribution { id -> scratchCardMap[id] } }

        val contributionSum = contributions.sum()
        return contributionSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}
