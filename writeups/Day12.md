# Day 12: Passage pathing
[My solution](../src/main/kotlin/puzzles/Day12.kt)

This problem subjected me to substantially subtle bugs -- my performance here was sub-par. Using mutable state variables and a recursive function, I was able to complete part 1, but part 2 got me into a mess.

## Part 1
Attempt #1 involved keeping a list of lower-case caves I had seen in the past as we traversed in depth-first fashion. The procedure is pretty simple:
- Add the current cave to the list of seen caves, if necessary (if we've been here before, return 0 immediately)
- Visit the cave, recurse over all its connections
- Remove the current cave from the list of caves, if necessary

If the cave we're visiting is "end", then we return 1; otherwise we produce the sum of visiting all the connections. This worked, and it got me through part 1.

## Part 2
This double-visiting business gave me a real headache. Now it was necessary to keep track not only of which caves we've seen, but also if we're currently doing a revisit. If we are, then it's important to remember *not* to tear this cave out of the visited list on the way back out. I had bugs, I didn't figure them out, so I restarted with the functional way: don't change things.

I re-wrote my core `search` function to take a few parameters: the cave we're currently visiting, a flag indicating if a revisit is still available, and a list of caves we've already seen. Again if the cave we're currently visiting is called "end", we immediately return 1. Otherwise:
- For each connection (that is not "start", and is not a lower-case cave we've seen before if no revisit is available),
- Take the sum of visiting each of these connections, making sure to set the revisit flag correctly (i.e. if we are in fact revisiting a small cave, set this to `true`)

The revised core `search` function:
```kotlin
fun search(current: String, revisitAvailable: Boolean, path: List<String>): Long {
    if (current == "end") return 1
    return connections[current]!!
        .filterNot { it == "start" || (!revisitAvailable && it[0].isLowerCase() && it in path) }
        .sumOf { search(it, revisitAvailable && !(it[0].isLowerCase() && it in path), path + it) }
}
```

Parsing the input is the same for both parts. The solution is executed prohibiting a double-visit for Part 1, and allowing a double-visit for Part 2:
```kotlin
override fun solvePart1() = solve(allowDoubleVisit = false)
override fun solvePart2() = solve(allowDoubleVisit = true)
```