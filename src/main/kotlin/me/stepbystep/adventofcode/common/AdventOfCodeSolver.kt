package me.stepbystep.adventofcode.common

import me.stepbystep.adventofcode.solvers.Day1Solver
import me.stepbystep.adventofcode.solvers.Day2Solver
import me.stepbystep.adventofcode.solvers.Day3Solver

class AdventOfCodeSolver {
    private val allSolvers: List<DaySolver> = listOf(
        Day1Solver(),
        Day2Solver(),
        Day3Solver(),
    )

    fun solveTask(day: Int, complexity: TaskComplexity): Int {
        val inputStream = javaClass.getResourceAsStream("/inputs/day$day.txt")
            ?: error("Input file for for day $day not found")
        val input = inputStream.bufferedReader().readLines()
        val solver = allSolvers[day - 1]
        return when (complexity) {
            TaskComplexity.Basic -> solver.solveBasic(input)
            TaskComplexity.Hard -> solver.solveHard(input)
        }
    }
}
