package day7

import java.io.File
import kotlin.math.abs

val input = File("src/main/resources/day7").readText()
val smallInput = arrayOf(16,1,2,0,4,2,7,1,2,14)

fun main() {
    val inputs = input.split(",")
    val parsed = inputs.map { it.toInt() }

    val crabs = parsed

    val max = crabs.maxOrNull()

    var smallestTotal : Long? = null
    var bestPosition = 0
    for (position in 0 .. max!!) {
        var total = 0L
        for (crab in crabs) {
            total += findFuelCost(crab, position)
        }
        if (smallestTotal == null || smallestTotal > total) {
            smallestTotal = total
            bestPosition = position
        }
    }

    println("The best position is $bestPosition, with a total of $smallestTotal")
}

fun findFuelCost(crab : Int, position: Int) : Int {
    val dif = abs(crab - position)
    return dif * (dif + 1) / 2
}