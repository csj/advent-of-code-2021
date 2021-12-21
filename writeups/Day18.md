# Day 18: Snailfish 
[<< Previous](Day17.md) | [Problem Statement](https://adventofcode.com/2021/day/18) | [My solution](../src/main/kotlin/puzzles/Day18.kt) | [Next >>](Day19.md)

This problem gave me all kinds of problems. Parsing, recursing, exploding, orders of operations, everything. 

I wound up going full JUnit on it, which was extremely helpful to diagnose which parts were working and which were not. I refused to resort to step-by-step debugging!

The style of the code gradually changed the more I worked. The two kinds of nodes (value and composite) started as simple data classes both implementing an interface. The functions that were built around them, `split`, `explode`, `asString`, `magnitude` etc., all took the form "if the argument is type A, do this; if the argument is type B, do that; otherwise throw an exception". This struck me as, well, *weird*. Why not just embed the behaviours in the respective classes? That's what I wound up doing.

I rediscovered the joy of parameterized unit tests. I avoided JUnit's native way of doing it and built `TestFactory`s instead. In Kotlin I found it more straight-forward to map a set of input-output pairs to `dynamicTest`s. See the solution for details. Having reacquainted myself with JUnit, I'm tempted to rebuild the sample input checking machinery in those terms instead ...

## Part 1
I'm proud to say that I arrived at the solution without making anything mutable: all the operations produce new nodes as appropriate. There are two basic operations: `explode()` and `split()`. 

**Exploding** is the process of building a new node that results after exploding something, anything, inside this one. If no explosion is found, `null` is returned. If an explosion is detected, a `Triple<Node, Int, Int>` is returned: the node thus created, and the values that need to bubble out to the left and right.

If a node itself needs to explode because it's deep enough in the hierarchy, we just return a Value node with a 0, and the left and right values that need to bubble outwards.

If a composite node finds that either of its children *has* exploded, we return a new Node with that new exploded node in the same slot, but with the numeric value bubbled up the other side. 

## Part 2
This is just more of the same; we need to take the inputs in pairs, add them up, take the magnitudes, and find the maximum:
```kotlin
val maxMagnitude = nodes.maxOf { node1 ->
    nodes.filterNot { it == node1 }.maxOf { node2 ->
        (node1 + node2).magnitude()
    }
}
```