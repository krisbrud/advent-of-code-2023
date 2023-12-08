package day07.part2

import println
import readInput

enum class Card {
    // Implicitly in ascending order
    Joker,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Queen,
    King,
    Ace;

    companion object {
        fun fromChar(char: Char): Card = when (char) {
            'A' -> Ace
            'K' -> King
            'Q' -> Queen
            'T' -> Ten
            '9' -> Nine
            '8' -> Eight
            '7' -> Seven
            '6' -> Six
            '5' -> Five
            '4' -> Four
            '3' -> Three
            '2' -> Two
            'J' -> Joker
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
    private fun List<Card>.sortedCounts() = this@sortedCounts.groupingBy { it }.eachCount().toList()
        .sortedBy { (_, count) -> count }

    fun withJokerReplaced(): Hand {
        val mostCommonNonJokerCard = cards.sortedCounts()
            .filter { (card, _) -> card != Card.Joker }
            .maxByOrNull { (_, count) -> count }?.first
            ?: Card.Ace // Default to ace if all cards are Jokers

        val cardsWithReplacements = cards.map { card ->
            when (card) {
                Card.Joker -> mostCommonNonJokerCard
                else -> card // Use the same card
            }
        }

        return Hand(cardsWithReplacements)
    }

    val handType: HandType by lazy {
        val replacedCards = withJokerReplaced().cards
        val sortedReplacedCards = replacedCards.sortedCounts()

        val (_, highestCount) = sortedReplacedCards.last()  // Assuming that there are always 5 cards in a hand
        val (_, secondHighestCount) = sortedReplacedCards.getOrNull(sortedReplacedCards.size - 2)
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
            val cards = handString.map(Card.Companion::fromChar)
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
    fun part2(input: List<String>): Int {
        val bidHands = input.map(BidHand.Companion::fromString)
        val rankedBidHands = bidHands.sortedBy { it.hand }.mapIndexed { index, bidHand ->
            RankedBidHand(
                bidHand = bidHand,
                rank = index + 1
            )
        }
        val totalWinnings = rankedBidHands.sumOf { it.winnings }
        return totalWinnings

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part2(testInput) == 5905)

    val input = readInput("day07/Day07")
    part2(input).println()
}
