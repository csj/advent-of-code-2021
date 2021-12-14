# Day 11: Dumbo Octopus
[My solution](../src/main/kotlin/puzzles/Day11.kt)

## Part 1
Not much to say about this problem: just increment all the elements of the grid every day, and watch for overflows. Spread the wealth and repeat until there are no flashes seen. Count em up and spit em out.

## Part 2
This time we need to find out on what day the dumbos first all flash at once. All the machinery should be in place ... the only question is how to adapt the solution to part 1 so that it also applies to part 2. In the end I wound up factoring out the bit that simulates a single step and returns the number of flashes. For part 1 it was repeated 100 times and the results were summed; for part 2 an infinite series of numbers was piped into the update function until it returned the size of the grid as a response.

```kotlin
override fun solvePart1(): String {
    val grid = readGroup().map { it.map { it.digitToInt() }.toIntArray() }
    return (0 until 100).sumOf { updateGrid(grid) }.toString()
}

override fun solvePart2(): String {
    val grid = readGroup().map { it.map { it.digitToInt() }.toIntArray() }
    return generateSequence(1) { it+1 }.first {
        updateGrid(grid) == grid.size * grid.first().size
    }.toString()
}
```