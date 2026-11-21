package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil
import kotlin.math.pow
import kotlin.streams.toList

class Adv2025Day3 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 357
            //println(part1("2025/2025-12-03-sample.txt"))
            // 17155
            //println(part1("2025/2025-12-03.txt"))

            // 3121910778619
            println(part2("2025/2025-12-03-sample.txt"))
            //
            println(part2("2025/2025-12-03.txt"))
        }

        fun part1(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            var res: Long = 0
            lines.forEach {
                val numbers = it.toCharArray().map { ch -> ch.digitToInt() }.toList()
                val pair1 = max(numbers, 0, numbers.size - 1)
                val pair2 = max(numbers, pair1.second + 1, numbers.size)
                res += pair1.first * 10 + pair2.first
            }
            return res
        }

        fun max(numbers: List<Int>, start: Int, end: Int): Pair<Int, Int> {
            var firstMaxI = -1
            var firstMax = -1
            for (i in start until end) {
                if (numbers[i] > firstMax) {
                    firstMax = numbers[i]
                    firstMaxI = i
                }
            }
            return Pair(firstMax, firstMaxI)
        }

        fun part2(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            var res: Long = 0
            val count = 12
            lines.forEach {
                val numbers = it.toCharArray().map { ch -> ch.digitToInt() }.toList()
                val numbers12 = ArrayList<Int>()
                var res12: Long = 0
                var start = 0
                for (i in 0 until 12) {
                    val index = 12 - i - 1
                    val pair = max(numbers, start, numbers.size - index)
                    res12 += pair.first * 10.0.pow(index).toLong()
                    start = pair.second + 1
                }
                res += res12
            }
            return res
        }
    }
}