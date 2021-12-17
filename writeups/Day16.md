# Day 16: Packet Decoder
[<< Previous](Day15.md) | [Problem Statement](https://adventofcode.com/2021/day/16) | [My solution](../src/main/kotlin/puzzles/Day16.kt) | [Next >>](Day17.md)

## Part 1
Man oh man, talk about a lengthy read. The problem statement looks long and nasty, but it's all right if you give it a chance and read the examples carefully.

The nice thing about this problem was that you never had to re-read a bit ever again. That meant that you could consider the input as a "stream" of bits and just eat them as you needed them. The only part that required keeping a count of anything was the case where an Operator packet expresses its children in terms of bit length.

I decided to do packets in two main types: `OpPacket` and `ValuePacket`, each inheriting an abstract base class `Packet`. I decided against using just a single class `Packet` that sometimes held a value and sometimes held sub-packets; this is against my religion and requires invariants to be held in the mind. My way is a bit more verbose, but the benefit is that the type system does a lot of the mental work for me.

Kotlin's built in `takeWhile` function of sequences was *almost* incredibly useful for parsing out value packets, because we want to keep on reading more 5-bit blobs as long as the first bit of a blob is `1`. The problem is we also want to use the first element that would return false (Kotlin's function would just not yield that value). I had to write a little `takeWhileInclusive` that also produces the first element that returns `false` for the provided predicate:

```kotlin
fun <T> Sequence<T>.takeWhileInclusive(pred: (T) -> Boolean) = sequence {
    for (item in this@takeWhileInclusive) {
        yield(item)
        if (!pred(item)) break
    }
}
```

I kind of smelled where part 2 was going to go, so I had my usual `parse` method pre-factored out this time (a bad habit to get into, in general). This function would simply start at 0 and parse the whole input as a single Packet. Part 1 involved walking the tree and adding up all the versions, so inside the part 1 function:
```kotlin
override fun solvePart1(): String {
    fun Packet.sumOfVersions(): Long =
        version + if (this is OpPacket) subPackets.sumOf { it.sumOfVersions() } else 0L
    return parse().sumOfVersions().toString()
}
```

Note that summing versions is a behaviour that is specific to solving part 1, so I didn't make it a part of the `Packet` class itself. I was content to just let it be a value.

## Part 2
Right, just as I thought, each operator type is going to have a specific behaviour. Well, the parsing is already all done; all that remains is to add an `evaluate` function:
```kotlin
override fun solvePart2(): String {
    fun Packet.evaluate(): Long = when (this) {
        is ValuePacket -> value
        is OpPacket -> {
            val subValues = subPackets.map { it.evaluate() }
            when(this.type) {
                0 -> subValues.sum()
                1 -> subValues.fold(1L) { acc, value -> acc * value }
                2 -> subValues.minOrNull()!!
                3 -> subValues.maxOrNull()!!
                5 -> if (subValues[0] > subValues[1]) 1 else 0
                6 -> if (subValues[0] < subValues[1]) 1 else 0
                7 -> if (subValues[0] == subValues[1]) 1 else 0
                else -> throw IllegalArgumentException("Unknown op type ${this.type}")
            }
        }
        else -> throw IllegalArgumentException("Unknown packet type ${this::class.simpleName}")
    }
    return parse().evaluate().toString()
}
```