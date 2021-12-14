# Day 9: Smoke Basin (also, [this guy](https://twitter.com/day9tv))
[My solution](../src/main/kotlin/puzzles/Day09.kt)

## Part 1
Pretty straightforward, just scan the lines in and look for characters that are lower than all their neighbours. Accounting for boundaries is the only really tricky part. There are two main approaches: (1) use a bunch of `if` conditions to guard against boundary cases (my approach), (2) simply pad the whole grid with walls, then run your search on the inner grid.

## Part 2
It's time to welcome back our old friend, [flood fill](https://en.wikipedia.org/wiki/Flood_fill). Essentially we regard any 9 as a wall, and any smaller number as not a wall. We scan the grid looking for non-walls (that we haven't seen), and flood it by keeping track of cells we've seen, then recursing in 4 directions to cells we've not yet seen (and that are still in bounds). The flooding procedure returns the size of the flooded area, in cells:

```kotlin
return 1 + floodFill(i - 1, j) + floodFill(i + 1, j) + floodFill(i, j - 1) + floodFill(i, j + 1)
```

1 for this cell, and then add the sizes of floods we find in all 4 directions.

Finally, we line the produced basins in order, take the 3 largest ones, and multiply them together:

```kotlin
return basins.sortedDescending().take(3).fold(1) { acc, i -> acc * i }.toString()
```