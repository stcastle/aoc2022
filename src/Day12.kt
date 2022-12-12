import java.util.LinkedList
import java.util.Queue

fun canGo(from: Char, to: Char): Boolean {
  val tFrom = if (from == 'S') 'a' else from
  val tTo = if (to == 'E') 'z' else to
  return tTo.code - tFrom.code <= 1
}

fun getNeighbors(
    row: Int,
    col: Int,
    grid: List<List<Char>>,
    visited: List<List<Boolean>>
): List<GridItem> {
  val lastRowIndex = grid.size - 1
  val lastColumnIndex = grid[0].size - 1
  val curr = grid[row][col]
  val neighbors: MutableList<GridItem> = mutableListOf()
  // up
  val potentialNeighbors =
      listOf(
          GridItem(row - 1, col),
          GridItem(row + 1, col),
          GridItem(row, col - 1),
          GridItem(row, col + 1))
  potentialNeighbors.forEach {
    if (it.row >= 0 &&
        it.col >= 0 &&
        it.row <= lastRowIndex &&
        it.col <= lastColumnIndex &&
        canGo(curr, grid[it.row][it.col]) &&
        !visited[it.row][it.col])
        neighbors.add(it)
  }
  return neighbors
}

fun getPath(parents: Map<GridItem, GridItem>, from: GridItem, to: GridItem): List<GridItem> {
  val path: MutableList<GridItem> = mutableListOf()
  var curr = to // work backward through parents.
  path.add(curr)
  while (curr != from) {
    // println("curr is $curr. Destination is $from. End was $to. Parent is ${parents[curr]}")
    curr = parents[curr]!!
    path.add(curr)
  }
  return path
}

fun createVisited(numRows: Int, numColumns: Int): MutableList<MutableList<Boolean>> =
    MutableList(numRows) { MutableList(numColumns) { false } }

/** Returns the shortest path from start to end. */
fun bfs(grid: List<List<Char>>, start: GridItem, end: GridItem): List<GridItem> {
  val visited = createVisited(grid.size, grid[0].size)
  val q: Queue<GridItem> = LinkedList()
  // maps a grid item to its parent.
  val parents: MutableMap<GridItem, GridItem> = mutableMapOf()
  q.add(start)
  visited[start.row][start.col] = true
  var success = false
  while (q.isNotEmpty()) {
    val v = q.poll()
    if (v == end) {
      success = true
      break
    }
    getNeighbors(v.row, v.col, grid, visited).forEach {
      q.add(it)
      visited[it.row][it.col] = true
      parents[it] = v
    }
  }
  return if (success) getPath(parents, start, end) else emptyList()
}

fun findStart(grid: List<List<Char>>) = findChar(grid, 'S')

fun findEnd(grid: List<List<Char>>) = findChar(grid, 'E')

fun findChar(grid: List<List<Char>>, char: Char): GridItem {
  grid.forEachIndexed { row, innerList ->
    innerList.forEachIndexed { col, c -> if (c == char) return GridItem(row, col) }
  }
  throw Exception("did not find char")
}

fun getStarts(grid: List<List<Char>>): List<GridItem> {
  val starts: MutableList<GridItem> = mutableListOf()
  grid.forEachIndexed { row, v ->
    v.forEachIndexed { col, c -> if (c == 'S' || c == 'a') starts.add(GridItem(row, col)) }
  }
  return starts
}

fun main() {
  fun part1(input: List<String>): Int {
    val grid = inputToLoL(input)
    val path = bfs(grid, findStart(grid), findEnd(grid))
    // println(path)
    return path.size - 1 // count the steps in the path, not the number of elements in the path.
  }

  fun part2(input: List<String>): Int {
    val grid = inputToLoL(input)
    val end = findEnd(grid)
    return getStarts(grid).map { start -> bfs(grid, start, end).size - 1 }.filter { it != -1 }.min()
  }

  val day = "12"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
