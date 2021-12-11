package day9

import java.io.File
import kotlin.collections.ArrayList

val input = File("src/main/kotlin/day9/input").readLines()
val grid = Array(input[0].length) { Array(input.size) { Node(-1, -1, -1) } }

fun main() {
    for (y in input.indices) {
        for (x in input[0].indices) {
            grid[x][y] = Node(x, y, input[y][x].digitToInt())
        }
    }

    val basins = ArrayList<Basin>()
    for (y in input.indices) {
        for (x in input[0].indices) {
            val center = getVal(y, x)
            if (getVal(y, x - 1) <= center) continue
            if (getVal(y - 1, x) <= center) continue
            if (getVal(y, x + 1) <= center) continue
            if (getVal(y + 1, x) <= center) continue
            basins.add(Basin(grid[x][y]))
        }
    }

    val sizes = Array(basins.size) { basins[it].getSize() }
    sizes.sort()
    sizes.reverse()
    var total = 1
    for (i in 0 .. 2) {
        total *= sizes[i]
    }
    println(total)
}

class Basin(center: Node) {

    private var openNodes = ArrayList<Node>()
    private val closedNodes = ArrayList<Node>()

    init {
        openNodes.add(center)
        while (true) {
            val edgeNodes = ArrayList<Node>()
            for (n in openNodes) {
                addNeighborsTo(n, edgeNodes)
            }
            closedNodes.addAll(openNodes)
            if (edgeNodes.size == 0) break
            openNodes = edgeNodes
        }
    }

    fun getSize(): Int {
        return closedNodes.size
    }

    private fun addNeighborsTo(node: Node, al: ArrayList<Node>) {
        val neighbors = node.getNeighbors()
        for (n in neighbors) {
            if (
                n != null &&
                !openNodes.contains(n) &&
                !closedNodes.contains(n) &&
                !al.contains(n) &&
                n.v != 9
            ) {
                al.add(n)
            }
        }
    }
}

data class Node(val x: Int, val y: Int, val v: Int) {

    fun getNeighbors(): Array<Node?> {
        return arrayOf(
            getOrNull(x - 1, y),
            getOrNull(x, y - 1),
            getOrNull(x + 1, y),
            getOrNull(x, y + 1)
        )
    }

    private fun getOrNull(x: Int, y: Int): Node? {
        return try {
            grid[x][y]
        } catch (ex: ArrayIndexOutOfBoundsException) {
            null
        }
    }

}

fun getVal(y: Int, x: Int): Int {
    return try {
        input[y][x].digitToInt()
    } catch (ex: IndexOutOfBoundsException) {
        10
    }
}

fun printVectorArray(arr: ArrayList<Array<Int>>) {
    print("[")
    for (v in arr) {
        print("(${v[1]}, ${v[0]}),")
    }
    print("]")
}