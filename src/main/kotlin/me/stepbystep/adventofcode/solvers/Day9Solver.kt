package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver

class Day9Solver : DaySolver {
    override fun solveBasic(inputLines: List<String>): Int {
        return inputLines
            .parseDisk()
            .makeCompact()
            .foldIndexed(0L) { index, acc, num ->
                acc + index * (num ?: 0)
            }
            .also { println(it) }
            .toInt()
    }

    override fun solveHard(inputLines: List<String>): Int {
        return inputLines
            .parseOptimizedDisk()
            .also { it.makeCompact() }
            .sectors
            .filter { it.length > 0 }
            .fold(0 to 0L) { (sumIndex, sum), sector ->
                val newSumIndex = sumIndex + sector.length
                val sumDiff = when (sector) {
                    is Sector.Space -> 0
                    is Sector.File -> {
                        (0L until sector.length).sumOf { (it + sumIndex) * sector.index }
                    }
                }
                newSumIndex to (sum + sumDiff)
            }
            .second
            .also { println(it) }
            .toInt()
    }

    private fun List<String>.parseDisk(): List<Int?> = buildList {
        for ((index, ch) in this@parseDisk.first().withIndex()) {
            if (index % 2 == 0) {
                repeat(ch.digitToInt()) {
                    add(index / 2)
                }
            } else {
                repeat(ch.digitToInt()) {
                    add(null)
                }
            }
        }
    }

    private fun List<String>.parseOptimizedDisk(): Disk = Disk().apply {
        for ((index, ch) in this@parseOptimizedDisk.first().withIndex()) {
            if (index % 2 == 0) {
                sectors.add(Sector.File(index / 2, ch.digitToInt()))
            } else {
                sectors.add(Sector.Space(ch.digitToInt()))
            }
        }
    }

    private fun List<Int?>.makeCompact(): List<Int?> {
        val result = toMutableList()
        var start = 0
        var end = lastIndex
        while (start <= end) {
            val current = result[end]
            if (current == null) {
                end--
                continue
            }

            val candidate = result[start]
            if (candidate != null) {
                start++
                continue
            }

            result[start] = current
            result[end] = null
        }

        return result
    }

    // 5315515797220
    // 6012119035500
    private fun Disk.makeCompact() {
        var fileIndex = sectors.lastIndex
        while (fileIndex >= 0) {
            val file = sectors[fileIndex--]
            if (file !is Sector.File) continue
            if (file.wasMoved) continue

            var spaceIndex = 0
            while (spaceIndex <= fileIndex) {
                val space = sectors[spaceIndex++]
                if (space !is Sector.Space) continue
                if (space.length < file.length) continue

                sectors[fileIndex + 1] = Sector.Space(file.length)
                sectors.add(spaceIndex - 1, file)
                space.length -= file.length
                fileIndex++
                file.wasMoved = true
                break
            }
        }
    }

    private data class Disk(
        val sectors: MutableList<Sector> = mutableListOf(),
    )

    private sealed class Sector {
        abstract var length: Int

        data class File(
            val index: Int,
            override var length: Int,
            var wasMoved: Boolean = false,
        ) : Sector()

        data class Space(override var length: Int) : Sector()
    }
}
