package day11

import java.io.File

val octopuses = OctopusGrid(File("src/main/kotlin/day11/input").readLines())

fun main() {
    var count = 0
    while (true) {
        count++
        if (octopuses.step()) break
    }
    println(count)
}

class OctopusGrid(input: List<String>) {

    val grid: Array<Array<Octopus>>

    init {
        grid = Array(input[0].length) {
            x -> Array(input.size) {
                y -> Octopus(x, y, input[x][y].digitToInt())
            }
        }
    }

    fun step(): Boolean {
        firstStep()
        return secondStep(0, ArrayList()) == getTotal()
    }

    private fun getTotal(): Int {
        return grid.size * grid[0].size
    }

    private fun firstStep() {
        grid.forEach { a -> a.forEach { o -> o.increaseEnergy() } }
    }

    private fun secondStep(flashCount: Int, flashedOctopuses: ArrayList<Octopus>): Int {
        var didSomething = false
        var flashCountVar = flashCount
        grid.forEach { a -> a.forEach { o ->
            if (!flashedOctopuses.contains(o) && o.flash()) {
                flashCountVar++
                flashedOctopuses.add(o)
                didSomething = true
            }
        } }
        return if (didSomething) secondStep(flashCountVar, flashedOctopuses)
        else flashCountVar
    }

}

data class Octopus(val x: Int, val y: Int, var v: Int) {

    fun flash(): Boolean {
        if (v != 0) return false
        v = 0
        getNeighbors().forEach { it?.flashed() }
        return true
    }

    private fun flashed() {
        if (v == 0) return
        increaseEnergy()
    }

    fun increaseEnergy() {
        if (v < 9) v++
        else v = 0
    }

    private fun getNeighbors(): Array<Octopus?> {
        return arrayOf(
            getOrNull(x - 1, y - 1),
            getOrNull(x - 1, y),
            getOrNull(x - 1, y + 1),
            getOrNull(x, y - 1),
            getOrNull(x, y),
            getOrNull(x, y + 1),
            getOrNull(x + 1, y - 1),
            getOrNull(x + 1, y),
            getOrNull(x + 1, y + 1)
        )
    }

    private fun getOrNull(x: Int, y: Int): Octopus? {
        return try {
            octopuses.grid[x][y]
        } catch (ex: ArrayIndexOutOfBoundsException) {
            null
        }
    }

}

