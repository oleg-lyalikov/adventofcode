package org.lialikov.adventofcode.model

enum class Operation {
    ADD,
    MULT,
    DIV;

    companion object {
        fun fromChar(char: Char): Operation {
            return when (char) {
                '+' -> ADD
                '*' -> MULT
                '/' -> DIV
                else -> throw IllegalArgumentException("$char")
            }
        }
    }
}