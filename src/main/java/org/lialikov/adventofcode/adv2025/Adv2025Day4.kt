package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil

class Adv2025Day4 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 13
            //println(part1("2025/2025-12-04-sample.txt"))
            // 1491
            //println(part1("2025/2025-12-04.txt"))

            // 43
            println(part2("2025/2025-12-04-sample.txt"))
            // 8722
            println(part2("2025/2025-12-04.txt"))
        }

        fun part1(inputFile: String) : Long {
            val map = FileUtil.readLines(inputFile).map { it.toCharArray() }
            var res = 0L
            for (i in 0 until map.size) {
                for (j in 0 until map[0].size) {
                    if (map[i][j] == '@' && rolls(map, i, j) < 4) {
                        res++
                    }
                }
            }
            return res
        }

        fun rolls(map: List<CharArray>, i: Int, j: Int): Int {
            var res = 0
            for (ii in i - 1 until i + 2) {
                for (jj in j - 1 until j + 2) {
                    if (ii < 0 || ii >= map.size) {
                        continue
                    }
                    if (jj < 0 || jj >= map[0].size) {
                        continue
                    }
                    if (ii == i && jj == j) {
                        continue
                    }
                    if (map[ii][jj] == '@') {
                        res++
                    }
                }
            }
            return res
        }

        fun part2(inputFile: String) : Int {
            val curr = FileUtil.readLines(inputFile).map { it.toCharArray() }
            val initialRollsCount = rollsCount(curr)
            var next = remove(curr)
            var prevRollsCount = initialRollsCount
            var nextRollsCount = rollsCount(next)
            while (prevRollsCount != nextRollsCount) {
                next = remove(next)
                prevRollsCount = nextRollsCount
                nextRollsCount = rollsCount(next)
            }
            return initialRollsCount - nextRollsCount
        }

        fun rollsCount(map: List<CharArray>): Int {
            var res = 0
            for (i in 0 until map.size) {
                for (j in 0 until map[0].size) {
                    if (map[i][j] == '@') {
                        res++
                    }
                }
            }
            return res
        }

        fun remove(map: List<CharArray>): List<CharArray> {
            val res = map.map { it.copyOf() }.toList()
            for (i in 0 until map.size) {
                for (j in 0 until map[0].size) {
                    if (map[i][j] == '@' && rolls(map, i, j) < 4) {
                        res[i][j] = '.'
                    }
                }
            }
            return res
        }
    }
}