package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil

class Adv2025Day1 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 3
            println(part1("2025/2025-12-01-sample.txt"))
            // 1135
            println(part1("2025/2025-12-01.txt"))

            // 6
            println(part2("2025/2025-12-01-sample.txt"))
            // 6558
            println(part2("2025/2025-12-01.txt"))
        }

        fun part1(inputFile: String) : Int {
            val lines = FileUtil.readLines(inputFile)
            var value = 50
            var res = 0
            lines.forEach { line ->
                val direction = line[0]
                val number = Integer.parseInt(line.substring(1))
                if (direction == 'R') {
                    value += number
                } else {
                    value -= number
                }
                while (value < 0) {
                    value += 100
                }
                while (value >= 100) {
                    value -= 100
                }
                if (value == 0) {
                    res++
                }
            }
            return res
        }

        fun part2(inputFile: String) : Int {
            val lines = FileUtil.readLines(inputFile)
            var value = 50
            var res = 0
            lines.forEach { line ->
                val direction = line[0]
                val number = Integer.parseInt(line.substring(1))
                val wasZero = value == 0
                when (direction) {
                    'R' -> {
                        value += number
                    }
                    'L' -> {
                        value -= number
                    }
                    else -> {
                        throw RuntimeException()
                    }
                }
                // 1 - 101 = -100
                // 1 - 102 = -101
                // 0 - 100 = -100
                // 0 - 101 = -101
                var change = 0
                if (value < 0) {
                    while (value < 0) {
                        value += 100
                        change++
                    }
                    if (wasZero) {
                        change--
                    }
                    if (value == 0) {
                        change++
                    }
                } else if (value > 0) {
                    // 1 + 99 = 100
                    // 1 + 100 = 101
                    // 0 + 100 = 100
                    // 0 + 101 = 101
                    while (value >= 100) {
                        value -= 100
                        change++
                    }
                } else {
                    change++
                }
                res += change
            }
            return res
        }
    }
}