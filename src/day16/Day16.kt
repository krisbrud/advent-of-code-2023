package day16

import println
import readInput

fun String.calculateHash(): Int = fold(0) { current, c ->
    val asciiValue = c.toInt()
    val added = current + asciiValue
    val multiplied = added * 17
    val afterModulo = multiplied % 256
    afterModulo
}

// Somewhat of a conflation of the steps used to build a box of lenses and the lens itself, but hey - it works ¯\_(ツ)_/¯
sealed class Step(val label: String) {
    class EqualsStep(label: String, val focalLength: Int) : Step(label)
    class MinusStep(label: String) : Step(label)

    companion object {
        fun fromString(string: String): Step = when {
            string.contains('=') -> {
                val focalLength = string.last().digitToInt()
                val label = string.dropLast(2)
                EqualsStep(label = label, focalLength = focalLength)
            }

            string.contains('-') -> {
                val label = string.dropLast(1)
                MinusStep(label = label)
            }

            else -> throw RuntimeException("Couldn't parse string '$string'")
        }
    }
}

fun Step.hash(): Int = label.calculateHash()

fun List<List<Step>>.focusingPower(): Int {
    val boxPowers = mapIndexed { i, box ->
        val boxNumber = i + 1
        val boxPower = box.filterIsInstance<Step.EqualsStep>().withIndex().sumOf { (j, step) ->
            val slotNumber = j + 1
            val focalLength = step.focalLength
            boxNumber * slotNumber * focalLength
        }
        boxPower
    }

    return boxPowers.sum()
}

fun main() {
    fun part1(input: List<String>): Int {
        val steps = input.first().split(',')
        val hashes = steps.map(String::calculateHash)
        return hashes.sum()
    }

    fun part2(input: List<String>): Int {
        val boxes = MutableList<MutableList<Step>>(256) { mutableListOf() }

        val steps = input.first().split(',').map(Step::fromString)

        steps.forEach { step ->
            val hash = step.hash()
            val box = boxes[hash]
            when (step) {
                is Step.MinusStep -> {
                    boxes[hash] = box.filterNot { it.label == step.label }.toMutableList()
                }

                is Step.EqualsStep -> {
                    boxes[hash] = when (val maybeIndex = box.indexOfFirst { it.label == step.label }) {
                        -1 -> {
                            box.add(step)
                            box
                        }

                        else -> {
                            box[maybeIndex] = step
                            box
                        }
                    }
                }
            }
        }

        return boxes.focusingPower()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day16/Day16_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("day16/Day16")
    part1(input).println()
    part2(input).println()
}
