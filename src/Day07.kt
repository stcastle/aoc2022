sealed interface Item {
  val name: String
  val size: Int

  fun nameSuffix(): String {
    val nameSuffix = name.split("/").last()
    return if (nameSuffix == "") "/" else nameSuffix
  }
}

/** Default size is -1 when unknown. */
class Directory(override val name: String, val parent: Directory?) : Item {

  override var size: Int = -1
    get() {
      if (field == -1) {
        field = items().fold(0) { acc: Int, item: Item -> acc + item.size }
      }
      return field
    }

  private val contents: MutableSet<Item> = mutableSetOf()

  fun add(item: Item) = contents.add(item)

  fun items(): Set<Item> = contents

  fun subdirectories(): List<Directory> = contents.filterIsInstance<Directory>().map { it }

  fun allsubdirectories(): List<Directory> =
      subdirectories().fold(subdirectories()) { acc, directory ->
        acc + directory.allsubdirectories()
      }

  // Just make the contents a set.
  // fun contains(item: Item) = contents.contains(item)

  // this is important for the sets.
  override fun equals(other: Any?) = other is Directory && this.name == other.name
  override fun hashCode(): Int {
    return name.hashCode()
  }

  override fun toString(): String =
      items().fold("- ${name} (dir, size=$size)") { acc: String, item: Item -> acc + "\n\t" + item }
}

data class File(override val name: String, override val size: Int) : Item {
  override fun equals(other: Any?) = other is File && this.name == other.name
  // override fun toString() = "- ${nameSuffix()} (file, size=$size)"
  override fun toString() = "- ${name} (file, size=$size)"
}

/** Finds all sub-directories, including this one, with size at most [max]. */
fun Directory.getAll(maxSize: Int): List<Directory> =
    this.allsubdirectories().fold(emptyList()) { acc: List<Directory>, directory: Directory ->
      if (directory.size <= maxSize) {
        acc + directory
      } else acc
    }

// fun addSizes(items: List<Item>) = items.sumOf { it.size }

/** Reads the input and returns the root [Directory]. */
fun buildFileSystem(input: List<String>): Directory {
  val rootDirectory = Directory("/", null)
  var currDir: Directory = rootDirectory
  input.forEach { line ->
    if (line == "$ cd /") {
      // Should only be the first line, so doesn't matter, but whatever.
      currDir = rootDirectory
    } else if (line == "$ cd ..") {
      currDir = currDir.parent!! // trouble if we cd .. from root.
    } else if (line.startsWith("$ cd")) {
      // Use the given name just as the suffix.
      val nameSuffix = line.split(" ").last()
      // root will look like "//".
      val name = currDir.name + "/" + nameSuffix
      // Any other cd is going down one directory.
      currDir =
          currDir.subdirectories().singleOrNull { it.name == name }
              ?: Directory(name = name, parent = currDir)
      currDir.parent?.add(currDir)
    } else if (line == "$ ls") {
      // Nothing to do here.
    } else if (line.startsWith("dir")) {
      val nameSuffix = line.split(" ").last()
      val name = currDir.name + "/" + nameSuffix
      currDir.add(
          currDir.subdirectories().find { it.name == name }
              ?: Directory(name = name, parent = currDir))
    } else {
      // Line is a file.
      val nameSuffix = line.split(" ").last()
      val name = currDir.name + "/" + nameSuffix
      val size = line.split(" ").first().toInt()
      currDir.add(File(name, size))
    }
  }
  return rootDirectory
}

fun main() {
  fun part1(input: List<String>): Int {
    val rootDirectory = buildFileSystem(input)
    // println(rootDirectory)
    // println(rootDirectory.allsubdirectories().map { it.nameSuffix() })
    return rootDirectory.getAll(maxSize = 100000).sumOf { it.size }
  }

  fun part2(input: List<String>): Int {
    val rootDirectory = buildFileSystem(input)
    val totalSpace = 70000000
    val totalSpaceNeeded = 30000000
    val openSpace = totalSpace - rootDirectory.size
    val spaceNeededToFree = totalSpaceNeeded - openSpace
    assert(spaceNeededToFree >= 0)
    // Directories that are too small.
    val directoriesTooSmall = rootDirectory.getAll(maxSize = spaceNeededToFree).toSet()
    // add back in any directories with size exactly equal to spaceNeededToFree
    val directoriesWeCanDelete =
        setOf(rootDirectory) + rootDirectory.allsubdirectories().toSet() - directoriesTooSmall +
            rootDirectory.allsubdirectories().filter { it.size == spaceNeededToFree }.toSet()
    // print(directoriesWeCanDelete.map { it.nameSuffix() })
    return directoriesWeCanDelete.minOf { it.size }
  }

  val day = "07"

  val testInput = readInput("Day${day}_test")
  println("Part 1 test = ${part1(testInput)}")

  val input = readInput("Day${day}")
  println("part1 = ${part1(input)}")

  println("Part 2 test = ${part2(testInput)}")
  println("part2 = ${part2(input)}")
}
