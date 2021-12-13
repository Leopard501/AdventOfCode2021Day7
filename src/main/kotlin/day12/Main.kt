package day12

import sun.misc.Queue
import java.io.File
import kotlin.collections.ArrayList

val caveSystem = CaveSystem(File("src/main/kotlin/day12/test1").readLines())

fun main() {
    println(caveSystem.toString())
    val pathTree = Tree(caveSystem)
    println(pathTree.ends)
    println(pathTree.ends.size)
}

class Tree(caveSystem: CaveSystem) {

    private val root = Node(caveSystem.start, null)

    val ends = ArrayList<Node>()

    init {
        //this is breadth-first search
        val searchQueue = Queue<Node>()
        searchQueue.enqueue(root)
        while (!searchQueue.isEmpty) {
            val top = searchQueue.dequeue()
            if (top.parent != null) top.parent.children.add(top)
            if (top.cave.name == "end") {
                ends.add(top)
                continue
            }
            for (c in top.cave.connectedCaves) {
                if (c.name == "start") continue
                if (
                    c.isLarge ||
                    top.hasRepeatedAncestor() && top.hasAsAncestor(c) < 2 ||
                    top.hasAsAncestor(c) < 1
                ) {
                    searchQueue.enqueue(Node(c, top))
                }
            }
        }
    }

    class Node(val cave: Cave, val parent: Node?) {

        val children = ArrayList<Node>()

        fun hasRepeatedAncestor(): Boolean {
            val ancestors = ArrayList<Node>()
            var n = this
            while (n.parent != null) {
                n = n.parent!!
                ancestors.add(n)
            }
            ancestors.sortBy { it.cave.name }
            for (i in 1 until ancestors.size) {
                if (ancestors[i].cave.name == ancestors[i-1].cave.name) return true
            }
            return false
        }

        fun hasAsAncestor(cave: Cave): Int {
            //this will miss if this is an ancestor to cave,
            //but it shouldn't be possible for cave to be its own ancestor
            var count = 0
            var n = this
            while (n.parent != null) {
                n = n.parent!!
                if (n.cave == cave) count++
            }
            return count
        }

        override fun toString(): String {
            var returnVar = "Node: {name=${cave.name}, ancestors=["
            var p = parent
            while (p != null) {
                returnVar += "${p.cave.name},"
                p = p.parent
            }
            returnVar += "]}"
            return returnVar
        }
    }
}

class CaveSystem(input: List<String>) {

    private val caves = ArrayList<Cave>()

    lateinit var start: Cave

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
                if (c.name == "start") start = c
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