#Day 2: Dive!
[My Solution](../src/main/kotlin/puzzles/Day02.kt)

There isn't much clever to say about this one -- just ... do what it says.

## Part 1
We're going to need to track our `horizontal` position and our `depth` by parsing the inputs and parsing them appropriately. To parse the text we could split the line into tokens, then based on which word was present in the first token, do something different with the second.

I decided to pull out my old friend regular expressions just to get reacquainted, because you just know there's going to be more of that in future contests.

The core of my solution (not that there's much more beyond this):

```kotlin
for (line in readGroup()) {
    "forward (\\d+)".toRegex().find(line)?.let { horz += it.groupValues[1].toInt() }
    "up (\\d+)".toRegex().find(line)?.let { depth -= it.groupValues[1].toInt() }     
    "down (\\d+)".toRegex().find(line)?.let { depth += it.groupValues[1].toInt() }
}
```

Yeah, there's some duplication, and yeah, we're unnecessarily building too many Regex's. Sometimes good enough is good enough.

## Part 2
According to the problem statement, this part is "actually slightly more complicated". The only gotcha here is that we need to start being careful about large numbers soon: my solution was *eerily* close to the 2.1B maximum value of `Int32`. 

