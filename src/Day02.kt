/** Beats is the option that the current one beats. */
enum class PlayOption() {
  ROCK,
  PAPER,
  SCISSORS,
}

fun String.toPlayOption(): PlayOption =
    when (this) {
      "A",
      "X" -> PlayOption.ROCK
      "B",
      "Y" -> PlayOption.PAPER
      "C",
      "Z" -> PlayOption.SCISSORS
      else -> throw Exception("Expecting A, B, C, X, Y, or Z. Got $this")
    }

fun PlayOption.toXYZ(): String =
    when (this) {
      PlayOption.ROCK -> "X"
      PlayOption.PAPER -> "Y"
      PlayOption.SCISSORS -> "Z"
    }

fun PlayOption.beats(): PlayOption =
    when (this) {
      PlayOption.ROCK -> PlayOption.SCISSORS
      PlayOption.PAPER -> PlayOption.ROCK
      PlayOption.SCISSORS -> PlayOption.PAPER
    }

fun PlayOption.beats(other: PlayOption): Boolean = this.beats() == other

fun String.whatDoIPlay(theirs: PlayOption): PlayOption =
    when (this) {
      // Lose
      "X" -> theirs.beats()
      // Draw
      "Y" -> theirs
      // Win
      "Z" -> theirs.beats().beats() // Trick of the cycle
      else -> throw Exception("Expecting X, Y, or Z. Got $this.")
    }

/** Score for what you played. */
fun baseScore(p: PlayOption): Int =
    when (p) {
      PlayOption.ROCK -> 1
      PlayOption.PAPER -> 2
      PlayOption.SCISSORS -> 3
    }

/** Score for losing, winning, or drawing. */
fun gameScore(theirs: PlayOption, mine: PlayOption): Int {
  // Draw.
  if (theirs == mine) {
    return 3
  }
  // Win.
  if (mine.beats(theirs)) {
    return 6
  }
  // Loss.
  return 0
}

fun main() {
  fun part1(input: List<String>): Int =
      input.fold(initial = 0) { score: Int, line: String ->
        val plays: List<String> = line.split(" ")
        assert(plays.size == 2)
        val theirs: PlayOption = plays.first().toPlayOption()
        val mine = plays.last().toPlayOption()
        score + baseScore(mine) + gameScore(theirs, mine)
      }

  fun part2(input: List<String>): Int {
    val newPlays =
        input.map { line ->
          val plays: List<String> = line.split(" ")
          assert(plays.size == 2)
          val theirs = plays.first().toPlayOption()
          val mine = plays.last().whatDoIPlay(theirs)
          listOf<String>(theirs.toXYZ(), mine.toXYZ()).joinToString(" ")
        }
    return part1(newPlays)
  }

  val day = "02"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
