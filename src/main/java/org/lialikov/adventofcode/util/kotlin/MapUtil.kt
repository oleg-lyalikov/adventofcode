package org.lialikov.adventofcode.util.kotlin

import org.lialikov.adventofcode.model.Position

class MapUtil {

    companion object {
        fun find(map: List<CharArray>, ch: Char): Position {
            for (i in map.indices) {
                for (j in map[i].indices) {
                    if (map[i][j] == ch) {
                        return Position(j, i)
                    }
                }
            }
            throw IllegalStateException("$ch cannot be found")
        }

        fun print(map: List<CharArray>) {
            print(map, 0, map[0].size - 1)
        }

        fun print(map: List<CharArray>, startJ: Int, endJ: Int) {
            for (line in map) {
                for (j in startJ..endJ) {
                    kotlin.io.print(line[j])
                }
                println()
            }
            println()
        }
    }

}