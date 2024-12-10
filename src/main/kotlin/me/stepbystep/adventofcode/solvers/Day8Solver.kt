package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver
import me.stepbystep.adventofcode.utils.calculateGCD

class Day8Solver : DaySolver {
    override fun solveBasic(inputLines: List<String>): Int {
        val placedAntiNodes = hashSetOf<Vec2>()
        val map = inputLines.parseMap()

        fun tryAdd(vec: Vec2) {
            if (vec.x in 0 until map.width && vec.y in 0 until map.height) {
                placedAntiNodes += vec
            }
        }

        for ((ch, firstAntennas) in map.antennas) {
            for (firstAntenna in firstAntennas) {
                for (secondAntenna in map.antennas[ch]!!) {
                    if (firstAntenna == secondAntenna) continue

                    val diff = secondAntenna - firstAntenna
                    tryAdd(secondAntenna + diff)
                    tryAdd(firstAntenna - diff)
                    if (diff.x % 3 == 0 && diff.y % 3 == 0) {
                        val thirdDiff = diff / 3
                        tryAdd(firstAntenna + thirdDiff)
                        tryAdd(secondAntenna - thirdDiff)
                    }
                }
            }
        }

        return placedAntiNodes.size
    }

    override fun solveHard(inputLines: List<String>): Int {
        val placedAntiNodes = hashSetOf<Vec2>()
        val map = inputLines.parseMap()

        fun tryAdd(vec: Vec2): Boolean {
            val isAdded = vec.x in 0 until map.width && vec.y in 0 until map.height
            if (isAdded) {
                placedAntiNodes += vec
            }

            return isAdded
        }

        for ((ch, firstAntennas) in map.antennas) {
            for (firstAntenna in firstAntennas) {
                for (secondAntenna in map.antennas[ch]!!) {
                    if (firstAntenna == secondAntenna) continue

                    val rawDiff = secondAntenna - firstAntenna
                    val divisor = calculateGCD(rawDiff.x, rawDiff.y)
                    val diff = rawDiff / divisor

                    var currentAntenna = firstAntenna
                    while (tryAdd(currentAntenna)) {
                        currentAntenna -= diff
                    }

                    currentAntenna = firstAntenna
                    while (tryAdd(currentAntenna)) {
                        currentAntenna += diff
                    }
                }
            }
        }

        return placedAntiNodes.size
    }

    private fun List<String>.parseMap(): CityMap {
        return CityMap(
            width = first().length,
            height = size,
            antennas = this
                .flatMapIndexed { y, line ->
                    line.mapIndexedNotNull { x, ch ->
                        if (ch != '.') Pair(ch, Vec2(x, y)) else null
                    }
                }
                .groupBy { it.first }
                .mapValues { (_, value) -> value.map { it.second } },
        )
    }

    private data class CityMap(
        val width: Int,
        val height: Int,
        val antennas: Map<Char, List<Vec2>>,
    )

    private data class Vec2(val x: Int, val y: Int) {
        operator fun minus(other: Vec2) = Vec2(x = x - other.x, y = y - other.y)
        operator fun plus(other: Vec2) = Vec2(x = x + other.x, y = y + other.y)
        operator fun div(num: Int) = Vec2(x = x / num, y = y / num)
    }
}
