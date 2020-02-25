package games.gameOfFifteen

import kotlin.random.Random

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val randomList = (1..15).toList().shuffled().toMutableList()
        while (!isEven(randomList) || (1..15).toList()==randomList) {
            val idx1 = Random.nextInt(randomList.size)
            val idx2 = Random.nextInt(randomList.size)
            val tmp = randomList[idx2]
            randomList[idx2] = randomList[idx1]
            randomList[idx1] = tmp
        }
        randomList
    }
}

