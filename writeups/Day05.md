# Day 5: Hydrothermal venture
[<< Previous](Day04.md) | [Problem Statement](https://adventofcode.com/2021/day/5) | [My solution](../src/main/kotlin/puzzles/Day05.kt) | [Next >>](Day06.md)

Aha! Regular expressions! I knew it.

## Part 1
The lines can be parsed easily using a regular expression, or awkwardly using splits and loops. Let's go with the former, along with some neat assignment technology:

```kotlin
val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
val (x1, y1, x2, y2) = regex.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
```
We need to drop one of the `groupValues`, because the first one matches the entire string.

To fill in the grid, we need only figure out what direction we're walking by examining the difference between `x1` and `x2`, and `y1` and `y2`. Since we're only doing horizontal or vertical lines, we should abort if `x1 == x2` or `y1 == y2`. Step and increment. Finally, count up how many cells have been touched more than once: `grid.flatten().count { it > 1 }`

## Part 2
Oh, diagonals are in now. No sweat: simply *don't* abort if the x or y doesn't line up. We should again extract the bit that's common to both parts (i.e. the vast majority of it), leaving the bodies of the two parts as:

```kotlin
override fun solvePart1() = solve(ignoreDiagonals = true)
override fun solvePart2() = solve(ignoreDiagonals = false)
```