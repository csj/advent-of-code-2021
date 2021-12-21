# Day 21: Dirac Dice
[<< Previous](Day20.md) | [Problem Statement](https://adventofcode.com/2021/day/21) | [My solution](../src/main/kotlin/puzzles/Day21.kt) | [Next >>](Day22.md)

One game, two very different programming challenges. There's essentially nothing reusable between the two parts.

## Part 1
A straight-forward roll-and-move simulation. Because the dice are deterministic, it's just a question of playing out what would occur, track the scores and positions in mutable variables, and return the result.

I chose to do the dice as an iterator so that I could simply `next()` anytime I needed another roll. The actual sequence of rolls is just the numbers 1..100 yielded out forever, as needed. Added bonus: we could track the number of times it was actually rolled! It's important to have some kind of state scoped somewhere inside part 1, because in my machinery, the same instance of the solving _class_ is used.

```kotlin
class Dice {
    var totalRolls = 0

    private fun diceSequence() = sequence {
        totalRolls = 0
        while (true) repeat(100) {
            totalRolls++
            yield(it+1)
        }
    }
    private val diceIterator = diceSequence().iterator()
    fun roll() = diceIterator.next()
}
// ...
val dice = Dice()
dice.roll()  // 1
dice.roll()  // 2, etc.
dice.totalRolls   // 2
```

## Part 2
All right, a completely different problem involving the same(-ish) game. Let's go.

First things first, a question: let's say I had 15 points, sitting on space #8, my opponent had 13 points, sitting on space #1, and it was my turn. Let's say I wanted to know in _how many situations_ from this point would I win, and how many my opponent would win. Does it matter how we got to this point? The answer is no: the answer is always the same.

First observe that if we are asked this question of a state that represents a win for either player, we can immediately return a result: 1 win for that player, 0 wins for the other player.

By recognizing that (1) a game situation has the same _result_ (we/they win pairs) no matter how we get to it, and (2) a game situation can be _expanded_ to other game situations, and its results combined to form the results for this state, we get our solution. 

I'll explain using a simpler example: let's say the dice only get thrown once per player, instead of 3 times. The initial situation gets split 3 ways, once each for rolling a 1, 2, or 3. Let's say we then expand those other states, and find that after rolling a 1 we win 100 times and lose 200 times. After rolling a 2 we win 101 times and lose 201 times, and after rolling a 3 we win 102 times and lose 202 times. We can then produce a result for the original position by simply combining these outcomes: win 303 times, lose 603 times.

When the dice are actually thrown 3 times, it's a little more complicated, but not much. We need only convert this to a "weighted" die that throws different _totals_: it can throw a `3` once (via 111), a `4` 3 times (via 112, 121, 211), etc. 
```kotlin
val rollDist = mapOf(
    3 to 1L, // 111
    4 to 3L, // 112 121 211
    5 to 6L, // 122 212 221 113 131 311
    6 to 7L, // 123*6, 222
    7 to 6L,
    8 to 3L,
    9 to 1L
)
```

Later on, when combining these results together, we need to _multiply_ the results back. We're going to do 7 different sub-states, but we need to make sure our results contain the results of 27 different universes.

By now, you know where I'm going with this -- we need only pull out our old friend `memoize`:

```kotlin
val solve = memoize<GameState, Pair<Long, Long>> { f -> { state ->
    // from this state, how many games will each of the players win?
    if (state.p1Score >= 21) Pair(1L, 0L)
    else if (state.p2Score >= 21) Pair(0L, 1L)
    else {
        rollDist.map { (roll, freq) ->
            val (p1Wins, p2Wins) = if (state.p1ToMove) {
                val newPos = (state.p1Position + roll - 1) % 10 + 1
                state.copy(p1Score = state.p1Score + newPos, p1Position = newPos, p1ToMove = false).let(f)
            } else {
                val newPos = (state.p2Position + roll - 1) % 10 + 1
                state.copy(p2Score = state.p2Score + newPos, p2Position = newPos, p1ToMove = true).let(f)
            }
            Pair(p1Wins * freq, p2Wins * freq)
        }.fold(Pair(0L, 0L)) { (a,b), (c,d) -> Pair(a+c, b+d) }
    }
} }
```

This works because `GameState` is done as a data class, which has value semantics and a nifty copy function out of the box. Thanks, data classes! *ting!*