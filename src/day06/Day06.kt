package day06

import println
import readInput

data class HighScore(val raceTime: Long, val distance: Long)

fun main() {
    fun distanceGiven(buttonTime: Long, raceTime: Long): Long {
        val initialSpeed = buttonTime
        val movingTime = raceTime - buttonTime
        val distance = initialSpeed * movingTime
        return distance
    }

    fun part1(input: List<String>): Int {
        val timeLimits = input.component1().drop(5).trim().split(" ")
            .map(String::trim).filter(String::isNotEmpty).map(String::toLong)
        val distances = input.component2().drop(9).trim().split(" ")
            .map(String::trim).filter(String::isNotEmpty).map(String::toLong)
        val highscores = timeLimits.zip(distances).map { (time, distance) -> HighScore(raceTime = time, distance = distance) }
        val winningButtonTimesPerHighscore = highscores.map { highscore ->
            (1 until highscore.raceTime).filter { buttonTime ->
                val distance = distanceGiven(buttonTime = buttonTime, raceTime = highscore.raceTime)
                distance > highscore.distance
            }
        }

        return winningButtonTimesPerHighscore.map { it.size }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val timeLimit = input.component1().drop(5).trim().filterNot(Char::isWhitespace).toLong()
        val distance = input.component2().drop(9).trim().filterNot(Char::isWhitespace).toLong()

        val highscore = HighScore(raceTime = timeLimit, distance = distance)
        val winningButtonTimesPerHighscoreCount = (1 until highscore.raceTime).filter { buttonTime ->
                val distance = distanceGiven(buttonTime = buttonTime, raceTime = highscore.raceTime)
                distance > highscore.distance
            }.size

        return winningButtonTimesPerHighscoreCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}
