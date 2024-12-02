package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver
import me.stepbystep.adventofcode.utils.toIntLists
import me.stepbystep.adventofcode.utils.transpose
import kotlin.math.abs

class Day1Solver : DaySolver {
    override fun solveBasic(inputLines: List<String>): Int {
        return inputLines
            .toIntLists()
            .transpose()
            .map { it.sorted() }
            .let { (left, right) ->
                left.indices.sumOf { abs(left[it] - right[it]) }
            }
    }

    override fun solveHard(inputLines: List<String>): Int {
        return inputLines
            .toIntLists()
            .transpose()
            .let { (left, right) ->
                val amounts = right.groupingBy { it }.eachCount()
                left.sumOf { num -> num * (amounts[num] ?: 0) }
            }
    }
}
