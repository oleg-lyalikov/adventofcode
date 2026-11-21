package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.model.Position3D
import org.lialikov.adventofcode.util.FileUtil
import kotlin.math.sqrt

class Adv2025Day8 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 40
            //println(part1("2025/2025-12-08-sample.txt", 10))
            // 90036
            //println(part1("2025/2025-12-08.txt", 1000))

            // 25272
            //println(part2("2025/2025-12-08-sample.txt"))
            // 6083499488
            println(part2("2025/2025-12-08.txt"))
        }

        fun part1(inputFile: String, iterations: Int) : Long {
            val lines = FileUtil.readLines(inputFile)
            val positions = ArrayList<Position3D>()
            for (line in lines) {
                val ints = line.split(",").map { it.toInt() }.toList()
                positions.add(Position3D(ints[0], ints[1], ints[2]))
            }
            println(positions)
            val distances = HashMap<Set<Position3D>, Double>()
            for (p1 in positions) {
                for (p2 in positions) {
                    if (p1 != p2) {
                        val set = setOf(p1, p2)
                        if (!distances.containsKey(set)) {
                            distances[set] = distance(p1, p2)
                        }
                    }
                }
            }
            val circuits = HashMap<Position3D, HashSet<Position3D>>()
            for (i in 0 until iterations) {
                var min: Double = 10000000000.0
                var p1: Position3D? = null
                var p2: Position3D? = null
                var eKey: Set<Position3D>? = null
                for (e in distances.entries) {
                    val iterator = e.key.iterator()
                    val ep1 = iterator.next()
                    val ep2 = iterator.next()
                    //val set1 = circuits.get(ep1)
                    //val set2 = circuits.get(ep2)
                    if (e.value < min) {
                        min = e.value
                        p1 = ep1
                        p2 = ep2
                        eKey = e.key
                    }
                }

                if (p1 == null || p2 == null) {
                    throw IllegalStateException("!!!")
                }

                val set1 = circuits[p1]
                val set2 = circuits[p2]
                if (set1 != null && set2 != null && set1 != set2) {
                    set1.addAll(set2)
                    for (p in set1) {
                        circuits[p] = set1
                    }
                } else if (set1 != null) {
                    set1.add(p2)
                    circuits[p2] = set1
                } else if (set2 != null) {
                    set2.add(p1)
                    circuits[p1] = set2
                } else {
                    val set = HashSet<Position3D>()
                    set.add(p1)
                    set.add(p2)
                    circuits[p1] = set
                    circuits[p2] = set
                }
                distances.remove(eKey)
            }
            val uniqueSets: HashSet<HashSet<Position3D>> = HashSet()
            for (set in circuits.values) {
                uniqueSets.add(set)
            }
            val sizesSorted = uniqueSets.map { it.size }.sorted().toList()
            val i = sizesSorted.size - 1
            return sizesSorted[i].toLong() * sizesSorted[i - 1] * sizesSorted[i - 2]
        }

        private fun distance(p1: Position3D, p2: Position3D): Double {
            val dx = p1.x - p2.x
            val dy = p1.y - p2.y
            val dz = p1.z - p2.z
            return sqrt(dx.toDouble() * dx + dy.toDouble() * dy + dz.toDouble() * dz)
        }

        fun part2(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            val positions = ArrayList<Position3D>()
            for (line in lines) {
                val ints = line.split(",").map { it.toInt() }.toList()
                positions.add(Position3D(ints[0], ints[1], ints[2]))
            }
            println(positions)
            val distances = HashMap<Set<Position3D>, Double>()
            for (p1 in positions) {
                for (p2 in positions) {
                    if (p1 != p2) {
                        val set = setOf(p1, p2)
                        if (!distances.containsKey(set)) {
                            distances[set] = distance(p1, p2)
                        }
                    }
                }
            }
            val circuits = HashMap<Position3D, HashSet<Position3D>>()
            while (true) {
                var min: Double = 10000000000.0
                var p1: Position3D? = null
                var p2: Position3D? = null
                var eKey: Set<Position3D>? = null
                for (e in distances.entries) {
                    val iterator = e.key.iterator()
                    val ep1 = iterator.next()
                    val ep2 = iterator.next()
                    if (e.value < min) {
                        min = e.value
                        p1 = ep1
                        p2 = ep2
                        eKey = e.key
                    }
                }

                if (p1 == null || p2 == null) {
                    throw IllegalStateException("!!!")
                }

                val set1 = circuits[p1]
                val set2 = circuits[p2]
                if (set1 != null && set2 != null && set1 != set2) {
                    set1.addAll(set2)
                    for (p in set1) {
                        circuits[p] = set1
                    }
                } else if (set1 != null) {
                    set1.add(p2)
                    circuits[p2] = set1
                } else if (set2 != null) {
                    set2.add(p1)
                    circuits[p1] = set2
                } else {
                    val set = HashSet<Position3D>()
                    set.add(p1)
                    set.add(p2)
                    circuits[p1] = set
                    circuits[p2] = set
                }
                if (circuits[p1]!!.size == positions.size) {
                    return p1.x.toLong() * p2.x
                }
                distances.remove(eKey)
            }
        }
    }
}