package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil
import java.util.LinkedList
import java.util.regex.Pattern

class Adv2025Day12 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 2
            //println(part1("2025/2025-12-12-sample.txt"))
            // 457
            println(part1("2025/2025-12-12.txt"))

            //
            //println(part2("2025/2025-12-12-sample.txt"))
            //
            //println(part2("2025/2025-12-12.txt"))
        }

        fun part1(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            val presents = ArrayList<Present>()
            val regions = ArrayList<Region>()
            var currentMap: ArrayList<CharArray> = ArrayList()
            lines.forEach {
                if (it.isBlank()) {
                    val parts = currentMap.sumOf { line ->
                        line.count { ch -> ch == '#' }
                    }
                    presents.add(Present(currentMap, parts))
                    currentMap = ArrayList()
                } else if (it.contains("x")) {
                    val x = it.substring(0, it.indexOf("x")).toInt()
                    val y = it.substring(it.indexOf("x") + 1, it.indexOf(":")).toInt()
                    val prs = it.substring(it.indexOf(":") + 1)
                        .split(" ")
                        .filter { str -> !str.isBlank() }
                        .map { str -> str.toInt() }
                        .toList()
                    regions.add(Region(x, y, prs))
                } else if (!it.contains(":")) {
                    currentMap.add(it.toCharArray())
                }
            }
            presents.forEach { it.print() }
            println(regions)
            println("Total regions: ${regions.size}")
            var res = 0L
            val percents = ArrayList<Double>()
            regions.forEach { region ->
                var sum = 0L
                for (i in 0 until region.presents.size) {
                    sum += (presents[i].parts * region.presents[i])
                }
                val square = region.x * region.y
                if (sum <= square) {
                    val percent = sum.toDouble() / square
                    println("Diff: ${square - sum}, percent: $percent")
                    percents.add(percent)
                    res++
                }
            }
            percents.sort()
            println(percents)
            return res
        }

        data class Present(val map: List<CharArray>, val parts: Int) {
            fun print() {
                println("Present parts: $parts")
                for (line in map) {
                    for (ch in line) {
                        print(ch)
                    }
                    println()
                }
            }
        }
        data class Region(val x: Int, val y: Int, val presents: List<Int>)
    }
}