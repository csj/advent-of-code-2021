# Day 17: Trick Shot
[<< Previous](Day16.md) | [Problem Statement](https://adventofcode.com/2021/day/17) | [My solution](../src/main/kotlin/puzzles/Day17.kt) | [Next >>](Day18.md)

This was a problem that gave me real trouble when trying to apply a clever approach. I junked my first solution, applied good old-fashioned straightforward coding, and got there.

## Part 1
The first part of this problem was quite interesting: how high can we launch and still hit the trench.

First observation: what goes up, must come down. That's obvious; what's slightly less obvious is that the projectile will have the same negative speed when it reaches y=0 than the positive speed it had when it was launched. If we think about what happens in the very next frame, the *maximum* speed the projectile could have is if it landed in the very bottom of the trench on the very next frame. In the example, the bottom of the trench is at y=-10, which means the very top speed it could have when y=0 is 10. The maximum height the projectile reaches is thus 1+2+3+...+9 = 45.

```text
..##......  <-- max height: 1+2 = 3
.#..#.....
..........
S....#....  <-- speed is -3 here
........TT
........TT
......#.TT  <-- bottom of trench (-3)
210123
```

So the answer would be 1 less than the bottom of the trench, triangled:
```kotlin
private fun triangle(n:Int) = n * (n + 1) / 2
// ...
return triangle(-y1-1).toString()
```

## Part 2
The first time, I tried some fancy tech that involved taking differences of triangles. There were so many fussy off-by-one problems, I just abandoned and started over with a more basic approach: I would simply try different launch trajectories and trace where the projectile goes (a very basic physics simulation is described). The only hitch is figuring out what the bounds of my search are.

We learned from part 1 that there's a maximum vertical launch velocity, and what it is (one less than the bottom of the trench). There's also a minimum: if you fire so far downward that it passes the bottom of the trench after one frame, that's too low. So possible values of `ly` (the vertical launch speed) are between `y1` and `-y1 - 1` (e.g. in the example given, between -10 and +9).

The x launch speed can be anywhere between 0 and `x2` (if you launch any faster than `x2` it will surpass the entire trench after the first frame). So simply loop both prospective launch speeds, trace where the projectile goes, count up the successes. If the projectile ends up in the trench at any point, it's a success; if the projectile ends up *past* the trench in either x or y directions, declare it a failure.