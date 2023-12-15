package day11.part1

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

    @Test
    fun `expandEmptyRows should work as expected when first row empty`() {
        val given = listOf("...", "#..", ".#.")
        val expected = listOf("...", "...", "#..", ".#.")

        assertEquals(expected, given.emptyRowsDuplicated())
    }

    @Test
    fun `expandEmptyRows should work as expected when last row empty`() {
        val given = listOf("#..", ".#.", "...")
        val expected = listOf("#..", ".#.", "...", "...")

        assertEquals(expected, given.emptyRowsDuplicated())
    }

    val universe = """
    ...#......
    .......#..
    #.........
    ..........
    ......#...
    .#........
    .........#
    ..........
    .......#..
    #...#.....
    """.trimIndent().split("\n")

    val expandedUniverse = """
    ....#........
    .........#...
    #............
    .............
    .............
    ........#....
    .#...........
    ............#
    .............
    .............
    .........#...
    #....#.......
    """.trimIndent().split("\n")

    @Test
    fun `test expanding universe`() {
        assertEquals(expandedUniverse, universe.expandEmptySpace())
    }

    @Test
    fun `test shortest path`() {
        assertEquals(5, expandedUniverse.shortestPathLength(
            from = Coordinate(row = 9, col = 0),
            to = Coordinate(row = 9, col = 5),
        ))
        assertEquals(15, expandedUniverse.shortestPathLength(
            from = Coordinate(row = 0, col = 4),
            to = Coordinate(row = 10, col = 9)
        ))
    }

}