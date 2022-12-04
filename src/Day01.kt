import kotlin.math.max

fun main() {
  fun part1(input: List<String>): Int {
    var sum = 0
    var max = 0
    input.forEach {
      if (it == "") {
        max = max(sum, max)
        sum = 0
      } else {
        sum += it.toInt()
      }
    }
    // one more time on the end
    return max(sum, max)
  }

  fun part2(input: List<String>): Int {
    var top3 = mutableListOf(0, 0, 0) // keep sorted with smallest item first.
    var sum = 0
    input.forEach {
      if (it == "") {
        if (sum > top3[0]) {
          top3[0] = sum
          top3 = top3.sorted().toMutableList()
        }
        sum = 0
      } else {
        sum += it.toInt()
      }
    }
    // one more time on the end.
    if (sum > top3[0]) {
      top3[0] = sum
      top3 = top3.sorted().toMutableList()
    }

    return top3.sum()
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day01_test")
  // println(part1(testInput))
  check(part1(testInput) == 24000)
  println(part2(testInput))

  val input = readInput("Day01")
  println("part1 = ${part1(input)}")
  println("part2 = ${part2(input)}")
}
