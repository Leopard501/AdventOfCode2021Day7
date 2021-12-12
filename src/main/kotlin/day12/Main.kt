package day12

import java.io.File

val caveSystem = CaveSystem(File("src/main/kotlin/day12/input").readLines())

fun main() {
    println(caveSystem.toString())
}

class CaveSystem(input: List<String>) {

    private val caves = ArrayList<Cave>()

    init {
        for (connections in input) {
            val ends = connections.split("-")
            val endCaves = Array(2) { Cave("") }
            for (i in ends.indices) {
                var c = Cave.getCaveFromString(caves, ends[i])
                if (c == null) {
                    c = Cave(ends[i])
                    caves += c
                }
                endCaves[i] = c
            }
            endCaves[0].addConnection(endCaves[1])
            endCaves[1].addConnection(endCaves[0])
        }
    }

    override fun toString(): String {
        var returnVar = "CaveSystem: ["
        caves.forEach { returnVar += "\n    $it" }
        returnVar += "\n]"
        return returnVar
    }

}

class Cave(val name: String) {

    val isLarge = name.lowercase() != name
    val connectedCaves = ArrayList<Cave>()

    fun addConnection(other: Cave) {
        if (connectedCaves.contains(other)) return
        connectedCaves.add(other)
    }

    companion object {
        @JvmStatic
        fun getCaveFromString(caveArray: ArrayList<Cave>, caveName: String): Cave? {
            caveArray.forEach { if (it.name == caveName) return it }
            return null
        }
    }

    override fun toString(): String {
        var returnVar = "Cave: {name=$name, isLarge=$isLarge, connectedCaves=["
        for (i in 0 .. connectedCaves.size - 2) {
            returnVar += "${connectedCaves[i].name}, "
        }
        returnVar += "${connectedCaves.last().name}]}"
        return returnVar
    }

}