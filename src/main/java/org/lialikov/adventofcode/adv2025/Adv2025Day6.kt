package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.model.Operation
import org.lialikov.adventofcode.util.FileUtil

class Adv2025Day6 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 4277556
            //println(part1("2025/2025-12-06-sample.txt"))
            // 6100348226985
            //println(part1("2025/2025-12-06.txt"))

            // 3263827
            println(part2("2025/2025-12-06-sample.txt"))
            // 12377473011151
            println(part2("2025/2025-12-06.txt"))
        }

        fun part1(inputFile: String) : Long {
            val tasks = readInput(inputFile)
            var res = 0L
            for (task in tasks) {
                if (task.op == Operation.ADD) {
                    var taskRes = 0L
                    for (number in task.numbers) {
                        taskRes += number
                    }
                    res += taskRes
                } else {
                    var taskRes = 1L
                    for (number in task.numbers) {
                        taskRes *= number
                    }
                    res += taskRes
                }
            }
            return res
        }

        private fun readInput(inputFile: String): List<Task> {
            val lines = FileUtil.readLines(inputFile)
            val tasks = ArrayList<Task>()
            val numbers = ArrayList<ArrayList<Long>>()
            for (line in lines) {
                val items = line.trim().split(Regex("\\s+"))
                if (items[0][0] != '*' && items[0][0] != '+') {
                    for (i in 0 until items.size) {
                        if (i >= numbers.size) {
                            numbers.add(ArrayList())
                        }
                        numbers[i].add(items[i].toLong())
                    }
                } else {
                    for (i in 0 until items.size) {
                        if (items[i].length != 1) {
                            throw IllegalStateException("!!!: " + items[i])
                        }
                        val op = if (items[i][0] == '+')
                            Operation.ADD
                        else if (items[i][0] == '*')
                            Operation.MULT
                        else throw IllegalStateException("!!!: " + items[i])
                        tasks.add(Task(numbers[i], op))
                    }
                }
            }
            return tasks
        }

        fun part2(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            val opIndex = lines.size - 1
            val maxJ = lines.maxOfOrNull { it.length }!!
            var op = Operation.fromChar(lines[opIndex][0])
            var taskRes = if (op == Operation.ADD) 0L else 1L
            var res = 0L
            for (j in 0 until maxJ) {
                if (j != 0 && j < lines[opIndex].length && lines[opIndex][j] != ' ') {
                    res += taskRes
                    op = Operation.fromChar(lines[opIndex][j])
                    taskRes = if (op == Operation.ADD) 0L else 1L
                }
                var num = ""
                for (i in 0 until lines.size - 1) {
                    if (j < lines[i].length && lines[i][j] != ' ') {
                        num += lines[i][j]
                    }
                }
                if (num.isEmpty()) {
                    continue
                }
                if (op == Operation.ADD) {
                    taskRes += num.toLong()
                } else {
                    taskRes *= num.toLong()
                }
            }
            res += taskRes
            return res
        }

        data class Task(val numbers: List<Long>, val op: Operation)
    }
}