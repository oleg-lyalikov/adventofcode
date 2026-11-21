package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.model.Position
import org.lialikov.adventofcode.util.FileUtil
import org.lialikov.adventofcode.util.kotlin.MapUtil

class Adv2025Day7 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 21
            //println(part1("2025/2025-12-07-sample.txt"))
            // 1678
            //println(part1("2025/2025-12-07.txt"))

            // 40
            println(part2("2025/2025-12-07-sample.txt"))
            // 357525737893560
            println(part2("2025/2025-12-07.txt"))
        }

        fun part1(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile).map { it.toCharArray() }
            val start = MapUtil.find(lines, 'S')
            var res = 0L
            for (i in start.y until lines.size - 1) {
                for (j in 0 until lines[i].size) {
                    if (lines[i][j] == 'S' || lines[i][j] == '|') {
                        if (lines[i + 1][j] == '.') {
                            lines[i + 1][j] = '|'
                        } else if (lines[i + 1][j] == '^') {
                            var split = false
                            if ((j - 1 >= 0) && lines[i + 1][j - 1] == '.') {
                                lines[i + 1][j - 1] = '|'
                                split = true
                            }
                            if (((j + 1) < lines[i + 1].size) && lines[i + 1][j + 1] == '.') {
                                lines[i + 1][j + 1] = '|'
                                split = true
                            }
                            if (split) {
                                res++
                            }
                        }
                    }
                }
            }
            MapUtil.print(lines)
            return res
        }

        fun part2(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile).map { it.toCharArray() }
            val start = MapUtil.find(lines, 'S')
            val resMap = HashMap<Position, Long>()
            for (i in start.y until lines.size - 1) {
                for (j in 0 until lines[i].size) {
                    if (lines[i][j] == 'S' || lines[i][j] == '|') {
                        if (lines[i + 1][j] == '^') {
                            val prevValue = resMap[Position(j, i)]!!
                            if ((j - 1 >= 0)) {
                                lines[i + 1][j - 1] = '|'
                                val p = Position(j - 1, i + 1)
                                val current = resMap.computeIfAbsent(p) { 0L }
                                resMap[p] = current + prevValue
                            }
                            if ((j + 1) < lines[i + 1].size) {
                                lines[i + 1][j + 1] = '|'
                                val p = Position(j + 1, i + 1)
                                val current = resMap.computeIfAbsent(p) { 0L }
                                resMap[p] = current + prevValue
                            }
                        } else if (lines[i + 1][j] == '.' || lines[i + 1][j] == '|') {
                            lines[i + 1][j] = '|'
                            if (lines[i][j] == 'S') {
                                resMap[Position(j, i)] = 1
                            }
                            val prevValue = resMap[Position(j, i)]!!
                            val p = Position(j, i + 1)
                            val current = resMap.computeIfAbsent(p) { 0L }
                            resMap[p] = current + prevValue
                        }
                    }
                }
            }

            //printMapValues(lines, resMap)

            var res = 0L
            for (j in 0 until lines[0].size) {
                val value = resMap.get(Position(j, lines.size - 1))
                if (value != null) {
                    res += value
                }
            }
            return res
        }

        private fun printMapValues(lines: List<CharArray>, resMap: HashMap<Position, Long>) {
            for (i in 0 until lines.size) {
                for (j in 0 until lines[0].size) {
                    val value = resMap.get(Position(j, i))
                    if (value == null) {
                        print("   ")
                    } else {
                        print(value)
                        if (value / 10 > 0) {
                            print(' ')
                        } else {
                            print("  ")
                        }
                    }
                }
                println()
            }
        }
    }
}