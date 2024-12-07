package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver

class Day5Solver : DaySolver {
    override fun solveBasic(inputLines: List<String>): Int {
        return inputLines
            .parseUpdates()
            .filterCorrect(inputLines.parseDescRules())
            .sumOf { nums -> nums[nums.size / 2] }
    }

    override fun solveHard(inputLines: List<String>): Int {
        val descendingRules = inputLines.parseDescRules()
        val allLines = inputLines.parseUpdates()

        return (allLines - allLines.filterCorrect(descendingRules).toSet())
            .map { it.toMutableList() }
            .onEach { nums ->
                var i = 0
                outer@ while (i < nums.size) {
                    val num = nums[i]
                    val rules = descendingRules[num]
                    if (rules == null) {
                        i++
                        continue
                    }

                    for (j in (i + 1)..nums.lastIndex) {
                        val otherNum = nums[j]
                        if (otherNum in rules) {
                            nums[j] = num
                            nums[i] = otherNum
                            continue@outer
                        }
                    }

                    i++
                }
            }
            .sumOf { nums -> nums[nums.size / 2] }
    }

    private fun List<String>.parseDescRules(): Map<Int, Set<Int>> =
        this
            .subList(0, indexOf(""))
            .map { line -> line.split("|").map { it.toInt() } }
            .groupingBy { (_, end) -> end }
            .fold(
                initialValueSelector = { _, element -> setOf(element[0]) },
                operation = { _, acc, element -> acc + element[0] },
            )

    private fun List<String>.parseUpdates(): List<List<Int>> =
        this
            .subList(indexOf("") + 1, size)
            .map { line -> line.split(",").map { it.toInt() } }

    private fun List<List<Int>>.filterCorrect(descendingRules: Map<Int, Set<Int>>): List<List<Int>> =
        this
            .filter { nums ->
                for ((index, num) in nums.withIndex()) {
                    val rules = descendingRules[num] ?: continue
                    val allSatisfied = nums.subList(index + 1, nums.size).all { it !in rules }
                    if (!allSatisfied) return@filter false
                }

                true
            }
}
