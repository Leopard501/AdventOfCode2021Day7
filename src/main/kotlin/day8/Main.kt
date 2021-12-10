package day8

import java.io.File

val input = File("src/main/resources/day8").readLines()

fun main() {
    val lines = arrayOfNulls<Line>(input.size)
    for (i in input.indices) lines[i] = Line(input[i])
    val digitCounts = arrayOfNulls<Array<Int>>(lines.size)
    for (i in lines.indices) digitCounts[i] = lines[i]?.getDigitCount()
    var sum = 0
    for (l in lines) sum += l?.combineDigits()!!
    println(sum)
}

fun containsSameChars(a: String, b: String): Boolean {
    if (a.length != b.length) return false
    val aChar = a.toCharArray()
    val bChar = b.toCharArray()
    aChar.sort()
    bChar.sort()
    return aChar.contentEquals(bChar)
}

fun printDigitCounts(digitCounts: Array<Int>) {
    var s = "["
    for (i in 0 .. digitCounts.size - 2) {
        s += "$i: ${digitCounts[i]}, "
    }
    s += "${digitCounts.size - 1}: ${digitCounts[digitCounts.size - 1]}]"
    println(s)
}

data class Line(private val input: String) {

    private val output: Array<String>
    private val signalPatterns: Array<String>
    private val digits = Array(10) { "" }
    private val segments = Array(7) { "" }

    init {
        val splitInput = input.split(" | ")

        signalPatterns = splitInput[0].split(" ").toTypedArray()
        output = splitInput[1].split(" ").toTypedArray()

        //find easy digits

        for (s in signalPatterns) {
            val d = getEasyDigit(s)
            if (d == -1) continue
            digits[d] = s
        }

        //find segments that can be inferred from easy digits

        segments[2] = digits[1]
        segments[5] = digits[1]

        var sevenRemaining = digits[7]
        sevenRemaining = sevenRemaining.replace(digits[1][0].toString(), "")
        sevenRemaining = sevenRemaining.replace(digits[1][1].toString(), "")
        segments[0] = sevenRemaining

        var fourRemaining = digits[4]
        fourRemaining = fourRemaining.replace(digits[1][0].toString(), "")
        fourRemaining = fourRemaining.replace(digits[1][1].toString(), "")
        segments[1] = fourRemaining
        segments[3] = fourRemaining

        var eightRemaining = digits[8]
        eightRemaining = eightRemaining.replace(digits[1][0].toString(), "")
        eightRemaining = eightRemaining.replace(digits[1][1].toString(), "")
        eightRemaining = eightRemaining.replace(segments[1][0].toString(), "")
        eightRemaining = eightRemaining.replace(segments[1][1].toString(), "")
        eightRemaining = eightRemaining.replace(sevenRemaining, "")
        segments[4] = eightRemaining
        segments[6] = eightRemaining

        //find digits with six segments

        val sixes = getDigitsOfLength(6)
        digits[0] = getSixDigit(segments[3], sixes)
        digits[6] = getSixDigit(segments[2], sixes)
        digits[9] = getSixDigit(segments[4], sixes)

        //find segments that can be inferred from that

        var currentSegment = segments[3]
        segments[3] = getSixSegment(segments[3], digits[0])
        segments[1] = currentSegment.replace(segments[3], "")
        currentSegment = segments[2]
        segments[2] = getSixSegment(segments[2], digits[6])
        segments[5] = currentSegment.replace(segments[2], "")
        currentSegment = segments[4]
        segments[4] = getSixSegment(segments[4], digits[9])
        segments[6] = currentSegment.replace(segments[4], "")

        //find final digits

        val fives = getDigitsOfLength(5)
        for (s in fives) {
            if (digitMatchesSegmentCombo(s, arrayOf(0, 2, 3, 4, 6))) digits[2] = s
            if (digitMatchesSegmentCombo(s, arrayOf(0, 2, 3, 5, 6))) digits[3] = s
            if (digitMatchesSegmentCombo(s, arrayOf(0, 1, 3, 5, 6))) digits[5] = s
        }
    }

    fun getDigitCount(): Array<Int> {
        val digitCounts = Array(10) { 0 }
        for (s in output) {
            for (i in digits.indices) {
                if (containsSameChars(s, digits[i])) {
                    digitCounts[i]++
                    break
                }
            }
        }
        return digitCounts
    }

    fun combineDigits(): Int {
        var out = ""
        for (s in output) {
            out += digitStringToInt(s)
        }
        return out.toInt()
    }

    private fun digitStringToInt(digit: String): Int {
        for (i in digits.indices) {
            if (containsSameChars(digit, digits[i])) return i
        }
        return -1
    }

    private fun getEasyDigit(s: String): Int {
        when (s.length) {
            2 -> return 1
            3 -> return 7
            4 -> return 4
            //5 -> 2, 3 or 5
            //6 -> 0, 6 or 9
            7 -> return 8
        }
        return -1
    }

    private fun digitMatchesSegmentCombo(digit: String, segmentIds: Array<Int>): Boolean {
        for (i in segmentIds) {
            if (!digit.contains(segments[i])) return false
        }
        return true
    }

    private fun getSixSegment(missingSegment: String, digit: String): String {
        for (c in missingSegment) {
            if (!digit.contains(c)) return c.toString()
        }
        return ""
    }

    private fun getSixDigit(missingSegment: String, sixes: Array<String>): String {
        for (s in sixes) {
            if (!(s.contains(missingSegment[0]) && s.contains(missingSegment[1]))) {
                return s
            }
        }
        return ""
    }

    private fun getDigitsOfLength(length: Int): Array<String> {
        val returnVal = Array(3) { "" }
        var i = 0
        for (s in signalPatterns) {
            if (s.length == length) {
                returnVal[i] = s
                i++
            }
            if (i == returnVal.size) break
        }
        return returnVal
    }

}