package me.stepbystep.adventofcode.solvers

import me.stepbystep.adventofcode.common.DaySolver

class Day6Solver : DaySolver {
    override fun solveBasic(inputLines: List<String>): Int {
        val visitedPositions = getWalkResult(
            map = inputLines,
            startPosition = inputLines.parseStartPosition(),
            startDirection = Direction.North,
        ) ?: error("Stepbro, I'm stuck in a loop!")

        return visitedPositions.size
    }

    override fun solveHard(inputLines: List<String>): Int {
        val startPosition = inputLines.parseStartPosition()
        val visitedPositions = getWalkResult(
            map = inputLines,
            startPosition = startPosition,
            startDirection = Direction.North,
        ) ?: error("Stepbro, I'm stuck in a loop!")

        val obstacleCandidates = visitedPositions - startPosition
        return obstacleCandidates.count { obstaclePos ->
            val modifiedLines = inputLines.mapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    if (Vec2(x, y) == obstaclePos) {
                        '#'
                    } else {
                        char
                    }
                }.joinToString(separator = "")
            }

            getWalkResult(
                map = modifiedLines,
                startPosition = startPosition,
                startDirection = Direction.North,
            ) == null
        }
    }

    private fun List<String>.parseStartPosition(): Vec2 =
        this
            .withIndex()
            .firstNotNullOf { (index, line) ->
                val x = line.indexOfFirst { it == '^' }
                if (x == -1) {
                    null
                } else {
                    x to index
                }
            }
            .let { (x, y) -> Vec2(x, y) }

    private fun getWalkResult(
        map: List<String>,
        startPosition: Vec2,
        startDirection: Direction,
    ): Set<Vec2>? {
        val visitedPos = mutableSetOf<VisitedPoint>()
        var position = startPosition
        var direction = startDirection
        while (position.y in map.indices && position.x in map[position.y].indices) {
            val visitedPoint = VisitedPoint(position, direction)
            if (visitedPoint in visitedPos) {
                return null
            }

            visitedPos += visitedPoint
            val unsafeNextPos = position + direction
            if (map.getOrNull(unsafeNextPos.y)?.getOrNull(unsafeNextPos.x) == '#') {
                direction = direction.next
                val visitedPoint2 = VisitedPoint(position, direction)
                if (visitedPoint2 in visitedPos) {
                    return null
                }
                visitedPos += visitedPoint2
                position += direction
            } else {
                position = unsafeNextPos
            }
        }

        return visitedPos.map { it.pos }.toSet()
    }
}

private data class Vec2(val x: Int, val y: Int)
private data class VisitedPoint(val pos: Vec2, val direction: Direction)

private enum class Direction(
    val dx: Int,
    val dy: Int,
) {
    North(0, -1),
    East(1, 0),
    South(0, 1),
    West(-1, 0),
    ;

    val next: Direction get() = Direction.entries[(ordinal + 1) % Direction.entries.size]
}

private operator fun Vec2.plus(direction: Direction) = Vec2(x + direction.dx, y + direction.dy)
