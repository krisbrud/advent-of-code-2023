package day05

import println
import readInput
import kotlin.math.min

data class RangeMapping(
    val destination: Long,
    val source: Long,
    val length: Long,
) {
    val sourceRange = source..<(source + length)
    val destinationRange = destination..<(destination + length)
    fun contains(element: Long): Boolean = sourceRange.contains(element)
    fun mapOrThrow(sourceElement: Long): Long {
        if (!sourceRange.contains(sourceElement)) throw RuntimeException("Outside range!")
        val offset = sourceElement - source
        return destination + offset
    }
}

data class RangeMappingGroup(
    val mappings: List<RangeMapping>
) {
    fun mapElement(element: Long): Long {
        val containingMapping = mappings.firstOrNull { it.contains(element) }
        return containingMapping?.mapOrThrow(element) ?: element
    }
}

fun List<RangeMappingGroup>.evaluate(element: Long): Long = fold(element) { acc, mappingGroup ->
    mappingGroup.mapElement(acc)
}

fun LongRange.intersection(other: LongRange): LongRange? {
    if (!(this.contains(other.first)
            || this.contains(other.last)
            || other.contains(first)
            || other.contains(last))) {
        return null
    }

    return min(last, other.last)..min(last, other.last)
}

fun main() {
    val seedPattern = """seeds: (.+)""".toRegex()
    val mappingPattern = """(.+)-to-(.+) map:([\s\S]*?)\n\s*\n""".toRegex()
    fun parseSeeds(input: String) = seedPattern.findAll(input).first().groups[1]!!.value.split(" ")
        .map(String::trim).map(String::toLong)

    fun parseMappingGroups(input: List<String>): List<RangeMappingGroup> = mappingPattern.findAll((input + "\n\n").joinToString("\n"))
        .map { it.groups }
        .toList()
        .map { matchGroupCollection ->
            matchGroupCollection.toList().component4()!!.value.trim().split("\n")
        }.map { lines ->
            lines.map { line ->
                val (destination, source, length) = line.split(" ").map { it.toLong() }
                RangeMapping(destination = destination, source = source, length = length)
            }
        }.map { mappings ->
            RangeMappingGroup(mappings)
        }

    fun part1(input: List<String>): Int {
        // Find a way through the mappings
        val inputWithNewlines = input + "\n\n"
        val seeds = parseSeeds(inputWithNewlines.first())

        val mappingGroups = mappingPattern.findAll(inputWithNewlines.joinToString("\n"))
            .map { it.groups }
            .toList()
            .map { matchGroupCollection ->
                matchGroupCollection.toList().component4()!!.value.trim().split("\n")
            }.map { lines ->
                lines.map { line ->
                    val (destination, source, length) = line.split(" ").map { it.toLong() }
                    RangeMapping(destination = destination, source = source, length = length)
                }
            }.map { mappings ->
                RangeMappingGroup(mappings)
            }


        val locations = seeds.map { mappingGroups.evaluate(it) }

        return locations.min().toInt()
    }

    fun part2(input: List<String>): Int {
        val inputWithNewlines = input + "\n\n"
        val seeds = parseSeeds(inputWithNewlines.first())
        val seedRanges = seeds.chunked(2).map { (firstSeed, length) ->
            firstSeed..<(firstSeed + length)
        }

        val mappingGroups = mappingPattern.findAll(inputWithNewlines.joinToString("\n"))
            .map { it.groups }
            .toList()
            .map { matchGroupCollection ->
                matchGroupCollection.toList().component4()!!.value.trim().split("\n")
            }.map { lines ->
                lines.map { line ->
                    val (destination, source, length) = line.split(" ").map { it.toLong() }
                    RangeMapping(destination = destination, source = source, length = length)
                }
            }.map { mappings ->
                RangeMappingGroup(mappings)
            }


        val locations = seeds.map { mappingGroups.evaluate(it) }

        return locations.min().toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35)
//    check(part2(testInput) == 30)

    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()
}
