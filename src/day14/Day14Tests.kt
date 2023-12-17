package day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Tests {
    @Test
    fun `transpose should work as expected`() {
        assertEquals(listOf("123", "456"), listOf("14", "25", "36").transpose())
        assertEquals(listOf("14", "25", "36"),listOf("123", "456").transpose())
        assertEquals(listOf("13", "24"),listOf("12", "34").transpose())
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
    """.trimIndent().split("\n")

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
    """.trimIndent().split("\n")

    @Test
    fun `tilting rocks north works as expected`() {
        assertEquals(tilted, beforeTilt.tiltRocksNorth())
    }

    @Test
    fun `calculating load works as expected`() {
        assertEquals(136, tilted.calculateLoad())
    }

}