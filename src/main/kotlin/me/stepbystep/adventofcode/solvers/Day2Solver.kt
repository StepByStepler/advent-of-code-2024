package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver
import me.stepbystep.adventofcode.utils.toIntLists

class Day2Solver : DaySolver {
    override fun solveBasic(inputLines: List<String>): Int {
        return inputLines
            .toIntLists()
            .count { it.isSafe() }
    }

    override fun solveHard(inputLines: List<String>): Int {
        return inputLines
            .toIntLists()
            .count { nums ->
                nums.isSafe() || nums.indices.any { index ->
                    nums.toMutableList().run {
                        removeAt(index)
                        isSafe()
                    }
                }
            }
    }

    private fun List<Int>.isSafe(): Boolean =
        List(size - 1) { i -> this[i + 1] - this[i] }.let { diffs ->
            diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
        }
}
