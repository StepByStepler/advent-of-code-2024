package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver
import kotlin.math.pow

class Day7Solver : DaySolver {
    private val lineRegex = "(\\d+): (.+)".toRegex()

    override fun solveBasic(inputLines: List<String>): Int {
        return inputLines
            .parseEquations()
            .sumOf { equation ->
                if (equation.isValid(equation.operands.first(), 1, false)) {
                    equation.result
                } else {
                    0
                }
            }
            .also { println(it) } // don't want to convert all return types to Long yet
            .toInt()
    }

    override fun solveHard(inputLines: List<String>): Int {
        return inputLines
            .parseEquations()
            .sumOf { equation ->
                if (equation.isValid(equation.operands.first(), 1, true)) {
                    equation.result
                } else {
                    0
                }
            }
            .also { println(it) } // don't want to convert all return types to Long yet
            .toInt()
    }

    private fun List<String>.parseEquations(): List<Equation> = map { line ->
        val (result, operands) = lineRegex.matchEntire(line)!!.destructured
        Equation(
            result = result.toLong(),
            operands = operands.split(" ").map { it.toLong() },
        )
    }

    private fun Equation.isValid(
        currentResult: Long,
        index: Int,
        allowConcatenation: Boolean,
    ): Boolean {
        return if (index == operands.size) {
            currentResult == result
        } else {
            isValid(
                currentResult = currentResult + operands[index],
                index = index + 1,
                allowConcatenation = allowConcatenation,
            ) || isValid(
                currentResult = currentResult * operands[index],
                index = index + 1,
                allowConcatenation = allowConcatenation,
            ) || (allowConcatenation && isValid(
                currentResult = (currentResult * 10f.pow(operands[index].toString().length).toLong()) + operands[index],
                index = index + 1,
                allowConcatenation = true,
            ))
        }
    }

    private data class Equation(
        val result: Long,
        val operands: List<Long>,
    )
}
