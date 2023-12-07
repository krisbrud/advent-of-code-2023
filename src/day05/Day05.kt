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

    fun mapRange(inputRange: LongRange): LongRange? = sourceRange.intersection(inputRange)?.let { intersectionRange ->
        mapOrThrow(intersectionRange.first)..mapOrThrow(intersectionRange.last)
    }
}

data class RangeMappingGroup(
    val mappings: List<RangeMapping>
) {
    fun mapElement(element: Long): Long {
        val containingMapping = mappings.firstOrNull { it.contains(element) }
        return containingMapping?.mapOrThrow(element) ?: element
    }

//    fun mapRange(inputRange: LongRange): List<LongRange> {
//        val sourceRangesWithMappings = mappings.filter { mapping ->
//            inputRange.intersection(mapping.sourceRange) != null
//        }.sortedBy { it.sourceRange.first }
//        val feedThrough = when(sourceRangesWithMappings.size) {
//            0 -> inputRange
//            else -> {
//                sourceRangesWithMappings.windowed(2).map { (prev, current) ->
//                    val prevlast = prev.sourceRange.last
//                    val currentFirst = current.sourceRange.first
//                    if (prevlast < currentFirst) {
//                        (prevlast)..(currentFirst)
//                    } else null
//                }.filterNotNull()
//            }
//        }
//
//
//        val mappedRanges = sourceRangesWithMappings.mapNotNull { rangeMapping -> rangeMapping.mapRange(inputRange) }
//        return mappedRanges + feedThrough
//    }
}

fun List<RangeMappingGroup>.evaluate(element: Long): Long = fold(element) { acc, mappingGroup ->
    mappingGroup.mapElement(acc)
}

//fun List<RangeMappingGroup>.evaluate(seedRange: LongRange): List<LongRange> = fold(listOf(seedRange)) { inputRanges: List<LongRange>, mappingGroup: RangeMappingGroup ->
//    inputRanges.map { inputRange -> mappingGroup.mapRange(inputRange) }.flatten()
//}

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
    fun String.parseSeeds(): List<Long> = seedPattern.findAll(this).first().groups[1]!!.value.split(" ")
        .map(String::trim).map(String::toLong)

    fun List<String>.parseMappingGroups(): List<RangeMappingGroup> = mappingPattern.findAll((this + "\n\n").joinToString("\n"))
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
        val seeds = input.first().parseSeeds()

        val mappingGroups = input.parseMappingGroups()

        val locations = seeds.map { mappingGroups.evaluate(it) }

        return locations.min().toInt()
    }

    fun part2(input: List<String>): Int {
        val seeds = input.first().parseSeeds()
        val seedRanges: List<LongRange> = seeds.chunked(2).map { (firstSeed, length) ->
            firstSeed..<(firstSeed + length)
        }

        val mappingGroups = input.parseMappingGroups()

        val minLocation = seedRanges.map { seedRange -> seedRange.minOf { seed -> mappingGroups.evaluate(seed) } }.min()

        return minLocation.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 46)

    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()
}
