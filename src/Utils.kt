import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/** A grid item. */
data class GridItem(val row: Int, val col: Int)

/** Reads lines from the given input txt file. */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/** Converts string to md5 hash. */
fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

fun inputToLoL(input: List<String>): List<List<Char>> {
  val res: MutableList<MutableList<Char>> = mutableListOf()
  input.forEach { line ->
    val chars = mutableListOf<Char>()
    line.forEach { c -> chars.add(c) }
    res.add(chars)
  }
  return res
}
