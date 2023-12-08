package day07.part2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class Day07Part2Test {
    val rank1Hand = BidHand.fromString("32T3K 765").hand
    val rank2Hand = BidHand.fromString("KK677 28").hand
    val rank3Hand = BidHand.fromString("T55J5 684").hand
    val rank4Hand = BidHand.fromString("QQQJA 483").hand
    val rank5Hand = BidHand.fromString("KTJJT 220").hand

    @Test
    fun `should not replace any cards when there are no jokers`() {
        assertEquals(rank1Hand, rank1Hand.withJokerReplaced())
        assertEquals(rank2Hand, rank2Hand.withJokerReplaced())
    }

    @Test
    fun `should replace hand 3  with jokers correctly`() {
        assertEquals(Hand(listOf(
            Card.Ten,
            Card.Five,
            Card.Five,
            Card.Five,
            Card.Five,
        )), rank3Hand.withJokerReplaced())
    }

    @Test
    fun `should replace hand 4 with jokers correctly`() {
        assertEquals(Hand(listOf(
            Card.Queen,
            Card.Queen,
            Card.Queen,
            Card.Queen,
            Card.Ace
        )), rank4Hand.withJokerReplaced())
    }

    @Test
    fun `should replace hand 5 with jokers correctly`() {
        assertEquals(Hand(listOf(
            Card.King,
            Card.Ten,
            Card.Ten,
            Card.Ten,
            Card.Ten,
        )), rank5Hand.withJokerReplaced())
    }

    @Test
    fun `should sort hand 1 correctly`() {
        assertAll(
            { assertTrue(rank1Hand < rank2Hand) },
            { assertTrue(rank1Hand < rank3Hand) },
            { assertTrue(rank1Hand < rank4Hand) },
            { assertTrue(rank1Hand < rank5Hand) },
        )
    }

    @Test
    fun `should sort hand 2 correctly`() {
        assertAll(
            { assertTrue(rank2Hand > rank1Hand) },
            { assertTrue(rank2Hand < rank3Hand) },
            { assertTrue(rank2Hand < rank4Hand) },
            { assertTrue(rank2Hand < rank5Hand) },
        )
    }

    @Test
    fun `should sort hand 3 correctly`() {
        assertAll(
            { assertTrue(rank3Hand > rank1Hand) },
            { assertTrue(rank3Hand > rank2Hand) },
            { assertTrue(rank3Hand < rank4Hand) },
            { assertTrue(rank3Hand < rank5Hand) },
        )
    }

    @Test
    fun `should sort hand 4 correctly`() {
        assertAll(
            { assertTrue(rank4Hand > rank1Hand) },
            { assertTrue(rank4Hand > rank2Hand) },
            { assertTrue(rank4Hand > rank3Hand) },
            { assertTrue(rank4Hand < rank5Hand) },
        )
    }
}
