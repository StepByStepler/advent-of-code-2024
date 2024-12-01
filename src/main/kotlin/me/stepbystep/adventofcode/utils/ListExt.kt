package me.stepbystep.adventofcode.utils

fun <T> List<List<T>>.transpose(): List<List<T>> {
    if (isEmpty()) return this

    return List(first().size) { j ->
        List(size) { i ->
            this[i][j]
        }
    }
}
