# Day 15: Chiton
[<< Previous](Day14.md) | [Problem Statement](https://adventofcode.com/2021/day/15) | [My solution](../src/main/kotlin/puzzles/Day15.kt) | [Next >>](Day16.md)

Path-finding eh, looks like it's time for our old friend Djyk, Dyk, what's this guy called again?

## Part 1
I won't re-hash the Dijkstra algorithm for doing path-finding, that is well covered [elsewhere](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm). Suffice it to say that knowing the name of this algorithm gets you 98% of the way there with your favourite search engine.

## Part 2
Eh, what's that, your CPU didn't catch fire on part 1? Well, good news, the grid is actually 25 times larger than you thought; good luck!

Pro tip: don't actually build the larger grid. Instead, use a function to determine the effective value of a cell at a given location. Here's mine (where the original grid was `nn` x `mm`):
```kotlin
fun cell(i: Int, j: Int) = (grid[i%nn][j%mm] + (i/nn) + (j/mm) - 1) % 9 + 1
```
