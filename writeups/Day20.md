# Day 20: Trench Map (a.k.a. Conway's Game of Blinky Death)
[<< Previous](Day19.md) | [Problem Statement](https://adventofcode.com/2021/day/20) | [My solution](../src/main/kotlin/puzzles/Day20.kt) | [Next >>](Day21.md)

Ooooh, you are clever, whoever wrote this problem. My hat is off to you. I'd be curious to see the success rate on this one.

## Part 1
Hey look, it's "custom" Game of Life, easy. Just loop over the image we have, (padding it with 0s), do some binary trickery, build the new (bigger) image, repeat twice. It produces the same two images as in the sample input. It produces the correct final count. Apply it to today's horrible input: Wrong answer! WHAT! Inconceivable!!

I'd bet 20-1 that everybody's input had a `#` in the first slot of their **image enhancement algorithm** and a `.` at the end. Consider what this means for an infinite grid: The infinite field of `.`s around the edge of the original image suddenly turns into an infinite field of `#`s! Not to worry, the last `.` in the algorithm will turn an infinite field of `#`s back into an infinite field of `.`s.

The takeaway here is that we need to be very careful which iteration we're on, and make sure that we pad with `1`/`true`/`#`/whatever instead of `0`/`false`/`.` anytime we're on an odd iteration, and we see that the input is ... evil (which again, I'm pretty sure everyone's was).

## Part 2
Rinse and repeat 50 times instead of 2; again make sure that blink mode is on.