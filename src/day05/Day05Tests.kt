package day05

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class RangeIntersectionTest {
    val someRange = (5L)..(10L)

    fun assertIntersectionEquals(expected: LongRange?, first: LongRange, second: LongRange) {
        assertEquals(expected, first.intersection(second))
        assertEquals(expected, second.intersection(first))
    }

    @Test
    fun `given fully contained, should return non-null intersection`() {
        assertIntersectionEquals(5L..10L, someRange, someRange)
        assertIntersectionEquals(5L..9L, (5L..9L), someRange)
        assertIntersectionEquals(6L..9L, (6L..9L), someRange)
        assertIntersectionEquals(6L..10L, (6L..10L), someRange)
        assertIntersectionEquals(5L..5L, (5L..5L), someRange)
        assertIntersectionEquals(10L..10L, (10L..10L), someRange)
        assertIntersectionEquals(7L..7L, (7L..7L), someRange)
    }

    @Test
    fun `given outside, should return null`() {
        assertIntersectionEquals(null, someRange, 1L..4L)
        assertIntersectionEquals(null, someRange, 4L..4L)
        assertIntersectionEquals(null, someRange, 11L..11L)
        assertIntersectionEquals(null, someRange, 11L..14L)
    }


    @Test
    fun `given at least one limit outside, should return correct intersection`() {
        // Full range as output, but at least one outside
        assertIntersectionEquals(5L..10L, someRange, 5L..11L)
        assertIntersectionEquals(5L..10L, someRange, 4L..10L)
        assertIntersectionEquals(5L..10L, someRange, 4L..11L)

        // Less than full range as output
        assertIntersectionEquals(5L..9L, someRange, 4L..9L)
        assertIntersectionEquals(6L..10L, someRange, 6L..11L)
        assertIntersectionEquals(5L..5L, someRange, 3L..5L)
        assertIntersectionEquals(5L..5L, someRange, 5L..5L)
        assertIntersectionEquals(10L..10L, someRange, 10L..12L)
        assertIntersectionEquals(10L..10L, someRange, 10L..10L)
    }
}

class RangeMappingTest {
    val rangeMapping1 = RangeMapping(destination = 50, source = 98, length = 2)
    val rangeMapping2 = RangeMapping(destination = 52, source = 50, length = 48)
    val rangeMappingGroup = RangeMappingGroup(listOf(rangeMapping1, rangeMapping2))

    @Test
    fun `should map rangemapping 1 correctly`() {
        assertEquals(50, rangeMapping1.mapOrThrow(98))
        assertEquals(51, rangeMapping1.mapOrThrow(99))
    }

    @Test
    fun `mapOrThrow should throw if outside of range`() {
        assertThrows(RuntimeException::class.java) {
            rangeMapping1.mapOrThrow(97)
        }
        assertThrows(RuntimeException::class.java) {
            rangeMapping1.mapOrThrow(100)
        }
    }

    @Test
    fun `should map individual ranges correctly`() {
        // destination range start, source range start, length
        // 50 98 2
        // 52 50 48

        val inputToExpectedOutputs = listOf<Pair<Int, Int>>(
            0 to 0,
            1 to 1,
            48 to 48,
            49 to 49,
            50 to 52,
            51 to 53,
            96 to 98,
            97 to 99,
            98 to 50,
            99 to 51,
        )

        inputToExpectedOutputs.forEach { (input, expected) ->
            assertEquals(expected.toLong(), rangeMappingGroup.mapElement(input.toLong()))
        }

    }
}