import java.io.File
import kotlin.math.abs

val input = File("src/main/resources/input").readText()
val smallInput = arrayOf(16,1,2,0,4,2,7,1,2,14)

fun main() {
    val inputs = input.split(",")
    val parsed = inputs.map { it.toInt() }

    val i = parsed

    val max = i.maxOrNull()

    var smallestTotal : Long? = null
    var bestPosition = 0
    for (n in 0 .. max!!) {
        var total = 0L
        for (j in i) {
            val dif = abs(j - n)
            total += dif
        }
        if (smallestTotal == null || smallestTotal > total) {
            smallestTotal = total
            bestPosition = n
        }
    }

    println("The best position is $bestPosition, with a total of $smallestTotal")
}