# Day 7: The Treachery of Whales (a.k.a. Prismatic Alignment simulator)
[<< Previous](Day06.md) | [Problem Statement](https://adventofcode.com/2021/day/7) | [My solution](../src/main/kotlin/puzzles/Day07.kt) | [Next >>](Day08.md)

## Part 1
A little thought suggests that the solution *must* involve arranging all the crabs in order, then finding the median. We're looking for a position that, were we to move it one position to the right, would increase the cost (because of the crabs on the left) by the same amount as it would decrease the cost (because of the crabs on the right). Then we realize that it's complicated by the fact that crabs can share the same position, as in the sample input, so we abandon this line of thinking.

Instead, we're just gonna walk from minimum to maximum, calculate the total cost of each prospective position, and report the minimum.

## Part 2
Heh heh heh -- our laziness has paid off. We need only change how cost is calculated. Our generalized solution can be calculated in terms of a cost function: how much does it cost a crab to move the given number of spaces. It can then be applied using two different cost functions as follows:

```kotlin
override fun solvePart1() = solve { it }
override fun solvePart2() = solve { it * (it+1) / 2 }
```