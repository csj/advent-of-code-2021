#Day 3: Binary diagnostic
[My solution](../src/main/kotlin/puzzles/Day03.kt)

##Part 1
This puzzle is as much an exercise in juggling new abstract concepts as it is following a procedure. Here we get introduced to some new terms we've never heard of: the **power consumption**, the **gamma rate** and the **epsilon rate**. The procedure itself is quite straightforward, and again there are a few approaches. It could be done in a loop, where the gamma rate is built up digit by digit (perhaps multiplying some accumulator by 2 at each step). 

I chose I would say a more functional approach. First find out what the digits in the gamma rate are, then combine them to form a number.

```kotlin
val gammaDigits = List(grid[0].length) { j ->   // (1)
    val groups = grid.map { it[j] }.groupBy { it }.mapValues { it.value.size }   // (2)
    if (groups.getOrDefault('0', 0) > groups.getOrDefault('1', 0)) '1' else '0'   // (3)
}
```

Oof, this is a mouthful ... let's break it down.
We're building a list of digits that's the same length as the first line of the input (line 1). For each index `j`, we select the `j`th character of each line into a list, group them by ... themselves, and finally compile the size of each group (line 2). On line 3 we compare the sizes of the groups to decide which digit to use (watch out for empty groups!)

Compiling the actual gamma rate is a one liner in Kotlin: `gammaDigits.joinToString("").toInt(2)`. This `toInt(2)` is *so* incredibly useful, I didn't know it existed. Look for something like it in your programming language of choice!

The epsilon rate can be discovered a similar way, but we notice that its bits should simply be inverses of the bits in the gamma rate, and so they should sum to something suspiciously clean. Examining the example input, we find 22 (10110) and 9 (01001). Their sum would be 31 (11111) -- or one less than the 5th power of 2. We can use this to calculate the epsilon rate from the gamma rate directly: `val epsilon = (1 shl grid[0].length) - 1 - gamma` 

##Part 2
This problem gives us a real challenge at keeping [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself), but we're going to give it a shot.

The main thing we want to avoid is expressing the procedure for paring down the list of items twice. Yes, the procedure for calculating the O<sub>2</sub> rating uses a different method for determining if an element in the list survives than the procedure for calculating the CO<sub>2</sub> rating, but perhaps the survival criteria can be injected into a single procedure.

The survival criteria can be described as a function which takes a List of strings and a position, and produces a function that can determine, for a string, whether it should survive or not. The procedure itself can then be successively applied to the original list of strings until one remains -- it can then be extracted as a number using the technique from Part 1.

```kotlin
fun procedure(crit: (List<String>, Int) -> (String) -> Boolean): Int {
    var subset = lines.toList()
    repeat(lines[0].length) { j ->
        subset = subset.filter(crit(subset, j))
        if (subset.size == 1) return subset[0].toInt(2)
    }
    throw IllegalStateException("Still too many lines :(")
}
```

It now remains for us to build the criteria themselves. The oxygen value can be found by applying the procedure with an inline filtering criteria, as follows:
```kotlin
val oxygen = procedure { subset, j -> {
    val most = subset.map { it[j] }.groupBy { it }.mapValues { it.value.size }
        .maxByOrNull { it.value * 10 + it.key.digitToInt() }!!.key
    it[j] == most
} }
```
We use a similar approach as in Part 1 to determine the most prominent digit in the position we're interested in (taking care to handle ties correctly), then finally returning a *function* that determines if a line survives (it should have the correct digit in the position): `it[j] == most`

A similar approach is used to calculate the CO<sub>2</sub>.