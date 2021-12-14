# Day 13: Transparent Origami
[<< Previous](Day12.md) | [Problem Statement](https://adventofcode.com/2021/day/13) | [My solution](../src/main/kotlin/puzzles/Day13.kt) | [Next >>](Day14.md)
## Part 1
Pretty straight-forward simulation. Make a 2-D array of booleans, fold the paper, add dots to the first half of the paper, remove dots from the second half. Finding the right indices to update was a bit of a tricky situation, but if we take the example of "fold along x=10", we realize that indices 20...11 should map onto indices 0...9, so that's just double the fold line position and subtract the index: 

```kotlin
for (x in 0 until foldX) grid[y][x] = grid[y][x] || grid[y][2*foldX - x]
```

## Part 2
Another case of the same only different. This time play out all the folds instead of just the first one. I enjoyed the surprise twist: it made me glad I didn't auto-submit answers in my code! :boom:

(Edit) With the benefit of *sleep*, I thought of a better approach. Rather than updating a *giant* grid with true/false values, it's probably far better to process a list of dots, and work out where those dots will appear after a fold. Ah well, *c'est la vie.*
