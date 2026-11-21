package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil
import kotlin.math.pow

class Adv2025Day2 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 1227775554
            //println(part1("2025/2025-12-02-sample.txt"))
            // 43952536386
            //println(part1("2025/2025-12-02.txt"))

            // 4174379265
            //println(part2("2025/2025-12-02-sample.txt"))
            // 54486209192
            println(part2("2025/2025-12-02.txt"))
        }

        fun part1(inputFile: String) : Long {
            val line = FileUtil.readLines(inputFile)[0]
            val ranges = line.split(",")
            var res = 0L
            ranges.forEach { r ->
                val beginEnd = r.split("-")
                val start = beginEnd[0].toLong()
                val end = beginEnd[1].toLong()
                //println("$start-$end")
                var next = next(start)
                while (doubleN(next) < start) {
                    next++;
                }
                var doubled = doubleN(next)
                while (doubled <= end) {
                    res += doubled
                    next++;
                    doubled = doubleN(next)
                }
            }
            return res
        }

        fun doubleN(n: Long): Long {
            return "$n$n".toLong()
        }

        fun next(number: Long): Long {
            val nStr = number.toString()
            if (nStr.length % 2 == 1) {
                return next(10.0.pow(nStr.length.toDouble()).toLong())
            }
            val p1 = nStr.take(nStr.length / 2)
            return p1.toLong()
        }

        fun part2(inputFile: String) : Long {
            val line = FileUtil.readLines(inputFile)[0]
            val ranges = line.split(",")
            var res = 0L
            ranges.forEach { r ->
                val beginEnd = r.split("-")
                var start = beginEnd[0].toLong()
                val end = beginEnd[1].toLong()
                //println(end - start)
                while (start <= end) {
                    if (isInvalidPart2(start)) {
                        res += start
                    }
                    start++
                }
            }
            return res
        }

        fun isInvalidPart2(n: Long): Boolean {
            val nStr = n.toString()
            val length: Int = nStr.length
            if (length < 2) {
                return false
            }
            for (i in 1 until (length / 2 + length % 2) + 1) {
                if (length % i != 0) {
                    continue
                }
                var j = 0
                var match = true;
                while (j < length - i) {
                    for (k in j until (j + i)) {
                        if (nStr[k] != nStr[k + i]) {
                            match = false
                            break
                        }
                    }
                    if (!match) {
                        break
                    }
                    j += i
                }
                if (!match) {
                    continue
                }
                return true
            }
            return false
        }
    }
}