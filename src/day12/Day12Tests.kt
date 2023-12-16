package day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Tests {
    val line1 = "???.### 1,1,3"
    val line2 = ".??..??...?##. 1,1,3"
    val line3 = "?#?#?#?#?#?#?#? 1,3,1,6"
    val line4 = "????.#...#... 4,1,1"
    val line5 = "????.######..#####. 1,6,5"
    val line6 = "?###???????? 3,2,1"


    @Test
    fun `test can place spring in first position when there are as many springs left as the size of the group`() {
        assertEquals(true, canPlaceSpringInFirstPosition("###", 3))
        assertEquals(true, canPlaceSpringInFirstPosition("#", 1))
        assertEquals(true, canPlaceSpringInFirstPosition("?", 1))
        assertEquals(false, canPlaceSpringInFirstPosition("###", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("##", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("?#", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("#?", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("??", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("?", 1))
    }

    @Test
    fun `test must place spring in first position`() {
//        assertEquals(true, mustPlaceSpringInFirstPosition("###", 2)) // Doesn't check if there is a mandatory after
        assertEquals(true, mustPlaceSpringInFirstPosition("?#.", 2))
//        assertEquals(true, mustPlaceSpringInFirstPosition("#?.", 2))
//        assertEquals(true, mustPlaceSpringInFirstPosition("#.", 1))
//        assertEquals(false, mustPlaceSpringInFirstPosition(".#", 1))
//        assertEquals(false, mustPlaceSpringInFirstPosition(".#.", 1))
//        assertEquals(true, mustPlaceSpringInFirstPosition("##.", 1)) // Doesn't check if there is a mandatory after
//        assertEquals(true, mustPlaceSpringInFirstPosition("##.", 2))
    }

    @Test
    fun `test can place spring in first position when there are more springs left than in the group`() {
        assertEquals(true, canPlaceSpringInFirstPosition("###?", 3))
        assertEquals(true, canPlaceSpringInFirstPosition("###.", 3))
        assertEquals(false, canPlaceSpringInFirstPosition("###.", 4))
        assertEquals(false, canPlaceSpringInFirstPosition("####", 3))
        assertEquals(false, canPlaceSpringInFirstPosition("##", 1))
        assertEquals(false, canPlaceSpringInFirstPosition("?#", 1))
        assertEquals(false, canPlaceSpringInFirstPosition("###?", 2))
        assertEquals(false, canPlaceSpringInFirstPosition("#.?", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("?#.", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("#?.", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("??.", 2))
        assertEquals(true, canPlaceSpringInFirstPosition("?.", 1))
        assertEquals(false, canPlaceSpringInFirstPosition(".", 1))
    }

    @Test
    fun `test lines should have correct number of possible configurations`() {
        assertEquals(1, line1.arrangements())
        assertEquals(4, line2.arrangements())
        assertEquals(1, line3.arrangements())
        assertEquals(1, line4.arrangements())
        assertEquals(4, line5.arrangements())
        assertEquals(10, line6.arrangements())
    }

    @Test
    fun `test possibleArrangements when there is one group left`() {
        assertEquals(4, possibleArrangements("????", listOf(1)))
        assertEquals(1, possibleArrangements("#???", listOf(1)))
        assertEquals(1, possibleArrangements("??#?", listOf(1)))
        assertEquals(1, possibleArrangements("???#", listOf(1)))
        assertEquals(1, possibleArrangements("???#", listOf(2)))
        assertEquals(0, possibleArrangements("#??#", listOf(2)))
        assertEquals(0, possibleArrangements("#?#", listOf(2)))
        assertEquals(1, possibleArrangements("##?", listOf(2)))
        assertEquals(1, possibleArrangements("?##", listOf(2)))
        assertEquals(1, possibleArrangements("?##???", listOf(2)))
        assertEquals(1, possibleArrangements("?##?", listOf(2)))
        assertEquals(2, possibleArrangements("?##?", listOf(3)))
    }

    @Test
    fun `test possibleArrangements when there is two groups left`() {
        assertEquals(3, possibleArrangements("????", listOf(1, 1)))
        assertEquals(1, possibleArrangements("????", listOf(1, 2)))
        assertEquals(0, possibleArrangements("????", listOf(2, 2)))
        assertEquals(0, possibleArrangements("??#?", listOf(2, 1)))
        assertEquals(1, possibleArrangements("??#??", listOf(2, 1)))
        assertEquals(1, possibleArrangements("?#?#", listOf(2, 1)))
//        assertEquals(1, possibleArrangements("#???", listOf(1)))
//        assertEquals(1, possibleArrangements("??#?", listOf(1)))
//        assertEquals(1, possibleArrangements("???#", listOf(1)))
//        assertEquals(1, possibleArrangements("???#", listOf(2)))
//        assertEquals(0, possibleArrangements("#??#", listOf(2)))
//        assertEquals(0, possibleArrangements("#?#", listOf(2)))
//        assertEquals(1, possibleArrangements("##?", listOf(2)))
//        assertEquals(1, possibleArrangements("?##", listOf(2)))
//        assertEquals(1, possibleArrangements("?##???", listOf(2)))
//        assertEquals(1, possibleArrangements("?##?", listOf(2)))
//        assertEquals(2, possibleArrangements("?##?", listOf(3)))
    }
}