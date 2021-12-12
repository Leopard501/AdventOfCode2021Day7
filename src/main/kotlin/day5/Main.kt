package day5

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val input = File("src/main/kotlin/day5/input").readLines()
val lines = Array(input.size) { Line(input[it]) }

fun main() {
    val grid = Grid()
    lines.forEach { grid.addLine(it) }
    grid.printGrid()
    println(grid.countOverlaps())
}

class Grid {

    private val grid = Array(getMaxX()) { Array(getMaxY()) { 0 } }

    fun addLine(line: Line) {
        if (line.y1 == line.y2) {
            for (x in min(line.x1, line.x2) .. max(line.x1, line.x2)) {
                grid[x][line.y1]++
            }
        } else if (line.x1 == line.x2) {
            for (y in min(line.y1, line.y2) .. max(line.y1, line.y2)) {
                grid[line.x1][y]++
            }
        } else {
            for (d in 0 .. abs(line.x1 - line.x2)) {
                grid[
                        line.x1 + d * -((line.x1 - line.x2) / abs(line.x1 - line.x2))
                ][
                        line.y1 + d * -((line.y1 - line.y2) / abs(line.y1 - line.y2))
                ]++
            }
        }
    }

    fun countOverlaps(): Int {
        var overlaps = 0
        grid.forEach { a -> a.forEach { i -> if (i > 1) overlaps++ } }
        return overlaps
    }

    fun printGrid() {
        for (y in grid[0].indices) {
            for (x in grid.indices) {
                if (grid[x][y] == 0) print(".")
                else print(grid[x][y])
            }; print("\n")
        }
    }

}

class Line(input: String) {

    val x1: Int
    val y1: Int
    val x2: Int
    val y2: Int

    init {
        val points = input.split(" -> ")

        val p1 = points[0].split(",")
        val p2 = points[1].split(",")

        x1 = p1[0].toInt()
        y1 = p1[1].toInt()
        x2 = p2[0].toInt()
        y2 = p2[1].toInt()
    }

}

fun getMaxX(): Int {
    var max = 0
    for (l in lines) {
        max = max.coerceAtLeast(l.x1 + 1)
        max = max.coerceAtLeast(l.x2 + 1)
    }
    return max
}

fun getMaxY(): Int {
    var max = 0
    for (l in lines) {
        max = max.coerceAtLeast(l.y1 + 1)
        max = max.coerceAtLeast(l.y2 + 1)
    }
    return max
}