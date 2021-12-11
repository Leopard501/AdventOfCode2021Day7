package day9

import java.io.File
import kotlin.collections.ArrayList

val input = File("src/main/kotlin/day9/test").readLines()

fun main() {
    val riskLevels = ArrayList<Array<Int>>()
    for (y in input.indices) {
        for (x in input[0].indices) {
            val center = getVal(y, x)
            if (getVal(y, x - 1) <= center) continue
            if (getVal(y - 1, x) <= center) continue
            if (getVal(y, x + 1) <= center) continue
            if (getVal(y + 1, x) <= center) continue
            riskLevels.add(arrayOf(y, x))
        }
    }

    printVectorArray(riskLevels)
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