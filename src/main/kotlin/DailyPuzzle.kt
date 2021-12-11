import java.util.*

abstract class DailyPuzzle(val day: Int) {
    abstract fun solvePart1(): String
    open fun solvePart2(): String { return "" }
    abstract val sampleInput: String
    abstract val sampleAnswerPart1: String
    abstract val sampleAnswerPart2: String?

    var scanner: Scanner = Scanner(System.`in`)

    fun readGroup(): List<String> = sequence {
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()!!
            if (line.isBlank()) break
            yield (line)
        }
    }.toList()

    fun readGroups(): List<List<String>> = sequence {
        while (scanner.hasNextLine()) {
            val group = readGroup()
            if (group.isEmpty()) break
            yield (group)
        }
    }.toList()

    fun readToks() = readGroup().flatMap { it.split(" ") }
}