# Day 19: Beacon Scanner (a.k.a. Bacon Spammer)
[<< Previous](Day18.md) | [Problem Statement](https://adventofcode.com/2021/day/19) | [My solution](../src/main/kotlin/puzzles/Day19.kt) | [Next >>](Day20.md)

This is the first problem that I couldn't just dive into coding, I had to do some stuff with ... (shield your eyes, children) ... *paper*.

I did some research to figure out what quaternions were, once and for all. Then I figured out a way to get by which didn't need them.

## Part 1
The first problem is working out how to rotate a scanner and produce sensible mappings of beacon locations. For this, I stared at a Rubik's cube, spun it around at different angles, and tracked where the red, white and blue sides faced. I emerged with this list:

```kotlin
val rotationMatrices = listOf(
    "xyz", "yzx", "zxy",
    "zyX", "xzY", "yxZ",
    "xYZ", "yZX", "zXY",
    "zYx", "xZy", "yXz",
    "ZXy", "XYz", "YZx",
    "YXZ", "ZYX", "XZY",
    "ZxY", "XyZ", "YzX",
    "Xzy", "Yxz", "Zyx",
)
```

The second problem is figuring out how to rotate a bunch of points that are *themselves* rotated. Oh man, we need to figure out for 24x24 different pairs of rotations, what the resulting rotation is. Uhh, quaternions. Right? Wrong.

We start, as the problem description does, by declaring that scanner 0 is the One True Orientation (OTO). Everything else will be transformed to this frame of reference. Then, because we know that all the scanners intersect eventually in a single big ol gob, we can begin a convoluted "search" over the scanners to eventually touch all the scanners. Two scanners are considered adjacent if their detected beacon sets overlap.

The scanner we are searching *from* is already corrected to the OTO (it is seeded to contain the first scanner only). Then, as we discover more scanners to consider as "expanded to", we correct them as we go (i.e. we rotate and translate them into the OTO). The double-rotation problem is thus eliminated: every new scanner is brought into the fold by comparing it to *corrected* beacon points. We continue until all the scanners are eaten, then we count up all the distinct corrected beacon points for our answer.

To detect whether two scanners overlap, we must consider each of 24 different rotations for the "new" scanner. With a prospective rotation in mind, it's just a simple loop over each possible pairing of beacons. We assume these two coincide, and then count up how many other beacons would also coincide with this vector difference in mind. If this is at least 12, we store the "corrected" beacons in our OTO list as described above.

## Part 2
This part just involved recording the locations of the *scanners* as we found new ones, by recording the successful difference between points observed in the overlap detection process. At the end of the search, we loop over the pairs of scanners and look for the biggest Manhattan-distance between them:

```kotlin
operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)
fun manhattanDistance() = abs(x) + abs(y) + abs(z)
// ...

locations.maxOf { a ->
    locations.maxOf { b ->
        (a-b).manhattanDistance()
    }
}
```