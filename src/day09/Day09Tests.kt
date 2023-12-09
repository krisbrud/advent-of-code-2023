package day09

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Tests {
    val list1 = listOf(0, 3, 6, 9, 12, 15)
    val list2 = listOf(3, 3, 3, 3, 3)
    val list3 = listOf(0, 0, 0, 0)

    @Test
    fun `diff should work as expected`() {
        assertEquals(list2, list1.diff())
        assertEquals(list3, list2.diff())
    }

    @Test
    fun `diffUntilAllZeroes should work as expected`() {
        assertEquals(listOf(list1, list2, list3), list1.diffUntilAllZeroes())
        assertEquals(listOf(list2, list3), list2.diffUntilAllZeroes())
        assertEquals(listOf(list3), list3.diffUntilAllZeroes())
    }

    @Test
    fun `should predict correctly`() {
        val history1 = listOf(0, 3, 6, 9, 12, 15)
        val history2 = listOf(1, 3, 6, 10, 15, 21)
        val history3 = listOf(10, 13, 16, 21, 30, 45)
        assertEquals(18, history1.diffUntilAllZeroes().predict())
        assertEquals(28, history2.diffUntilAllZeroes().predict())
        assertEquals(68, history3.diffUntilAllZeroes().predict())
    }
}