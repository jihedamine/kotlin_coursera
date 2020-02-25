package games.gameOfFifteen

import board.Cell
import board.Direction
import board.Direction.*
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.getAllCells().zip(initializer.initialPermutation) {cell, value -> board[cell] = value }
    }

    override fun canMove() = true

    override fun hasWon() : Boolean = board.getAllCells().map { board[it] } == (1..15) + listOf(null)

        //    15  5  12  2
        //    1  10  7  8
        //    9  4     11
        //    13 14  3 6
        override fun processMove(direction: Direction) {
            fun Cell.swapCell(i: Int, j: Int) {
                board.getCellOrNull(i, j)?.let {
                    board[this] = board[it]
                    board[it] = null
                }
            }

            val emptyCell = board.find { it == null } ?: return
            when(direction) {
                UP -> emptyCell.swapCell(emptyCell.i + 1, emptyCell.j)
                DOWN -> emptyCell.swapCell(emptyCell.i - 1, emptyCell.j)
                RIGHT -> emptyCell.swapCell(emptyCell.i, emptyCell.j - 1)
                LEFT -> emptyCell.swapCell(emptyCell.i, emptyCell.j + 1)
            }

        }


        override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]

}