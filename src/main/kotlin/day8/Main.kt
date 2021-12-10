package day8

import java.io.File

const val SMALL_INPUT = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
val input = File("src/main/resources/day8").readLines()

fun main() {
    val lines = arrayOfNulls<Line>(input.size)
    for (i in input.indices) lines[i] = Line(input[i])
    val digitCounts = arrayOfNulls<Array<Int>>(lines.size)
    for (i in lines.indices) digitCounts[i] = lines[i]?.let { getDigitCounts(it.output) }
    val finalDigitCounts = combineDigitCounts(digitCounts)
    printDigitCounts(finalDigitCounts)
    println(finalDigitCounts.sum())
}

data class Line(private val input: String) {

    val output : Array<String>
    val signalPatterns : Array<String>

    init {
        val splitInput = input.split(" | ")

        signalPatterns = splitInput[0].split(" ").toTypedArray()
        output = splitInput[1].split(" ").toTypedArray()
    }

}

fun getDigitCounts(strings : Array<String>) : Array<Int> {
    val digitCounts = Array(10) { 0 }
    for (i in strings.indices) {
        val digit = getDigitFromString(strings[i])
        if (digit == -1) continue
        digitCounts[digit]++
    }
    return digitCounts
}

fun getDigitFromString(s : String) : Int {
    when (s.length) {
        2 -> return 1
        3 -> return 7
        4 -> return 4
        7 -> return 8
    }
    return -1
}

fun combineDigitCounts(digitCounts : Array<Array<Int>?>) : Array<Int> {
    val total = Array(10) { 0 }
    for (dc in digitCounts) {
        if (dc != null) {
            for (i in dc.indices) {
                total[i] += dc[i]
            }
        }
    }
    return total
}

fun printDigitCounts(digitCounts : Array<Int>) {
    var s = "["
    for (i in 0 .. digitCounts.size - 2) {
        s += "$i: ${digitCounts[i]}, "
    }
    s += "${digitCounts.size - 1}: ${digitCounts[digitCounts.size - 1]}]"
    println(s)
}