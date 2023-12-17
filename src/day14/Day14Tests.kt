package day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Tests {
    @Test
    fun `transpose should work as expected`() {
        assertEquals(listOf("123", "456"), listOf("14", "25", "36").transpose())
        assertEquals(listOf("14", "25", "36"), listOf("123", "456").transpose())
        assertEquals(listOf("13", "24"), listOf("12", "34").transpose())
    }

    val beforeTilt = """
    O....#....
    O.OO#....#
    .....##...
    OO.#O....O
    .O.....O#.
    O.#..O.#.#
    ..O..#O..O
    .......O..
    #....###..
    #OO..#....
    """.trimIndent().splitByLines()

    val tilted = """
    OOOO.#.O..
    OO..#....#
    OO..O##..O
    O..#.OO...
    ........#.
    ..#....#.#
    ..O..#.O.O
    ..O.......
    #....###..
    #....#....
    """.trimIndent().splitByLines()

    @Test
    fun `tilting rocks north works as expected`() {
        assertEquals(tilted, beforeTilt.tiltRocksNorth())
    }

    @Test
    fun `calculating load works as expected`() {
        assertEquals(136, tilted.calculateLoad())
    }

    fun String.splitByLines(): List<String> = split("\n")

    val oneCycle = """
    .....#....
    ....#...O#
    ...OO##...
    .OO#......
    .....OOO#.
    .O#...O#.#
    ....O#....
    ......OOOO
    #...O###..
    #..OO#....
    """.trimIndent().splitByLines()

    val twoCycles = """
    .....#....
    ....#...O#
    .....##...
    ..O#......
    .....OOO#.
    .O#...O#.#
    ....O#...O
    .......OOO
    #..OO###..
    #.OOO#...O
    """.trimIndent().splitByLines()

    val threeCycles = """
    .....#....
    ....#...O#
    .....##...
    ..O#......
    .....OOO#.
    .O#...O#.#
    ....O#...O
    .......OOO
    #...O###.O
    #.OOO#...O
    """.trimIndent().splitByLines()

    @Test
    fun `cycles should work as expected`() {
        val actualOneCycle = beforeTilt.cycle()
        val actualTwoCycles = actualOneCycle.cycle()
        val actualThreeCycles = actualTwoCycles.cycle()
        assertEquals(oneCycle, actualOneCycle)
        assertEquals(twoCycles, actualTwoCycles)
        assertEquals(threeCycles, actualThreeCycles)
    }

    @Test
    fun `rotate right should work as expected`() {
        val given = listOf("12", "34")
        val expect = listOf("31", "42")
        assertEquals(expect, given.rotateRight())
    }

    @Test
    fun `rotate left should work as expected`() {
        val given = listOf("31", "42")
        val expect = listOf("12", "34")
        assertEquals(expect, given.rotateLeft())
    }
}