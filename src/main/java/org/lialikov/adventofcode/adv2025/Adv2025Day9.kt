package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.model.Direction
import org.lialikov.adventofcode.model.Position
import org.lialikov.adventofcode.util.FileUtil
import java.io.PrintWriter
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.io.path.toPath
import kotlin.math.abs


class Adv2025Day9 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 50
            //println(part1("2025/2025-12-09-sample.txt"))
            // 4741451444
            //println(part1("2025/2025-12-09.txt"))

            // 24
            println(part2("2025/2025-12-09-sample.txt"))
            // 1562459680 (48878 square out of 123256 (39.65567599143247 %))
            println(part2("2025/2025-12-09.txt"))
        }

        fun part1(inputFile: String) : Long {
            val positions = FileUtil.readLines(inputFile).map {
                val numbers = it.split(",").stream().map { str -> str.toInt() }.toList()
                Position(numbers[0], numbers[1])
            }.toList()
            var max: Long = 0
            for (p1 in positions) {
                for (p2 in positions) {
                    if (p1 != p2) {
                        val dx = abs(p1.x - p2.x) + 1
                        val dy = abs(p1.y - p2.y) + 1
                        val square = dx * dy.toLong()
                        if (square > max) {
                            max = square
                        }
                    }
                }
            }
            return max
        }

