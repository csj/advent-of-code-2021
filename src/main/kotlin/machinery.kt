import java.util.*

suspend fun DailyPuzzle.play() {
    val actualInput = getInputForDay(this.day)
    println("-- DAY ${this.day} --")

    this.scanner = Scanner(this.sampleInput)
    val part1SampleSolution = this.solvePart1()
    if (part1SampleSolution == this.sampleAnswerPart1) {
        println("Part 1 sample is correct: $part1SampleSolution")
    } else {
        println("Part 1 sample is incorrect (Expected: ${this.sampleAnswerPart1}, Actual: $part1SampleSolution)")
        return
    }

    this.scanner = Scanner(actualInput)
    val part1ActualSolution = this.solvePart1()
    println("Part 1 solution: $part1ActualSolution")

    this.sampleAnswerPart2?.let {
        this.scanner = Scanner(this.sampleInput)
        val part2SampleSolution = this.solvePart2()
        if (part2SampleSolution == it) {
            println("Part 2 sample is correct: $part2SampleSolution")
        } else {
            println("Part 2 sample is incorrect (Expected: ${this.sampleAnswerPart2}, Actual: $part2SampleSolution)")
            return
        }
        this.scanner = Scanner(actualInput)
        val part2ActualSolution = this.solvePart2()
        println("Part 2 solution: $part2ActualSolution")
    }
}