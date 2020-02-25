package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

open class SquareBoardImpl(final override val width: Int) : SquareBoard {
    private val cells: Array<Array<Cell>> = Array(width) { i -> Array(width) { j -> Cell(i + 1, j + 1) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if ((i !in 1..width) || (j !in 1..width)) null else cells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        fun Int.requireIndexWithinRange() = require((i in 1..width)) { "$this should be between 1 and $width" }
        i.requireIndexWithinRange()
        j.requireIndexWithinRange()
        return cells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> = cells.flatMap { it.toList() }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val result = ArrayList<Cell>()
        for (j in jRange.getNormalizedRange()) {
            result.add(cells[i.getWithinRangeIndex()][j])
        }

        return result
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val result = ArrayList<Cell>()
        for (i in iRange.getNormalizedRange()) {
            result.add(cells[i][j.getWithinRangeIndex()])
        }
        return result
    }

    private fun IntProgression.getNormalizedRange() = IntProgression.fromClosedRange(first.getWithinRangeIndex(), last.getWithinRangeIndex(), step)
    private fun Int.getWithinRangeIndex() = if (this < 0) 0 else if (this > width) width - 1 else this - 1

    override fun Cell.getNeighbour(direction: Direction) =
            when (direction) {
                UP -> getCellOrNull(i - 1, j)
                DOWN -> getCellOrNull(i + 1, j)
                RIGHT -> getCellOrNull(i, j + 1)
                LEFT -> getCellOrNull(i, j - 1)
            }

}

class GameBoardImpl<T>(boardWidth: Int) : GameBoard<T>, SquareBoardImpl(boardWidth) {
    private val cellsMap: MutableMap<Cell, T?> = HashMap()
    init {
        getAllCells().forEach { cellsMap[it] = null }
    }

    override fun get(cell: Cell) = cellsMap[cell]

    override fun set(cell: Cell, value: T?) {
        cellsMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
            cellsMap.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? {
        for ((key, value) in cellsMap) {
            if (predicate(value)) return key
        }
        return null
    }

    override fun any(predicate: (T?) -> Boolean) = cellsMap.any { (_, value) -> predicate(value) }

    override fun all(predicate: (T?) -> Boolean): Boolean = cellsMap.all { (_, value) -> predicate(value) }

}