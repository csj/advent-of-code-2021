# Day 8: Seven Segment Search
[<< Previous](Day07.md) | [Problem Statement](https://adventofcode.com/2021/day/8) | [My solution](../src/main/kotlin/puzzles/Day08.kt) | [Next >>](Day09.md)

Woah, this looks complicated.

## Part 1
Well this is pretty simple, just count up the tokens in all lines that appear after the `|` that contain 2, 3, 4, or 7 characters.

## Part 2
Now the tricky part. In order to figure out which panel is actually represented by each letter, we can first study the digit diagrams and note how many times each panel is used over the 10 digits. Then we could count up how many instances of the letters a-g appear in the input digits and go from there.

We have a:8, b:6, c:8, d:7, e:4, f:9, g:7. Ahh, still ambiguous. How are supposed to tell, if a letter appears 8 times, whether it's supposed to represent an "a" or a "c"? Or if a letter appears 7 times, it might equally well represent a "d" or a "g". However -- we can use an insight drawn from Part 1: we can tell unambiguously which of the input digits represents 4, because it contains 4 panels. This gives us the letters used in the 4, and our problems are solved: the "a" is the 8-appearing letter that *doesn't* appear in the 4, while the "c" is the 8-appearing letter that *does* appear in the 4. Likewise, the "d" appears in the 4 while the "g" doesn't.

Armed with the correct mapping, we can build the output digits, mash them together with some `* 10` folding technology, and we're there!