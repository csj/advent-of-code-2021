# Day 22: Reactor Reboot
[<< Previous](Day21.md) | [Problem Statement](https://adventofcode.com/2021/day/22) | [My solution](../src/main/kotlin/puzzles/Day22.kt) | [Next >>](Day23.md)

All right, here's a problem that we _already know_ what part 2 is going to be. See those big numbers? Yeah, just ignore those. I'm onto you, mister. The question is, solve Part 1 easily and then solve the more general hard problem in Part 2? Or just YOLO it for Part 1 as well?

## Part 1
The first part could have been solved by starting with 100x100x100 array of bools, walking over the inputs, setting the cubes to true/false in order, then counting up the remains.

That is, if we didn't solve it the more general, harder way on the easy inputs. Read on!

## Part 2
Now the actual coordinates get much bigger. Writing individual cells is no longer an option. We need something a little more clever.

First, let's ask ourselves how we might solve this harder problem differently if, say, we observed that _all_ the cuboids described in our input happened to have coordinates that were multiples of 100. We would say pfft, easy: just divide them all by 100 and repeat our solution to step 1. Just remember to multiply everything by 100x100x100 when we count it up again. Why does this work? Because we can _guarantee_ that all cubes inside that cuboid will have the same answer: on or off.

We can do the same thing even if the inputs don't behave. All we need to do is keep track of coordinate values that act as start or end points in the input. Then, we need to walk over all the _ranges_ thus created, find the last cuboid in the input that contains that range, and use that to decide if that box would be _entirely_ on or off.

Example:
```text
...****..        ...+--+..   -- 0
...****..        ...|**|..
...****..        ...+--+..   
.........        .........   -- 3
.**..**..        .++..++..   -- 4
.**..**..        .++..++..   
.........        .........   -- 6

                  | | | |
                  1 3 5 7
```

The coordinates of interest are labeled (we take the start coordinates directly, and the end coordinates + 1, which we will treat as _exclusive_ bounds). For example, the box that includes y coordinates 0..2 causes 0 and 3 to become coordinates of interest. Once we have compiled our lists of interesting coordinates, we simply loop over all the sub-boxes thus formed, and check them against the input.

In this example, we will have 3 x-ranges to check: [1,3), [3,5), and [5,7) (where [x,y) means a range that includes x but excludes y). Likewise, we have 3 y-ranges to check: [0,3), [3,4), and [4,6). So we will have 9 boxes to check in all, and it's sufficient to find the _last_ cuboid that covers it, since it will be the one to determine the final state of that box.

Finally, as we're counting up boxes that should be on, we should contribute the _size_ of that box (since it represents many individual cubes).

This solution worked for me, but it took about **20 minutes** to chug away at the input before finally producing an answer. I went back and tried again with some parallel processing, and came across this nifty tidbit on the interwebs:
```kotlin
private suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}
```
Near as I can tell, this asynchronously applies the function `f` to all elements of a list, converts each one to a promise/task/whatever, `await`s all of them, and finally produces the results of the application of `f` to each element. Perfect: rather than loop through the x-, y- and z-ranges on a single thread, let's instead use this to assign each x-range to a mini-sum, called and executed asynchronously. At the very end, we'll just sum them all together.

```kotlin
val slices = runBlocking(Dispatchers.Default) {
    xStops.windowed(2).pmap { (x, xe) ->
        var countOn = 0L
        for ((y, ye) in yStops.windowed(2)) {
            for ((z, ze) in zStops.windowed(2)) {
                val isOn = cuboids.firstOrNull { it.xr.contains(x) && it.yr.contains(y) && it.zr.contains(z) }?.isOn ?: false
                if (isOn) countOn += 1L * (xe - x) * (ye - y) * (ze - z)
            }
        }
        countOn
    }
}
return slices.sum().toString()
```

Note here again we're using `windowed(2)` to produce convenient sliding windows corresponding to our start-end coordinate pairs.

This version completed in about **2.5 minutes** and produced the same correct answer.