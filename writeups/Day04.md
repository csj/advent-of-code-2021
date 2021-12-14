# Day 4: Giant Squid (a.k.a. Bingo Simulator)
[<< Previous](Day03.md) | [Problem Statement](https://adventofcode.com/2021/day/4) | [My solution](../src/main/kotlin/puzzles/Day04.kt) | [Next >>](Day05.md)

## Part 1
Not much to say about this one: play bingo, detect winning combinations. One little gotcha: the spacing is a bit awkward in the input, so if you're not careful you'll be reading empty tokens.

Keep track of which bingo card wins the fastest, and add up all the uncalled numbers that remain. Bingo bango bongo.

## Part 2
Huh -- now we want the card that wins *last* (apparently squids are sore losers). Maybe we can separate the procedure for finding out what a single bingo card does (how many numbers it uses up; what its final score is) from the *application* of these ideas. In part 1 we want the one with the fewest used up numbers; in part 2 we want the one with the most used up numbers. So:

```kotlin
override fun solvePart1(): String {
    val boardResults = solveAllBoards()
    val firstWinner = boardResults.minByOrNull { it.moves }!!
    return firstWinner.score.toString()
}

override fun solvePart2(): String {
    val boardResults = solveAllBoards()
    val lastWinner = boardResults.maxByOrNull { it.moves }!!
    return lastWinner.score.toString()
}
```