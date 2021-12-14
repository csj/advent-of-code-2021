# Day 10: Syntax scoring
[<< Previous](Day09.md) | [Problem Statement](https://adventofcode.com/2021/day/10) | [My solution](../src/main/kotlin/puzzles/Day10.kt) | [Next >>](Day11.md)

I found a cheeky way to solve both parts with a single function; read on :)

## Part 1
Ok, we need to find the first illegal character in the line. We're going to need to push opening characters onto some kind of stack and pop them off again as we read matching closing characters. The moment we find a closing character we don't expect, we return a score corresponding to the closing character we *did* expect.

The stack itself could use either the call stack (i.e. recursive function calls indexing the string at different places). Instead, I just went with a list of characters that could be added to and removed from.

```kotlin
for (c in line) {
    when (c) {
        '(', '[', '{', '<' -> stack.add(c)
        ')' -> stack.removeLast().let { if (it != '(') return 3 }
        ']' -> stack.removeLast().let { if (it != '[') return 57 }
        '}' -> stack.removeLast().let { if (it != '{') return 1197 }
        '>' -> stack.removeLast().let { if (it != '<') return 25137 }
    }
}
```
## Part 2
If only there were a way to have a single function that could produce one result if the line is legal, and another if the line is illegal. Hello, negative numbers! In my solution the above bit for returning illegal values is inverted to return negative values, and part 2's solution simply weeds these out:
```kotlin
private fun List<Long>.median() = sorted().let { it[it.size / 2] }
override fun solvePart2() = readGroup().map { solve(it) }.filter { it > 0 }.median().toString()
```

Assuming a line survives the parsing process without returning some negative score, the description of the scoring mechanism for part 2 slides perfectly into a `fold` implementation, as follows:
```kotlin
return stack.reversed().fold(0L) { acc, c -> acc * 5 + when(c) {
    '(' -> 1; '[' -> 2; '{' -> 3; '<' -> 4
    else -> throw IllegalArgumentException("Invalid character: $c")
} }
```

Again, make sure to use your favourite 64-bit number type here.