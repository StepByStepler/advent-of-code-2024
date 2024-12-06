package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver
import kotlin.jvm.internal.Ref.IntRef

class Day4Solver : DaySolver {
    override fun solveBasic(inputLines: List<String>): Int {
        val result = IntRef()
        for (y in inputLines.indices) {
            for (x in inputLines[y].indices) {
                for (dx in -1..1) {
                    for (dy in -1..1) {
                        if (dx != 0 || dy != 0) {
                            countWord("XMAS", 0, inputLines, x, y, dx, dy, result)
                        }
                    }
                }
            }
        }
        return result.element
    }

    override fun solveHard(inputLines: List<String>): Int {
        var result = 0
        for (y in inputLines.indices) {
            for (x in inputLines[y].indices) {
                if (inputLines[y][x] != 'A') continue

                val leftTop = inputLines.getOrNull(y - 1)?.getOrNull(x - 1) ?: continue
                val leftBottom = inputLines.getOrNull(y + 1)?.getOrNull(x - 1) ?: continue
                val rightTop = inputLines.getOrNull(y - 1)?.getOrNull(x + 1) ?: continue
                val rightBottom = inputLines.getOrNull(y + 1)?.getOrNull(x + 1) ?: continue
                if (checkMS(leftTop, rightBottom) && checkMS(leftBottom, rightTop)) {
                    result++
                }
            }
        }

        return result
    }

    private fun countWord(
        word: String,
        charIndex: Int,
        input: List<String>,
        x: Int,
        y: Int,
        dx: Int,
        dy: Int,
        result: IntRef,
    ) {
        if (y !in input.indices) return
        if (x !in input[y].indices) return
        if (input[y][x] != word[charIndex]) return
        if (charIndex == word.lastIndex) {
            result.element++
            return
        }

        countWord(
            word = word,
            charIndex = charIndex + 1,
            input = input,
            x = x + dx,
            y = y + dy,
            dx = dx,
            dy = dy,
            result = result,
        )
    }

    private fun checkMS(first: Char, second: Char): Boolean =
        (first == 'M' && second == 'S') || (first == 'S' && second == 'M')
}
