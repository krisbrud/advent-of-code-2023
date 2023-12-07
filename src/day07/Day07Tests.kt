package day07

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CardComparisonTest {
    val hand1 = BidHand.fromString("32T3K 765").hand
    val hand2 = BidHand.fromString("T55J5 684").hand
    val hand3 = BidHand.fromString("KK677 28").hand
    val hand4 = BidHand.fromString("KTJJT 220").hand
    val hand5 = BidHand.fromString("QQQJA 483").hand

    @Test
    fun `should sort cards with same handtype correctly`() {
        assertTrue(hand3 > hand4)
        assertTrue(hand5 > hand2)
    }
}