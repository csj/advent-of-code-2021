# Day 6: Lanternfish

[<< Previous](Day05.md) | [Problem Statement](https://adventofcode.com/2021/day/6) | [My solution](../src/main/kotlin/puzzles/Day06.kt) | [Next >>](Day07.md)

Oof, the problem statement's author has wasted no time throwing the word "**exponentially**" in our faces. DANGER. WARNING.

## Part 1
The main insight to take away is that not each fish needs to be modeled separately. Instead, we know that all fish that share the same timer would do the same thing, so we can instead track the *number* of fish of different timers as each day goes by. (Also it might be about time to use a larger data structure to track our fish numbers in).

Because each fish's timer decreases by one every tick, we can actually do something very simple: start with a list of 9 counters, and on each tick just *remove* the first entry (the one designating the fish that are about to spawn), and re-add this to the end of the list, representing the new `[8]` entry. We'll also need to add it to the new `[6]` entry.

```kotlin
repeat(times) {
    val zeroes = ageGroups.removeAt(0)
    ageGroups[6] += zeroes
    ageGroups += zeroes
}
```

Simulate 80 times, sum up the groups.

## Part 2
Extract the common bits (i.e. most of it), this time simulate 256 times. Make sure to use `Long` instead of `Int` (or whatever 64-bit number you have access to)!

```kotlin
override fun solvePart1() = solve(80)
override fun solvePart2() = solve(256)
```