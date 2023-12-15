package day11.part2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Tests {
    @Test
    fun `transpose should work as expected`() {
        val given = listOf("ABC", "DEF", "GHI")
        val expected = listOf("ADG", "BEH", "CFI")

        assertEquals(expected, given.transpose())
    }

    @Test
    fun `transpose should work as expected when there are more cols than rows`() {
        val given = listOf("ABC", "DEF")
        val expected = listOf("AD", "BE", "CF")

        assertEquals(expected, given.transpose())
    }

    @Test
    fun `transpose should work as expected when there are more rows than cols`() {
        val given = listOf("AB", "DE", "GH")
        val expected = listOf("ADG", "BEH")

        assertEquals(expected, given.transpose())
    }
}