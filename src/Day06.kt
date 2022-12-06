import java.util.*

class SizedQueue<T>(val maxSize: Int) : LinkedList<T>() {
  override fun add(element: T): Boolean {
    if (super.size >= maxSize) {
      super.remove()
    }
    return super.add(element)
  }
}

fun <T> SizedQueue<T>.allDifferent(): Boolean {
  // For this use case, return false if size is not max.
  if (this.size < this.maxSize) {
    return false
  }
  val uniqueElements: MutableSet<T> = mutableSetOf()
  for (i in 0 until this.size) {
    uniqueElements.add(this.elementAt(i))
  }
  return this.size == uniqueElements.size
}

fun solve(input: List<String>, maxSize: Int): Int {
  val line = input.single()
  val queue = SizedQueue<Char>(maxSize)
  line.forEachIndexed { index, c ->
    queue.add(c)
    if (queue.allDifferent()) {
      return index + 1
    }
  }
  throw Exception("didn't find 4 all different.")
}

fun main() {
  fun part1(input: List<String>) = solve(input, maxSize = 4)

  fun part2(input: List<String>) = solve(input, maxSize = 14)

  val day = "06"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
