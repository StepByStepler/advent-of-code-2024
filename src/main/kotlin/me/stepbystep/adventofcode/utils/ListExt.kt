package me.stepbystep.adventofcode.utils

fun <T> List<List<T>>.transpose(): List<List<T>> {
    if (isEmpty()) return this

    return List(first().size) { j ->
        List(size) { i ->
            this[i][j]
        }
    }
}

fun List<String>.toIntLists(): List<List<Int>> =
    map { line -> line.split("\\s+".toRegex()).map { it.toInt() } }
