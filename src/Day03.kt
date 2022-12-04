/** Finds the common character in the two strings. */
fun findCommon(a: String, b: String): Char {
  a.forEach { if (b.contains(it)) return it }
  throw Exception("No common character found between the two strings.\n\tFirst: $a\n\tSecond: $b")
}

/** Finds the common character among all the strings. */
fun findCommon(s: List<IndexedValue<String>>): Char {
  val strings = s.map { it.value }
  val firstString = strings.first()
  firstString.forEach { char -> if (strings.drop(1).all { it.contains(char) }) return char }
  throw Exception("No common char found in the list of strings: $strings")
}

fun Char.priority(): Int {
  if (this in 'a'..'z') {
    return this.code - 'a'.code + 1
  }
  if (this in 'A'..'Z') {
    return this.code - 'A'.code + 27
  }
  throw Exception("Unexpected char: $this")
}

fun main() {
  fun part1(input: List<String>): Int {
    val commonItems: List<Char> =
        input.map {
          val numItems = it.length
          val half: Int = numItems / 2
          val first = it.substring(0, half)
          val second = it.substring(half)
          findCommon(first, second)
        }
    return commonItems.fold(0) { acc: Int, item: Char -> acc + item.priority() }
  }

  fun part2(input: List<String>): Int {
    // Makes a map where values are the groups of 3.
    val groupsOfThree = input.withIndex().groupBy { it.index / 3 }
    val badges: List<Char> = groupsOfThree.map { entry -> findCommon(entry.value) }
    return badges.fold(0) { acc: Int, item: Char -> acc + item.priority() }
  }

  val day = "03"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
