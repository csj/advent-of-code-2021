# Day 1: Sonar Sweep
[My Solution](../src/main/kotlin/puzzles/Day01.kt)

A very straightforward problem, designed to whet the appetite and get us familiar with the contest environment.

## Part 1
The problem is simple: look for numbers that are larger than the previous number.

There are many ways to do it, and most solutions would probably use some form of loop. If you're on the first iteration, don't check anything; for every other iteration, simply check the number you recorded on the previous iteration and increment your count if the current number is larger than the previous number.

I wanted to try a functional approach (this is going to be a common theme), and I encountered Kotlin's `windowed` function. This takes a list of things and produces a sliding window over its elements, returning "frames". I used this to produce frames of size 2, then counted the frames where the second number was larger than the first number.

```kotlin
lines.windowed(2).count { it[1] > it[0] }
```

## Part 2
A wrench is added: now we aren't comparing just single numbers to each other; now we're taking sums of multiple numbers. We can still use the same approach if we observe one simple fact: if we compare the sum of 3 numbers (say, A, B, and C) with the sum of 3 numbers from the next sliding window (say, B, C, and D), we can see that it is sufficient to simply compare A and D to see if the sum would increase or decrease (B and C being common to both).

The problem is simple to modify when regarded in those terms -- we need only consider windows of size 4, and repeat the exact same procedure.

```kotlin
lines.windowed(4).count { it[3] > it[0] }
```