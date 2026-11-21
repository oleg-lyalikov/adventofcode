package org.lialikov.adventofcode.adv2025

import org.lialikov.adventofcode.util.FileUtil
import java.util.concurrent.atomic.AtomicInteger

class Adv2025Day10 {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // 7
            //println(part1("2025/2025-12-10-sample.txt"))
            // 425
            //println(part1("2025/2025-12-10.txt"))

            // 33
            println(part2("2025/2025-12-10-sample.txt"))
            // 15986 That's not the right answer; your answer is too high.
            // 15883
            //println(part2("2025/2025-12-10.txt"))
        }

        fun part1(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            var res: Long = 0
            lines.forEach { line ->
                val diagram = line.substring(1, line.indexOf(']'))
                val diagramBoolArray = BooleanArray(diagram.length)
                for (i in 0 until diagram.length) {
                    diagramBoolArray[i] = diagram[i] == '#'
                }
                val groupsStr = line.substring(line.indexOf(']') + 1, line.indexOf('{'))
                val wirings = groupsStr.split('(').filter { !it.trim().isBlank() }.map { part ->
                    part.dropLast(2).split(',').map { it.toInt() }.toList()
                }.toList()
                println("$diagram $wirings")
                for (i in 1 until wirings.size) {
                    val indexes = ArrayList<Int>(i)
                    for (j in 0 until i) {
                        indexes.add(j)
                    }
                    var found = false
                    while (true) {
                        //println(indexes)
                        val array = BooleanArray(diagram.length)
                        for (ii in indexes) {
                            val wiring = wirings[ii]
                            for (w in wiring) {
                                array[w] = !array[w]
                            }
                        }
                        var equals = true
                        for (ii in 0 until array.size) {
                            if (array[ii] != diagramBoolArray[ii]) {
                                equals = false
                                break
                            }
                        }
                        if (equals) {
                            found = true
                            println(indexes)
                            break
                        }

                        // increment
                        var index = indexes.size - 1
                        var maxForI = wirings.size - indexes.size + index
                        var stop = false
                        while (indexes[index] == maxForI) {
                            index--
                            maxForI = wirings.size - indexes.size + index
                            if (index < 0) {
                                stop = true
                                break
                            }
                        }
                        if (stop) {
                            break
                        }
                        indexes[index]++
                        for (i in index + 1 until indexes.size) {
                            indexes[i] = indexes[index] + (i - index)
                        }
                    }
                    if (found) {
                        res += i
                        break
                    }
                }
            }
            return res
        }

        fun part2ResCache(inputFile: String): Map<Int, Int> {
            val resCache = HashMap<Int, Int>()
            if (inputFile == "2025/2025-12-10.txt") {
                resCache[0] = 220
                resCache[1] = 64
                resCache[2] = 69
                resCache[3] = 228
                resCache[4] = 98
                resCache[5] = 45
                resCache[6] = 47
                resCache[7] = 98
                resCache[8] = 64
                resCache[9] = 50
                resCache[10] = 92
                resCache[11] = 96
                resCache[12] = 73
                resCache[13] = 29
                resCache[14] = 41
                resCache[15] = 86
                resCache[16] = 79
                resCache[17] = 66
                resCache[18] = 32
                resCache[19] = 28
                resCache[20] = 65
                resCache[21] = 47
                resCache[22] = 20
                resCache[23] = 64
                resCache[24] = 57
                resCache[25] = 80
                resCache[26] = 58
                resCache[27] = 112
                resCache[28] = 50
                resCache[29] = 27
                resCache[30] = 55
                resCache[31] = 44
                resCache[32] = 87
                resCache[33] = 146
                resCache[34] = 74
                resCache[35] = 113
                resCache[36] = 207
                resCache[37] = 56
                resCache[38] = 127
                resCache[39] = 228
                resCache[40] = 221
                resCache[41] = 125
                resCache[42] = 50
                resCache[43] = 67
                resCache[44] = 102
                resCache[45] = 17
                resCache[46] = 40
                resCache[47] = 69
                resCache[48] = 15
                resCache[49] = 271 // ?
                resCache[50] = 98
                resCache[51] = 118
                resCache[52] = 217 // ?
                resCache[53] = 156
                resCache[54] = 133
                resCache[55] = 60
                resCache[56] = 97
                resCache[57] = 127
                resCache[58] = 61
                resCache[59] = 85
                resCache[60] = 59
                resCache[61] = 225
                resCache[62] = 83
                resCache[63] = 48
                resCache[95] = 90
                resCache[109] = 119
            }
            return resCache
        }

        fun part2(inputFile: String) : Long {
            val lines = FileUtil.readLines(inputFile)
            var res = 0L
            val resCache = part2ResCache(inputFile)
            for (lineI in 0 until lines.size) {
                val line = lines[lineI]
                println("Line $lineI")
                val groupsStr = line.substring(line.indexOf(']') + 1, line.indexOf('{'))
                val wirings = groupsStr.split('(').filter { !it.trim().isBlank() }.map { part ->
                    part.dropLast(2).split(',').map { it.toInt() }.toList()
                }.toList()
                val joltages = line.substring(line.indexOf('{') + 1, line.indexOf("}"))
                    .split(",")
                    .map { it.toInt() }
                    .toList()
                val task = Task(wirings, joltages)
                //println(wirings)
                //println(joltages)
                val equations = ArrayList<Equation>()
                for (i in 0 until joltages.size) {
                    val equation = HashSet<Num>()
                    for (j in 0 until wirings.size) {
                        for (n in wirings[j]) {
                            if (n == i) {
                                equation.add(Num(j, true))
                            }
                        }
                    }
                    equations.add(Equation(equation, joltages[i], fromInput = true))
                }
                println(equations)
                val solution = IntArray(wirings.size) { -1 }
                val eqSol = EqSol(equations, solution)
                var prevSol = eqSol

                var nextSol = sumEquations(prevSol)
                while (nextSol != null && nextSol.equations.size < 5000) {
                    prevSol = nextSol
                    nextSol = sumEquations(nextSol)
                }

                val maxMinCount = 100_000
                val sol = if (resCache.containsKey(lineI)) resCache[lineI] else
                    funfunfun(prevSol, task, AtomicInteger(0), maxMinCount) ?: throw IllegalStateException(":(")
                //val sol = funfunfun(prevSol, task, AtomicInteger(0), maxMinCount) ?: throw IllegalStateException(":(")
                println("Task result: $sol")
                res += sol!!
            }
            return res
        }

        fun solutionSum(eqSol: EqSol): Int {
            return eqSol.solution.sum()
        }

        fun solved(eqSol: EqSol, task: Task): Boolean {
            if (eqSol.solution.count { it < 0 } > 0) {
                return false
            }
            val wireResults = IntArray(task.maxWiring + 1) { 0 }
            for (i in 0 until eqSol.solution.size) {
                val iValue = eqSol.solution[i]
                val wiring = task.wirings[i]
                for (num in wiring) {
                    wireResults[num] += iValue
                }
            }
            for (i in 0 until wireResults.size) {
                if (wireResults[i] != task.joltages[i]) {
                    return false
                }
            }
            return true
        }

        fun calculateIndexTryOrder(equations: List<Equation>,
                                   solution: IntArray): List<Int> {
            val solutionExists = HashSet<Int>()
            for (i in 0 until solution.size) {
                if (solution[i] >= 0) {
                    solutionExists.add(i)
                }
            }
            val numIByEqSize = HashMap<Int, HashSet<Int>>()
            var maxSize = 0
            for (eq in equations) {
                numIByEqSize.computeIfAbsent(eq.intSet.size) { HashSet() }.addAll(eq.intSet)
                if (eq.intSet.size > maxSize) {
                    maxSize = eq.intSet.size
                }
            }
            val visited = HashSet<Int>()
            val res = ArrayList<Int>()
            for (i in 1 until maxSize + 1) {
                val set = numIByEqSize[i]
                set?.forEach {
                    if (!visited.contains(it) && !solutionExists.contains(it)) {
                        res.add(it)
                        visited.add(it)
                    }
                }
            }
            return res
        }

        fun funfunfun(prevSol: EqSol, task: Task, minCount: AtomicInteger, maxMinCount: Int): Int? {
            if (solved(prevSol, task)) {
                return solutionSum(prevSol)
            }

            var min: Int? = null

            //for (i in 0 until prevSol.solution.size) {
            //val visited = HashSet<Int>()
            //val rand = SecureRandom()
            //for (i in prevSol.solution.size - 1 downTo 0) {
            //for (i in 0 until prevSol.solution.size) {

            for (i in prevSol.numITryOrder) {
//            while (visited.size != prevSol.solution.size) {
//                val i = rand.nextInt(0, prevSol.solution.size)
//                if (visited.contains(i)) {
//                    continue
//                }
//                visited.add(i)

                val solI = prevSol.solution[i]
                if (solI >= 0) {
                    continue
                }

                for (j in 0 until task.maxJoltagePerWiring[i]!! + 1) {
                    val trySol = prevSol.solution.copyOf()
                    trySol[i] = j
                    val tryEqSol = EqSol(prevSol.equations, trySol)
                    var res = updateEquations(tryEqSol, task)
                    if (res != null) {
                        if (solved(res, task)) {
                            val solSum = solutionSum(res)
                            if (min == null || solSum < min) {
                                min = solSum
                                minCount.incrementAndGet()
                                println("New min: $min, count: ${minCount.get()}")
                            }
                            continue
                        }
                        val solSum = funfunfun(res, task, minCount, maxMinCount)
                        if (solSum != null && (min == null || solSum < min)) {
                            min = solSum
                            minCount.incrementAndGet()
                            println("New min: $min, count: ${minCount.get()}")
                        }
                    }
                    if (minCount.get() > maxMinCount) {
                        return min
                    }
                }
                // TODO: ?
                return min
            }
            return min
        }

        fun updateEquations(eqSol: EqSol, task: Task): EqSol? {
            val valuesExist = HashSet<Int>()
            for (i in 0 until eqSol.solution.size) {
                if (eqSol.solution[i] >= 0) {
                    valuesExist.add(i)
                }
            }
            val equations = ArrayList<Equation>()
            var globalReplaced = false
            val newSolution = eqSol.solution.copyOf()
            for (eq in eqSol.equations) {
                if (eq.nums.size == 1) {
                    val lastNum = eq.nums.iterator().next()
                    val lastValue = if (lastNum.positive) eq.res else -eq.res
                    if (lastValue < 0) {
                        return null
                    }
                    val fromSolution = eqSol.solution[lastNum.i]
                    if (fromSolution >= 0) {
                        if (fromSolution != lastValue) {
                            return null
                        }
                    } else {
                        if (task.maxJoltagePerWiring[lastNum.i]!! < lastValue) {
                            return null
                        }
                        newSolution[lastNum.i] = lastValue
                        valuesExist.add(lastNum.i)
                        globalReplaced = true
                    }
                } else {
                    var replaced = false
                    for (eqNum in eq.nums) {
                        if (valuesExist.contains(eqNum.i)) {
                            val vvv = newSolution[eqNum.i]
                            val toAdd = if (eqNum.positive) -vvv else vvv
                            val newRes = eq.res + toAdd
                            val newNums = HashSet(eq.nums)
                            newNums.remove(eqNum)

                            equations.add(Equation(newNums, newRes, fromInput = false))
                            replaced = true
                            globalReplaced = true
                            break
                        }
                    }
                    if (!replaced) {
                        equations.add(eq)
                    }
                }
            }
            if (globalReplaced) {
                return updateEquations(EqSol(equations, newSolution), task)
            }
            return eqSol
        }

        fun sumEquations(eqSol: EqSol): EqSol? {
            val resEquations = ArrayList(eqSol.equations)
            val newSolution = eqSol.solution.copyOf()
            var changed = false
            for (i in 0 until eqSol.equations.size) {
                for (j in (i + 1) until eqSol.equations.size) {
                    val eq1 = eqSol.equations[i]
                    val eq2 = eqSol.equations[j]
                    val maxSize = eq1.intSet.size.coerceAtLeast(eq2.intSet.size)

                    val all = eq1.intSet.plus(eq2.intSet)
                    val diffs = ArrayList<Equation?>()
                    diffs.add(diff(eq1, eq2, true))
                    diffs.add(diff(eq1, eq2, false))
                    for (diff in diffs) {
                        if (diff != null && !diff.intSet.isEmpty()) {
                            if (diff.intSet.size == 1) {
                                val num = diff.nums.iterator().next()
                                val value = if (num.positive) diff.res else -diff.res
                                if (newSolution[num.i] >= 0 && newSolution[num.i] != value) {
                                    throw IllegalStateException("!!!")
                                } else if (newSolution[num.i] < 0) {
                                    newSolution[num.i] = value
                                    changed = true
                                }
                            //} else if (diff.intSet.size < (all.size / 2 + all.size % 2 + 1)) {
                            //} else if (diff.intSet.size < (all.size - 1)) {
                            } else if (diff.intSet.size < all.size && diff.intSet.size < maxSize) {
                                var alreadyAdded = false
                                val diffNumsNegative = diff.nums.map { Num(it.i, !it.positive) }.toSet()
                                for (eq in resEquations) {
                                    if (eq.nums == diff.nums || eq.nums == diffNumsNegative) {
                                        alreadyAdded = true
                                        break
                                    }
                                }
                                if (!alreadyAdded) {
                                    resEquations.add(diff)
                                    changed = true
//                                    if (!eq1.fromInput) {
//                                        equationsToRemove.add(eq1)
//                                    }
//                                    if (!eq2.fromInput) {
//                                        equationsToRemove.add(eq2)
//                                    }
                                }
                            }
                        }
                    }
                }
            }
            return if (changed) {
                val equationsToRemove = HashSet<Equation>()
                for (i in 0 until resEquations.size) {
                    for (j in (i + 1) until resEquations.size) {
                        val eq1 = resEquations[i]
                        val eq2 = resEquations[j]
                        if (eq1.intSet.containsAll(eq2.intSet) && !eq1.fromInput) {
                            equationsToRemove.add(eq1)
                        } else if (eq2.intSet.containsAll(eq1.intSet) && !eq2.fromInput) {
                            equationsToRemove.add(eq2)
                        }
                    }
                }
                //                0      1      2     3        4         5          6              7          8           9       10     11        12
                // [##.#..#.#.] (1,5) (2,4,5) (1,9) (2,6) (0,3,4,5,7) (0,3,4) (0,1,2,3,5,6,9) (0,2,3,5,7,8) (3,5) (3,5,6,7,8,9) (1,2) (5,6,7,8,9) (8) {51,52,203,75,167,237,70,59,51,70}
                resEquations.removeAll(equationsToRemove)
                if (eqSol.equations.size == resEquations.size) {
                    return null
                }
                EqSol(resEquations, newSolution)
            } else {
                null
            }
        }

        fun diff(eq1: Equation, eq2: Equation, plus: Boolean): Equation? {
            val nums = HashSet<Num>()
            for (num1 in eq1.nums) {
                var found = false
                for (num2 in eq2.nums) {
                    if (num1.i == num2.i) {
                        found = true
                        val bothPositive = num1.positive && num2.positive
                        val bothNegative = !num1.positive && !num2.positive
                        if (plus && (bothPositive || bothNegative)) {
                            return null
                        } else if (!plus && !(bothPositive || bothNegative)) {
                            return null
                        }
                    }
                }
                if (!found) {
                    nums.add(num1)
                }
            }
            for (num2 in eq2.nums) {
                var found = false
                for (num1 in eq1.nums) {
                    if (num2.i == num1.i) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    nums.add(if (plus) num2 else Num(num2.i, if (num2.positive) false else true))
                }
            }
            val res = if (plus) eq1.res + eq2.res else eq1.res - eq2.res
            return Equation(nums, res, fromInput = false)
        }

        fun calculateMaxJoltagePerWiring(wirings: List<List<Int>>, joltages: List<Int>): Map<Int, Int> {
            val res = HashMap<Int, Int>()
            for (i in 0 until wirings.size) {
                val wiring = wirings[i]
                res[i] = wiring.maxOfOrNull { joltages[it] }!!
            }
            return res;
        }

        data class Task(val wirings: List<List<Int>>,
                        val joltages: List<Int>,
                        val maxJoltage: Int = joltages.max(),
                        val maxWiring: Int = wirings.maxOfOrNull { it.max() }!!,
                        val maxJoltagePerWiring: Map<Int, Int> = calculateMaxJoltagePerWiring(wirings, joltages)
        )
        data class Equation(val nums: Set<Num>, val res: Int, val fromInput: Boolean, val intSet: Set<Int> = nums.map { it.i }.toSet())
        data class Num(val i: Int, val positive: Boolean)
        data class EqSol(
            val equations: List<Equation>,
            val solution: IntArray,
            val numITryOrder: List<Int> = calculateIndexTryOrder(equations, solution)
        ) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as EqSol

                if (equations != other.equations) return false
                if (!solution.contentEquals(other.solution)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = equations.hashCode()
                result = 31 * result + solution.contentHashCode()
                return result
            }
        }
    }
}