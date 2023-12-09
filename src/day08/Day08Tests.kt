package day08

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Tests {
    @Test
    fun `firstFactor should work as expected`() {
        assertEquals(null, 1.firstFactor())
        assertEquals(null, 0.firstFactor())
        assertEquals(2, 2.firstFactor())
        assertEquals(3, 3.firstFactor())
        assertEquals(2, 4.firstFactor())
    }

    @Test
    fun `factorize should give correct factors`() {
        assertEquals(listOf(2, 3), 6.factorize())
        assertEquals(listOf(2, 2, 3), 12.factorize())
    }
}