package day07

import println
import readInput

enum class Card {
    // Implicitly in ascending order
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King,
    Ace;

    companion object {
        fun fromChar(char: Char): Card = when (char) {
            'A' -> Ace
            'K' -> King
            'Q' -> Queen
            'J' -> Jack
            'T' -> Ten
            '9' -> Nine
            '8' -> Eight
            '7' -> Seven
            '6' -> Six
            '5' -> Five
            '4' -> Four
            '3' -> Three
            '2' -> Two
            else -> throw RuntimeException("Unknown char $char")
        }
    }
}

enum class HandType {
    // Implicitly in ascending order
    HighCard,
    OnePair,
    TwoPairs,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind;
}

fun List<Card>.compareByValues(other: List<Card>): Int = when (val result = first().compareTo(other.first())) {
    0 -> drop(1).compareByValues(other.drop(1))  // cards are equal, compare by next card
    else -> result  // cards are different, return result
}


data class Hand(val cards: List<Card>) : Comparable<Hand> {
    val handType: HandType by lazy {
        val counts = cards.groupingBy { it }.eachCount().toList() // (Card, Int)
        val sortedByCounts = counts.sortedBy { (_, count) -> count }
        val (_, highestCount) = sortedByCounts.last()  // Assuming that there are always 5 cards in a hand
        val (_, secondHighestCount) = sortedByCounts.getOrNull(sortedByCounts.size - 2)
            ?: Pair(null, null) // Avoid crash if we have five of a kind
        when (highestCount) {
            5 -> HandType.FiveOfAKind
            4 -> HandType.FourOfAKind
            3 -> if (secondHighestCount == 2) HandType.FullHouse else HandType.ThreeOfAKind
            2 -> if (secondHighestCount == 2) HandType.TwoPairs else HandType.OnePair
            else -> HandType.HighCard
        }
    }

    override fun compareTo(other: Hand): Int {
        val byHandType = handType.compareTo(other.handType)
        return if (byHandType != 0) byHandType else cards.compareByValues(other.cards)
    }
}

data class BidHand(
    val hand: Hand,
    val bid: Int,
) {
    companion object {
        fun fromString(string: String): BidHand = string.split(" ").let { (handString, bidString) ->
            val bid = bidString.toInt()
            val cards = handString.map(Card::fromChar)
            BidHand(
                hand = Hand(cards),
                bid = bid,
            )
        }
    }
}

data class RankedBidHand(
    val bidHand: BidHand,
    val rank: Int
) {
    val winnings = rank * bidHand.bid
}

fun main() {
    fun part1(input: List<String>): Int {
        val bidHands = input.map(BidHand::fromString)
        val rankedBidHands = bidHands.sortedBy { it.hand }.mapIndexed { index, bidHand ->
            RankedBidHand(
                bidHand = bidHand,
                rank = index + 1
            )
        }
        val totalWinnings = rankedBidHands.sumOf { it.winnings }
        return totalWinnings
    }

    fun part2(input: List<String>): Int {


        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
//    check(part2(testInput) == 71503)

    val input = readInput("day07/Day07")
    part1(input).println()
    part2(input).println()
}
