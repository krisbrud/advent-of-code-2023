package day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Tests {
    @Test
    fun `transpose should work as expected`() {
        assertEquals(listOf("123", "456"), listOf("14", "25", "36").transpose())
        assertEquals(listOf("14", "25", "36"),listOf("123", "456").transpose())
        assertEquals(listOf("13", "24"),listOf("12", "34").transpose())
    }
}