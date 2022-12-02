fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val day = ""

    val testInput = readInput("Day${day}_test")
    println("Part 1 test = ${part1(testInput)}")

    val input = readInput("Day${day}")
    println("part1 = ${part1(input)}")

    println("Part 2 test = ${part2(testInput)}")
    println("part2 = ${part2(input)}")
}
