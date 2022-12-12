import kotlin.math.max

/** Holds values for the max tree height in all directions leading up to this spot. */
data class MaxTreeHeight(
    var north: Int = -1,
    var east: Int = -1,
    var south: Int = -1,
    var west: Int = -1
)

fun MaxTreeHeight.treeIsVisible(tree: Int) =
    tree > north || tree > east || tree > south || tree > west

/** create the list of lists. */
fun createHeights(input: List<String>): MutableList<MutableList<MaxTreeHeight>> {
  val heights = mutableListOf<MutableList<MaxTreeHeight>>()
  input.forEachIndexed { index, line ->
    heights.add(mutableListOf())
    line.forEach { heights[index].add(MaxTreeHeight()) }
  }
  return heights
}

/** update the heights in place. */
fun MutableList<MutableList<MaxTreeHeight>>.fillNorthAndWestHeights(input: List<String>) {
  val lastRowIndex = input.size - 1
  val lastColumnIndex = input[0].length - 1
  input.forEachIndexed { row, line ->
    line.forEachIndexed { column, _ ->
      // Keep all the outside heights as -1.
      if (!(row == 0 || column == 0 || row == lastRowIndex || column == lastColumnIndex)) {
        this[row][column].north =
            max(this[row - 1][column].north, input[row - 1][column].digitToInt())
        this[row][column].west =
            max(this[row][column - 1].west, input[row][column - 1].digitToInt())
      }
    }
  }
}

fun MutableList<MutableList<MaxTreeHeight>>.fillSouthAndEastHeights(input: List<String>) {
  val lastRowIndex = input.size - 1
  val lastColumnIndex = input[0].length - 1
  input.withIndex().reversed().forEach { indexedValue ->
    val row = indexedValue.index
    indexedValue.value.withIndex().reversed().forEach { lineIndexValue ->
      val column = lineIndexValue.index
      if (!(row == 0 || column == 0 || row == lastRowIndex || column == lastColumnIndex)) {
        this[row][column].south =
            max(this[row + 1][column].south, input[row + 1][column].digitToInt())
        this[row][column].east =
            max(this[row][column + 1].east, input[row][column + 1].digitToInt())
      }
    }
  }
}

fun countVisible(input: List<String>, heights: List<List<MaxTreeHeight>>): Int {
  var count = 0
  heights.forEachIndexed { row, value ->
    value.forEachIndexed { column, maxTreeHeight ->
      val tree = input[row][column].digitToInt()
      if (maxTreeHeight.treeIsVisible(tree)) {
        // println("Visible at Row $row and Column $column")
        count++
      }
    }
  }
  return count
}

fun main() {
  fun part1(input: List<String>): Int {
    val heights: MutableList<MutableList<MaxTreeHeight>> = createHeights(input)
    heights.fillNorthAndWestHeights(input)
    heights.fillSouthAndEastHeights(input)
    // heights.forEach { println(it) }
    return countVisible(input, heights)
  }

  fun part2(input: List<String>): Int {
    val lastRowIndex = input.size - 1
    val lastColumnIndex = input[0].length - 1
    var maxScenicScore = 0
    input.forEachIndexed { row, line ->
      line.forEachIndexed { column, c ->
        // Only check trees on the inside because the edges have viewing score zero.
        // if (!(row == 0 || column == 0 || row == lastRowIndex || column == lastColumnIndex)) {
        val tree = c.digitToInt()
        // Checks North.
        var north = 0
        for (currRow in (row - 1) downTo 0) {
          north++
          if (input[currRow][column].digitToInt() >= tree) break
        }
        // South.
        var south = 0
        for (currRow in (row + 1)..lastRowIndex) {
          south++
          if (input[currRow][column].digitToInt() >= tree) break
        }
        // East.
        var east = 0
        for (currCol in (column + 1)..lastColumnIndex) {
          east++
          if (input[row][currCol].digitToInt() >= tree) break
        }
        // West.
        var west = 0
        for (currCol in (column - 1) downTo 0) {
          west++
          if (input[row][currCol].digitToInt() >= tree) break
        }
        val scenicScore = north * east * south * west
        // println(
        // "Row $row and column $column has score $scenicScore: ($north, $east, $south, $west)")
        maxScenicScore = max(scenicScore, maxScenicScore)
      }
      // }
    }
    return maxScenicScore
  }

  val day = "08"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
