import java.util.Stack

data class Move(val numMoves: Int, val from: Int, val to: Int)

data class Crates(val stacks: Map<Int, Stack<Char>>, val moves: List<Move>)

fun parseInput(input: List<String>): Crates {
  val stacks: MutableMap<Int, Stack<Char>> = mutableMapOf()
  val moves: MutableList<Move> = mutableListOf()
  var initialStacksAreBuilt = false
  input.forEach { line ->
    if (line.startsWith('1')) {
      initialStacksAreBuilt = true
    }
    if (!initialStacksAreBuilt) {
      line.forEachIndexed { index, char ->
        if (char in 'A'..'Z') {
          val stackNumber = index / 4 + 1 // "zero-indexed" stack is ignored.
          if (!stacks.containsKey(stackNumber)) {
            stacks[stackNumber] = Stack<Char>()
          }
          stacks[stackNumber]!!.add(0, char) // Add it to the bottom ;).
        }
      }
    }
    if (line.startsWith("move")) { // stacks are built so we may be creating moves
      val elems = line.split(" ")
      moves.add(Move(numMoves = elems[1].toInt(), from = elems[3].toInt(), to = elems[5].toInt()))
    }
  }

  return Crates(stacks, moves)
}

fun main() {
  fun part1(input: List<String>): String {
    val crates = parseInput(input)
    for (move in crates.moves) {
      repeat(move.numMoves) { crates.stacks[move.to]!!.push(crates.stacks[move.from]!!.pop()) }
    }
    var s = ""
    crates.stacks.toSortedMap().forEach { s += it.value.peek() }
    return s
  }

  fun part2(input: List<String>): String {
    val crates = parseInput(input)
    val intermediateStack =
        Stack<Char>() // use an intermediate stack. We could also add each to the same position.
    for (move in crates.moves) {
      // either add each crate to an intermediate stack or add each new one to the same index.
      repeat(move.numMoves) { intermediateStack.push(crates.stacks[move.from]!!.pop()) }
      while (!intermediateStack.empty()) {
        crates.stacks[move.to]!!.push(intermediateStack.pop())
      }
    }
    var s = ""
    crates.stacks.toSortedMap().forEach { s += it.value.peek() }
    return s
  }

  val day = "05"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
