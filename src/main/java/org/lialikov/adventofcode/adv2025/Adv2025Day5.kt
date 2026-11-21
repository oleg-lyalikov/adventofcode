package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil

class Adv2025Day5 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 3
            //println(part1("2025/2025-12-05-sample.txt"))
            // 775
            //println(part1("2025/2025-12-05.txt"))

            // 14
            println(part2("2025/2025-12-05-sample.txt"))
            // 350684792662845
            println(part2("2025/2025-12-05.txt"))
        }

        fun part1(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            val ranges = ArrayList<Range>()
            val ingredients = ArrayList<Long>()
            var readRanges = true;
            for (line: String in lines) {
                if (line.isBlank()) {
                    readRanges = false
                    continue
                }
                if (readRanges) {
                    val split = line.split("-")
                    ranges.add(Range(split[0].toLong(), split[1].toLong()))
                } else {
                    ingredients.add(line.toLong())
                }
            }
            var res = 0L
            for (ing in ingredients) {
                for (range in ranges) {
                    if (ing >= range.start && ing <= range.end) {
                        res++
                        break
                    }
                }
            }
            return res
        }

        fun part2(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            var ranges = ArrayList<Range>()
            for (line: String in lines) {
                if (line.isBlank()) {
                    break
                }
                val split = line.split("-")
                ranges.add(Range(split[0].toLong(), split[1].toLong()))
            }

            var newRanges = merge(ranges)
            while (newRanges != ranges) {
                ranges = newRanges
                newRanges = merge(newRanges)
            }

            var res = 0L
            for (range in newRanges) {
                res += (range.end - range.start + 1)
            }
            return res
        }

        private fun merge(ranges: List<Range>): ArrayList<Range> {
            val res = ArrayList<Range>()
            for (i in 0 until ranges.size) {
                val range = ranges[i]
                var newRange = range
                for (j in (i + 1) until ranges.size) {
                    val jRange = ranges[j]
                    if (jRange.start > newRange.end || jRange.end < newRange.start) {
                        continue
                    }
                    newRange = Range(newRange.start.coerceAtMost(jRange.start), newRange.end.coerceAtLeast(jRange.end))
                }
                if (!contains(res, newRange)) {
                    res.add(newRange)
                }
            }
            return res
        }

        private fun contains(ranges: List<Range>, range: Range): Boolean {
            for (r in ranges) {
                if (range.start >= r.start && range.end <= r.end) {
                    return true
                }
            }
            return false
        }

        data class Range(val start: Long, val end: Long)
    }
}