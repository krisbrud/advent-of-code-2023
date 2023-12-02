package day02

import println
import readInput
import java.lang.RuntimeException
import kotlin.math.max

class CubeSample(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    /**
     * Find the maximum number of cubes per color from two CubeSamples
     */
    fun maxPerColor(other: CubeSample) = CubeSample(
        red = max(red, other.red),
        green = max(green, other.green),
        blue = max(blue, other.blue),
    )

    /**
     * Check if the sample is within a maximum limit per color
     */
    fun withinLimit(redLimit: Int, greenLimit: Int, blueLimit: Int): Boolean {
        return red <= redLimit && green <= greenLimit && blue <= blueLimit
    }

    /**
     * The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together.
     */
    fun power(): Int = red * green * blue
}

class Game(
    val id: Int,
    val samples: List<CubeSample>) {

    /**
     * Check if a game is possible given some maximum count of red, green and blue cubes
     */
    fun possibleGiven(red: Int, green: Int, blue: Int): Boolean {
        val maxShown = samples.reduce(CubeSample::maxPerColor)
        return maxShown.withinLimit(redLimit = red, blueLimit = blue, greenLimit = green)
    }

    /**
     * The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together.
     */
    fun powerOfFewestPossibleCubes(): Int {
        val minPossibleColors = samples.reduce(CubeSample::maxPerColor)
        return minPossibleColors.power()
    }

    companion object {
        private val idPattern = """Game (\d*):""".toRegex()

        private val redPattern = """(\d*) red""".toRegex()
        private val bluePattern = """(\d*) blue""".toRegex()
        private val greenPattern = """(\d*) green""".toRegex()

        private fun Regex.parseColorOrZero(sampleString: String): Int {
            val found = this.find(sampleString)
            val match = found?.groupValues?.component2() // Component 1 matches the entire pattern, 2 matches the group
            return match?.toIntOrNull() ?: 0
        }

        fun parseLine(line: String): Game {
            val id = idPattern.find(line)?.groupValues
                ?.component2()// Component 1 matches the pattern, 2 matches the group (i.e. the id)
                ?.toIntOrNull()
                ?: throw RuntimeException("Couldn't match id")

            val samples = line.split(":")
                .component2() // The actual ids drawn, not the game ids
                .split(";")
                .map { sampleString ->
                    val red = redPattern.parseColorOrZero(sampleString)
                    val blue = bluePattern.parseColorOrZero(sampleString)
                    val green = greenPattern.parseColorOrZero(sampleString)

                    CubeSample(red = red, blue = blue, green = green)
                }

            return Game(id, samples)
        }
    }
}


fun main() {
    fun part1(input: List<String>): Int {
        // Problem statement: Which games would have been possible if the bag contained only
        // - 12 red cubes,
        // - 13 green cubes, and
        // - 14 blue cubes?
        // -> What is the sum of the IDs of those games?

        val games = input.map(Game::parseLine)

        val possibleGames = games.filter { game ->
            game.possibleGiven(
                red = 12,
                green = 13,
                blue = 14,
            )
        }

        return possibleGames.sumOf(Game::id)
    }

    fun part2(input: List<String>): Int {
        // What is the fewest number of cubes of each color that could have been in the bag to make the game possible?
        // For each game, find the minimum set of cubes that must have been present.
        // What is the sum of the power of these sets?

        val games = input.map(Game::parseLine)
        val powers = games.map(Game::powerOfFewestPossibleCubes)

        return powers.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}
