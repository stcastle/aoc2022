fun getIntRanges(input: List<String>): Pair<List<IntRange>, List<IntRange>> {
  val first: MutableList<IntRange> = mutableListOf()
  val second: MutableList<IntRange> = mutableListOf()
  input.forEach { line ->
    val ranges: List<String> = line.split(',')
    require(ranges.size == 2)
    val firstRange: List<String> = ranges.first().split('-')
    first.add(IntRange(firstRange.first().toInt(), firstRange.last().toInt()))
    val secondRange = ranges.last().split('-')
    second.add(IntRange(secondRange.first().toInt(), secondRange.last().toInt()))
  }
  return Pair(first, second)
}

fun main() {
  fun part1(input: List<String>): Int {
    val (first, second) = getIntRanges(input)

    return first.zip(second).count {
      it.first.all { elementInFirst -> it.second.contains(elementInFirst) } ||
          it.second.all { elementInSecond -> it.first.contains(elementInSecond) }
    }
  }

  fun part2(input: List<String>): Int {
    val (first, second) = getIntRanges(input)
    return first.zip(second).count {
      it.first.any { elementInFirst -> it.second.contains(elementInFirst) } ||
          it.second.any { elementInSecond -> it.first.contains(elementInSecond) }
    }
  }

  val day = "04"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
