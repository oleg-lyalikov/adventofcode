package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil
import java.util.LinkedList

class Adv2025Day11 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 5
            //println(part1("2025/2025-12-11-sample.txt"))
            // 749
            //println(part1("2025/2025-12-11.txt"))

            // 2
            //println(part2("2025/2025-12-11-sample2.txt"))
            // 13875 That's not the right answer; your answer is too low.
            // 45549519385500 That's not the right answer; your answer is too low.
            // 142315981060500 That's not the right answer; your answer is too low.
            // 420257875695750
            println(part2("2025/2025-12-11.txt"))
        }

        fun part1(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            val outputs = HashMap<String, List<String>>()
            lines.forEach {
                val key = it.substring(0, it.indexOf(":"))
                val outs = it.substring(it.indexOf(":") + 1)
                    .split(" ")
                    .filter { name -> !name.isBlank() }
                    .toList()
                outputs[key] = outs
            }
            println(outputs)
            val toVisit = LinkedList<Path>()
            toVisit.add(Path("you", listOf("you")))
            val visited = HashSet<String>()
            var res = 0L
            while (!toVisit.isEmpty()) {
                val path = toVisit.poll()
                visited.add(path.item)
                val outs = outputs[path.item]
                for (out in outs!!) {
                    if (visited.contains(out)) {
                        continue
                    }
                    if (out == "out") {
                        res++
                        continue
                    }
                    val newPath = ArrayList(path.path)
                    newPath.add(out)
                    toVisit.add(Path(out, newPath))
                }
            }
            return res
        }

        fun part2(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            val outputs = HashMap<String, List<String>>()
            val inputs = HashMap<String, ArrayList<String>>()
            lines.forEach {
                val key = it.substring(0, it.indexOf(":"))
                val outs = it.substring(it.indexOf(":") + 1)
                    .split(" ")
                    .filter { name -> !name.isBlank() }
                    .toList()
                outputs[key] = outs
                for (out in outs) {
                    var list = inputs[out]
                    if (list == null) {
                        list = ArrayList()
                        inputs[out] = list
                    }
                    list.add(key)
                }
            }
            println(outputs)
            println(inputs)

            //println(num("dac", "out", outputs))
            //println(num("fft", "out", outputs))

            val toVisit = LinkedList<String>()
            toVisit.addAll(inputs["out"]!!)
            val resMap = HashMap<String, Long>()
            resMap["out"] = 1
            val paths = HashMap<String, Long>()
            paths["out"] = 1
            val visited = HashSet<String>()
            while (!toVisit.isEmpty()) {
                val item = toVisit.poll()
                if (visited.contains(item)) {
                    continue
                }
                val num = num(item, "out", outputs, resMap)
                resMap[item] = num
                visited.add(item)
                val ins = inputs[item]
                if (ins != null) {
                    toVisit.addAll(ins)
                }
            }
            // 13875
            val dacN = resMap["dac"]!!
            // 1322062617256
            val fftN = resMap["fft"]!!
            // 92390918968423743
            val svrN = resMap["svr"]!!
            if (dacN < fftN) {
                // 14611122
                // 4676422
                val fromFftToDac = num123("fft", "dac", outputs, inputs)
                // 702
                // 2073
                val fromSvrToFft = num123("svr", "fft", outputs, inputs)
                return dacN * fromFftToDac * fromSvrToFft
            } else {
                val fromDacToFft = num123("dac", "fft", outputs, inputs)
                val fromSvrToDac = num123("svr", "dac", outputs, inputs)
                return fftN * fromDacToFft * fromSvrToDac
            }
        }

        private fun num123(start: String, end: String, outputs: HashMap<String, List<String>>, inputs: HashMap<String, ArrayList<String>>): Long {
            val toVisit = LinkedList<String>()
            toVisit.addAll(inputs[end]!!)
            val resMap = HashMap<String, Long>()
            resMap[end] = 1
            val paths = HashMap<String, Long>()
            paths[end] = 1
            val visited = HashSet<String>()
            while (!toVisit.isEmpty()) {
                val item = toVisit.poll()
                if (visited.contains(item)) {
                    continue
                }
                val num = num(item, end, outputs, resMap)
                resMap[item] = num
                visited.add(item)
                val ins = inputs[item]
                if (ins != null) {
                    toVisit.addAll(ins)
                }
            }
            return resMap[start]!!
        }

        private fun num(start: String, end: String, outputs: HashMap<String, List<String>>, resMap: HashMap<String, Long>): Long {
            val toVisit = LinkedList<List<String>>()
            toVisit.add(listOf(start))
            var res = 0L
            var i = 0
            while (!toVisit.isEmpty()) {
                i++
                if (i % 10000 == 0) {
                    println("Iteration: $i")
                }
                if (i > 7_000_000) {
                    break
                }
                val path = toVisit.poll()
                val lastItem = path[path.size - 1]
                val outs = outputs[lastItem] ?: continue
                for (out in outs) {
                    if (resMap.containsKey(out)) {
                        res += resMap[out]!!
                        continue
                    }
                    if (path.contains(out)) {
                        continue
                    }
                    if (out == end) {
                        res++
                        continue
                    }
                    val newPath = ArrayList(path)
                    newPath.add(out)
                    toVisit.add(newPath)
                }
            }
            return res
        }

        data class Path(val item: String, val path: List<String>)
    }
}