//        fun part21(inputFile: String) : Long {
//            var maxX = 0
//            var maxY = 0
//            val positions = FileUtil.readLines(inputFile).map {
//                val numbers = it.split(",").stream().map { str -> str.toInt() }.toList()
//                val res = Position(numbers[0], numbers[1])
//                if (res.x > maxX) {
//                    maxX = res.x
//                }
//                if (res.y > maxY) {
//                    maxY = res.y
//                }
//                res
//            }.toMutableList()
//            val map = Array(maxY + 1) { Array<Direction?>(maxX + 1) { null } }
//            var p1 = positions[0]
//            positions.add(p1)
//            for (i in 1 until positions.size) {
//                val p2 = positions[i]
//
//                if (p1.x == p2.x) {
//                    if (p2.y > p1.y) {
//                        for (j in p1.y until p2.y) {
//                            map[j][p1.x] = Direction.S
//                        }
//                    } else if (p2.y < p1.y) {
//                        for (j in p2.y + 1 until p1.y + 1) {
//                            map[j][p1.x] = Direction.N
//                        }
//                    } else {
//                        error("x")
//                    }
//                } else if (p1.y == p2.y) {
//                    if (p2.x > p1.x) {
//                        for (j in p1.x until p2.x) {
//                            map[p1.y][j] = Direction.E
//                        }
//                    } else if (p2.x < p1.x) {
//                        for (j in p2.x + 1 until p1.x + 1) {
//                            map[p1.y][j] = Direction.W
//                        }
//                    } else {
//                        error("x")
//                    }
//                } else {
//                    error("!!!")
//                }
//
//                p1 = p2
//            }
//
//            for (y in 0 until map.size) {
//                for (x in 0 until map[y].size) {
//                    print(if (map[y][x] == null) '.' else map[y][x]!!.name)
//                }
//                println()
//            }
//
//            val squares = ArrayList<Square>()
//            for (i in 0 until positions.size) {
//                val p1 = positions[i]
//                for (j in i + 1 until positions.size) {
//                    val p2 = positions[j]
//                    val dx = abs(p1.x - p2.x) + 1
//                    val dy = abs(p1.y - p2.y) + 1
//                    val square = dx * dy.toLong()
//                    squares.add(Square(p1, p2, square))
//                }
//            }
//            squares.sortDescending()
//
//            for (square in squares) {
//                val minX = square.p1.x.coerceAtMost(square.p2.x)
//                val maxX = square.p1.x.coerceAtLeast(square.p2.x)
//                val minY = square.p1.y.coerceAtMost(square.p2.y)
//                val maxY = square.p1.y.coerceAtLeast(square.p2.y)
//                var outside = false
//                for (i in minY until maxY + 1) {
//                    if (!isInside(Position(minX, i), map) || !isInside(Position(maxX, i), map)) {
//                        outside = true
//                        break
//                    }
//                }
//                for (j in minX until maxX + 1) {
//                    if (!isInside(Position(j, minY), map) || !isInside(Position(j, maxY), map)) {
//                        outside = true
//                        break
//                    }
//                }
//                if (!outside) {
//                    return square.square
//                }
//            }
//            return 0L
//        }

        fun part2(inputFile: String) : Long {
            val positions = FileUtil.readLines(inputFile).map {
                val numbers = it.split(",").stream().map { str -> str.toInt() }.toList()
                Position(numbers[0], numbers[1])
            }.toMutableList()
            positions.add(positions[0])

            val map = InputMap(HashMap())
            val cache = Cache(HashMap(), HashMap())
            var p1 = positions[0]
            val positionsToCheck = HashSet<Position>()
            for (i in 1 until positions.size) {
                val p2 = positions[i]

                if (p1.x == p2.x) {
                    val p1p2Min = p1.y.coerceAtMost(p2.y)
                    val current = cache.minYByX.computeIfAbsent(p1.x) { p1p2Min }
                    if (p1p2Min < current) {
                        cache.minYByX[p1.x] = p1p2Min
                    }

                    if (p2.y > p1.y) {
                        for (j in p1.y until p2.y) {
                            map.byY.computeIfAbsent(j) { ArrayList() }
                                .add(Segment(p1.x, p1.x, Direction.S))
                        }
                    } else if (p2.y < p1.y) {
                        for (j in p2.y + 1 until p1.y + 1) {
                            map.byY.computeIfAbsent(j) { ArrayList() }
                                .add(Segment(p1.x, p1.x, Direction.N))
                        }
                    } else {
                        error("x")
                    }
                } else if (p1.y == p2.y) {
                    val xDiff = abs(p1.x - p2.x)
                    if (xDiff > 10000) {
//                        if (p1.x > p2.x) {
//                            positionsToCheck.add(p1)
//                        } else {
//                            positionsToCheck.add(p2)
//                        }
                        positionsToCheck.add(p1)
                        positionsToCheck.add(p2)
                    }
                    if (p2.x > p1.x) {
                        map.byY.computeIfAbsent(p1.y) { ArrayList() }
                            .add(Segment(p1.x, p2.x, Direction.E))
                        for (xx in p1.x until p2.x + 1) {
                            val current = cache.minYByX.computeIfAbsent(xx) { p1.y }
                            if (p1.y < current) {
                                cache.minYByX[xx] = p1.y
                            }
                        }
                    } else if (p2.x < p1.x) {
                        map.byY.computeIfAbsent(p1.y) { ArrayList() }
                            .add(Segment(p2.x, p1.x, Direction.W))
                        for (xx in p2.x until p1.x + 1) {
                            val current = cache.minYByX.computeIfAbsent(xx) { p1.y }
                            if (p1.y < current) {
                                cache.minYByX[xx] = p1.y
                            }
                        }
                    } else {
                        error("x")
                    }
                } else {
                    error("!!!")
                }

                p1 = p2
            }

            map.byY.entries.forEach { it.value.sort() }

            //print(map, Adv2025Day9.javaClass.getResource("/2025/2025-12-09.txt").toURI().toPath().parent.resolve("2025-12-09-map.txt"))

            val squares = ArrayList<Square>()
            for (i in 0 until positions.size) {
                val p1 = positions[i]
                for (j in i + 1 until positions.size) {
                    val p2 = positions[j]
                    val dx = abs(p1.x - p2.x) + 1
                    val dy = abs(p1.y - p2.y) + 1
                    val square = dx * dy.toLong()
                    squares.add(Square(p1, p2, square))
                }
            }
            squares.sortDescending()

            val yUp = HashMap<Position, Boolean>()
            for (p1 in positionsToCheck) {
                for (p2 in positionsToCheck) {
                    if (p1.y != p2.y) {
                        yUp[p1] = p2.y < p1.y
                    }
                }
            }

            // 26455 - for 4 positions, before yUp
            // 39549 - for 4 positions, with yUp
            for (squareI in 39549 until squares.size) {
                val square = squares[squareI]
                val percents = squareI * 100.0 / squares.size
                println("${java.util.Date()}: Processing $squareI square out of ${squares.size} ($percents %), cache size: ${cache.isInside.size}")
                if (!positionsToCheck.contains(square.p1) && !positionsToCheck.contains(square.p2)) {
                    continue
                }
                if (positionsToCheck.contains(square.p1)) {
                    if (yUp[square.p1]!! && square.p2.y < square.p1.y) {
                        continue
                    } else if (!yUp[square.p1]!! && square.p2.y > square.p1.y) {
                        continue
                    }
                } else if (positionsToCheck.contains(square.p2)) {
                    if (yUp[square.p2]!! && square.p2.y > square.p1.y) {
                        continue
                    } else if (!yUp[square.p2]!! && square.p2.y < square.p1.y) {
                        continue
                    }
                }
                if (cache.isInside.size > 1_000_000) {
                    cache.isInside.clear()
                }
                val minX = square.p1.x.coerceAtMost(square.p2.x)
                val maxX = square.p1.x.coerceAtLeast(square.p2.x)
                val minY = square.p1.y.coerceAtMost(square.p2.y)
                val maxY = square.p1.y.coerceAtLeast(square.p2.y)
                var outside = false
                for (i in minY until maxY + 1) {
                    if (!isInside(Position(minX, i), map, cache) || !isInside(Position(maxX, i), map, cache)) {
                        outside = true
                        break
                    }
                }
                for (j in minX until maxX + 1) {
                    if (!isInside(Position(j, minY), map, cache) || !isInside(Position(j, maxY), map, cache)) {
                        outside = true
                        break
                    }
                }
                if (!outside) {
                    return square.square
                }
            }
            return 0L
        }

        private fun print(map: InputMap, path: Path? = null) {
            val writer: PrintWriter = if (path != null) PrintWriter(path.pathString, "UTF-8") else PrintWriter(System.out)

            for (y in map.byY.keys.sorted()) {
                var i = 0
                for (s in map.byY[y]!!) {
                    while (i < s.x1) {
                        writer.print('.')
                        i++
                    }
                    while (i < s.x2 + 1) {
                        writer.print(s.d.name)
                        i++
                    }
                }
                writer.println()
            }
            writer.close()
        }

        fun isInside(p: Position, map: InputMap, cache: Cache): Boolean {
            val fromCache = cache.isInside[p]
            if (fromCache != null) {
                return fromCache
            }
            var wasMissing = false
            for (j in p.y downTo cache.minYByX[p.x]!!) {
                val xList = map.byY[j]
                if (xList == null) {
                    wasMissing = true
                    continue
                }
                var direction: Direction? = null
                for (j in 0 until xList.size) {
                    val segment = xList[j]
                    if (p.x >= segment.x1 && p.x <= segment.x2) {
                        direction = segment.d
                        break
                    }
                }
                if (direction == null) {
                    wasMissing = true
                    continue
                }
                return when (direction) {
                    Direction.E -> {
                        cache.isInside[p] = true
                        true
                    }
                    Direction.W -> {
                        cache.isInside[p] = !wasMissing
                        !wasMissing
                    }
                    Direction.N -> {
                        cache.isInside[p] = !wasMissing
                        !wasMissing
                    }
                    Direction.S -> {
                        cache.isInside[p] = !wasMissing
                        !wasMissing
                    }
                }
            }
            cache.isInside[p] = false
            return false
        }

        fun isInside1(p: Position, map: Array<Array<Direction?>>): Boolean {
            if (map[p.y][p.x] != null) {
                return true
            }
            for (j in p.y downTo 0) {
                val direction = map[j][p.x]
                if (direction != null) {
                    return when (direction) {
                        Direction.E -> {
                            true
                        }
                        Direction.W -> {
                            false
                        }
                        Direction.N -> {
                            false
                        }
                        else -> {
                            error("should not happen")
                        }
                    }
                }
            }
            return false
        }

        data class Cache(val isInside: HashMap<Position, Boolean>, val minYByX: HashMap<Int, Int>)
        data class InputMap(val byY: HashMap<Int, ArrayList<Segment>>)
        data class Segment(val x1: Int, val x2: Int, val d: Direction): Comparable<Segment> {
            override fun compareTo(other: Segment): Int {
                return x1.compareTo(other.x1)
            }
        }
        data class Square(val p1: Position, val p2: Position, val square: Long): Comparable<Square> {
            override fun compareTo(other: Square): Int {
                return if (square > other.square) 1 else if (square == other.square) 0 else -1
            }
        }
    }
}