package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver

class Day3Solver : DaySolver {
    private val mulRegex = Regex("mul\\((\\d+),(\\d+)\\)")
    private val doRegex = Regex("do\\(\\)")
    private val dontRegex = Regex("don't\\(\\)")

    override fun solveBasic(inputLines: List<String>): Int {
        return inputLines
            .sumOf { line ->
                mulRegex
                    .findAll(line)
                    .sumOf { matchResult ->
                        val (a, b) = matchResult.destructured
                        a.toInt() * b.toInt()
                    }
            }
    }

    override fun solveHard(inputLines: List<String>): Int {
        val input = inputLines.joinToString(separator = "")
        val doStarts = doRegex.findAll(input).map { it.range.first }.toList()
        val dontStarts = dontRegex.findAll(input).map { it.range.first }.toList()

        return mulRegex
            .findAll(input)
            .sumOf inner@ { matchResult ->
                val (a, b) = matchResult.destructured
                val mulResult = a.toInt() * b.toInt()
                val index = matchResult.range.first
                val afterDontIndex = dontStarts.findLast { index > it } ?: return@inner mulResult
                val beforeDoIndex = doStarts.find { it in afterDontIndex..index }
                if (beforeDoIndex == null) {
                    0
                } else {
                    mulResult
                }
            }
    }
}
