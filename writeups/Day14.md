# Day 14: Extended Polymerization
[<< Previous](Day13.md) | [Problem Statement](https://adventofcode.com/2021/day/14) | [My solution](../src/main/kotlin/puzzles/Day14.kt) | [Next >>](Day15.md)

It's time for recursion again. Hey, haven't we answered this smaller problem before, and doesn't it apply here equally, as well? I should write this down, so I don't need to recalculate it later. Enter our old friend: **memoization**.

First, observe that we don't need to actually construct the strings in order to solve the problem. The problem statement simply asks us to keep track of the frequency of each letter in the final string.

Second, observe that each pair of letters, when expanded a set number of times, always produces the same stuff irrespective of anything else going on outside the pair. We can use this to our advantage.

Consider a very simple case. We start with the string `"AC"`. We are further told to insert a `"B"` between any two letters. We want to expand our input 3 times. The stuff that we end up with is going to be exactly: whatever we get when we expand `"AB"` *2* times, plus whatever we get when we expand `"BC"` 2 times. For convenience, we're going to report the final list of stuff that includes the first character, but excludes the last character. This way, the two sub-cases will dovetail nicely into the main case.

```text
Original: AC

After 1 step: ABC
              12

After 2 steps: ABBBC
               1122
               
After 3 steps: ABBBBBBBC
               11112222
```

We keep recursing downwards in this fashion until we get to 0; where we can report a very simple distribution of stuff: it contains only the first letter of the pair.

In the simple example, we can see that expanding `"AC"` 3 times produces a distribution of 1 "A" and 7 "B"s (we ignore the last character). This was forged by the marriage of expanding `"AB"` twice (1 "A" and 3 "B"s) and expanding `"BC"` twice (4 "B"s).

A little thought will show that the above calculation will involve processing the string `"BB"` more than once. Once that's done once, it doesn't need to be done again; this is where memoization comes to our rescue. Furthermore, if we're ever asked to expand `"AC"` 3 times again, we know we don't have to do the work again.

So, ok we're going to memoize, and not solve the same sub-problem more than once. My first approach was to keep a Map of inputs I've seen before, and store the results as the values. Then my function would first look up in this map before doing anything, and record into it on the way out.

But then I wondered if the solution could be generalized: could we take a function and produce a *memoized* version of it? And the answer is yes: I found a sample online and reproduced it here -- I will admit I don't *fully* understand it yet, but I hope to in time.

The core function is as follows:
```kotlin
val memoizedSim = memoize<Pair<String, Int>, List<Long>> { f -> { pair ->
    val (input, steps) = pair
    val newLetter = transformMap[input]!!
    if (steps == 0) List(n) { i -> if (i == letterIndex(input[0])) 1 else 0 }
    else f(input[0] + newLetter to steps-1) + f(newLetter + input[1] to steps-1)
}}
```

This reads loosely as: if the steps to execute is 0, then immediately return a simple distribution containing only 1 of the first letter. Otherwise, recurse using the new middle letter, and "add" the two distributions together. The add function is a nice bit of `operator` magic:
```kotlin
operator fun List<Long>.plus(other: List<Long>) = zip(other).map { (a,b) -> a + b }
```